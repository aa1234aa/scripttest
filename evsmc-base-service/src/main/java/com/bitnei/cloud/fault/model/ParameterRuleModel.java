package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.ParameterRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ParameterRule新增模型<br>
* 描述： ParameterRule新增模型<br>
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
* <td>2019-02-27 16:35:01</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ParameterRuleModel", description = "平台规则Model")
public class ParameterRuleModel extends BaseModel {

    private String id;

    @ColumnHeader(title = "规则名称")
    @NotBlank(message = "规则名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 2, max = 64, message = "规则名称长度为2~64个字符", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "规则名称")
    private String name;

    @ColumnHeader(title = "适用车辆型号")
    @NotBlank(message = "适用车辆型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "适用车辆型号, all 为全部")
    private String vehModelId;

    @ColumnHeader(title = "适用车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "所属零部件")
    private String subordinatePartsId;

    @ColumnHeader(title = "所属零部件")
    @ApiModelProperty(value = "所属零部件")
    @DictName(code = "SUBORDINATE_PARTS", joinField = "subordinatePartsId")
    private String subordinateParts;

    @ColumnHeader(title = "是否产生报警 1：是；0：否；默认：是")
    @ApiModelProperty(value = "是否产生报警 1：是；0：否；默认：是")
    private Integer produceAlarm;

    @ColumnHeader(title = "是否产生报警")
    @ApiModelProperty(value = "是否产生报警")
    @DictName(code = "BOOL_TYPE", joinField = "produceAlarm")
    private String produceAlarmDisplay;

    @ColumnHeader(title = "报警级别 0：无、1：1级、2：2级、3：3级")
    @NotNull(message = "0：无、1：1级、2：2级、3：3级不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "0：无、1：1级、2：2级、3：3级")
    private Integer alarmLevel;

    @ColumnHeader(title = "报警级别")
    @ApiModelProperty(value = "报警级别")
    @DictName(code = "ALARM_LEVEL", joinField = "alarmLevel")
    private String alarmLevelDisplay;

    @ColumnHeader(title = "响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知")
    @NotNull(message = "响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知; 以,隔开")
    private String responseMode;

    @ColumnHeader(title = "响应方式")
    @ApiModelProperty(value = "响应方式")
    private String responseModeDisplay;

    @ColumnHeader(title = "级别描述")
    @Pattern(regexp="^[\\u4e00-\\u9fa5a-zA-Z0-9\\(\\)\\/\\-]{0,20}|$",message="级别描述格式为:中英文及数字组成,仅允许特殊字符：“()/-”,长度为:0~20个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "级别描述")
    private String levelDescribe;

    @ColumnHeader(title = "开始时间")
    @Min(value = 0, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private Integer beginThreshold;

    @ColumnHeader(title = "结束时间阈值")
    @Min(value = 0, message = "结束时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "结束时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private Integer endThreshold;

    @Min(value = 1, message = "开始故障值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 5000, message = "开始故障值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "开始故障值连续帧数(帧)")
    @NotNull(message = "开始故障值连续帧数不能为空")
    private Integer beginCountThreshold;

    @Min(value = 1, message = "结束正常值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 5000, message = "结束正常值连续帧数范围为1-5000", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "结束正常值连续帧数(帧)")
    @NotNull(message = "结束正常值连续帧数不能为空")
    private Integer endCountThreshold;

    @ColumnHeader(title = " 启用状态 是否有效 1=启用, 0=禁用")
    @NotNull(message = " 启用状态 是否有效 1=启用, 0=禁用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = " 启用状态 是否有效 1=启用, 0=禁用")
    private Integer enabledStatus;

    @ColumnHeader(title = "启用状态")
    @ApiModelProperty(value = "启用状态")
    @DictName(code = "BOOL_TYPE", joinField = "enabledStatus")
    private String enabledStatusDisplay;

    @NotNull(message = "是否启用持续时间不能为空")
    @ApiModelProperty(value = " 是否启用持续时间 是否有效 1=启用, 0=禁用")
    private Integer enableTimeThreshold;

    @DictName(code = "ENABLED_STATUS", joinField = "enableTimeThreshold")
    private String enableTimeThresholdDesc;

    @NotNull(message = "是否启用持续帧数不能为空")
    @ApiModelProperty(value = " 是否启用持续帧数 是否有效 1=启用, 0=禁用")
    private Integer enableCountThreshold;

    @DictName(code = "ENABLED_STATUS", joinField = "enableCountThreshold")
    private String enableCountThresholdDesc;

    @NotBlank(message = "公式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 0, max = 100, message = "公式长度为:0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "公式")
    private String formula;

    @ColumnHeader(title = "规则公式")
    @ApiModelProperty(value = "规则公式中文显示字段")
    private String formulaDisplay;

    @ApiModelProperty(value = "公式代码,方便前端处理")
    private String formulaCode;

    @ApiModelProperty(value = "公式中文, 方便前端处理")
    private String formulaChinese;

    @ColumnHeader(title = "参数异常描述")
    @Length(max = 200, message = "参数异常描述长度为:0~200个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "参数异常描述")
    private String dataConstDescribe;

    @ColumnHeader(title = "解决方案")
    @Length(max = 200, message = "解决方案长度为:0~200个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "解决方案")
    private String solutionDescribe;

    @NotBlank(message = "协议类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "协议类型id, 取dc_rule_type表id")
    private String dcRuleTypeId;

    /**
     * 删0：否
     */
    private Integer deleteStatus;

    private String createTime;

    private String createBy;

    private String updateTime;

    private String updateBy;

    @ApiModelProperty(value = "是否预设通用报警规则: 1=是, 0=否")
    private Integer presetRule;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ParameterRuleModel fromEntry(ParameterRule entry){
        ParameterRuleModel m = new ParameterRuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
