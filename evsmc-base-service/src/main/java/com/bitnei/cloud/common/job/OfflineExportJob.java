package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.Exception.CancelRequestException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.OfflineExportCancel;
import com.bitnei.cloud.common.handler.OfflineExportHandler;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.sys.dao.OfflineExportMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import jodd.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

/**
 * @author xzp
 */
@ElasticJobConf(name = "OfflineExportJob", shardingTotalCount = 12, cron = "*/3 * * * * ?",
        description = "离线导出作业", eventTraceRdbDataSource = "druidDataSource")
@Slf4j
@Component
@RequiredArgsConstructor
public class OfflineExportJob implements SimpleJob {

    @Value("${" + OfflineExportHandler.EXPORT_DIRECTORY_CONFIG_KEY + "}")
    private String exportDirectory;

    @Autowired
    private OfflineExportMapper mapper;

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    private final Environment env;

    /**
     * 执行作业.
     * 本次分片执行完成之前, 不会再次触发, 可以简单认为分片内不会因为 cron 造成重复执行.
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(final ShardingContext shardingContext) {

        if (!JobCheckUtil.getJobEnableStatus(getClass())) {
            return;
        }

        final String property = Optional.ofNullable(exportDirectory).orElse(".");
        final File directory = new File(property).getAbsoluteFile();
        if (!directory.exists()) {
            log.warn("离线导出目录[{}]不存在, 请创建并挂载到nfs.", directory.getPath());
            return;
        }

        // 分片总数
        final int shardingTotalCount = shardingContext.getShardingTotalCount();
        // 分配于本作业实例的分片项
        final int shardingItem = shardingContext.getShardingItem();

        log.trace("离线导出任务[{}-{}]执行开始", shardingTotalCount, shardingItem);
        final BigInteger[] count = new BigInteger[]{BigInteger.ZERO};
        mapper.selectSharding(
                shardingTotalCount,
                shardingItem,
                buildOfflineExportResultHandler(shardingTotalCount, shardingItem, count),
                env.getProperty("spring.application.name", "evsmc-base-service")
        );
        log.trace("离线导出任务[{}-{}]执行完毕, 当前批次共{}个任务", shardingTotalCount, shardingItem, count[0]);

    }

    @NotNull
    private ResultHandler<OfflineExport> buildOfflineExportResultHandler(final int shardingTotalCount, final int shardingItem, final BigInteger[] outCount) {
        return (final ResultContext<? extends OfflineExport> resultContext) -> {
            final OfflineExport task = resultContext.getResultObject();
            try {

                log.trace("离线导出任务[{}-{}-{}]执行开始", shardingTotalCount, shardingItem, task.getId());
                processTask(task);
                log.trace("离线导出任务[{}-{}-{}]执行完毕", shardingTotalCount, shardingItem, task.getId());

                final OfflineExport entity = new OfflineExport();
                entity.setId(task.getId());
                entity.setExportNote(DateUtil.getNow());
                entity.setStateMachine(OfflineExportStateMachine.FINISH);
                this.update(entity);

                OfflineExportProgress.pushProgress(
                        ws,
                        task.getId(),
                        task.getCreateBy(),
                        "已完成",
                        OfflineExportStateMachine.FINISH,
                        100L,
                        "离线导出任务执行完毕"
                );

            } catch (final CancelRequestException e) {

                OfflineExportCancel.finishRequest(
                        redis,
                        task.getCreateBy(),
                        task.getExportFileName()
                );

                log.warn("离线导出任务[{}-{}-{}]执行取消", shardingTotalCount, shardingItem, task.getId());

                OfflineExportProgress.pushProgress(
                        ws,
                        task.getId(),
                        task.getCreateBy(),
                        "已取消",
                        OfflineExportStateMachine.CANCELED,
                        0L,
                        "离线导出任务执行取消"
                );
            } catch (Throwable e) {

                if (e instanceof InvocationTargetException) {
                    e = ((InvocationTargetException) e).getTargetException();
                }
                log.warn("离线导出任务[{}-{}-{}]执行异常", shardingTotalCount, shardingItem, task.getId(), e);

                final OfflineExport entity = new OfflineExport();
                entity.setId(task.getId());
                entity.setExportNote(ExceptionUtils.getStackTrace(e));
                entity.setStateMachine(OfflineExportStateMachine.EXCEPTED);
                this.update(entity);

                OfflineExportProgress.pushProgress(
                        ws,
                        task.getId(),
                        task.getCreateBy(),
                        "有异常",
                        OfflineExportStateMachine.EXCEPTED,
                        0L,
                        "离线导出任务执行异常"
                );
            }

            OfflineExportProgress.deleteProgress(
                    redis,
                    task.getCreateBy(),
                    task.getExportFileName()
            );

            outCount[0] = outCount[0].add(BigInteger.ONE);
        };
    }

    private void processTask(final OfflineExport task) throws Exception {
        final String exportServiceName = task.getExportServiceName();
        final Class<?> exportServiceClass = Class.forName(exportServiceName);
        final Object exportService = ApplicationContextProvider.getBean(exportServiceClass);
        final String exportMethodName = task.getExportMethodName();

        final String taskId = task.getId();
        final String createBy = task.getCreateBy();
        final Date createTime = DateUtil.strToDate_ex_full(task.getCreateTime());
        final String exportFileName = task.getExportFileName();
        final String exportMethodParams = task.getExportMethodParams();

        if (exportService instanceof IOfflineExportCallback) {
            ((IOfflineExportCallback) exportService).exportOfflineProcessor(
                    taskId,
                    createBy,
                    createTime,
                    exportFileName,
                    exportMethodParams
            );
        } else {
            ReflectUtil.invoke(
                    exportService,
                    exportMethodName,
                    taskId,
                    createBy,
                    createTime,
                    exportFileName,
                    exportMethodParams
            );
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private int update(@NotNull final OfflineExport entity) {

        // todo xzp: 使用 AOP 实现

        entity.setUpdateBy(ServletUtil.getCurrentUser());
        entity.setUpdateTime(DateUtil.getNow());
        return mapper.update(entity);
    }

}
