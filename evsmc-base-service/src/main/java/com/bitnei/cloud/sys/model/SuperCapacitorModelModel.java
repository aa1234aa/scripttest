package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.SuperCapacitorModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SuperCapacitorModel新增模型<br>
* 描述： SuperCapacitorModel新增模型<br>
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
* <td>2018-12-06 23:38:00</td>
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
@ApiModel(value = "SuperCapacitorModelModel", description = "超级电容型号Model")
public class SuperCapacitorModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "超级电容型号", example = "F12345", notNull = true, desc = "超级电容型号")
    @NotBlank(message = "超级电容型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "超级电容型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "超级电容型号")
    private String name;


    @ColumnHeader(title = "额定电压(V)", example = "380", notNull = false, desc = "额定电压(V)")
    @Digits(integer = 10, fraction = 0, message = "额定电压(V)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定电压(V)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "额定电压(V)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定电压(V)")
    private Double fixedVol;

    @ColumnHeader(title = "额定电流(A)", example = "220", notNull = false, desc = "额定电流(A)")
    @Digits(integer = 10, fraction = 0, message = "额定电流(A)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定电流(A)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "额定电流(A)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定电流(A)")
    private Double fixedCurrent;

    @ColumnHeader(title = "总储电量(kW.h)", example = "18650", notNull = true, desc = "总储电量(kW.h)")
    @NotNull(message = "总储电量(kW.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 10, fraction = 0, message = "总储电量(kW.h)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "总储电量(kW.h)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "总储电量(kW.h)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "总储电量(kW.h)")
    private Double totalCapacity;

    @ColumnHeader(title = "能量密度(Wh/kg)", example = "330", notNull = false, desc = "能量密度(Wh/kg)")
    @Digits(integer = 10, fraction = 0, message = "能量密度(Wh/kg)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "能量密度(Wh/kg)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "能量密度(Wh/kg)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "能量密度(Wh/kg)")
    private Double capacityDensity;

    @ColumnHeader(title = "功率密度(W/kg)", example = "290", notNull = false, desc = "功率密度(W/kg)")
    @Digits(integer = 10, fraction = 0, message = "功率密度(W/kg)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "功率密度(W/kg)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "功率密度(W/kg)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "功率密度(W/kg)")
    private Double powerCapacity;

    @NotBlank(message = "超级电容生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "超级电容生产企业")
    private String unitId;

    /** 超级电容生产企业名称显示**/
    @ColumnHeader(title = "超级电容生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "超级电容生产企业")
    @NotBlank(message = "超级电容生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "超级电容生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "超级电容生产企业名称")
    @LinkName(table = "sys_unit", column = "name", joinField = "unitId",desc = "")
    private String unitIdDisplay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    public String getUnitIdDisplay() {
        return unitIdDisplay;
    }

    public void setUnitIdDisplay(String unitIdDisplay) {
        this.unitIdDisplay = unitIdDisplay;
    }

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
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    public Double getFixedVol() {
        return fixedVol;
    }

    public void setFixedVol(Double fixedVol) {
        this.fixedVol = fixedVol;
    }
    public Double getFixedCurrent() {
        return fixedCurrent;
    }

    public void setFixedCurrent(Double fixedCurrent) {
        this.fixedCurrent = fixedCurrent;
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
    public Double getPowerCapacity() {
        return powerCapacity;
    }

    public void setPowerCapacity(Double powerCapacity) {
        this.powerCapacity = powerCapacity;
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

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SuperCapacitorModelModel fromEntry(SuperCapacitorModel entry){
        SuperCapacitorModelModel m = new SuperCapacitorModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
