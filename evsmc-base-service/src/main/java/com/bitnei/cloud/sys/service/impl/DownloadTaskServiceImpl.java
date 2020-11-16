package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.OfflineExportCancel;
import com.bitnei.cloud.common.handler.OfflineExportHandler;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.PageRowBoundsUtil;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.orm.bean.Sort;
import com.bitnei.cloud.sys.dao.OfflineExportMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.model.DownloadTaskModel;
import com.bitnei.cloud.sys.service.DownloadTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xzp
 */
@Slf4j
@Service
public class DownloadTaskServiceImpl implements DownloadTaskService {

    @Value("${" + OfflineExportHandler.EXPORT_DIRECTORY_CONFIG_KEY + "}")
    private String exportDirectory;

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private OfflineExportMapper mapper;

    @Override
    public PagerResult list(@NotNull final PagerInfo pagerInfo) {

        final Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);

        final String userName = ServletUtil.getCurrentUser();
        params.put(OfflineExport.Fields.createBy, userName);

        // 获取当前用户权限
        final String authSQL = DataAccessKit.getAuthSQL(OfflineExport.TABLE, OfflineExport.TABLE_ALIAS);
        params.put(Constants.AUTH_SQL, authSQL);

        params.put(
            "sorts",
            ImmutableList.of(
                new Sort(OfflineExport.Fields.createTime, Sort.DESC),
                new Sort(OfflineExport.Fields.updateTime, Sort.DESC)
            )
        );

        final PageRowBounds pageRowBounds = PageRowBoundsUtil.fromPagerInfo(pagerInfo);

        final Page<OfflineExport> page = mapper.select(
            params,
            pageRowBounds
        );

        final PagerResult pagerResult = new PagerResult();

        pagerResult.setData(page.getResult().stream()
            .map((OfflineExport entity) ->
                DownloadTaskModel.fromEntry(
                    entity,
                    OfflineExportProgress.getProgress(
                        redis,
                        entity.getCreateBy(),
                        entity.getExportFileName()
                    )
                )
            )
            .collect(Collectors.toList())
        );
        pagerResult.setTotal(page.getTotal());

        return pagerResult;
    }

    @Override
    public int delete(@NotNull final String... ids) {

        final String userName = ServletUtil.getCurrentUser();

        final int[] count = new int[1];
        if (ids.length > 0) {

            for (final String id : ids) {

                if (StringUtils.isBlank(id)) {
                    continue;
                }

                final OfflineExport condition = new OfflineExport();
                condition.setId(id);
                condition.setCreateBy(userName);
                mapper.select(
                    condition,
                    (@NotNull final ResultContext<? extends OfflineExport> context) -> {
                        final OfflineExport entity = context.getResultObject();
                        this.deleteFile(entity);
                        count[0] = count[0] + mapper.delete(entity);
                    });
            }
        } else {
            final OfflineExport condition = new OfflineExport();
            condition.setCreateBy(userName);
            mapper.select(
                condition,
                (@NotNull final ResultContext<? extends OfflineExport> context) -> {
                    final OfflineExport entity = context.getResultObject();
                    this.deleteFile(entity);
                });
            count[0] = mapper.delete(condition);
        }

        return count[0];
    }

    @Override
    public void retry(@NotNull String... ids) {

        if (!ArrayUtils.isNotEmpty(ids)) {
            return;
        }

        mapper.retryExceptionalTask(
            ImmutableSet.copyOf(
                Stream.of(ids)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet())
            )
        );
    }

    @Override
    public void cancel(@NotNull String... ids) {

        if (!ArrayUtils.isNotEmpty(ids)) {
            return;
        }

        final ImmutableSet<String> idSet = ImmutableSet.copyOf(
            Stream.of(ids)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet())
        );

        final List<OfflineExport> entities = mapper.findByIds(idSet);
        for (OfflineExport entity : entities) {
            OfflineExportCancel.createRequest(
                redis,
                entity.getCreateBy(),
                entity.getExportFileName()
            );
        }

        mapper.cancelExportingTask(idSet);
    }

    private void deleteFile(@NotNull final OfflineExport entity) {

        if (OfflineExportStateMachine.CREATED == entity.getStateMachine()
            || OfflineExportStateMachine.EXPORTING == entity.getStateMachine()) {
            OfflineExportCancel.createRequest(
                redis,
                entity.getCreateBy(),
                entity.getExportFileName()
            );
        }

        final String property = Optional.ofNullable(exportDirectory).orElse(".");
        final File file = Paths.get(property, entity.getCreateBy(), entity.getExportFileName()).toAbsolutePath().toFile();
        if (file.exists()) {
            if (file.delete()) {
                log.warn("离线导出文件[{}/{}]删除失败", entity.getCreateBy(), entity.getExportFileName());
            }
        }
    }
}
