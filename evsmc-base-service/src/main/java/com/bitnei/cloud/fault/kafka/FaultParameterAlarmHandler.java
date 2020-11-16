package com.bitnei.cloud.fault.kafka;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.model.KafkaFaultAlarmModel;
import com.bitnei.cloud.fault.model.ParameterRuleModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.IParameterRuleService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc： 参数故障信息入库处理
 * @Author: joinly
 * @Date: 2019/3/9
 */
@Slf4j
@Service
public class FaultParameterAlarmHandler extends AbstractKafkaHandler {

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IParameterRuleService parameterRuleService;

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Override
    public boolean handle(String message) {
        log.info("========开始处理参数故障信息========");
        log.info("========故障信息: {}", message);
        Gson gson = new Gson();
        KafkaFaultAlarmModel model = gson.fromJson(message, KafkaFaultAlarmModel.class);
        if ("1".equals(model.getStatus())) {
            AlarmInfo entry = model.fromEntry();

            //入库之前通过msgId检查消息是否已经存在
            AlarmInfo info = alarmInfoService.findByMsgId(model.getMsgId());
            try {
                if (null != info) {
                    // 已存在报警判断是否报警等级提高, 是则更新
                    // 或预置19项里等级大于0，并且原记录是异常等级数据，也更新
                    int newAlarmLevel = entry.getAlarmLevel();
                    if (newAlarmLevel > info.getAlarmLevel() ||
                            (newAlarmLevel > 0 && newAlarmLevel < 4 && info.getAlarmLevel().equals(9999))) {
                        alarmInfoService.updateAlarmLevel(info.getId(), newAlarmLevel);
                        log.error("故障报警等级已提高！msgId：{}", model.getMsgId());
                        // 修改entry变量为最新信息, 以便后续推送报警使用
                        info.setAlarmLevel(newAlarmLevel);
                        info.setVin(model.getVin());
                        entry = info;
                        entry.setId(info.getId());

                        // 判断是否预置参数19项，如果是则判断报警等级是否1级以上，修改对应的报警提醒响应方式
                        // 特殊处理
                        ParameterRuleModel parameterRule = parameterRuleService.get(entry.getRuleId());

                        // 判断是否预设19项通用报警，19项通用报警配置等级为0
                        // 非19项的采用配置的等级

                        if (parameterRule.getPresetRule().equals(Constants.BoolType.yes.getValue()) &&
                                null == parameterRule.getAlarmLevel()) {
                            //如果为19项上报的故障等级大于1，则需要更新响应方式为向web响应
                            if (entry.getAlarmLevel() == 2) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", entry.getId());
                                params.put("ruleId", entry.getRuleId());
                                params.put("responseMode", "1,2");
                                alarmInfoService.updateAlarm(params);

                                //需要提醒
                                entry.setResponseMode("1,2");
                            }
                            //如果为19项上报的故障等级大于2，则需要更新响应方式为向web响应
                            if (entry.getAlarmLevel() > 2) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", entry.getId());
                                params.put("ruleId", entry.getRuleId());
                                params.put("responseMode", "1,2,3,4,5");
                                alarmInfoService.updateAlarm(params);

                                //需要提醒
                                entry.setResponseMode("1,2,3,4,5");
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    //开始
                    entry.setFaultType(1);
                    entry.setFaultStatus(1);
                    entry.setFaultBeginTime(DateUtil.converseStr(model.getBeginTime()));
                    buildEntry(entry);
                    //入库
                    entry = alarmInfoService.insert(entry);
                }
                //报警推送
                log.info("========kafka======参数故障信息推送=======");
                alarmInfoService.sendFaultAlarmInfo(entry);
            } catch (Exception e) {
                log.error("处理参数故障信息报错：", e);
            }
        } else if ("3".equals(model.getStatus())) {
            try {
                //结束报警
                alarmInfoService.stopAlarmByMsgId(model.getMsgId(), DateUtil.converseStr(model.getEndTime()));
            } catch (Exception e) {
                log.error("处理参数故障结束报错：", e);
            }
        }
        log.info("========结束处理参数故障信息========");
        return false;
    }

    private void buildEntry(AlarmInfo entry) {
        // 获取车辆信息
        try {
            VehicleModel model = vehicleService.getByUuid(entry.getUuid());
            entry.setVehicleId(model.getId());
            entry.setLicensePlate(model.getLicensePlate());
            entry.setVin(model.getVin());
        } catch (BusinessException e) {
            log.error("====== uuid为：{} 的车辆不存在,msgId: {} ======", entry.getUuid(), entry.getMsgId());
            throw new BusinessException("通过uuid车辆不存在");
        }
        // 获取参数异常规则信息
        try {
            ParameterRuleModel parameterRule = parameterRuleService.get(entry.getRuleId());

            // 判断是否预设19项通用报警，19项通用报警配置等级为0
            // 非19项的采用配置的等级
            boolean isSystem = parameterRule.getPresetRule().equals(Constants.BoolType.yes.getValue()) &&
                    null == parameterRule.getAlarmLevel();

            if (isSystem) {
                //如果是19项上报的故障等级是0，则为异常数据
                if (0 == entry.getAlarmLevel() || entry.getAlarmLevel() > 3) {
                    entry.setAlarmLevel(9999);
                }

            } else {
                //否则取配置的报警等级
                entry.setAlarmLevel(parameterRule.getAlarmLevel());
            }

            //平台的父亲规则id为默认为：0
            entry.setParentRuleId("0");
            entry.setRuleName(parameterRule.getName());
            entry.setResponseMode(parameterRule.getResponseMode());

            //特殊处理，如果上报的报警等级是1或者是异常等级，都不响应提醒到web
            if (isSystem) {
                if (entry.getAlarmLevel().equals(1) || entry.getAlarmLevel().equals(9999)) {
                    entry.setResponseMode("");
                }

                if (entry.getAlarmLevel() == 2) {
                    entry.setResponseMode("1,2");
                }
                //如果为19项上报的故障等级大于2，则需要更新响应方式为向web响应
                if (entry.getAlarmLevel() > 2 && entry.getAlarmLevel() < 9999) {
                    entry.setResponseMode("1,2,3,4,5");
                }
            }

            //未处理
            entry.setProcesStatus(1);
            //未推送
            entry.setPushStatus(1);
        } catch (BusinessException e) {
            log.error("====== ruleId为：{} 的参数异常规则不存在或者规则已被删除, msgId: {}", entry.getRuleId(), entry.getMsgId());
            throw new BusinessException("参数异常规则不存在或者规则被删除");
        }
    }
}

