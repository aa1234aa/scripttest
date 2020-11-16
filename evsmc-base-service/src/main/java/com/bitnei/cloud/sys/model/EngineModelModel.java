package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseExtendModel;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

import com.bitnei.cloud.sys.domain.EngineModel;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EngineModel新增模型<br>
* 描述： EngineModel新增模型<br>
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
* <td>2018-12-03 11:16:29</td>
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "EngineModelModel", description = "发动机型号Model")
@ExtendTable(table = "sys_engine_model")
public class EngineModelModel extends BaseExtendModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "发动机型号", example = "EM001", notNull = true, desc = "发动机型号")
    @NotBlank(message = "发动机型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "发动机型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发动机型号")
    private String name;

    @ApiModelProperty(value = "进气方式")
    @NotNull(message = "进气方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer intakeWay;

    /** 发动机进气方式名称显示**/
    @ColumnHeader(title = "进气方式", example = "自然吸气", notNull = true, desc = "进气方式:自然吸气，涡轮增压，机械增压")
    @NotBlank(message = "进气方式不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "进气方式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "DRIVE_INTAKE_MODE", joinField = "intakeWay")
    @ApiModelProperty(value = "发动机进气方式名称")
    private String intakeWayDisplay;

    @NotNull(message = "循环方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "循环方式")
    private Integer circulationWay;

    /** 循环方式名称显示**/
    @ColumnHeader(title = "循环方式", example = "奥托循环", notNull = true, desc = "循环方式:奥托循环、阿特金森循环、狄塞尔循环、米勒循环")
    @NotBlank(message = "循环方式不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "循环方式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "DRIVE_CYCLE_MODE", joinField = "circulationWay")
    @ApiModelProperty(value = "循环方式名称")
    private String circulationWayDisplay;

    @NotNull(message = "燃料形式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "燃料形式")
    private Integer fuelForm;

    /** 燃料形式名称显示**/
    @ColumnHeader(title = "燃料形式", example = "汽油", notNull = true, desc = "燃料形式:汽油，柴油，天然气")
    @NotBlank(message = "燃料形式不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "燃料形式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "FUEL_FORM", joinField = "fuelForm")
    @ApiModelProperty(value = "燃料形式名称")
    private String fuelFormDisplay;

    @ColumnHeader(title = "油箱容量(L)", example = "100", notNull = false, desc = "油箱容量(L)")
    @Digits(integer = 4, fraction = 2, message = "油箱容量(L)整数长度1~4位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "油箱容量(L)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "9999", message = "油箱容量(L)必须是一个数字，其值必须小于等于9999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "油箱容量(L)")
    private Double tankCapacity;

    @ColumnHeader(title = "储气罐个数(个)", example = "10", notNull = false, desc = "储气罐个数(个)")
    @Digits(integer = 3, fraction = 0, message = "储气罐个数(个)整数长度1~3位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "储气罐个数(个)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "999", message = "储气罐个数(个)必须是一个数字，其值必须小于等于999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "储气罐个数(个)")
    private Integer gasholderNumber;

    @ApiModelProperty(value = "储气罐类型")
    private Integer gasholderType;

    /** 储气罐类型名称显示**/
    @ColumnHeader(title = "储气罐类型", example = "液化气", notNull = false, desc = "储气罐类型:液化气、天然气")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "储气罐类型长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "GPS_CONTAINER_TYPE", joinField = "gasholderType")
    @ApiModelProperty(value = "储气罐类型名称")
    private String gasholderTypeDisplay;

    @ColumnHeader(title = "储气罐容量", example = "100", notNull = false, desc = "储气罐容量")
    @Digits(integer = 3, fraction = 0, message = "储气罐容量整数长度1~3位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "储气罐容量必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "999", message = "储气罐容量必须是一个数字，其值必须小于等于999", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "储气罐容量")
    private Double gasholderCapacity;

    @ColumnHeader(title = "品牌名称", example = "B0001", notNull = false, desc = "品牌名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,20}|$", message = "品牌名称长度4-20个中英文、数字", groups = {GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ColumnHeader(title = "发动机控制器型号", example = "C0001", notNull = false, desc = "发动机控制器型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "发动机控制器型号长度2-50个中英文、数字", groups = {GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "发动机控制器型号")
    private String controllerModel;

    @ColumnHeader(title = "峰值功率(KW)", example = "9999", notNull = true, desc = "峰值功率(KW)")
    /** @NotNull(message = "峰值功率(KW)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class}) **/
    @Digits(integer = 7, fraction = 2, message = "峰值功率(KW)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率(KW)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率(KW)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率(KW)")
    private Double peakPower;

    @ColumnHeader(title = "峰值功率转速(r/min)", example = "9999", notNull = true, desc = "峰值功率转速(r/min)")
    /** @NotNull(message = "峰值功率转速(r/min)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class}) **/
    @Digits(integer = 7, fraction = 2, message = "峰值功率转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值功率转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值功率转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值功率转速(r/min)")
    private Double peakRotateSpeed;

    @ColumnHeader(title = "峰值转矩(N.m)", example = "999", notNull = true, desc = "峰值转矩(N.m)")
    /** @NotNull(message = "峰值转矩(N.m)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class}) **/
    @Digits(integer = 7, fraction = 2, message = "峰值转矩(N.m)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值转矩(N.m)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值转矩(N.m)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值转矩(N.m)")
    private Double peakTorque;

    @ColumnHeader(title = "峰值扭矩转速(r/min)", example = "9999", notNull = true, desc = "峰值扭矩转速(r/min)")
    /** @NotNull(message = "峰值扭矩转速(r/min)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class}) **/
    @Digits(integer = 7, fraction = 2, message = "峰值扭矩转速(r/min)整数长度1~7位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "峰值扭矩转速(r/min)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @DecimalMax(value = "1000000", message = "峰值扭矩转速(r/min)必须是一个数字，其值必须小于等于1000000", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "峰值扭矩转速(r/min)")
    private Double peakTorqueRotateSpeed;

    @NotBlank(message = "发动机生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "发动机生产企业")
    private String prodUnitId;

    /** 发动机生产企业名称显示**/
    @ColumnHeader(title = "发动机生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "发动机生产企业")
    @NotBlank(message = "发动机生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "发动机生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "prodUnitId",desc = "")
    @ApiModelProperty(value = "发动机生产企业名称")
    private String prodUnitDisplay;

    @LinkName(table = "sys_unit", column = "organization_code", joinField = "prodUnitId")
    @ApiModelProperty(value = "发动机生产企业社会信用码")
    private String prodUnitOrgCode;

    @ApiModelProperty(value = "发动机控制器生产企业")
    private String controllerProdUnitId;

    /** 发动机控制器生产企业名称显示**/
    @ColumnHeader(title = "发动机控制器生产企业", example = "海马汽车零配件有限公司", notNull = false, desc = "发动机控制器生产企业")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "发动机控制器生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "controllerProdUnitId",desc = "")
    @ApiModelProperty(value = "发动机控制器生产企业名称")
    private String controllerProdUnitDisplay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "OBD型号")
    private String obdModel;
    /** OBD型号名称显示**/
    @LinkName(table = "sys_term_model", column = "term_model_name", joinField = "obdModel",desc = "")
    @ApiModelProperty(value = "OBD型号名称")
    private String obdModelDisplay;

    @ApiModelProperty(value = "OBD生产厂")
    private String obdUnitId;
    /** OBD生产厂名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "obdUnitId",desc = "")
    @ApiModelProperty(value = "OBD生产厂名称")
    private String obdUnitDisplay;

    @ApiModelProperty(value = "接口照片")
    private String interfacePhoto;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static EngineModelModel fromEntry(EngineModel entry){
        EngineModelModel m = new EngineModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
