package com.bitnei.cloud.sms.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.*;
import com.bitnei.cloud.common.client.SmsServiceClient;
import com.bitnei.cloud.common.client.UniComSmsServiceClient;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sms.dao.SmsTaskItemMapper;
import com.bitnei.cloud.sms.dao.SmsTaskMapper;
import com.bitnei.cloud.sms.domain.SmsTask;
import com.bitnei.cloud.sms.domain.SmsTaskItem;
import com.bitnei.cloud.sms.domain.TermSmsTaskExport;
import com.bitnei.cloud.sms.domain.VehicleMsisd;
import com.bitnei.cloud.sms.model.SmsTemplateModel;
import com.bitnei.cloud.sms.model.TermSmsTaskModel;
import com.bitnei.cloud.sms.model.UniComErrorCode;
import com.bitnei.cloud.sms.service.ISmsTaskService;
import com.bitnei.cloud.sms.service.ITermSmsTaskService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
public class TermSmsTaskService extends BaseService implements ITermSmsTaskService {

    @Resource
    private SmsTaskItemMapper smsTaskItemMapper;

    @Resource
    private SmsServiceClient smsServiceClient;

    @Resource
    private SmsTaskMapper smsTaskMapper;

    @Autowired
    private ISmsTaskService smsTaskService;

    @Autowired
    private IVehicleService vehicleService;

    @Value("${use.aliyun.term.sms:false}")
    private boolean useAliyunTermSms;

    @Resource
    private UniComSmsServiceClient uniComSmsServiceClient;

    @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("serviceType", 2);
        PagerResult pr = findPagerModel("termSmsTaskPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
        List<TermSmsTaskModel> models = new ArrayList();
        for (Object entry : pr.getData()) {
            SmsTask obj = (SmsTask) entry;
            models.add(TermSmsTaskModel.fromEntry(obj));
        }
        pr.setData(Collections.singletonList(models));
        return pr;
    }

    @Override
    public TermSmsTaskModel get(String id) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.put("id", id);
        SmsTask entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        TermSmsTaskModel model = TermSmsTaskModel.fromEntry(entry);
        //草稿
        if (entry.getStatus().intValue() == 1) {
            if (entry.getAddAll() != null && entry.getAddAll().intValue() == 1) { //添加全部
                model.setTotal(smsTaskItemMapper.totalByTaskId(id));
            } else {
                params = new HashMap<>();
                params.put("taskId", id);
                model.setItems(smsTaskItemMapper.listSmsTaskItemByTaskId(params));
            }

        } else {
            Map<String, BigDecimal> successMap = smsTaskItemMapper.successTotal(id);
            String successTotal = ((successMap != null && !successMap.isEmpty()) ? String.valueOf(successMap.get("successTotal")) : "0");
            String total = ((successMap != null && !successMap.isEmpty()) ? String.valueOf(successMap.get("total")) : "0");
            model.setStatistic(successTotal + "/" + total);
        }
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(TermSmsTaskModel model) {
        if (StringUtils.isBlank(model.getVehicleIds())) {
            throw new BusinessException("请选择车辆...");
        }
        //2、终端短信唤醒
        model.setServiceType(2);

        //1、保存task
        SmsTask obj = new SmsTask();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());

        //2、查询模板
        SmsTemplateModel smsTemplate = smsTaskService.getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }
        obj.setSmsContent(smsTemplate.getContent());

        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        model.setId(id);

        //3、保存明细
        List<String> phoneNumber = addItem(model);

        //4、发送短信
        if (useAliyunTermSms && model.getStatus().intValue() == 0) {
            send(phoneNumber, id, true, smsTemplate.getTemplateId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TermSmsTaskModel model) {
        if (StringUtils.isBlank(model.getVehicleIds())) {
            throw new BusinessException("请选择车辆...");
        }


        //1、删除原来的
        smsTaskItemMapper.deleteByTaskId(model.getId());

        //2、修改task
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        SmsTask obj = new SmsTask();
        obj.setId(model.getId());
        obj.setStatus(model.getStatus());
        obj.setRemarks(model.getRemarks());
        //终端短信唤醒
        obj.setServiceType(2);

        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        //3、查询模板
        SmsTemplateModel smsTemplate = smsTaskService.getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }

        //4、保存明细
        List<String> phoneNumber = addItem(model);

        //5、发送短信
        if (useAliyunTermSms && model.getStatus().intValue() == 0) {
            send(phoneNumber, model.getId(), true, smsTemplate.getTemplateId());
        }
    }

    private List<String> addItem(TermSmsTaskModel model) {
        //3、保存明细
        SmsTaskItem items = null;
        String[] vehicleIds = model.getVehicleIds().split(",");
        List<String> phoneNumber = new ArrayList<>();
        //查询出车辆 终端的 msisd
        List<VehicleMsisd> vehicleMsisds = smsTaskItemMapper.listVehicleMsisd(vehicleIds);
        for (VehicleMsisd vehicleMsisd : vehicleMsisds) {
            items = new SmsTaskItem();
            items.setId(UtilHelper.getUUID());
            items.setTaskId(model.getId());
            items.setServiceType(model.getServiceType());
            items.setVehicleId(vehicleMsisd.getVehicleId());
            items.setVin(vehicleMsisd.getVin());
            items.setMsisd(vehicleMsisd.getMsisd());
            items.setIccid(vehicleMsisd.getIccid());
            items.setCreateBy(ServletUtil.getCurrentUser());
            items.setCreateTime(DateUtil.getNow());
            if (!useAliyunTermSms) {
                //等待定时任务发送
                items.setSendStatus(4);
            }
            smsTaskItemMapper.insert(items);

            if (StringUtils.isNotBlank(vehicleMsisd.getMsisd())) {
                phoneNumber.add(vehicleMsisd.getMsisd());
            }
        }
        return phoneNumber;
    }

    private void send(List<String> phoneNumber, String taskId, boolean flag, String tempLateCode) {
        String phones = Joiner.on(",").join(phoneNumber);
        SmsMessageBody smsMessageBody = new SmsMessageBody();
        smsMessageBody.setPhoneNumber(phones);

        //阿里云短信模板code
        smsMessageBody.setTemplateCode(tempLateCode);

        SendSmsResult result = smsServiceClient.sendSpreadSms(smsMessageBody);

        if (result.getCode() == 500) {
            log.error("=============终端指令短信发送失败===============");
            throw new BusinessException("终端指令短信发送失败: " + result.getMessage());
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

        //5、发送状态查询
        if (flag) {
            smsTaskService.processCallBack(bizId, phoneNumber, taskId);
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
        return smsTaskService.deleteMulti(ids);
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("serviceType", 2);
        new ExcelExportHandler<TermSmsTaskModel>(this, "termSmsTaskPagerModel", params, "sms/res/termSmsTask/export.xls", "终端短信指令") {
        }.work();
        return;
    }

    @Override
    public void exportDetails(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("serviceType", 2);
        new ExcelExportHandler<TermSmsTaskExport>(this, "termSmsTaskExport", params, "sms/res/termSmsTask/exportDetails.xls", "终端短信指令详情导出") {
        }.work();
        return;
    }

    @Override
    public void batchInsert(TermSmsTaskModel model) {
        Map<String, String> condition = new HashMap<>();
        if (StringUtils.isNotBlank(model.getQueryMsisd())) {
            condition.put("msisd", model.getQueryMsisd());
        }
        if (StringUtils.isNotBlank(model.getQueryOperUnitId())) {
            condition.put("operUnitId", model.getQueryOperUnitId());
        }
        if (StringUtils.isNotBlank(model.getQueryVehModelId())) {
            condition.put("vehModelId", model.getQueryVehModelId());
        }
        if (StringUtils.isNotBlank(model.getQueryVin())) {
            condition.put("vin", model.getQueryVin());
        }

        //离线车辆并且sim卡不为空的车辆
        condition.put("onlineStatus", "2");
        condition.put("filterMsisd", "0");

        PagerInfo pagerInfo = ServletUtil.mapToPagerInfo(condition);
        List<VehicleModel> vehicleModels = (List<VehicleModel>) vehicleService.vehicleList(pagerInfo);
        if (CollectionUtils.isEmpty(vehicleModels)) {
            throw new BusinessException("请选择车辆..");
        }

        String taskIkd;

        //2、查询模板
        SmsTemplateModel smsTemplate = smsTaskService.getSmsTemplate(model.getTemplateId());
        if (smsTemplate == null) {
            throw new BusinessException("短信模板不存在");
        }

        if (StringUtils.isBlank(model.getId())) {
            //1、保存task
            SmsTask obj = new SmsTask();
            BeanUtils.copyProperties(model, obj);
            String id = UtilHelper.getUUID();
            obj.setId(id);
            obj.setCreateBy(ServletUtil.getCurrentUser());
            obj.setCreateTime(DateUtil.getNow());

            obj.setSmsContent(smsTemplate.getContent());

            int res = super.insert(obj);
            if (res == 0) {
                throw new BusinessException("新增失败");
            }
            taskIkd = id;
        } else {

            //1、删除原来的
            smsTaskItemMapper.deleteByTaskId(model.getId());

            //2、修改task
            Map<String, Object> params = DataAccessKit.getAuthMap("sms_task", "gst");
            SmsTask obj = new SmsTask();
            obj.setId(model.getId());
            obj.setStatus(model.getStatus());
            obj.setRemarks(model.getRemarks());

            obj.setUpdateBy(ServletUtil.getCurrentUser());
            obj.setUpdateTime(DateUtil.getNow());
            params.putAll(MapperUtil.Object2Map(obj));
            int res = super.updateByMap(params);
            if (res == 0) {
                throw new BusinessException("更新失败");
            }
            taskIkd = model.getId();
        }

        //3、保存明细
        SmsTaskItem items = null;
        List<String> phoneNumber = new ArrayList<>();
        //查询出车辆 终端的 msisd
        for (VehicleModel vehicleModel : vehicleModels) {
            items = new SmsTaskItem();
            items.setId(UtilHelper.getUUID());
            items.setTaskId(taskIkd);
            items.setServiceType(model.getServiceType());
            items.setVehicleId(vehicleModel.getId());
            items.setVin(vehicleModel.getVin());
            items.setMsisd(vehicleModel.getMsisd());
            items.setIccid(vehicleModel.getIccid());
            items.setCreateBy(ServletUtil.getCurrentUser());
            items.setCreateTime(DateUtil.getNow());
            if (!useAliyunTermSms) {
                //等待定时任务发送
                items.setSendStatus(4);
            }
            smsTaskItemMapper.insert(items);

            if (StringUtils.isNotBlank(vehicleModel.getMsisd())) {
                phoneNumber.add(vehicleModel.getMsisd());
            }
        }

        //4、发送短信
        if (useAliyunTermSms && model.getStatus().intValue() == 0) {
            send(phoneNumber, model.getId(), false, smsTemplate.getTemplateId());
        }
    }

    @Override
    public void processAddAllMsgCallBack() {
        Map<String, Object> params = new HashMap<>();
        /**等待回执**/
        params.put("sendStatus", 1);
        /**添加全部查询结果**/
        params.put("addAll", 1);
        /**终端指令下发**/
        params.put("serviceType", 2);
        /**不是联通短信**/
        params.put("filterIsNullIccId", "filterIsNullIccId");
        List<SmsTaskItem> smsTaskItems = smsTaskItemMapper.pagerModel(params);

        QuerySendSmsBody querySendSmsBody = null;
        QuerySendSmsResult sendSmsResult = null;
        Map<String, Object> condit = null;
        for (SmsTaskItem taskItem : smsTaskItems) {
            if (useAliyunTermSms && StringUtils.isBlank(taskItem.getIccid())) {
                querySendSmsBody = new QuerySendSmsBody();
                querySendSmsBody.setBizId(taskItem.getBizId());
                querySendSmsBody.setPhoneNumber(taskItem.getMsisd());
                querySendSmsBody.setCurrentPage(1);
                querySendSmsBody.setPageSize(10);
                String date = DateUtil.formatTime(new Date(), "yyyyMMdd");
                querySendSmsBody.setSendDate(date);

                sendSmsResult = smsServiceClient.querySendDetail(querySendSmsBody);
            } else {
                sendSmsResult = uniComSmsServiceClient.querySendDetail(taskItem.getBizId());
            }

            if (sendSmsResult.getCode() == 200) {
                SmsSendDetail smsSendDetail = sendSmsResult.getSmsSendDetails().get(0);
                //修改发送状态
                condit = new HashMap<>();
                condit.put("sendTime", smsSendDetail.getSendDate());
                condit.put("sendStatus", smsSendDetail.getSendStatus());
                condit.put("failMsg", smsSendDetail.getErrCode());
                condit.put("taskId", taskItem.getTaskId());
                condit.put("msisd", taskItem.getMsisd());
                condit.put("updateBy", ServletUtil.getCurrentUser());
                condit.put("updateTime", DateUtil.getNow());
                if (smsSendDetail.getSendStatus().intValue() != 1) {
                    smsTaskItemMapper.updateSendTimeAndStatus(condit);
                }
            }
        }
    }

    @Override
    public void sendUniComSms() {
        Map<String, Object> params = new HashMap<>();
        /**等待发送**/
        params.put("sendStatus", 4);
        /**iccid不为空 联通短信**/
        params.put("filterIsNotNullIccId", "filterIsNotNullIccId");
        /**终端指令下发**/
        params.put("serviceType", 2);
        List<SmsTaskItem> smsTaskItems = smsTaskItemMapper.pagerModel(params);
        int i = 0;
        for (SmsTaskItem taskItem : smsTaskItems) {
            UniComSmsBody uniComSmsBody = new UniComSmsBody();
            uniComSmsBody.setIccid(taskItem.getIccid());

            SmsTemplateModel smsTemplate = smsTaskService.getSmsTemplate(taskItem.getTemplateId());
            if (smsTemplate == null) {
                log.error("终端短信模板不存在...");
                continue;
            }
            //短信指令
            uniComSmsBody.setContent(smsTemplate.getContent());
            if (i % 4 == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            params = new HashMap<>();
            SendSmsResult sendSmsResult = uniComSmsServiceClient.sendSms(uniComSmsBody);
            if (sendSmsResult.getCode() == 200) {
                params.put("id", taskItem.getId());
                params.put("bizId", sendSmsResult.getBizId());
                params.put("updateBy", ServletUtil.getCurrentUser());
                params.put("updateTime", DateUtil.getNow());
                params.put("sendStatus", 1);
                smsTaskItemMapper.updateUniComBizId(params);
            } else {
                //发送失败
                params.put("taskId", taskItem.getTaskId());
                params.put("msisd", taskItem.getMsisd());
                params.put("sendTime", DateUtil.getNow());
                params.put("sendStatus", 2);
                params.put("failMsg", UniComErrorCode.getRuleEnum(sendSmsResult.getMessage()));
                params.put("updateBy", ServletUtil.getCurrentUser());
                params.put("updateTime", DateUtil.getNow());
                smsTaskItemMapper.updateSendTimeAndStatus(params);
            }
        }
    }

    @Override
    public void processUniComSmsCallBack() {
        Map<String, Object> params = new HashMap<>();
        /**等待回执**/
        params.put("sendStatus", 1);
        /**添加全部查询结果**/
        params.put("filterIsNotNullIccId", "filterIsNullIccId");
        /**终端指令下发**/
        params.put("serviceType", 2);
        List<SmsTaskItem> smsTaskItems = smsTaskItemMapper.pagerModel(params);

        Map<String, Object> condit = null;
        int i = 0;
        for (SmsTaskItem taskItem : smsTaskItems) {
            if (i % 4 == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            QuerySendSmsResult sendSmsResult = uniComSmsServiceClient.querySendDetail(taskItem.getBizId());
            if (sendSmsResult.getCode() == 200) {
                SmsSendDetail smsSendDetail = sendSmsResult.getSmsSendDetails().get(0);
                //修改发送状态
                condit = new HashMap<>();
                condit.put("taskId", taskItem.getTaskId());
                condit.put("msisd", taskItem.getMsisd());
                condit.put("sendTime", smsSendDetail.getSendDate());
                condit.put("sendStatus", smsSendDetail.getSendStatus());
                condit.put("failMsg", smsSendDetail.getErrCode());
                condit.put("updateBy", ServletUtil.getCurrentUser());
                condit.put("updateTime", DateUtil.getNow());
                if (smsSendDetail.getSendStatus().intValue() != 1) {
                    smsTaskItemMapper.updateSendTimeAndStatus(condit);
                }
            }
        }
    }
}
