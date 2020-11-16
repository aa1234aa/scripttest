package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.das.HisDataClient;
import com.bitnei.cloud.common.client.model.GlobalResponse;
import com.bitnei.cloud.common.client.model.PageRequest;
import com.bitnei.cloud.common.client.model.PageResult;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.compent.kafka.service.LocalThreadFactory;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.fault.domain.CodeRuleItem;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.fault.service.ICodeTypeService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.domain.RemoteDiagnose;
import com.bitnei.cloud.sys.model.DiagnoseDto;
import com.bitnei.cloud.sys.model.RemoteDiagnoseDetailModel;
import com.bitnei.cloud.sys.model.RemoteDiagnoseModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseDetailService;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import scala.Predef;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： RemoteDiagnoseService实现<br>
 * 描述： RemoteDiagnoseService实现<br>
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
 * <td>2019-03-25 16:14:50</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.RemoteDiagnoseMapper")
@RequiredArgsConstructor
public class RemoteDiagnoseService extends BaseService implements IRemoteDiagnoseService {

    private final HisDataClient hisDataClient;
    private final ICodeTypeService codeTypeService;
    private final IVehicleService vehicleService;
    private final ICodeRuleItemService codeRuleItemService;
    private final IUserService userService;
    private final IRemoteDiagnoseDetailService remoteDiagnoseDetailService;
    private final ScheduledExecutorService realTimeDiagnoseScheduleExecutor;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose", "rd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<RemoteDiagnose> entries = findBySqlId("pagerModel", params);
            List<RemoteDiagnoseModel> models = new ArrayList();
            for (RemoteDiagnose entry : entries) {
                RemoteDiagnose obj = (RemoteDiagnose) entry;
                models.add(RemoteDiagnoseModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RemoteDiagnoseModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                RemoteDiagnose obj = (RemoteDiagnose) entry;
                models.add(RemoteDiagnoseModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public RemoteDiagnoseModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose", "rd");
        params.put("id", id);
        RemoteDiagnose entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return RemoteDiagnoseModel.fromEntry(entry);
    }


    @Override
    public void insert(RemoteDiagnoseModel model) {

        RemoteDiagnose obj = new RemoteDiagnose();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public String insertReturnId(RemoteDiagnoseModel model) {
        RemoteDiagnose obj = new RemoteDiagnose();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        return id;
    }

    @Override
    public void update(RemoteDiagnoseModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose", "rd");

        RemoteDiagnose obj = new RemoteDiagnose();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose", "rd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose", "rd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<RemoteDiagnose>(this, "pagerModel", params, "sys/res/remoteDiagnose/export.xls", "远程诊断记录") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "REMOTEDIAGNOSE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<RemoteDiagnoseModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RemoteDiagnoseModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RemoteDiagnoseModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "REMOTEDIAGNOSE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<RemoteDiagnoseModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RemoteDiagnoseModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RemoteDiagnoseModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    @SneakyThrows
    public ResultMsg diagnose(DiagnoseDto diagnoseDto) {

        VehicleModel vehicleModel = vehicleService.getByVin(diagnoseDto.getVin());

        //存放报文
        List<Map<String, String>> dataMaps = new ArrayList<>();

        String dataItemsStr = codeTypeService.getAllFaultTypeCode();
        String[] dataItems = dataItemsStr.split(",");

        PageRequest pageRequest = new PageRequest();
        pageRequest.setVid(vehicleModel.getUuid());
        pageRequest.setColumns(Arrays.stream(dataItems).collect(Collectors.toList()));

        //如果是历史诊断，直接查历史数据
        if (StringUtils.isNotEmpty(diagnoseDto.getBeginTime()) && StringUtils.isNotEmpty(diagnoseDto.getEndTime())) {

            int minites = DateUtil.getTimesDiff(3, diagnoseDto.getBeginTime(), diagnoseDto.getEndTime());
            if (minites > 10) {
                return ResultMsg.getResult(null, "诊断时间不能超过10分钟", -1);
            }

            pageRequest.setStartTime(diagnoseDto.getBeginTime());
            pageRequest.setEndTime(diagnoseDto.getEndTime());
            if (vehicleModel.isG6()) {
                pageRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
            }
            if (vehicleModel.isGb()) {
                pageRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            }
            pageRequest.setReadMode(DataReadMode.ALL);

            GlobalResponse<PageResult<Map<String, String>>> response =
                    hisDataClient.findPageByUUID(pageRequest);

            //查询历史区间的数据
            if (response != null && response.getData() != null &&
                    CollectionUtils.isNotEmpty(response.getData().getData())) {
                dataMaps = response.getData().getData();
            }

            if (CollectionUtils.isNotEmpty(dataMaps)) {
                //0为历史诊断
                return dealRemoteDiagnoseRecord(vehicleModel, dataMaps, 0, dataItems,
                        diagnoseDto.getBeginTime());
            }

            return ResultMsg.getResult(null,
                    "数据获取失败,请确认车辆终端在该历史时间区间处于启动状态", -1);
        } //否则就是实时诊断
        else {

            Date diagnoseTime = new Date();
            String diagnoseTimeStr = DateUtil.formatTime(diagnoseTime, DateUtil.FULL_ST_FORMAT);

            int i = 12;
            while (i-- > 0) {

                ScheduledFuture<List<Map<String, String>>> future = realTimeDiagnoseScheduleExecutor.schedule(() -> {

                    Date endTime = com.bitnei.cloud.sys.util.DateUtil.addSecond(new Date(), 5);
                    String endTimeStr = DateUtil.formatTime(endTime, DateUtil.FULL_ST_FORMAT);

                    pageRequest.setStartTime(diagnoseTimeStr);
                    pageRequest.setEndTime(endTimeStr);
                    if (vehicleModel.isG6()) {
                        pageRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                    }
                    if (vehicleModel.isGb()) {
                        pageRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
                    }
                    pageRequest.setReadMode(DataReadMode.ALL);

                    GlobalResponse<PageResult<Map<String, String>>> response =
                            hisDataClient.findPageByUUID(pageRequest);
                    //查询区间的数据
                    if (response != null && response.getData() != null &&
                            CollectionUtils.isNotEmpty(response.getData().getData())) {
                        return response.getData().getData();
                    }

                    return null;
                }, 5 * 1000L, TimeUnit.MILLISECONDS);

                //每10s查询一次，endTime增加10s
                dataMaps = future.get();
                if (CollectionUtils.isNotEmpty(dataMaps)) {
                    break;
                }
            }
            if (CollectionUtils.isNotEmpty(dataMaps)) {
                //1为实时诊断，只取第一帧获取到的数据
                return dealRemoteDiagnoseRecord(vehicleModel, Collections.singletonList(dataMaps.get(0)),
                        1, dataItems, diagnoseTimeStr);
            }
        }

        return ResultMsg.getResult(null,
                "当前车辆终端数据获取失败,或终端连接异常,请确认车辆终端处于启动状态", -1);
    }

    /**
     * 处理诊断记录
     *
     * @param vehicleModel
     * @param dataMaps
     * @param diagnosticMode 0为历史 1为实时
     * @param dataItems
     */
    private ResultMsg dealRemoteDiagnoseRecord(VehicleModel vehicleModel,
                                               List<Map<String, String>> dataMaps,
                                               Integer diagnosticMode,
                                               String[] dataItems,
                                               String uploadTime) {

        RemoteDiagnoseModel remoteDiagnoseModel = new RemoteDiagnoseModel();
        Map<String, String> dataMap;
        List<Map<String, String>> list = new ArrayList<>();
        List<Map<String, String>> resultsList = new ArrayList<>();

        if (diagnosticMode == 1) {
            dataMap = dataMaps.get(0);
            list.addAll(dealDataMap(dataMap, dataItems, vehicleModel));
        }
        //否则是历史诊断
        else {
            //历史诊断要处理所有的报文，可能有效率问题
            dataMaps.forEach(it -> list.addAll(dealDataMap(it, dataItems, vehicleModel)));
        }

        //存入远程诊断表中
        remoteDiagnoseModel.setCreateBy(ServletUtil.getCurrentUser());
        remoteDiagnoseModel.setCreateById(userService.findByUsername(ServletUtil.getCurrentUser()).getId());
        remoteDiagnoseModel.setCreateTime(DateUtil.getNow());
        //诊断方式
        remoteDiagnoseModel.setDiagnosticMode(diagnosticMode);
        remoteDiagnoseModel.setDiagnositicTime(DateUtil.getNow());
        remoteDiagnoseModel.setLicensePlate(vehicleModel.getLicensePlate());
        remoteDiagnoseModel.setVin(vehicleModel.getVin());
        remoteDiagnoseModel.setVehModelName(vehicleModel.getVehModelName());

        //判断诊断结果
        String result = SysDefine.DIAGNBOSE_MODE.NORMAL;
        StringBuilder sb = new StringBuilder();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            if (list.get(i).get("\"result\"").replaceAll("\"", "")
                    .equals(SysDefine.DIAGNBOSE_MODE.EXCEPTION) && result.equals(SysDefine.DIAGNBOSE_MODE.NORMAL)) {
                result = SysDefine.DIAGNBOSE_MODE.EXCEPTION;
            } else if (list.get(i).get("\"result\"").replaceAll("\"", "")
                    .equals(SysDefine.DIAGNBOSE_MODE.FAULT) &&
                    (result.equals(SysDefine.DIAGNBOSE_MODE.NORMAL) ||
                            result.equals(SysDefine.DIAGNBOSE_MODE.EXCEPTION))) {
                result = SysDefine.DIAGNBOSE_MODE.FAULT;
            }
            if (!resultsList.contains(list.get(i))) {
                sb.append(list.get(i).toString());
                if (i < len - 1) {
                    sb.append(",");
                }
                resultsList.add(list.get(i));
            }

        }
        sb.insert(0, "[").append("]");
        //诊断结果
        remoteDiagnoseModel.setResultState(Integer.valueOf(result));
        String id = insertReturnId(remoteDiagnoseModel);

        //插入明细表

        RemoteDiagnoseDetailModel remoteDiagnoseDetailModel = new RemoteDiagnoseDetailModel();
        remoteDiagnoseDetailModel.setCreateTime(remoteDiagnoseModel.getDiagnositicTime());
        remoteDiagnoseDetailModel.setDiagnoseId(id);
        remoteDiagnoseDetailModel.setLicensePlate(remoteDiagnoseModel.getLicensePlate());
        remoteDiagnoseDetailModel.setUploadTime(uploadTime);
        if (',' == sb.toString().charAt(sb.toString().length() - 2)) {
            remoteDiagnoseDetailModel.setResult(sb.toString().substring(0, sb.toString().length() - 2) + "]");
        } else {
            remoteDiagnoseDetailModel.setResult(sb.toString());
        }

        String diagnoseDetailId = remoteDiagnoseDetailService.insertReturnId(remoteDiagnoseDetailModel);
        RemoteDiagnoseDetailModel diagnoseDetailModel = remoteDiagnoseDetailService.get(diagnoseDetailId);
        log.error("======================诊断结束时间：===" + DateUtil.getNow());
        return ResultMsg.getResult(diagnoseDetailModel);
    }

    /**
     * @param fault      当前故障种类的报文
     * @param vehModelId 车型id
     * @param typeCode   故障类型编码
     * @param result     故障类型编码对应的结果
     * @return
     */
    //TODO 监控面板中有使用该部分逻辑,如果修改了此函数,记得去群里通知一下
    private List<Map<String, String>> analyzeReport(String fault, String vehModelId, String typeCode, String result) {
        List<Map<String, String>> resultList = new ArrayList<>();
        List<CodeRuleItemModel> codeRuleItemModels =
                codeRuleItemService.getByTypeCodeAndVehModelId(typeCode, vehModelId);
        String[] faults = fault.split("\\|");

        codeRuleItemModels.forEach(it -> {
            Map<String, String> tempMap = new HashMap<>();
            if ("".equals(result)) {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.EXCEPTION + "\"");
            } else if ("0".equals(result)) {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
            } else if (Integer.valueOf(result) > 0) {
                //判断是按bit解析还是其他
                switch (it.getAnalyzeType()) {
                    //2位按bit解析
                    case 2: {
                        //移位 1
                        int i1 = it.getStartPoint() % 32;
                        //移动报文段 2
                        int i2 = it.getStartPoint() / 32;
                        if (faults.length > i2) {
                            if ((Integer.valueOf(faults[i2]) & (1 << i1)) >> i1 != Integer.valueOf(it.getNormalCode())) {
                                //故障
                                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");
                            } else {
                                //正常
                                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
                            }
                        }
                        break;
                    }
                    case 1: {
                        tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
                        if (StringUtils.isEmpty(it.getNormalCode())) {
                            //故障
                            tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");
                        } else {
                            String faultCode = new BigInteger(it.getExceptionCode(), 16)
                                    .toString(10);
                            for (String item : faults) {
                                if (item.equals(faultCode)) {
                                    //故障
                                    tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");

                                }
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            } else {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.EXCEPTION + "\"");
            }
            tempMap.put("\"faultName\"", "\"" + it.getFaultName() + "\"");
            tempMap.put("\"id\"", "\"" + it.getId() + "\"");
            resultList.add(tempMap);
        });

        return resultList;
    }

    /**
     * 处理某一条报文
     *
     * @param dataMap
     * @param dataItems
     */
    @Override
    public List<Map<String, String>> dealDataMap(Map<String, String> dataMap, String[] dataItems,
                                                 VehicleModel vehicleModel) {
        List<Map<String, String>> dealResults = new ArrayList();
        for (int i = 0; i < dataItems.length; i += 2) {
            if (dataMap.get(dataItems[i + 1]) != null && dataMap.get(dataItems[i]) != null) {
//                log.error("faultmap.get(dataItemArray[i + 1])=============：" + dataMap.get(dataItems[i + 1]));
//                log.error("faultmap.get(dataItemArray[i])=============：" + dataMap.get(dataItems[i]));
//                log.error("dataItemArray[i + 1]=============：" + dataItems[i + 1]);
//                log.error("vehicleEntity.getVehModelId()=============：" + vehicleModel.getVehModelId());
                dealResults.addAll(this.analyzeReport(dataMap.get(dataItems[i + 1]), vehicleModel.getVehModelId(),
                        dataItems[i + 1], dataMap.get(dataItems[i])));
            }
        }
        return dealResults;
    }

    @Override
    public List<Map<String, String>> dealDataMapFast(Map<String, String> dataMap, String[] dataItems,
                                                     VehicleModel vehicleModel) {
        List<Map<String, String>> dealResults = new ArrayList<>();
        Map<String, List<CodeRuleItemModel>> cacheMap = Maps.newHashMap();
        Map<String, List<CodeRuleItem>> codeRuleMap = Maps.newHashMap();
        List<CodeRuleItem> codeRuleItemList = codeRuleItemService.getAllFault();

        if (CollectionUtils.isNotEmpty(codeRuleItemList)) {
            codeRuleItemList = codeRuleItemList.stream().filter(a -> a.getEnabledStatus() != null && a.getEnabledStatus() == 1).collect(Collectors.toList());
            codeRuleMap = codeRuleItemList.stream().filter(q -> StringUtils.isNotBlank(q.getFaultCodeType())).collect(Collectors.groupingBy(CodeRuleItem::getFaultCodeType));
        }
        for (int i = 0; i < dataItems.length; i += 2) {
            if (dataMap.get(dataItems[i + 1]) != null && dataMap.get(dataItems[i]) != null) {
                String key = String.format("%s_%s", dataItems[i + 1], vehicleModel.getVehModelId());
                if (!cacheMap.containsKey(key)) {

                    List<CodeRuleItemModel> codeRuleItemModels =
                            codeRuleItemService.getByTypeCodeAndVehModelIdFast(dataItems[i + 1], vehicleModel.getVehModelId(), codeRuleMap.getOrDefault(dataItems[i + 1], null));
                    cacheMap.put(key, codeRuleItemModels);
                } else {
                    int a = 0;
                }
                List<Map<String, String>> list = analyzeReport(dataMap.get(dataItems[i + 1]), dataMap.get(dataItems[i]), cacheMap.get(key));
                dealResults.addAll(list);
            }
        }
        return dealResults;
    }

    private List<Map<String, String>> analyzeReport(String fault, String result, List<CodeRuleItemModel> codeRuleItemModels) {
        List<Map<String, String>> resultList = new ArrayList<>();

        String[] faults = fault.split("\\|");

        codeRuleItemModels.forEach(it -> {
            Map<String, String> tempMap = new HashMap<>();
            if ("".equals(result)) {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.EXCEPTION + "\"");
            } else if ("0".equals(result)) {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
            } else if (Integer.valueOf(result) > 0) {
                //判断是按bit解析还是其他
                switch (it.getAnalyzeType()) {
                    //2位按bit解析
                    case 2: {
                        //移位 1
                        int i1 = it.getStartPoint() % 32;
                        //移动报文段 2
                        int i2 = it.getStartPoint() / 32;
                        if (faults.length > i2) {
                            if ((Integer.valueOf(faults[i2]) & (1 << i1)) >> i1  != Integer.valueOf(it.getNormalCode())) {
                                //故障
                                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");
                            } else {
                                //正常
                                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
                            }
                        }
                        break;
                    }
                    case 1: {
                        tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"");
                        if (StringUtils.isEmpty(it.getNormalCode())) {
                            //故障
                            tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");
                        } else {
                            String faultCode = new BigInteger(it.getExceptionCode(), 16)
                                    .toString(10);
                            for (String item : faults) {
                                if (item.equals(faultCode)) {
                                    //故障
                                    tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"");

                                }
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            } else {
                tempMap.put("\"result\"", "\"" + SysDefine.DIAGNBOSE_MODE.EXCEPTION + "\"");
            }
            tempMap.put("\"faultName\"", "\"" + it.getFaultName() + "\"");
            tempMap.put("\"id\"", "\"" + it.getId() + "\"");
            resultList.add(tempMap);
        });

        return resultList;
    }
}
