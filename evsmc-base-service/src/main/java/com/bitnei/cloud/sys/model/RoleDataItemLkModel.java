package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bitnei.cloud.sys.domain.RoleDataItemLk;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RoleDataItemLk新增模型<br>
* 描述： RoleDataItemLk新增模型<br>
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
* <td>2018-11-22 10:28:44</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@ApiModel(value = "RoleDataItemLkModel", description = "数据项角色数据项中间表Model")
public class RoleDataItemLkModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "角色模块id")
    @NotEmpty(message = "角色模块id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "角色模块id")
    private String roleModuleId;

    @ColumnHeader(title = "模块数据id")
    @NotEmpty(message = "模块数据id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "模块数据id")
    private String moduleDataId;

    /** 模块数据项id **/
    @ColumnHeader(title = "模块数据项id")
    @ApiModelProperty(value = "模块数据项id")
    private String moduleDataItemId;

    @ColumnHeader(title = "是否隐藏")
    @NotNull(message = "是否隐藏不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否隐藏")
    private Integer isHidden;

    @ColumnHeader(title = "是否脱敏")
    @NotNull(message = "是否脱敏不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否脱敏")
    private Integer isSensitive;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    private String updateBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRoleModuleId() {
        return roleModuleId;
    }

    public void setRoleModuleId(String roleModuleId) {
        this.roleModuleId = roleModuleId;
    }
    public String getModuleDataId() {
        return moduleDataId;
    }

    public void setModuleDataId(String moduleDataId) {
        this.moduleDataId = moduleDataId;
    }

    public String getModuleDataItemId() {
        return moduleDataItemId;
    }

    public void setModuleDataItemId(String moduleDataItemId) {
        this.moduleDataItemId = moduleDataItemId;
    }

    public Integer getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }
    public Integer getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(Integer isSensitive) {
        this.isSensitive = isSensitive;
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

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static RoleDataItemLkModel fromEntry(RoleDataItemLk entry){
        RoleDataItemLkModel m = new RoleDataItemLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
