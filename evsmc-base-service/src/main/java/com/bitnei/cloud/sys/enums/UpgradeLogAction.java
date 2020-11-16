package com.bitnei.cloud.sys.enums;

public enum UpgradeLogAction {

    ADD_PACK(10, "新增升级包"),

    EDIT_PACK(20, "编辑升级包"),

    DEL_PACK(30, "删除升级包"),

    CREATE_TASK(40, "创建任务"),

    DEL_TASK(50, "删除任务"),

    START_TASK(60, "开始任务"),

    STOP_TASK(80, "终止车辆升级"),

    STOP_VEH_TASK(90, "强制终止车辆升级"),

    EDIT_PASSWORD(1000, "重置操作密码");

    private int value;

    private String desc;

    UpgradeLogAction(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
