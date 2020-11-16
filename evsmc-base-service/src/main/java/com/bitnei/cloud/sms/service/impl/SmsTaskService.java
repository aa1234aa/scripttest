package com.bitnei.cloud.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.*;
import com.bitnei.cloud.common.client.SmsServiceClient;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.compent.kafka.service.LocalThreadFactory;
import com.bitnei.cloud.fault.model.NotifierSettingModel;
import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sms.dao.SmsTaskItemMapper;
import com.bitnei.cloud.sms.dao.SmsTaskMapper;
import com.bitnei.cloud.sms.domain.SmsTask;
import com.bitnei.cloud.sms.domain.SmsTaskItem;
import com.bitnei.cloud.sms.model.*;
import com.bitnei.cloud.sms.service.ISmsTaskService;
import com.bitnei.cloud.sys.dao.MsgTemplateMapper;
import com.bitnei.cloud.sys.dao.OwnerPeopleMapper;
import com.bitnei.cloud.sys.domain.MsgTemplate;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.model.VehOwnerModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： SmsTaskService实现<br>
 * 描述： SmsTaskService实现<br>
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
 * <td>2019-08-16 09:41:04</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sms.dao.SmsTaskMapper")
@RequiredArgsConstructor
public class SmsTaskService extends BaseService implements ISmsTaskService {

    @Resource
    private SmsTaskMapper smsTaskMapper;

    @Resource
    private SmsTaskItemMapper smsTaskItemMapper;

    @Autowired
    private IVehOwnerService vehOwnerService;

    @Autowired
    private IOwnerPeopleService ownerPeopleService;

    @Autowired
    private INotifierSettingService notifierSettingService;

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private SmsServiceClient smsServiceClient;

    private final ScheduledExecutorService smsSendCallbackScheduleExecutor;

    @Resource
    private OwnerPeopleMapper ownerPeopleMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<SmsTask> entries = findBySqlId("pagerModel", params);
            List<SmsTaskModel> models = new ArrayList();
            for (SmsTask entry : entries) {
                SmsTask obj = (SmsTask) entry;
                models.add(SmsTaskModel.fromEntry(obj));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<SmsTaskModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                SmsTask obj = (SmsTask) entry;
                models.add(SmsTaskModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public SmsTaskModel get(String id) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.put("id", id);
        SmsTask entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        SmsTaskModel model = SmsTaskModel.fromEntry(entry);
        model.setVariables(JSONObject.parseArray(entry.getVariables(), FieldModel.class));

        //草稿
        if (entry.getStatus().intValue() == 1) {
            params = new HashMap<>();
            params.put("taskId", id);

            List<SmsTaskItem> smsTaskItems = smsTaskItemMapper.listSmsTaskItemByTaskId(params);
            if (CollectionUtils.isNotEmpty(smsTaskItems)) {
                model.setItems(smsTaskItems);
                /*model.setReceiverType(smsTaskItems.get(0).getReceiverType());*/
            }
        } else {
            Map<String, BigDecimal> successMap = smsTaskItemMapper.successTotal(id);
            String total = ((successMap != null && !successMap.isEmpty()) ? String.valueOf(successMap.get("total")) : "0");
            String successTotal = ((successMap != null && !successMap.isEmpty()) ? String.valueOf(successMap.get("successTotal")) : "0");
            model.setStatistic(successTotal + "/" + total);
        }
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(SmsTaskModel model) {
        if (StringUtils.isBlank(model.getReceiverIds())) {
            throw new BusinessException("请选择接收用户...");
        }
        model.setServiceType(1);

        //1、保存task
        SmsTask obj = new SmsTask();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());

        //查询模板
        SmsTemplateModel smsTemplate = getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }

        //短信内容
        String smsContent = replaceSmsContent(smsTemplate.getContent(), model.getVariables());
        obj.setSmsContent(smsContent);

        //参数json
        String variable = JSON.toJSONString(model.getVariables());
        obj.setVariables(variable);

        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }

        //联系人信息
        String strReceiverIds = "";
        List<UserModel> userModels = new ArrayList<>();
        Map<Integer, String> userIdType = sendUserType(model.getReceiverIds());
        for (Map.Entry<Integer, String> entry : userIdType.entrySet()) {
            Map<String, String> condition = new HashMap<>();
            condition.put("ids", entry.getValue());
            PagerInfo pagerInfo = ServletUtil.mapToPagerInfo(condition);
            userModels.addAll(listReceiverInfo(pagerInfo, entry.getKey()));
            if (StringUtils.isBlank(strReceiverIds)) {
                strReceiverIds = entry.getValue();
            } else {
                strReceiverIds = strReceiverIds + "," + entry.getValue();
            }
        }

        //2、保存明细
        List<String> msisds = addItem(id, strReceiverIds, userModels);

        //3、发送短信
        if (model.getStatus().intValue() == 0) {
            send(model, smsTemplate.getTemplateId(), msisds, id);
        }
    }

    private Map<Integer, String> sendUserType(String receiverIds) {
        Map<Integer, String> userTypeMap = new HashMap<>();
        String[] strReceiverIds = receiverIds.split(",");
        for (String receiverId : strReceiverIds) {
            String[] idType = receiverId.split("-");
            if (idType.length != 2) {
                continue;
            }

            Integer type = Integer.valueOf(idType[0]);
            String ids = idType[1];

            if (userTypeMap.containsKey(type)) {
                ids = userTypeMap.get(type) + "," + ids;

            }
            userTypeMap.put(type, ids);
        }
        return userTypeMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SmsTaskModel model) {

        //删除
        smsTaskItemMapper.deleteByTaskId(model.getId());

        SmsTask obj = new SmsTask();
        obj.setId(model.getId());
        obj.setStatus(model.getStatus());
        obj.setRemarks(model.getRemarks());

        //查询模板
        SmsTemplateModel smsTemplate = getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }
        //短信内容
        String smsContent = replaceSmsContent(smsTemplate.getContent(), model.getVariables());
        obj.setSmsContent(smsContent);
        //参数json
        String variable = JSON.toJSONString(model.getVariables());
        obj.setVariables(variable);

        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());

        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        //联系人信息
        String strReceiverIds = "";
        List<UserModel> userModels = new ArrayList<>();
        Map<Integer, String> userIdType = sendUserType(model.getReceiverIds());
        for (Map.Entry<Integer, String> entry : userIdType.entrySet()) {
            Map<String, String> condition = new HashMap<>();
            condition.put("ids", entry.getValue());
            PagerInfo pagerInfo = ServletUtil.mapToPagerInfo(condition);
            userModels.addAll(listReceiverInfo(pagerInfo, entry.getKey()));
            if (StringUtils.isBlank(strReceiverIds)) {
                strReceiverIds = entry.getValue();
            } else {
                strReceiverIds = strReceiverIds + "," + entry.getValue();
            }
        }

        //2、保存明细
        List<String> msisds = addItem(model.getId(), strReceiverIds, userModels);

        //3、发送短信
        if (model.getStatus().intValue() == 0) {
            send(model, smsTemplate.getTemplateId(), msisds, model.getId());
        }
    }

    private void send(SmsTaskModel model, String templateCode, List<String> msisds, String taskId) {

        String phones = Joiner.on(",").join(msisds);
        SmsMessageBody smsMessageBody = new SmsMessageBody();
        smsMessageBody.setPhoneNumber(phones);
        smsMessageBody.setTemplateCode(templateCode);

        Map<String, String> templateParams = new HashMap<>();
        List<FieldModel> list = model.getVariables();
        for (FieldModel fieldModel : list) {
            templateParams.put(fieldModel.getValueName(), fieldModel.getValue());
        }
        smsMessageBody.setTemplateParams(templateParams);
        SendSmsResult result = smsServiceClient.sendSms(smsMessageBody);

        if (result.getCode() == 500) {
            log.error("====短信发送失败======");
            throw new BusinessException("短信发送失败");
        }

        String bizId = result.getBizId();
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        params.put("bizId", bizId);
        params.put("updateBy", ServletUtil.getCurrentUser());
        params.put("updateTime", DateUtil.getNow());
        smsTaskMapper.updateBizId(params);

        params.put("taskId", taskId);
        smsTaskItemMapper.updateBizId(params);

        //发送状态查询
        //processCallBack(bizId, msisds, taskId);
    }

    /**
     * 保存数据项目
     **/
    private List<String> addItem(String taskId, String strReceiverIds, List<UserModel> userModels) {
        SmsTaskItem items = null;
        List<String> msisds = new ArrayList<>();
        String[] receiverIds = strReceiverIds.split(",");
        for (int i = 0; i < receiverIds.length; i++) {
            items = new SmsTaskItem();
            items.setId(UtilHelper.getUUID());
            items.setTaskId(taskId);
            //短信下发
            items.setServiceType(1);
            for (UserModel userModel : userModels) {
                if (receiverIds[i].equals(userModel.getId())) {
                    items.setReceiverId(userModel.getId());
                    items.setReceiver(userModel.getName());
                    items.setMsisd(userModel.getTelPhone());
                    items.setCreateBy(ServletUtil.getCurrentUser());
                    items.setCreateTime(DateUtil.getNow());
                    //用户类型
                    items.setReceiverType(userModel.getUserType());
                    //等待回执
                    items.setSendStatus(1);
                    msisds.add(userModel.getTelPhone());
                    break;
                }
            }
            smsTaskItemMapper.insert(items);
        }
        return msisds;
    }

    private List<UserModel> listReceiverInfo(PagerInfo pagerInfo, Integer receiverType) {
        List<UserModel> list = new ArrayList<>();
        //接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人
        if (receiverType.intValue() == 1) {
            List<OwnerPeopleModel> ownerPeoples = (List<OwnerPeopleModel>) ownerPeopleService.list(pagerInfo);
            for (OwnerPeopleModel ownerPeople : ownerPeoples) {
                UserModel userModel = new UserModel(ownerPeople.getId(), ownerPeople.getOwnerName(), ownerPeople.getTelPhone());
                userModel.setUnitName(ownerPeople.getUnitName());
                userModel.setJobPost(ownerPeople.getJobPost());
                userModel.setJobNumber(ownerPeople.getJobNumber());
                userModel.setUserType(receiverType);
                list.add(userModel);
            }
        } else if (receiverType.intValue() == 2) {
            List<VehOwnerModel> vehOwnerModels = (List<VehOwnerModel>) vehOwnerService.list(pagerInfo);
            for (VehOwnerModel vehOwnerModel : vehOwnerModels) {
                UserModel userModel = new UserModel(vehOwnerModel.getId(), vehOwnerModel.getOwnerName(), vehOwnerModel.getTelPhone());
                userModel.setUserType(receiverType);
                list.add(userModel);
            }
        } else if (receiverType.intValue() == 3) {
            List<NotifierSettingModel> notifierSettingModels = (List<NotifierSettingModel>) notifierSettingService.list(pagerInfo);
            for (NotifierSettingModel model : notifierSettingModels) {
                UserModel userModel = new UserModel(model.getId(), model.getName(), model.getMobile());
                userModel.setAreaName(model.getAreaNames());
                userModel.setUserType(receiverType);
                list.add(userModel);
            }
        }
        return list;
    }

    @Override
    public void processCallBack(String bizId, List<String> phoneNumber, String taskId) {
        //30秒后，执行发送情况

        smsSendCallbackScheduleExecutor.schedule(() -> {
            QuerySendSmsBody querySendSmsBody = null;
            QuerySendSmsResult sendSmsResult = null;
            for (String phone : phoneNumber) {
                querySendSmsBody = new QuerySendSmsBody();
                querySendSmsBody.setBizId(bizId);
                querySendSmsBody.setPhoneNumber(phone);
                querySendSmsBody.setCurrentPage(1);
                querySendSmsBody.setPageSize(10);
                String date = DateUtil.formatTime(new Date(), "yyyyMMdd");
                querySendSmsBody.setSendDate(date);

                sendSmsResult = smsServiceClient.querySendDetail(querySendSmsBody);
                if (sendSmsResult.getCode() == 200) {
                    SmsSendDetail smsSendDetail = sendSmsResult.getSmsSendDetails().get(0);
                    //修改发送状态
                    Map<String, Object> params = new HashMap<>();
                    params.put("sendTime", smsSendDetail.getSendDate());
                    params.put("sendStatus", smsSendDetail.getSendStatus());
                    params.put("failMsg", "DELIVERED".equals(smsSendDetail.getErrCode()) ? "" : smsSendDetail.getErrCode());
                    params.put("taskId", taskId);
                    params.put("msisd", phone);
                    params.put("updateBy", ServletUtil.getCurrentUser());
                    params.put("updateTime", DateUtil.getNow());
                    smsTaskItemMapper.updateSendTimeAndStatus(params);
                }
            }
        }, 40, TimeUnit.SECONDS);
    }

    private String replaceSmsContent(String smsContent, List<FieldModel> variables) {
        for (FieldModel fieldModel : variables) {
            smsContent = smsContent.replaceAll("\\$\\{" + fieldModel.getValueName() + "\\}", fieldModel.getValue());
        }
        return smsContent;
    }

    @Override
    public SmsTemplateModel getSmsTemplate(String templateId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", templateId);

        MsgTemplate msgTemplate = msgTemplateMapper.findById(params);
        SmsTemplateModel model = null;
        if (msgTemplate != null) {
            model = new SmsTemplateModel(msgTemplate.getTemplateName(), msgTemplate.getTemplateId(), msgTemplate.getContent(), msgTemplate.getMsgParam());
            if (StringUtils.isNotBlank(model.getMsgParam())) {
                processSmsParam(model.getMsgParam(), model);
            }
        }
        return model;
    }

    private void processSmsParam(String smsParam, SmsTemplateModel model) {
        String[] fields = smsParam.split(";");
        for (String field : fields) {
            String[] property = field.split("=");
            if (property.length == 2) {
                FieldModel fieldModel = new FieldModel();
                fieldModel.setValueName(property[0].trim());
                fieldModel.setDisplayName(property[1].trim());
                model.getFieldModels().add(fieldModel);
            }
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            //删除任务明细
            smsTaskItemMapper.deleteByTaskId(id);
            count += r;
        }
        return count;
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.put("serviceType", 1);
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<SmsTask>(this, "receiverPagerModel", params, "sms/res/smsTask/export.xls", "短信下发任务") {
        }.work();
        return;
    }

    @Override
    public void exportDetails(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<SmsTaskItem>(this, "exportDetails", params, "sms/res/smsTask/exportDetails.xls", "短信下发任务明细") {
            @Override
            public Object process(SmsTaskItem entity) {
                SmsTaskItemModel model = SmsTaskItemModel.fromEntry(entity);
                if (entity.getReceiverType() != null && entity.getReceiverType().intValue() == 1) {
                    OwnerPeople owner = ownerPeopleMapper.findOwnerById(entity.getReceiverId());
                    model.setUnitName(null != owner ? owner.getUnitName() : "");
                } else {
                    model.setUnitName("");
                }
                model.setTaskUpdateBy(entity.getUpdateBy());
                model.setTaskUpdateTime(entity.getUpdateTime());
                return model;
            }
        }.work();
        return;
    }

    @Override
    public Object receiverPagerModel(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("serviceType", 1);
        PagerResult pr = findPagerModel("receiverPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
        List<SmsTaskModel> models = new ArrayList();
        for (Object entry : pr.getData()) {
            SmsTask obj = (SmsTask) entry;
            models.add(SmsTaskModel.fromEntry(obj));
        }
        pr.setData(Collections.singletonList(models));
        return pr;
    }

    @Override
    public String preview(SmsTaskModel model) {
        //查询模板
        SmsTemplateModel smsTemplateModel = getSmsTemplate(model.getTemplateId());

        //短信内容
        String smsContent = replaceSmsContent(smsTemplateModel.getContent(), model.getVariables());
        return smsContent;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(SmsTaskModel model) {
        Map<String, String> condition = new HashMap<>();

        if (model.getReceiverType().intValue() == 3) {
            condition.put("name", model.getQueryName());
            condition.put("mobile", model.getQueryPhone());
        } else {
            condition.put("ownerName", model.getQueryName());
            condition.put("telPhone ", model.getQueryPhone());
        }
        PagerInfo pagerInfo = ServletUtil.mapToPagerInfo(condition);

        //联系人
        List<UserModel> userModels = listReceiverInfo(pagerInfo, model.getReceiverType());
        if (CollectionUtils.isEmpty(userModels)) {
            throw new BusinessException("该类型下没有联系人");
        }

        //1、保存task
        SmsTask obj = new SmsTask();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());

        //查询模板
        SmsTemplateModel smsTemplate = getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }
        String smsContent = replaceSmsContent(smsTemplate.getContent(), model.getVariables());
        obj.setSmsContent(smsContent);
        String variable = JSON.toJSONString(model.getVariables());
        obj.setVariables(variable);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }

        //2、保存明细
        List<String> msisds = batchAddItem(id, userModels);
        //3、发送短信
        send(model, smsTemplate.getTemplateId(), msisds, id);
    }

    /**
     * 保存数据项目
     **/
    private List<String> batchAddItem(String taskId, List<UserModel> userModels) {
        SmsTaskItem items = null;
        List<String> msisds = new ArrayList<>();
        for (UserModel userModel : userModels) {
            items = new SmsTaskItem();
            items.setId(UtilHelper.getUUID());
            items.setTaskId(taskId);
            //短信下发
            items.setServiceType(1);
            items.setReceiverId(userModel.getId());
            items.setReceiver(userModel.getName());
            items.setMsisd(userModel.getTelPhone());
            items.setCreateBy(ServletUtil.getCurrentUser());
            items.setCreateTime(DateUtil.getNow());
            //用户类型
            items.setReceiverType(userModel.getUserType());
            //等待回执
            items.setSendStatus(1);
            msisds.add(userModel.getTelPhone());
            smsTaskItemMapper.insert(items);
        }
        return msisds;
    }

    /**
     * 用户导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("用户导入查询模板.xls", "手机号", new String[]{"10010"});
    }

    @Override
    public void smsCallBackJob() {
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");
        params.put("serviceType", 1);
        params.put("status", 0);
        params.put("sendStatus", 1);
        List<SmsTaskItem> items = smsTaskItemMapper.pagerModel(params);

        QuerySendSmsBody querySendSmsBody = null;
        QuerySendSmsResult sendSmsResult = null;

        for (SmsTaskItem item : items) {
            querySendSmsBody = new QuerySendSmsBody();
            querySendSmsBody.setBizId(item.getBizId());
            querySendSmsBody.setPhoneNumber(item.getMsisd());
            querySendSmsBody.setCurrentPage(1);
            querySendSmsBody.setPageSize(10);
            String date = DateUtil.formatTime(DateUtil.parseDate(item.getCreateTime()), "yyyyMMdd");
            querySendSmsBody.setSendDate(date);

            sendSmsResult = smsServiceClient.querySendDetail(querySendSmsBody);
            if (sendSmsResult.getCode() == 200) {
                SmsSendDetail smsSendDetail = sendSmsResult.getSmsSendDetails().get(0);
                //修改发送状态
                params = new HashMap<>();
                params.put("sendTime", smsSendDetail.getSendDate());
                params.put("sendStatus", smsSendDetail.getSendStatus());
                params.put("failMsg", "DELIVERED".equals(smsSendDetail.getErrCode()) ? "" : smsSendDetail.getErrCode());
                params.put("taskId", item.getTaskId());
                params.put("msisd", item.getMsisd());
                params.put("updateBy", ServletUtil.getCurrentUser());
                params.put("updateTime", DateUtil.getNow());
                smsTaskItemMapper.updateSendTimeAndStatus(params);
            }
        }
    }
}
