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

import com.bitnei.cloud.sys.domain.FuelSystemModel;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： FuelSystemModel新增模型<br>
* 描述： FuelSystemModel新增模型<br>
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
@ApiModel(value = "FuelSystemModelModel", description = "燃料电池系统型号Model")
public class FuelSystemModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "燃料电池系统型号", example = "FS001", notNull = true, desc = "燃料电池系统型号")
    @NotBlank(message = "燃料电池系统型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "燃料电池系统型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "燃料电池系统型号")
    private String name;

    @ColumnHeader(title = "燃料电池系统控制器型号", example = "FC001", notNull = false, desc = "燃料电池系统控制器型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "燃料电池系统控制器型号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "燃料电池系统控制器型号")
    private String controllerModel;

    @ColumnHeader(title = "额定功率(KW)", example = "1000", notNull = false, desc = "额定功率(KW)")
    @Digits(integer = 7, fraction = 2, message = "额定功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "额定功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定功率(KW)")
    private Double ratedPower;

    @ColumnHeader(title = "峰值功率(KW)", example = "1000", notNull = false, desc = "峰值功率(KW)")
    @Digits(integer = 7, fraction = 2, message = "峰值功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    @ColumnHeader(title = "体积功率密度(kw/L)", example = "90", notNull = false, desc = "体积功率密度(kw/L)")
    @Digits(integer = 7, fraction = 2, message = "体积功率密度(kw/L)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "体积功率密度(kw/L)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "体积功率密度(kw/L)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "体积功率密度(kw/L)")
    private Double volumePowerDensity;

    @ColumnHeader(title = "燃料电池升压器电高输出电压(V)", example = "220", notNull = false, desc = "燃料电池升压器电高输出电压(V)")
    @Digits(integer = 7, fraction = 2, message = "燃料电池升压器电高输出电压(V)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "燃料电池升压器电高输出电压(V)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "燃料电池升压器电高输出电压(V)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "燃料电池升压器电高输出电压(V)")
    private Double maxOutputVoltage;

    @ColumnHeader(title = "储氢罐个数(个)", example = "10", notNull = false, desc = "储氢罐个数(个)")
    @Digits(integer = 3, fraction = 0, message = "储氢罐个数(个)整数长度1~3位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "储氢罐个数(个)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "999", message = "储氢罐个数(个)必须是一个数字，其值必须小于等于999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "储氢罐个数")
    private Integer hydrogenStorageNumber;

    @ColumnHeader(title = "储氢罐容量(L)", example = "100", notNull = true, desc = "储氢罐容量(L)")
    @NotNull(message = "储氢罐容量(L)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 4, fraction = 2, message = "储氢罐容量(L)整数长度1~4位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "储氢罐容量(L)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999", message = "储氢罐容量(L)必须是一个数字，其值必须小于等于9999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "储氢罐容量(L)")
    private Double hydrogenStorageCapacity;

    @NotBlank(message = "燃料电池系统生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "燃料电池系统生产企业")
    private String prodUnitId;

    /** 燃料电池系统生产企业名称显示**/
    @ColumnHeader(title = "燃料电池系统生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "燃料电池系统生产企业")
    @NotBlank(message = "燃料电池系统生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "燃料电池系统生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "燃料电池系统生产企业名称")
    private String prodUnitDisplay;

    @ApiModelProperty(value = "燃料电池系统控制器生产企业")
    private String controllerProdUnitId;

    /** 燃料电池系统控制器生产企业名称显示**/
    @ColumnHeader(title = "燃料电池系统控制器生产企业", example = "海马汽车零配件有限公司", notNull = false, desc = "燃料电池系统控制器生产企业")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "燃料电池系统控制器生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "controllerProdUnitId",desc = "")
    @ApiModelProperty(value = "燃料电池系统控制器生产企业名称")
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
    public static FuelSystemModelModel fromEntry(FuelSystemModel entry){
        FuelSystemModelModel m = new FuelSystemModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
