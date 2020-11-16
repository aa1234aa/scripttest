package com.bitnei.cloud.sms.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SmsTaskItems实体<br>
* 描述： SmsTaskItems实体<br>
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
* <td>2019-08-17 15:45:24</td>
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
public class SmsTaskItem extends TailBean {

    /** 主键 **/
    private String id;
    /** 任务id **/
    private String taskId;
    /** 业务类型: 1、短信下发; 2、终端短信唤醒 **/
    private Integer serviceType;
    /** 接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人 **/
    private Integer receiverType;
    /** 接收人id **/
    private String receiverId;
    /** 接收人 **/
    private String receiver;
    /** 车辆id **/
    private String vehicleId;
    /** 发送成功状态 : 1成功, 2失败 **/
    private Integer sendStatus;
    /** 失败原因 **/
    private String failMsg;
    /** 电话号码 **/
    private String msisd;
    /** 车架号 **/
    private String vin;
    /** 发送时间 **/
    private String sendTime;
    /** 短信发送流水号 **/
    private String bizId;

    /** 发送人 **/
    private String createBy;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String updateBy;
    /** 修改时间 **/
    private String updateTime;

    private String templateId;
    private String templateName;
    private String smsContent;
    private String status;
    /** 备注 **/
    private String remarks;

    /**故障id**/
    private String faultId;

    /**吉利联通发送ICCID**/
    private String iccid;

}
