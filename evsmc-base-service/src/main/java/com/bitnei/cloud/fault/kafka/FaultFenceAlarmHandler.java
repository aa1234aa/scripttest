package com.bitnei.cloud.fault.kafka;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.model.KafkaFenceAlarmModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.monitor.model.ElectronicFenceModel;
import com.bitnei.cloud.monitor.service.IElectronicFenceService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc： 电子围栏报警信息入库处理
 * @Author: DFY
 * @Date: 2019/05/24
 */
@Slf4j
@Service
public class FaultFenceAlarmHandler extends AbstractKafkaHandler {

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IElectronicFenceService electronicFenceService;

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Override
    public boolean handle(String message) {
        log.info("========开始处理电子围栏报警信息========");
        log.info("========电子围栏报警信息: {}", message);
        Gson gson = new Gson();
        KafkaFenceAlarmModel model = gson.fromJson(message, KafkaFenceAlarmModel.class);
        if ("BEGIN".equals(model.getEventStage())) {

            //入库之前通过msgId检查消息是否已经存在
            AlarmInfo info = alarmInfoService.findByMsgId(model.getMessageId());
            if (null != info) {
                log.error("重复报警信息入库, msgId：{}", model.getMessageId());
                return false;
            }

            AlarmInfo entry = model.fromEntry();
            //开始 围栏报警状态为3
            entry.setFaultType(3);
            // 未结束
            entry.setFaultStatus(1);
            try {
                // 开始报警时间
                entry.setFaultBeginTime(DateUtil.converseStr(model.getNoticeTime()));

                // 关联电子围栏规则设置
                buildEntry(entry);

                //入库
                entry = alarmInfoService.insert(entry);

                //报警推送
                log.info("========kafka======电子围栏报警信息推送=======");
                alarmInfoService.sendFaultAlarmInfo(entry);
            } catch (Exception e) {
                log.error("处理电子围栏报警信息报错：", e);
            }
        } else if ("END".equals(model.getEventStage())) {
            try {
                //结束报警
                alarmInfoService.stopAlarmByMsgId(model.getMessageId(), DateUtil.converseStr(model.getDataTime()));
            } catch (Exception e) {
                log.error("处理电子围栏报警信息报错：", e);
            }
        }
        log.info("========结束处理电子围栏报警信息========");
        return false;
    }

    private void buildEntry(AlarmInfo entry) {
        // 获取车辆信息
        try {
            VehicleModel model = vehicleService.getByUuid(entry.getUuid());
            entry.setVehicleId(model.getId());
            entry.setLicensePlate(model.getLicensePlate());
            entry.setVin(model.getVin());
            entry.setUuid(model.getUuid());
        } catch (BusinessException e) {
            log.error("====== uuid为：{} 的车辆不存在,msgId: {} ======", entry.getUuid(), entry.getMsgId());
            throw new BusinessException("通过uuid车辆不存在");
        }
        // 获取参数异常规则信息
        try {
            // 获取电子围栏规则
            ElectronicFenceModel electronicFence = electronicFenceService.get(entry.getRuleId());
            // 规则被禁用
            if (electronicFence.getStatusFlag()==0) {
                log.error("电子围栏规则[{}]已禁用不需要产生报警", entry.getRuleId());
                throw new BusinessException("电子围栏规则已禁用不需要产生报警");
            }

            //平台的父亲规则id为默认为：0
            entry.setParentRuleId("0");
            // 围栏名称
            entry.setRuleName(electronicFence.getName());
            // 响应类型
            entry.setResponseMode(electronicFence.getResponseMode()+"");
            //未处理
            entry.setProcesStatus(1);
            //未推送
            entry.setPushStatus(1);
        } catch (BusinessException e) {
            log.error("====== ruleId为：{} 的电子围栏规则不存在或者规则已被禁用, msgId: {}", entry.getRuleId(), entry.getMsgId());
            throw new BusinessException("电子围栏规则不存在或者规则被禁用");
        }
    }
}

