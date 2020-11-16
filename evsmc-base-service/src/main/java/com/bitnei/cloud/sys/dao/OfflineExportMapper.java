package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.google.common.collect.ImmutableSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author xzp
 */
@Mapper
public interface OfflineExportMapper extends IMapper {

    /**
     * 重复导出服务计数
     *
     * @param createBy          导出任务创建用户
     * @param exportServiceName 导出服务CanonicalName
     * @return 符合条件的记录数量
     */
    int countDuplicateTask(
        @Param(value = "create_by") @NotNull final String createBy,
        @Param(value = "export_service_name") @NotNull final String exportServiceName,
        @Param(value = "systemName") @NotNull final String systemName);

    /**
     * 查询分片任务, 分片单位是任务创建时间的秒部分[0, 60)
     *
     * @param shardingTotalCount 分片总数
     * @param shardingItem       当前分片
     * @param handler            处理分片任务
     */
    void selectSharding(
        @Param("shardingTotalCount") final int shardingTotalCount,
        @Param("shardingItem") final int shardingItem,
        @NotNull final ResultHandler<OfflineExport> handler,
        @Param("systemName") final String systemName);

    /**
     * 通过ID查询
     *
     * @param ids 待更新的任务标识
     * @return 离线导出任务
     */
    List<OfflineExport> findByIds(@Param("ids") @NotNull final ImmutableSet<String> ids);

    /**
     * 重试导出异常或取消的任务
     *
     * @param ids 待更新的任务标识
     */
    void retryExceptionalTask(@Param("ids") @NotNull final ImmutableSet<String> ids);

    /**
     * 取消导出中的任务
     *
     * @param ids 待更新的任务标识
     */
    void cancelExportingTask(@Param("ids") @NotNull final ImmutableSet<String> ids);
}
