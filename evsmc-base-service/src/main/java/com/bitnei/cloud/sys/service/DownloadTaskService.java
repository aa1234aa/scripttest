package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;

import javax.validation.constraints.NotNull;

/**
 * @author xzp
 */
public interface DownloadTaskService {

    /**
     * 任务列表 -> (任务标识, 导出文件名称, 开始时间, 任务状态)
     *
     * @param pagerInfo 分页参数
     * @return 返回所有
     */
    @NotNull
    PagerResult list(@NotNull final PagerInfo pagerInfo);

    /**
     * 删除任务 -> 任务状态进行中，将任务停止并从列表内删除
     * 清空任务 -> 将列表内所有任务停止，并删除任务及任务文件
     *
     * @param ids 任务标识集合
     * @return 影响行数
     */
    int delete(@NotNull final String... ids);

    /**
     * 重试有异常的导出
     *
     * @param ids 任务标识集合
     */
    void retry(@NotNull final String... ids);

    /**
     * 取消导出任务
     *
     * @param ids 任务标识集合
     */
    void cancel(@NotNull final String... ids);
}
