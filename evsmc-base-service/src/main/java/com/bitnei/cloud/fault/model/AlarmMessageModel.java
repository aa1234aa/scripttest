package com.bitnei.cloud.fault.model;

import lombok.Data;

/**
 * @Desc： 故障消息
 * @Author: joinly
 * @Date: 2019/6/1
 */

@Data
public class AlarmMessageModel {

    /**故障规则名称**/
    private String ruleName;

    /**故障类型**/
    private Integer faultType;

    /**故障类型名称**/
    private String faultTypeName;

    /**故障等级名称**/
    private String alarmLevelName;

    /**故障等级**/
    private Integer alarmLevel;

    /**响应方式**/
    private String responseMode;

    /**车辆vin**/
    private String vin;

    /**车辆id**/
    private String vehicleId;

    /**故障id**/
    private String id;

    public AlarmMessageModel(String ruleName, Integer faultType, String faultTypeName, String alarmLevelName,
                             Integer alarmLevel, String responseMode, String vin, String vehicleId, String id) {
        this.ruleName = ruleName;
        this.faultType = faultType;
        this.faultTypeName = faultTypeName;
        this.alarmLevelName = alarmLevelName;
        this.alarmLevel = alarmLevel;
        this.responseMode = responseMode;
        this.vin = vin;
        this.vehicleId = vehicleId;
        this.id = id;
    }
}
