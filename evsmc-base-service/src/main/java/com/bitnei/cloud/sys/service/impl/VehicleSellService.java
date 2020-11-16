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
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.model.VehicleSellModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.bitnei.cloud.sys.service.IVehicleSellService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleMapper" )
public class VehicleSellService extends BaseService implements IVehicleSellService {

    @Resource
    private VehicleStageLogMapper vehicleStageLogMapper;
    @Resource
    private IVehOwnerService vehOwnerService;
    @Resource
    private IUnitService unitService;
    @Resource
    private IOwnerPeopleService ownerPeopleService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private AreaService areaService;
    @Resource
    private DictMapper dictMapper;

    @Override
    public VehicleSellModel get(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id",id);
        Vehicle entry = super.unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehicleSellModel.fromEntry(entry);
    }

    @Override
    public void save(VehicleSellModel model) {
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", model.getVin());
        Vehicle vehicle = super.unique("findByVin", params);
        if(vehicle == null) {
            throw new BusinessException("VIN码已不存在");
        }
        String errorInfo = this.validaModel(model);
        if(errorInfo != null) {
            throw new BusinessException(errorInfo);
        }
        insert(model, vehicle);

    }

    public void insert(VehicleSellModel model, Vehicle vehicle){
        model.setId(vehicle.getId());
        String currentTime = DateUtil.getNow();
        String currentUser = ServletUtil.getCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("vehicleId", vehicle.getId());
        param.put("stageName", Vehicle.STAGE_TYPE_ENUM.SELL.getValue());
        VehicleStageLog stageLog = vehicleStageLogMapper.findByVehIdAndStage(param);
        // 首次新增销售信息, 车辆阶段为销售阶段
        if(vehicle.getIsSelled() == null || vehicle.getIsSelled().equals(Constants.NO)||null==stageLog){
            vehicle.setStage(Vehicle.STAGE_TYPE_ENUM.SELL.getValue());
            vehicle.setStageChangeDate(currentTime);

            // 新增销售阶段记录
            stageLog = new VehicleStageLog();
            stageLog.setId(UtilHelper.getUUID());
            stageLog.setVehicleId(vehicle.getId());
            stageLog.setStageName(Vehicle.STAGE_TYPE_ENUM.SELL.getValue());
            stageLog.setCreateTime(model.getSellDate());
            stageLog.setCreateBy(currentUser);
            stageLog.setUpdateTime(currentTime);
            stageLog.setUpdateBy(currentUser);
            vehicleStageLogMapper.insert(stageLog);
        }else{
            // 更新销售阶段记录

            stageLog.setCreateTime(model.getSellDate());
            stageLog.setCreateBy(currentUser);
            stageLog.setUpdateTime(currentTime);
            stageLog.setUpdateBy(currentUser);
            vehicleStageLogMapper.update(stageLog);
        }
        // 购车领域处理
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PRIVATE_SPHERE.getValue().equals(model.getSellForField())) {
            model.setSellPubUnitId(null);
        } else if(Vehicle.SEEL_FOR_FIELD_ENUM.PUBLIC_SPHERE.getValue().equals(model.getSellForField())) {
            model.setSellPriVehOwnerId(null);
        }

        BeanUtils.copyProperties(model, vehicle);
        vehicle.setCreateTime(currentTime);
        vehicle.setCreateBy(currentUser);
        vehicle.setIsSelled(Constants.YES);
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(MapperUtil.Object2Map(vehicle));
        int res = super.update("updateSell", params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }

    }

    private String validaModel(VehicleSellModel model) {

        if(StringUtils.isNotBlank(model.getSellPriVehOwnerId())) {
            try {
                vehOwnerService.get(model.getSellPriVehOwnerId());
            } catch (Exception e) {
                return "购车车主不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getSellPubUnitId())) {
            try {
                unitService.get(model.getSellPubUnitId());
            } catch (Exception e) {
                return "购车单位不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getSell4sUnitId())) {
            try {
                unitService.get(model.getSell4sUnitId());
            } catch (Exception e) {
                return "经销商不存在";
            }
        }
        if(StringUtils.isNotBlank(model.getSellSellerId())) {
            try {
                ownerPeopleService.get(model.getSellSellerId());
            } catch (Exception e) {
                return "销售人员不存在";
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

            List<Vehicle> entries = findBySqlId("findVehSellPager", params);
            List<VehicleSellModel> models = new ArrayList<>();
            for(Vehicle entry: entries){
                models.add(VehicleSellModel.fromEntry(entry));
            }
            return models;

        } else { // 分页查询

            PagerResult pr = findPagerModel("findVehSellPager", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehicleSellModel> models = Lists.newArrayList();
            for (Object obj : pr.getData()) {
                Vehicle entry = (Vehicle) obj;
                models.add(VehicleSellModel.fromEntry(entry));
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
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            vehicle.setIsSelled(Constants.NO);

            // 删除销售阶段记录
            Map<String, Object> stageParams = new HashMap<>();
            stageParams.put("vehicleId", id);
            stageParams.put("stageName", Vehicle.STAGE_TYPE_ENUM.SELL.getValue());
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
            int r = super.update("updateSell", params);
            count += r;
        }
        return count;
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<Vehicle>(this, "findVehSellPager", params, "sys/res/vehicle/exportSell.xls", "车辆销售列表", VehicleSellModel.class) {
        }.work();
    }


    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "VEHICLESELL"+ WsMessageConst.BATCH_IMPORT_SUFFIX;
        work(file, messageType);
    }

    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "VEHICLESELL"+ WsMessageConst.BATCH_UPDATE_SUFFIX;
        work(file, messageType);
    }

    private void work(MultipartFile file, String messageType) {
        new ExcelBatchHandler<VehicleSellModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model  数据model
             */
            @Override
            public List<String> extendValidate(VehicleSellModel model) {
                boolean isAdd = StringUtils.equals("VEHICLESELL"+ WsMessageConst.BATCH_IMPORT_SUFFIX,this.messageType);
                List<String> errors = extendValidateModel(model, isAdd);
                log.info("batchImport === " + Joiner.on("-").skipNulls().join(errors));
                return errors;
            }
            /**
             *  保存实体
             * @param model  数据model
             */
            @Override
            public void saveObject(VehicleSellModel model) {
                Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
                params.put("vin", model.getVin());
                Vehicle vehicle = unique("findByVin", params);
                insert(model, vehicle);
            }
        }.work();
    }

    private List<String> extendValidateModel(VehicleSellModel model, boolean isAdd) {
        List<String> errors = Lists.newArrayList();
        // VIN码
        if(StringUtils.isNotBlank(model.getVin())) {
            Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
            params.put("vin", model.getVin());
            Vehicle vehicle = super.unique("findByVin", params);
            if(vehicle == null) {
                errors.add("VIN码不存在");
            } else if(isAdd && Constant.TrueAndFalse.TRUE.equals(vehicle.getIsSelled())) {
                errors.add("车辆销售信息已存在");
            }
        }
        // 购车城市
        if(StringUtils.isNotBlank(model.getSellCityName())) {
            try {
                AreaModel area = areaService.getByName(model.getSellCityName());
                model.setSellCityId(area.getId());
            } catch (BusinessException e) {
                errors.add("购车城市不存在");
            }
        }
        // 购车领域
        if(StringUtils.isNotBlank(model.getSellForFieldName())) {
            Map<String, Object> dictPar = ImmutableMap.of("type", "SEEL_FOR_FIELD", "name", model.getSellForFieldName());
            Dict dict = dictMapper.getByTypeAndName(dictPar);
            if(dict != null) {
                model.setSellForField(Integer.parseInt(dict.getValue()));
            } else {
                errors.add("购车领域填写错误");
            }
        }
        // 购车车主
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PRIVATE_SPHERE.getValue().equals(model.getSellForField())) {
            if(StringUtils.isNotBlank(model.getOwnerName())) {
                VehOwner owner = vehOwnerService.findByOwnerName(model.getOwnerName());
                if(owner != null ) {
                    model.setSellPriVehOwnerId(owner.getId());
                } else {
                    errors.add("购车车主信息不存在");
                }
            } else {
                errors.add("购车车主不能为空");
            }

        }
        // 购车单位
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PUBLIC_SPHERE.getValue().equals(model.getSellForField())) {
            if(StringUtils.isNotBlank(model.getSellPubUnitName())) {
                try {
                    String id = unitService.validateNameCode(model.getSellPubUnitName(),Constant.UNIT_TYPE_CODE.SELL);
                    model.setSellPubUnitId(id);
                } catch (BusinessException e) {
                    errors.add("购车单位不存在或类型不一致");
                }
            } else {
                errors.add("购车单位不能为空");
            }

        }
        // 经销商
        if(StringUtils.isNotBlank(model.getSell4sUnitName())) {
            try {
                UnitModel unit = unitService.getByName(model.getSell4sUnitName());
                model.setSell4sUnitId(unit.getId());
            } catch (BusinessException e) {
                errors.add("经销商不存在");
            }
        }
        // 销售人员
        if(StringUtils.isNotBlank(model.getSellSellerName())) {
            OwnerPeople ownerPeople = ownerPeopleService.findByOwnerName(model.getSellSellerName());
            if(ownerPeople != null) {
                model.setSellSellerId(ownerPeople.getId());
            } else {
                errors.add("销售人员不存在");
            }
        }
        return errors;
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆销售导入模板.xls" , VehicleSellModel.class);
    }

    /**
     * 生成批量更新模板文件
     *
     * @param pagerInfo
     */
    @Override
    public void getBatchUpdateTemplateFile(PagerInfo pagerInfo) {

        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Vehicle> vehicles = vehicleMapper.findVehSellPager(params);

        //2、将entity转为model, 具体参考多条件查询里的处理
        List<VehicleSellModel> models = new ArrayList<>();
        for (Vehicle entry : vehicles) {
            VehicleSellModel model = VehicleSellModel.fromEntry(entry);
            models.add(model);
        }
        //3、调用DataLoader渲染Model里的linkName之类的数据
        DataLoader.loadNames(models);
        //调用EasyExcel生成文件并下载
        EasyExcel.renderBatchUpdateDemoFile("车辆销售批量更新模板.xls",VehicleSellModel.class, models);
    }

    /**
     * 导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆销售导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

}
