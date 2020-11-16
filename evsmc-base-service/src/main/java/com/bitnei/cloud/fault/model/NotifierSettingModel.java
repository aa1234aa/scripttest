package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.NotifierSetting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierSetting新增模型<br>
 * 描述： NotifierSetting新增模型<br>
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
 * <td>2019-03-06 11:31:31</td>
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
@ApiModel(value = "NotifierSettingModel", description = "报警通知人设置Model")
public class NotifierSettingModel extends BaseModel {

    private String id;

    @ColumnHeader(title = "姓名", example = "张三", desc = "姓名长度为:2~20个字符")
    @NotBlank(message = "姓名不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Length(min = 2, max = 20, message = "姓名长度为2~20个字符", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "姓名")
    private String name;

    @ColumnHeader(title = "手机号", example = "13500000000", desc = "请输入11位数的手机号")
    @NotBlank(message = "电话不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[0-9]{11}$", message = "请输入11位数的手机号",
        groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "区域id, 多条以,分开 如 value1,value2")
    private String areaIds;

    @ColumnHeader(title = "责任区域", example = "广东省,珠海市", notNull = false, desc = "非必填, 可输入行政区域名称, 多个用英文逗号(,)分隔")
    @ApiModelProperty(value = "区域名称")
    private String areaNames;

    @NotBlank(message = "故障类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "故障类型")
    private String faultType;

    @ColumnHeader(title = "推送消息类型", example = "参数", desc = "推送消息类型: 参数, 故障码, 围栏, 安全风险等级; 多个用英文逗号(,)分隔")
    @NotBlank(message = "推送消息类型不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "故障类型名称")
    private String faultTypeDisplay;

    @ColumnHeader(title = "是否关联所有故障码报警规则", notNull = false, example = "是", desc = "是否全部: 是=关联所有规则, 否=暂不关联规则,可在导入成功后手动选择添加指定规则, 缺省默认为'否'; 注意: '推送消息类型'中需包含了'故障码'类型此设置才生效")
    @ApiModelProperty(value = "故障类型名称")
    private String codeRuleDisplay;

    @ColumnHeader(title = "是否关联所有参数异常报警规则名称", notNull = false, example = "是", desc = "是否全部: 是=关联所有规则, 否=暂不关联规则,可在导入成功后手动选择添加指定规则, 缺省默认为'否'; 注意: '推送消息类型'中需包含了'参数'类型此设置才生效")
    @ApiModelProperty(value = "参数异常报警规则名称")
    private String parameterRuleDisplay;

    @ColumnHeader(title = "是否关联所有电子围栏报警名称", notNull = false, example = "是", desc = "是否全部: 是=关联所有规则, 否=暂不关联规则,可在导入成功后手动选择添加指定规则, 缺省默认为'否'; 注意: '推送消息类型'中需包含了'围栏'类型此设置才生效")
    @ApiModelProperty(value = "电子围栏报警名称")
    private String enclosureRuleDisplay;

    @Deprecated
    @ApiModelProperty(value = "故障等级, v2.4已弃用此字段")
    private String alarmLevel;
    @Deprecated
    @ApiModelProperty(value = "故障等级, v2.4已弃用此字段")
    private String alarmLevelDisplay;

    @NotNull(message = "是否关联平台账号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否关联平台账号")
    private Integer relationUserStatus;

    @ColumnHeader(title = "是否关联平台账号", example = "是")
    @NotNull(message = "是否关联平台账号不能为空", groups = {GroupExcelImport.class})
    @DictName(code = "BOOL_TYPE", joinField = "relationUserStatus")
    @ApiModelProperty(value = "是否关联平台账号")
    private String relationUserDisplay;

    @ApiModelProperty(value = "平台账号id")
    private String userId;

    @ColumnHeader(title = "平台账号", example = "admin", desc = "输入平台现有账号")
    @ApiModelProperty(value = "平台账号")
    @LinkName(table = "sys_user", column = "username", joinField = "userId")
    private String userName;

    @ApiModelProperty(value = "是否同步账号下车辆")
    private Integer syncVehicleStatus;

    @ColumnHeader(title = "是否同步账号下车辆", example = "是")
    @NotNull(message = "是否同步账号下车辆不能为空", groups = {GroupExcelImport.class})
    @DictName(code = "BOOL_TYPE", joinField = "syncVehicleStatus")
    @ApiModelProperty(value = "是否同步账号下车辆")
    private String syncVehicleDisplay;

    @NotNull(message = "是否启用不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否启用")
    private Integer enabledStatus;

    @ColumnHeader(title = "是否启用", example = "是")
    @NotNull(message = "是否启用不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "启用状态")
    @DictName(code = "BOOL_TYPE", joinField = "enabledStatus")
    private String enabledStatusDisplay;

    @ColumnHeader(title = "备注", notNull = false)
    @ApiModelProperty(value = "备注")
    private String remark;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    /** 分配的人数 **/
    @ApiModelProperty(value = "分配车辆数")
    private int allocateVehicleCount;

    @ApiModelProperty(value = "关联故障码规则集合")
    private List<NotifierRuleLkModel> codeRuleList;

    @ApiModelProperty(value = "关联参数规则集合")
    private List<NotifierRuleLkModel> parameterRuleList;

    @ApiModelProperty(value = "关联围栏规则集合")
    private List<NotifierRuleLkModel> enclosureRuleList;


    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static NotifierSettingModel fromEntry(NotifierSetting entry) {
        if (null == entry) {
            return null;
        }
        NotifierSettingModel m = new NotifierSettingModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
