package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.dao.VehicleSubsidyMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.commons.util.MapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleSubsidyService实现<br>
 * 描述： VehicleSubsidyService实现<br>
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
 * <td>2018-12-12 17:40:52</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleSubsidyMapper" )
@RequiredArgsConstructor
public class VehicleSubsidyService extends BaseService implements IVehicleSubsidyService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Resource
    private IVehicleEngeryDeviceLkService vehicleEngeryDeviceLkService;
    @Resource
    private IVehicleDriveDeviceLkService vehicleDriveDeviceLkService;
    @Resource
    private IVehiclePowerDeviceLkService vehiclePowerDeviceLkService;
    @Resource
    private VehicleSubsidyMapper vehicleSubsidyMapper;
    @Resource
    private IVehModelService vehModelService;
    @Resource
    private IOfflineExportService offlineExportService;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private IVehicleService vehicleService;

    /**
     *
     * 车辆补贴信息查询
     * @param pagerInfo 分页对象
     * @return obj
     */
    @Override
    public Object subsidyList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<Vehicle> entries = findBySqlId("findPager", params);
            List<VehicleSubsidyModel> models = new ArrayList<>();
            for(Vehicle entry: entries){
                models.add(VehicleSubsidyModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findPager", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehicleSubsidyModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                Vehicle obj = (Vehicle)entry;
                models.add(VehicleSubsidyModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据id获取
     * @return model
     */
    @Override
    public VehicleSubsidyModel get(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.put("id",id);
        Vehicle entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        List<VehicleEngeryDeviceLkModel> engertyList = vehicleEngeryDeviceLkService.listByVehicleId(id);
        List<VehicleDriveDeviceLkModel> driveList = vehicleDriveDeviceLkService.listByVehicleId(id, 1);
        List<VehiclePowerDeviceLkModel> powerList = vehiclePowerDeviceLkService.listByVehicleId(id, 2);
        return VehicleSubsidyModel.fromEntry(entry,engertyList, driveList, powerList);
    }

    /**
     * 编辑车辆补贴
     * @param model model
     */
    @Override
    public void update(VehicleSubsidyModel model) {

        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        // 申报次数默认值=0
        if(model.getSusidyApplyCount() == null) {
            model.setSusidyApplyCount(0);
        }
        Vehicle obj = new Vehicle();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }


    private static String join(String srcStr, Object appendStr) {
        if(appendStr != null) {
            if(StringUtils.isNotBlank(srcStr)) {
                return srcStr + "/" + appendStr.toString();
            } else {
                return appendStr.toString();
            }
        }
        return srcStr;
    }
    /**
     * 车辆补贴信息导出
     * @param pagerInfo 分页对象
     */
    @Override
    public void subsidyExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        long startTime = System.currentTimeMillis();
        log.info("startTime>>>>>>>>>>>>> " + startTime);
        List<VehicleSubsidyExcelModel> entries = findBySqlId("findVehSubsidyPager", params);
        DataLoader.loadNames(entries);
        long t1 = System.currentTimeMillis();
        log.info("t1>>>>>>>>>>>>> " + t1);
        List<ExcelData> excelDataList = new ArrayList<>();
        ExcelData excelData = new ExcelData();
        excelData.setListRowIndex(2);

        //查询装置、车型信息
        Map<String, VehicleEngeryDeviceLkModel> edMap = new HashMap<>();
        Map<String, VehicleDriveDeviceLkModel> ddMap = new HashMap<>();
        Map<String, VehiclePowerDeviceLkModel> pdMap = new HashMap<>();
        List<VehicleEngeryDeviceLkModel> edList = vehicleEngeryDeviceLkService.findEngeryListByVehParams(params);
        if(edList != null && !edList.isEmpty()) {
            for (VehicleEngeryDeviceLkModel lk : edList) {
                edMap.put(lk.getVehicleId(), lk);
            }
        }
        List<VehicleDriveDeviceLkModel> ddList = vehicleDriveDeviceLkService.findDriveListByVehParams(params);
        if(ddList != null && !ddList.isEmpty()) {
            for (VehicleDriveDeviceLkModel lk : ddList) {
                ddMap.put(lk.getVehicleId(), lk);
            }
        }
        List<VehiclePowerDeviceLkModel> pdList = vehiclePowerDeviceLkService.findPowerListByVehParams(params);
        if(ddList != null && !pdList.isEmpty()) {
            for (VehiclePowerDeviceLkModel lk : pdList) {
                pdMap.put(lk.getVehicleId(), lk);
            }
        }
        long t2 = System.currentTimeMillis();
        log.info("t2>>>>>>>>>>>>> " + t2);
        for (VehicleSubsidyExcelModel model : entries) {
            if (!edMap.isEmpty() && edMap.get(model.getVehicleId()) != null){
                VehicleEngeryDeviceLkModel ed = edMap.get(model.getVehicleId());
                model.setBatteryCellModel(ed.getBatteryCellModel());
                model.setModelType(ed.getModelTypeDisplay());
                model.setBatteryCellUnitName(ed.getBatteryCellUnitName());
                model.setModelName(ed.getCapacityModelName());
                model.setTotalElecCapacity(ed.getTotalCapacityStr());
                model.setCapacityDensity(ed.getCapacityDensityStr());
                model.setUnitName(ed.getUnitName());
                model.setBatteryTypeName(ed.getBatteryTypeName());
                model.setInvoiceNo(ed.getInvoiceNo());
            }
            if(!ddMap.isEmpty() && ddMap.get(model.getVehicleId()) != null) {
                VehicleDriveDeviceLkModel dd = ddMap.get(model.getVehicleId());
                model.setDriveMotorModelName(dd.getDriveMotorModelName());
                model.setDriveInvoiceNo(dd.getInvoiceNo());
                model.setProdUnitName(dd.getProdUnitName());
                model.setRatedPower(dd.getRatedPowerStr());
            }
            if(!pdMap.isEmpty() && pdMap.get(model.getVehicleId()) != null) {
                VehiclePowerDeviceLkModel pd = pdMap.get(model.getVehicleId());
                model.setFuelModelName(pd.getFuelBatteryModelName());
                model.setFuelInvoiceNo(pd.getInvoiceNo());
                model.setFuelProdUnitName(pd.getProdUnitName());
                model.setFuelRatedPower(pd.getRatedPowerStr());
            }
            if (StringUtils.isNotBlank(model.getEfficiencyJson())) {
                ObjectMapper om = new ObjectMapper();
                try {
                    VehModelEfficiencyModel em = om.readValue(model.getEfficiencyJson(), VehModelEfficiencyModel.class);
                    model.setChargingRate(em.getFastChargeRatio()==null?null:em.getFastChargeRatio().toString());
                } catch (Exception e2) {
                    log.error("error", e2);
                }
            }
            excelData.getData().add(model);
        }
        long t3 = System.currentTimeMillis();
        log.info("t3>>>>>>>>>>>>> " + t3);
        if(CollectionUtils.isEmpty(excelData.getData())) {
            excelData.getData().add(new VehicleSubsidyExcelModel());
        }
        excelDataList.add(excelData);
        try {
            String templatePath = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
            String templateFile = templatePath + "sys/res/VehicleSubsidy/exportTemp.xlsx";
            String date = DateUtil.formatTime(new Date(), "yyyyMMddHHmmss");
            String outName = String.format("车辆补贴信息列表-导出-%s.xlsx", date);
            File file = EasyExcel.createSheetFile(excelDataList, templateFile, outName);
            EasyExcel.createXlsResponse(file, outName,"123456", false);
        } catch (Exception ex) {
            log.error("车辆补贴信息导出异常：", ex);
            throw new BusinessException("车辆补贴信息导出失败");
        }

//        ExcelExportHandler<Vehicle> handler = new ExcelExportHandler<Vehicle>(this, "findPager", params, "sys/res/VehicleSubsidy/export.xls", "车辆补贴信息列表", VehicleSubsidyModel.class) {
//        };
//        handler.setListRowIndex(2);
//        handler.work();
    }

    private List<String> extendValidateModel(VehicleSubsidyModel model){
        List<String> errors = Lists.newArrayList();
        // VIN
        try {
            VehicleModel vehicle = vehicleService.getByVin(model.getVin());
            model.setId(vehicle.getId());
        } catch (BusinessException ex) {
            errors.add("VIN码不存在");
        }
        // 申报状态
        if (StringUtils.isNotBlank(model.getSubsidyApplyStatusName())) {
            Map<String, Object> p = ImmutableMap.of("type", "SUBSIDY_APPLY_STATUS", "name", model.getSubsidyApplyStatusName());
            Dict dict = dictMapper.getByTypeAndName(p);
            if (dict != null && StringUtils.isNumeric(dict.getValue())) {
                model.setSubsidyApplyStatus(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("申报状态填写不正确");
            }
        }
        // 申报次数
        if(model.getSusidyApplyCount() != null ) {
            boolean b = model.getSusidyApplyCount() >= 0 && model.getSusidyApplyCount() < 100;
            if(!b) {
                errors.add("申报次数范围为0-99");
            }
        }
        return errors;
    }

    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "VEHSUBSIDY"+ WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<VehicleSubsidyModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model  数据model
             */
            @Override
            public List<String> extendValidate(VehicleSubsidyModel model) {
                return extendValidateModel(model);
            }
            /**
             *  保存实体
             * @param model  数据model
             */
            @Override
            public void saveObject(VehicleSubsidyModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public void getBatchUpdateTemplateFile(PagerInfo pagerInfo) {

        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Vehicle> vehicles = findBySqlId("findPager", params);

        //2、将entity转为model, 具体参考多条件查询里的处理
        List<VehicleSubsidyModel> models = new ArrayList<>();
        for (Vehicle entry : vehicles) {
            VehicleSubsidyModel model = VehicleSubsidyModel.fromEntry(entry);
            models.add(model);
        }
        //3、调用DataLoader渲染Model里的linkName之类的数据
        DataLoader.loadNames(models);
        //调用EasyExcel生成文件并下载
        EasyExcel.renderBatchUpdateDemoFile("车辆补贴批量更新模板.xls",VehicleSubsidyModel.class, models);

    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆补贴导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

    @Override
    public String exportOffline(@NotNull PagerInfo pagerInfo) {
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "车辆补贴列表";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        // 创建离线导出任务
        return offlineExportService.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );

    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void exportOfflineProcessor(
        @NotNull final String taskId,
        @NotNull final String createBy,
        @NotNull final Date createTime,
        @NotNull final String exportFileName,
        @NotNull final String exportMethodParams) throws Exception {

        log.info("执行车辆补贴离线导出任务:{}", exportFileName);
        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        final String excelTemplateFile = "sys/res/VehicleSubsidy/exportTemplate.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.vehicleSubsidyMapper::findPager,
            params,
            this::fromEntityToModel,
            this.vehicleSubsidyMapper::findPager,
            redis,
            ws
        );
    }

    @NotNull
    private List<VehicleSubsidyExcelModel> fromEntityToModel(final @NotNull List<Vehicle> entities) {

        final ArrayList<VehicleSubsidyExcelModel> models = Lists.newArrayList();

        for (final Vehicle e : entities) {
            List<VehicleEngeryDeviceLkModel> engertyList = vehicleEngeryDeviceLkService.listByVehicleId(e.getId());
            List<VehicleDriveDeviceLkModel> driveList = vehicleDriveDeviceLkService.listByVehicleId(e.getId(), 1);
            List<VehiclePowerDeviceLkModel> powerList = vehiclePowerDeviceLkService.listByVehicleId(e.getId(), 2);
            VehModelModel vehModel = vehModelService.get(e.getVehModelId());
            final VehicleSubsidyExcelModel model = VehicleSubsidyExcelModel.fromEntry(e, engertyList, driveList, powerList, vehModel);
            models.add(model);
        }

//        DataLoader.loadNames(models);

        return models;
    }


}
