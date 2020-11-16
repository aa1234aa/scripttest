package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： User实体<br>
 * 描述： User实体<br>
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
public class User extends TailBean {
    /**
     * 主键 *
     */
    private String id;
    /**
     * 用户名 *
     */
    private String username;
    /**
     * 密码 *
     */
    @JsonIgnore
    private String password;
    /**
     * 负责人id *
     */
    private String ownerId;
    /**
     * 有效起始日期 *
     */
    private String permitStartDate;
    /**
     * 有效截止日期 *
     */
    private String permitEndDate;
    /**
     * 永久有效 1:是 0:否 *
     */
    private Integer isNeverExpire;
    /**
     * 是否有效 *
     */
    private Integer isValid;
    /**
     * 默认角色 *
     */
    private String defRoleId;
    /**
     * 是否开通app权限 *
     */
    private Integer openApp;
    /**
     * 是否开通微信权限 *
     */
    private Integer openWx;
    /**
     * 是否所有权限组 *
     */
    private Integer isAllPermissions;
    /**
     * 创建时间 *
     */
    private String createTime;
    /**
     * 创建人 *
     */
    private String createBy;
    /**
     * 更新时间 *
     */
    private String updateTime;
    /**
     * 更新人 *
     */
    private String updateBy;
    /**
     * 组织架构id，多个用逗号分隔
     */
    private String deptIds;
    /**
     * 组织架构name，多个用逗号分隔
     */
    private String deptNames;
    /**
     * 组织权限id，多个用逗号分隔
     */
    private String groupIds;
    /**
     * 组织权限name，多个用逗号分隔
     */
    private String groupNames;

    private String roleIds;

    private String roleNames;

    /**
     * 头像id
     */
    private String photoId;

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPermitStartDate() {
        return permitStartDate;
    }

    public void setPermitStartDate(String permitStartDate) {
        this.permitStartDate = permitStartDate;
    }

    public String getPermitEndDate() {
        return permitEndDate;
    }

    public void setPermitEndDate(String permitEndDate) {
        this.permitEndDate = permitEndDate;
    }

    public Integer getIsNeverExpire() {
        return isNeverExpire;
    }

    public void setIsNeverExpire(Integer isNeverExpire) {
        this.isNeverExpire = isNeverExpire;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getDefRoleId() {
        return defRoleId;
    }

    public void setDefRoleId(String defRoleId) {
        this.defRoleId = defRoleId;
    }

    public Integer getOpenApp() {
        return openApp;
    }

    public void setOpenApp(Integer openApp) {
        this.openApp = openApp;
    }

    public Integer getOpenWx() {
        return openWx;
    }

    public void setOpenWx(Integer openWx) {
        this.openWx = openWx;
    }

    public Integer getIsAllPermissions() {
        return isAllPermissions;
    }

    public void setIsAllPermissions(Integer isAllPermissions) {
        this.isAllPermissions = isAllPermissions;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}
