package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.DataItemGroup;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemGroup新增模型<br>
* 描述： DataItemGroup新增模型<br>
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
* <td>2019-01-30 11:25:14</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DataItemGroupModel", description = "数据项类型Model")
public class DataItemGroupModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "类型名称")
    @NotBlank(message = "类型名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,45}|$", message = "类型名称长度为2-45个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "类型名称")
    private String name;

    @ColumnHeader(title = "协议类型ID")
    @NotBlank(message = "协议类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议类型ID")
    private String ruleTypeId;

    @ColumnHeader(title = "类型编码")
    @NotBlank(message = "类型编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[a-zA-Z0-9]{1,20}|$", message = "类型编码长度1-20个英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "类型编码")
    private String code;

    @ColumnHeader(title = "备注")
    @Pattern(regexp = "^[\\s\\S]{0,100}|$", message = "备注长度为0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


    /** 协议类型名称显示**/
    @LinkName(table = "dc_rule_type", column = "name", joinField = "ruleTypeId",desc = "")
    @ApiModelProperty(value = "协议类型名称")
    private String ruleTypeDisplay;

    @ColumnHeader(title = "上一级")
    @ApiModelProperty(value = "上一级")
    private String parentId;
    /** 上一级名称显示 **/
    @LinkName(table = "dc_data_item_group", column = "name", joinField = "parentId", desc = "上一级名称")
    private String parentName;

    @ColumnHeader(title = "树路径")
    @ApiModelProperty(value = "树路径")
    private String path;

    @ColumnHeader(title = "路径名称")
    @ApiModelProperty(value = "路径名称")
    private String pathName;

    @ApiModelProperty(value = "端口号")
    private String port;

    @ApiModelProperty(value = "是否国标: 0：否 1：是")
    private Integer isNationalStandard;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataItemGroupModel fromEntry(DataItemGroup entry){
        DataItemGroupModel m = new DataItemGroupModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
