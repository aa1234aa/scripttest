package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Role新增模型<br>
* 描述： Role新增模型<br>
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
* <td>2018-11-22 10:06:09</td>
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
@Setter
@Getter
@ApiModel(value = "RoleModel", description = "角色管理Model")
public class RoleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "角色名称")
    //@NotEmpty(message = "角色名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 2, max = 30, message = "角色名称为2-30个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "角色名称")
    private String name;

    @ColumnHeader(title = "状态 1：有效 0：无效")
    @NotNull(message = "状态 1：有效 0：无效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "状态 1：有效 0：无效")
    private Integer isValid;

    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    private String isValidName;

    @ColumnHeader(title = "是否管理员 1:是 0:否")
    //@NotNull(message = "是否管理员 1:是 0:否不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否管理员 1:是 0:否")
    private Integer isAdmin;

    @DictName(code = "BOOL_TYPE", joinField = "isAdmin")
    private String isAdminName;

    @ColumnHeader(title = "首页地址")
    //@NotEmpty(message = "首页地址不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "首页地址")
    private String indexUrl;

    @ColumnHeader(title = "备注")
    //@NotEmpty(message = "备注不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    //@Length(min = 2, max = 30, message = "备注为2-30个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_module", column = "name", joinField = "indexUrl", desc = "模块名称")
    @ApiModelProperty(value = "模块名称")
    private String indexUrlName;

    @ApiModelProperty(value = "关联账户数量")
    private Integer userCount;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 关联账号多个id,用逗号隔开 **/
    @ApiModelProperty(value = "关联账号id列表")
    private String userIds;

    /**
     * 将实体转为前台model
     * @param entry Role
     * @return RoleModel
     */
    public static RoleModel fromEntry(Role entry){
        RoleModel m = new RoleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
