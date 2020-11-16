package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.ForwardRuleItem;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRuleItem新增模型<br>
* 描述： ForwardRuleItem新增模型<br>
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
* <td>2019-02-20 14:59:53</td>
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
@ApiModel(value = "ForwardRuleItemModel", description = "转发规则明细表Model")
public class ForwardRuleItemModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ColumnHeader(title = "归属转发规则ID")
    @NotEmpty(message = "归属转发规则ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "归属转发规则ID")
    private String forwardRuleId;

    @ColumnHeader(title = "数据权限项ID")
    @NotEmpty(message = "数据权限项ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据权限项ID")
    private String resourceItemId;

    @ColumnHeader(title = "操作符(0:等于 1:不等于)")
    @ApiModelProperty(value = "操作符(0:等于 1:不等于)")
    private Integer op;

    @ColumnHeader(title = "与前一条规则逻辑运算符(0:与 1:或)")
    @ApiModelProperty(value = "与前一条规则逻辑运算符(0:与 1:或)")
    private Integer preLogicOp;

    @ColumnHeader(title = "值")
    @ApiModelProperty(value = "值")
    private String val;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 数据权限项名称显示**/
    @LinkName(table = "sys_core_resource_item", column = "name", joinField = "resourceItemId",desc = "")
    @ApiModelProperty(value = "数据权限项名称")
    private String resourceItemDisplay;

    /** 操作符名称显示**/
    @DictName(code = "OPERATOR", joinField = "op")
    @ApiModelProperty(value = "操作符名称")
    private String opDisplay;

    /** 逻辑运算符名称显示**/
    @DictName(code = "LOGICAL_OPERATOR", joinField = "preLogicOp")
    @ApiModelProperty(value = "逻辑运算符名称")
    private String preLogicOpDisplay;

    @ApiModelProperty(value = "值名称")
    private String valName;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardRuleItemModel fromEntry(ForwardRuleItem entry){
        ForwardRuleItemModel m = new ForwardRuleItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
