package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bitnei.cloud.sys.domain.PowerDevice;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PowerDevice新增模型<br>
* 描述： PowerDevice新增模型<br>
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
@ApiModel(value = "PowerDeviceModel", description = "发电装置信息Model")
public class PowerDeviceModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "发电装置编码", example = "PD001", notNull = true, desc = "发电装置编码")
    @NotBlank(message = "发电装置编码不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "发电装置编码长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发电装置编码")
    private String code;

    @ApiModelProperty(value = "发电装置类型 1:燃油发电机 2:燃料电池系统")
    private Integer modelType;

    /** 发电装置类型名称显示**/
    @ColumnHeader(title = "发电装置类型", example = "燃油发电机", notNull = true, desc = "发电装置类型 1:燃油发电机 2:燃料电池系统")
    @NotBlank(message = "发电装置类型不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "发电装置类型长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "GENERATING_DEVICE_TYPE", joinField = "modelType")
    @ApiModelProperty(value = "发电装置类型名称")
    private String modelTypeDisplay;

    @ApiModelProperty(value = "发电装置型号ID")
    private String modelId;

    @ColumnHeader(title = "发电装置型号", example = "FG001", notNull = true, desc = "发电装置型号")
    @NotBlank(message = "发电装置型号不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "发电装置型号长度2-100个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "发电装置型号名称")
    private String modelName;

    @ApiModelProperty(value = "燃料电池系统型号id")
    private String fuelBatteryModelId;

    @ApiModelProperty(value = "燃油发电机型号id")
    private String fuelGeneratorModelId;

    @ColumnHeader(title = "发电装置序号", example = "S001", notNull = false, desc = "发电装置序号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "发电装置序号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发电装置序号")
    private String sequenceNumber;

    @NotNull(message = "安装位置不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "安装位置 1：前置 2：中置 3：后置")
    private Integer installPosition;

    /** 安装位置名称显示**/
    @ColumnHeader(title = "安装位置", example = "前置", notNull = true, desc = "安装位置 1：前置 2：中置 3：后置")
    @NotNull(message = "安装位置不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,10}|$", message = "安装位置长度2-10个中文", groups = {GroupExcelImport.class})
    @DictName(code = "GENERATOR_INSTALL_POSITION", joinField = "installPosition")
    @ApiModelProperty(value = "安装位置名称")
    private String installPositionDisplay;

    @ColumnHeader(title = "发电装置生产日期", example = "2019-04-01", notNull = false, desc = "发电装置生产日期,格式为：YYYY-MM-DD")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "发电装置生产日期格式不正确,格式为：(yyyy-MM-dd)", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "发电装置生产日期")
    private String produceDate;

    @ColumnHeader(title = "发电装置发票号", example = "1001", notNull = false, desc = "发电装置发票号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9()/-]{0,32}|$", message = "发电装置发票号长度为0-32个中英文、数字及特殊字符()/-", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发电装置发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "发电装置控制器型号")
    private String controllerModel;

    @ApiModelProperty(value = "发电装置生产企业ID")
    private String prodUnitId;

    @ApiModelProperty(value = "发电装置生产企业名称")
    private String prodUnitName;

    @ApiModelProperty(value = "发电装置控制器生产企业ID")
    private String controllerProdUnitId;

    @ApiModelProperty(value = "发电装置控制器生产企业名称")
    private String controllerProdUnitName;

    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    /** 燃料电池系统型号名称显示**/
    @LinkName(table = "sys_fuel_system_model", column = "name", joinField = "fuelBatteryModelId",desc = "")
    @ApiModelProperty(value = "燃料电池系统型号名称")
    private String batteryModelDisplay;
    /** 燃料电池系统控制器型号显示**/
    @LinkName(table = "sys_fuel_system_model", column = "controller_model", joinField = "fuelBatteryModelId",desc = "")
    @ApiModelProperty(value = "燃料电池系统控制器型号")
    private String bControllerModelDisplay;
    /** 燃料电池系统控制器生产企业ID**/
    @LinkName(table = "sys_fuel_system_model", column = "controller_prod_unit_id", joinField = "fuelBatteryModelId",desc = "",level = 1)
    @ApiModelProperty(value = "燃料电池系统控制器生产企业ID")
    private String bcpUnitId;
    /** 燃料电池系统控制器生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "bcpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "燃料电池系统控制器生产企业名称")
    private String bcpUnitDisplay;
    /** 燃料电池系统生产企业ID**/
    @LinkName(table = "sys_fuel_system_model", column = "prod_unit_id", joinField = "fuelBatteryModelId",desc = "",level = 1)
    @ApiModelProperty(value = "燃料电池系统生产企业ID")
    private String bpUnitId;
    /** 燃料电池系统生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "bpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "燃料电池系统生产企业名称")
    private String bpUnitDisplay;

    @ApiModelProperty(value = "燃料电池系统峰值功率")
    private Double fsPeakPower;


    /** 燃油发电机型号名称显示**/
    @LinkName(table = "sys_fuel_generator_model", column = "name", joinField = "fuelGeneratorModelId",desc = "")
    @ApiModelProperty(value = "燃油发电机型号名称")
    private String generatorModelDisplay;
    /** 燃油发电机控制器型号显示**/
    @LinkName(table = "sys_fuel_generator_model", column = "controller_model", joinField = "fuelGeneratorModelId",desc = "")
    @ApiModelProperty(value = "燃油发电机控制器型号")
    private String gControllerModelDisplay;
    /** 燃油发电机控制器生产企业ID**/
    @LinkName(table = "sys_fuel_generator_model", column = "controller_prod_unit_id", joinField = "fuelGeneratorModelId",desc = "",level = 1)
    @ApiModelProperty(value = "燃油发电机控制器生产企业ID")
    private String gcpUnitId;
    /** 燃油发电机控制器生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "gcpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "燃油发电机控制器生产企业名称")
    private String gcpUnitDisplay;
    /** 燃油发电机生产企业ID**/
    @LinkName(table = "sys_fuel_generator_model", column = "prod_unit_id", joinField = "fuelGeneratorModelId",desc = "",level = 1)
    @ApiModelProperty(value = "燃油发电机生产企业ID")
    private String gpUnitId;
    /** 燃油发电机生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "gpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "燃油发电机生产企业名称")
    private String gpUnitDisplay;

    @ApiModelProperty(value = "燃油发电机峰值功率")
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

    public String getModelTypeDisplay() {
        return modelTypeDisplay;
    }

    public void setModelTypeDisplay(String modelTypeDisplay) {
        this.modelTypeDisplay = modelTypeDisplay;
    }

    public String getInstallPositionDisplay() {
        return installPositionDisplay;
    }

    public void setInstallPositionDisplay(String installPositionDisplay) {
        this.installPositionDisplay = installPositionDisplay;
    }

    public String getBatteryModelDisplay() {
        return batteryModelDisplay;
    }

    public void setBatteryModelDisplay(String batteryModelDisplay) {
        this.batteryModelDisplay = batteryModelDisplay;
    }

    public String getbControllerModelDisplay() {
        return bControllerModelDisplay;
    }

    public void setbControllerModelDisplay(String bControllerModelDisplay) {
        this.bControllerModelDisplay = bControllerModelDisplay;
    }

    public String getBcpUnitId() {
        return bcpUnitId;
    }

    public void setBcpUnitId(String bcpUnitId) {
        this.bcpUnitId = bcpUnitId;
    }

    public String getBcpUnitDisplay() {
        return bcpUnitDisplay;
    }

    public void setBcpUnitDisplay(String bcpUnitDisplay) {
        this.bcpUnitDisplay = bcpUnitDisplay;
    }

    public String getBpUnitId() {
        return bpUnitId;
    }

    public void setBpUnitId(String bpUnitId) {
        this.bpUnitId = bpUnitId;
    }

    public String getBpUnitDisplay() {
        return bpUnitDisplay;
    }

    public void setBpUnitDisplay(String bpUnitDisplay) {
        this.bpUnitDisplay = bpUnitDisplay;
    }

    public String getGeneratorModelDisplay() {
        return generatorModelDisplay;
    }

    public void setGeneratorModelDisplay(String generatorModelDisplay) {
        this.generatorModelDisplay = generatorModelDisplay;
    }

    public String getgControllerModelDisplay() {
        return gControllerModelDisplay;
    }

    public void setgControllerModelDisplay(String gControllerModelDisplay) {
        this.gControllerModelDisplay = gControllerModelDisplay;
    }

    public String getGcpUnitId() {
        return gcpUnitId;
    }

    public void setGcpUnitId(String gcpUnitId) {
        this.gcpUnitId = gcpUnitId;
    }

    public String getGcpUnitDisplay() {
        return gcpUnitDisplay;
    }

    public void setGcpUnitDisplay(String gcpUnitDisplay) {
        this.gcpUnitDisplay = gcpUnitDisplay;
    }

    public String getGpUnitId() {
        return gpUnitId;
    }

    public void setGpUnitId(String gpUnitId) {
        this.gpUnitId = gpUnitId;
    }

    public String getGpUnitDisplay() {
        return gpUnitDisplay;
    }

    public void setGpUnitDisplay(String gpUnitDisplay) {
        this.gpUnitDisplay = gpUnitDisplay;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
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

    public String getProdUnitName() {
        return prodUnitName;
    }

    public void setProdUnitName(String prodUnitName) {
        this.prodUnitName = prodUnitName;
    }

    public String getControllerProdUnitId() {
        return controllerProdUnitId;
    }

    public void setControllerProdUnitId(String controllerProdUnitId) {
        this.controllerProdUnitId = controllerProdUnitId;
    }

    public String getControllerProdUnitName() {
        return controllerProdUnitName;
    }

    public void setControllerProdUnitName(String controllerProdUnitName) {
        this.controllerProdUnitName = controllerProdUnitName;
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

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PowerDeviceModel fromEntry(PowerDevice entry){
        PowerDeviceModel m = new PowerDeviceModel();
        BeanUtils.copyProperties(entry, m);
        DataLoader.loadNames(m);
        if(entry.getModelType()!= null && entry.getModelType() == 1){ //燃油发电机型号
            m.setModelId(entry.getFuelGeneratorModelId());
            m.setModelName(m.getGeneratorModelDisplay());
            m.setControllerModel(m.getgControllerModelDisplay());
            m.setProdUnitId(m.getGpUnitId());
            m.setProdUnitName(m.getGpUnitDisplay());
            m.setControllerProdUnitId(m.getGcpUnitId());
            m.setControllerProdUnitName(m.getGcpUnitDisplay());
            m.setPeakPower(m.getFgPeakPower());
        }else if(entry.getModelType()!= null && entry.getModelType() == 2){ //燃料电池系统型号
            m.setModelId(entry.getFuelBatteryModelId());
            m.setModelName(m.getBatteryModelDisplay());
            m.setControllerModel(m.getbControllerModelDisplay());
            m.setProdUnitId(m.getBpUnitId());
            m.setProdUnitName(m.getBpUnitDisplay());
            m.setControllerProdUnitId(m.getBcpUnitId());
            m.setControllerProdUnitName(m.getBcpUnitDisplay());
            m.setPeakPower(m.getFsPeakPower());
        }
        return m;
    }

}
