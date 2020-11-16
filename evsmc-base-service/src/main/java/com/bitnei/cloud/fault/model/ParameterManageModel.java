package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.ParameterManage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ParameterManage新增模型<br>
 * 描述： ParameterManage新增模型<br>
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
 * <td>2019-03-01 09:17:04</td>
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
@ApiModel(value = "ParameterManageModel", description = "报警参数项管理Model")
public class ParameterManageModel extends BaseModel {

    private String id;

    @NotBlank(message = "协议类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "协议类型id, 取dc_rule_type表id")
    private String dcRuleTypeId;

    @ColumnHeader(title = "协议类型", example = "GB_T32960", desc = "输入系统中现有的协议类型名称")
    @NotBlank(message = "协议类型不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "协议类型名称")
    private String dcRuleTypeName;

    @NotBlank(message = "协议数据项不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "协议数据项id 取dc_data_item表id")
    private String dcDataItemId;

    @ColumnHeader(title = "协议数据项", example = "车速", desc = "输入系统已有协议数据项名称")
    @NotBlank(message = "协议数据项不能为空", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "数据项名称")
    @LinkName(table = "dc_data_item", joinField = "dcDataItemId")
    private String dcDataItemName;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数据项编号")
    private String seqNo;

    @ColumnHeader(title = "CODE", example = "123", desc = "CODE字段需为正整数, 长度1~4")
    @Min(value = 0, message = "CODE字段需为正整数, 长度1~4", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 9999, message = "CODE字段需为正整数, 长度1~4", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = "CODE字段不能为空, 需为正整数, 长度1~4", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "CODE, 产品预留字段")
    private Integer code;

    @ColumnHeader(title = "备注", notNull = false, desc = "备注长度为100个字符以内")
    @Length(max = 100, message = "备注过长, 请控制长度为100个字符以内",
        groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "备注")
    private String remark;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static ParameterManageModel fromEntry(ParameterManage entry) {
        ParameterManageModel m = new ParameterManageModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
