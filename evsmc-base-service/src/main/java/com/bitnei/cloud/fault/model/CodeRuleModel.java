package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.CodeRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

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
@ApiModel(value = "CodeRuleModel", description = "故障码规则表Model")
public class CodeRuleModel extends BaseModel {

    private String id;

    @NotBlank(message = "故障种类不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "故障种类ID, fault_code_type表的id")
    private String faultCodeTypeId;

    @NotBlank(message = "故障码种类不能为空", groups = {GroupExcelImport.class})
    @LinkName(table = "fault_code_type", joinField = "faultCodeTypeId")
    @ApiModelProperty(value = "故障码类型名称")
    private String faultCodeTypeName;

    @NotBlank(message = "故障码规则名称不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Length(min = 2, max = 64, message = "故障码规则名称长度为2~64个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "故障码规则名称")
    private String faultName;

    @NotNull(message = "解析方式不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "解析方式, 1=字节， 2＝bit")
    private Integer analyzeType;

    @NotBlank(message = "解析方式不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "解析方式名称")
    @DictName(code = "ANALYZE_TYPE", joinField = "analyzeType")
    private String analyzeTypeDisplay;

    @NotBlank(message = "车辆型号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆公告型号id, 以, 的形式组成的串")
    private String vehModelId;

    @NotBlank(message = "适用车型（车辆公告型号）不能为空", groups = {GroupExcelImport.class})
    private String vehModelName;

    @NotBlank(message = "正常码（无故障状态故障码）不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[0-9a-fA-F]{1,8}$", message = "正常码格式需为:十六进制字符,长度为:1~8个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "正常码（无故障状态故障码）")
    private String normalCode;

    @NotNull(message = " 启用状态 是否有效 1=启用, 0=禁用，不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = " 启用状态 是否有效 1=启用, 0=禁用")
    private Integer enabledStatus;

    @ApiModelProperty(value = "启用状态")
    @DictName(code = "ENABLED_STATUS", joinField = "enabledStatus")
    private String enabledStatusDisplay;

    @ApiModelProperty(value = " 是否启用持续时间 是否有效 1=启用, 0=禁用")
    private Integer enableTimeThreshold;

    @DictName(code = "ENABLED_STATUS", joinField = "enableTimeThreshold")
    private String enableTimeThresholdDesc;

    @ApiModelProperty(value = " 是否启用持续帧数 是否有效 1=启用, 0=禁用")
    private Integer enableCountThreshold;

    @DictName(code = "ENABLED_STATUS", joinField = "enableCountThreshold")
    private String enableCountThresholdDesc;

    @Min(value = 0, message = "起始位需为正整数", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "起始位")
    private Integer startPoint;

    @ApiModelProperty(value = "故障码长度")
    @Min(value = 1, message = "故障码长度不能小于1", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = " 故障码长度不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private Integer exceptionCodeLength;

    @Length(max = 200, message = "故障描述长度为:0~200个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "故障描述")
    private String faultDescribe;

    @Length(max = 200, message = "解决方案长度为:0~200个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "解决方案描述")
    private String solutionDescribe;

    @Min(value = 0, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "开始时间阈值(秒)")
    private Integer beginThreshold;

    @Min(value = 0, message = "结束时间阈值范围为0-3600", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "结束时间阈值范围为0-3600", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "结束时间阈值(秒)")
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

    /**
     * 所属零部件
     **/
    @ApiModelProperty(value = "所属零部件ID")
    private String subordinatePartsId;

    @ApiModelProperty(value = "所属零部件名称")
    @DictName(code = "SUBORDINATE_PARTS", joinField = "subordinatePartsId")
    private String subordinateParts;

    /**
     * 删除状态  1：是；0：否
     */
    private Integer deleteStatus;

    private String createTime;

    private String createBy;

    private String updateTime;

    private String updateBy;

    @Valid
    @NotEmpty(message = "规则数据不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "规则数据")
    private List<CodeRuleItemModel> items;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static CodeRuleModel fromEntry(CodeRule entry) {
        CodeRuleModel m = new CodeRuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
