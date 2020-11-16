package com.bitnei.cloud.sys.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xzp
 */
public interface IOfflineExportService {

    /**
     * 创建离线导出任务
     *
     * @param exportFilePrefixName 导出文件名称前缀
     * @param exportServiceName    导出服务名称
     * @param exportMethodName     导出方法名称
     * @param exportMethodParams   导出方法参数
     * @return 任务标识
     * @see com.bitnei.cloud.common.handler.IOfflineExportCallback#exportOfflineProcessor
     */
    @SuppressWarnings("UnusedReturnValue")
    @Transactional(rollbackFor = Exception.class)
    @NotNull
    String createTask(
        @NotNull final String exportFilePrefixName,
        @NotNull final String exportServiceName,
        @NotNull final String exportMethodName,
        @NotNull final String exportMethodParams);
}
