package com.bitnei.cloud.sms.domain;

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
* 功能： SmsTask实体<br>
* 描述： SmsTask实体<br>
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
* <td>2019-08-16 09:41:04</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsTask extends TailBean {

    /** 主键 **/
    private String id;
    /** 模板id **/
    private String templateId;
    /** 模板名称 **/
    private String templateName;
    /** 对应短信提供商的模板code **/
    private String templateCode;
    /** 业务类型: 1、短信下发; 2、终端短信唤醒 **/
    private Integer serviceType;
    /** 任务状态: 1发送, 2未发送(草搞) **/
    private Integer status;
    /** 备注 **/
    private String remarks;
    /** 发送人 **/
    private String createBy;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String updateBy;
    /** 修改时间 **/
    private String updateTime;


    /** 短信内容 **/
    private String smsContent;

    /** 接收人/车辆id, 以json格式保存 **/
    private String receivers;

    /** 以json格式保存 **/
    private String variables;

    /** 短信发送流水号 **/
    private String bizId;

    /** 是否为 添加全部查询结果:  0是, 1不是 **/
    private Integer addAll;
}
