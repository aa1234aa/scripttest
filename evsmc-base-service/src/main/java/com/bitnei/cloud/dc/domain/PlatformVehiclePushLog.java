package com.bitnei.cloud.dc.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformVehiclePushLog实体<br>
* 描述： PlatformVehiclePushLog实体<br>
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
* <td>2019-02-19 14:19:06</td>
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
public class PlatformVehiclePushLog extends TailBean {

    /** 主键 **/
    private String id;
    /** 车辆ID **/
    private String vehicleId;
    /** 推送平台ID **/
    private String forwardPlatformId;
    /** 推送时间 **/
    private String reqTime;
    /** 响应时间 **/
    private String rspTime;
    /** 推送状态 **/
    private Integer pushStatus;
    /** 错误原因 **/
    private String errorMessage;
    /** 人工确认账号 **/
    private String confirmUser;
    /** 转发请求内容 **/
    private String reqBody;
    /** 解密请求内容 **/
    private String decryptReqBody;
    /** 响应原始内容 **/
    private String rspBody;
    /** 解密响应内容 **/
    private String decryptRspBody;
    /** 创建时间 **/
    private String createTime;
    /** 备注 **/
    private String remark;

}
