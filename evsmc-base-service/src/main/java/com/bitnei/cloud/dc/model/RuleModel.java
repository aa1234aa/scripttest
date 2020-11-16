package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.Rule;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Rule新增模型<br>
* 描述： Rule新增模型<br>
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
* <td>2019-01-31 14:38:56</td>
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
@ApiModel(value = "RuleModel", description = "通讯协议Model")
public class RuleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "名称")
    @NotBlank(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,32}|$", message = "名称长度2-32个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "协议类型ID")
    @NotBlank(message = "协议类型ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议类型ID")
    private String ruleTypeId;

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

    /** 协议类型端口显示**/
    @LinkName(table = "dc_rule_type", column = "port", joinField = "ruleTypeId",desc = "")
    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "是否支持加密")
    private Integer supportEncrypt;

    @ApiModelProperty(value = "通讯方式")
    private Integer protocol;

    @ApiModelProperty(value = "是否支持加密")
    @DictName(code = "BOOL_TYPE", joinField = "supportEncrypt")
    private String supportEncryptDisplay;

    @ApiModelProperty(value = "通讯方式")
    @DictName(code = "PROTOCOL_MODE", joinField = "protocol")
    private String protocolDisplay;

    @ApiModelProperty(value = "同步类型下的属性")
    private Integer syncDataItem;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static RuleModel fromEntry(Rule entry){
        RuleModel m = new RuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
