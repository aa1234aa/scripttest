package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： User新增模型<br>
 * 描述： User新增模型<br>
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
 * <td>2018-11-12 10:33:47</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@ApiModel(value = "UserModel", description = "用户管理Model")
public class UserModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "用户名")
    @NotEmpty(message = "用户名不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Length(min = 3, max = 30, message = "用户名长度为3-30位字母或数字")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ColumnHeader(title = "密码")
    @NotEmpty(message = "密码不能为空", groups = {GroupInsert.class})
  //  @Pattern(regexp = "^[a-z0-9A-Z`~!@#$%\\^&*()_+-=\\{}\\|\\[\\]:;'<>?,.]{6,30}+$", message = "密码为6-30位字母、数字、特殊字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "密码")
    private String password;

    @ColumnHeader(title = "负责人id")
    @NotEmpty(message = "负责人不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "负责人id")
    private String ownerId;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_owner_people", column = "owner_name", joinField = "ownerId", desc = "负责人名称")
    private String ownerName;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_owner_people", column = "tel_phone", joinField = "ownerId", desc = "负责人电话")
    private String telPhone;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_owner_people", column = "unit_id", joinField = "ownerId", desc = "负责人单位")
    private String unitId;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_unit", column = "name", joinField = "unitId", desc = "负责人单位名称", level = 2)
    private String unitName;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_owner_people", column = "job_number", joinField = "ownerId", desc = "负责人工号")
    private String jobNumber;

    @ColumnHeader(title = "有效起始日期")
//    @NotEmpty(message = "有效起始日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "有效起始日期")
    private String permitStartDate;

    @ColumnHeader(title = "有效截止日期")
//    @NotEmpty(message = "有效截止日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "有效截止日期")
    private String permitEndDate;

    @ColumnHeader(title = "永久有效 1:是 0:否")
    @NotNull(message = "账号有效期不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "永久有效 1:是 0:否")
    private Integer isNeverExpire;

    @ColumnHeader(title = "是否有效1是0否")
//    @NotNull(message = "是否有效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否有效1是0否")
    private Integer isValid;

    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    private String isValidName;

    @ColumnHeader(title = "默认角色")
//    @NotEmpty(message = "角色不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "默认角色")
    private String defRoleId;

    /**
     * 默认角色名称
     */
    @LinkName(table = "sys_role", column = "name", joinField = "defRoleId", desc = "默认角色名称")
    private String defRoleName;

    @ColumnHeader(title = "用户角色列表")
    @NotEmpty(message = "角色不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "用户角色列表")
    private String roleIds;

    @ColumnHeader(title = "用户角色列表")
    @ApiModelProperty(value = "用户角色名称列表")
    private String roleNames;

    @ColumnHeader(title = "是否开通app权限")
    @NotNull(message = "是否开通app权限不能为空1是0否", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否开通app权限")
    private Integer openApp;

    @DictName(code = "OPEN_APP_PERMISSION", joinField = "openApp")
    private String openAppName;

    @ColumnHeader(title = "是否开通微信权限")
    @NotNull(message = "是否开通微信权限不能为空1是0否", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否开通微信权限")
    private Integer openWx;

    @DictName(code = "BOOL_TYPE", joinField = "openWx")
    private String openWxName;

    @ColumnHeader(title = "是否所有权限组")
    @NotNull(message = "是否所有权限组不能为空1是0否", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否所有权限组")
    private Integer isAllPermissions;

    @DictName(code = "BOOL_TYPE", joinField = "isAllPermissions")
    private String isAllPermissionsName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 组织架构id，多个用逗号分隔
     */
    @ApiModelProperty(value = "单位类型id")
    private String deptIds;
    /**
     * 组织架构名称，多个用逗号分隔
     */
//    @ApiModelProperty(value = "单位类型名称")
    private String deptNames;

    /**
     * 组织权限id，多个用逗号分隔
     */
    @ApiModelProperty(value = "单位类型id")
    private String groupIds;
    /**
     * 组织权限名称，多个用逗号分隔
     */
//    @ApiModelProperty(value = "单位类型名称")
    private String groupNames;

    @ApiModelProperty(value = "账号是否冻结")
    private Integer isFrozen;


    private String isFrozenDisplay;
    @ApiModelProperty(value = "账号冻结原因")
    private String frozenReason;

    /**
     * 头像id
     */
    @ApiModelProperty(value = "头像id")
    private String photoId;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static UserModel fromEntry(User entry) {
        UserModel m = new UserModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
