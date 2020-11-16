package com.bitnei.cloud.dc.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformInformation实体<br>
* 描述： PlatformInformation实体<br>
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
public class PlatformInformation extends TailBean {

    /** id **/
    private String id;
    /** 名称 **/
    private String name;
    /** 用户名 **/
    private String username;
    /** 密码 **/
    private String password;
    /** 唯一识别码 **/
    private String cdkey;
    /** 平台类型 **/
    private Integer type;
    /** 联系人 **/
    private String contacts;
    /** 联系电话 **/
    private String telephone;
    /** 电子邮件 **/
    private String email;
    /** 白名单 **/
    private String whitelist;
    /** 区域ID **/
    private String areaId;
    /** 启用状态 **/
    private Integer isOpen;
    /** 协议类型ID **/
    private String ruleTypeId;
    /** 环境 **/
    private Integer environment;
    /** 连接状态 **/
    private Integer status;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

}
