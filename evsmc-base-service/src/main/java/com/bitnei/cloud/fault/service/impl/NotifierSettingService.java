package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.fault.dao.NotifierSettingMapper;
import com.bitnei.cloud.fault.dao.NotifierVehicleLkMapper;
import com.bitnei.cloud.fault.domain.NotifierSetting;
import com.bitnei.cloud.fault.domain.NotifierVehicleLk;
import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.model.NotifierRuleLkModel;
import com.bitnei.cloud.fault.model.NotifierSettingModel;
import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.AreaModel;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IAreaService;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.impl.UserService;
import com.bitnei.cloud.sys.service.impl.VehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierSettingService实现<br>
 * 描述： NotifierSettingService实现<br>
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
 * <td>2019-03-06 11:31:31</td>
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
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.NotifierSettingMapper")
public class NotifierSettingService extends BaseService implements INotifierSettingService {

    @Resource
    private NotifierVehicleLkMapper notifierVehicleLkMapper;

    @Autowired
    private NotifierRuleLkService notifierRuleLkService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private VehicleService vehicleService;

    @Resource
    private NotifierSettingMapper notifierSettingMapper;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private UserService userService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if(params.containsKey("ids")) {
            String ids = (String) params.get("ids");
            params.put("ids", StringUtils.split(ids,","));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<NotifierSetting> entries = findBySqlId("pagerModel", params);
            List<NotifierSettingModel> models = new ArrayList<>();
            for (NotifierSetting entry : entries) {
                NotifierSettingModel model = NotifierSettingModel.fromEntry(entry);
                //故障类型
                model.setFaultTypeDisplay(injectData(model.getFaultType(), "FAULT_TYPE"));
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<NotifierSettingModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                NotifierSetting obj = (NotifierSetting) entry;
                NotifierSettingModel model = NotifierSettingModel.fromEntry(obj);
                //故障类型
                model.setFaultTypeDisplay(injectData(model.getFaultType(), "FAULT_TYPE"));
                //分配车辆数
                model.setAllocateVehicleCount(notifierVehicleLkMapper.allocateVehicleCount(model.getId()));
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object notifiers(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<NotifierSetting> entries = findBySqlId("notifiers", params);
            List<NotifierSettingModel> models = new ArrayList<>();
            for (NotifierSetting entry : entries) {
                NotifierSettingModel model = NotifierSettingModel.fromEntry(entry);
                //故障类型
                model.setFaultTypeDisplay(injectData(model.getFaultType(), "FAULT_TYPE"));
                //分配车辆数
                model.setAllocateVehicleCount(notifierVehicleLkMapper.allocateVehicleCount(model.getId()));
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("notifiers", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<NotifierSettingModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                NotifierSetting obj = (NotifierSetting) entry;
                NotifierSettingModel model = NotifierSettingModel.fromEntry(obj);
                //故障类型
                model.setFaultTypeDisplay(injectData(model.getFaultType(), "FAULT_TYPE"));
                //分配车辆数
                model.setAllocateVehicleCount(notifierVehicleLkMapper.allocateVehicleCount(model.getId()));
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public NotifierSettingModel get(String id) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
        params.put("id", id);
        NotifierSetting entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        NotifierSettingModel model = NotifierSettingModel.fromEntry(entry);
        model.setAllocateVehicleCount(notifierVehicleLkMapper.allocateVehicleCount(id));
        //故障类型
        model.setFaultTypeDisplay(injectData(model.getFaultType(), "FAULT_TYPE"));
        // 获取关联规则
        model.setParameterRuleList(notifierRuleLkService.list(id, FaultTypeEnum.PARAMETER));
        model.setCodeRuleList(notifierRuleLkService.list(id, FaultTypeEnum.CODE));
        model.setEnclosureRuleList(notifierRuleLkService.list(id, FaultTypeEnum.ENCLOSURE));
        return model;
    }

    @Override
    public List<String> findForAlarm(Map<String, Object> params) {
        return notifierSettingMapper.findForAlarm(params);
    }

    @Override
    public List<String> findForRisk(Map<String, Object> params) {
        return notifierSettingMapper.findForRisk(params);
    }

    private String injectData(String type, String dictType) {
        List<DictModel> dictModels = dictService.findByDictType(dictType);
        String[] str = type.split(",");
        String display = "";
        for (String value : str) {
            for (DictModel dictModel : dictModels) {
                if (value.equals(dictModel.getValue())) {
                    display = display + dictModel.getName() + ",";
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(display)) {
            display = display.substring(0, display.length() - 1);
        }
        return display;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(NotifierSettingModel model) {
        saveCheck(model);
        NotifierSetting obj = new NotifierSetting();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        obj.setAreaNames(getAreaNames(obj.getAreaIds()));
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        saveRuleLk(id, model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NotifierSettingModel model) {
        saveCheck(model);
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
        NotifierSetting obj = new NotifierSetting();
        BeanUtils.copyProperties(model, obj);
        obj.setAreaNames(getAreaNames(obj.getAreaIds()));
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        saveRuleLk(model.getId(), model);
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

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
            // 同时删除分配的车辆
            notifierVehicleLkMapper.deleteByNotifierId(id);
            // 删除规则关联信息
            notifierRuleLkService.deleteByNotifierId(id);
        }
        return count;
    }


    /**
     * 同步账号车辆
     *
     * @param notifierSetting
     */
    private void syncVehicle(NotifierSetting notifierSetting) {
        if (StringUtils.isNotBlank(notifierSetting.getUserId())) {
            String userId = notifierSetting.getUserId();
            String notifierId = notifierSetting.getId();
            // 账号下的车辆
            List<String> userVehicleList = vehicleService.getVehicleIdsByUserId(userId);
            notifierVehicleLkMapper.deleteByNotifierId(notifierId);
            NotifierVehicleLk obj = null;
            for (String userVehicleId : userVehicleList) {
                obj = new NotifierVehicleLk();
                obj.setId(UtilHelper.getUUID());
                obj.setCreateTime(DateUtil.getNow());
                obj.setCreateBy(ServletUtil.getCurrentUser());
                obj.setNotifierId(notifierId);
                obj.setVehicleId(userVehicleId);

                notifierVehicleLkMapper.insert(obj);
            }
            log.info("===============已同步故障负责人关联帐号[{}]车辆：{}辆", userId, userVehicleList.size());
        }
    }

    @Override
    public void syncNotifierVehicle() {
        //获取当权限的map;  关联平台账号、同步账号下车辆、启用状态
        Map<String, Object> params = new HashMap<>();
        params.put("relationUserStatus", 1);
        params.put("syncVehicleStatus", 1);
        params.put("enabledStatus", 1);

        List<NotifierSetting> entries = findBySqlId("pagerModel", params);
        log.info("===============同步故障负责人账号下的车辆开始==============={}", DateUtil.getNow());
        log.info("========共======={}条", entries.size());
        for (NotifierSetting notifierSetting : entries) {
            syncVehicle(notifierSetting);
        }
        log.info("===============同步故障负责人账号下的车辆结束==============={}", DateUtil.getNow());
    }

    @Override
    public NotifierSettingModel getNotifierByUserIdAndVehicleId(String vehicleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
        params.put("vehicleId", vehicleId);
        TokenInfo tokenInfo = ServletUtil.getCurrentTokenInfo();
        if (null == tokenInfo) {
            throw new BusinessException("获取CurrentTokenInfo为空,操作失败");
        }
        params.put("userId", tokenInfo.getUserId());
        NotifierSetting notifierSetting = notifierSettingMapper.getNotifierByUserIdAndVehicleId(params);
        NotifierSettingModel model = NotifierSettingModel.fromEntry(notifierSetting);
        return model;
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "NOTIFIERSETTING" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<NotifierSettingModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(NotifierSettingModel model) {
                List<String> errorMsgList = new ArrayList<>();
                StringBuffer sb;
                String[] array;
                Integer value;
                // 验证区域名称有效性
                if (StringUtils.isNotBlank(model.getAreaNames())) {
                    array = model.getAreaNames().split(",");
                    AreaModel areaModel;
                    sb = new StringBuffer();
                    for (String areaName : array) {
                        try {
                            areaModel = areaService.getByName(areaName.trim());
                            sb.append(areaModel.getId()).append(",");
                        } catch (Exception e) {
                            errorMsgList.add("区域名称[" + areaName + "]不存在");
                        }
                    }
                    if (0 < sb.length()) {
                        model.setAreaIds(sb.substring(0, sb.length() - 1));
                    }
                }

                // 验证推送消息类型(故障类型)有效性
                if (StringUtils.isNotBlank(model.getFaultTypeDisplay())) {
                    array = model.getFaultTypeDisplay().split(",");
                    sb = new StringBuffer();
                    for (String type : array) {
                        value = FaultTypeEnum.getValueByDesc(type.trim());
                        if (null != value) {
                            sb.append(value.toString()).append(",");
                            // 设置相应类型规则关联
                            NotifierRuleLkModel lk = new NotifierRuleLkModel();
                            lk.setRuleId("all");
                            if (FaultTypeEnum.PARAMETER.getValue() == value.intValue()) {
                                if (Constants.BoolType.yes.getDesc().equals(model.getParameterRuleDisplay())) {
                                    model.setParameterRuleList(Lists.newArrayList(lk));
                                }else if(StringUtils.isNotBlank(model.getParameterRuleDisplay())
                                        && !Constants.BoolType.no.getDesc().equals(model.getParameterRuleDisplay())){
                                    errorMsgList.add("'是否关联所有参数异常报警规则名称'列内容有误, 仅允许输入: 是、否");
                                }
                            }
                            if (FaultTypeEnum.CODE.getValue() == value.intValue()) {
                                if (Constants.BoolType.yes.getDesc().equals(model.getCodeRuleDisplay())) {
                                    model.setCodeRuleList(Lists.newArrayList(lk));
                                }else if(StringUtils.isNotBlank(model.getCodeRuleDisplay())
                                        && !Constants.BoolType.no.getDesc().equals(model.getCodeRuleDisplay())){
                                    errorMsgList.add("'是否关联所有故障码报警规则'列内容有误, 仅允许输入: 是、否");
                                }
                            }
                            if (FaultTypeEnum.ENCLOSURE.getValue() == value.intValue()) {
                                if (Constants.BoolType.yes.getDesc().equals(model.getEnclosureRuleDisplay())) {
                                    model.setEnclosureRuleList(Lists.newArrayList(lk));
                                }else if(StringUtils.isNotBlank(model.getEnclosureRuleDisplay())
                                        && !Constants.BoolType.no.getDesc().equals(model.getEnclosureRuleDisplay())){
                                    errorMsgList.add("'是否关联所有电子围栏报警名称'列内容有误, 仅允许输入: 是、否");
                                }
                            }
                        } else {
                            errorMsgList.add("推送消息类型[" + type + "]不存在");
                        }
                    }
                    if (0 < sb.length()) {
                        model.setFaultType(sb.substring(0, sb.length() - 1));
                    }
                }

                // 是否关联平台账号有效性校验
                if (StringUtils.isNotBlank(model.getRelationUserDisplay())) {
                    value = Constants.BoolType.getValue(model.getRelationUserDisplay().trim());
                    if (null != value) {
                        model.setRelationUserStatus(value);
                        if (Constants.BoolType.yes.getValue() == value) {
                            // 平台账号有效性校验
                            if (StringUtils.isNotBlank(model.getUserName())) {
                                try {
                                    UserModel user = userService.findByUsername(model.getUserName().trim());
                                    model.setUserId(user.getId());
                                } catch (Exception e) {
                                    errorMsgList.add("用户名[" + model.getUserName() + "]不存在");
                                }
                            }

                            // 是否同步账号下车辆有效性校验
                            if (StringUtils.isNotBlank(model.getSyncVehicleDisplay())) {
                                value = Constants.BoolType.getValue(model.getSyncVehicleDisplay().trim());
                                if (null != value) {
                                    model.setSyncVehicleStatus(value);
                                } else {
                                    errorMsgList.add("是否同步账号下车辆有误");
                                }
                            }
                        }
                        try {
                            saveCheck(model);
                        } catch (Exception e) {
                            errorMsgList.add(e.getMessage());
                        }
                    } else {
                        errorMsgList.add("是否关联平台账号有误");
                    }
                }

                // 是否启用有效性校验
                if (StringUtils.isNotBlank(model.getEnabledStatusDisplay())) {
                    value = Constants.BoolType.getValue(model.getEnabledStatusDisplay().trim());
                    if (null != value) {
                        model.setEnabledStatus(value);
                    } else {
                        errorMsgList.add("是否启用有误");
                    }
                }

                return errorMsgList;
            }

            /**
             * 保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(NotifierSettingModel model) {
                insert(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("报警提醒推送配置导入模板.xls", NotifierSettingModel.class);
    }

    /**
     * 获取行政区域名称字符串
     *
     * @param areaIds
     * @return
     */
    private String getAreaNames(String areaIds) {
        StringBuffer areaNameSb = new StringBuffer();
        if (StringUtils.isNotBlank(areaIds)) {
            AreaModel areaModel = null;
            String[] areaIdArray = areaIds.split(",");
            for (int i = 0; i < areaIdArray.length; i++) {
                areaModel = areaService.get(areaIdArray[i].trim());
                if (null != areaModel) {
                    areaNameSb.append(areaModel.getName());
                    if (i < (areaIdArray.length - 1)) {
                        areaNameSb.append(",");
                    }
                }
            }
        }
        return areaNameSb.toString();
    }

    /**
     * 保存校验
     *
     * @param model
     */
    private void saveCheck(NotifierSettingModel model) {
        // 如果设置了关联账号
        if (Constants.BoolType.yes.getValue() == model.getRelationUserStatus().intValue()) {
            if (StringUtils.isBlank(model.getUserId())) {
                throw new BusinessException("用户账号不能为空");
            }
            // 获取当权限的map
            Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_setting", "fns");
            params.put("userId", model.getUserId());
            if (StringUtils.isNotBlank(model.getId())) {
                params.put("skipId", model.getId());
            }
            int count = notifierSettingMapper.count(params);
            if (0 < count) {
                throw new BusinessException("用户账号已经被关联了");
            }
        } else {
            // 不关联账号则清空账号信息和不同步
            model.setUserId("");
            model.setSyncVehicleStatus(Constants.BoolType.no.getValue());
        }

    }

    /**
     * 保存关联规则
     *
     * @param notifierId 负责人ID
     * @param model      车辆负责人Model
     */
    private void saveRuleLk(String notifierId, NotifierSettingModel model) {
        // 删除原关联数据
        notifierRuleLkService.deleteByNotifierId(notifierId);
        // 故障类型包含参数异常,并且关联参数规则集合不为空, 则保存关联参数规则
        if (model.getFaultType().contains(String.valueOf(FaultTypeEnum.PARAMETER.getValue())) && CollectionUtils.isNotEmpty(model.getParameterRuleList())) {
            List<String> parameterRuleIdList = model.getParameterRuleList().stream().map(NotifierRuleLkModel::getRuleId).collect(Collectors.toList());
            notifierRuleLkService.save(notifierId, FaultTypeEnum.PARAMETER.getValue(), parameterRuleIdList);
        }
        // 故障类型包含故障码,并且关联参数规则集合不为空, 则保存关联故障码规则
        if (model.getFaultType().contains(String.valueOf(FaultTypeEnum.CODE.getValue())) && CollectionUtils.isNotEmpty(model.getCodeRuleList())) {
            List<String> codeRuleIdList = model.getCodeRuleList().stream().map(NotifierRuleLkModel::getRuleId).collect(Collectors.toList());
            notifierRuleLkService.save(notifierId, FaultTypeEnum.CODE.getValue(), codeRuleIdList);

        }
        // 故障类型包含电子围栏,并且关联参数规则集合不为空, 则保存关联围栏规则
        if (model.getFaultType().contains(String.valueOf(FaultTypeEnum.ENCLOSURE.getValue())) && CollectionUtils.isNotEmpty(model.getEnclosureRuleList())) {
            List<String> enclosureRuleIdList = model.getEnclosureRuleList().stream().map(NotifierRuleLkModel::getRuleId).collect(Collectors.toList());
            notifierRuleLkService.save(notifierId, FaultTypeEnum.ENCLOSURE.getValue(), enclosureRuleIdList);

        }
    }
}
