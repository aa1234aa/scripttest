package com.bitnei.cloud.fault.enums;

/**
 * 解析方式枚举类
 */
public enum EnabledStatusEnum {

    ENABLED(1, "启用"), DISABLED(0, "禁用");

    /**
     * 枚举值
     */
    private int value;

    /**
     * 值描述
     */
    private String desc;

    EnabledStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述获取对应枚举值
     *
     * @param desc
     * @return
     */
    public static Integer getValueByDesc(String desc) {
        for (EnabledStatusEnum em : EnabledStatusEnum.values()) {
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
    public static String getDescByValue(int value) {
        for (EnabledStatusEnum em : EnabledStatusEnum.values()) {
            if (em.getValue() == value) {
                return em.getDesc();
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
