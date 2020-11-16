package com.bitnei.cloud.fault.enums;

/**
 * 解析方式枚举类
 */
public enum AnalyzeTypeEnum {

    BYTE(1, "按字节解析"), BIT(2, "按bit位解析");

    /**
     * 枚举值
     */
    private int value;

    /**
     * 值描述
     */
    private String desc;

    AnalyzeTypeEnum(int value, String desc) {
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
        for (AnalyzeTypeEnum em : AnalyzeTypeEnum.values()) {
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
        for (AnalyzeTypeEnum em : AnalyzeTypeEnum.values()) {
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
