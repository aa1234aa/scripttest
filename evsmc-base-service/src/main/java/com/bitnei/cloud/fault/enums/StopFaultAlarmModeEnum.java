package com.bitnei.cloud.fault.enums;

/**
 * 结束报警方式
 */
public enum StopFaultAlarmModeEnum {

    /**
     * 自动结束(状态=已结束)
     */
    AUTO_STOP(0),

    /**
     * 处理结束(状态=已结束 && 处理状态=已处理)
     */
    PROCESS_STOP(1);

    private int value;

    StopFaultAlarmModeEnum(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }

}
