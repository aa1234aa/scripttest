package com.bitnei.cloud.fault.enums;

/**
 * 响应方式枚举类
 */
public enum ResponseModeEnum {

    NONE("0", "无"), SYSTEM("1", "系统弹窗"), SOUND("2", "声音提醒"), APP("3", "APP弹窗提醒"), MESSAGE("4", "短信通知");

    /**
     * 枚举值
     */
    private String value;

    /**
     * 值描述
     */
    private String desc;

    ResponseModeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述获取对应枚举值
     *
     * @param desc
     * @return
     */
    public static String getValueByDesc(String desc) {
        for (ResponseModeEnum em : ResponseModeEnum.values()) {
            if (em.getDesc().equals(desc)) {
                return em.getValue();
            }
        }
        return null;
    }

    /**
     * 通过枚举值获取对应描述
     *
     * @param value
     * @return
     */
    public static String getDescByValue(String value) {
        for (ResponseModeEnum em : ResponseModeEnum.values()) {
            if (em.getValue().equals(value)) {
                return em.getDesc();
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
