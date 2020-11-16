package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.client.VehRiskClient;
import com.bitnei.cloud.fault.domain.IncidentHandling;
import com.bitnei.cloud.fault.model.ResultMsgModel;
import com.bitnei.cloud.fault.model.TruUpAlarmDocModel;
import com.bitnei.cloud.fault.model.TruUpDealModel;
import com.bitnei.cloud.fault.service.IIncidentHandlingService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.fault.domain.SafetyAccidents;
import com.bitnei.cloud.fault.model.SafetyAccidentsModel;
import com.bitnei.cloud.fault.service.ISafetyAccidentsService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.domain.VehicleEngeryDeviceLk;
import com.bitnei.cloud.sys.model.BatteryDeviceModelModel;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.bitnei.cloud.sys.model.VehicleEngeryDeviceLkModel;
import com.bitnei.cloud.sys.service.*;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SafetyAccidentsService实现<br>
* 描述： SafetyAccidentsService实现<br>
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
* <td>2019-07-02 16:50:26</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.SafetyAccidentsMapper" )
public class SafetyAccidentsService extends BaseService implements ISafetyAccidentsService {

    @Resource
    private IVehicleEngeryDeviceLkService vehicleEngeryDeviceLkService;
    @Resource
    private IEngeryDeviceService engeryDeviceService;
    @Resource
    private IVehicleEngeryDeviceLkService engeryDeviceLkService;

    @Resource
    private IVehicleService vehicleService;
    @Resource
    private VehRiskClient vehRiskClient;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private IIncidentHandlingService incidentHandlingService;
    @Resource
    private IBatteryDeviceModelService batteryDeviceModelService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<SafetyAccidents> entries = findBySqlId("pagerModel", params);
            List<SafetyAccidentsModel> models = new ArrayList();
            for(SafetyAccidents entry: entries){
                SafetyAccidents obj = (SafetyAccidents)entry;
                this.getData(obj);
                SafetyAccidentsModel model = SafetyAccidentsModel.fromEntry(obj);
                models.add(model);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<SafetyAccidentsModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                SafetyAccidents obj = (SafetyAccidents)entry;
                this.getData(obj);
                models.add(SafetyAccidentsModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    public void getData(SafetyAccidents entry){

       if (StringUtils.isNotBlank(entry.getVehicleId()) && StringUtils.isNotBlank(entry.getEngeryDeviceIds())){
           VehicleEngeryDeviceLk lk = new VehicleEngeryDeviceLk();
           lk.setVehicleId(entry.getVehicleId());
           lk.setEngeryDeviceId(entry.getEngeryDeviceIds());
           VehicleEngeryDeviceLk vehicleEngeryDeviceLk = vehicleEngeryDeviceLkService.unique("pagerModel", lk);
           if (vehicleEngeryDeviceLk != null){
               VehicleEngeryDeviceLkModel lkModel = VehicleEngeryDeviceLkModel.fromEntry(vehicleEngeryDeviceLk);
               DataLoader.loadNames(lkModel);

               // 可充电储能装置编码
               entry.setEngeryDeviceNames(lkModel.getEngeryDeviceName());
               // 单体生产企业
               entry.setBatteryCellUnit(lkModel.getBatteryCellUnitName());
               // 电池单体型号
               entry.setBatteryCellModels(lkModel.getBatteryCellModel());
               // 可充电储能装置型号
               entry.setBatteryModel(lkModel.getBatteryModelName());
               // 储能装置生产企业
               entry.setEngeryDeviceUnit(lkModel.getUnitName());
           }
       }
    }

    @Override
    public SafetyAccidentsModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");
        params.put("id",id);
        SafetyAccidents entry = unique("findById", params);
        this.getData(entry);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return SafetyAccidentsModel.fromEntry(entry);
    }


    @Override
    public void insert(SafetyAccidentsModel model) {

        Vehicle entry = vehicleService.unique("uniqueFindByVin", model.getVin());
        if (entry == null){
            throw new BusinessException("vin码不存在");
        }
        else{
            model.setVehicleId(entry.getId());
            model.setVid(entry.getUuid());
            SafetyAccidents obj = new SafetyAccidents();
            BeanUtils.copyProperties(model, obj);
            String id = UtilHelper.getUUID();
            String createBy = ServletUtil.getCurrentUser();
            String createTime = DateUtil.getNow();
            obj.setId(id);
            obj.setReportState(1);
            obj.setDataReportState(1);
            obj.setReportUploadState(1);
            obj.setFinalOperator(createBy);
            obj.setOperatingTime(createTime);
            obj.setRelation(model.getRelation() == null ? 0 : model.getRelation());
            obj.setCreateTime(createTime);
            obj.setCreateBy(createBy);
            if (null != obj.getTime() && obj.getTime().length() == 18){
                StringBuffer newTime = new StringBuffer(obj.getTime());
                newTime.insert(10," ");
                obj.setTime(newTime.toString());
            }
            int res = super.insert(obj);
            if (res == 0 ){
                throw new BusinessException("新增失败");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SafetyAccidentsModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");

        Vehicle entry = vehicleService.unique("uniqueFindByVin", model.getVin());
        if (entry == null){
            throw new BusinessException("vin码不存在");
        }
        model.setVehicleId(entry.getId());
        model.setVid(entry.getUuid());
        SafetyAccidents obj = new SafetyAccidents();
        BeanUtils.copyProperties(model, obj);
        String updateTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        obj.setReportState(1);
        obj.setFinalOperator(updateBy);
        obj.setOperatingTime(updateTime);
        obj.setUpdateTime(updateTime);
        obj.setUpdateBy(updateBy);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");
        //TODO 服务端验证一下是否勾选了上报成功的记录
        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<SafetyAccidents>(this, "pagerModel", params, "fault/res/safetyAccidents/export.xls", "安全事故管理") {
            @Override
            public Object process(SafetyAccidents entity) {
                getData(entity);

                return super.process(entity);
            }

        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "SAFETYACCIDENTS"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<SafetyAccidentsModel>(file, messageType, GroupExcelImport.class,0,1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(SafetyAccidentsModel model) {

                List<String> errors = Lists.newArrayList();
                // 验证VIN码是否存在
                if (StringUtils.isNotBlank(model.getVin())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
                    params.put("vin", model.getVin());
                    Vehicle entry = vehicleService.unique("uniqueFindByVin", params);
                    if (entry == null) {
                        errors.add("车辆VIN码不存在");
                    }
                    else {
                        // 用于判断可充电储能装置是否存在的查询条件
                        model.setVehicleId(entry.getId());
                    }
                }
                if (model.getVehicleStateName() != null && !"".equals(model.getVehicleStateName())){
                    // 事故发生时车辆状态
                    Map<String, Object> typeParams = ImmutableMap.of("type", "FAULT_VEHICLE_STATUS", "name", model.getVehicleStateName());
                    Dict dict = dictMapper.getByTypeAndName(typeParams);
                    if(dict != null) {
                        model.setVehicleState(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("事故发生时车辆状态填写错误");
                    }
                }
                String error = extendValidateModel(model);
                if (!"".equals(error)){
                    errors.add(error);
                }
                if (errors.size() > 0) {
                    log.info("batchImport === " + Joiner.on("-").skipNulls().join(errors));
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(SafetyAccidentsModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "SAFETYACCIDENTS"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<SafetyAccidentsModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(SafetyAccidentsModel model) {

                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(SafetyAccidentsModel model) {
                update(model);
            }
        }.work();

    }


    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("导入模板.xls", SafetyAccidentsModel.class);
    }

    private String extendValidateModel(SafetyAccidentsModel model) {

        String error = "";
        String engeryDeviceName = model.getEngeryDeviceNames();
        String vehicleId = model.getVehicleId();
        try {
            EngeryDeviceModel engeryDeviceModel = engeryDeviceService.findByName(engeryDeviceName);
            model.setEngeryDeviceIds(engeryDeviceModel.getId());
        }catch (BusinessException e){
            error = e.getMessage();
        }
        if (StringUtil.isNotBlank(model.getEngeryDeviceIds())){
            VehicleEngeryDeviceLk lk = new VehicleEngeryDeviceLk();
            // 可充电储能装置id
            lk.setEngeryDeviceId(model.getEngeryDeviceIds());
            lk.setVehicleId(vehicleId);
            VehicleEngeryDeviceLk en = engeryDeviceLkService.unique("findByDeviceIdAndVehicleId",lk);
            if (en == null){
                error = "车辆没有关联此可充电储能装置";
            }
        }
        return error;
    }

    @Override
    public String truUpDeal(SafetyAccidentsModel data){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");

        String[] arr = data.getId().split(",");
        String updateTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        // 当上报平台为国家平台时
        if (data.getPlatform() == 1){
            // ids 用于存储需上报车型预案的id
            Set<String> ids = Sets.newHashSet();
            for (String id : arr){
                SafetyAccidentsModel model = this.get(id);
                DataLoader.loadNames(model);
                TruUpDealModel m = new TruUpDealModel();
                m.setVehModel(model.getVehModelName());
                m.setBatteryModel(model.getBatteryModel());
                m.setBatteryUnit(model.getEngeryDeviceUnit());
                m.setBatteryMonModel(model.getBatteryCellModels());
                m.setBatteryMonUnit(model.getBatteryCellUnit());
                m.setUseCity(model.getOperAreaName());
                m.setUseUnitName(model.getOperUnitName());
                m.setMileage(model.getVehicleTotalMileage());
                m.setVehState(String.valueOf(model.getVehicleState()));
                m.setFactoryDate(model.getProduceDate());
                m.setSaleDate(model.getSellDate());
                m.setErrorMessage(model.getFaultSituation());
                m.setTroToken(model.getRepresentation());
                m.setTroEffect(model.getAccident());
                m.setTroReason(model.getReason());
                m.setPhoto1(model.getPhoto1Id());
                m.setPhoto2(model.getPhoto2Id());
                m.setPhoto3(model.getPhoto3Id());
                m.setPhoto4(model.getPhoto4Id());
                ResultMsgModel resultMsgModel =  vehRiskClient.truUpDeal(m);
                SafetyAccidentsModel updateModel = new SafetyAccidentsModel();
                if (resultMsgModel.getData() != null){
                    updateModel.setReportNumber(resultMsgModel.getData().toString());
                }
                updateModel.setPlatform(1);
                updateModel.setFinalOperator(updateBy);
                updateModel.setOperatingTime(updateTime);
                updateModel.setUpdateTime(updateTime);
                updateModel.setUpdateBy(updateBy);
                updateModel.setId(id);
                if (resultMsgModel.getCode() == 0){
                    updateModel.setReportState(4);
                    updateModel.setReasonsForFailure(null);
                }
                else {
                    updateModel.setReportState(3);
                    updateModel.setReasonsForFailure(resultMsgModel.getMsg());
                }
                params.putAll(MapperUtil.Object2Map(updateModel));
                super.update("updateUnit",params);

                // 根据是否关联车型，上报车型事故处置预案
                if (model.getRelation() != null && model.getRelation() == 1){
                    IncidentHandling incidentHandling = incidentHandlingService.unique("findByVehModelId",model.getVehModelId());
                    if (incidentHandling != null){
                        // 目前上报车型事故处置预案默认上报到国家平台
                        ids.add(incidentHandling.getId());
                    }
                }
            }
            if (ids.size()>0){
                String reportIds = String.join(",", ids);
                Thread t1 = new Thread(() -> {
                    incidentHandlingService.truUpAlarm(reportIds ,"1");
                });
                t1.start();
            }
            return "事故已上报至国家平台";
        }
        else {
            return "目前请选择上报平台为国家平台";
        }
    }

    @Override
    public String truUpAlarmDocs(SafetyAccidentsModel data){

        // 判断是否重复上报或上传
        SafetyAccidentsModel model = this.get(data.getId());
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_safety_accidents", "fsa");
        String updateTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        data.setOperatingTime(updateTime);
        data.setUpdateBy(updateBy);
        data.setUpdateTime(updateTime);
        data.setFinalOperator(updateBy);
        TruUpAlarmDocModel truUpAlarmDocModel = new TruUpAlarmDocModel();
        truUpAlarmDocModel.setTroCode(data.getReportNumber());
        if (data.getSuperviseDataFileId() != null && !"".equals(data.getSuperviseDataFileId()) && model.getDataReportState() != 2){
            truUpAlarmDocModel.setRealState(data.getSuperviseDataFileId());
            ResultMsgModel resultMsgModel = vehRiskClient.truUpAlarmDocs(truUpAlarmDocModel);
            if (resultMsgModel.getCode() == 0){
                // 成功
                data.setDataReportState(2);
            }
            else {
                data.setDataReportState(3);
            }
            params.putAll(MapperUtil.Object2Map(data));
            int res = update("updateByDataReportState",params);
            if (res == 0){
                throw new BusinessException("本地更新状态时发生异常");
            }
            return "成功上报监管数据";

        }
        else if (data.getAnalysisReportFileId() != null && !"".equals(data.getAnalysisReportFileId()) && model.getReportUploadState() != 2){
            truUpAlarmDocModel.setAnalysisName(data.getAnalysisReportFileId());
            ResultMsgModel resultMsgModel = vehRiskClient.truUpAlarmDocs(truUpAlarmDocModel);
            if (resultMsgModel.getCode() == 0){
                 // 成功
                data.setReportUploadState(2);
            }
            else {
                data.setReportUploadState(3);
            }
            params.putAll(MapperUtil.Object2Map(data));
            int res = update("updateByReportUploadState",params);
            if (res == 0){
                throw new BusinessException("本地更新状态时发生异常");
            }
            return "成功上传分析报告";
        }
        else {
            return "请确认是否已经选择附件,并且请勿重复上报已成功上报的记录";
        }
    }
}
