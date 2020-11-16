package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
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

import javax.validation.constraints.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeRule新增模型<br>
 * 描述： CodeRule新增模型<br>
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
 * <td>2019-02-25 16:55:47</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CodeRuleImportModel", description = "故障码规则导入Model")
public class CodeRuleImportModel extends BaseModel {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "解析方式, 1=字节， 2＝bit")
    private Integer analyzeType;
    @ColumnHeader(title = "解析方式", example = "按bit位解析", desc = "解析方式: 按字节解析，按bit位解析")
    @NotBlank(message = "解析方式不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "解析方式")
    private String analyzeTypeDisplay;

    @ColumnHeader(title = "故障名称", example = "启动故障", desc = "故障名称长度为:2~64个字符")
    @NotBlank(message = "故障码规则名称不能为空", groups = {GroupExcelImport.class})
    @Length(min = 2, max = 64, message = "故障码规则名称长度为2~64个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "故障名称")
    private String faultName;

    @ApiModelProperty(value = "适用车型（车辆型号, 以, 的形式组成的串")
    private String vehModelId;
    @ColumnHeader(title = "适用车型（车辆型号）", example = "CLGGXH001,CLGGXH002", desc = "输入系统已录入车辆型号,多车辆型号用英文逗号(,)分隔")
    @NotBlank(message = "适用车型（车辆型号）不能为空", groups = {GroupExcelImport.class})
    private String vehModelName;
    @ApiModelProperty(value = "故障种类ID, fault_code_type表的id")
    private String faultCodeTypeId;

    @ColumnHeader(title = "故障码种类", example = "自定义(与系统中故障种类匹配)", desc = "输入系统中已有故障种类名称")
    @NotBlank(message = "故障码种类不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "故障码种类名称")
    private String faultCodeTypeName;

    @ColumnHeader(title = "正常状态故障码", example = "0", desc = "正常码格式为:十六进制字符,长度为:1~8个字符")
    @NotBlank(message = "正常码（无故障状态故障码）不能为空", groups = {GroupExcelImport.class})
    @Pattern(regexp = "^[0-9a-fA-F]{1,8}$", message = "正常码格式需为:十六进制字符,长度为:1~8个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "正常码（无故障状态故障码）")
    private String normalCode;

    @ApiModelProperty(value = "是否启用持续时间 是否有效 1=启用, 0=禁用")
    private Integer enableTimeThreshold;

    @NotNull(message = "是否启用持续时间不能为空")
    @ColumnHeader(title = "是否启用持续时间", example = "启用", desc = "输入启用状态: 启用、禁用")
    private String enableTimeThresholdDesc;

    @ColumnHeader(title = "开始持续时间阈值", example = "0", notNull = false)
    @Min(value = 0, message = "开始持续时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "开始持续时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private Integer beginThreshold;

    @ColumnHeader(title = "结束持续时间阈值", example = "0", notNull = false)
    @Min(value = 0, message = "结束持续时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "结束持续时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private Integer endThreshold;

    @ApiModelProperty(value = "是否启用持续帧数 是否有效 1=启用, 0=禁用")
    private Integer enableCountThreshold;

    @NotNull(message = "是否启用持续帧数不能为空")
    @ColumnHeader(title = "是否启用持续帧数", example = "启用", desc = "输入启用状态: 启用、禁用")
    private String enableCountThresholdDesc;

    @ColumnHeader(title = "开始故障码连续帧数(单位:帧)", example = "60", desc = "开始故障值连续帧数需为正整数", notNull = false)
    @Min(value = 1, message = "开始故障码连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 5000, message = "开始故障码连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "开始故障码连续帧数帧数(单位:帧)")
    private Integer beginCountThreshold;

    @ColumnHeader(title = "结束正常值连续帧数(单位:帧)", example = "60", desc = "结束正常值连续帧数需为正整数", notNull = false)
    @Min(value = 1, message = "结束正常值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 5000, message = "结束正常值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "结束正常值连续帧数(单位:帧)")
    private Integer endCountThreshold;

    @ApiModelProperty(value = " 启用状态 是否有效 1=启用, 0=禁用")
    private Integer enabledStatus;

    @ColumnHeader(title = "启用状态", example = "启用", desc = "输入启用状态: 启用、禁用")
    @NotBlank(message = "启用状态不允许为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "启用状态")
    private String enabledStatusDisplay;

    @ColumnHeader(title = "故障描述", example = "故障描述", notNull = false, desc = "故障描述长度为:0~200个字符")
    @Length(max = 200, message = "故障描述长度为:0~200个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "故障描述")
    private String faultDescribe;

    @ColumnHeader(title = "解决方案", example = "步骤1,2,3", notNull = false, desc = "解决方案长度为:0~200个字符")
    @Length(max = 200, message = "解决方案长度为:0~200个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "解决方案描述")
    private String solutionDescribe;

    @ColumnHeader(title = "起始位", example = "1", desc = "故障码处在一个故障种类中的起始位置")
    @Min(value = 0, message = "起始位需要为正整数", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "起始位")
    private Integer startPoint;

    @ColumnHeader(title = "故障码", example = "1", desc = "1.同一个故障种类下，故障码不允许重复; 2.十六进制，无需填写0x; 3.字符长度1~8个字符; 4.按bit为解析输入故障码数值限定0或1;")
    @NotBlank(message = "故障码不能为空", groups = {GroupExcelImport.class})
    @Pattern(regexp = "^[0-9a-fA-F]{1,8}$", message = "故障码格式为:十六进制字符,长度为:1~8个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "故障码")
    private String exceptionCode;

    @ColumnHeader(title = "故障码长度", example = "1", desc = "故障码长度为必填项, 长度为1-4")
    @NotNull(message = "故障码长度不允许为空", groups = {GroupExcelImport.class})
    @Pattern(regexp = "\\d{1,4}", message = "按位解析时故障码长度为1-4")
    @ApiModelProperty(value = "故障码长度")
    private Integer exceptionCodeLength;

    @ApiModelProperty(value = "报警级别;  0=无、1=1级、2=2级、3=3级")
    private Integer alarmLevel;
    @ColumnHeader(title = "报警级别", example = "一级", desc = "报警级别: 无、一级、二级、三级")
    @NotBlank(message = "报警级别不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "报警级别")
    private String alarmLevelDisplay;

    @ApiModelProperty(value = "响应方式：0=无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知")
    private String responseMode;
    @ColumnHeader(title = "响应方式", example = "系统弹窗,声音提醒", desc = "响应方式：无、系统弹窗、声音提醒、APP弹窗提醒、短信通知; 多个使用英文逗号(,)分隔")
    @NotBlank(message = "响应方式不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "响应方式")
    private String responseModeDisplay;

    @ColumnHeader(title = "所属零部件名称", example = "通用报警")
    @NotBlank(message = "所属零部件不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "所属零部件名称")
    private String subordinateParts;
    private String subordinatePartsId;
}
