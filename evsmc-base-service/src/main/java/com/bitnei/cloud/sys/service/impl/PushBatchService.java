package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import com.bitnei.cloud.dc.model.ForwardVehicleModel;
import com.bitnei.cloud.dc.model.PlatformVehiclePushLogModel;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.dc.service.IPlatformVehiclePushLogService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.PushBatch;
import com.bitnei.cloud.sys.domain.PushBatchDetail;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.IPushBatchDetailService;
import com.bitnei.cloud.sys.service.IPushBatchService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushBatchService实现<br>
* 描述： PushBatchService实现<br>
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
* <td>2019-02-27 19:36:18</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.PushBatchMapper" )
public class PushBatchService extends BaseService implements IPushBatchService {

    @Resource
    private IPushBatchDetailService pushBatchDetailService;
    @Resource
    private IForwardVehicleService forwardVehicleService;
    @Resource
    private IPlatformVehiclePushLogService platformVehiclePushLogService;

   @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch", "ppb");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<PushBatch> entries = findBySqlId("pagerModel", params);
            List<PushBatchModel> models = new ArrayList<>();
            for(PushBatch entry: entries){
                models.add(PushBatchModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<PushBatchModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                PushBatch obj = (PushBatch)entry;
                models.add(PushBatchModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public PushBatchModel get(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch", "ppb");
        params.put("id",id);
        PushBatch entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PushBatchModel.fromEntry(entry);
    }


    @Override
    public void insert(PushBatchModel model) {

        PushBatch obj = new PushBatch();
        BeanUtils.copyProperties(model, obj);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(PushBatchModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch", "ppb");
        PushBatch obj = new PushBatch();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }



    @Override
    public void batchInsert(PushModel model) {
        String id = UtilHelper.getUUID();
        PushBatchModel batchModel = new PushBatchModel();
        batchModel.setBatchNum(model.getBatchNum());
        batchModel.setBatchType(model.getBatchType());
        batchModel.setId(id);
        batchModel.setStatus(PushBatch.STATUS_DID);
        batchModel.setRemark(model.getMfrsCode());
        this.insert(batchModel);
        // 推送批次详情
        for (PushDetailModel detailModel : model.getList()) {
            PushBatchDetailModel detail = new PushBatchDetailModel();
            detail.setBatchId(id);
            if(detailModel.getStatus().equals(1)) {
                detail.setPushStatus(PushBatchDetail.PUSH_STATUS_ING);
            } else if(detailModel.getStatus().equals(2)) {
                detail.setPushStatus(PushBatchDetail.PUSH_STATUS_FAIL);
            }
            detail.setFormId(detailModel.getFormId());
            detail.setFormName(detailModel.getFormName());
            detail.setErrorInfo(detailModel.getMessage());
            detail.setForwardVehicleId(detailModel.getId());
            detail.setAuditStatus(PushBatchDetail.AUDIT_STATUS_DID);
            pushBatchDetailService.insert(detail);
            boolean modelBool = model.getBatchType().equals(PushBatch.BATCH_TYPE_VEH_MODEL)
                    && detailModel.getStatus().equals(2);
            if(model.getBatchType().equals(PushBatch.BATCH_TYPE_VEH) || modelBool ) {
                ForwardVehicleModel vehicleModel = new ForwardVehicleModel();
                vehicleModel.setId(detailModel.getId());
                if(detailModel.getStatus().equals(1)) {
                    vehicleModel.setPushStatus(ForwardVehicle.PUSH_STATUS_ING);
                } else {
                    vehicleModel.setPushStatus(ForwardVehicle.PUSH_STATUS_FAIL);
                    vehicleModel.setErrorMessage(detailModel.getMessage());
                }
                forwardVehicleService.changeVehStatus(vehicleModel);
            }
            // 静态数据推送日志
            if(model.getPushStatus() != null) {
                PlatformVehiclePushLogModel logModel = new PlatformVehiclePushLogModel();
                logModel.setVehicleId(model.getVehicleId());
                logModel.setForwardPlatformId(model.getPlatformId());
                logModel.setReqTime(model.getReqTime());
                logModel.setRspTime(model.getRspTime());
                logModel.setPushStatus(model.getPushStatus());
                logModel.setErrorMessage(model.getErrorMessage());
                logModel.setReqBody(model.getReqBody());
                logModel.setDecryptReqBody(model.getDecryptReqBody());
                logModel.setRspBody(model.getRspBody());
                logModel.setDecryptRspBody(model.getDecryptRspBody());
                logModel.setConfirmUser(model.getConfirmBy());
                logModel.setRemark(model.getRemark());
                platformVehiclePushLogService.insert(logModel);
            }

        }
    }

    @Override
    public void batchUpdate(VehFeedbackModel model) {
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch", "ppb");
        params.put("batchNum",model.getBnum());
        PushBatch entry = unique("findByBatchNum", params);
        if(entry == null || entry.getStatus().equals(PushBatch.STATUS_YET)) {
            throw new BusinessException("批次号不存在或已反馈");
        }
        entry.setStatus(PushBatch.STATUS_YET);
        super.update(entry);
        for (VehFeedbackMessageModel vmm : model.getMessages()) {
            PushBatchDetailModel detailModel = pushBatchDetailService.getByBatchIdAndFormName(entry.getId(), vmm.getId());
            if(detailModel == null ) {
                log.debug("上报反馈信息不存在,bnum +"+model.getBnum()+"id="+vmm.getId());
                continue;
            }
            if(vmm.getCode().equals("0")) {
                detailModel.setPushStatus(PushBatchDetail.PUSH_STATUS_SUCCESS);
            } else if(vmm.getCode().equals("1")) {
                detailModel.setPushStatus(PushBatchDetail.PUSH_STATUS_FAIL);
                detailModel.setErrorInfo(vmm.getMessage());
            }
            pushBatchDetailService.update(detailModel);
            if(entry.getBatchType().equals(PushBatch.BATCH_TYPE_VEH)) {
                ForwardVehicleModel vehicleModel = new ForwardVehicleModel();
                vehicleModel.setId(detailModel.getForwardVehicleId());
                if(vmm.getCode().equals("0")) {
                    vehicleModel.setPushStatus(ForwardVehicle.PUSH_STATUS_SUCCESS);
                } else if(vmm.getCode().equals("1")) {
                    vehicleModel.setPushStatus(ForwardVehicle.PUSH_STATUS_FAIL);
                    vehicleModel.setErrorMessage(vmm.getMessage());
                }
                forwardVehicleService.changeVehStatus(vehicleModel);
            }
        }
    }

    @Override
    public Object pushBatchDetails(PagerInfo pagerInfo) {
        return pushBatchDetailService.list(pagerInfo);
    }

    @Override
    public void pushBatchDetailUpdate(VehAuditModel model) {
        PushBatchDetailModel detailModel = pushBatchDetailService.get(model.getId());
        detailModel.setAuditStatus(Integer.parseInt(model.getCode()));
        detailModel.setAuditMessage(model.getMessage());
        pushBatchDetailService.update(detailModel);
    }
}
