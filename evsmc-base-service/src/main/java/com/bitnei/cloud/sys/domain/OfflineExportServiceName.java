package com.bitnei.cloud.sys.domain;

/**
 * @author xzp
 */
public final class OfflineExportServiceName {

    /**
     * 监控中心 -> 远程车辆监控 -> 车辆实时状态
     */
    public static final String VehicleRealStatus = "VehicleRealStatus";

    /**
     * 监控中心 -> 异常车辆 -> 无CAN车辆历史记录
     */
    public static final String VehNotCan = "VehNotCan";

    /**
     * 监控中心 -> 异常车辆 -> 定位异常车辆历史记录
     */
    public static final String VehNotPosition = "VehNotPosition";

    /**
     * 监控中心 -> 异常车辆 -> 长期离线车辆历史记录
     */
    public static final String VehIdleRecord = "VehIdleRecord";

    /**
     * 监控中心 -> 异常车辆 -> SOC过低车辆历史记录
     */
    public static final String SocVehicleLog = "SocVehicleLog";

    /**
     * 统计分析 -> 运营分析 -> 车辆运行统计 -> 车辆闲置情况统计
     */
    public static final String IdleVehicle = "IdleVehicle";

    /**
     * 统计分析 -> 工况分析 -> 车辆历史状态报表
     */
    public static final String HistoryState = "HistoryState";

    /**
     * 故障报警 -> 历史报警提醒
     */
    public static final String AlarmInfoHistory = "AlarmInfoHistory";
}
