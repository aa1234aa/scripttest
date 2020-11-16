package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.model.VehRiskNoticeModel;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/5/31
 */
public interface ISendFaultAlarmMessageService {

    void sendMessageToWeb(AlarmInfo alarmInfo);


    void sendMessageToJiGuang(AlarmInfo alarmInfo);

    void sendSms(AlarmInfo alarmInfo);


    /**
     * 通过ws发送车辆风险通知到web
     * @param riskNotice 消息
     */
    void sendRiskMessageToWeb(VehRiskNoticeModel riskNotice);

    /**
     * 车辆风险通知app极光推送
     * @param riskNotice 消息
     */
    void sendRiskMessageJiGuang(VehRiskNoticeModel riskNotice);

}
