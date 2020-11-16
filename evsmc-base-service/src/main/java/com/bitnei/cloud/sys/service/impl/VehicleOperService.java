package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.dao.VehicleStageLogMapper;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.AreaModel;
import com.bitnei.cloud.sys.model.VehicleOperModel;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 车辆运营使用service impl
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleMapper" )
public class VehicleOperService extends BaseService implements IVehicleOperService {

    @Resource
    private VehicleStageLogMapper vehicleStageLogMapper;
    @Resource
    private IUnitService unitService;
    @Resource
    private IAreaService areaService;
    @Resource
    private IVehOwnerService vehOwnerService;
    @Resource
    private IOwnerPeopleService ownerPeopleService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private DictMapper dictMapper;

    @Override
    public VehicleOperModel get(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id",id);
        Vehicle entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehicleOperModel.fromEntry(entry);
    }

    @Override
    public void save(VehicleOperModel model) {
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", model.getVin());
        Vehicle vehicle = unique("findByVin", params);
        if(vehicle == null) {
            throw new BusinessException("VIN码不存在");
        }
        model.setId(vehicle.getId());
        String errorInfo = this.validaModel(model);
        if(errorInfo != null ) {
            throw new BusinessException(errorInfo);
        }
        insert(model, vehicle);
    }

    @Transactional(rollbackFor = Exception.class)
    private void insert(VehicleOperModel model, Vehicle vehicle) {
        String currentTime = DateUtil.getNow();
        String currentUser = ServletUtil.getCurrentUser();
        // 首次新增运营使用信息, 车辆阶段为运营阶段
        Map<String, Object> param = new HashMap<>();
        param.put("vehicleId", vehicle.getId());
        param.put("stageName", Vehicle.STAGE_TYPE_ENUM.OPER.getValue());
        VehicleStageLog stageLog = vehicleStageLogMapper.findByVehIdAndStage(param);
        if(vehicle.getIsOpered() == null || vehicle.getIsOpered().equals(Constants.NO)||stageLog==null){
            vehicle.setStage(Vehicle.STAGE_TYPE_ENUM.OPER.getValue());
            vehicle.setStageChangeDate(currentTime);

            // 新增运营阶段记录
            stageLog = new VehicleStageLog();
            stageLog.setId(UtilHelper.getUUID());
            stageLog.setVehicleId(vehicle.getId());
            stageLog.setStageName(Vehicle.STAGE_TYPE_ENUM.OPER.getValue());
            stageLog.setCreateTime(model.getOperTime());
            stageLog.setCreateBy(currentUser);
            stageLog.setUpdateTime(currentTime);
            stageLog.setUpdateBy(currentUser);
            vehicleStageLogMapper.insert(stageLog);
        }else{
            // 更新运营阶段记录

            stageLog.setCreateTime(model.getOperTime());
            stageLog.setCreateBy(currentUser);
            stageLog.setUpdateTime(currentTime);
            stageLog.setUpdateBy(currentUser);
            vehicleStageLogMapper.update(stageLog);
        }
        // 运营性质处理: 单位运营时个人车主为null, 个人车主时运营单位为null
        if(Vehicle.VEH_USE_DICT_ENUM.UNIT_OPER.getValue().equals(model.getOperUseType())) {
            model.setOperVehOwnerId(null);
        } else if(Vehicle.VEH_USE_DICT_ENUM.PERSONAGE_OPER.getValue().equals(model.getOperUseType())) {
            model.setOperUnitId(null);
        }
        BeanUtils.copyProperties(model, vehicle);
        // 车牌号自动转换为大写
        vehicle.setLicensePlate(StringUtils.upperCase(vehicle.getLicensePlate()));
        vehicle.setUpdateTime(currentTime);
        vehicle.setUpdateBy(currentUser);
        vehicle.setIsOpered(Constants.YES);
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(MapperUtil.Object2Map(vehicle));
        int res = super.update("updateOper", params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }



    private String validaModel(VehicleOperModel model) {

        // 验证车牌号是否存在
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        Integer vCount = vehicleMapper.findByLicensePlateCount(model.getLicensePlate(), model.getId());
        if(vCount > 0) {
            return "车牌号已存在";
        }
        if(StringUtils.isNotBlank(model.getOperInterNo())) {
            params.put("operInterNo",model.getOperInterNo());
            Vehicle vehicle = unique("findByOperInterNo", params);
            if(vehicle != null && !vehicle.getId().equals(model.getId())) {
                return "内部运营编号已存在";
            }
        } else {
            model.setOperInterNo(null);
        }
        if(StringUtils.isNotBlank(model.getOperLicenseCityId())) {
            try {
                areaService.get(model.getOperLicenseCityId());
            } catch (Exception e) {
                return  "上牌城市不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getOperVehOwnerId())) {
            try {
                vehOwnerService.get(model.getOperVehOwnerId());
            } catch (Exception e) {
                return  "个人车主不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getOperUnitId())) {
            try {
                unitService.get(model.getOperUnitId());
            } catch (Exception e) {
                return  "运营单位不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getOperAreaId())) {
            try {
                areaService.get(model.getOperAreaId());
            } catch (Exception e) {
                return  "运营区域不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getOperSupportOwnerId())) {
            try {
                ownerPeopleService.get(model.getOperSupportOwnerId());
            } catch (Exception e) {
                return  "售后负责人不存在";
            }
        }

        if (StringUtils.isNotEmpty(model.getAnnualInspectionDate())) {
            if (null == model.getAnnualInspectionStatus()) {
                return "年检日期与年检状态不符，年检状态应和年检日期情况一致";
            }
        }
        //年检日期的校验
        if (null != model.getAnnualInspectionStatus()) {

            //初始化年检过期时间
            model.setAnnualInspectionDate(model.getAnnualInspectionDate());
            if (model.getAnnualInspectionStatus().equals(1) &&
                    StringUtils.isNotEmpty(model.getAnnualInspectionDate())) {
                return "年检日期与年检状态不符，未年检状态不存在年检日期";
            }
            if (model.getAnnualInspectionStatus().equals(2)) {
                if (StringUtils.isEmpty(model.getAnnualInspectionDate())) {
                    return "请填写年检日期";
                }
                if (DateUtil.getNow().compareTo(model.getAnnualInspectionPeriod()) > 0) {
                    return "年检日期与年检状态不符，年检已过期";
                }
            }
            if (model.getAnnualInspectionStatus().equals(3)) {
                if (StringUtils.isEmpty(model.getAnnualInspectionDate())) {
                    return "请填写年检日期";
                }
                if (DateUtil.getNow().compareTo(model.getAnnualInspectionPeriod()) <= 0) {
                    return "年检日期与年检状态不符，年检未过期";
                }
            }
        }
        return null;
    }

    @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if(params.containsKey("vehicleIds")) {
            String vehModelIds = (String) params.get("vehicleIds");
            params.put("vehicleIds", StringUtils.split(vehModelIds,","));
        }
        // 非分页查询
        if(pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<Vehicle> entries = findBySqlId("findVehOperPager", params);
            List<VehicleOperModel> models = new ArrayList<>();
            for(Vehicle entry: entries){
                models.add(VehicleOperModel.fromEntry(entry));
            }
            return models;

        } else { // 分页查询
            PagerResult pr = findPagerModel("findVehOperPager", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehicleOperModel> models = Lists.newArrayList();
            for (Object obj : pr.getData()) {
                Vehicle entry = (Vehicle) obj;
                models.add(VehicleOperModel.fromEntry(entry));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public int deleteMulti(String ids) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        String[] arr = ids.split(",");
        int count = 0;
        for ( String id : arr ){
            params.put("id",id);
            Vehicle entry = unique("findById", params);
            if(entry == null) {
                throw new BusinessException("记录不存在");
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            vehicle.setIsOpered(Constants.NO);
//            vehicle.setLicenseType(entry.getLicenseType());
//            vehicle.setLicensePlate(entry.getLicensePlate());

            // 删除运营阶段记录
            Map<String, Object> stageParams = new HashMap<>();
            stageParams.put("vehicleId", id);
            stageParams.put("stageName", Vehicle.STAGE_TYPE_ENUM.OPER.getValue());
            VehicleStageLog stageLog = vehicleStageLogMapper.findByVehIdAndStage(stageParams);
            Map<String, Object> delParams = new HashMap<>();
            delParams.put("id", stageLog.getId());
            vehicleStageLogMapper.delete(delParams);

            // 车辆阶段取为最新一条日志阶段
            VehicleStageLog newStageLog = vehicleStageLogMapper.getNewst(stageParams);
            if(newStageLog != null) {
                vehicle.setStage(newStageLog.getStageName());
                vehicle.setStageChangeDate(DateUtil.getNow());
            }
            params.putAll(MapperUtil.Object2Map(vehicle));
            int r = super.update("updateOper", params);
            count += r;
        }
        return count;
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<Vehicle>(this, "findVehOperPager",
            params, "sys/res/vehicle/exportOper.xls", "车辆运营使用列表", VehicleOperModel.class) {
        }.work();
    }

    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "VEHICLEOPER"+ WsMessageConst.BATCH_IMPORT_SUFFIX;
        work(file, messageType);
    }

    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "VEHICLEOPER"+ WsMessageConst.BATCH_UPDATE_SUFFIX;
        work(file, messageType);
    }

    private void work(MultipartFile file, String messageType) {
        new ExcelBatchHandler<VehicleOperModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model 数据model
             */
            @Override
            public List<String> extendValidate(VehicleOperModel model) {
                boolean isAdd = StringUtils.equals("VEHICLEOPER"+ WsMessageConst.BATCH_IMPORT_SUFFIX,this.messageType);
                List<String> errors = extendValidateModel(model, isAdd);
                log.info("extendValidate === " + Joiner.on("-").skipNulls().join(errors));
                return errors;
            }
            /**
             *  保存实体
             * @param model 数据model
             */
            @Override
            public void saveObject(VehicleOperModel model) {
                Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
                params.put("vin", model.getVin());
                Vehicle vehicle = unique("findByVin", params);
                model.setId(vehicle.getId());
                model.setSellLicenseImgId(vehicle.getSellLicenseImgId());
                insert(model, vehicle);
            }
        }.work();
    }

    private List<String> extendValidateModel(VehicleOperModel model, boolean isAdd) {
        List<String> errors = Lists.newArrayList();
        // VIN码
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        if(StringUtils.isNotBlank(model.getVin())) {
            params.put("vin", model.getVin());
            Vehicle vehicle = super.unique("findByVin", params);
            if(vehicle == null) {
                errors.add("VIN码不存在");
            } else {
                if(isAdd && Constant.TrueAndFalse.TRUE.equals(vehicle.getIsOpered())) {
                    errors.add("车辆运营信息已存在");
                }
                model.setId(vehicle.getId());
            }
        }
        // 车牌号
        if(StringUtils.isNotBlank(model.getLicensePlate())) {
            Integer vCount = vehicleMapper.findByLicensePlateCount(model.getLicensePlate(), model.getId());
            if(vCount > 0) {
                errors.add("车牌号已存在");
            }
        }
        // 车牌类型
        if(StringUtils.isNotBlank(model.getLicenseName())) {
            Map<String, Object> dictPar = ImmutableMap.of("type", "LICENSE_TYPE", "name", model.getLicenseName());
            Dict dict = dictMapper.getByTypeAndName(dictPar);
            if(dict != null) {
                model.setLicenseType(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("车牌类型填写错误");
            }
        }
        // 车牌颜色
        if(StringUtils.isNotBlank(model.getLicenseColorName())) {
            Map<String, Object> dictColor = ImmutableMap.of("type", "LICENSE_COLOR", "name", model.getLicenseColorName());
            Dict dict = dictMapper.getByTypeAndName(dictColor);
            if(dict != null) {
                model.setLicensePlateColor(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("车牌颜色填写错误");
            }
        }
        // 上牌城市
        if(StringUtils.isNotBlank(model.getOperLicenseCityName())) {
            try {
                AreaModel area = areaService.getByName(model.getOperLicenseCityName());
                model.setOperLicenseCityId(area.getId());
            } catch (BusinessException e) {
                errors.add("上牌城市不存在");
            }
        }

        // 车辆用途
        if(StringUtils.isNotBlank(model.getOperUseForName())) {
            Map<String, Object> p = ImmutableMap.of("type", "VEH_USE_FOR", "name", model.getOperUseForName());
            Dict d = dictMapper.getByTypeAndName(p);
            if(d != null) {
                model.setOperUseFor(Integer.parseInt(d.getValue()));
            } else {
                errors.add("车辆用途填写错误");
            }
        }
        // 运营性质
        if(StringUtils.isNotBlank(model.getOperUserTypeName())) {
            Map<String, Object> usep = ImmutableMap.of("type", "VEH_USE_DICT", "name", model.getOperUserTypeName());
            Dict useDict = dictMapper.getByTypeAndName(usep);
            if(useDict != null) {
                model.setOperUseType(Integer.parseInt(useDict.getValue()));
                if(StringUtils.isNotBlank(model.getOperUnitOrOwnerName())) {
                    // 运营单位
                    if(Vehicle.VEH_USE_DICT_ENUM.UNIT_OPER.getValue().equals(Integer.parseInt(useDict.getValue()))) {

                        try {
                            String id = unitService.validateNameCode(model.getOperUnitOrOwnerName(),Constant.UNIT_TYPE_CODE.OPER);
                            model.setOperUnitId(id);
                        } catch (BusinessException e) {
                            errors.add("运营单位不存在或类型不一致");
                        }
                    }
                    // 运营个人车主
                    else if(Vehicle.VEH_USE_DICT_ENUM.PERSONAGE_OPER.getValue().equals(Integer.parseInt(useDict.getValue()))) {
                        VehOwner owner = vehOwnerService.findByOwnerName(model.getOperUnitOrOwnerName());
                        if(owner != null ) {
                            model.setOperVehOwnerId(owner.getId());
                        } else {
                            errors.add("个人车主不存在");
                        }
                    }
                } else {
                    errors.add("运营单位/个人车主不能为空");
                }
            } else {
                errors.add("运营性质填写错误");
            }
        }
        // 运营单位内部编号
        if(StringUtils.isNotBlank(model.getOperInterNo())) {
            params.put("operInterNo", model.getOperInterNo());
            Vehicle vh = super.unique("findByOperInterNo", params);
            if(vh != null && !vh.getVin().equals(model.getVin())) {
                errors.add("运营单位内部编号已存在");
            }
        }
        // 运营区域
        if(StringUtils.isNotBlank(model.getOperAreaName())) {
            try {
                AreaModel operArea = areaService.getByName(model.getOperAreaName());
                model.setOperAreaId(operArea.getId());
            } catch (BusinessException e) {
                errors.add("运营区域不存在");
            }
        }
        // 校验投运时间有效性
        if( StringUtils.isNotBlank(model.getOperTime()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getOperTime())){
                errors.add("投运时间不合法");
            }
        }
        // 车辆售后负责人
        if(StringUtils.isNotBlank(model.getOperSupportOwnerName())) {
            OwnerPeople ownerPeople = ownerPeopleService.findByOwnerName(model.getOperSupportOwnerName());
            if(ownerPeople != null) {
                model.setOperSupportOwnerId(ownerPeople.getId());
            } else {
                errors.add("车辆售后负责人不存在");
            }
        }
        // 存放城市
        if(StringUtils.isNotBlank(model.getOperSaveCityName())) {
            try {
                AreaModel saveArea = areaService.getByName(model.getOperSaveCityName());
                model.setOperSaveCityId(saveArea.getId());
            } catch (BusinessException e) {
                errors.add("存放城市不存在");
            }
        }
        // 对应充电桩城市
        if(StringUtils.isNotBlank(model.getOperChgCityName())) {
            try {
                AreaModel chgArea = areaService.getByName(model.getOperChgCityName());
                model.setOperChgCityId(chgArea.getId());
            } catch (BusinessException e) {
                errors.add("对应充电桩城市不存在");
            }
        }
        // 校验行驶证注册日期有效性
        if( StringUtils.isNotBlank(model.getSellLicenseRegDate()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getSellLicenseRegDate())){
                errors.add("行驶证注册日期不合法");
            }
        }
        // 校验行驶证发放日期有效性
        if( StringUtils.isNotBlank(model.getSellLicenseGrantDate()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getSellLicenseGrantDate())){
                errors.add("行驶证发放日期不合法");
            }
        }

        // 校验初次登记日期有效性
        if( StringUtils.isNotBlank(model.getInitialRegistrationDate()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getInitialRegistrationDate())){
                errors.add("初次登记日期不合法");
            }
        }
        // 年检状态 与 年检日期 校验
        if (StringUtils.isBlank(model.getInspectionStatusName())) {
            if (StringUtils.isNotEmpty(model.getAnnualInspectionDate())) {
                errors.add("年检日期与年检状态不符，年检状态应和年检日期情况一致");
            }
        }else{
            if (StringUtils.isNotEmpty(model.getAnnualInspectionDate())) {
                if (!com.bitnei.cloud.sys.util.DateUtil.validate(model.getAnnualInspectionDate())) {
                    errors.add("年检日期不合法");
                } else {
                    if (("未年检").equals(model.getInspectionStatusName())) {
                        errors.add("年检日期与年检状态不符，未年检状态不存在年检日期");
                    }
                    if ("已年检".equals(model.getInspectionStatusName()) && DateUtil.getNow().compareTo(model.getAnnualInspectionPeriod()) > 0) {
                        errors.add("年检日期与年检状态不符，年检已过期");
                    }
                    if ("年检过期".equals(model.getInspectionStatusName()) && DateUtil.getNow().compareTo(model.getAnnualInspectionPeriod()) <= 0) {
                        errors.add("年检日期与年检状态不符，年检未过期");
                    }
                }
            } else {
                if (!("未年检").equals(model.getInspectionStatusName())){
                    errors.add("年检日期与年检状态不符，年检状态应和年检日期情况一致");
                }
            }
            Map<String, Object> dictInsp = ImmutableMap.of("type", "ANNUAL_INSPECTION_STATUS", "name", model.getInspectionStatusName());
            Dict dict = dictMapper.getByTypeAndName(dictInsp);
            if (dict != null) {
                model.setAnnualInspectionStatus(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("年检状态填写错误");
            }
        }
        // 二级维护状态 与 二级维护时间 校验
        /*if (StringUtils.isNotEmpty(model.getSecondaryMtcTime())) {
            if (StringUtils.isBlank(model.getMtcStatusName())) {
                errors.add("二级维护时间与二级维护状态不符，二级维护状态应和二级维护时间情况一致");
            }else{
                if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getSecondaryMtcTime())){
                    errors.add("二级维护时间不合法");
                }else {
                    Map<String, Object> dictMtc = ImmutableMap.of("type", "SECONDARY_MTC_STATUS", "name", model.getMtcStatusName());
                    Dict dict = dictMapper.getByTypeAndName(dictMtc);
                    if (dict != null) {
//                        if (model.getMtcStatusName().equals("未维护")) {
//                            errors.add("二级维护时间与二级维护状态不符，未维护状态不存在二级维护时间");
//                        }
//                        if ("已维护".equals(model.getMtcStatusName()) && DateUtil.getNow().compareTo(model.getSecondaryMtcTime()) > 0) {
//                            errors.add("二级维护时间与二级维护状态不符，年检已过期");
//                        }
                        model.setSecondaryMtcStatus(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("二级维护状态填写错误");
                    }
                }
            }
        }else {
            if (StringUtils.isNotBlank(model.getMtcStatusName()) && "已维护".equals(model.getMtcStatusName())){
                errors.add("二级维护时间与二级维护状态不符，二级维护状态应和二级维护时间情况一致");
            }
        }*/
        // 校验保养有效期有效性
        if( StringUtils.isNotBlank(model.getMaintenancePeriod()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getMaintenancePeriod())){
                errors.add("保养有效期不合法");
            }
        }
        // 校验二级维护时间有效性
        if( StringUtils.isNotBlank(model.getSecondaryMtcTime()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getSecondaryMtcTime())){
                errors.add("二级维护时间不合法");
            }
        }
        if (StringUtils.isNotBlank(model.getMtcStatusName())) {
            Map<String, Object> dictMtc = ImmutableMap.of("type", "SECONDARY_MTC_STATUS", "name", model.getMtcStatusName());
            Dict dict = dictMapper.getByTypeAndName(dictMtc);
            if (dict != null) {
                model.setSecondaryMtcStatus(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("二级维护状态填写错误");
            }
        }
        // 校验强制报废日期有效性
        if( StringUtils.isNotBlank(model.getForcedScrapDate()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getForcedScrapDate())){
                errors.add("强制报废日期不合法");
            }
        }
        // 校验上牌时间有效性
        if( StringUtils.isNotBlank(model.getOperLicenseTime()) ){
            if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getOperLicenseTime())){
                errors.add("上牌时间不合法");
            }
        }
        return errors;
    }

    /**
     * 批量导入模板
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆运营导入模板.xls", VehicleOperModel.class);
    }

    /**
     * 批量更新模板
     *
     * @param pagerInfo PagerInfo
     */
    @Override
    public void getBatchUpdateTemplateFile(PagerInfo pagerInfo) {

        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Vehicle> vehicles = vehicleMapper.findVehOperPager(params);

        //2、将entity转为model, 具体参考多条件查询里的处理
        List<VehicleOperModel> models = new ArrayList<>();
        for (Vehicle entry : vehicles) {
            VehicleOperModel model = VehicleOperModel.fromEntry(entry);
            models.add(model);
        }
        //3、调用DataLoader渲染Model里的linkName之类的数据
        DataLoader.loadNames(models);
        //调用EasyExcel生成文件并下载
        EasyExcel.renderBatchUpdateDemoFile("车辆运营批量更新模板.xls", VehicleOperModel.class, models);
    }

    /**
     * 导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆运营导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

    @Override
    public int updateInspectStatusOutOfPeriodByVins(List<String> vins) {
        return vehicleMapper.updateInspectStatusOutOfPeriodByVins(vins);
    }
}
