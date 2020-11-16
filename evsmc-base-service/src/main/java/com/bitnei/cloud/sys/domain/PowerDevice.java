package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PowerDevice实体<br>
* 描述： PowerDevice实体<br>
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
* <td>2018-12-10 17:03:09</td>
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
public class PowerDevice extends TailBean {

    /** 主键 **/
    private String id;
    /** 发电装置编码 **/
    private String code;
    /** 发电装置类型 1:燃油发电机型号 2:燃料电池系统型号 **/
    private Integer modelType;
    /** 燃料电池系统型号id **/
    private String fuelBatteryModelId;
    /** 燃油发电机型号id **/
    private String fuelGeneratorModelId;
    /** 发电装置序号 **/
    private String sequenceNumber;
    /** 安装位置 1：前置 2：中置 3：后置 **/
    private Integer installPosition;
    /** 发电装置生产日期 **/
    private String produceDate;
    /** 发电装置发票号 **/
    private String invoiceNo;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;


    /** 发电装置型号 **/
    private String modelName;

    /** 控制器型号 **/
    private String controllerModel;

    /** 发电装置生产企业**/
    private String prodUnitId;

    /** 发电装置控制器生产企业**/
    private String controllerProdUnitId;

    /** 峰值功率(KW) **/
    private Double peakPower;

    /** 燃料电池系统峰值功率 **/
    private Double fsPeakPower;
    /** 燃油发电机峰值功率 **/
    private Double fgPeakPower;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }
    public String getFuelBatteryModelId() {
        return fuelBatteryModelId;
    }

    public void setFuelBatteryModelId(String fuelBatteryModelId) {
        this.fuelBatteryModelId = fuelBatteryModelId;
    }
    public String getFuelGeneratorModelId() {
        return fuelGeneratorModelId;
    }

    public void setFuelGeneratorModelId(String fuelGeneratorModelId) {
        this.fuelGeneratorModelId = fuelGeneratorModelId;
    }
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    public Integer getInstallPosition() {
        return installPosition;
    }

    public void setInstallPosition(Integer installPosition) {
        this.installPosition = installPosition;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    public Double getFsPeakPower() {
        return fsPeakPower;
    }

    public void setFsPeakPower(Double fsPeakPower) {
        this.fsPeakPower = fsPeakPower;
    }

    public Double getFgPeakPower() {
        return fgPeakPower;
    }

    public void setFgPeakPower(Double fgPeakPower) {
        this.fgPeakPower = fgPeakPower;
    }
}
