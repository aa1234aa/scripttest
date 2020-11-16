package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： FuelGeneratorModel实体<br>
* 描述： FuelGeneratorModel实体<br>
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
* <td>2018-12-10 17:02:52</td>
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
public class FuelGeneratorModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 燃油发电机型号 **/
    private String name;
    /**  进气方式 **/
    private Integer airInlet;
    /** 燃料形式 **/
    private Integer fuelForm;
    /** 油箱容量(L) **/
    private Double tankCapacity;
    /** 品牌名称 **/
    private String brandName;
    /** 燃油发电机控制器型号 **/
    private String controllerModel;
    /** 峰值功率(KW) **/
    private Double peakPower;
    /** 峰值功率转速(r/min) **/
    private Double peakPowerSpeed;
    /** 峰值转矩(N.m) **/
    private Double peakTorque;
    /** 峰值扭矩转速(r/min) **/
    private Double peakTorqueSpeed;
    /** 燃油发电机生产企业 **/
    private String prodUnitId;
    /** 燃油发电机控制器生产企业 **/
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
    public Integer getAirInlet() {
        return airInlet;
    }

    public void setAirInlet(Integer airInlet) {
        this.airInlet = airInlet;
    }
    public Integer getFuelForm() {
        return fuelForm;
    }

    public void setFuelForm(Integer fuelForm) {
        this.fuelForm = fuelForm;
    }
    public Double getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(Double tankCapacity) {
        this.tankCapacity = tankCapacity;
    }
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getControllerModel() {
        return controllerModel;
    }

    public void setControllerModel(String controllerModel) {
        this.controllerModel = controllerModel;
    }
    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }
    public Double getPeakPowerSpeed() {
        return peakPowerSpeed;
    }

    public void setPeakPowerSpeed(Double peakPowerSpeed) {
        this.peakPowerSpeed = peakPowerSpeed;
    }

    public Double getPeakTorque() {
        return peakTorque;
    }

    public void setPeakTorque(Double peakTorque) {
        this.peakTorque = peakTorque;
    }

    public Double getPeakTorqueSpeed() {
        return peakTorqueSpeed;
    }

    public void setPeakTorqueSpeed(Double peakTorqueSpeed) {
        this.peakTorqueSpeed = peakTorqueSpeed;
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
