package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.client.VehRiskClient;
import com.bitnei.cloud.fault.dao.VehRiskNoticeMapper;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.VehRiskNotice;
import com.bitnei.cloud.fault.model.*;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.ISendFaultAlarmMessageService;
import com.bitnei.cloud.fault.service.IVehRiskNoticeService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehRiskNoticeService实现<br>
 * 描述： VehRiskNoticeService实现<br>
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
 * <td>2019-07-08 18:07:56</td>
 * <td>lijiezhou</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author lijiezhou
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.VehRiskNoticeMapper")
public class VehRiskNoticeService extends BaseService implements IVehRiskNoticeService {

    @Resource
    private IVehicleService vehicleService;

    @Resource
    private ISendFaultAlarmMessageService sendFaultAlarmMessageService;

    @Resource
    private VehRiskNoticeMapper vehRiskNoticeMapper;

    @Resource
    private VehRiskClient vehRiskClient;

    @Resource
    private IAlarmInfoService alarmInfoService;

    @Resource
    private IAlarmInfoHistoryService alarmInfoHistoryService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        Map<String, Object> opinionParams = new HashMap<String,Object>();
        opinionParams.put("trueOrFalse",1);

        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehRiskNotice> entries = findBySqlId("pagerModel", params);
            List<VehRiskNoticeModel> models = new ArrayList();
            for (VehRiskNotice entry : entries) {
                VehRiskNotice obj = (VehRiskNotice) entry;
                opinionParams.put("code",obj.getCode());
                FaultDisposalOpinionsModel model = unique("findOpinionsByCode", opinionParams);
                obj.setOpinion(model == null ? null : model.getOpinions());
                models.add(VehRiskNoticeModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehRiskNoticeModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                VehRiskNotice obj = (VehRiskNotice) entry;
                opinionParams.put("code",obj.getCode());
                FaultDisposalOpinionsModel model = unique("findOpinionsByCode", opinionParams);
                obj.setOpinion(model == null ? null : model.getOpinions());
                models.add(VehRiskNoticeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object historyList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehRiskNotice> entries = findBySqlId("historyPagerModel", params);
            List<VehRiskNoticeModel> models = new ArrayList();
            for (VehRiskNotice entry : entries) {
                VehRiskNotice obj = (VehRiskNotice) entry;
                lastHistory(obj);
                VehRiskNoticeModel data = VehRiskNoticeModel.fromEntry(obj);
                models.add(data);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("historyPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehRiskNoticeModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                VehRiskNotice obj = (VehRiskNotice) entry;
                lastHistory(obj);
                VehRiskNoticeModel data = VehRiskNoticeModel.fromEntry(obj);
                models.add(data);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object findAnnotationsByCode(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = new HashMap<String, Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehRiskHistoryAdjudicateModel> models = findBySqlId("findAnnotationsByCode", params);
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findAnnotationsByCode", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehRiskHistoryAdjudicateModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                models.add((VehRiskHistoryAdjudicateModel) entry);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object findOpinionsByCode(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = new HashMap<String, Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<FaultDisposalOpinionsModel> models = findBySqlId("findOpinionsByCode", params);
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findOpinionsByCode", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<FaultDisposalOpinionsModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                models.add((FaultDisposalOpinionsModel) entry);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public VehRiskNoticeModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");
        params.put("id", id);
        VehRiskNotice entry = unique("findById", params);
        if (entry != null) {
            Map<String, Object> opinionParams = new HashMap<String,Object>();
            opinionParams.put("trueOrFalse",1);
            opinionParams.put("code",entry.getCode());
            FaultDisposalOpinionsModel model = unique("findOpinionsByCode", opinionParams);
            entry.setOpinion(model == null ? null : model.getOpinions());
        }
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehRiskNoticeModel.fromEntry(entry);
    }

    @Override
    public VehRiskNoticeModel getCount() {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        VehRiskNotice entry = unique("findByNoticeStateAndRiskLevelCount", params);
        if (entry == null) {
            entry = new VehRiskNotice();
            entry.setLevelOneAndTwo(0);
            entry.setLevelThreeAndFour(0);
            entry.setLevelFive(0);
            entry.setUnRead(0);
            entry.setUnReply(0);
            entry.setReply(0);
            entry.setExamining(0);
        }
        VehRiskNoticeModel model = VehRiskNoticeModel.fromEntry(entry);
        // app端展示占比
        if (model.getAllNum() != null && model.getAllNum() > 0){
            model.setUnReadRatio(getRatio(model.getUnRead(),model.getAllNum()));
            model.setUnReplyRatio(getRatio(model.getUnReply(),model.getAllNum()));
            model.setReplyRatio(getRatio(model.getReply(),model.getAllNum()));
            model.setExaminingRatio(getRatio(model.getExamining(),model.getAllNum()));
        }
        else{
            model.setUnReadRatio("0%");
            model.setUnReplyRatio("0%");
            model.setReplyRatio("0%");
            model.setExaminingRatio("0%");
        }
        return model;
    }

    public String getRatio(Integer num, Integer all){
        DecimalFormat d = (DecimalFormat) NumberFormat.getPercentInstance();
        return d.format((double) num / all);
    }

    @Override
    public void insert(VehRiskNoticeModel model) {
        VehRiskNotice obj = new VehRiskNotice();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        model.setId(id);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehRiskNoticeModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");

        VehRiskNotice obj = new VehRiskNotice();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public void updateReadByCode(String code) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");
        Map<String, Object> params2 = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");

        String[] arr = code.split(",");
        List<String> codes = new ArrayList<String>();
        List<String> codes2 = new ArrayList<String>();
        VehRiskNotice obj = new VehRiskNotice();
        VehRiskNotice obj2 = new VehRiskNotice();
        String nowTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        // 已读时间
        obj.setReadTime(nowTime);
        // 通知状态为1：已读
        obj.setNoticeStatus(1);
        obj.setStateUpdateTime(nowTime);
        obj.setProcessingTime(nowTime);
        obj.setLastOperator(updateBy);
        obj.setUpdateTime(nowTime);
        obj.setUpdateBy(updateBy);
        obj2.setProcessingTime(nowTime);
        obj2.setLastOperator(updateBy);
        obj2.setUpdateTime(nowTime);
        obj2.setUpdateBy(updateBy);
        List<SafeUpSateModel> list = Lists.newArrayList();
        for (String s : arr) {
            VehRiskNotice data = unique("findByCode", s);
            if (data != null && data.getNoticeStatus() != null){
                if (data.getNoticeStatus() == 0 || data.getNoticeStatus() == 2){
                    codes.add(s);
                    SafeUpSateModel safeUpSateModel = new SafeUpSateModel();
                    safeUpSateModel.setCode(s);
                    safeUpSateModel.setVin(data.getVin());
                    safeUpSateModel.setState("1");
                    safeUpSateModel.setDangerLevel(String.valueOf(data.getRiskLevel()));
                    safeUpSateModel.setUpdateTime(nowTime);
                    list.add(safeUpSateModel);
                }
                // 非未读消息执行批量阅读时，只更新处理时间和操作人
                else {
                    codes2.add(s);
                }
            }
        }
        if (codes.size() > 0){
            ResultMsgModel resultMsgModel = vehRiskClient.safeUpSate(list);
            if (resultMsgModel.getCode() == 0){
                obj.setCodes(codes);
                params.putAll(MapperUtil.Object2Map(obj));
                super.update("updateByCode", params);
            }
            else {
                throw new BusinessException("批量阅读异常");
            }
        }
        if (codes2.size() > 0){
            obj2.setCodes(codes2);
            params2.putAll(MapperUtil.Object2Map(obj2));
            super.update("updateByCode", params2);
        }
    }

    @Override
    public void updateReplyByCode(VehRiskNoticeModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");
        Map<String, Object> params2 = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");

        String people = ServletUtil.getCurrentUser();
        String nowTime = DateUtil.getNow();
        String[] arr = model.getCode().split(",");
        // 用于插入记录表
        // 用于更新0、2、1、3 未读、未读超时、已读、未回复状态
        List<String> codes1 = new ArrayList<String>();
        VehRiskNotice obj = new VehRiskNotice();
        obj.setNoticeStatus(4);
        obj.setStateUpdateTime(nowTime);
        obj.setProcessingTime(nowTime);
        obj.setUpdateTime(nowTime);
        obj.setUpdateBy(people);
        obj.setLastOperator(people);
        // 用于更新5、6 已回复、审核中状态
        List<String> codes2 = new ArrayList<String>();
        VehRiskNotice obj2 = new VehRiskNotice();
        obj2.setProcessingTime(nowTime);
        obj2.setUpdateTime(nowTime);
        obj2.setUpdateBy(people);
        obj2.setLastOperator(people);
        List<String> codes = new ArrayList<String>();
        for (String s : arr) {
            VehRiskNotice entry = unique("findByCode", s);
            // 1、2、3、4 未读、未读超时、已读、未回复
            if (entry.getNoticeStatus() != null){
                if (entry.getNoticeStatus() != 4 && entry.getNoticeStatus() != 5) {
                    codes1.add(s);
                }
                else{
                    codes2.add(s);
                }
            }
        }
        if (codes1.size()>0){
            // 修改状态传值给国家平台
            List<SafeUpReplySateModel> list = Lists.newArrayList();
            codes1.forEach(s -> {
                VehRiskNotice vehRiskNotice = unique("findByCode", s);
                SafeUpReplySateModel data = new SafeUpReplySateModel();
                data.setVin(vehRiskNotice.getVin());
                data.setCode(s);
                // 消息状态改为 4：已回复
                data.setState("4");
                data.setUpdateTime(nowTime);
                data.setNote(model.getAnnotations());
                data.setDocs(model.getFileId());
                list.add(data);
            });

            ResultMsgModel resultMsgModel = vehRiskClient.safeUpReplySate(list);

            if (resultMsgModel.getCode() == 1){
                throw new BusinessException("批量回复失败");
            }
            codes.addAll(codes1);
            obj.setCodes(codes1);
            params.putAll(MapperUtil.Object2Map(obj));
            // 0、2、1、3未读、未读超时、已读、未回复状态的更新调用这条sql
            int res = super.update("updateByCode2", params);
        }
        if (codes2.size()>0){
            // 修改状态传值给国家平台
            List<SafeUpReplySateModel> list = Lists.newArrayList();
            codes2.forEach(s -> {
                VehRiskNotice vehRiskNotice = unique("findByCode", s);
                SafeUpReplySateModel data = new SafeUpReplySateModel();
                data.setVin(vehRiskNotice.getVin());
                data.setCode(s);
                // 消息状态改为4/5  已回复/审核中  不需更改状态
                data.setState(String.valueOf(vehRiskNotice.getNoticeStatus()));
                data.setUpdateTime(nowTime);
                data.setNote(model.getAnnotations());
                data.setDocs(model.getFileId());
                list.add(data);
            });
            ResultMsgModel resultMsgModel = vehRiskClient.safeUpReplySate(list);
            if (resultMsgModel.getCode() == 1){
                throw new BusinessException("批量回复失败");
            }
            codes.addAll(codes2);
            obj2.setCodes(codes2);
            params2.putAll(MapperUtil.Object2Map(obj2));
            // 4/5  已回复/审核中调用这条sql
            int res2 = super.update("updateByCode2-2", params2);
        }
        List<VehRiskHistoryAdjudicateModel> listHistory = Lists.newArrayList();
        codes.forEach(s -> {
            VehRiskHistoryAdjudicateModel history = new VehRiskHistoryAdjudicateModel();
            history.setFileId(model.getFileId());
            history.setAnnotations(model.getAnnotations());
            history.setCreateTime(nowTime);
            history.setCreateBy(people);
            String id = UtilHelper.getUUID();
            history.setId(id);
            history.setCode(s);
            listHistory.add(history);
        });
        int res3 = vehRiskNoticeMapper.insertHistory(listHistory);
        if (res3 == 0){
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_veh_risk_notice", "vrn");

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
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<SafeRiskModel> models) {
        for (SafeRiskModel model : models) {
            VehRiskNoticeModel noticeModel = new VehRiskNoticeModel();
            VehicleModel vm = vehicleService.getByVin(model.getVin());
            VehRiskNotice vrn = unique("findByCode", model.getCode());
            if(vrn != null ) {
                log.error("消息编号({})已存在", model.getCode());
                continue;
            }
            noticeModel.setVehicleId(vm.getId());
            noticeModel.setCode(model.getCode());
            // 报警发生时间
            noticeModel.setFirstNoticeTime(model.getAlarmTime());
            noticeModel.setNoticeStatus(VehRiskNotice.READ_STATUS_UN);
            noticeModel.setName(model.getAlarmName());
            noticeModel.setNoticeType(Integer.parseInt(model.getType()));
            noticeModel.setStateUpdateTime(model.getAlarmTime());
            if(StringUtils.isNumeric(model.getDangerLevel())) {
                noticeModel.setRiskLevel(Integer.parseInt(model.getDangerLevel()));
            }
            // 通知原文
            String noticeOriginalText = String.format("车牌为:%s, VIN码为:%s, 于%s发生%s, 请及时处理!", vm.getLicensePlate(),
                    vm.getVin(), noticeModel.getFirstNoticeTime(), noticeModel.getName());
            noticeModel.setNoticeOriginalText(noticeOriginalText);
            insert(noticeModel);
            // 通知ws
            try {
                sendFaultAlarmMessageService.sendRiskMessageToWeb(noticeModel);
            } catch (Exception ex) {
                log.error("推送安全风险消息到web出错：", ex);
            }
            // 极光推送
            try {
                sendFaultAlarmMessageService.sendRiskMessageJiGuang(noticeModel);
            } catch (Exception ex) {
                log.error("推送安全风险消息到app出错", ex);
            }


        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<SafeRiskUpModel> models) {

        String nowTime = DateUtil.getNow();
        for (SafeRiskUpModel model : models) {

            VehRiskNotice vrn = unique("findByCode", model.getCode());
            if(vrn == null ) {
                log.error("消息编号({})不存在", model.getCode());
                continue;
            }
            if(!vrn.getVin().equals(model.getVin())) {
                log.error("消息编号({})与vin({})不匹配", model.getCode(), model.getVin());
                continue;
            }
            if(vrn.getNoticeStatus().equals(6)) {
                log.error("消息编号({})已处理完成", model.getCode());
                continue;
            }

            VehRiskNoticeModel noticeModel = new VehRiskNoticeModel();
            int status = Integer.parseInt(model.getState());
            if (status == 5 || status == 7){
                // 审核中
                noticeModel.setNoticeStatus(5);
            }
            else {
                noticeModel.setNoticeStatus(status);
            }

            noticeModel.setCode(model.getCode());
            noticeModel.setVin(model.getVin());
            if(StringUtils.isNumeric(model.getDangerLevel())) {
                noticeModel.setRiskLevel(Integer.parseInt(model.getDangerLevel()));
            } else {
                noticeModel.setRiskLevel(vrn.getRiskLevel());
            }
            noticeModel.setStateUpdateTime(model.getUpdateTime());
            this.update(noticeModel);

            if (status == 4 || status == 5 || status == 6 || status == 7){
                // 添加意见到历史意见记录表
                String id = UtilHelper.getUUID();
                String createBy = ServletUtil.getCurrentUser();
                FaultDisposalOpinionsModel faultDisposalOpinionsModel = new FaultDisposalOpinionsModel();
                faultDisposalOpinionsModel.setCode(model.getCode());
                faultDisposalOpinionsModel.setTime(model.getUpdateTime());
                faultDisposalOpinionsModel.setOpinions(model.getDealNote());
                faultDisposalOpinionsModel.setCreateTime(nowTime);
                faultDisposalOpinionsModel.setId(id);
                faultDisposalOpinionsModel.setCreateBy(createBy);
                insert("insertOpinionsByCode",faultDisposalOpinionsModel);
            }
        }
    }

    @Override
    public Object alarmInfoList(PagerInfo pagerInfo) {

        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String end = String.valueOf(params.get("firstNoticeTime"));
        // 传入时间 结束时间为首次通知时间，开始时间往前面推10分钟
        String begin = DateUtil.getDate(DateUtil.strToDate_ex_full(end).getTime() - 600000);
        params.put("beginTime",begin);
        params.put("endTime",end);
        // 排除围栏报警
        params.put("excludeFaultTypes",3);
        // isNow 为1时查询当前警报
        if (params.get("isNow") != null && "1".equals(String.valueOf(params.get("isNow")))){
            //非分页查询
            if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
                List<AlarmInfo> entries = alarmInfoService.findBySqlId("pagerModel", params);
                List<AlarmInfoModel> models = new ArrayList<>();
                long nowTime = System.currentTimeMillis();
                for(AlarmInfo entry: entries) {
                    AlarmInfoModel data = AlarmInfoModel.fromEntry(entry);
                    this.getDuration(data,nowTime);
                    models.add(data);
                }
                return models;
            }
            //分页查询
            else {
                PagerResult pr = alarmInfoService.findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
                List<AlarmInfoModel> models = Lists.newArrayList();
                long nowTime = System.currentTimeMillis();
                for (Object entry : pr.getData()) {
                    AlarmInfo obj = (AlarmInfo) entry;
                    AlarmInfoModel data = AlarmInfoModel.fromEntry(obj);
                    this.getDuration(data,nowTime);
                    models.add(data);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
        // 查询历史已结束警报
        else {

            Condition param = new Condition();
            param.setName("beginTime");
            param.setValue(begin);
            Condition param2 = new Condition();
            param2.setName("endTime");
            param2.setValue(end);
            Condition param3 = new Condition();
            param3.setName("excludeFaultTypes");
            param3.setValue("3");
            if (params.get("ruleName") != null){
                Condition param4 = new Condition();
                param4.setName("ruleName");
                param4.setValue(String.valueOf(params.get("ruleName")));
                pagerInfo.getConditions().add(param4);
            }
            if (params.get("faultType") != null){
                Condition param5 = new Condition();
                param5.setName("faultType");
                param5.setValue(String.valueOf(params.get("faultType")));
                pagerInfo.getConditions().add(param5);
            }
            pagerInfo.getConditions().add(param);
            pagerInfo.getConditions().add(param2);
            pagerInfo.getConditions().add(param3);
            return alarmInfoHistoryService.list(pagerInfo);
        }

    }

    private void getDuration(AlarmInfoModel model, long nowTime){

        if (model.getFaultBeginTime() != null){
            if (model.getFaultEndTime() == null){
                long duration = nowTime - DateUtil.strToDate_ex_full(model.getFaultBeginTime()).getTime();
                model.setDuration(this.format(duration));
            }
            else {
                long duration = DateUtil.strToDate_ex_full(model.getFaultEndTime()).getTime() - DateUtil.strToDate_ex_full(model.getFaultBeginTime()).getTime();
                model.setDuration(this.format(duration));
            }
        }
    }

    private String format(long time){

        int day = (int)(time / 86400000);
        int hour = (int)(time % 86400000 / 3600000);
        int minute = (int)(time % 86400000 % 3600000 / 60000);
        int second = (int)(time % 86400000 % 3600000 % 60000 / 1000);
        // 返回报警持续时长新的时间格式
        return String.format("%s天%s小时%s分%s秒", day, hour, minute, second);
    }

    @Override
    public void updateNoticeStatus(){

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("type",1);
        List<VehRiskNotice> list = findBySqlId("findNotReadRisk",params);
        long nowTime = System.currentTimeMillis();
        String nowDate = DateUtil.getDate(nowTime);
        // 存储需要更新状态的code值
        List<String> listCode = Lists.newArrayList();
        list.forEach(p -> {
            String time = p.getFirstNoticeTime();
            long firstNoticeTime = DateUtil.strToDate_ex_full(time).getTime();
            int oneHour = 3600000;
            if (nowTime - firstNoticeTime >= oneHour){
                listCode.add(p.getCode());
            }
        });
        if (listCode.size()>0){
            VehRiskNoticeModel model = new VehRiskNoticeModel();
            model.setCodes(listCode);
            model.setStateUpdateTime(nowDate);
            model.setNoticeStatus(2);
            model.setUpdateTime(nowDate);
            model.setUpdateBy(ServletUtil.getCurrentUser());
            Map<String,Object> params2 = new HashMap<String,Object>();
            params2.putAll(MapperUtil.Object2Map(model));
            update("updateNoticeStatus",params2);
        }
        params.put("type",2);
        List<String> listCode2 = Lists.newArrayList();
        List<VehRiskNotice> list2 = findBySqlId("findNotReadRisk",params);
        list2.forEach(p -> {
            String time = p.getFirstNoticeTime();
            long firstNoticeTime = DateUtil.strToDate_ex_full(time).getTime();
            int oneday = 86400000;
            if (nowTime - firstNoticeTime >= oneday){
                listCode2.add(p.getCode());
            }
        });
        if (listCode2.size()>0){
            VehRiskNoticeModel model = new VehRiskNoticeModel();
            model.setCodes(listCode2);
            model.setStateUpdateTime(nowDate);
            model.setNoticeStatus(3);
            model.setUpdateTime(nowDate);
            model.setUpdateBy(ServletUtil.getCurrentUser());
            Map<String,Object> params2 = new HashMap<String,Object>();
            params2.putAll(MapperUtil.Object2Map(model));
            update("updateNoticeStatus",params2);
        }
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehRiskNotice>(this, "pagerModel", params, "fault/res/vehRiskNotice/export.xls", "当前国家平台车辆风险通知") {
            @Override
            public Object process(VehRiskNotice entity) {
                combineAnnotationsAndOpinions(entity);
                return super.process(entity);
            }
        }.work();

        return;

    }

    @Override
    public void historyExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehRiskNotice>(this, "historyPagerModel", params, "fault/res/vehRiskNotice/historyExport.xls", "当前国家平台车辆风险通知") {
            @Override
            public Object process(VehRiskNotice entity) {
                lastHistory(entity);
                return super.process(entity);
            }
        }.work();

        return;

    }

    private void lastHistory(VehRiskNotice data){

        Map<String, Object> newParams = new HashMap<String,Object>(16);
        newParams.put("code",data.getCode());
        newParams.put("trueOrFalse",1);
        VehRiskHistoryAdjudicateModel model1 = unique("findAnnotationsByCode", newParams);
        FaultDisposalOpinionsModel model2 = unique("findOpinionsByCode", newParams);
        if (model1 != null){
            data.setLastAnnotations(model1.getAnnotations());
            data.setLastOpinionTime(model1.getCreateTime());
        }
        if (model2 != null){
            data.setOpinion(model2.getOpinions());
        }
    }

    /** 当前风险等级通知 导出时 合并所有管理员意见、处理结果**/
    private void combineAnnotationsAndOpinions(VehRiskNotice data){

        Map<String, Object> newParams = new HashMap<>(16);
        newParams.put("code",data.getCode());
        List<VehRiskHistoryAdjudicateModel> adjudicateList = findBySqlId("findAnnotationsByCode", newParams);
        List<FaultDisposalOpinionsModel> opinionList = findBySqlId("findOpinionsByCode", newParams);
        if (adjudicateList != null){
            StringBuilder annotationsStr = new StringBuilder();
            for (VehRiskHistoryAdjudicateModel adjudicate : adjudicateList){
                annotationsStr.append(adjudicate.getCreateTime()).append(" ");
                annotationsStr.append(adjudicate.getAnnotations()).append("；");
            }
            data.setAnnotations(annotationsStr.toString());
        }
        if (opinionList != null){
            StringBuilder opinionStr = new StringBuilder();
            for (FaultDisposalOpinionsModel opinion : opinionList){
                opinionStr.append(opinion.getTime()).append(" ");
                opinionStr.append(opinion.getOpinions()).append("；");
            }
            data.setOpinion(opinionStr.toString());
        }
    }
}
