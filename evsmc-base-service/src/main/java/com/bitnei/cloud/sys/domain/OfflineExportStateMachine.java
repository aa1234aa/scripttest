package com.bitnei.cloud.sys.domain;

/**
 * @author xuzhipeng
 */
public final class OfflineExportStateMachine {

    /**
     * 已创建
     */
    public static final int CREATED = 1;

    /**
     * 导出中
     */
    public static final int EXPORTING = 2;

    /**
     * 已完成
     */
    public static final int FINISH = 3;

    /**
     * 已取消
     */
    public static final int CANCELED = 4;

    /**
     * 有异常
     */
    public static final int EXCEPTED = 5;
}
