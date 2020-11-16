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

import com.bitnei.cloud.dc.domain.PlatformInformation;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformInformation新增模型<br>
* 描述： PlatformInformation新增模型<br>
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
* <td>2019-01-29 19:29:35</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PlatformInformationModel", description = "平台转入配置Model")
public class PlatformInformationModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ColumnHeader(title = "名称")
    @NotBlank(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]{2,20}$",message="名称请勿输入特殊字符且长度为2-20个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "用户名")
    @NotBlank(message = "用户名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9]{2,50}$",message="用户名请勿输入中文及特殊字符且长度为2-50个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @ColumnHeader(title = "密码")
    @NotBlank(message = "密码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[^\\u4e00-\\u9fa5]{4,60}$",message="密码请勿输入中文且长度为4-60个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "密码")
    private String password;

    @ColumnHeader(title = "唯一识别码")
    @Pattern(regexp="^[^\\u4e00-\\u9fa5]{2,100}|$",message="唯一识别码请勿输入中文且长度为2-100个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "唯一识别码")
    private String cdkey;

    @ColumnHeader(title = "平台类型")
    @NotNull(message = "平台类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "平台类型")
    private Integer type;

    @ColumnHeader(title = "联系人")
    @NotBlank(message = "联系人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]{2,30}$",message="联系人请勿输入特殊字符且长度为2-30个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ColumnHeader(title = "联系电话")
    @NotBlank(message = "联系电话不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[0-9]{11}$",message="请输入11位数的电话号码", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ColumnHeader(title = "电子邮件")
    @NotBlank(message = "电子邮件不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 5, max = 50, message = "邮箱地址长度为5-50个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @Pattern(regexp="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",message="请输入正确的邮箱格式且长度为5-50个字符", groups = { GroupInsert.class,GroupUpdate.class})
//    @Email(message="请输入正确的邮箱格式且长度为5-50个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ColumnHeader(title = "白名单")
//    ip地址正则表达式：((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)
    @Pattern(regexp="^([A-Za-z0-9]{2,50}|((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d))(;[A-Za-z0-9]{2,50}|((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d))*$",message = "白名单请勿输入中文及特殊字符且长度为2-50个字符，多个以英文';'分号间隔’", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "白名单")
    private String whitelist;

    @ColumnHeader(title = "区域ID")
    @NotBlank(message = "区域ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "区域ID")
    private String areaId;

    @ColumnHeader(title = "启用状态")
    @ApiModelProperty(value = "启用状态")
    private Integer isOpen;

    @ColumnHeader(title = "协议类型ID")
    @NotBlank(message = "协议类型ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议类型ID")
    private String ruleTypeId;

    @ColumnHeader(title = "环境")
    @ApiModelProperty(value = "环境")
    private Integer environment;

    @ColumnHeader(title = "连接状态")
    @ApiModelProperty(value = "连接状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


    /** 区域名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "areaId", desc = "")
    private String areaName;
    /** 协议类型**/
    @LinkName(table = "dc_rule_type", column = "name", joinField = "ruleTypeId", desc = "")
    private String ruleTypeName;
    /** 平台类型**/
    @DictName(code = "PLATFORM_TYPE",joinField = "type")
    private String typeName;
    /** 启用标志**/
    @DictName(code = "BOOL_TYPE",joinField = "isOpen")
    private String isOpenName;
    /** 环境类型**/
    @DictName(code = "ENV_TYPE",joinField = "environment")
    private String environmentName;
    /** 平台连接状态**/
    @DictName(code = "PLATFORM_CONNECT_STATUS",joinField = "status")
    private String statusName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PlatformInformationModel fromEntry(PlatformInformation entry){
        PlatformInformationModel m = new PlatformInformationModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
