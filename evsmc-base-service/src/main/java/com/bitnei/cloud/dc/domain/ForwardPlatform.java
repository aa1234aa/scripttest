package com.bitnei.cloud.dc.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import io.swagger.annotations.ApiModelProperty;
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
* 功能： ForwardPlatform实体<br>
* 描述： ForwardPlatform实体<br>
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
public class ForwardPlatform extends TailBean {

    /** 主键 **/
    private String id;
    /** 平台名称 **/
    private String name;
    /** 静态数据推送 **/
    private Integer staticForwardPlatform;
    /** 转发方式 **/
    private Integer forwardMode;
    /** 目的地址 **/
    private String host;
    /** 端口(或命名空间) **/
    private String port;
    /** 用户名 **/
    private String username;
    /** 密码 **/
    private String password;
    /** 协议ID **/
    private String ruleId;
    /** 唯一识别码 **/
    private String cdkey;
    /** 优先级 **/
    private Integer priority;
    /** 备注 **/
    private String note;
    /** 平台连接状态 **/
    private Integer connectStatus;
    /** 启用状态 **/
    private Integer isValid;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    /**平台是否同步全部通讯协议数据项**/
    private Integer syncDataItem;

}
