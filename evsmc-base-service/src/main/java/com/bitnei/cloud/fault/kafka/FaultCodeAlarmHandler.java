package com.bitnei.cloud.fault.kafka;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.fault.model.CodeRuleModel;
import com.bitnei.cloud.fault.model.KafkaFaultAlarmModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc： 故障码故障报警信息入库处理
 * @Author: joinly
 * @Date: 2019/3/9
 */
@Slf4j
@Service
public class FaultCodeAlarmHandler extends AbstractKafkaHandler {

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private ICodeRuleItemService codeRuleItemService;

    @Autowired
    private ICodeRuleService codeRuleService;

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Override
    public boolean handle(String message) {
        log.info("========开始处理故障码故障信息========");
        log.info("========故障信息: {}", message);
        Gson gson = new Gson();
        KafkaFaultAlarmModel model = gson.fromJson(message, KafkaFaultAlarmModel.class);
        if ("1".equals(model.getStatus())) {

            AlarmInfo entry = model.fromEntry();

            //入库之前通过msgId检查消息是否已经存在
            AlarmInfo info = alarmInfoService.findByMsgId(model.getMsgId());

            try {
                if (null != info) {
//                    log.error("重复故障信息入库, msgId：{}", model.getMsgId());
//                    return false;

                    // 已存在报警判断是否报警等级提高, 是则更新
                    int newAlarmLevel = entry.getAlarmLevel();
                    if (newAlarmLevel > info.getAlarmLevel()) {
                        alarmInfoService.updateAlarmLevel(info.getId(), newAlarmLevel);
                        log.error("故障报警等级已提高！msgId：{}", model.getMsgId());
                        // 修改entry变量为最新信息, 以便后续推送报警使用
                        info.setAlarmLevel(newAlarmLevel);
                        info.setVin(model.getVin());
                        entry = info;
                        entry.setId(info.getId());
                    } else {
                        return false;
                    }
                } else {
                    //开始
                    entry.setFaultType(2);
                    entry.setFaultStatus(1);

                    entry.setFaultBeginTime(DateUtil.converseStr(model.getBeginTime()));
                    buildEntry(entry);
                    //入库
                    entry = alarmInfoService.insert(entry);

                }
                //报警推送
                log.info("========kafka======故障码故障信息推送=======");
                alarmInfoService.sendFaultAlarmInfo(entry);
            } catch (Exception e) {
                log.error("处理故障码故障信息报错：", e);
            }

        } else if ("3".equals(model.getStatus())) {
            try {
                //结束报警
                alarmInfoService.stopAlarmByMsgId(model.getMsgId(), DateUtil.converseStr(model.getEndTime()));
            } catch (Exception e) {
                log.error("处理故障码结束报错：", e);
            }
        }
        log.info("========结束处理故障码故障信息========");
        return false;
    }

    private void buildEntry(AlarmInfo entry) {
        // 获取车辆信息
        try {
            VehicleModel model = vehicleService.getByUuid(entry.getUuid());
            entry.setVehicleId(model.getId());
            entry.setVin(model.getVin());
            entry.setLicensePlate(model.getLicensePlate());
        } catch (BusinessException e) {
            log.error("====== uuid为：{} 的车辆不存在,msgId: {} ======", entry.getUuid(), entry.getMsgId());
            throw new BusinessException("通过uuid车辆不存在");
        }

        // 获取故障码规则信息
        try {
            CodeRuleItemModel codeRuleItemModel = codeRuleItemService.get(entry.getRuleId());

            CodeRuleModel codeRuleModel = codeRuleService.get(codeRuleItemModel.getFaultCodeRuleId());
            entry.setParentRuleId(codeRuleModel.getId());
            entry.setRuleName(codeRuleModel.getFaultName());
            entry.setResponseMode(codeRuleItemModel.getResponseMode());
            //未处理
            entry.setProcesStatus(1);
            //未推送
            entry.setPushStatus(1);
        } catch (BusinessException e) {
            log.error("====== ruleId为：{} 的故障码规则项不存在或者规则项被删除, msgId: {}", entry.getRuleId(), entry.getMsgId());
            throw new BusinessException("故障码规则不存在或者规则被删除");
        }
    }
}
