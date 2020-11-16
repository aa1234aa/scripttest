package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.DriveDevice;
import com.bitnei.cloud.sys.domain.PowerDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
* 查询驱动装置型号 model
* @author hzr
* @since JDK1.8
*/
@ApiModel(value = "DriveDeviceTempModel", description = "驱动装置型号Model")
public class DriveDeviceTempModel extends BaseModel {

    @ApiModelProperty(value = "型号id")
    private String id;

    @ColumnHeader(title = "驱动装置类型 1:驱动电机 2:发动机")
    @ApiModelProperty(value = "驱动装置类型 1:驱动电机 2:发动机")
    private Integer modelType;

    /** 驱动装置类型名称显示**/
    @DictName(code = "DRIVE_DEVICE", joinField = "modelType")
    @ApiModelProperty(value = "驱动装置类型名称")
    private String modelTypeDisplay;


    /** 驱动装置型号 **/
    @ColumnHeader(title = "驱动装置型号")
    @ApiModelProperty(value = "驱动装置型号")
    private String modelName;

    /** 峰值功率(KW) **/
    @ColumnHeader(title = "峰值功率(KW)")
    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    /** 控制器型号 **/
    @ColumnHeader(title = "驱动装置控制器型号")
    @ApiModelProperty(value = "驱动装置控制器型号")
    private String controllerModel;

    /** 驱动装置生产企业**/
    @ColumnHeader(title = "驱动装置生产企业")
    @ApiModelProperty(value = "驱动装置生产企业")
    private String prodUnitId;

    /** 驱动装置控制器生产企业**/
    @ColumnHeader(title = "驱动装置控制器生产企业")
    @ApiModelProperty(value = "驱动装置控制器生产企业")
    private String controllerProdUnitId;

    /** 驱动装置生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "驱动装置生产企业名称")
    private String prodUnitDisplay;

    /** 驱动装置控制器生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "controllerProdUnitId",desc = "")
    @ApiModelProperty(value = "驱动装置控制器生产企业名称")
    private String controllerProdUnitDisplay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public String getModelTypeDisplay() {
        return modelTypeDisplay;
    }

    public void setModelTypeDisplay(String modelTypeDisplay) {
        this.modelTypeDisplay = modelTypeDisplay;
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

    public String getProdUnitDisplay() {
        return prodUnitDisplay;
    }

    public void setProdUnitDisplay(String prodUnitDisplay) {
        this.prodUnitDisplay = prodUnitDisplay;
    }

    public String getControllerProdUnitDisplay() {
        return controllerProdUnitDisplay;
    }

    public void setControllerProdUnitDisplay(String controllerProdUnitDisplay) {
        this.controllerProdUnitDisplay = controllerProdUnitDisplay;
    }

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DriveDeviceTempModel fromEntry(DriveDevice entry){
        DriveDeviceTempModel m = new DriveDeviceTempModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
