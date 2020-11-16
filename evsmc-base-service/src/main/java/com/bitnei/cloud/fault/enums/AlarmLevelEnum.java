package com.bitnei.cloud.fault.enums;

/**
 * 故障类型枚举类
 */
public enum AlarmLevelEnum {

    NONE(0, "无"), LEVEL_1(1, "一级"), LEVEL_2(2, "二级"), LEVEL_3(3, "三级"), LEVEL_4(4, "四级");

    /**
     * 枚举值
     */
    private int value;

    /**
     * 值描述
     */
    private String desc;

    AlarmLevelEnum(int value, String desc) {
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
        for (AlarmLevelEnum em : AlarmLevelEnum.values()) {
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
        for (AlarmLevelEnum em : AlarmLevelEnum.values()) {
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
