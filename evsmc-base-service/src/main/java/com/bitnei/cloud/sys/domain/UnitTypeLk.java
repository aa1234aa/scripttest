package com.bitnei.cloud.sys.domain;

/**
 * @author DFY
 * @description 单位类型-单位关联表
 * @date 2018/11/6
 */
public class UnitTypeLk {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 单位类型id
     */
    private String unitTypeId;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
