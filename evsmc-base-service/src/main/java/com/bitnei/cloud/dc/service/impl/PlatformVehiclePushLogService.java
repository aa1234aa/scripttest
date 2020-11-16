package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import com.bitnei.cloud.dc.domain.PlatformVehiclePushLog;
import com.bitnei.cloud.dc.model.ForwardVehicleModel;
import com.bitnei.cloud.dc.model.PlatformVehiclePushLogModel;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.dc.service.IPlatformVehiclePushLogService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.model.EcoRespModel;
import com.bitnei.cloud.sys.model.FilingRecordModel;
import com.bitnei.cloud.sys.service.IEncryptionChipModelService;
import com.bitnei.cloud.sys.service.IFilingRecordService;
import com.bitnei.cloud.sys.util.StringUtils;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： PlatformVehiclePushLogService实现<br>
 * 描述： PlatformVehiclePushLogService实现<br>
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
 * <td>2019-02-19 14:19:06</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.PlatformVehiclePushLogMapper")
public class PlatformVehiclePushLogService extends BaseService implements IPlatformVehiclePushLogService {

    @Resource
    private IForwardVehicleService forwardVehicleService;
    @Resource
    private IFilingRecordService filingRecordService;
    @Resource
    private IEncryptionChipModelService encryptionChipModelService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_platform_vehicle_push_log", "log");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<PlatformVehiclePushLog> entries = findBySqlId("pagerModel", params);
            List<PlatformVehiclePushLogModel> models = new ArrayList<>();
            for (PlatformVehiclePushLog entry : entries) {
                models.add(PlatformVehiclePushLogModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PlatformVehiclePushLogModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                PlatformVehiclePushLog obj = (PlatformVehiclePushLog) entry;
                models.add(PlatformVehiclePushLogModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public PlatformVehiclePushLogModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_platform_vehicle_push_log", "log");
        params.put("id", id);
        PlatformVehiclePushLog entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return PlatformVehiclePushLogModel.fromEntry(entry);
    }

    @Override
    public void insert(PlatformVehiclePushLogModel model) {
        PlatformVehiclePushLog obj = new PlatformVehiclePushLog();
        BeanUtils.copyProperties(model, obj);
        obj.setCreateTime(DateUtil.getNow());
        obj.setId(UtilHelper.getUUID());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void add(EcoRespModel model) {
        // 修改转发平台状态
        int pushStatus, fromStatus;
        ForwardVehicleModel fvm = new ForwardVehicleModel();
        fvm.setId(model.getForwardVehicleId());
        fvm.setErrorMessage(model.getMessage());

        if(Constant.SUCCESS.equals(model.getType())) {
            pushStatus = 3;
            fromStatus = Constant.TrueAndFalse.TRUE;
            if (model.getRespType() == Constant.RESP_TYPE_VEHICLE) {
                fvm.setPushStatus(ForwardVehicle.PUSH_STATUS_SUCCESS);
                forwardVehicleService.changeVehStatus(fvm);
            }
        } else if(Constant.FAIL.equals(model.getType())) {
            pushStatus = 4;
            fromStatus = Constant.TrueAndFalse.FALSE;
            fvm.setPushStatus(ForwardVehicle.PUSH_STATUS_FAIL);
            forwardVehicleService.changeVehStatus(fvm);
        } else {
            throw new BusinessException("参数异常");
        }
        // 静态数据推送日志
        if(StringUtils.isNotBlank(model.getPlatformId())) {
            PlatformVehiclePushLogModel logModel = new PlatformVehiclePushLogModel();
            logModel.setVehicleId(model.getVehicleId());
            logModel.setForwardPlatformId(model.getPlatformId());
            logModel.setReqTime(model.getReqTime());
            logModel.setRspTime(model.getResponseTime());
            logModel.setPushStatus(pushStatus);
            logModel.setErrorMessage(model.getMessage());
            logModel.setReqBody(model.getReqBody());
            logModel.setRspBody(model.getRspBody());
            logModel.setConfirmUser(model.getConfirmBy());
            logModel.setRemark(model.getRemark());
            this.insert(logModel);

            // 国六防篡改备案上报记录
            FilingRecordModel recordModel = new FilingRecordModel();
            recordModel.setFromId(model.getFromId());
            recordModel.setFromStatus(fromStatus);
            recordModel.setFromType(model.getRespType());
            filingRecordService.insert(recordModel);
        }

        // 更新储存芯片型号上报成功后返回的国家备案编码
//        if(Constant.SUCCESS.equals(model.getType()) && model.getRespType() == Constant.RESP_TYPE_CHIP) {
//            if(model.getData() != null && model.getData().containsKey("chipPrefix")) {
//                String filingCode = model.getData().get("chipPrefix");
//                EncryptionChipModelModel chipModel = encryptionChipModelService.get(model.getFromId());
//                if(chipModel != null) {
//                    chipModel.setFilingCode(filingCode);
//                    encryptionChipModelService.update(chipModel);
//                }
//            }
//        }


    }

    @Override
    public void update(PlatformVehiclePushLogModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_platform_vehicle_push_log", "log");
        PlatformVehiclePushLog obj = new PlatformVehiclePushLog();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_platform_vehicle_push_log", "log");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_platform_vehicle_push_log", "log");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<PlatformVehiclePushLog>(this, "pagerModel", params, "dc/res/platformVehiclePushLog/export.xls", "静态数据推送日志") {
        }.work();

        return;
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "PLATFORMVEHICLEPUSHLOG" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<PlatformVehiclePushLogModel>(file, messageType, GroupExcelImport.class) {
            @Override
            public List<String> extendValidate(PlatformVehiclePushLogModel model) {
                return null;
            }

            @Override
            public void saveObject(PlatformVehiclePushLogModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {

        String messageType = "PLATFORMVEHICLEPUSHLOG" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<PlatformVehiclePushLogModel>(file, messageType, GroupExcelUpdate.class) {

            @Override
            public List<String> extendValidate(PlatformVehiclePushLogModel model) {
                return null;
            }

            @Override
            public void saveObject(PlatformVehiclePushLogModel model) {
                update(model);
            }
        }.work();

    }


}
