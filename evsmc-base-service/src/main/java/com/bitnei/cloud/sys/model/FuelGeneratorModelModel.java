package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

import com.bitnei.cloud.sys.domain.FuelGeneratorModel;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： FuelGeneratorModel新增模型<br>
* 描述： FuelGeneratorModel新增模型<br>
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
@ApiModel(value = "FuelGeneratorModelModel", description = "燃油发电机型号Model")
public class FuelGeneratorModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "燃油发电机型号", example = "FG001", notNull = true, desc = "燃油发电机型号")
    @NotBlank(message = "燃油发电机型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "燃油发电机型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "燃油发电机型号")
    private String name;

    @ApiModelProperty(value = "进气方式")
    private Integer airInlet;

    /** 进气方式名称显示**/
    @ColumnHeader(title = "进气方式", example = "自然吸气", notNull = false, desc = "进气方式:自然吸气，涡轮增压，机械增压")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,10}|$", message = "进气方式长度2-10个中文", groups = {GroupExcelImport.class})
    @DictName(code = "DRIVE_INTAKE_MODE", joinField = "airInlet")
    @ApiModelProperty(value = "进气方式名称")
    private String airInletDisplay;

    @NotNull(message = "燃料形式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "燃料形式")
    private Integer fuelForm;

    /** 燃料形式名称显示**/
    @ColumnHeader(title = "燃料形式", example = "汽油", notNull = true, desc = "燃料形式:汽油，柴油")
    @NotBlank(message = "燃料形式不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,10}|$", message = "燃料形式长度2-10个中文", groups = {GroupExcelImport.class})
    @DictName(code = "GENERATOR_FUEL_FORM", joinField = "fuelForm")
    @ApiModelProperty(value = "燃料形式名称")
    private String fuelFormDisplay;

    @ColumnHeader(title = "油箱容量(L)", example = "100", notNull = false, desc = "油箱容量(L)")
    @Digits(integer = 4, fraction = 2, message = "油箱容量(L)整数长度1~4位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "油箱容量(L)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999", message = "油箱容量(L)必须是一个数字，其值必须小于等于9999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "油箱容量(L)")
    private Double tankCapacity;

    @ColumnHeader(title = "品牌名称", example = "B001", notNull = false, desc = "品牌名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,20}|$", message = "品牌名称长度4-20个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ColumnHeader(title = "燃油发电机控制器型号", example = "FC001", notNull = false, desc = "燃油发电机控制器型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "燃油发电机控制器型号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "燃油发电机控制器型号")
    private String controllerModel;

    @ColumnHeader(title = "峰值功率(KW)", example = "1000", notNull = true, desc = "峰值功率(KW)")
    @NotNull(message = "峰值功率(KW)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "峰值功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    @ColumnHeader(title = "峰值功率转速(r/min)", example = "1000", notNull = false, desc = "峰值功率转速(r/min)")
    @Digits(integer = 7, fraction = 0, message = "峰值功率转速(r/min)整数长度1~7位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率转速(r/min)")
    private Double peakPowerSpeed;

    @ColumnHeader(title = "峰值转矩(N.m)", example = "1000", notNull = true, desc = "峰值转矩(N.m)")
    @NotNull(message = "峰值转矩(N.m)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "峰值转矩(N.m)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值转矩(N.m)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值转矩(N.m)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值转矩(N.m)")
    private Double peakTorque;

    @ColumnHeader(title = "峰值扭矩转速(r/min)", example = "1200", notNull = false, desc = "峰值扭矩转速(r/min)")
    @Digits(integer = 7, fraction = 2, message = "峰值扭矩转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值扭矩转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值扭矩转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值扭矩转速(r/min)")
    private Double peakTorqueSpeed;

    @NotBlank(message = "燃油发电机生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "燃油发电机生产企业")
    private String prodUnitId;

    /** 燃油发电机生产企业名称显示**/
    @ColumnHeader(title = "燃油发电机生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "燃油发电机生产企业")
    @NotBlank(message = "燃油发电机生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "燃油发电机生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "燃油发电机生产企业名称")
    private String prodUnitDisplay;

    @ApiModelProperty(value = "燃油发电机控制器生产企业")
    private String controllerProdUnitId;

    /** 燃油发电机控制器生产企业名称显示**/
    @ColumnHeader(title = "燃油发电机控制器生产企业", example = "海马汽车零配件有限公司", notNull = false, desc = "燃油发电机控制器生产企业")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "燃油发电机控制器生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "controllerProdUnitId",desc = "")
    @ApiModelProperty(value = "燃油发电机控制器生产企业名称")
    private String controllerProdUnitDisplay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
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

    public String getAirInletDisplay() {
        return airInletDisplay;
    }

    public void setAirInletDisplay(String airInletDisplay) {
        this.airInletDisplay = airInletDisplay;
    }

    public String getFuelFormDisplay() {
        return fuelFormDisplay;
    }

    public void setFuelFormDisplay(String fuelFormDisplay) {
        this.fuelFormDisplay = fuelFormDisplay;
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

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static FuelGeneratorModelModel fromEntry(FuelGeneratorModel entry){
        FuelGeneratorModelModel m = new FuelGeneratorModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
