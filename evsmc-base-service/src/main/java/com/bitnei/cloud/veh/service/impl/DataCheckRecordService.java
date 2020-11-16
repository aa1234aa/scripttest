package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.das.ProtocolMessageClient;
import com.bitnei.cloud.common.client.model.*;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.service.impl.ProtocolMessageService;
import com.bitnei.cloud.sys.util.MessageBean;
import com.bitnei.cloud.veh.dao.DataCheckRecordMapper;
import com.bitnei.cloud.veh.dao.DataCheckRuleItemMapper;
import com.bitnei.cloud.veh.dao.DataCheckRuleMapper;
import com.bitnei.cloud.veh.domain.DataCheckRecord;
import com.bitnei.cloud.veh.domain.DataCheckRule;
import com.bitnei.cloud.veh.domain.DataCheckRuleItem;
import com.bitnei.cloud.veh.model.*;
import com.bitnei.cloud.veh.service.IDataCheckRecordService;
import com.bitnei.cloud.veh.service.IDataItemCheckResultService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DataCheckRecordService实现<br>
 * 描述： DataCheckRecordService实现<br>
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
 * <td>2019-09-17 14:11:42</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DataCheckRecordMapper")
@RequiredArgsConstructor
public class DataCheckRecordService extends BaseService implements IDataCheckRecordService {

    private final IVehicleService vehicleService;
    private final DataCheckRuleMapper dataCheckRuleMapper;
    private final Environment env;
    private final DataCheckRuleItemMapper dataCheckRuleItemMapper;
    private final ProtocolMessageClient protocolMessageClient;
    private final ProtocolMessageService protocolMessageService;
    private final ThreadPoolExecutor vehCheckExecutor;
    private final IDataItemCheckResultService dataItemCheckResultService;
    private final DataCheckRecordMapper dataCheckRecordMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_data_check_record", "dcre");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<DataCheckRecord> entries = findBySqlId("pagerModel", params);
            List<DataCheckRecordModel> models = new ArrayList();
            for (DataCheckRecord entry : entries) {
                DataCheckRecord obj = (DataCheckRecord) entry;
                models.add(DataCheckRecordModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataCheckRecordModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                DataCheckRecord obj = (DataCheckRecord) entry;
                models.add(DataCheckRecordModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public DataCheckRecordModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_data_check_record", "dcre");
        params.put("id", id);
        DataCheckRecord entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return DataCheckRecordModel.fromEntry(entry);
    }


    @Override
    public void insert(DataCheckRecordModel model) {

        DataCheckRecord obj = new DataCheckRecord();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    public String insertReturnId(DataCheckRecordModel model) {
        DataCheckRecord obj = new DataCheckRecord();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        return id;
    }

    @Override
    public void update(DataCheckRecordModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_data_check_record", "dcre");

        DataCheckRecord obj = new DataCheckRecord();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_data_check_record", "dcre");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_data_check_record", "dcre");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DataCheckRecord>(this, "pagerModel", params, "veh/res/dataCheckRecord/export.xls", "车辆数据检测记录") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DATACHECKRECORD" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DataCheckRecordModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataCheckRecordModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataCheckRecordModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DATACHECKRECORD" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DataCheckRecordModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataCheckRecordModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataCheckRecordModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 车辆下线检测：开始检测
     *
     * @param vin
     * @return
     */
    @Override
    public DataCheckResult check(String vin) {

        //再次检查
        int validateFlag = validateCheck(vin);

        if (validateFlag != -1) {
            switch (validateFlag) {
                case 1:
                    throw new BusinessException("VIN码输入不正确");
                case 2:
                    throw new BusinessException("VIN码不存在或无此车权限");
                case 3:
                    throw new BusinessException("该车辆（VIN码）没有绑定检测规则");
                default:
                    throw new BusinessException("未知错误");
            }
        }

        //开始检测逻辑，数据准备

        //获取车辆信息
        VehicleModel vehicleModel = vehicleService.getByVinValidateAuth(vin);

        //根据车辆信息获取车辆的检测规则
        Map<String, Object> params = new HashMap<>();
        params.put("vehModelId", vehicleModel.getVehModelId());
        params.put("protocolId", vehicleModel.getSupportProtocol());
        DataCheckRule dataCheckRule = dataCheckRuleMapper.findVehCheckRule(params);

        //根据规则获取需要检测的数据项
        params.put("checkRuleId", dataCheckRule.getId());
        List<DataCheckRuleItem> dataItems = dataCheckRuleItemMapper.getCheckItems(params);
        //数据项编码和名称映射map
        Map<String, String> dataNoToName = dataItems.stream().collect(
                Collectors.toMap(DataCheckRuleItem::getSeqNo, DataCheckRuleItem::getItemName));

        //获取要往前检测多少天的数据
        Integer checkDays = env.getProperty("veh.check.days", Integer.class, 1);

        //性能瓶颈猜测在内存，一次只取一天的数据进行检测
        Date endTime = new Date();

        //检测时间范围
        Date beginRange = com.bitnei.cloud.sys.util.DateUtil.addDay(endTime, -1 * checkDays);
        String beginRangeStr = com.bitnei.cloud.sys.util.DateUtil.formatTime(
                beginRange, com.bitnei.cloud.sys.util.DateUtil.FULL_ST_FORMAT);
        String endRangeStr = com.bitnei.cloud.sys.util.DateUtil.formatTime(
                endTime, com.bitnei.cloud.sys.util.DateUtil.FULL_ST_FORMAT);

        int loginPacketNum = 0;
        int logoutPacketNum = 0;
        int dataPacketNum = 0;

        double finalTimesRange = 0;
        //计算需上传报文数
        int finalMissPackPart = 0;
        //异常数据项检测，异常报文总数
        AtomicInteger dataExceptionNum = new AtomicInteger(0);
        //获取实时数据上报频率配置，单位（秒/帧），计算应上报的数据报文数。默认10秒1帧
        double intervalSecond = env.getProperty("veh.check.data.interval", double.class, 10d);

        //每500条的结果列表
        List<Map<String, PartDataCheckResult>> totalResultMapList = Collections.synchronizedList(new ArrayList<>());

        for (int i = 1; i <= checkDays; i++) {
            Date beginTime = com.bitnei.cloud.sys.util.DateUtil.addDay(endTime, -1);
            String beginTimeStr = com.bitnei.cloud.sys.util.DateUtil.formatTime(
                    beginTime, com.bitnei.cloud.sys.util.DateUtil.FULL_ST_FORMAT);
            String endTimeStr = com.bitnei.cloud.sys.util.DateUtil.formatTime(
                    endTime, com.bitnei.cloud.sys.util.DateUtil.FULL_ST_FORMAT);

            //组装参数
            ProtocolQueryModel protocolQueryModel = buildProtocolRequest(beginTimeStr, endTimeStr, vehicleModel);

            GlobalResponse<PageResult<ProtocolMessageData>> globalResponse =
                    protocolMessageClient.findPageByUUID(protocolQueryModel);
            if (null == globalResponse || globalResponse.getData() == null ||
                    CollectionUtils.isEmpty(globalResponse.getData().getData())) {
                //该段时间无报文信息
                //一天天往前推
                endTime = com.bitnei.cloud.sys.util.DateUtil.addDay(endTime, -1 * checkDays);
                continue;
            }
            List<JSONObject> results = protocolMessageService.getData(globalResponse, vin,
                    vehicleModel.getInterNo(), vehicleModel.getLicensePlate(), vehicleModel.getRuleTypeName());

            //循环顺便计算相差时间超过三分钟的数据
            double secondsDiffSum = 0;
            //计算有多少段间隔的报文段
            int missPackPart = 0;

            JSONObject lastObject = null;
            for (JSONObject jsonObject : results) {

                Integer type = jsonObject.getInt("type");
                switch (type) {
                    case 1:
                        loginPacketNum++;
                        break;
                    case 2:
                        dataPacketNum++;
                        //计算间隔超过3分钟的实时报文数据
                        if (null != lastObject) {
                            String lastServerReceiveTime = lastObject.getString("serverRecvTime");
                            String nextServerReceiveTime = jsonObject.getString("serverRecvTime");
                            double diff = com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(
                                    4, lastServerReceiveTime, nextServerReceiveTime);
                            //3分钟间隔为180秒
                            if (diff > 180) {
                                secondsDiffSum += com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(
                                        4, lastServerReceiveTime, nextServerReceiveTime);
                                missPackPart++;
                            }
                        } else {
                            //第一帧实时报文要计算与统计开始时间的时间差
                            String nextServerReceiveTime = jsonObject.getString("serverRecvTime");
                            secondsDiffSum += com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(
                                    4, beginTimeStr, nextServerReceiveTime);
                        }
                        lastObject = jsonObject;
                        break;
                    case 4:
                        logoutPacketNum++;
                        break;
                    default:
                        break;
                }
            }
            //最后一帧实时报文要计算与统计开始时间的时间差
            if (null != lastObject) {
                String lastServerReceiveTime = lastObject.getString("serverRecvTime");
                secondsDiffSum += com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(
                        4, lastServerReceiveTime, endTimeStr);
            } else {
                //如果不存在实时报文，则不需要上传报文
                secondsDiffSum = com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(
                        4, beginTimeStr, endTimeStr);
            }

            //计算应上传报文总时长（单位：秒）
            double timesRange = com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(4, beginTimeStr, endTimeStr);
            double partTimesRange = timesRange - secondsDiffSum;
            if (missPackPart > 0) {
                //缺了多少个区间，就补充多少个间隔秒（默认10s）
                partTimesRange += intervalSecond * missPackPart;
            }
            finalTimesRange += partTimesRange;
            finalMissPackPart += missPackPart;

            //原始报文数据分批，每500条一批
            List<List<JSONObject>> jsonObjectListList = Lists.partition(results, 500);
            List<CompletableFuture> futures = new ArrayList<>();
            //每一千条统计一次，避免内存爆炸
            for (List<JSONObject> jsonObjects : jsonObjectListList) {
                //异步计算每个分片的统计结果
                CompletableFuture future = CompletableFuture.runAsync(() -> {
                    //每一帧报文的统计结果
                    Map<String, PartDataCheckResult> partResultMap = new HashMap<>();
                    for (JSONObject object : jsonObjects) {
                        Integer type = object.getInt("type");
                        //类型为2的是实时信息上报报文
                        if (type != 2) {
                            continue;
                        }
                        //该报文是否包含异常标记
                        Boolean errorFlag = false;

                        List<MessageBean> messageBeans = protocolMessageService.doParse(
                                object.get("data").toString(), vehicleModel.isG6() ?
                                        ProtocolMessageService.G6 : ProtocolMessageService.GB, true);
                        for (MessageBean messageBean : messageBeans) {
                            //如果该报文包含检测数据项，该数据项检测条数则+1，如果是异常，则该数据项异常报文条数+1
                            if (null != messageBean.getSeqNo() && dataNoToName.containsKey(messageBean.getSeqNo())) {
                                PartDataCheckResult partDataCheckResult;
                                if (partResultMap.containsKey(messageBean.getSeqNo())) {
                                    partDataCheckResult = partResultMap.get(messageBean.getSeqNo());
                                } else {
                                    partDataCheckResult = new PartDataCheckResult();
                                    partDataCheckResult.setSeqNo(messageBean.getSeqNo());
                                }
                                partDataCheckResult.setCheckPackNum(partDataCheckResult.getCheckPackNum() + 1);
                                if (messageBean.getState() == 1) {
                                    errorFlag = true;
                                    partDataCheckResult.setErrorPackNum(partDataCheckResult.getErrorPackNum() + 1);
                                }
                                partResultMap.put(messageBean.getSeqNo(), partDataCheckResult);
                            }
                        }
                        //计算异常报文总条数，可能会稍微影响性能，考虑是否需要保留
                        if (errorFlag) {
                            dataExceptionNum.incrementAndGet();
                        }
                    }
                    totalResultMapList.add(partResultMap);
                }, vehCheckExecutor);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            //一天天往前推
            endTime = com.bitnei.cloud.sys.util.DateUtil.addDay(endTime, -1 * checkDays);
        }

        //统计报文类型结果
        Boolean checkPackketNumResult = loginPacketNum > 0 && logoutPacketNum > 0 && dataPacketNum > 0;

        //统计丢包率结果
        //计算需上传报文数
        int needUploadNum;
        if (dataPacketNum == 0) {
            needUploadNum = (int) (finalTimesRange / intervalSecond);
        } else if (finalMissPackPart == 0) {
            needUploadNum = (int) (finalTimesRange / intervalSecond) + 1;
        } else if (finalMissPackPart > 0) {
            needUploadNum = (int) (finalTimesRange / intervalSecond) + finalMissPackPart;
        } else {
            needUploadNum = (int) (finalTimesRange / intervalSecond);
        }
        //实际丢包率
        double packetRealLossRate = needUploadNum == 0 ? 0 : 1 - (double) dataPacketNum / (double) needUploadNum;

        //统计数据项检测结果
        Map<String, PartDataCheckResult> finalResultMap = new HashMap<>();
        for (Map<String, PartDataCheckResult> map : totalResultMapList) {
            for (String seqNo : map.keySet()) {
                if (finalResultMap.containsKey(seqNo)) {
                    PartDataCheckResult result = finalResultMap.get(seqNo);
                    PartDataCheckResult result1 = map.get(seqNo);
                    result.setCheckPackNum(result.getCheckPackNum() + result1.getCheckPackNum());
                    result.setErrorPackNum(result.getErrorPackNum() + result1.getErrorPackNum());
                } else {
                    finalResultMap.put(seqNo, map.get(seqNo));
                }
            }
        }

        //组装检测结果
        DataCheckRecordModel checkRecord = new DataCheckRecordModel();

        //检测时间区间
        checkRecord.setCheckTimeBg(beginRangeStr);
        checkRecord.setCheckTimeEd(endRangeStr);

        //报文类型检测，对应的三种报文数量
        checkRecord.setLoginPacketNum(loginPacketNum);
        checkRecord.setLogoutPacketNum(logoutPacketNum);
        checkRecord.setRealStatusPacketNum(dataPacketNum);
        checkRecord.setPacketTypeCheckResult(checkPackketNumResult ? 1 : 0);

        //丢包率检测数据
        checkRecord.setNeedUploadNum(needUploadNum);
        checkRecord.setRealUploadNum(dataPacketNum);
        checkRecord.setPacketLossRate(dataCheckRule.getPacketLossRate());
        checkRecord.setPacketRealLossRate(packetRealLossRate);
        //如果实际丢包率大于阈值，则未通过，否则通过（0：未通过，1：通过）
        checkRecord.setLossCheckResult(packetRealLossRate > dataCheckRule.getPacketLossRate() ? 0 : 1);

        //数据项异常检测结果
        checkRecord.setDataPacketNum(dataPacketNum);
        checkRecord.setDataExceptionNum(dataExceptionNum.get());
        checkRecord.setDataItemException(dataCheckRule.getDataItemException());
        checkRecord.setDataRealException(
                dataPacketNum == 0 ? 0 : (double) checkRecord.getDataExceptionNum() / (double) dataPacketNum);
        checkRecord.setDataExceptionResult(checkRecord.getDataExceptionNum() > 0 ? 0 : 1);

        //车辆vin，异常原因和总结果
        checkRecord.setVin(vin);
        checkRecord.setVehModelId(vehicleModel.getVehModelId());
        checkRecord.setVehModelName(vehicleModel.getVehModelName());
        if (checkPackketNumResult && checkRecord.getLossCheckResult() == 1 &&
                checkRecord.getDataExceptionResult() == 1) {
            checkRecord.setCheckResult(1);
        } else {
            checkRecord.setCheckResult(0);
            String reason = "";
            if (!checkPackketNumResult) {
                reason += "报文类型异常";
            }
            if (checkRecord.getLossCheckResult() == 0) {
                if (StringUtils.isNotEmpty(reason)) {
                    reason += ",";
                }
                reason += "丢包率异常";
            }
            if (checkRecord.getDataExceptionResult() == 0) {
                if (StringUtils.isNotEmpty(reason)) {
                    reason += ",";
                }
                reason += "数据项异常";
            }
            checkRecord.setReason(reason);
        }

        checkRecord.setCreateTime(endRangeStr);
        checkRecord.setCreateBy(ServletUtil.getCurrentUser());
        //写主表
        String id = insertReturnId(checkRecord);

        //写子表
        //子表数据处理
        List<DataItemCheckResultModel> itemCheckResultModels = new ArrayList<>();
        Set<String> resultSet = new HashSet<>();
        for (Map.Entry<String, PartDataCheckResult> partDataCheckResult : finalResultMap.entrySet()) {
            resultSet.add(partDataCheckResult.getKey());
            PartDataCheckResult itemResult = partDataCheckResult.getValue();
            DataItemCheckResultModel detailResultModel = new DataItemCheckResultModel();
            detailResultModel.setCheckRecordId(id);
            detailResultModel.setVin(vin);
            detailResultModel.setSeqNo(partDataCheckResult.getKey());
            detailResultModel.setDataItemName(dataNoToName.get(detailResultModel.getSeqNo()));
            detailResultModel.setItemPacketNum(itemResult.getCheckPackNum());
            detailResultModel.setItemExceptionNum(itemResult.getErrorPackNum());
            detailResultModel.setDataItemException(dataCheckRule.getDataItemException());
            detailResultModel.setItemRealException(
                    (double) itemResult.getErrorPackNum() / (double) itemResult.getCheckPackNum());
            detailResultModel.setItemExceptionResult(itemResult.getErrorPackNum() > 0 ? 0 : 1);
            detailResultModel.setCreateBy(ServletUtil.getCurrentUser());
            detailResultModel.setCreateTime(endRangeStr);
            dataItemCheckResultService.insert(detailResultModel);
            itemCheckResultModels.add(detailResultModel);
        }
        //处理检测结果不存在的项
        for (DataCheckRuleItem dataCheckRuleItem : dataItems) {
            if (!resultSet.contains(dataCheckRuleItem.getSeqNo())) {
                DataItemCheckResultModel detailResultModel = new DataItemCheckResultModel();
                detailResultModel.setCheckRecordId(id);
                detailResultModel.setVin(vin);
                detailResultModel.setSeqNo(dataCheckRuleItem.getSeqNo());
                detailResultModel.setDataItemName(dataCheckRuleItem.getItemName());
                detailResultModel.setItemPacketNum(0);
                detailResultModel.setItemExceptionNum(0);
                detailResultModel.setDataItemException(dataCheckRule.getDataItemException());
                detailResultModel.setItemRealException(0d);
                detailResultModel.setItemExceptionResult(1);
                detailResultModel.setCreateBy(ServletUtil.getCurrentUser());
                detailResultModel.setCreateTime(endRangeStr);
                dataItemCheckResultService.insert(detailResultModel);
                itemCheckResultModels.add(detailResultModel);
            }
        }

        return new DataCheckResult(checkRecord, itemCheckResultModels);
    }

    /**
     * 局部数据项检测结果内部类
     */
    class PartDataCheckResult {

        /**
         * 数据项编码
         */
        private String seqNo;

        /**
         * 检测报文数
         */
        private int checkPackNum = 0;

        /**
         * 异常报文数
         */
        private int errorPackNum = 0;

        public int getCheckPackNum() {
            return checkPackNum;
        }

        public String getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(String seqNo) {
            this.seqNo = seqNo;
        }

        public void setCheckPackNum(int checkPackNum) {
            this.checkPackNum = checkPackNum;
        }

        public int getErrorPackNum() {
            return errorPackNum;
        }

        public void setErrorPackNum(int errorPackNum) {
            this.errorPackNum = errorPackNum;
        }
    }

    /**
     * 组装查询参数
     *
     * @return
     */
    private ProtocolQueryModel buildProtocolRequest(String beginTimeStr, String endTimeStr,
                                                    VehicleModel vehicleModel) {
        ProtocolQueryModel protocolQueryModel = new ProtocolQueryModel();
        protocolQueryModel.setVid(vehicleModel.getUuid());
        protocolQueryModel.setStartTime(beginTimeStr);
        protocolQueryModel.setEndTime(endTimeStr);
        //国六和国标区分
        if (vehicleModel.isG6()) {
            protocolQueryModel.setRuleType(RuleTypeEnum.GB_T17691.getCode());
        }
        if (vehicleModel.isGb()) {
            protocolQueryModel.setRuleType(RuleTypeEnum.GB_T32960.getCode());
        }
        protocolQueryModel.setPageNo(1);
        protocolQueryModel.setPageSize(Integer.MAX_VALUE);
        protocolQueryModel.setAsc(true);
        return protocolQueryModel;
    }

    /**
     * 检测前校验：
     * -1:正常，1:VIN码输入不正确，2:VIN码不存在或无此车权限，3:该车辆（VIN码）没有绑定检测规则
     */
    @Override
    public int validateCheck(String vin) {

        if (StringUtils.isEmpty(vin) || vin.length() != 17 || !vin.matches("^[a-zA-Z0-9]{17}$")) {
            return 1;
        }

        VehicleModel vehicleModel = vehicleService.getByVinValidateAuth(vin);
        if (null == vehicleModel) {
            return 2;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("vehModelId", vehicleModel.getVehModelId());
        params.put("protocolId", vehicleModel.getSupportProtocol());
        DataCheckRule dataCheckRule = dataCheckRuleMapper.findVehCheckRule(params);
        if (null == dataCheckRule) {
            return 3;
        }


        return -1;
    }

    @Override
    public DataCheckStatistic statistic(DataCheckStatisticParam param) {

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("beginTime", param.getBeginTime());
        params.put("endTime", param.getEndTime());

        //检测车辆数变化趋势（按日期，需要二次处理）
        List<DataCheckNumStatistic> numStatistics = dataCheckRecordMapper.getNumStatistics(params);
        //各车辆型号中检测结果分布
        List<DataCheckVehModelStatistic> vehModelStatistics = dataCheckRecordMapper.getVehModelStatistics(params);
        //检测未通过车辆异常分布
        DataCheckErrorDistribution errorDistribution = dataCheckRecordMapper.getCheckErrorNum(params);

        //统计横轴点以及对应统计结果map
        Map<String, DataCheckNumStatistic> resultMap = new HashMap<>();

        //处理检测车辆数变化趋势结果，按维度进行不同处理
        switch (param.getDimension()) {
            case 1:
            case 2: {
                numStatistics.forEach(it -> {
                    it.setStatisticItem(StringUtils.substring(it.getStatisticItem(), 0, 7));
                    DataCheckNumStatistic numStatistic;
                    if (resultMap.containsKey(it.getStatisticItem())) {
                        numStatistic = resultMap.get(it.getStatisticItem());
                    } else {
                        numStatistic = new DataCheckNumStatistic();
                        numStatistic.setStatisticItem(it.getStatisticItem());
                        resultMap.put(it.getStatisticItem(), numStatistic);
                    }
                    numStatistic.setCheckNum(numStatistic.getCheckNum() + it.getCheckNum());
                    numStatistic.setErrorNum(numStatistic.getErrorNum() + it.getErrorNum());
                    numStatistic.setPassNum(numStatistic.getPassNum() + it.getPassNum());
                });
                //获取日期范围列表
                List<String> range = com.bitnei.cloud.sys.util.DateUtil.getBetweenRange(
                        2, param.getBeginTime(), param.getEndTime()).stream().map(it ->
                        StringUtils.substring(it, 0, 7)).collect(Collectors.toList());

                //处理完的数据保存到原对象中，
                numStatistics = buildResult(resultMap, range);
                break;
            }
            case 3: {
                numStatistics.forEach(it -> {
                    it.setStatisticItem(StringUtils.substring(it.getStatisticItem(), 5, 10));
                    resultMap.put(it.getStatisticItem(), it);
                });
                //获取日期范围列表
                List<String> range = com.bitnei.cloud.sys.util.DateUtil.getBetweenRange(
                        1, param.getBeginTime(), param.getEndTime()).stream().map(it ->
                        StringUtils.substring(it, 5, 10)).collect(Collectors.toList());

                numStatistics = buildResult(resultMap, range);
                break;
            }
            default:
                break;
        }

        //处理检测未通过车辆异常分布
        BigDecimal zero = new BigDecimal(0);
        Integer totalNum = errorDistribution.getDataErrorNum() + errorDistribution.getLossCheckErrorNum() +
                errorDistribution.getPacketTypeErrorNum();
        if (totalNum <= 0) {
            //如果异常全部为0，则无异常分布
            errorDistribution.setExistException(false);
            errorDistribution.setDataErrorRate(zero);
            errorDistribution.setLossCheckErrorRate(zero);
            errorDistribution.setPacketTypeErrorRate(zero);
        } else {
            //否则存在异常分布
            errorDistribution.setExistException(true);
            errorDistribution.setDataErrorRate(new BigDecimal((double) errorDistribution.getDataErrorNum()
                    / (double) totalNum).setScale(3, BigDecimal.ROUND_HALF_UP));
            errorDistribution.setLossCheckErrorRate(new BigDecimal((double) errorDistribution.getLossCheckErrorNum()
                    / (double) totalNum).setScale(3, BigDecimal.ROUND_HALF_UP));
            errorDistribution.setPacketTypeErrorRate(new BigDecimal(1).subtract(
                    errorDistribution.getDataErrorRate()).subtract(errorDistribution.getLossCheckErrorRate())
                    .setScale(3, BigDecimal.ROUND_HALF_UP));

        }

        DataCheckStatistic dataCheckStatistic = new DataCheckStatistic();
        dataCheckStatistic.setNumStatistics(numStatistics);
        dataCheckStatistic.setVehModelStatistics(vehModelStatistics);
        dataCheckStatistic.setErrorDistribution(errorDistribution);

        return dataCheckStatistic;
    }

    /**
     * 组装结果
     *
     * @param resultMap 结果集合
     * @param range     时间范围
     * @return
     */
    private List<DataCheckNumStatistic> buildResult(Map<String, DataCheckNumStatistic> resultMap,
                                                    List<String> range) {
        return range.stream().map(it -> {
            if (!resultMap.containsKey(it)) {
                DataCheckNumStatistic numStatistic = new DataCheckNumStatistic();
                numStatistic.setStatisticItem(it);
                return numStatistic;
            }
            return resultMap.get(it);
        }).collect(Collectors.toList());
    }

    @Override
    public DataCheckResult getCheckResult(String id) {
        DataCheckRecordModel recordModel = get(id);
        PagerInfo pagerInfo = new PagerInfo();
        List<DataItemCheckResultModel> results =
                (List<DataItemCheckResultModel>) dataItemCheckResultService.list(pagerInfo);
        return new DataCheckResult(recordModel, results);
    }
}
