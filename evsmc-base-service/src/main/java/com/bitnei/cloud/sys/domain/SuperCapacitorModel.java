package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import java.lang.String;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SuperCapacitorModel实体<br>
* 描述： SuperCapacitorModel实体<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2018-12-06 23:38:00</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
public class SuperCapacitorModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 型号名称 **/
    private String name;
    /** 超级电容生产企业 **/
    private String unitId;
    /** 额定电压(V) **/
    private Double fixedVol;
    /** 额定电流(A) **/
    private Double fixedCurrent;
    /** 总储电量(kW.h) **/
    private Double totalCapacity;
    /** 能量密度(Wh/kg) **/
    private Double capacityDensity;
    /** 功率密度(W/kg) **/
    private Double powerCapacity;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    public Double getFixedVol() {
        return fixedVol;
    }

    public void setFixedVol(Double fixedVol) {
        this.fixedVol = fixedVol;
    }
    public Double getFixedCurrent() {
        return fixedCurrent;
    }

    public void setFixedCurrent(Double fixedCurrent) {
        this.fixedCurrent = fixedCurrent;
    }
    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    public Double getCapacityDensity() {
        return capacityDensity;
    }

    public void setCapacityDensity(Double capacityDensity) {
        this.capacityDensity = capacityDensity;
    }
    public Double getPowerCapacity() {
        return powerCapacity;
    }

    public void setPowerCapacity(Double powerCapacity) {
        this.powerCapacity = powerCapacity;
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
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}
