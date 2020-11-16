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

import com.bitnei.cloud.dc.domain.ForwardPlatform;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardPlatform新增模型<br>
* 描述： ForwardPlatform新增模型<br>
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
* <td>2019-02-12 14:46:42</td>
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
@ApiModel(value = "ForwardPlatformModel", description = "平台转发配置Model")
public class ForwardPlatformModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "平台名称")
    @NotBlank(message = "平台名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,50}|$", message = "平台名称长度为2-50个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "平台名称")
    private String name;

    @ColumnHeader(title = "静态数据推送")
    @NotNull(message = "静态数据推送不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "静态数据推送")
    private Integer staticForwardPlatform;

    @ColumnHeader(title = "转发方式")
    @NotNull(message = "转发方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发方式")
    private Integer forwardMode;

    @ColumnHeader(title = "目的地址")
    @NotBlank(message = "目的地址不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{5,60}|$", message = "目的地址长度为5-60个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "目的地址")
    private String host;

    @ColumnHeader(title = "端口(或命名空间)")
    @NotBlank(message = "端口(或命名空间)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,60}|$", message = "端口(或命名空间)长度为2-60个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "端口(或命名空间)")
    private String port;

    @ColumnHeader(title = "用户名")
    @NotBlank(message = "用户名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,12}|$", message = "用户名长度为2-12个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @ColumnHeader(title = "密码")
    @NotBlank(message = "密码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,20}|$", message = "密码长度为2-20个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "密码")
    private String password;

    @ColumnHeader(title = "协议ID")
    @NotBlank(message = "协议ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议ID")
    private String ruleId;

    @ColumnHeader(title = "唯一识别码")
    @Pattern(regexp = "^.{2,100}|$", message = "唯一识别码长度为2-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "唯一识别码")
    private String cdkey;

    @ColumnHeader(title = "优先级")
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ColumnHeader(title = "备注")
    @Pattern(regexp = "^[\\s\\S]{0,100}|$", message = "备注长度为0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @ColumnHeader(title = "平台连接状态")
    @ApiModelProperty(value = "平台连接状态")
    private Integer connectStatus;

    @ColumnHeader(title = "启用状态")
    @ApiModelProperty(value = "启用状态")
    private Integer isValid;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 协议名称显示**/
    @LinkName(table = "dc_rule", column = "name", joinField = "ruleId",desc = "")
    @ApiModelProperty(value = "协议名称")
    private String ruleDisplay;

    /** 平台连接状态名称显示**/
    @DictName(code = "PLATFORM_CONNECT_STATUS", joinField = "connectStatus")
    @ApiModelProperty(value = "平台连接状态名称")
    private String connectStatusDisplay;

    /** 静态数据推送名称显示**/
    @DictName(code = "PUSH_PLATFORM_TYPE", joinField = "staticForwardPlatform")
    @ApiModelProperty(value = "静态数据推送名称")
    private String staticForwardDisplay;

    /** 转发方式名称显示**/
    @DictName(code = "FORWARD_MODE", joinField = "forwardMode")
    @ApiModelProperty(value = "转发方式名称")
    private String forwardModeDisplay;

    /** 优先级名称显示**/
    @DictName(code = "PRIORITY", joinField = "priority")
    @ApiModelProperty(value = "优先级名称")
    private String priorityDisplay;

    /** 启用状态名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    @ApiModelProperty(value = "启用状态名称")
    private String isValidDisplay;

    @ApiModelProperty(value = "平台是否同步全部通讯协议数据项")
    private Integer syncDataItem;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardPlatformModel fromEntry(ForwardPlatform entry){
        ForwardPlatformModel m = new ForwardPlatformModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
