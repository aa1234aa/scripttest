package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.model.RuleTypeModel;
import com.bitnei.cloud.dc.service.IRuleTypeService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.fault.service.IParameterRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.VehModel;
import com.bitnei.cloud.sys.enums.PowerModeEnum;
import com.bitnei.cloud.sys.model.VehModelAlarmModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.model.VehNoticeModel;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.service.IVehModelAlarmService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.cloud.sys.service.IVehNoticeService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehModelService实现<br>
 * 描述： VehModelService实现<br>
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
 * <td>2018-11-12 14:54:43</td>
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
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehModelMapper")
@Slf4j
public class VehModelService extends BaseService implements IVehModelService {

    @Resource
    private IVehModelAlarmService vehModelAlarmService;
    @Resource
    private IVehNoticeService vehNoticeService;
    @Resource
    private IRuleTypeService ruleTypeService;
    @Resource
    private IUnitService unitService;

    @Resource
    private IParameterRuleService parameterRuleService;
    @Resource
    private ICodeRuleService codeRuleService;

    private final String vehModelIds = "vehModelIds";

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.containsKey(vehModelIds)) {
            String ids = (String) params.get(vehModelIds);
            params.put("vehModelIds", StringUtils.split(ids, ","));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehModel> entries = findBySqlId("pagerModel", params);
            List<VehModelModel> models = new ArrayList<>();
            for (VehModel entry : entries) {
                models.add(VehModelModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehModelModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                VehModel obj = (VehModel) entry;
                models.add(VehModelModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<VehModelModel> findList(Map<String, Object> map) {
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
        params.putAll(map);
        if (params.containsKey(vehModelIds)) {
            String ids = (String) params.get(vehModelIds);
            params.put("vehModelIds", StringUtils.split(ids, ","));
        }
        List<VehModel> entries = findBySqlId("pagerModel", params);
        List<VehModelModel> models = new ArrayList<>();
        for (VehModel entry : entries) {
            models.add(VehModelModel.fromEntry(entry));
        }
        return models;
    }

    @Override
    public List<VehModelModel> getAll() {
        List<VehModel> entries = findBySqlId("pagerModel", new HashMap<>());
        List<VehModelModel> models = new ArrayList<>();
        for (VehModel entry : entries) {
            models.add(VehModelModel.fromEntry(entry));
        }
        return models;
    }

    @Override
    public VehModelModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
        params.put("id", id);
        VehModel entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("车型不存在");
        }
        VehModelModel model = VehModelModel.fromEntry(entry);
        VehModelAlarmModel alarmModel;
        try {
            alarmModel = vehModelAlarmService.get(id);
        } catch (Exception ex) {
            alarmModel = new VehModelAlarmModel();
        }
        model.setAlarmModel(alarmModel);
        return model;
    }

    @Override
    public VehModelModel getByName(String vehModelName) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
        params.put("vehModelName", vehModelName);
        VehModel entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("车辆型号已不存在");
        }
        VehModelModel model = VehModelModel.fromEntry(entry);
        VehModelAlarmModel alarmModel;
        try {
            alarmModel = vehModelAlarmService.get(entry.getId());
        } catch (Exception ex) {
            alarmModel = new VehModelAlarmModel();
        }
        model.setAlarmModel(alarmModel);
        return model;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(VehModelModel model) {
        validate(model);
        VehModel obj = new VehModel();
        BeanUtils.copyProperties(model, obj);

        if (model.getEfficiency() != null) {
            setEfficiencyJson(model, obj);
        }
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);

        // 新增通用报警阈值信息
        if (model.getAlarmModel() != null) {
            // 传统燃油车不需要通用报警级别设置
            model.getAlarmModel().setId(id);
            vehModelAlarmService.insert(model.getPowerMode(), model.getAlarmModel());
        }

        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    private void validate(VehModelModel model) {
        validate(model, errorMsg -> {
            throw new BusinessException(errorMsg);
        });
    }

    private void validate(VehModelModel model, Consumer<String> errorCallback) {
        // 不同动力方式下的参数必填校验
        validatePowerModeRequired(model, errorCallback);
        // 通用报警规则校验
        validateRuleEnableRequired(model, errorCallback);
    }

    /**
     * 不同动力方式下的参数必填校验
     * 1 纯电动汽车
     * 2 增程式电动车
     * 3 燃料电动汽车
     * 4 混合动力汽车
     * 5 插电式混合动力汽车
     * 6 传统燃油车
     *
     * @param model
     * @param errorCallback
     */
    private void validatePowerModeRequired(VehModelModel model, final Consumer<String> errorCallback) {
        // 除了传统燃油车，都需要校验以下参数必填
        if (model.getPowerMode() != PowerModeEnum.MODEL_6.ordinal()) {
            // 总储电量
            if (model.getEfficiency().getTotalStorageCapacity() == null) {
                errorCallback.accept("总储电量不能为空");
                return;
            }
            // 电池系统能量密度
            if (model.getEfficiency().getEnergyDensityBatterySystem() == null) {
                errorCallback.accept("电池系统能量密度不能为空");
                return;
            }
            // 电池种类
            if (model.getBattType() == null) {
                errorCallback.accept("电池种类不能为空");
                return;
            }
            // 电池包数量
            if (model.getBatteryPackageCount() == null) {
                errorCallback.accept("电池包数量不能为空");
                return;
            }
            // 驱动电机种类
            if (model.getDriveType() == null) {
                errorCallback.accept("驱动电机种类不能为空");
                return;
            }
            // 驱动方式
            if (model.getDriveMode() == null) {
                errorCallback.accept("驱动方式不能为空");
                return;
            }
            // 驱动电机数量
            if (model.getDriveDeviceCount() == null) {
                errorCallback.accept("驱动电机数量不能为空");
                return;
            }
        }

        // 除了混合动力汽车，都需要校验以下参数必填
        if (model.getPowerMode() != PowerModeEnum.MODEL_4.ordinal()) {
            // 等速续驶里程
            if (model.getEfficiency().getConstantSpeedRange() == null) {
                errorCallback.accept("等速续驶里程不能为空");
                return;
            }
            if(model.getEfficiency().getDrivingMileageUnderWorkingConditions() == null){
                errorCallback.accept("工况续驶里程(Km)不能为空");
                return;
            }
        }

        // 除混合动力汽车、插电式混合动力汽车、传统燃油车，都需要校验以下参数必填
        if (model.getPowerMode() != PowerModeEnum.MODEL_4.ordinal() &&
            model.getPowerMode() != PowerModeEnum.MODEL_5.ordinal() &&
            model.getPowerMode() != PowerModeEnum.MODEL_6.ordinal()) {

            // 百公里耗电量
            if (model.getEfficiency().getKilometresElectricityConsumption() == null) {
                errorCallback.accept("百公里耗电量不能为空");
                return;
            }
        }
        if (model.getPowerMode() == PowerModeEnum.MODEL_6.ordinal()){
            // 传统燃油车 备案激活模式必填
            if (model.getRecordActivationMode() == null){
                errorCallback.accept("备案激活模式不能为空");
                return;
            }
        }
    }

    private static final String VEH_MODEL_ALARM_EMPTY = ",;,;,";

    /**
     * 通用报警规则校验
     * 1 纯电动汽车
     * 2 增程式电动车
     * 3 燃料电动汽车
     * 4 混合动力汽车
     * 5 插电式混合动力汽车
     * 6 传统燃油车
     *
     * @param model
     * @param errorCallback
     */
    private void validateRuleEnableRequired(VehModelModel model, final Consumer<String> errorCallback) {
        if (model.getAlarmModel() == null) {
            return;
        }
        VehModelAlarmModel alarmModel = model.getAlarmModel();
        if (alarmModel.getEnable() == 0) {
            // 禁用状态不检查
            return;
        }
        // 校验平台响应方式
        if (StringUtils.isBlank(alarmModel.getResponseMode())) {
            errorCallback.accept("平台响应方式不能为空");
            return;
        }
        // 校验开始时间阈值
        if (alarmModel.getBeginThreshold() == null) {
            errorCallback.accept("开始时间阈值不能为空");
            return;
        }
        // 校验结束时间阈值
        if (alarmModel.getEndThreshold() == null) {
            errorCallback.accept("结束时间阈值不能为空");
            return;
        }

        // region 校验报警阈值不能全部级别为空

        if( alarmIsEmpty(alarmModel.getTemperatureDifferenceAlarm()) ){
            errorCallback.accept("温度差异报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getBatteryHighTemperaturAlarm()) ){
            errorCallback.accept("电池高温报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getEnergyDeviceOvervoltageAlarm()) ){
            errorCallback.accept("车载储能装置类型过压报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getEnergyDeviceUndervoltageAlarm()) ){
            errorCallback.accept("车载储能装置类型欠压报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getDriveMotorControllerTemperaAlarm()) ){
            errorCallback.accept("驱动电机控制器温度报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getDriveMotorTemperatureAlarm()) ){
            errorCallback.accept("驱动电机温度报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getSingleBatteryOvervoltageAlarm()) ){
            errorCallback.accept("单体电池过压报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getSingleBatteryUndervoltageAlarm()) ){
            errorCallback.accept("单体电池欠压报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getSocLowAlarm()) ){
            errorCallback.accept("SOC低报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getSocJumpAlarm()) ){
            errorCallback.accept("SOC跳变报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getSocHighAlarm()) ){
            errorCallback.accept("SOC过高报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getBatteryConsistencyAlarm()) ){
            errorCallback.accept("电池单体一致性差报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getVehicularDeviceOverchargeAlarm()) ){
            errorCallback.accept("车载储能装置过充报警值，最少保证其中一个级别有值");
            return;
        }
        if( alarmIsEmpty(alarmModel.getInsulationFaultAlarm()) ){
            errorCallback.accept("绝缘故障报警值，最少保证其中一个级别有值");
            return;
        }

        // endregion
    }

    private boolean alarmIsEmpty(String alarm){
        if( StringUtils.isBlank(alarm) || VEH_MODEL_ALARM_EMPTY.equals(alarm) ){
            return true;
        }
        return false;
    }


    /**
     * 设置整车参数json
     *
     * @param model 车辆型号model
     * @param obj   车辆型号实体
     */
    private void setEfficiencyJson(VehModelModel model, VehModel obj) {
        try {
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(model.getEfficiency());
            if (StringUtils.isNotBlank(json)) {
                obj.setEfficiencyJson(json);
            }
        } catch (JsonProcessingException e) {
            log.error("error", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VehModelModel model) {
        validate(model);
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");

        VehModel obj = new VehModel();
        BeanUtils.copyProperties(model, obj);

        setEfficiencyJson(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);

        if (model.getAlarmModel() != null) {
            // 传统燃油车不需要通用报警级别设置
            model.getAlarmModel().setId(model.getId());
            vehModelAlarmService.save(model.getPowerMode(), model.getAlarmModel());
        }else{
            // 处理车辆型号通用报警阈值，禁用报警规则
            parameterRuleService.disabledVehicleModelRule(model.getId());
        }
        // 动力方式为传统燃油车时，删除通用报警阈值
        if (model.getPowerMode() == PowerModeEnum.MODEL_6.ordinal()) {
            vehModelAlarmService.deleteMulti(model.getId());
        }
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

    }

    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            if (r > 0) {
                // 删除车辆车型报警信息
                vehModelAlarmService.deleteMulti(id);

                //删除该车型下的故障规则
                parameterRuleService.deleteByVehModelId(id);
                codeRuleService.deleteByVehModelId(id);

                // 处理车辆型号通用报警阈值，禁用报警规则
                parameterRuleService.disabledVehicleModelRule(id);
            }
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehModel>(this, "pagerModel", params, "sys/res/vehModel/export.xls", "车型管理") {
        }.work();

    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHMODEL" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<VehModelModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehModelModel
             * @return 错误列表集合
             */
            @Override
            public List<String> extendValidate(VehModelModel model) {
                List<String> errors = Lists.newArrayList();
                // 车辆型号
                if (StringUtils.isNotBlank(model.getVehModelName())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
                    params.put("vehModelName", model.getVehModelName());
                    VehModel entry = unique("findByName", params);
                    if (entry != null) {
                        errors.add("车辆型号已存在");
                    }
                }
                // 车辆公告型号
                if (StringUtils.isNotBlank(model.getNoticeName())) {
                    try {
                        VehNoticeModel notice = vehNoticeService.getByName(model.getNoticeName());
                        model.setNoticeId(notice.getId());
                    } catch (BusinessException e) {
                        errors.add("车辆公告型号不存在");
                    }
                }
                // 协议类型
                if (StringUtils.isNotBlank(model.getRuleIdName())) {
                    try {
                        RuleTypeModel rule = ruleTypeService.getByName(model.getRuleIdName());
                        model.setRuleId(rule.getId());
                    } catch (BusinessException e) {
                        errors.add("协议类型不存在");
                    }
                }
                // 车辆厂商
                if (StringUtils.isNotBlank(model.getVehUnitName())) {
                    try {
                        String id = unitService.validateNameCode(model.getVehUnitName(), Constant.UNIT_TYPE_CODE.VEHICLE);
                        model.setVehUnitId(id);
                    } catch (BusinessException e) {
                        errors.add("车辆厂商不存在或类型不一致");
                    }
                }

                return errors;

            }

            /**
             *  保存实体
             *
             * @param model VehModelModel
             */
            @Override
            public void saveObject(VehModelModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "VEHMODEL" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<VehModelModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehModelModel
             * @return 错误列表
             */
            @Override
            public List<String> extendValidate(VehModelModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model VehModelModel
             */
            @Override
            public void saveObject(VehModelModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public Map<String, String> getVehModelModelNames(String vehModelIds) {
        //车辆型号
        StringBuilder vehModelName = new StringBuilder();
        StringBuilder vehModelId = new StringBuilder();
        if ("all".equals(vehModelIds) || StringUtils.isBlank(vehModelIds)) {
            vehModelName.append("全部车型");
            vehModelId.append(vehModelIds);
        } else {
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
            params.put("vehModelIds", StringUtils.split(vehModelIds, ","));
            List<VehModel> entries = findBySqlId("pagerModel", params);
            int size = entries.size();
            int i = 0;
            for (VehModel model : entries) {
                vehModelName.append(model.getVehModelName());
                vehModelId.append(model.getId());
                i++;
                if (i < size) {
                    vehModelName.append(",");
                    vehModelId.append(",");
                }
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("vehModelName", vehModelName.toString());
        map.put("vehModelId", vehModelId.toString());
        return map;
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆型号导入模板.xls", VehModelModel.class);
    }

    /**
     * 通过车辆ID查询所属车型信息
     *
     * @param vehicleId
     * @return
     */
    @Override
    public VehModelModel getByVehicleId(String vehicleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model", "vm");
        params.put("vehicleId", vehicleId);
        VehModel entry = unique("getByVehicleId", params);
        if (entry == null) {
            throw new BusinessException("车型不存在");
        }
        VehModelModel model = VehModelModel.fromEntry(entry);
        return model;
    }

}
