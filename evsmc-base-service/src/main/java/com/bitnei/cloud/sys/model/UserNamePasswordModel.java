package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
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
import javax.validation.constraints.Pattern;

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
 * <td>renshuo</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author renshuo
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@ApiModel(value = "UserNamePasswordModel", description = "用户重置密码Model")
public class UserNamePasswordModel extends BaseModel {


    @ColumnHeader(title = "用户名")
    @NotEmpty(message = "用户名不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Length(min = 3, max = 30, message = "用户名长度为3-30位字母或数字")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ColumnHeader(title = "密码")
    @NotEmpty(message = "密码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
   // @Pattern(regexp = "^[a-z0-9A-Z`~!@#$%\\^&*()_+-=\\{}\\|\\[\\]:;'<>?,.]{6,30}+$", message = "密码为6-30位字母、数字、特殊字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "密码")
    private String password;

}
