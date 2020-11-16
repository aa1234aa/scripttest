package com.bitnei.cloud.fault.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.Exception.CancelRequestException;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.AlarmClient;
import com.bitnei.cloud.common.client.model.AlarmExportReq;
import com.bitnei.cloud.common.client.model.AlarmGetReq;
import com.bitnei.cloud.common.client.model.AlarmProcessReq;
import com.bitnei.cloud.common.client.model.AlarmSearchReq;
import com.bitnei.cloud.common.client.model.ElasticsearchPageResult;
import com.bitnei.cloud.common.client.model.FieldSortBuilder;
import com.bitnei.cloud.common.client.model.GlobalResponse;
import com.bitnei.cloud.common.client.model.Location;
import com.bitnei.cloud.common.client.model.SortOrder;
import com.bitnei.cloud.common.handler.AbstractOfflineExportCsvHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.OfflineExportCancel;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.handler.ServiceBase;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.GpsUtil;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.dc.dao.DataItemMapper;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.fault.dao.AlarmInfoHistoryMapper;
import com.bitnei.cloud.fault.dao.AlarmProcessMapper;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.domain.AlarmProcess;
import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.model.AlarmInfoHistoryModel;
import com.bitnei.cloud.fault.model.AlarmProcessModel;
import com.bitnei.cloud.fault.model.CodeRuleModel;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.model.VehicleInfoModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmInfoHistoryService实现<br>
 * 描述： AlarmInfoHistoryService实现<br>
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
 * <td>2019-03-04 20:02:06</td>
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
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.AlarmInfoHistoryMapper")
public class AlarmInfoHistoryService extends ServiceBase implements IAlarmInfoHistoryService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Resource
    private AlarmInfoHistoryMapper alarmInfoHistoryMapper;

    @Autowired
    private IOfflineExportService offlineExportService;

    @Resource
    private IVehModelService vehModelService;

    @Resource
    private DataItemMapper dataItemMapper;

    @Autowired
    private ICodeRuleService codeRuleService;

    @Autowired
    private AlarmClient alarmClient;

    @Autowired
    private AlarmProcessMapper alarmProcessMapper;

    @Autowired
    private IVehicleService vehicleService;

    /**
     * 大数据查询历史报警数据列表接口所需返回字段--基础字段
     */
    private static final String[] SOURCE_INCLUDES_BASE = {"id", "faultType", "parentRuleId", "ruleId", "ruleName",
            "alarmLevel", "faultBeginTime", "faultEndTime", "faultStatus", "faultLocation", "responseMode",
            "triggerItem", "processStatus", "processTime", "pushStatus", "pushTime", "faultCreateTime", "msgId",
            "vehicleId", "vin", "vid", "licensePlate", "vehModelName"};

    /**
     * 大数据查询历史报警数据详情接口所需返回字段--处理记录
     */
    private static final String[] SOURCE_INCLUDES_PROCESSES = {"alarmProcesses"};

    /**
     * 大数据查询历史报警数据详情接口所需返回字段--全部字段
     */
    private static final String[] SOURCE_INCLUDES_ALL = ArrayUtils.addAll(SOURCE_INCLUDES_BASE, SOURCE_INCLUDES_PROCESSES);

    @Override
    public boolean insert(List<AlarmInfoHistory> alarmInfoHistoryList) {
        long time = System.currentTimeMillis();
        boolean result;
        try {
            final List<com.bitnei.cloud.common.client.model.AlarmInfoHistory> bigDataList =
                    Collections.synchronizedList(new ArrayList<>());

            alarmInfoHistoryList.stream().parallel().forEach(alarmInfoHistory -> {
                // 进行对象实体转换
                com.bitnei.cloud.common.client.model.AlarmInfoHistory bigDataAlarmInfoHistory =
                        convertBigDataModel(alarmInfoHistory);
                bigDataList.add(bigDataAlarmInfoHistory);
            });

            // 调用大数据保存接口
            com.bitnei.cloud.common.client.model.AlarmInfoHistory[] bigDataArray =
                    bigDataList.toArray(new com.bitnei.cloud.common.client.model.AlarmInfoHistory[0]);

            GlobalResponse<Boolean> response = alarmClient.add(bigDataArray);
            result = response.getData();
        } catch (Exception e) {
            log.error("推送历史报警数据至大数据存储异常!", e);
            result = false;
        }
        log.debug("结束推送历史报警数据至大数据存储, 调用结果:{}, 耗时(ms):{}", result, System.currentTimeMillis() - time);
        return result;
    }

    @Override
    public boolean alarmProcess(AlarmProcessModel model) {
        boolean result;
        String[] faultAlarmIdArray = model.getFaultAlarmIds().split(",");
        model.setCreateTime(DateUtil.getNow());
        model.setCreateBy(ServletUtil.getCurrentUser());
        // 历史报警提醒添加处理记录时，应该保留之前的处理状态和是否再次提醒
//        if (model.getAgainRemindStatus() == null && model.getProcesStatus() == null){
//            List<AlarmProcessModel> listModel = this.getAlarmProcess(model.getFaultAlarmIds());
//            if (listModel != null && listModel.size() > 0){
//                model.setAgainRemindStatus(listModel.get(0).getAgainRemindStatus());
//                model.setProcesStatus(listModel.get(0).getProcesStatus());
//            }
//        }
        com.bitnei.cloud.common.client.model.AlarmProcess bigDataAlarmProcess = convertBigDataProcessModel(model);
        AlarmProcessReq alarmProcessReq = new AlarmProcessReq();
        alarmProcessReq.setIds(faultAlarmIdArray);
        alarmProcessReq.setAlarmProcess(bigDataAlarmProcess);
        try {
            GlobalResponse<Boolean> response = alarmClient.alarmProcess(alarmProcessReq);
            result = response.getData();
        } catch (Exception e) {
            log.error("调用大数据插入历史报警处理记录异常!", e);
            result = false;
        }
        return result;
    }

    @Override
    public PagerResult list(PagerInfo pagerInfo) {
        long time = System.currentTimeMillis();
        PagerResult result = new PagerResult();
        AlarmSearchReq alarmSearchReq = convertQueryModel(pagerInfo, SOURCE_INCLUDES_ALL);
        long requestTime = System.currentTimeMillis();
        GlobalResponse<ElasticsearchPageResult<com.bitnei.cloud.common.client.model.AlarmInfoHistory>> response =
                alarmClient.findPage(alarmSearchReq);
        log.debug("已调用大数据分页查询接口, 耗时(ms): {}", System.currentTimeMillis() - requestTime);
        if (null == response || null == response.getData() || CollectionUtils.isEmpty(response.getData().getData())) {
            result.setTotal(0L);
            result.setData(new ArrayList());
            return result;
        }
        ElasticsearchPageResult<com.bitnei.cloud.common.client.model.AlarmInfoHistory> pageResult = response.getData();
        List<AlarmInfoHistoryModel> modelList = convertModel(pageResult.getData());
        result.setTotal(pageResult.getTotalCount());
        result.setData(Collections.singletonList(modelList));
        log.debug("即将返回大数据历史报警查询结果, 总耗时(ms):{}", System.currentTimeMillis() - time);
        return result;
    }

    @Deprecated
    public Object _list(PagerInfo pagerInfo) {
        long beginTime = System.currentTimeMillis();
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<AlarmInfoHistory> entries = findBySqlId("pagerModel", params);
            log.debug("已获取历史报警数据非分页查询结果, 查询耗时(ms): {}", System.currentTimeMillis() - beginTime);
            return fromEntityToModel(entries);
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            log.debug("已获取历史报警数据分页查询结果, 查询耗时(ms): {}", System.currentTimeMillis() - beginTime);
            List<AlarmInfoHistoryModel> models = fromEntityToModel(
                pr.getData()
                    .stream()
                    .map(entry -> (AlarmInfoHistory) entry)
                    .collect(Collectors.toList())
            );
            //地址
            for (AlarmInfoHistoryModel model : models) {
                model.setAddress("");
                if (model.getLongitude() != null && model.getLatitude() != null) {
                    String[] lngAndLat = GpsUtil.transformFromWGSToGCJ(model.getLongitude().toString(),
                        model.getLatitude().toString());
                    if (lngAndLat.length > 0 && !(StringUtils.isBlank(lngAndLat[0]) || StringUtils.isBlank(lngAndLat[1]))) {
                        model.setLongitude(Double.valueOf(lngAndLat[0]));
                        model.setLatitude(Double.valueOf(lngAndLat[1]));
                        String address = GpsUtil.getAddress(lngAndLat[0], lngAndLat[1]);
                        model.setAddress(StringUtils.isNotBlank(address) ? address : "--");
                    }
                }
            }
            pr.setData(Collections.singletonList(models));
            log.debug("即将返回历史报警数据分页查询结果, 方法执行耗时(ms): {}", System.currentTimeMillis() - beginTime);
            return pr;
        }
    }

    @Override
    public List<AlarmInfoHistory> findAllByTableName(String tableName) {
        return alarmInfoHistoryMapper.findAllByTableName(tableName);
    }

    @Override
    public List<AlarmInfoHistory> findPageByTableName(String tableName, String faultBeginTime,
                                                      int limit, String faultId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("faultBeginTime", faultBeginTime);
        params.put("limit", limit);
        params.put("faultId", faultId);
        return alarmInfoHistoryMapper.findPageByTableName(params);
    }

    private String duration(String beginTime, String endTime) {
        String duration = "";
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            duration = DateUtil.duration(beginTime, endTime);
        }
        return duration;
    }

    @Override
    public List<AlarmProcessModel> getAlarmProcess(String id) {
        AlarmGetReq alarmGetReq = new AlarmGetReq();
        alarmGetReq.setId(id);
        alarmGetReq.setSourceIncludes(SOURCE_INCLUDES_PROCESSES);
        GlobalResponse<com.bitnei.cloud.common.client.model.AlarmInfoHistory> response = alarmClient.get(alarmGetReq);
        List<AlarmProcessModel> processModelList = new ArrayList<>();
        if (null != response && null != response.getData() && null != response.getData().getAlarmProcesses()) {
            AlarmProcessModel alarmProcessModel;
            for (com.bitnei.cloud.common.client.model.AlarmProcess alarmProcess : response.getData().getAlarmProcesses()) {
                alarmProcessModel = convertProcessModel(alarmProcess);
                processModelList.add(alarmProcessModel);
            }
        }
        // 设置按创建时间降序排序
        return processModelList.stream().sorted(Comparator.comparing(AlarmProcessModel::getCreateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public AlarmInfoHistoryModel get(String id, String faultBeginTime) {
        long time = System.currentTimeMillis();
        AlarmGetReq alarmGetReq = new AlarmGetReq();
        alarmGetReq.setId(id);
        alarmGetReq.setSourceIncludes(SOURCE_INCLUDES_BASE);
        GlobalResponse<com.bitnei.cloud.common.client.model.AlarmInfoHistory> response = alarmClient.get(alarmGetReq);
        log.debug("已调用大数据获取历史报警详情接口, 耗时(ms): {}", System.currentTimeMillis() - time);
        if (null == response || null == response.getData()) {
            throw new BusinessException("对象已不存在");
        }
        // 对象实体转换
        com.bitnei.cloud.common.client.model.AlarmInfoHistory history = response.getData();
        AlarmInfoHistoryModel model = convertModel(history);
        // 设置车主信息
        setVehicleSellInfo(model);
        //地理位置
        if (model.getLongitude() != null && model.getLatitude() != null) {
            String address = GpsUtil.getAddress(String.valueOf(model.getLongitude()), String.valueOf(model.getLatitude()));
            model.setAddress(StringUtils.isNotBlank(address) ? address : "--");
        } else {
            model.setAddress("");
        }
        // 设置故障触发项文字描述
        String triggerItemDisplay = assembleTriggerItemDisplay(model);
        model.setTriggerItem(triggerItemDisplay);

        log.debug("即将返回大数据历史报警详情, 总耗时(ms):{}", System.currentTimeMillis() - time);
        return model;
    }

    /**
     * 组装故障触发项文字描述
     *
     * @param model 故障报警Model
     * @return
     */
    private String assembleTriggerItemDisplay(AlarmInfoHistoryModel model) {
        long time = System.currentTimeMillis();
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
                        display = display.replaceAll("d" + dataItem.getSeqNo(), dataItem.getName());
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
        log.trace("结束组装故障触发项文字描述, 耗时(ms): {}", System.currentTimeMillis() - time);
        return display;
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        pagerInfo.setStart(0);
        pagerInfo.setLimit(100);
        PagerResult result = list(pagerInfo);
        List<Object> data = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result.getData()) && null != result.getData().get(0)) {
            data = (List<Object>) result.getData().get(0);
        }
        DataLoader.loadNames(data);
        String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String srcFile = srcBase + "fault/res/alarmInfoHistory/export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("历史故障信息");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(data);
        String outName = String.format("%s-导出-%s.xls", "历史故障信息", DateUtil.getShortDate());
        EasyExcel.renderResponse(srcFile, outName, ed);
    }

    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "历史故障信息";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        Condition condition = new Condition();
        condition.setName("authCode");
        condition.setValue(DataAccessKit.getAuthCode("sys_vehicle", "sv"));
        pagerInfo.getConditions().add(condition);
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        // 创建离线导出任务
        return offlineExportService.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );
    }

    @Override
    public void exportOfflineProcessor(
        @NotNull final String taskId,
        @NotNull final String createBy,
        @NotNull final Date createTime,
        @NotNull final String exportFileName,
        @NotNull final String exportMethodParams) throws Exception {

        log.debug("执行离线导出任务:{}", exportFileName);

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);

        List<String> titleList = Arrays.asList("报警提醒名称", "处理状态", "提醒类型", "等级", "响应方式", "推送状态", "开始时间", "结束时间", "持续时长", "VIN", "车牌号", "车辆型号", "历史处理记录");
        // 查询条件
        AlarmSearchReq alarmSearchReq = convertQueryModel(pagerInfo, SOURCE_INCLUDES_ALL);
        AlarmExportReq alarmExportReq = new AlarmExportReq();
        BeanUtils.copyProperties(alarmSearchReq, alarmExportReq);

        new AbstractOfflineExportCsvHandler() {
            @Override
            protected long writeBody(final @NotNull OutputStreamWriter writer) throws IOException {
                long[] totalCount = new long[1];
                do {
                    try {
                        GlobalResponse<ElasticsearchPageResult<com.bitnei.cloud.common.client.model.AlarmInfoHistory>> response = alarmClient.exportPage(alarmExportReq);
                        if(null == response
                                || null == response.getData()
                                || CollectionUtils.isEmpty(response.getData().getData())){
                            log.debug("历史报警离线导出");
                            break;
                        }
                        ElasticsearchPageResult<com.bitnei.cloud.common.client.model.AlarmInfoHistory> pageResult =response.getData();
                        // 设置滚动id，导出时的游标标识
                        alarmExportReq.setScrollId(pageResult.getScrollId());

                        List<AlarmInfoHistoryModel> resultList = convertModel(pageResult.getData());
                        DataLoader.loadNames(resultList);
                        resultList.stream().forEach(alarmInfoHistoryModel -> {
                            List<String> valueList = new ArrayList<>();
                            valueList.add(alarmInfoHistoryModel.getRuleName());
                            valueList.add(alarmInfoHistoryModel.getProcesStatusDisplay());
                            valueList.add(alarmInfoHistoryModel.getFaultTypeDisplay());
                            valueList.add(alarmInfoHistoryModel.getAlarmLevelDisplay());
                            valueList.add(alarmInfoHistoryModel.getResponseModeDisplay());
                            valueList.add(alarmInfoHistoryModel.getPushStatusDisplay());
                            valueList.add(alarmInfoHistoryModel.getFaultBeginTime());
                            valueList.add(alarmInfoHistoryModel.getFaultEndTime());
                            valueList.add(alarmInfoHistoryModel.getDuration());
                            valueList.add(alarmInfoHistoryModel.getVin());
                            valueList.add(alarmInfoHistoryModel.getLicensePlate());
                            valueList.add(alarmInfoHistoryModel.getVehModelName());
                            valueList.add(alarmInfoHistoryModel.getProcessRecord());
                            try {
                                totalCount[0] += 1;
                                super.writeBody(writer, Collections.singletonList(valueList));
                            } catch (IOException ignore) {
                                log.error("历史报警离线导出 回调异常,中止本次导出");
                            }
                        });

                        final long progress = OfflineExportProgress.computeProgress(totalCount[0], pageResult.getTotalCount());
                        OfflineExportProgress.updateProgress(
                                redis,
                                createBy,
                                exportFileName,
                                progress
                        );
                        OfflineExportProgress.pushProgress(
                                ws,
                                taskId,
                                createBy,
                                "导出中",
                                OfflineExportStateMachine.EXPORTING,
                                progress,
                                ""
                        );
                    } catch (Exception e) {
                        log.error("历史报警离线导出异常,中止本次导出", e);
                        break;
                    }
                    if (OfflineExportCancel.existRequest(
                            redis,
                            createBy,
                            exportFileName
                    )) {
                        throw new CancelRequestException();
                    }
                } while (true);
                return totalCount[0];
            }
        }.csv(createBy, exportFileName, titleList);
    }

    @NotNull
    List<AlarmInfoHistoryModel> fromEntityToModel(final @NotNull List<AlarmInfoHistory> entities) {
        long beginTime = System.currentTimeMillis();
        final List<AlarmInfoHistoryModel> models = Lists.newArrayList();
        for (final AlarmInfoHistory entity : entities) {
            final AlarmInfoHistoryModel model = new AlarmInfoHistoryModel();

            BeanUtils.copyProperties(entity, model);
            try {
                org.apache.commons.beanutils.BeanUtils.copyProperties(model, entity.getTails());
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.warn("拷贝 AlarmInfoHistory.Tails 属性到 AlarmInfoHistoryModel 异常", e);
            }

            model.setDuration(
                duration(
                    model.getFaultBeginTime(),
                    model.getFaultEndTime()
                )
            );

            //车辆是否删除
            if (entity.getVehDelete() == 1) {
                model.setVin(entity.getVin() + "(车辆已删除)");
            }

            models.add(model);
        }

        loadProcessRecord(models);

        DataLoader.loadNames(models);
        log.debug("已完成历史报警数据实体转换, 耗时(ms): {}", System.currentTimeMillis() - beginTime);
        return models;
    }

    @Override
    public void deleteByTable(String tableName) {
        alarmInfoHistoryMapper.deleteByTable(tableName);
    }

    @Override
    public void deleteBatch(String tableName, List<AlarmInfoHistory> alarmInfoHistoryList) {
        alarmInfoHistoryMapper.deleteBatch(tableName, alarmInfoHistoryList);
    }

    private void loadProcessRecord(@NotNull final List<AlarmInfoHistoryModel> models) {

        final Map<String, AlarmInfoHistoryModel> modelMap = Maps.newHashMap();
        models.forEach(model -> modelMap.put(model.getId(), model));

        final String columnFaultAlarmId = "fault_alarm_id";
        final String columnRemark = "remark";
        final String columnCreateTime = "create_time";

        // 按年月分区
        final Map<String, List<AlarmInfoHistoryModel>> modelsGroupBySharding = modelMap
            .values()
            .stream()
            .collect(Collectors.groupingBy(
                (final AlarmInfoHistoryModel model) -> {
                    try {
                        return DateFormatUtils.format(
                            DateUtils.parseDate(model.getFaultBeginTime(), "yyyy-MM-dd HH:mm:ss"),
                            "yyyy-MM");
                    } catch (ParseException e) {
                        return "";
                    }
                }));

        modelsGroupBySharding.forEach(
            (final String shardingProp, final List<AlarmInfoHistoryModel> groupModels) -> {

                if ("".equals(shardingProp)) {
                    return;
                }

                // 查询处理记录并按故障分组
                final Map<String, List<Map<String, String>>> remarksGroupByFaultAlarmId = alarmInfoHistoryMapper
                    .processRecords(
                        // 当月开始时刻作为参数
                        shardingProp + "-01 00:00:00",
                        groupModels
                            .stream()
                            .map(m -> m.getId())
                            .collect(Collectors.toList())
                    )
                    .stream()
                    .collect(Collectors.groupingBy(r -> r.get(columnFaultAlarmId)));

                remarksGroupByFaultAlarmId.forEach(
                    (final String faultAlarmId, final List<Map<String, String>> remarks) -> {
                        // 按处理时间正序
                        remarks.sort(Comparator.comparing(m -> m.get(columnCreateTime)));

                        // 构建处理记录
                        final StringBuilder builder = new StringBuilder();
                        builder.append("第1次: ").append(remarks.get(0).get(columnRemark));
                        int index = 1;
                        while (index < remarks.size()) {
                            builder.append("\n第").append(index+1).append("次: ").append(remarks.get(index).get(columnRemark));
                            index += 1;
                        }
                        final String processRecord = builder.toString();

                        // 更新处理记录
                        modelMap.get(faultAlarmId).setProcessRecord(processRecord);
                    });
            });
    }

    /**
     * 设置车辆销售信息
     * @param model
     */
    private void setVehicleSellInfo(AlarmInfoHistoryModel model) {
        long time = System.currentTimeMillis();
        Map<String, Object> vehicleSellInfoMap = vehicleService.getSellInfo(model.getVehicleId());
        if (MapUtils.isNotEmpty(vehicleSellInfoMap)) {
            if (null != vehicleSellInfoMap.get("sell_for_field")) {
                model.setSellForField(String.valueOf(vehicleSellInfoMap.get("sell_for_field")));
            }
            if (null != vehicleSellInfoMap.get("sell_unit_or_vita")) {
                model.setSellUnitOrVita(String.valueOf(vehicleSellInfoMap.get("sell_unit_or_vita")));
            }
            if (null != vehicleSellInfoMap.get("telephone")) {
                model.setTelephone(String.valueOf(vehicleSellInfoMap.get("telephone")));
            }
        }
        log.trace("已获取历史报警车辆销售信息, 耗时(ms): {}", System.currentTimeMillis() - time);
    }

    /**
     * 将大数据映射对象转换成本地MODEL
     *
     * @param alarmInfoHistory 大数据接口历史报警对象
     * @return
     */
    private AlarmInfoHistoryModel convertModel(com.bitnei.cloud.common.client.model.AlarmInfoHistory alarmInfoHistory) {
        if (null == alarmInfoHistory) {
            return null;
        }
        AlarmInfoHistoryModel result = new AlarmInfoHistoryModel();
        BeanUtils.copyProperties(alarmInfoHistory, result);
        if (null != alarmInfoHistory.getFaultLocation()) {
            result.setLatitude(alarmInfoHistory.getFaultLocation().getLat());
            result.setLongitude(alarmInfoHistory.getFaultLocation().getLon());
        }
        result.setProcesStatus(alarmInfoHistory.getProcessStatus());
        result.setProcesTime(alarmInfoHistory.getProcessTime());
        result.setCreateTime(alarmInfoHistory.getFaultCreateTime());
        result.setUuid(alarmInfoHistory.getVid());
        // 处理记录设置
        com.bitnei.cloud.common.client.model.AlarmProcess[] alarmProcessArray = alarmInfoHistory.getAlarmProcesses();
        result = setProcessRecord(result, alarmProcessArray);
        return result;
    }

    /**
     * 批量将大数据映射对象转换成本地MODEL
     * @param alarmInfoHistoryList 大数据接口历史报警对象集合
     * @return
     */
    private List<AlarmInfoHistoryModel> convertModel(List<com.bitnei.cloud.common.client.model.AlarmInfoHistory> alarmInfoHistoryList) {
        List<AlarmInfoHistoryModel> modelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(alarmInfoHistoryList)) {
            alarmInfoHistoryList.stream().forEach(alarmInfoHistory -> {
                AlarmInfoHistoryModel model = convertModel(alarmInfoHistory);
                model.setDuration(duration(model.getFaultBeginTime(), model.getFaultEndTime()));
                modelList.add(model);
            });
        }
        return modelList;
    }

    /**
     * 将本地历史报警对象转换成大数据接口对象
     *
     * @param alarmInfoHistory 大数据接口历史报警对象
     * @return
     */
    private com.bitnei.cloud.common.client.model.AlarmInfoHistory convertBigDataModel(AlarmInfoHistory alarmInfoHistory) {
        long time = System.currentTimeMillis();
        if (null == alarmInfoHistory) {
            return null;
        }
        String id = alarmInfoHistory.getId();
        com.bitnei.cloud.common.client.model.AlarmInfoHistory result = new com.bitnei.cloud.common.client.model.AlarmInfoHistory();
        BeanUtils.copyProperties(alarmInfoHistory, result);

        // 1.对象实体转换, 个别属性差异需手动设置
        double latitude = new BigDecimal(alarmInfoHistory.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        double longitude = new BigDecimal(alarmInfoHistory.getLongitude()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        Location location = new Location(latitude, longitude);
        result.setFaultLocation(location);
        result.setProcessStatus(alarmInfoHistory.getProcesStatus());
        result.setProcessTime(alarmInfoHistory.getProcesTime());
        result.setFaultCreateTime(alarmInfoHistory.getCreateTime());

        // 2.获取并设置处理记录
        Map<String, Object> params = new HashMap<>();
        params.put("faultAlarmId", alarmInfoHistory.getId());
        params.put("faultBeginTime", alarmInfoHistory.getFaultBeginTime());
        final List<com.bitnei.cloud.common.client.model.AlarmProcess> bigDataAlarmProcessList = new ArrayList<>();
        List<AlarmProcess> alarmProcessList = alarmProcessMapper.pagerModel(params);
        alarmProcessList.stream().forEach(alarmProcess -> {
            com.bitnei.cloud.common.client.model.AlarmProcess bigDataAlarmProcess = new com.bitnei.cloud.common.client.model.AlarmProcess();
            BeanUtils.copyProperties(alarmProcess, bigDataAlarmProcess);
            bigDataAlarmProcessList.add(bigDataAlarmProcess);
        });
        result.setAlarmProcesses(bigDataAlarmProcessList.toArray(new com.bitnei.cloud.common.client.model.AlarmProcess[0]));

        // 3.获取并设置车辆相关信息
        List<VehicleInfoModel> vehicleInfoModelList = vehicleService.vehicles(alarmInfoHistory.getUuid(), null);
        if (CollectionUtils.isNotEmpty(vehicleInfoModelList)) {
            // 单车查询只会有一行记录
            BeanUtils.copyProperties(vehicleInfoModelList.get(0), result);
        }

        // 4.重新设置报警数据ID, 避免中间实体转换过程中相同ID属性被覆盖
        result.setId(id);

        if(StringUtils.isNotBlank(alarmInfoHistory.getWhoCanSeeMe())) {
            result.setWhoCanSeeMe(alarmInfoHistory.getWhoCanSeeMe().split("\\s+"));
        }
        else{
            result.setWhoCanSeeMe(new String[0]);

        }
        log.trace("完成转换大数据历史报警对象, 耗时(ms): {}", System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 转换查询对象
     * @param pagerInfo
     * @return
     */
    private AlarmSearchReq convertQueryModel(PagerInfo pagerInfo, String[] sourceIncludes) {
        long time = System.currentTimeMillis();
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        AlarmSearchReq alarmSearchReq = new AlarmSearchReq();
        // 所需返回字段
        alarmSearchReq.setSourceIncludes(sourceIncludes);
        // 按报警结束时间排序
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder();
        fieldSortBuilder.setFieldName("faultEndTime");
        fieldSortBuilder.setSortOrder(SortOrder.DESC);
        FieldSortBuilder[] sortBuilders = {fieldSortBuilder};
        alarmSearchReq.setSortBuilders(sortBuilders);
        // 权限
        if (null != params.get("authCode")) {
            log.info("查询历史报警权限编码：{}", params.get("authCode"));
            alarmSearchReq.setWhoCanSeeMe((String) params.get("authCode"));
        } else {
            String authCode = DataAccessKit.getAuthCode("sys_vehicle", "sv");
            log.info("查询历史报警权限编码：{}", authCode);
            alarmSearchReq.setWhoCanSeeMe(authCode);
        }
        // 分页信息
        int size = 10;
        if (pagerInfo.getLimit() != null && pagerInfo.getLimit() > 0) {
            size = pagerInfo.getLimit();
        }
        alarmSearchReq.setPageNo(pagerInfo.getStart() / size + 1);
        alarmSearchReq.setPageSize(pagerInfo.getLimit());

        // 查询条件设置
        if (null != params.get("ruleName")) {
            alarmSearchReq.setRuleName((String) params.get("ruleName"));
        }
        if (null != params.get("vin")) {
            alarmSearchReq.setVin((String) params.get("vin"));
        }
        if (null != params.get("beginTime")) {
            alarmSearchReq.setFaultBeginTimeStart((String) params.get("beginTime"));
        }
        if (null != params.get("endTime")) {
            alarmSearchReq.setFaultBeginTimeEnd((String) params.get("endTime"));
        }
        if (null != params.get("licensePlate")) {
            alarmSearchReq.setLicensePlate((String) params.get("licensePlate"));
        }
        if (null != params.get("vehModelId")) {
            alarmSearchReq.setVehModelId((String) params.get("vehModelId"));
        }
        if (null != params.get("faultType")) {
            alarmSearchReq.setFaultType(Integer.valueOf((String) params.get("faultType")));
        }
        if (null != params.get("alarmLevel")) {
            alarmSearchReq.setAlarmLevel(Integer.valueOf((String) params.get("alarmLevel")));
        }
        if (null != params.get("excludeFaultTypes")) {
            String []arr = ((String) params.get("excludeFaultTypes")).split(",");
            int[] excludeFaultTypes = new int[arr.length];
            for (int i = 0;i<arr.length;i++){
                excludeFaultTypes[i] = Integer.valueOf(arr[i]);
            }
            alarmSearchReq.setExcludeFaultTypes(excludeFaultTypes);
        }
        log.trace("完成转换大数据查询对象, 耗时(ms): {}", System.currentTimeMillis() - time);
        return alarmSearchReq;
    }

    /**
     * 转换本地Model
     * @param alarmProcess
     * @return
     */
    private AlarmProcessModel convertProcessModel(com.bitnei.cloud.common.client.model.AlarmProcess alarmProcess){
        if(null == alarmProcess){
            return null;
        }
        AlarmProcessModel model = new AlarmProcessModel();
        BeanUtils.copyProperties(alarmProcess, model);
        model.setProcesStatus(alarmProcess.getProcessStatus());
        return model;
    }

    /**
     * 转换大数据处理记录对象
     * @param alarmProcessModel
     * @return
     */
    private com.bitnei.cloud.common.client.model.AlarmProcess convertBigDataProcessModel(AlarmProcessModel alarmProcessModel) {
        if (null == alarmProcessModel) {
            return null;
        }
        com.bitnei.cloud.common.client.model.AlarmProcess alarmProcess = new com.bitnei.cloud.common.client.model.AlarmProcess();
        BeanUtils.copyProperties(alarmProcessModel, alarmProcess);
        alarmProcess.setProcessStatus(alarmProcessModel.getProcesStatus());
        return alarmProcess;
    }

    /**
     * 设置处理记录
     *
     * @param model
     * @return
     */
    private AlarmInfoHistoryModel setProcessRecord(AlarmInfoHistoryModel model, com.bitnei.cloud.common.client.model.AlarmProcess[] alarmProcessArray) {
        long time = System.currentTimeMillis();
        if (ArrayUtils.isNotEmpty(alarmProcessArray)) {
            List<com.bitnei.cloud.common.client.model.AlarmProcess> processlList = new ArrayList<>();
            Collections.addAll(processlList, alarmProcessArray);
            // 按创建时间自然排序
            processlList = processlList.stream().sorted(Comparator.comparing(com.bitnei.cloud.common.client.model.AlarmProcess::getCreateTime)).collect(Collectors.toList());

            List<AlarmProcessModel> processModellList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            int[] i = {1};
            processlList.stream().forEach(alarmProcess -> {
                AlarmProcessModel alarmProcessModel = convertProcessModel(alarmProcess);
                processModellList.add(alarmProcessModel);
                sb.append("第").append(i[0]).append("次：").append(alarmProcessModel.getRemark()).append("\n");
                i[0]++;
            });

            model.setProcessRecord(sb.toString());
            // 按创建时间降序排序
            model.setProcesslList(processModellList.stream().sorted(Comparator.comparing(AlarmProcessModel::getCreateTime).reversed()).collect(Collectors.toList()));
        }
        log.trace("完成历史报警处理记录转换设置, 耗时(ms): {}", System.currentTimeMillis() - time);
        return model;
    }
}
