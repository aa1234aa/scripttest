package com.bitnei.cloud.sms.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Data;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/26
 */

@Data
public class SmsTaskItemDetail extends TailBean {

    private String id;

    private String taskId;

    private Integer serviceType;

    private Integer receiverType;

    private String receiverId;

    private String receiver;

    private String vehicleId;

    private Integer sendStatus;

    private String failMsg;

    private String msisd;

    /** 发送时间 **/
    private String sendTime;

    /**短信模板**/
    private String templateName;

    /**短信内容**/
    private String smsContent;

    /** 车架号 **/
    private String vin;

    private String licensePlate;

    private Integer operUseType;

    private String operUnitId;

    private String vehModelId;


    private String faultId;

    /**报警开始时间**/
    private String faultBeginTime;

}
