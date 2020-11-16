package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EngeryDevice实体<br>
* 描述： EngeryDevice实体<br>
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
* <td>2018-12-06 23:59:45</td>
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
public class EngeryDevice extends TailBean {

    /** 主键 **/
    private String id;
    /** 可充电储能装置编码 **/
    private String name;
    /** 可充电装置类型 1:动力蓄电池 2:超级电容
 **/
    private Integer modelType;
    /** 超级电容型号id **/
    private String capacityModelId;
    /** 动力蓄电池型号id **/
    private String batteryModelId;
    /** 安装位置
 **/
    private String installPostion;
    /** 生产日期 **/
    private String produceDate;
    /** 发票号 **/
    private String invoiceNo;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 生产厂商 **/
    private String unitId;

    /** 可充电储能装置型号 **/
    private String modelName;

    /** 电池种类 **/
    private Integer batteryType;

    /** 总储电量(kW.h) **/
    private Double totalCapacity;

    /** 能量密度(Wh/kg) **/
    private Double capacityDensity;

    /** 可充电储能装置生产企业**/
    private String prodUnitId;

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
    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }
    public String getCapacityModelId() {
        return capacityModelId;
    }

    public void setCapacityModelId(String capacityModelId) {
        this.capacityModelId = capacityModelId;
    }
    public String getBatteryModelId() {
        return batteryModelId;
    }

    public void setBatteryModelId(String batteryModelId) {
        this.batteryModelId = batteryModelId;
    }
    public String getInstallPostion() {
        return installPostion;
    }

    public void setInstallPostion(String installPostion) {
        this.installPostion = installPostion;
    }
    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(Integer batteryType) {
        this.batteryType = batteryType;
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

    public String getProdUnitId() {
        return prodUnitId;
    }

    public void setProdUnitId(String prodUnitId) {
        this.prodUnitId = prodUnitId;
    }
}
