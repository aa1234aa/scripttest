package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.fault.dao.ParameterRuleMapper;
import com.bitnei.cloud.fault.domain.ParameterRule;
import com.bitnei.cloud.fault.enums.VehicleModelRuleTypeEnum;
import com.bitnei.cloud.fault.model.FormulaModel;
import com.bitnei.cloud.fault.model.ParameterRuleModel;
import com.bitnei.cloud.fault.service.INotifierRuleLkService;
import com.bitnei.cloud.fault.service.IParameterRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehModelAlarm;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.model.VehModelAlarmModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ParameterRuleService实现<br>
 * 描述： ParameterRuleService实现<br>
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
 * <td>2019-02-27 16:35:01</td>
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
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.ParameterRuleMapper")
public class ParameterRuleService extends BaseService implements IParameterRuleService {

    @Autowired
    private IVehModelService vehModelService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private AlarmInfoService alarmInfoService;

    @Resource
    private ParameterRuleMapper parameterRuleMapper;

    @Autowired
    private INotifierRuleLkService notifierRuleLkService;

    private static final String PRODUCE_ALARM = "PRODUCE_ALARM";
    private static final String ALARM_LEVEL = "ALARM_LEVEL";
    private static final String RESPONSE_MODE = "RESPONSE_MODE";
    private static final String ENABLED_STATUS = "ENABLED_STATUS";

    /**
     * 公式运算符
     */
    private static final List<String> OPERATOR_ARRAY = Lists.newArrayList("+", "-", "*", "/", "<", ">", ">=", "<=", "(", ")", "==", "&&", "||");

    /**
     * 正则表达式：正负数字(8,2)
     */
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^(\\-)?([0-9]{1,6})(\\.[0-9]{1,2})?$");

    /**
     * 内置19项规则
     */
    private static final Pattern PRESET_RULE_19ITEM_PATTERN = Pattern.compile("^d[0-9]\\d*==1.0$");

    @Override
    public Object list(PagerInfo pagerInfo) {
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<ParameterRule> entries = findBySqlId("pagerModel", params);
            List<ParameterRuleModel> models = new ArrayList<>();
            for (ParameterRule entity : entries) {
                ParameterRuleModel model = ParameterRuleModel.fromEntry(entity);
                setVehModelName(model, "list");
                model.setResponseModeDisplay(dictService.getDictNames(entity.getResponseMode(), RESPONSE_MODE));
                resetPresetRule19ItemValue(model);
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<ParameterRuleModel> models = new ArrayList<>();
            for (Object entity : pr.getData()) {
                ParameterRule obj = (ParameterRule) entity;
                ParameterRuleModel model = ParameterRuleModel.fromEntry(obj);
                setVehModelName(model, "list");
                model.setResponseModeDisplay(dictService.getDictNames(obj.getResponseMode(), RESPONSE_MODE));
                resetPresetRule19ItemValue(model);
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public ParameterRuleModel get(String id) {
        ParameterRule entity = parameterRuleMapper.findById(id);
        if (entity == null) {
            throw new BusinessException("参数异常提醒规则已不存在");
        }
        ParameterRuleModel model = ParameterRuleModel.fromEntry(entity);
        setVehModelName(model, "getMethod");
        model.setResponseModeDisplay(dictService.getDictNames(entity.getResponseMode(), RESPONSE_MODE));
        resetPresetRule19ItemValue(model);
        return model;
    }

    @Override
    public void insert(ParameterRuleModel model) {
        saveCheck(model);
        ParameterRule obj = new ParameterRule();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = parameterRuleMapper.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public String insertReturnId(ParameterRuleModel model) {
        saveCheck(model);
        ParameterRule obj = new ParameterRule();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = parameterRuleMapper.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ParameterRuleModel newModel) {
        saveCheck(newModel);
        ParameterRule obj = new ParameterRule();
        BeanUtils.copyProperties(newModel, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());


        ParameterRuleModel oldModel = get(newModel.getId());

        // 判断是否预设报警规则, 预设规则不允许修改删除
        if (oldModel.getPresetRule().equals(Constants.BoolType.yes.getValue())) {
            throw new BusinessException("系统预设通用报警不允许修改");
        }

        if (ruleChangeScope(newModel, oldModel)) {
            //规则变了, 删除以前的, 插入一条新的
            parameterRuleMapper.delete(newModel.getId());
            String newId = insertReturnId(newModel);

            //更新推送规则
            notifierRuleLkService.updateRuleIdByOld(oldModel.getId(), newId);
        } else {
            Map<String, Object> updateMap = updateAlarm(newModel, oldModel);
            if (MapUtils.isNotEmpty(updateMap)) {
                updateMap.put("ruleId", oldModel.getId());
                alarmInfoService.updateAlarm(updateMap);
            }
            parameterRuleMapper.update(obj);
        }
    }

    /**
     * 根据规则主要信息变化判断是否需要生成新的规则
     *
     * @param newModel 待更新Model
     * @param newModel 原始Model
     * @return
     */
    private boolean ruleChangeScope(ParameterRuleModel newModel, ParameterRuleModel oldModel) {
        // 适用车型有变化, 需要生成新的规则
        if (!oldModel.getVehModelId().equalsIgnoreCase(newModel.getVehModelId())) {
            return true;
        }
        // 规则公式被修改, 需要生成新的规则
        if (!oldModel.getFormula().equals(newModel.getFormula())) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否需要更新当前报警信息: 故障规则名称, 报警级别, 响应方式
     *
     * @param newModel
     * @param oldModel
     * @return
     */
    private Map<String, Object> updateAlarm(ParameterRuleModel newModel, ParameterRuleModel oldModel) {
        Map<String, Object> updateMap = new HashMap<>();
        // 故障规则名称变化
        if (!oldModel.getName().equals(newModel.getName())) {
            updateMap.put("ruleName", newModel.getName());
        }
        // 报警级别变化
        if (!oldModel.getAlarmLevel().equals(newModel.getAlarmLevel())) {
            updateMap.put("alarmLevel", newModel.getAlarmLevel());
        }
        // 响应方式变化
        if (!oldModel.getResponseMode().equals(newModel.getResponseMode())) {
            updateMap.put("responseMode", newModel.getResponseMode());
        }
        return updateMap;
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {
        String[] arr = ids.split(",");
        ParameterRuleModel oldModel;
        int count = 0;
        for (String id : arr) {
            oldModel = get(id);
            // 判断是否预设报警规则, 预设规则不允许修改删除
            if (oldModel.getPresetRule().equals(Constants.BoolType.yes.getValue())) {
                throw new BusinessException("系统预设通用报警不允许删除");
            }

            int r = parameterRuleMapper.delete(id);

            //删除对应的推送规则
            notifierRuleLkService.deleteByRuleId(id);

            count += r;
        }
        return count;
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        new ExcelExportHandler<ParameterRule>(this, "pagerModel", params, "fault/res/parameterRule/export.xls", "平台规则") {
            @Override
            public Object process(ParameterRule entity) {
                ParameterRuleModel model = (ParameterRuleModel) super.process(entity);
                setVehModelName(model, "export");
                model.setResponseModeDisplay(dictService.getDictNames(entity.getResponseMode(), RESPONSE_MODE));
                resetPresetRule19ItemValue(model);
                return model;
            }
        }.work();
    }

    /**
     * 重置19项的响应级别，响应方式
     *
     * @param model
     */
    private void resetPresetRule19ItemValue(ParameterRuleModel model) {
        if (1 == model.getPresetRule() && PRESET_RULE_19ITEM_PATTERN.matcher(model.getFormula()).matches()) {
            // 如果是内置的19项规则，则重置
            model.setResponseMode(null);
            model.setResponseModeDisplay(null);
            model.setAlarmLevel(null);
            model.setAlarmLevelDisplay("动态");
        }
    }

    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "PARAMETERRULE" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<ParameterRuleModel>(file, messageType, GroupExcelImport.class) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ParameterRuleModel model) {

                List<String> externErrorStrings = new ArrayList<>();
                if (StringUtils.isBlank(model.getName())) {
                    externErrorStrings.add("规则名称不能为空");
                }
                //查询车辆型号
                if (StringUtils.isBlank(model.getVehModelName())) {
                    model.setVehModelId("all");
                } else {
                    VehModelModel vehModelModel = vehModelService.getByName(model.getVehModelName().trim());
                    if (null != vehModelModel) {
                        model.setVehModelId(vehModelModel.getId());
                    } else {
                        externErrorStrings.add("适用车辆型号不存在");
                    }
                }
                if (StringUtils.isBlank(model.getProduceAlarmDisplay())) {
                    externErrorStrings.add("是否产生报警不能为空");
                }
                if (StringUtils.isBlank(model.getAlarmLevelDisplay())) {
                    externErrorStrings.add("报警级别不能为空");
                }
                if (StringUtils.isBlank(model.getResponseModeDisplay())) {
                    externErrorStrings.add("响应方式不能为空");
                }
                if (StringUtils.isBlank(model.getFormula())) {
                    externErrorStrings.add("规则公式不能为空");
                }
                if (null == model.getBeginThreshold()) {
                    externErrorStrings.add("开始时间阈值不能为空");
                }
                if (null == model.getEndThreshold()) {
                    externErrorStrings.add("结束时间阈值不能为空");
                }
                return externErrorStrings;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ParameterRuleModel model) {
                //是否产生报警
                List<DictModel> dictModels = dictService.findByDictType(PRODUCE_ALARM);
                for (DictModel dictModel : dictModels) {
                    if (dictModel.getName().equals(model.getProduceAlarmDisplay())) {
                        model.setProduceAlarm(Integer.valueOf(dictModel.getValue()));
                    }
                }
                //故障等级
                dictModels = dictService.findByDictType(ALARM_LEVEL);
                for (DictModel dictModel : dictModels) {
                    if (dictModel.getName().equals(model.getAlarmLevelDisplay())) {
                        model.setAlarmLevel(Integer.valueOf(dictModel.getValue()));
                    }
                }
                //响应方式
                dictModels = dictService.findByDictType(RESPONSE_MODE);
                String[] displays = model.getResponseModeDisplay().split(",");
                StringBuilder values = new StringBuilder("");
                for (String name : displays) {
                    for (DictModel dictModel : dictModels) {
                        if (dictModel.getName().equals(name)) {
                            values.append(dictModel.getValue());
                            values.append(",");
                            break;
                        }
                    }
                }
                String value = values.toString().endsWith(",") ? values.toString().substring(0, values.length() - 1) : values.toString();
                model.setResponseMode(value);
                //启用状态
                dictModels = dictService.findByDictType(ENABLED_STATUS);
                for (DictModel dictModel : dictModels) {
                    if (dictModel.getName().equals(model.getEnabledStatusDisplay())) {
                        model.setEnabledStatus(Integer.valueOf(dictModel.getValue()));
                    }
                }
                insert(model);
            }
        }.work();
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("参数异常报警规则导入模板.xls", ParameterRuleModel.class);
    }

    /**
     * 统计数量
     *
     * @param params
     * @return
     */
    @Override
    public int count(Map<String, Object> params) {
        return parameterRuleMapper.count(params);
    }

    @Override
    public void deleteByVehModelId(String vehModelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehModelId", vehModelId);
        params.put("updateTime", DateUtil.getNow());
        params.put("updateBy", ServletUtil.getCurrentUser());
        parameterRuleMapper.deleteByVehModelId(params);
    }

    // region 处理车辆型号通用规则

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleVehicleModelRule(VehModelAlarmModel newModel, VehModelAlarm oldModel) {
        if (newModel == null) {
            return;
        }
        if (newModel.getEnable() == 0) {
            // 如果禁用，将规则停掉
            disableAllVehicleModelRule(newModel.getId());
            return;
        }
        if (oldModel == null || oldModel.getEnable() == 0) {
            // 如果没有录入过
            inertVehicleModelRule(newModel);
        } else {
            boolean rebuildAll = needRebuildAllVehicleModelRule(newModel, oldModel);
            if (rebuildAll) {
                // 将type为1的车辆型号下的所有通用规则停用
                disableAllVehicleModelRule(newModel.getId());
                // 重新生成新的规则
                inertVehicleModelRule(newModel);
            } else {
                // 判断单条规则变更
                updateVehicleModelRule(newModel, oldModel);
            }

            // 如果平台响应方式有变更，则统一更新, 不影响storm计算逻辑，无需重新生成新规则
            String newResponseModel = newModel.getResponseMode() == null ? "" : newModel.getResponseMode();
            String oldResponseModel = oldModel.getResponseMode() == null ? "" : oldModel.getResponseMode();
            if (!newResponseModel.equals(oldResponseModel)) {
                // 响应方式有变更
                parameterRuleMapper.updateVehicleModelRuleResponseMode(newModel.getId(), newModel.getResponseMode(), ServletUtil.getCurrentUser(), DateUtil.getNow());
            }
        }
    }

    @Override
    public void disabledVehicleModelRule(final String vehModelId) {
        // 如果禁用，将规则停掉
        disableAllVehicleModelRule(vehModelId);
    }

    /**
     * 检查是否需要重新构建所有规则
     * 影响所有规则的参数都需要重新生成整套规则
     *
     * @param newModel
     * @param oldModel
     * @return true 重新生成所有规则
     */
    private boolean needRebuildAllVehicleModelRule(VehModelAlarmModel newModel, VehModelAlarm oldModel) {
        // 开始时间阈值有变更
        if (!newModel.getBeginThreshold().equals(oldModel.getBeginThreshold())) {
            return true;
        }
        // 结束时间阈值有变更
        if (!newModel.getEndThreshold().equals(oldModel.getEndThreshold())) {
            return true;
        }
        return false;
    }

    /**
     * 禁用该车辆型号下的type为1通用规则
     *
     * @param vehModelId 车辆型号ID
     */
    private void disableAllVehicleModelRule(String vehModelId) {
        disableVehicleModelRule(vehModelId, null, null);
    }

    /**
     * 禁用该车辆型号下type为1 且 指定报警类型下的通用规则
     *
     * @param vehModelId 车辆型号ID
     * @param type       VehicleModelRuleTypeEnum.getType()
     */
    private void disableVehicleModelRule(String vehModelId, String type) {
        disableVehicleModelRule(vehModelId, type, null);
    }

    /**
     * 禁用该车辆型号下type为1 且 指定报警类型、指定某个级别的通用规则
     *
     * @param vehModelId 车辆型号ID
     * @param type       VehicleModelRuleTypeEnum.getGroupName()
     */
    private void disableVehicleModelRule(String vehModelId, String type, Integer level) {
        parameterRuleMapper.deleteVehicleModelRule(vehModelId, level, type, ServletUtil.getCurrentUser(), DateUtil.getNow());
    }

    /**
     * 新增车型通用规则
     *
     * @param newModel
     */
    private void inertVehicleModelRule(VehModelAlarmModel newModel) {
        List<ParameterRule> levelRules = new ArrayList<>(45);
        // 温度差异报警值
        generateRule(VehicleModelRuleTypeEnum.TEMPERATURE_DIFFERENCE_ALARM, newModel, levelRules);
        // 电池高温报警值
        generateRule(VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM, newModel, levelRules);
        // 车载储能装置类型过压报警值
        generateRule(VehicleModelRuleTypeEnum.ENERGY_DEVICE_OVERVOLTAGE_ALARM, newModel, levelRules);
        // 车载储能装置类型欠压报警值
        generateRule(VehicleModelRuleTypeEnum.ENERGY_DEVICE_UNDERVOLTAGE_ALARM, newModel, levelRules);
        // 驱动电机控制器温度报警值
        generateRule(VehicleModelRuleTypeEnum.DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM, newModel, levelRules);
        // 驱动电机温度报警值
        generateRule(VehicleModelRuleTypeEnum.DRIVE_MOTOR_TEMPERATURE_ALARM, newModel, levelRules);
        // 单体电池过压报警值
        generateRule(VehicleModelRuleTypeEnum.SINGLE_BATTERY_OVERVOLTAGE_ALARM, newModel, levelRules);
        // 单体电池欠压报警值
        generateRule(VehicleModelRuleTypeEnum.SINGLE_BATTERY_UNDERVOLTAGE_ALARM, newModel, levelRules);
        // SOC低报警值
        generateRule(VehicleModelRuleTypeEnum.SOC_LOW_ALARM, newModel, levelRules);
        // SOC跳变报警值
        generateRule(VehicleModelRuleTypeEnum.SOC_JUMP_ALARM, newModel, levelRules);
        // SOC过高报警值
        generateRule(VehicleModelRuleTypeEnum.SOC_HIGH_ALARM, newModel, levelRules);
        // 电池单体一致性差报警值
        generateRule(VehicleModelRuleTypeEnum.BATTERY_CONSISTENCY_ALARM, newModel, levelRules);
        // 车载储能装置过充报警值
        generateRule(VehicleModelRuleTypeEnum.VEHICULAR_DEVICE_OVERCHARGE_ALARM, newModel, levelRules);
        // 绝缘故障报警值
        generateRule(VehicleModelRuleTypeEnum.INSULATION_FAULT_ALARM, newModel, levelRules);
        // 保存到规则表中
        levelRules.forEach(rule -> parameterRuleMapper.insert(rule));
    }


    /**
     * 分割符
     */
    private static final String SPLITTER_LEVEL = ",";
    private static final String SPLITTER_VEHICLE_RULE = ";";

    public void generateRule(VehicleModelRuleTypeEnum type, VehModelAlarmModel newModel, List<ParameterRule> levelRules) {
        String value = "";
        switch (type) {
            case SOC_LOW_ALARM:
                value = newModel.getSocLowAlarm();
                break;
            case SOC_HIGH_ALARM:
                value = newModel.getSocHighAlarm();
                break;
            case SOC_JUMP_ALARM:
                value = newModel.getSocJumpAlarm();
                break;
            case INSULATION_FAULT_ALARM:
                value = newModel.getInsulationFaultAlarm();
                break;
            case BATTERY_CONSISTENCY_ALARM:
                value = newModel.getBatteryConsistencyAlarm();
                break;
            case TEMPERATURE_DIFFERENCE_ALARM:
                value = newModel.getTemperatureDifferenceAlarm();
                break;
            case BATTERY_HIGH_TEMPERATUR_ALARM:
                value = newModel.getBatteryHighTemperaturAlarm();
                break;
            case DRIVE_MOTOR_TEMPERATURE_ALARM:
                value = newModel.getDriveMotorTemperatureAlarm();
                break;
            case ENERGY_DEVICE_OVERVOLTAGE_ALARM:
                value = newModel.getEnergyDeviceOvervoltageAlarm();
                break;
            case ENERGY_DEVICE_UNDERVOLTAGE_ALARM:
                value = newModel.getEnergyDeviceUndervoltageAlarm();
                break;
            case SINGLE_BATTERY_OVERVOLTAGE_ALARM:
                value = newModel.getSingleBatteryOvervoltageAlarm();
                break;
            case SINGLE_BATTERY_UNDERVOLTAGE_ALARM:
                value = newModel.getSingleBatteryUndervoltageAlarm();
                break;
            case VEHICULAR_DEVICE_OVERCHARGE_ALARM:
                value = newModel.getVehicularDeviceOverchargeAlarm();
                break;
            case DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM:
                value = newModel.getDriveMotorControllerTemperaAlarm();
                break;
            default:
                break;
        }

        appendToLevelRules(type, newModel, levelRules, value);
    }

    /**
     * 生成表达式
     *
     * @param levelValue
     * @param type
     * @return
     */
    public FormulaModel generateFormula(
        String levelValue,
        VehicleModelRuleTypeEnum type) {

        String var;
        String varDesc;
        if (type.getMinParam() == null) {
            var = type.getMaxParam();
            varDesc = type.getMaxDesc();
        } else if (type.getMaxParam() == null) {
            var = type.getMinParam();
            varDesc = type.getMinDesc();
        } else {
            var = type.getMaxParam() + "-" + type.getMinParam();
            varDesc = type.getMaxDesc() + "-" + type.getMinDesc();
        }

        return generateNormalFormula(levelValue, var, varDesc);
    }

    /**
     * 生成区间内表达式
     *
     * @param levelValue
     * @param var
     * @return
     */
    private FormulaModel generateNormalFormula(String levelValue, String var, String varDesc) {
        FormulaModel formulaModel = new FormulaModel();
        String formula = "";
        String formulaDesc = "";
        // 表达式： 变量 >= 下限 && 变量 < 上限
        if (!SPLITTER_LEVEL.equals(levelValue)) {
            String[] levelArray = levelValue.split(SPLITTER_LEVEL);
            if (levelArray.length == 1) {
                // 如果只设置下限
                formula = var + ">=" + levelArray[0];
                formulaDesc = varDesc + ">=" + levelArray[0];
            } else {
                if (StringUtils.isNotBlank(levelArray[0])) {
                    formula = var + ">=" + levelArray[0];
                    formulaDesc = varDesc + ">=" + levelArray[0];
                }
                if (StringUtils.isNotBlank(levelArray[1])) {
                    if (!formula.isEmpty()) {
                        formula += "&&";
                        formulaDesc += "且";
                    }
                    formula += var + "<" + levelArray[1];
                    formulaDesc += varDesc + "<" + levelArray[1];
                }
            }
        }
        formulaModel.setFormula(formula);
        formulaModel.setFormulaDesc(formulaDesc);
        return formulaModel;
    }

    /**
     * 生成排除区间外表达式
     *
     * @param levelValue
     * @param var
     * @return
     */
    private FormulaModel generateExcludeFormula(String levelValue, String var, String varDesc) {
        FormulaModel formulaModel = new FormulaModel();
        String excludeFormula = "";
        String excludeFormulaDesc = "";
        // 表达式： 变量 <下限 or 变量 > 上限
        if (!SPLITTER_LEVEL.equals(levelValue)) {
            String[] levelArray = levelValue.split(SPLITTER_LEVEL);
            if (levelArray.length == 1) {
                // 如果只设置下限
                excludeFormula = var + "<" + levelArray[0];
                excludeFormulaDesc = varDesc + "<" + levelArray[0];
            } else {
                if (StringUtils.isNotBlank(levelArray[0])) {
                    excludeFormula = var + "<" + levelArray[0];
                    excludeFormulaDesc = varDesc + "<" + levelArray[0];
                }
                if (StringUtils.isNotBlank(levelArray[1])) {
                    if (!excludeFormula.isEmpty()) {
                        excludeFormula += "||";
                        excludeFormulaDesc += "或";
                    }
                    excludeFormula += var + ">=" + levelArray[1];
                    excludeFormulaDesc += varDesc + ">=" + levelArray[1];
                }
            }
        }
        formulaModel.setFormula(excludeFormula);
        formulaModel.setFormulaDesc(excludeFormulaDesc);
        return formulaModel;
    }

    /**
     * 编辑车型通用规则
     *
     * @param newModel
     * @param oldModel
     */
    private void updateVehicleModelRule(VehModelAlarmModel newModel, VehModelAlarm oldModel) {
        // 新增列表
        List<ParameterRule> levelRules = new ArrayList<>(45);
        // 待删除列表
        List<ImmutableMap<String, String>> removeRules = new ArrayList<>(15);
        // 温度差异报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.TEMPERATURE_DIFFERENCE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 电池高温报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM, newModel, oldModel, levelRules, removeRules);
        // 车载储能装置类型过压报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.ENERGY_DEVICE_OVERVOLTAGE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 车载储能装置类型欠压报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.ENERGY_DEVICE_UNDERVOLTAGE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 驱动电机控制器温度报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM, newModel, oldModel, levelRules, removeRules);
        // 驱动电机温度报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.DRIVE_MOTOR_TEMPERATURE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 单体电池过压报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.SINGLE_BATTERY_OVERVOLTAGE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 单体电池欠压报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.SINGLE_BATTERY_UNDERVOLTAGE_ALARM, newModel, oldModel, levelRules, removeRules);
        // SOC低报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.SOC_LOW_ALARM, newModel, oldModel, levelRules, removeRules);
        // SOC跳变报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.SOC_JUMP_ALARM, newModel, oldModel, levelRules, removeRules);
        // SOC过高报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.SOC_HIGH_ALARM, newModel, oldModel, levelRules, removeRules);
        // 电池单体一致性差报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.BATTERY_CONSISTENCY_ALARM, newModel, oldModel, levelRules, removeRules);
        // 车载储能装置过充报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.VEHICULAR_DEVICE_OVERCHARGE_ALARM, newModel, oldModel, levelRules, removeRules);
        // 绝缘故障报警值
        vehicleModelRuleChangeChecked(VehicleModelRuleTypeEnum.INSULATION_FAULT_ALARM, newModel, oldModel, levelRules, removeRules);
        // 将旧规则标记为删除
        removeRules.forEach(removeItem -> disableVehicleModelRule(removeItem.get("vehModelId"), removeItem.get("groupName")));
        // 重新创建规则
        levelRules.forEach(rule -> parameterRuleMapper.insert(rule));
    }

    /**
     * 车辆型号通用规则变更内容检查
     *
     * @param type       车辆型号通用报警规则类型
     * @param newModel   新值
     * @param oldModel   旧值
     * @param levelRules 变更后的规则列表
     */
    public void vehicleModelRuleChangeChecked(
        VehicleModelRuleTypeEnum type,
        VehModelAlarmModel newModel,
        VehModelAlarm oldModel,
        List<ParameterRule> levelRules,
        List<ImmutableMap<String, String>> removeRules) {

        String newValue = "";
        String oldValue = "";
        switch (type) {
            case SOC_LOW_ALARM:
                newValue = newModel.getSocLowAlarm();
                oldValue = oldModel.getSocLowAlarm();
                break;
            case SOC_HIGH_ALARM:
                newValue = newModel.getSocHighAlarm();
                oldValue = oldModel.getSocHighAlarm();
                break;
            case SOC_JUMP_ALARM:
                newValue = newModel.getSocJumpAlarm();
                oldValue = oldModel.getSocJumpAlarm();
                break;
            case INSULATION_FAULT_ALARM:
                newValue = newModel.getInsulationFaultAlarm();
                oldValue = oldModel.getInsulationFaultAlarm();
                break;
            case BATTERY_CONSISTENCY_ALARM:
                newValue = newModel.getBatteryConsistencyAlarm();
                oldValue = oldModel.getBatteryConsistencyAlarm();
                break;
            case TEMPERATURE_DIFFERENCE_ALARM:
                newValue = newModel.getTemperatureDifferenceAlarm();
                oldValue = oldModel.getTemperatureDifferenceAlarm();
                break;
            case BATTERY_HIGH_TEMPERATUR_ALARM:
                newValue = newModel.getBatteryHighTemperaturAlarm();
                oldValue = oldModel.getBatteryHighTemperaturAlarm();
                break;
            case DRIVE_MOTOR_TEMPERATURE_ALARM:
                newValue = newModel.getDriveMotorTemperatureAlarm();
                oldValue = oldModel.getDriveMotorTemperatureAlarm();
                break;
            case ENERGY_DEVICE_OVERVOLTAGE_ALARM:
                newValue = newModel.getEnergyDeviceOvervoltageAlarm();
                oldValue = oldModel.getEnergyDeviceOvervoltageAlarm();
                break;
            case ENERGY_DEVICE_UNDERVOLTAGE_ALARM:
                newValue = newModel.getEnergyDeviceUndervoltageAlarm();
                oldValue = oldModel.getEnergyDeviceUndervoltageAlarm();
                break;
            case SINGLE_BATTERY_OVERVOLTAGE_ALARM:
                newValue = newModel.getSingleBatteryOvervoltageAlarm();
                oldValue = oldModel.getSingleBatteryOvervoltageAlarm();
                break;
            case SINGLE_BATTERY_UNDERVOLTAGE_ALARM:
                newValue = newModel.getSingleBatteryUndervoltageAlarm();
                oldValue = oldModel.getSingleBatteryUndervoltageAlarm();
                break;
            case VEHICULAR_DEVICE_OVERCHARGE_ALARM:
                newValue = newModel.getVehicularDeviceOverchargeAlarm();
                oldValue = oldModel.getVehicularDeviceOverchargeAlarm();
                break;
            case DRIVE_MOTOR_CONTROLLER_TEMPERA_ALARM:
                newValue = newModel.getDriveMotorControllerTemperaAlarm();
                oldValue = oldModel.getDriveMotorControllerTemperaAlarm();
                break;
            default:
                break;
        }

        if (newValue.equals(oldValue)) {
            // 新旧值没有变化
            return;
        }

        removeRules.add(ImmutableMap.<String, String>builder()
            .put("vehModelId", newModel.getId())
            .put("groupName", type.getGroupName())
            .build());

        appendToLevelRules(type, newModel, levelRules, newValue);

    }

    private void appendToLevelRules(
        final VehicleModelRuleTypeEnum type,
        final VehModelAlarmModel newModel,
        final List<ParameterRule> levelRules,
        String levelValue) {

        // 格式：1级下限,1级上限;2级下限,2级上限;3级下限,3级上限
        String[] tmp = levelValue.split(SPLITTER_VEHICLE_RULE);
        // 格式：下限,上限
        String level1 = tmp[0].trim();
        String level2 = tmp[1].trim();
        String level3 = tmp[2].trim();

        if (!SPLITTER_LEVEL.equals(level1)) {
            // 报警级别 1
            levelRules.add(generateRule(level1, 1, type, newModel));
        }
        if (!SPLITTER_LEVEL.equals(level2)) {
            // 报警级别 2
            levelRules.add(generateRule(level2, 2, type, newModel));
        }
        if (!SPLITTER_LEVEL.equals(level3)) {
            // 报警级别 3
            levelRules.add(generateRule(level3, 3, type, newModel));
        }
    }

    private ParameterRule generateRule(
        String levelValue,
        int level,
        VehicleModelRuleTypeEnum type,
        VehModelAlarmModel vehModelAlarmModel) {

        boolean ignoreThreshold = type == VehicleModelRuleTypeEnum.SOC_JUMP_ALARM;

        // 生成表达式
        FormulaModel formula = generateFormula(levelValue, type);

        ParameterRule rule = new ParameterRule();
        rule.setId(UUID.randomUUID().toString());
        rule.setVehModelId(vehModelAlarmModel.getId());
        rule.setAlarmLevel(level);
        rule.setType(1);
        rule.setFormula(formula.getFormula());
        rule.setFormulaDisplay(formula.getFormulaDesc());
        if (!ignoreThreshold) {
            // SOC跳变不设置时间阈值
            rule.setBeginThreshold(vehModelAlarmModel.getBeginThreshold());
            rule.setEndThreshold(vehModelAlarmModel.getEndThreshold());
        }else{
            rule.setBeginThreshold(0);
            rule.setEndThreshold(0);
        }
        // 启用时间阈值
        rule.setEnableTimeThreshold(1);
        // 禁用帧数阈值
        rule.setEnableCountThreshold(0);
        rule.setBeginCountThreshold(0);
        rule.setEndCountThreshold(0);
        rule.setEnabledStatus(1);
        rule.setGroupName(type.getGroupName());
        rule.setName(type.getRuleName());
        rule.setResponseMode(vehModelAlarmModel.getResponseMode());
        rule.setCreateTime(DateUtil.getNow());
        rule.setCreateBy(ServletUtil.getCurrentUser());

        return rule;
    }

    // endregion

    /**
     * 保存数据校验
     *
     * @param newRuleModel
     */
    private void saveCheck(ParameterRuleModel newRuleModel) {
        // 名称重复校验
        Map<String, Object> params = new HashMap<>();
        params.put("name", newRuleModel.getName());
        if (StringUtils.isNotBlank(newRuleModel.getId())) {
            params.put("skipId", newRuleModel.getId());
        }
        if (0 < count(params)) {
            throw new BusinessException("规则名称已存在，操作失败");
        }

        // 公式校验
        String[] codeArray = newRuleModel.getFormulaCode().split(",");
        Matcher isNumber;
        for (String code : codeArray) {
            if (StringUtils.isBlank(code)) {
                throw new BusinessException("公式参数出现空异常, 请检查提交的公式参数!");
            } else if (!code.startsWith("d") && !OPERATOR_ARRAY.contains(code)) {
                try {
                    new BigDecimal(code);
                } catch (Exception e) {
                    throw new BusinessException("公式参数有误!请检查提交的公式参数!");
                }
                isNumber = DOUBLE_PATTERN.matcher(code);
                if (!isNumber.matches()) {
                    throw new BusinessException("插入数值仅允许长度1~8，精确到小数点后2位，支持正负数，操作失败");
                }
            }
        }

        // 至少启用一个阈值
        if(((null == newRuleModel.getEnableTimeThreshold() || newRuleModel.getEnableTimeThreshold() == 0) &&
                (null == newRuleModel.getEnableCountThreshold() || newRuleModel.getEnableCountThreshold() == 0))){
            throw new BusinessException("时间或者帧数至少启用一个阈值");
        }

        if (newRuleModel.getEnableTimeThreshold() == 1) {
            if (null == newRuleModel.getBeginThreshold() || null == newRuleModel.getEndThreshold()) {
                throw new BusinessException("已启用的阈值类型，开始阈值和结束阈值不能为空");
            }
        } else {
            newRuleModel.setBeginThreshold(null);
            newRuleModel.setEndThreshold(null);
        }
        if (newRuleModel.getEnableCountThreshold() == 1) {
            if (null == newRuleModel.getBeginCountThreshold() || null == newRuleModel.getEndCountThreshold()) {
                throw new BusinessException("已启用的阈值类型，开始阈值和结束阈值不能为空");
            }
        } else {
            newRuleModel.setBeginCountThreshold(null);
            newRuleModel.setEndCountThreshold(null);
        }
    }

    private void setVehModelName(ParameterRuleModel model, String method) {
        Map<String, String> map = vehModelService.getVehModelModelNames(model.getVehModelId());
        model.setVehModelName(map.get("vehModelName"));
        String vehModelId = map.get("vehModelId");
        if ("getMethod".equals(method)) {
            if (model.getVehModelId().length() != vehModelId.length()) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", model.getId());
                params.put("vehModelId", vehModelId);
                params.put("updateTime", DateUtil.getNow());
                params.put("updateBy", ServletUtil.getCurrentUser());
                parameterRuleMapper.updateVehModelId(params);
            }
        }
        model.setVehModelId(vehModelId);
    }
}
