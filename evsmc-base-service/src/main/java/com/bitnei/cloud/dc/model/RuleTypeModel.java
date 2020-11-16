package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.RuleType;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RuleType新增模型<br>
* 描述： RuleType新增模型<br>
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
* <td>2019-01-30 10:36:15</td>
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
@ApiModel(value = "RuleTypeModel", description = "协议类型Model")
public class RuleTypeModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "类型名称")
    @NotBlank(message = "类型名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,64}|$", message = "类型名称长度2-64字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "类型名称")
    private String name;

    @ColumnHeader(title = "类型编号")
    @NotBlank(message = "类型编号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[a-zA-Z0-9]{1,32}|$", message = "类型编号长度1-32个英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "类型编号")
    private String code;

    @ColumnHeader(title = "排序")
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

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

    @ColumnHeader(title = "端口")
    @NotNull(message = "端口不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Digits(integer = 5, fraction = 0, message = "端口长度1-5个数字", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "1", message = "端口必须是一个数字，其值必须大于等于1", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "65535", message = "端口必须是一个数字，其值必须小于等于65535", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "端口")
    private Integer port;

    @ColumnHeader(title = "是否支持加密")
    @NotNull(message = "是否支持加密不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否支持加密")
    private Integer supportEncrypt;

    @ColumnHeader(title = "通讯方式")
    @NotNull(message = "通讯方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "通讯方式")
    private Integer protocol;

    @ApiModelProperty(value = "是否支持加密")
    @DictName(code = "BOOL_TYPE", joinField = "supportEncrypt")
    private String supportEncryptDisplay;

    @ApiModelProperty(value = "通讯方式")
    @DictName(code = "PROTOCOL_MODE", joinField = "protocol")
    private String protocolDisplay;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static RuleTypeModel fromEntry(RuleType entry){
        RuleTypeModel m = new RuleTypeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
