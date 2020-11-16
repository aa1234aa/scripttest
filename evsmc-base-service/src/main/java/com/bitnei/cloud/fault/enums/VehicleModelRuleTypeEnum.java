package com.bitnei.cloud.fault.enums;

import lombok.Getter;

/**
 * 车辆型号通用报警规则类型
 *
 * @author xuzhijie
 */

public enum VehicleModelRuleTypeEnum {

    /**
     * 温度差异报警值
     */
    TEMPERATURE_DIFFERENCE_ALARM(
        "2609",
        "最高温度值",
        true,
        "2612",
        "最低温度值",
        true,
        "TEMPERATURE_DIFFERENCE_ALARM",
        "温度差异报警"),

    /**
     * 电池高温报警值
     */
    BATTERY_HIGH_TEMPERATUR_ALARM(
        "2609",
        "最高温度值",
        true,
        null,
        null,
        true,
        "BATTERY_HIGH_TEMPERATUR_ALARM",
        "电池高温报警"),

    /**
     * 车载储能装置类型过压报警值
     */
    ENERGY_DEVICE_OVERVOLTAGE_ALARM(
        "2613",
        "总电压",
        true,
        null,
        null,
        true,
        "ENERGY_DEVICE_OVERVOLTAGE_ALARM",
        "车载储能装置类型过压报警"),

    /**
     * 车载储能装置类型欠压报警值
     */
    ENERGY_DEVICE_UNDERVOLTAGE_ALARM(
        "2613",
        "总电压",
        true,
        null,
        null,
        true,
        "ENERGY_DEVICE_UNDERVOLTAGE_ALARM",
        "车载储能装置类型欠压报警"),

    /**
     * 驱动电机控制器温度报警值
     */
    DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM(
        "2302",
        "驱动电机控制器温度",
        true,
        null,
        null,
        true,
        "DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM",
        "驱动电机控制器温度报警"),

    /**
     * 驱动电机温度报警值
     */
    DRIVE_MOTOR_TEMPERATURE_ALARM(
        "2304",
        "驱动电机温度",
        true,
        null,
        null,
        true,
        "DRIVE_MOTOR_TEMPERATURE_ALARM",
        "驱动电机温度报警"),

    /**
     * 单体电池过压报警值
     */
    SINGLE_BATTERY_OVERVOLTAGE_ALARM(
        "2603",
        "电池单体电压最高值",
        true,
        null,
        null,
        true,
        "SINGLE_BATTERY_OVERVOLTAGE_ALARM",
        "单体电池过压报警"),

    /**
     * 单体电池欠压报警值
     */
    SINGLE_BATTERY_UNDERVOLTAGE_ALARM(
        "2606",
        "电池单体电压最低值",
        true,
        null,
        null,
        true,
        "SINGLE_BATTERY_UNDERVOLTAGE_ALARM",
        "单体电池欠压报警"),

    /**
     * SOC低报警值
     */
    SOC_LOW_ALARM(
        "7615",
        "SOC",
        true,
        null,
        null,
        true,
        "SOC_LOW_ALARM",
        "SOC低报警"),

    /**
     * SOC跳变报警值
     */
    SOC_JUMP_ALARM(
        "7615",
        "本帧SOC",
        true,
        "7615",
        "上一帧SOC",
        false,
        "SOC_JUMP_ALARM",
        "SOC跳变报警"),

    /**
     * SOC过高报警值
     */
    SOC_HIGH_ALARM(
        "7615",
        "SOC",
        true,
        null,
        null,
        true,
        "SOC_HIGH_ALARM",
        "SOC过高报警"),

    /**
     * 电池单体一致性差报警值
     */
    BATTERY_CONSISTENCY_ALARM(
        "2603",
        "电池单体电压最高值",
        true,
        "2606",
        "电池单体电压最低值",
        true,
        "BATTERY_CONSISTENCY_ALARM",
        "电池单体一致性差报警"),

    /**
     * 车载储能装置过充报警值
     */
    VEHICULAR_DEVICE_OVERCHARGE_ALARM(
        "2613",
        "总电压",
        true,
        null,
        null,
        true,
        "VEHICULAR_DEVICE_OVERCHARGE_ALARM",
        "车载储能装置过充报警"),

    /**
     * 绝缘故障报警值
     */
    INSULATION_FAULT_ALARM(
        "2617",
        "绝缘电阻",
        true,
        null,
        null,
        true,
        "INSULATION_FAULT_ALARM",
        "绝缘故障报警");

    /**
     * 内部协议号 - 上限参数
     */
    private String max;
    /**
     * 内部协议号 - 上限参数描述
     */
    @Getter
    private String maxDesc;

    /**
     * true 取当前帧， false取上一帧
     */
    private boolean maxCurrentFrame;

    /**
     * 内部协议号 - 下限参数
     */
    private String min;
    /**
     * 内部协议号 - 下限参数描述
     */
    @Getter
    private String minDesc;

    /**
     * true 取当前帧， false取上一帧
     */
    private boolean minCurrentFrame;

    /**
     * 故障类型描述
     */
    @Getter
    private String groupName;

    /**
     * 故障类型名称
     */
    @Getter
    private String ruleName;

    VehicleModelRuleTypeEnum(
        String max,
        String maxDesc,
        boolean maxCurrentFrame,
        String min,
        String minDesc,
        boolean minCurrentFrame,
        String groupName,
        String ruleName) {

        this.max = max;
        this.maxDesc = maxDesc;
        this.maxCurrentFrame = maxCurrentFrame;
        this.min = min;
        this.minDesc = minDesc;
        this.minCurrentFrame = minCurrentFrame;
        this.groupName = groupName;
        this.ruleName = ruleName;
    }

    /**
     * 表达式参数
     * d开头表示使用当前帧数据参与计算
     * c开头表示使用上一帧数据参与计算
     *
     * @return
     */
    public String getMaxParam() {
        if( this.max == null ){
            return null;
        }
        if (this.maxCurrentFrame) {
            return "d" + this.max;
        }
        return "c" + this.max;
    }

    public String getMinParam() {
        if( this.min == null ){
            return null;
        }
        if (this.minCurrentFrame) {
            return "d" + this.min;
        }
        return "c" + this.min;
    }

}
