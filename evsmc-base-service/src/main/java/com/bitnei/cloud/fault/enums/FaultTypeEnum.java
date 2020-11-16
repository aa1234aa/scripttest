package com.bitnei.cloud.fault.enums;

/**
 * 故障类型枚举类
 */
public enum FaultTypeEnum {

    PARAMETER(1, "参数"), CODE(2, "故障码"), ENCLOSURE(3, "围栏"), SECURITY_RISK(4, "安全风险等级");

    /**
     * 枚举值
     */
    private int value;

    /**
     * 值描述
     */
    private String desc;

    FaultTypeEnum(int value, String desc) {
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
        for (FaultTypeEnum em : FaultTypeEnum.values()) {
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
        for (FaultTypeEnum em : FaultTypeEnum.values()) {
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
