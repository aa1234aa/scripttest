package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.GpsUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.dc.dao.DataItemMapper;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.fault.dao.AlarmInfoMapper;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.enums.StopFaultAlarmModeEnum;
import com.bitnei.cloud.fault.model.AlarmInfoModel;
import com.bitnei.cloud.fault.model.AlarmMakerModel;
import com.bitnei.cloud.fault.model.CodeRuleModel;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.IAlarmProcessService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.fault.service.ISendFaultAlarmMessageService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.sys.domain.Unit;
import com.bitnei.cloud.sys.model.InstructSendRuleModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.model.VehOwnerModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.service.impl.UnitService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmInfoService实现<br>
 * 描述： AlarmInfoService实现<br>
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
 * <td>2019-03-01 16:23:08</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.AlarmInfoMapper")
public class AlarmInfoService extends BaseService implements IAlarmInfoService {

    @Autowired
    private IVehOwnerService vehOwnerService;

    @Autowired
    private UnitService unitService;

    @Resource
    private IVehModelService vehModelService;

    @Resource
    private DataItemMapper dataItemMapper;

    @Autowired
    private ICodeRuleService codeRuleService;

    @Autowired
    private RealDataClient realDataClient;

    @Resource
    private AlarmInfoMapper alarmInfoMapper;

    @Autowired
    private IAlarmInfoHistoryService alarmInfoHistoryService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private ISendFaultAlarmMessageService sendFaultAlarmMessageService;

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IAlarmProcessService alarmProcessService;

    @Value("${fault.push.jiguang.switch:false}")
    private boolean pushSwitch;

    /**
     * 结束报警方式: 0=自动结束(状态=已结束; 默认), 1=处理结束(状态=已结束 && 处理状态=已处理)
     */
    @Value("${stop.fault.alarm.mode:0}")
    private int stopFaultAlarmMode;

    @Override
    public Object list(PagerInfo pagerInfo) {
        long beginTime = System.currentTimeMillis();
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<AlarmInfo> entries = findBySqlId("pagerModel", params);
            log.debug("已获取当前报警数据非分页查询结果, 查询耗时(ms): {}", System.currentTimeMillis() - beginTime);
            List<AlarmInfoModel> models = new ArrayList<>();
            for (AlarmInfo entry : entries) {
                AlarmInfoModel model = AlarmInfoModel.fromEntry(entry);
                model.setResponseModeDisplay(dictService.getDictNames(entry.getResponseMode(), "RESPONSE_MODE"));
                injectSysVehicleInfo(entry, model);
                models.add(model);
            }
            log.debug("即将返回当前报警数据非分页查询结果, 方法执行耗时(ms): {}", System.currentTimeMillis() - beginTime);
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            log.debug("已获取当前报警数据分页查询结果, 查询耗时(ms): {}", System.currentTimeMillis() - beginTime);
            List<AlarmInfoModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                AlarmInfo obj = (AlarmInfo) entry;
                AlarmInfoModel model = AlarmInfoModel.fromEntry(obj);
                model.setResponseModeDisplay(dictService.getDictNames(obj.getResponseMode(), "RESPONSE_MODE"));
                injectSysVehicleInfo(obj, model);
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            log.debug("即将返回当前报警数据分页查询结果, 方法执行耗时(ms): {}", System.currentTimeMillis() - beginTime);
            return pr;
        }
    }

    private void injectSysVehicleInfo(AlarmInfo info, AlarmInfoModel model) {
        if (StringUtils.isNotBlank(info.getSellForField())) {
            if ("1".equals(info.getSellForField())) {
                //购车个人
                if (StringUtils.isNotBlank(info.getSellPriVehOwnerId())) {

                    //获取当权限的map
                    VehOwnerModel entry = vehOwnerService.get(info.getSellPriVehOwnerId());
                    if (null != entry) {
                        model.setSellUnitOrVita(entry.getOwnerName());
                        model.setTelephone(entry.getTelPhone());
                    }
                }
            } else {
                //购车单位
                if (StringUtils.isNotBlank(info.getSellPubUnitId())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
                    params.put("id", info.getSellPubUnitId());
                    Unit entry = unitService.unique("findById", params);
                    if (null != entry) {
                        model.setSellUnitOrVita(entry.getName());
                        model.setTelephone(entry.getTelephone());
                    }
                }
            }
        } else {
            model.setSellUnitOrVita("");
            model.setTelephone("");
        }
        //车辆型号
        //model.setVehModelName(vehModelService.getVehModelModelNames(model.getVehModelId()));
        setVehModelName(model);

        //车辆是否删除
        if (info.getVehDelete().equals(1)) {
            model.setVin(info.getVin() + "(车辆已删除)");
        }
    }

    @Override
    public AlarmInfoModel get(String id) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.put("id", id);
        AlarmInfo entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("查无此报警详情, 可能已结束报警, 可到'历史报警提醒'页面查看详情");
        }
        AlarmInfoModel model = AlarmInfoModel.fromEntry(entry);
        injectSysVehicleInfo(entry, model);
        model.setResponseModeDisplay(dictService.getDictNames(entry.getResponseMode(), "RESPONSE_MODE"));
        //实时地理位置、偏移量处理
        model.setAddress("");
        model.setAlarmAddress("");

        VehicleModel vehicleModel = vehicleService.getByVin(model.getVin());
        if (StringUtils.isNotBlank(entry.getUuid())) {

            DataRequest dataRequest = new DataRequest();
            dataRequest.setReadMode(DataReadMode.GPS_ADDRESS | DataReadMode.GPS_OFFSET_GD);
            dataRequest.setVid(model.getUuid());
            if (vehicleModel.isGb()) {
                dataRequest.setColumns(Arrays.asList(DataItemKey.Lng, DataItemKey.Lat));
                dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
                Map<String, String> map = realDataClient.findByUuid(dataRequest);
                if (!map.isEmpty() && StringUtils.isNotBlank(map.get(DataItemKey.Lng + ".gd"))
                        && StringUtils.isNotBlank(map.get(DataItemKey.Lat + ".gd"))) {
                    model.setRealTimeLongitude(Double.parseDouble(map.get(DataItemKey.Lng + ".gd")));
                    model.setRealTimeLatitude(Double.parseDouble(map.get(DataItemKey.Lat + ".gd")));
                    model.setAddress(StringUtils.isNotBlank(map.get("address")) ? map.get("address") : "--");
                }
            }
            if (vehicleModel.isG6()) {
                dataRequest.setColumns(Arrays.asList(DataItemKey.G6_ITEM_60041, DataItemKey.G6_ITEM_60042));
                dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                Map<String, String> map = realDataClient.findByUuid(dataRequest);
                if (!map.isEmpty() && StringUtils.isNotBlank(map.get(DataItemKey.G6_ITEM_60041 + ".gd"))
                        && StringUtils.isNotBlank(map.get(DataItemKey.G6_ITEM_60042 + ".gd"))) {
                    model.setRealTimeLongitude(Double.parseDouble(map.get(DataItemKey.G6_ITEM_60041 + ".gd")));
                    model.setRealTimeLatitude(Double.parseDouble(map.get(DataItemKey.G6_ITEM_60042 + ".gd")));
                    model.setAddress(StringUtils.isNotBlank(map.get("address")) ? map.get("address") : "--");
                }
            }
        }
        if (model.getLongitude() != null && model.getLatitude() != null) {
            String alarmAddress = GpsUtil.getAddress(model.getLongitude().toString(),
                    model.getLatitude().toString());
            model.setAlarmAddress(StringUtils.isNotBlank(alarmAddress) ? alarmAddress : "--");
        }
        // 设置故障触发项文字描述
        String triggerItemDisplay = assembleTriggerItemDisplay(model);
        model.setTriggerItem(triggerItemDisplay);
        return model;
    }

    /**
     * 组装故障触发项文字描述
     *
     * @param model 故障报警Model
     * @return
     */
    private String assembleTriggerItemDisplay(AlarmInfoModel model) {
        String display;
        try {
            display = model.getTriggerItem();
            if (StringUtils.isNotBlank(display)) {
                if (model.getFaultType().equals(FaultTypeEnum.PARAMETER.getValue())) {
                    VehModelModel vehModel = vehModelService.getByVehicleId(model.getVehicleId());
                    //获取当权限的map
                    Map<String, Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
                    params.put("ruleTypeId", vehModel.getRuleId());
                    List<DataItem> dataItemList = dataItemMapper.pagerModel(params);
                    for (DataItem dataItem : dataItemList) {
                        display = display.replaceAll("d" + dataItem.getSeqNo(), dataItem.getName())
                                .replaceAll("c" + dataItem.getSeqNo(), "上一帧" + dataItem.getName());
                    }
                } else if (model.getFaultType().equals(FaultTypeEnum.CODE.getValue())) {
                    CodeRuleModel codeRule = codeRuleService.get(model.getParentRuleId());
                    display = codeRule.getFaultName() + ":" + display;
                }
            }
        } catch (Exception e) {
            log.error("组装故障触发项文字描述出现异常, 故障报警ID:{}, 异常信息:{}", model.getId(), e.getMessage());
            display = model.getTriggerItem();
        }
        return display;
    }

    @Override
    public AlarmInfo insert(AlarmInfo alarmInfo) {
        alarmInfo.setId(UtilHelper.getUUID());
        alarmInfo.setCreateTime(DateUtil.getNow());
        int res = super.insert(alarmInfo);
        if (res == 0) {
            throw new BusinessException("添加故障信息失败");
        }
        return alarmInfo;
    }

    @Override
    public void update(AlarmInfoModel model) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_alarm_info", "fai");
        AlarmInfo obj = new AlarmInfo();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        super.updateByMap(params);
    }

    @Override
    public void updateAlarm(Map<String, Object> updateMap) {
        if (MapUtils.isNotEmpty(updateMap)) {
            alarmInfoMapper.updateAlarm(updateMap);
        }
    }

    @Override
    public void updateProcesStatus(String[] ids, Integer procesStatus) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_alarm_info", "fai");
        params.put("ids", ids);
        params.put("procesStatus", procesStatus);
        params.put("procesTime", DateUtil.getNow());
        alarmInfoMapper.updateProcesStatus(params);
    }

    @Override
    public void updateAlarmLevel(String id, int alarmLevel) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_alarm_info", "fai");
        params.put("id", id);
        params.put("alarmLevel", alarmLevel);
        alarmInfoMapper.updateAlarmLevel(params);
    }

    @Override
    public void stopAlarmByMsgId(String kafkaMsgId,String faultEndTime) {
        // 同步到历史数据表中
        AlarmInfo alarmInfo = alarmInfoMapper.findByMsgId(kafkaMsgId);
        if (null != alarmInfo) {
            alarmInfo.setFaultEndTime(faultEndTime);
            stopAlarm(alarmInfo, null);
        } else {
            log.error("结束实时故障信息时, 通过msgId查询无数据, 检查storm传来的值是否正确.    msgId : " + kafkaMsgId);
        }
    }

    /**
     * 结束报警
     *
     * @param alarmInfo 当前报警实例
     * @param msg       结束原因
     */
    private void stopAlarm(AlarmInfo alarmInfo, String msg) {
        //获取当权限的map
        String faultEndTime = DateUtil.getNow();
        if (StringUtils.isNotBlank(alarmInfo.getFaultEndTime())){
            faultEndTime=alarmInfo.getFaultEndTime();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("msgId", alarmInfo.getMsgId());
        params.put("faultStatus", 2);
        params.put("faultEndTime", faultEndTime);
        // 首先更新当前报警状态
        alarmInfoMapper.updateByMsgId(params);

        // 判断配置的结束报警方式
//        log.info("结束报警方式:{}", stopFaultAlarmMode);

        if (StopFaultAlarmModeEnum.AUTO_STOP.getValue() == stopFaultAlarmMode) {
            AlarmInfoHistory alarmInfoHistory = new AlarmInfoHistory();
            BeanUtils.copyProperties(alarmInfo, alarmInfoHistory);
            alarmInfoHistory.setFaultStatus(2);
            alarmInfoHistory.setFaultEndTime(faultEndTime);
            if (StringUtils.isNotBlank(msg)) {
                alarmInfoHistory.setRuleName(alarmInfoHistory.getRuleName() + msg);
            }
            log.info("即将归档至大数据历史报警中! msgId:{}", alarmInfoHistory.getMsgId());
            boolean result = alarmInfoHistoryService.insert(Lists.newArrayList(alarmInfoHistory));
            // 成功同步至大数据历史报警则删除本地当前报警数据
            if (result) {
                log.info("==================同步到历史数据表(大数据)中、并删除[{}] ", alarmInfoHistory.getId());
                alarmInfoMapper.deleteById(alarmInfoHistory.getId());
                alarmProcessService.deleteByFaultAlarmId(alarmInfoHistory.getFaultBeginTime(), alarmInfoHistory.getId());
            }
        }
    }

    /**
     * 批量结束报警
     *
     * @param alarmInfos 当前报警实例
     * @param msg
     */
    private void stopAlarmBatch(List<AlarmInfo> alarmInfos, String msg) {
        String faultEndTime = DateUtil.getNow();
        Map<String, Object> params = new HashMap<>();
        params.put("msgIds", alarmInfos.stream().map(AlarmInfo::getMsgId).collect(Collectors.toList()));
        params.put("faultStatus", 2);
        params.put("faultEndTime", faultEndTime);
        // 首先批量更新当前报警状态
        alarmInfoMapper.updateByMsgIds(params);

        // 批量处理停止报警
        if (StopFaultAlarmModeEnum.AUTO_STOP.getValue() == stopFaultAlarmMode) {

            List<AlarmInfoHistory> alarmInfoHistories = alarmInfos.stream().map(alarmInfo ->
                    buildAlarmInfoHistory(alarmInfo, msg, faultEndTime)).collect(Collectors.toList());
            boolean result = alarmInfoHistoryService.insert(alarmInfoHistories);

            if (result) {
                List<String> ids = alarmInfoHistories.stream().map(AlarmInfoHistory::getId)
                        .collect(Collectors.toList());
                log.info("==================同步到历史数据表(大数据)中、并删除[{}] ", ids);
                alarmInfoMapper.deleteBatch(ids);

                //分表的删除暂时不优化
                alarmInfoHistories.forEach(it ->
                        alarmProcessService.deleteByFaultAlarmId(it.getFaultBeginTime(), it.getId()));

            }
        }
    }

    private AlarmInfoHistory buildAlarmInfoHistory(AlarmInfo alarmInfo, String msg, String faultEndTime) {
        AlarmInfoHistory alarmInfoHistory = new AlarmInfoHistory();
        BeanUtils.copyProperties(alarmInfo, alarmInfoHistory);
        alarmInfoHistory.setFaultStatus(2);
        alarmInfoHistory.setFaultEndTime(faultEndTime);
        if (StringUtils.isNotBlank(msg)) {
            alarmInfoHistory.setRuleName(alarmInfoHistory.getRuleName() + msg);
        }
        return alarmInfoHistory;
    }

    /**
     * 通过车辆的vid和电子围栏规则id 结束报警
     *
     * @param uuid
     * @param ruleId 电子围栏规则id
     */
    @Override
    public void stopByFenceAlarm(String uuid, String ruleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        if (null != uuid && !"".equals(uuid)) {
            params.put("uuid", uuid);
        }
        params.put("ruleId", ruleId);
        // 指定类型为电子围栏
        params.put("faultType", "3");
        List<AlarmInfo> entries = findBySqlId("pagerModel", params);
        if (null != entries && entries.size() > 0) {
//            for (AlarmInfo alarmInfo : entries) {
//                // 结束报警
//                stopAlarm(alarmInfo, null);
//            }

            //改成批量
            stopAlarmBatch(entries, null);
        }
    }

    /**
     * 批量结束报警
     *
     * @param alarmInfos 将结束报警集合
     * @param msg        结束原因
     */
    private void stopAlarm(List<AlarmInfo> alarmInfos, String msg) {
        if (CollectionUtils.isNotEmpty(alarmInfos)) {
            for (AlarmInfo alarmInfo : alarmInfos) {
                stopAlarm(alarmInfo, msg);
            }
        }
    }

    @Override
    public void stopAlarmByRuleId(String ruleId, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("ruleId", ruleId);
        List<AlarmInfo> alarmInfos = alarmInfoMapper.pagerModel(map);
        stopAlarm(alarmInfos, msg);
    }

    @Override
    public void stopAlarmByParentRuleId(String parentRuleId, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentRuleId", parentRuleId);
        List<AlarmInfo> alarmInfos = alarmInfoMapper.pagerModel(map);
        stopAlarm(alarmInfos, msg);
    }

    @Override
    public void stopAlarmByVehicleId(String vehicleId, String msg) {
        // 获取该车辆当前所有报警消息
        Map<String, Object> map = new HashMap<>();
        map.put("vehicleId", vehicleId);
        List<AlarmInfo> alarmInfos = alarmInfoMapper.pagerModel(map);
        stopAlarm(alarmInfos, msg);
    }

    @Override
    public void stopCodeAlarmByVehModelId(String vehModelId, String parentRuleId, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("vehModelId", vehModelId);
        map.put("faultType", FaultTypeEnum.CODE.getValue());
        map.put("parentRuleId", parentRuleId);
        List<AlarmInfo> alarmInfos = alarmInfoMapper.pagerModel(map);
        stopAlarm(alarmInfos, msg);
    }

    @Override
    public void stopParamAlarmByVehModelId(String vehModelId, String ruleId, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("vehModelId", vehModelId);
        map.put("faultType", FaultTypeEnum.PARAMETER.getValue());
        map.put("ruleId", ruleId);
        List<AlarmInfo> alarmInfos = alarmInfoMapper.pagerModel(map);
        stopAlarm(alarmInfos, msg);
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<AlarmInfo>(this, "pagerModel", params, "fault/res/alarmInfo/export.xls", "故障信息", AlarmInfoModel.class) {
            @Override
            public Object process(AlarmInfo entity) {
                //车辆型号
                AlarmInfoModel model = (AlarmInfoModel) super.process(entity);
                model.setResponseModeDisplay(dictService.getDictNames(entity.getResponseMode(), "RESPONSE_MODE"));
                //model.setVehModelName(vehModelService.getVehModelModelNames(entity.getVehModelId()));
                setVehModelName(model);
                return model;
            }
        }.work();
    }

    @Override
    public Map<String, Integer> statistic() {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        Map<String, BigDecimal> processMap = alarmInfoMapper.processTotal(params);
        Map<String, BigDecimal> faultTypeMap = alarmInfoMapper.faultTypeTotal(params);

        Map<String, Integer> map = new HashMap<>();

        int unprocessTotal = ((processMap != null && !processMap.isEmpty()) ? processMap.get("unprocessTotal").intValue() : 0);

        int processingTotal = ((processMap != null && !processMap.isEmpty()) ? processMap.get("processingTotal").intValue() : 0);

        int processedTotal = ((processMap != null && !processMap.isEmpty()) ? processMap.get("processedTotal").intValue() : 0);

        map.put("unprocessTotal", unprocessTotal);
        map.put("processingTotal", processingTotal);
        map.put("processedTotal", processedTotal);

        int parameterTotal = ((faultTypeMap != null && !faultTypeMap.isEmpty()) ? faultTypeMap.get("parameterTotal").intValue() : 0);
        int faultCodeTotal = ((faultTypeMap != null && !faultTypeMap.isEmpty()) ? faultTypeMap.get("faultCodeTotal").intValue() : 0);
        int fenceTotal = ((faultTypeMap != null && !faultTypeMap.isEmpty()) ? faultTypeMap.get("fenceTotal").intValue() : 0);

        map.put("parameterTotal", parameterTotal);
        map.put("faultCodeTotal", faultCodeTotal);
        map.put("fenceTotal", fenceTotal);
        return map;
    }

    @Override
    public void pushAlarmInfo() {
        List<AlarmInfo> list = alarmInfoMapper.list();
        log.info("====定时任务推送====");
        for (AlarmInfo info : list) {
            sendMessageToJiGuang(info);
        }
    }

    @Override
    public void sendFaultAlarmInfo(AlarmInfo alarmInfo) {
        if (StringUtils.isEmpty(alarmInfo.getResponseMode())) {
            //防御
            alarmInfo.setResponseMode("");
        }
        sendMessageToWeb(alarmInfo);
        sendMessageToJiGuang(alarmInfo);
        // 发送短信
        sendSms(alarmInfo);
    }

    /**
     * WEB推送
     *
     * @param alarmInfo
     */
    private void sendMessageToWeb(AlarmInfo alarmInfo) {
        String[] responseMode = alarmInfo.getResponseMode().split(",");
        List<String> modes = Arrays.asList(responseMode);
        try {
            //推送给web
            boolean flag = modes.contains("1") || modes.contains("2");
            if (flag) {
                sendFaultAlarmMessageService.sendMessageToWeb(alarmInfo);
            }
        } catch (Exception e) {
            log.error("推送故障报警消息到web出错：", e);
        }
    }

    /**
     * 极光推送
     *
     * @param alarmInfo
     */
    private void sendMessageToJiGuang(AlarmInfo alarmInfo) {
        String[] responseMode = alarmInfo.getResponseMode().split(",");
        List<String> modes = Arrays.asList(responseMode);
        try {
            //极光推送
            if (pushSwitch) {
                //log.info("====pushSwitch:===={}", pushSwitch);
                //响应方式为3 则是app推送
                boolean flag = modes.contains("3");
                if (flag) {
                    sendFaultAlarmMessageService.sendMessageToJiGuang(alarmInfo);
                    //修改状态
                    AlarmInfoModel model = new AlarmInfoModel();
                    model.setId(alarmInfo.getId());
                    model.setPushStatus(2);
                    model.setPushTime(com.bitnei.cloud.sys.util.DateUtil.getNow());
                    update(model);
                }
            }
        } catch (Exception e) {
            log.error("推送故障报警消息到极光出错：", e);
        }
    }

    /**
     * 短信推送
     * @param alarmInfo
     */
    public void sendSms(AlarmInfo alarmInfo) {
        String[] responseMode = alarmInfo.getResponseMode().split(",");
        List<String> modes = Arrays.asList(responseMode);
        try {
            //短信推送
            boolean flag = modes.contains("4");
            if (flag) {
                sendFaultAlarmMessageService.sendSms(alarmInfo);
            }
        } catch (Exception e) {
            log.error("短信推送故障报警消息出错：", e);
        }
    }

    @Override
    public AlarmMakerModel getAlarmMakerInfo(String id) {

        AlarmMakerModel alarmMakerModel = new AlarmMakerModel();

        AlarmInfoModel alarmInfoModel = get(id);
        BeanUtils.copyProperties(alarmInfoModel, alarmMakerModel);

        //故障取实时经纬度
        alarmMakerModel.setLatitude(alarmInfoModel.getRealTimeLatitude());
        alarmMakerModel.setLongitude(alarmInfoModel.getRealTimeLongitude());

        alarmMakerModel.setAddress(alarmInfoModel.getAlarmAddress());

        //处理报警持续时间
        if (null != alarmMakerModel.getFaultBeginTime()) {
            Date beginDate = DateUtil.strToDate_ex_full(alarmMakerModel.getFaultBeginTime());
            if (null != alarmMakerModel.getFaultEndTime()) {
                Date endDate = DateUtil.strToDate_ex_full(alarmMakerModel.getFaultEndTime());
                alarmMakerModel.setFaultTime(InstructSendRuleModel.getDatePoor(beginDate, endDate, null));
            } else {
                alarmMakerModel.setFaultTime(InstructSendRuleModel.getDatePoor(beginDate, new Date(), null));
            }
        }

        return alarmMakerModel;
    }

    @Override
    public AlarmInfo findByMsgId(String msgId) {
        return alarmInfoMapper.findByMsgId(msgId);
    }

    private void setVehModelName(AlarmInfoModel model) {
        Map<String, String> map = vehModelService.getVehModelModelNames(model.getVehModelId());
        model.setVehModelName(map.get("vehModelName"));
        model.setVehModelId(map.get("vehModelId"));
    }

    @Override
    public List<AlarmInfoHistory> findStopAlarm(Map<String, Object> params) {
        return alarmInfoMapper.findStopAlarm(params);
    }

    @Override
    public void deleteBatch(List<String> ids) {
        alarmInfoMapper.deleteBatch(ids);
    }

}
