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

import com.bitnei.cloud.sys.domain.DriveDevice;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DriveDevice新增模型<br>
* 描述： DriveDevice新增模型<br>
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
* <td>2018-12-03 11:17:33</td>
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
@ApiModel(value = "DriveDeviceModel", description = "驱动装置信息Model")
public class DriveDeviceModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "驱动装置编码", example = "DD001", notNull = true, desc = "驱动装置编码")
    @NotBlank(message = "驱动装置编码不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "驱动装置编码长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "驱动装置编码")
    private String code;

    @ApiModelProperty(value = "驱动装置类型 1:驱动电机 2:发动机")
    private Integer modelType;

    /** 驱动装置类型名称显示**/
    @ColumnHeader(title = "驱动装置类型", example = "驱动电机", notNull = true, desc = "驱动装置类型 1:驱动电机 2:发动机")
    @NotBlank(message = "驱动装置类型不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "驱动装置类型长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "DRIVE_DEVICE", joinField = "modelType")
    @ApiModelProperty(value = "驱动装置类型名称")
    private String modelTypeDisplay;

    @ApiModelProperty(value = "驱动装置型号ID")
    private String modelId;

    @ColumnHeader(title = "驱动装置型号", example = "DM001", notNull = true, desc = "驱动装置型号，选自驱动电机型号或者发动机型号，所选型号需与驱动装置类型一致")
    @NotBlank(message = "驱动装置型号不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "驱动装置型号长度2-100个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "驱动装置型号名称")
    private String modelName;

    @ColumnHeader(title = "驱动装置序号", example = "11", notNull = false, desc = "驱动装置序号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "驱动装置序号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "驱动装置序号")
    private String sequenceNumber;

    @NotNull(message = "安装位置不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "安装位置 1:前置 2:后置 3:轮边 4:轮毂内")
    private Integer installPosition;

    /** 安装位置名称显示**/
    @ColumnHeader(title = "安装位置", example = "前置", notNull = true, desc = "安装位置 1:前置 2:后置 3:轮边 4:轮毂内")
    @DictName(code = "INSTALL_POSITION", joinField = "installPosition")
    @NotNull(message = "安装位置不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,10}|$", message = "安装位置长度2-10个中文", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "安装位置名称")
    private String installPositionDisplay;

    @ColumnHeader(title = "生产日期", example = "2019-02-01", notNull = false, desc = "生产日期,格式为：(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "生产日期格式不正确,格式为：(yyyy-MM-dd)", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "生产日期")
    private String produceDate;

    @ColumnHeader(title = "驱动装置发票号", example = "1002", notNull = false, desc = "驱动装置发票号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9()/-]{0,32}|$", message = "驱动装置发票号长度为0-32个中英文、数字及特殊字符()/-", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "驱动装置发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "驱动装置控制器型号")
    private String controllerModel;

    @ApiModelProperty(value = "驱动装置生产企业ID")
    private String prodUnitId;

    @ApiModelProperty(value = "驱动装置生产企业名称")
    private String prodUnitName;

    @ApiModelProperty(value = "驱动装置控制器生产企业ID")
    private String controllerProdUnitId;

    @ApiModelProperty(value = "驱动装置控制器生产企业名称")
    private String controllerProdUnitName;

    @ApiModelProperty(value = "驱动电机型号")
    private String driveModelId;

    @ApiModelProperty(value = "发动机型号")
    private String engineModelId;

    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    /** 驱动电机型号名称显示**/
    @LinkName(table = "sys_drive_motor_model", column = "name", joinField = "driveModelId",desc = "")
    @ApiModelProperty(value = "驱动电机型号名称")
    private String driveModelDisplay;
    /** 驱动电机控制器型号显示**/
    @LinkName(table = "sys_drive_motor_model", column = "controller_model", joinField = "driveModelId",desc = "")
    @ApiModelProperty(value = "驱动电机控制器型号")
    private String dControllerModelDisplay;
    /** 驱动电机控制器生产企业ID**/
    @LinkName(table = "sys_drive_motor_model", column = "controller_prod_unit_id", joinField = "driveModelId",desc = "",level = 1)
    @ApiModelProperty(value = "驱动电机控制器生产企业ID")
    private String dcpUnitId;
    /** 驱动电机控制器生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "dcpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "驱动电机控制器生产企业名称")
    private String dcpUnitDisplay;
    /** 驱动电机生产企业ID**/
    @LinkName(table = "sys_drive_motor_model", column = "prod_unit_id", joinField = "driveModelId",desc = "",level = 1)
    @ApiModelProperty(value = "驱动电机生产企业ID")
    private String dpUnitId;
    /** 驱动电机生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "dpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "驱动电机生产企业名称")
    private String dpUnitDisplay;
    @ApiModelProperty(value = "驱动电机峰值功率(KW)")
    @LinkName(table = "sys_drive_motor_model", column = "peak_power", joinField = "driveModelId",desc = "")
    private Double dmPeakPower;


    /** 发动机型号名称显示**/
    @LinkName(table = "sys_engine_model", column = "name", joinField = "engineModelId",desc = "")
    @ApiModelProperty(value = "发动机型号名称")
    private String engineModelDisplay;
    /** 发动机控制器型号显示**/
    @LinkName(table = "sys_engine_model", column = "controller_model", joinField = "engineModelId",desc = "")
    @ApiModelProperty(value = "发动机控制器型号")
    private String eControllerModelDisplay;
    /** 发动机控制器生产企业ID**/
    @LinkName(table = "sys_engine_model", column = "controller_prod_unit_id", joinField = "engineModelId",desc = "",level = 1)
    @ApiModelProperty(value = "发动机控制器生产企业ID")
    private String ecpUnitId;
    /** 发动机控制器生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "ecpUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "发动机控制器生产企业名称")
    private String ecpUnitDisplay;
    /** 发动机生产企业ID**/
    @LinkName(table = "sys_engine_model", column = "prod_unit_id", joinField = "engineModelId",desc = "",level = 1)
    @ApiModelProperty(value = "发动机生产企业ID")
    private String epUnitId;
    /** 发动机生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "epUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "发动机生产企业名称")
    private String epUnitDisplay;
    @ApiModelProperty(value = "发动机峰值功率(KW)")
    @LinkName(table = "sys_engine_model", column = "peak_power", joinField = "engineModelId",desc = "")
    private Double emPeakPower;


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
    public String getDriveModelId() {
        return driveModelId;
    }

    public void setDriveModelId(String driveModelId) {
        this.driveModelId = driveModelId;
    }
    public String getEngineModelId() {
        return engineModelId;
    }

    public void setEngineModelId(String engineModelId) {
        this.engineModelId = engineModelId;
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

    public String getDriveModelDisplay() {
        return driveModelDisplay;
    }

    public void setDriveModelDisplay(String driveModelDisplay) {
        this.driveModelDisplay = driveModelDisplay;
    }

    public String getEngineModelDisplay() {
        return engineModelDisplay;
    }

    public void setEngineModelDisplay(String engineModelDisplay) {
        this.engineModelDisplay = engineModelDisplay;
    }

    public String getDcpUnitId() {
        return dcpUnitId;
    }

    public void setDcpUnitId(String dcpUnitId) {
        this.dcpUnitId = dcpUnitId;
    }

    public String getDcpUnitDisplay() {
        return dcpUnitDisplay;
    }

    public void setDcpUnitDisplay(String dcpUnitDisplay) {
        this.dcpUnitDisplay = dcpUnitDisplay;
    }

    public String getDpUnitId() {
        return dpUnitId;
    }

    public void setDpUnitId(String dpUnitId) {
        this.dpUnitId = dpUnitId;
    }

    public String getDpUnitDisplay() {
        return dpUnitDisplay;
    }

    public void setDpUnitDisplay(String dpUnitDisplay) {
        this.dpUnitDisplay = dpUnitDisplay;
    }

    public String getEcpUnitId() {
        return ecpUnitId;
    }

    public void setEcpUnitId(String ecpUnitId) {
        this.ecpUnitId = ecpUnitId;
    }

    public String getEcpUnitDisplay() {
        return ecpUnitDisplay;
    }

    public void setEcpUnitDisplay(String ecpUnitDisplay) {
        this.ecpUnitDisplay = ecpUnitDisplay;
    }

    public String getEpUnitId() {
        return epUnitId;
    }

    public void setEpUnitId(String epUnitId) {
        this.epUnitId = epUnitId;
    }

    public String getEpUnitDisplay() {
        return epUnitDisplay;
    }

    public void setEpUnitDisplay(String epUnitDisplay) {
        this.epUnitDisplay = epUnitDisplay;
    }

    public String getdControllerModelDisplay() {
        return dControllerModelDisplay;
    }

    public void setdControllerModelDisplay(String dControllerModelDisplay) {
        this.dControllerModelDisplay = dControllerModelDisplay;
    }

    public String geteControllerModelDisplay() {
        return eControllerModelDisplay;
    }

    public void seteControllerModelDisplay(String eControllerModelDisplay) {
        this.eControllerModelDisplay = eControllerModelDisplay;
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

    public Double getDmPeakPower() {
        return dmPeakPower;
    }

    public void setDmPeakPower(Double dmPeakPower) {
        this.dmPeakPower = dmPeakPower;
    }

    public Double getEmPeakPower() {
        return emPeakPower;
    }

    public void setEmPeakPower(Double emPeakPower) {
        this.emPeakPower = emPeakPower;
    }

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DriveDeviceModel fromEntry(DriveDevice entry){
        DriveDeviceModel m = new DriveDeviceModel();
        BeanUtils.copyProperties(entry, m);
        DataLoader.loadNames(m);
        if(entry.getModelType()!= null && entry.getModelType() == 1){ //驱动电机
            m.setModelId(entry.getDriveModelId());
            m.setModelName(m.getDriveModelDisplay());
            m.setControllerModel(m.getdControllerModelDisplay());
            m.setProdUnitId(m.getDpUnitId());
            m.setProdUnitName(m.getDpUnitDisplay());
            m.setControllerProdUnitId(m.getDcpUnitId());
            m.setControllerProdUnitName(m.getDcpUnitDisplay());
            m.setPeakPower(m.getDmPeakPower());
        }else if(entry.getModelType()!= null && entry.getModelType() == 2){ //发动机
            m.setModelId(entry.getEngineModelId());
            m.setModelName(m.getEngineModelDisplay());
            m.setControllerModel(m.geteControllerModelDisplay());
            m.setProdUnitId(m.getEpUnitId());
            m.setProdUnitName(m.getEpUnitDisplay());
            m.setControllerProdUnitId(m.getEcpUnitId());
            m.setControllerProdUnitName(m.getEcpUnitDisplay());
            m.setPeakPower(m.getEmPeakPower());
        }
        return m;
    }

}
