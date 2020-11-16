package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-module <br>
 * 功能： 用户登录model <br>
 * 描述： 用户登录model <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2018-${MOTH}-22</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@ApiModel(value = "LoginModel", description = "用户登入Model")
public class LoginModel {
    /** 用户名 **/
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名" )
    private String username;
    /** 密码 **/
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty(value = "密码" )
    private String password;
    /** 验证码 **/
    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码" )
    private String validCode;

    /** 手机号 **/
    @ApiModelProperty(value = "手机号" )
    private String mobile;

    /** 是否app登录  1 是 0 否 **/
    @ApiModelProperty(value = "是否app登录  1 是 0 否" )
    private String isApp;
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

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsApp() {
        return isApp;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }
}
