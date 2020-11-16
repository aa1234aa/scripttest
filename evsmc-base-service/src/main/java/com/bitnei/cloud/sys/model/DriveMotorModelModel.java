package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.DriveMotorModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DriveMotorModel新增模型<br>
* 描述： DriveMotorModel新增模型<br>
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
* <td>2018-12-03 11:12:12</td>
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
@Setter
@Getter
@ApiModel(value = "DriveMotorModelModel", description = "驱动电机型号Model")
public class DriveMotorModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "驱动电机型号", example = "DM001", notNull = true, desc = "驱动电机型号")
    @NotBlank(message = "驱动电机型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "驱动电机型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "型号名称")
    private String name;

    @NotNull(message = "驱动电机种类不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "驱动电机种类")
    private Integer driveType;

    /** 驱动电机种类名称显示**/
    @ColumnHeader(title = "驱动电机种类", example = "永磁同步", notNull = true, desc = "驱动电机种类:永磁同步,交流异步,无刷直流,开关磁阻")
    @DictName(code = "DRIVE_DEVICE_TYPE", joinField = "driveType")
    @NotBlank(message = "驱动电机种类不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "驱动电机种类长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "驱动电机种类名称")
    private String driveTypeDisplay;

    @ApiModelProperty(value = "驱动电机冷却方式")
    private Integer driveColdMode;

    /** 驱动电机冷却方式名称显示**/
    @ColumnHeader(title = "驱动电机冷却方式", example = "液冷", notNull = false, desc = "驱动电机冷却方式:液冷,油冷,风冷")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "驱动电机冷却方式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "DRIVE_COLD_MODE", joinField = "driveColdMode")
    @ApiModelProperty(value = "驱动电机冷却方式名称")
    private String driveColdModeDisplay;

    @ColumnHeader(title = "额定电压(V)", example = "220", notNull = false, desc = "额定电压(V)")
    @Digits(integer = 7, fraction = 2, message = "额定电压(V)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定电压(V)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "额定电压(V)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定电压(V)")
    private Double ratedVoltage;

    @ColumnHeader(title = "驱动电机最大工作电流(A)", example = "100", notNull = false, desc = "驱动电机最大工作电流(A)")
    @Digits(integer = 7, fraction = 2, message = "最大工作电流(A)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "驱动电机最大工作电流(A)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "最大工作电流(A)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "最大工作电流(A)")
    private Double maxElectricity;

    @ColumnHeader(title = "驱动电机控制器型号", example = "C0001", notNull = false, desc = "驱动电机控制器型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,30}|$", message = "驱动电机控制器型号长度2-30个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "驱动电机控制器型号")
    private String controllerModel;

    @ColumnHeader(title = "有效比功率(KW/kg)", example = "10", notNull = true, desc = "有效比功率(KW/kg)")
    @NotNull(message = "有效比功率(KW/kg)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "有效比功率(KW/kg)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "有效比功率(KW/kg)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "有效比功率(KW/kg)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "有效比功率(KW/kg)")
    private Double validPower;

    @ColumnHeader(title = "额定功率(KW)", example = "1000", notNull = false, desc = "额定功率(KW)")
    @Digits(integer = 7, fraction = 2, message = "额定功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "额定功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定功率(KW)")
    private Double ratedPower;

    @ColumnHeader(title = "峰值功率(KW)", example = "9999", notNull = true, desc = "峰值功率(KW)")
    @NotNull(message = "峰值功率(KW)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "峰值功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    @ColumnHeader(title = "额定转速(r/min)", example = "1000", notNull = false, desc = "额定转速(r/min)")
    @Digits(integer = 7, fraction = 2, message = "额定转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "额定转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定转速(r/min)")
    private Double ratedRotateSpeed;

    @ColumnHeader(title = "峰值转速(r/min)", example = "9999", notNull = true, desc = "峰值转速(r/min)")
    @NotNull(message = "峰值转速(r/min)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "峰值转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值转速(r/min)")
    private Double peakRotateSpeed;

    @ColumnHeader(title = "额定转矩(N.m)", example = "1000", notNull = false, desc = "额定转矩(N.m)")
    @Digits(integer = 7, fraction = 2, message = "额定转矩(N.m)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定转矩(N.m)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "额定转矩(N.m)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "额定转矩(N.m)")
    private Double ratedTorque;

    @ColumnHeader(title = "峰值转矩(N.m)", example = "9999", notNull = true, desc = "峰值转矩(N.m)")
    @NotNull(message = "峰值转矩(N.m)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 7, fraction = 2, message = "峰值转矩(N.m)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值转矩(N.m)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值转矩(N.m)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值转矩(N.m)")
    private Double peakTorque;

    @ColumnHeader(title = "驱动电机最大输出转矩(N.m)", example = "9999", notNull = false, desc = "驱动电机最大输出转矩(N.m)")
    @Digits(integer = 7, fraction = 2, message = "驱动电机最大输出转矩(N.m)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "驱动电机最大输出转矩(N.m)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "驱动电机最大输出转矩(N.m)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "驱动电机最大输出转矩(N.m)")
    private Double maxOutputTorque;

    @ColumnHeader(title = "驱动电机最高转速(r/min)", example = "9999", notNull = false, desc = "驱动电机最高转速(r/min)")
    @Digits(integer = 7, fraction = 2, message = "驱动电机最高转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "驱动电机最高转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "驱动电机最高转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "驱动电机最高转速(r/min)")
    private Double maxRotateSpeed;

    @NotBlank(message = "驱动电机生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "驱动电机生产企业")
    private String prodUnitId;

    /** 驱动电机生产企业名称显示**/
    @ColumnHeader(title = "驱动电机生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "驱动电机生产企业")
    @NotBlank(message = "驱动电机生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "驱动电机生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "驱动电机生产企业名称")
    private String prodUnitDisplay;

    @ApiModelProperty(value = "驱动电机控制器生产企业")
    private String controllerProdUnitId;

    /** 驱动电机控制器生产企业名称显示**/
    @ColumnHeader(title = "驱动电机控制器生产企业", example = "海马汽车零配件有限公司", notNull = false, desc = "驱动电机控制器生产企业")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "驱动电机控制器生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "controllerProdUnitId",desc = "")
    @ApiModelProperty(value = "驱动电机控制器生产企业名称")
    private String controllerProdUnitDisplay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "安装位置(1:前置 2:后置 3:轮边 4:轮毂内)")
    private String installPosition;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DriveMotorModelModel fromEntry(DriveMotorModel entry){
        DriveMotorModelModel m = new DriveMotorModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
