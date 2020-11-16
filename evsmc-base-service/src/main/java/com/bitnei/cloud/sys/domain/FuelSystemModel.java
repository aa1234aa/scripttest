package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： FuelSystemModel实体<br>
* 描述： FuelSystemModel实体<br>
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
* <td>2018-12-10 17:02:38</td>
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
public class FuelSystemModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 型号名称 **/
    private String name;
    /** 额定功率(KW) **/
    private Double ratedPower;
    /** 峰值功率(KW) **/
    private Double peakPower;
    /** 体积功率密度(kw/L) **/
    private Double volumePowerDensity;
    /** 燃料电池升压器电高输出电压(V) **/
    private Double maxOutputVoltage;
    /** 储氢罐个数 **/
    private Integer hydrogenStorageNumber;
    /** 储氢罐容量(L) **/
    private Double hydrogenStorageCapacity;
    /** 燃料电池系统控制器型号 **/
    private String controllerModel;
    /** 燃料电池系统生产企业 **/
    private String prodUnitId;
    /** 燃料电池系统控制器生产企业 **/
    private String controllerProdUnitId;
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
    public Double getRatedPower() {
        return ratedPower;
    }

    public void setRatedPower(Double ratedPower) {
        this.ratedPower = ratedPower;
    }

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    public Double getVolumePowerDensity() {
        return volumePowerDensity;
    }

    public void setVolumePowerDensity(Double volumePowerDensity) {
        this.volumePowerDensity = volumePowerDensity;
    }
    public Double getMaxOutputVoltage() {
        return maxOutputVoltage;
    }

    public void setMaxOutputVoltage(Double maxOutputVoltage) {
        this.maxOutputVoltage = maxOutputVoltage;
    }
    public Integer getHydrogenStorageNumber() {
        return hydrogenStorageNumber;
    }

    public void setHydrogenStorageNumber(Integer hydrogenStorageNumber) {
        this.hydrogenStorageNumber = hydrogenStorageNumber;
    }
    public Double getHydrogenStorageCapacity() {
        return hydrogenStorageCapacity;
    }

    public void setHydrogenStorageCapacity(Double hydrogenStorageCapacity) {
        this.hydrogenStorageCapacity = hydrogenStorageCapacity;
    }
    public String getControllerModel() {
        return controllerModel;
    }

    public void setControllerModel(String controllerModel) {
        this.controllerModel = controllerModel;
    }
    public String getProdUnitId() {
        return prodUnitId;
    }

    public void setProdUnitId(String prodUnitId) {
        this.prodUnitId = prodUnitId;
    }
    public String getControllerProdUnitId() {
        return controllerProdUnitId;
    }

    public void setControllerProdUnitId(String controllerProdUnitId) {
        this.controllerProdUnitId = controllerProdUnitId;
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
