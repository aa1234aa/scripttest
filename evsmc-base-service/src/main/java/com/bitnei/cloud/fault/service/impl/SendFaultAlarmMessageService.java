package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.bean.*;
import com.bitnei.cloud.common.client.PushServiceClient;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.smsInterface.SmsWsServiceClient;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.NotifierSetting;
import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.model.AlarmMessageModel;
import com.bitnei.cloud.fault.model.RiskNoticeMessageModel;
import com.bitnei.cloud.fault.model.VehRiskNoticeModel;
import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.bitnei.cloud.fault.service.ISendFaultAlarmMessageService;
import com.bitnei.cloud.sys.dao.UserFaultResponseModeSettingMapper;
import com.bitnei.cloud.sys.domain.MsgTemplate;
import com.bitnei.cloud.sys.domain.UserFaultResponseModeSetting;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.VehicleOperModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IMsgTemplateService;
import com.bitnei.cloud.sys.service.IVehicleOperService;
import com.google.common.collect.Maps;
import jodd.json.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/5/31
 */
@Slf4j
@Service
public class SendFaultAlarmMessageService implements ISendFaultAlarmMessageService {

    @Resource
    private WsServiceClient wsServiceClient;

    @Resource
    private PushServiceClient pushServiceClient;

    @Autowired
    private IDictService dictService;

    @Autowired
    private INotifierSettingService notifierSettingService;

    @Resource
    private UserFaultResponseModeSettingMapper userFaultResponseModeSettingMapper;

    @Autowired
    private IVehicleOperService vehicleOperService;
    @Autowired
    private IMsgTemplateService msgTemplateService;

    @Resource
    private SmsWsServiceClient smsWsServiceClient;

    @Override
    public void sendMessageToWeb(AlarmInfo alarmInfo) {
        AlarmMessageModel model = new AlarmMessageModel(alarmInfo.getRuleName(), alarmInfo.getFaultType(),
                dictService.getDictName(String.valueOf(alarmInfo.getFaultType()), "FAULT_TYPE"),
                dictService.getDictName(String.valueOf(alarmInfo.getAlarmLevel()), "ALARM_LEVEL"),
                alarmInfo.getAlarmLevel(),
                alarmInfo.getResponseMode(), alarmInfo.getVin(), alarmInfo.getVehicleId(), alarmInfo.getId());

        WsMessage wsMessage = new WsMessage();
        wsMessage.setData(model);
        wsMessage.setResourceType("alarm");
        wsMessage.setType("object");

        String message = new JsonSerializer().deep(true).serialize(wsMessage);

        //当前在线用户
        List<OnlineUser> onlineUserList = wsServiceClient.onlineUsers();
        //车辆分配的用户
        Map<String, Object> params = new HashMap<>();
        params.put("vehicleId", alarmInfo.getVehicleId());
        params.put("faultType", String.valueOf(alarmInfo.getFaultType()));
        String ruleId = alarmInfo.getFaultType().equals(FaultTypeEnum.CODE.getValue()) ? alarmInfo.getParentRuleId() : alarmInfo.getRuleId();
        params.put("ruleId", ruleId);
        List<String> needSendUserList = notifierSettingService.findForAlarm(params);
        for (OnlineUser user : onlineUserList) {
            if (needSendUserList.contains(user.getId())) {
                log.info("=======推送故障信息==to =========== : " + user.getUsername());
                //发送web信息=
                wsServiceClient.sendToUser(user.getId(), message);
            }
        }
    }

    @Override
    public void sendMessageToJiGuang(AlarmInfo alarmInfo) {
        //车辆分配的用户
        Map<String, Object> params = new HashMap<>();
        params.put("vehicleId", alarmInfo.getVehicleId());
        params.put("enabledStatus", 1);
        params.put("appPopup", 1);
        params.put("faultType", String.valueOf(alarmInfo.getFaultType()));
        String ruleId = alarmInfo.getFaultType().equals(FaultTypeEnum.CODE.getValue()) ? alarmInfo.getParentRuleId() : alarmInfo.getRuleId();
        params.put("ruleId", ruleId);
        List<UserFaultResponseModeSetting> notifierSettings = userFaultResponseModeSettingMapper.notifierStting(params);
        MessageBody messageBody = new MessageBody();
        if (notifierSettings.size() > 0) {
            List<String> userIds = new ArrayList();
            for (UserFaultResponseModeSetting setting : notifierSettings) {
                userIds.add(setting.getUserId());
            }
            String[] alias = new String[userIds.size()];
            alias = userIds.toArray(alias);
            messageBody.setAlias(alias);
        } else {
            log.info("极光推送失败====车辆未分配管理人或者用户未设置开通app接收信息=====");
            return;
        }

        String typeName = dictService.getDictName(String.valueOf(alarmInfo.getFaultType()), "FAULT_TYPE");
        String levelName =dictService.getDictName(String.valueOf(alarmInfo.getAlarmLevel()), "ALARM_LEVEL");
        messageBody.setTitle(typeName);
        messageBody.setContentType("text");
        String licensePlate = StringUtils.isNotBlank(alarmInfo.getLicensePlate())? alarmInfo.getLicensePlate() : alarmInfo.getVin();
        if (alarmInfo.getFaultType().intValue() == 1) {
            messageBody.setMsgContent("车辆：" + licensePlate + " 出现 "+ levelName +" 参数故障,请及时查看。");
        } else if (alarmInfo.getFaultType().intValue() == 2) {
            messageBody.setMsgContent("车辆：" + licensePlate + " 出现 "+ levelName +" 故障码故障, 请及时处理。");
        } else if (alarmInfo.getFaultType().intValue() == 3) {
            messageBody.setMsgContent("车辆：" + licensePlate + " 出现 "+ levelName +" 电子围栏故障, 请及时处理。");
        }
        messageBody.setExtra("faultId:"+alarmInfo.getId()+"#faultBeginTime:"+alarmInfo.getFaultBeginTime());
        log.info("====app消息开始推送啦====: {}", messageBody.toString());
        PushResult result = pushServiceClient.pushMessage(messageBody);
        if (result.getCode() == 200) {
            log.info("====极光推送成功====");
        } else {
            log.error("====极光推送失败====：{}", result.getErrorMessage());
        }
    }

    @Override
    public void sendSms(AlarmInfo alarmInfo) {
        // 获取短信模板ID
        Map<String,Object> tempParams = DataAccessKit.getAuthMap("sys_msg_template", "mt");
        tempParams.put("interCode", "1000");
        tempParams.put("status", 1);
        MsgTemplate msgTemp = msgTemplateService.unique("findByInterCode", tempParams);
        if (msgTemp == null){
            log.info("===========故障报警 短信模板不存在===========");
        }else {
            // 获取车辆运营信息
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
            params.put("vin", alarmInfo.getVin());
            List<Vehicle> entries = vehicleOperService.findBySqlId("findVehOperPager", params);
            VehicleOperModel model = new VehicleOperModel();
            StringBuffer templateParam = new StringBuffer();
            templateParam.append(alarmInfo.getLicensePlate()).append(";");
            templateParam.append(alarmInfo.getRuleName());
            for (Vehicle entry : entries) {
                model = VehicleOperModel.fromEntry(entry);
                log.info("===========短信推送故障信息 to : " + model.getOperUnitOrOwnerPhone() + "(" + model.getOperUnitOrOwnerName() + ")");
                smsWsServiceClient.sendPhoneSms(model.getOperUnitOrOwnerPhone(), msgTemp.getTemplateId(), templateParam.toString());
            }
        }
    }

    @Override
    public void sendRiskMessageToWeb(VehRiskNoticeModel riskNotice) {
        String title = String.format("车辆安全风险通知(%s级)",riskNotice.getRiskLevel());
        RiskNoticeMessageModel model = new RiskNoticeMessageModel(riskNotice.getCode(),title, riskNotice.getNoticeOriginalText());
        WsMessage wsMessage = new WsMessage();
        wsMessage.setData(model);
        wsMessage.setResourceType("risk");
        wsMessage.setType("object");
        String message = new JsonSerializer().deep(true).serialize(wsMessage);
        //当前在线用户
        List<OnlineUser> onlineUserList = wsServiceClient.onlineUsers();
        //查询车辆分配的安全风险通知用户
        Map<String, Object> params = new HashMap<>(4);
        params.put("vehicleId", riskNotice.getVehicleId());
        params.put("faultType", NotifierSetting.FAULT_TYPE_RISK);
        List<String> users = notifierSettingService.findForRisk(params);
        for (OnlineUser user : onlineUserList) {
            if (users.contains(user.getId())) {
                log.info("=======推送安全风险信息==to =========== : " + user.getUsername());
                //发送web信息=
                wsServiceClient.sendToUser(user.getId(), message);
            }
        }
    }

    @Override
    public void sendRiskMessageJiGuang(VehRiskNoticeModel riskNotice) {
        String title = String.format("车辆安全风险通知(%s级)",riskNotice.getRiskLevel());
        //查询车辆分配的安全风险通知用户
        Map<String, Object> params = new HashMap<>(4);
        params.put("vehicleId", riskNotice.getVehicleId());
        params.put("faultType", NotifierSetting.FAULT_TYPE_RISK);
        List<String> users = notifierSettingService.findForRisk(params);
        // 极光推送消息体
        JPushMessageBody messageBody = new JPushMessageBody();
        if (users.size() > 0) {
            String[] alias = users.toArray(new String[0]);
            messageBody.setAlias(alias);
        } else {
            log.info("极光推送失败====车辆风险通知未分配管理人用户=====");
            return;
        }
        messageBody.setContentType("text");
        messageBody.setTitle(title);
        messageBody.setMsgContent(riskNotice.getNoticeOriginalText());
        // 额外信息
        Map<String, String> extraMap = Maps.newHashMap();
        extraMap.put("id", riskNotice.getId());
        extraMap.put("code", riskNotice.getCode());
        String extra = new JsonSerializer().deep(true).serialize(extraMap);
        messageBody.setExtra(extra);
        PushMessageResult result = pushServiceClient.pushMessage(messageBody);
        if(result.getCode() == HttpStatus.SC_OK ) {
            log.info("极光推送成功={}", messageBody.getMsgContent());
        } else {
            log.info("极光推送失败={}", messageBody.getMsgContent());
        }


    }
}
