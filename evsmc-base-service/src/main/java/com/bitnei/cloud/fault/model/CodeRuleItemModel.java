package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.CodeRuleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleItem新增模型<br>
* 描述： CodeRuleItem新增模型<br>
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
* <td>2019-02-25 18:10:28</td>
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
@ApiModel(value = "CodeRuleItemModel", description = "故障码规则项Model")
public class CodeRuleItemModel extends BaseModel {

    @ApiModelProperty(value = "主键, 修改时不能为空")
    private String id;

    @ColumnHeader(title = "故障规则主表id")
    @ApiModelProperty(value = "故障规则主表id")
    private String faultCodeRuleId;

    @ColumnHeader(title = "故障码")
    @NotBlank(message = "故障码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[0-9a-fA-F]{1,8}$",message="故障码格式为:十六进制字符,长度为:1~8个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "故障码")
    private String exceptionCode;

    @ColumnHeader(title = "是否产生告警 1=是, 0=否")
    @ApiModelProperty(value = "是否产生告警 1=是, 0=否")
    private Integer produceAlarm;

    @ApiModelProperty(value = "是否产生告警")
    @DictName(code = "BOOL_TYPE", joinField = "produceAlarm")
    private String produceAlarmDisplay;

    @ColumnHeader(title = "报警级别;  0=无、1=1级、2=2级、3=3级")
    @NotNull(message = "报警级别;  0=无、1=1级、2=2级、3=3级不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报警级别;  0=无、1=1级、2=2级、3=3级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警级别")
    @DictName(code = "ALARM_LEVEL", joinField = "alarmLevel")
    private String alarmLevelDisplay;

    @ColumnHeader(title = "响应方式：0=无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知")
    @NotNull(message = "响应方式：0=无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应方式：0=无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知")
    private String responseMode;

    @ApiModelProperty(value = "响应方式")
    private String responseModeDisplay;

    @ColumnHeader(title = "删除状态  1：是；0：否")
    @ApiModelProperty(value = "删除状态  1：是；0：否")
    private Integer deleteStatus;


    @ApiModelProperty(value = "故障码规则名称")
    private String faultName;

    @ApiModelProperty(value = "解析方式, 1=字节， 2＝bit")
    private Integer analyzeType;

    @ApiModelProperty(value = "起始位")
    private Integer startPoint;

    @ApiModelProperty(value = "正常码（无故障状态故障码）")
    private String normalCode;

    @ApiModelProperty(value = "车辆公告型号id, 以, 的形式组成的串")
    private String vehModelId;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CodeRuleItemModel fromEntry(CodeRuleItem entry){
        CodeRuleItemModel m = new CodeRuleItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
