package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.DriveDevice;
import com.bitnei.cloud.sys.domain.EngeryDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
* 查询可充电储能装置型号 model
* @author hzr
* @since JDK1.8
*/
@ApiModel(value = "DriveDeviceTempModel", description = "可充电储能装置型号Model")
public class EnergyDeviceTempModel extends BaseModel {

    @ApiModelProperty(value = "型号id")
    private String id;

    @ColumnHeader(title = "可充电装置类型 1:动力蓄电池 2:超级电容")
    @ApiModelProperty(value = "可充电装置类型 1:动力蓄电池 2:超级电容")
    private Integer modelType;

    /** 可充电储能装置类型名称显示**/
    @DictName(code = "ENERGY_DEVICE_TYPE", joinField = "modelType")
    @ApiModelProperty(value = "可充电装置类型名称")
    private String modelTypeDisplay;

    @ColumnHeader(title = "电池种类(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池)")
    @ApiModelProperty(value = "电池种类(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池)")
    private Integer batteryType;

    /** 电池种类字典名称显示**/
    @DictName(code = "BATTERY_TYPE", joinField = "batteryType")
    @ApiModelProperty(value = "电池种类名称")
    private String  batteryTypeDisplay;


    /** 可充电储能装置型号 **/
    @ColumnHeader(title = "可充电储能装置型号")
    @ApiModelProperty(value = "可充电储能装置型号")
    private String modelName;

    @ColumnHeader(title = "总储电量(kW.h)")
    @ApiModelProperty(value = "总储电量(kW.h)")
    private Double totalCapacity;

    @ColumnHeader(title = "能量密度(Wh/kg)")
    @ApiModelProperty(value = "能量密度(Wh/kg)")
    private Double capacityDensity;

    /** 可充电储能装置生产企业**/
    @ColumnHeader(title = "可充电储能装置生产企业")
    @ApiModelProperty(value = "可充电储能装置生产企业")
    private String prodUnitId;

    /** 可充电储能装置生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "可充电储能装置生产企业名称")
    private String prodUnitDisplay;

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

    public Integer getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(Integer batteryType) {
        this.batteryType = batteryType;
    }

    public String getBatteryTypeDisplay() {
        return batteryTypeDisplay;
    }

    public void setBatteryTypeDisplay(String batteryTypeDisplay) {
        this.batteryTypeDisplay = batteryTypeDisplay;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public String getProdUnitDisplay() {
        return prodUnitDisplay;
    }

    public void setProdUnitDisplay(String prodUnitDisplay) {
        this.prodUnitDisplay = prodUnitDisplay;
    }

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static EnergyDeviceTempModel fromEntry(EngeryDevice entry){
        EnergyDeviceTempModel m = new EnergyDeviceTempModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
