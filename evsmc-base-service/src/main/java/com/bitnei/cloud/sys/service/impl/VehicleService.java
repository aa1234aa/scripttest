package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.StringUtilExs;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.constant.ResourceConstant;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.dc.model.RuleModel;
import com.bitnei.cloud.dc.service.IForwardRuleService;
import com.bitnei.cloud.dc.service.IRuleService;
import com.bitnei.cloud.fault.dao.NotifierVehicleLkMapper;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.*;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.cloud.sys.util.RandomUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jodd.util.StringUtil;
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
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleService实现<br>
 * 描述： VehicleService实现<br>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleMapper")
@RequiredArgsConstructor
public class VehicleService extends BaseService implements IVehicleService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Resource
    private IVehicleDriveDeviceLkService driveDeviceLkService;
    @Resource
    private IVehicleEngeryDeviceLkService engeryDeviceLkService;
    @Resource
    private IVehiclePowerDeviceLkService powerDeviceLkService;
    @Resource
    private VehicleStageLogMapper vehicleStageLogMapper;
    @Resource
    private IDriveDeviceService driveDeviceService;
    @Resource
    private IEngeryDeviceService engeryDeviceService;
    @Resource
    private IPowerDeviceService powerDeviceService;
    @Resource
    private IVehModelService vehModelService;
    @Resource
    private ITermModelUnitService termModelUnitService;
    @Resource
    private IUnitService unitService;
    @Resource
    private NotifierVehicleLkMapper notifierVehicleLkMapper;
    @Resource
    private IRuleService ruleService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private DictMapper dictMapper;
    @Resource
    private IDriveMotorModelService driveMotorModelService;
    @Resource
    private IAlarmInfoService alarmInfoService;
    @Resource
    private ITermModelService termModelService;
    @Resource
    private IOfflineExportService offlineExportService;
    @Autowired
    private IForwardRuleService forwardRuleService;
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupResourceRuleMapper groupResourceRuleMapper;


    private final RealDataClient realDataClient;

    // 在线
    public static final int ONLINE = 1;
    // 离线
    public static final int OFFLINE = 2;


    @Override
    public Object vehicleList(PagerInfo pagerInfo) {

        return vehicleList(pagerInfo, new HashMap<>());
    }

    /**
     * 车辆列表查询
     *
     * @param pagerInfo PagerInfo
     * @param extraMap
     * @return
     */
    @Override
    public Object vehicleList(PagerInfo pagerInfo, Map<String, Object> extraMap) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.putAll(extraMap);
        String vehicleIds = "vehicleIds";
        if (params.containsKey(vehicleIds)) {
            String vehModelIds = (String) params.get(vehicleIds);
            params.put(vehicleIds, StringUtils.split(vehModelIds, ","));
        }
        if (params.containsKey("vehModelIds")) {
            params.put("vehModelIds", StringUtils.split(params.get("vehModelIds").toString(), ","));
        }
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<Vehicle> entries = findBySqlId("findPagerModel", params);
            return models(entries);

        } else { //分页查询
            PagerResult pr = findPagerModel("findPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehicleModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                Vehicle obj = (Vehicle) entry;
                int count = notifierVehicleLkMapper.allocateCount(obj.getId());
                VehicleModel model = VehicleModel.fromEntry(obj);
                model.setFaultNoticesCount(count);
                models.add(model);
            }
            pr.setData(Lists.newArrayList(models));
            return pr;
        }
    }


    /**
     * 用户权限车辆
     *
     * @param userId
     * @param pagerInfo
     * @return
     */
    @Override
    public Object userVehicles(String userId, PagerInfo pagerInfo) {

        //获取用户
        Map<String, Object> userParams = Maps.newHashMap();
        userParams.put("id", userId);
        User user = userMapper.findById(userParams);
        //获取用户的默认组车辆
        String selectVal = groupResourceRuleMapper.getGroupRuleValByUser(userId, ResourceConstant.RESOURCE_ITEM_SELF_ID);
        Set<String> valSet = Sets.newHashSet();
        valSet.addAll(StringUtilExs.spiltString(selectVal, ","));


        Map<String, Object> params = Maps.newHashMap();
        String authSql = DataAccessKit.getUserAuthSql(userId, "sys_vehicle", "sv", false);
        String currentUserAuthSql = DataAccessKit.getAuthSQL("sys_vehicle", "sv");
        if (StringUtil.isNotEmpty(currentUserAuthSql)) {
            authSql = String.format("(%s) and (%s)", authSql, currentUserAuthSql);
        }
        params.put("authSQL", authSql);
        Object result = vehicleList(pagerInfo, params);
        if (result instanceof PagerResult) {
            PagerResult pr = (PagerResult) result;
            for (int i = 0; i < pr.getData().size(); i++) {
                VehicleModel vehicleModel = (VehicleModel) pr.getData().get(i);
                Integer addModel = 0;
                if (user.getUsername().equals(vehicleModel.getCreateBy())) {
                    addModel = 0;
                } else if (valSet.contains(vehicleModel.getId())) {
                    addModel = 1;
                } else {
                    addModel = 2;
                }
                vehicleModel.setAddSource(addModel);
            }
        }
        return result;
    }

    /**
     * 非用户权限车辆
     *
     * @param userId
     * @param pagerInfo
     * @return
     */
    @Override
    public Object userVehiclesNot(String userId, PagerInfo pagerInfo) {

        Map<String, Object> params = Maps.newHashMap();
        String splitStr = "and   not exists";
        String authNotSql = DataAccessKit.getUserNotAuthSql(userId, "sys_vehicle", "sv");
        if (authNotSql.contains(splitStr)) {
            authNotSql = authNotSql.replaceAll(splitStr, "or exists");
        }

        String currentUserAuthSql = DataAccessKit.getAuthSQL("sys_vehicle", "sv");
        if (StringUtil.isNotEmpty(currentUserAuthSql)) {
            authNotSql = String.format("(%s) and (%s)", authNotSql, currentUserAuthSql);
        }
        params.put("authSQL", authNotSql);
        return vehicleList(pagerInfo, params);
    }


    @Override
    public PagerResult vehicleListByBaseInfo(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        //分页查询
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        PagerResult pr = findPagerModel("findPagerModelByBaseInfo", params,
                pagerInfo.getStart(), pagerInfo.getLimit());
        List<VehicleModel> models = Lists.newArrayList();
        for (Object entry : pr.getData()) {
            Vehicle obj = (Vehicle) entry;
            VehicleModel model = VehicleModel.fromEntry(obj);
            model.setFaultNoticesCount(notifierVehicleLkMapper.allocateCount(obj.getId()));
            models.add(model);
        }
        pr.setData(Collections.singletonList(models));
        return pr;
    }

    private List<VehicleModel> models(List<Vehicle> entries) {
        List<VehicleModel> models = new ArrayList<>();
        for (Vehicle entry : entries) {
            int count = notifierVehicleLkMapper.allocateCount(entry.getId());
            VehicleModel model = VehicleModel.fromEntry(entry);
            model.setFaultNoticesCount(count);
            models.add(model);
        }
        return models;
    }

    @Override
    public List<VehicleModel> vehicleListByBaseInfoList(List<String> vins, List<String> interNos,
                                                        List<String> licensePlates, List<String> iccids) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.put("vins", vins);
        params.put("interNos", interNos);
        params.put("licensePlates", licensePlates);
        params.put("iccids", iccids);
        List<Vehicle> entries = findBySqlId("findPagerModelByBaseInfoList", params);
        return models(entries);
    }


    @Override
    public List<VehicleModel> list(Map<String, Object> params) {
        List<Vehicle> entries = findBySqlId("pagerModel", params);
        List<VehicleModel> models = new ArrayList<>();
        for (Vehicle entry : entries) {
            models.add(VehicleModel.fromEntry(entry));
        }
        return models;

    }


    @Override
    public VehicleModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id", id);
        Vehicle entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }

        VehicleModel vehicleModel = VehicleModel.fromEntry(entry);

        // 生产阶段记录集
        Map<String, Object> stageParams = ImmutableMap.of("vehicleId", id);
        List<VehicleStageLog> vehicleStageLogs = vehicleStageLogMapper.pagerModel(stageParams);
        List<VehicleStageLogModel> models = Lists.newArrayList();
        for (VehicleStageLog stageLog : vehicleStageLogs) {
            VehicleStageLogModel model1 = VehicleStageLogModel.fromEntry(stageLog);
            models.add(model1);
        }
        vehicleModel.setStageLogs(models);
        // 驱动装置
        List<VehicleDriveDeviceLkModel> list1 = this.driveDeviceLkService.listByVehicleId(entry.getId(), null);
        if (list1 != null && list1.size() > 0) {
            String driveDeviceIds = list1.stream().map(VehicleDriveDeviceLkModel::getDrvieDeviceId).collect(Collectors.joining(","));
            String driveDeviceIdCodes = list1.stream().map(VehicleDriveDeviceLkModel::getDrvieDeviceCode).collect(Collectors.joining(","));
            vehicleModel.setDriveDeviceIds(driveDeviceIds);
            vehicleModel.setDriveDeviceCodes(driveDeviceIdCodes);
        }
        // 可充电储能装置
        List<VehicleEngeryDeviceLkModel> list2 = this.engeryDeviceLkService.listByVehicleId(entry.getId());
        if (list2 != null && list2.size() > 0) {
            String engeryDeviceIds = list2.stream().map(VehicleEngeryDeviceLkModel::getEngeryDeviceId).collect(Collectors.joining(","));
            String engeryDeviceNames = list2.stream().map(VehicleEngeryDeviceLkModel::getEngeryDeviceName).collect(Collectors.joining(","));
            vehicleModel.setEngeryDeviceIds(engeryDeviceIds);
            vehicleModel.setEngeryDeviceNames(engeryDeviceNames);
        }
        // 发电装置
        List<VehiclePowerDeviceLkModel> list3 = this.powerDeviceLkService.listByVehicleId(entry.getId(), null);
        if (list3 != null && list3.size() > 0) {
            String powerDeviceIds = list3.stream().map(VehiclePowerDeviceLkModel::getPowerDeviceId).collect(Collectors.joining(","));
            String powerDeviceCodes = list3.stream().map(VehiclePowerDeviceLkModel::getPowerDeviceCode).collect(Collectors.joining(","));
            vehicleModel.setPowerDeviceIds(powerDeviceIds);
            vehicleModel.setPowerDeviceCodes(powerDeviceCodes);
        }
        return vehicleModel;

    }


    @Override
    public VehicleModel getByVin(String vin) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", vin);
        Vehicle entry = unique("findByVin", params);
        if (entry == null) {
            throw new BusinessException("VIN码不存在");
        }
        return VehicleModel.fromEntry(entry);
    }


    @Override
    public VehicleModel getByIdSimple(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id", id);
        Vehicle entry = unique("findByIdSimple", params);
        if (entry == null) {
            throw new BusinessException("车辆已不存在");
        }
        return VehicleModel.fromEntry(entry);
    }

    @Override
    public VehicleModel getByVinOrNull(String vin) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", vin);
        Vehicle entry = unique("findByVin", params);
        if (entry == null) {
            return null;
        }
        return VehicleModel.fromEntry(entry);
    }

    public VehicleModel getByVinValidateAuth(String vin) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", vin);
        Vehicle entry = unique("findByVinValidateAuth", params);
        if (entry == null) {
            return null;
        }
        return VehicleModel.fromEntry(entry);
    }

    @Override
    public VehicleWithOnlineStatus getVehicleWithStatusByVin(String vin) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", vin);
        Vehicle entry = unique("findByVin", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        VehicleWithOnlineStatus vehicleWithOnlineStatus = VehicleWithOnlineStatus.fromEntry(entry);
        vehicleWithOnlineStatus.setOnlineStatus(getVehicleOnlineStatus(vin));
        return vehicleWithOnlineStatus;
    }

    @Override
    public VehicleModel getByUuid(String uuid) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("uuid", uuid);
        Vehicle entry = unique("findByUuid", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehicleModel.fromEntry(entry);
    }

    @Override
    public VehicleModel getByLicensePlate(String licensePlate) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("licensePlate", licensePlate);
        Vehicle entry = unique("findByLicensePlate", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehicleModel.fromEntry(entry);
    }

    @Override
    public VehicleModel getByInterNo(String interNo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("interNo", interNo);
        Vehicle entry = unique("findByInterNo", params);
        if (entry == null) {
            throw new BusinessException("内部编号不存在");
        }
        return VehicleModel.fromEntry(entry);
    }

    @Override
    public VehicleModel getByOperInterNo(String operInterNo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("operInterNo", operInterNo);
        Vehicle entry = unique("findByOperInterNo", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehicleModel.fromEntry(entry);
    }


    private Vehicle findByTermId(String termId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("termId", termId);
        return unique("findByTermId", params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(VehicleModel model) {

        String errorInfo = validaModel(model);
        if (errorInfo != null) {
            throw new BusinessException(errorInfo);
        }
        Vehicle obj = new Vehicle();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        // 系统内部编号 使用uuid
        String createTime = DateUtil.getNow();
        String uuid = UtilHelper.getUUID();
        String createBy = ServletUtil.getCurrentUser();
        obj.setVin(StringUtils.upperCase(obj.getVin()));
        obj.setId(id);
        obj.setUuid(uuid);
        obj.setCreateTime(createTime);
        obj.setCreateBy(createBy);
        // 生产阶段处理
        obj.setStage(Vehicle.STAGE_TYPE_ENUM.PRODUCTION.getValue());
        if (StringUtils.isNotEmpty(obj.getFactoryDate())) {
            obj.setStage(Vehicle.STAGE_TYPE_ENUM.STORAGE.getValue());
        }
        obj.setStageChangeDate(createTime);
        // 申报状态默认为未申报,申报次数为0
        obj.setSubsidyApplyStatus(Constant.SUBSIDY_APPLY_STATUS.UNDECLARED);
        obj.setSusidyApplyCount(0);

        int res = super.insert(obj);

        // 新增生产阶段记录
        this.addStageLog(id, Vehicle.STAGE_TYPE_ENUM.PRODUCTION.getValue(), obj.getProduceDate());
        // 新增入库阶段记录
        this.addStageLog(id, Vehicle.STAGE_TYPE_ENUM.STORAGE.getValue(), obj.getFactoryDate());
        // 新增零配件信息
        this.batchAddEngeryDeviceLk(model.getEngeryDeviceIds(), createBy, createTime, id);
        this.batchAddPowerDeviceLK(model.getPowerDeviceIds(), createBy, createTime, id);
        this.batchAddDriveDeviceLk(model.getDriveDeviceIds(), createBy, createTime, id);

        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    /**
     * 新增生产阶段记录
     */
    private void addStageLog(String vehicleId, String stageName, String createTime) {
        VehicleStageLog stageLog = new VehicleStageLog();
        stageLog.setId(UtilHelper.getUUID());
        stageLog.setVehicleId(vehicleId);
        stageLog.setStageName(stageName);
        stageLog.setCreateTime(createTime);
        stageLog.setCreateBy(ServletUtil.getCurrentUser());
        stageLog.setUpdateTime(DateUtil.getNow());
        stageLog.setUpdateBy(ServletUtil.getCurrentUser());
        vehicleStageLogMapper.insert(stageLog);
    }

    /**
     * 更新车辆阶段记录
     */
    private void updateStageLog(String vehicleId, String stageName, String createTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicleId", vehicleId);
        params.put("stageName", stageName);
        VehicleStageLog stageLog = vehicleStageLogMapper.findByVehIdAndStage(params);
        if (null==stageLog){
            addStageLog(vehicleId,stageName,createTime);
            return;
        }
        stageLog.setCreateTime(createTime);
        stageLog.setCreateBy(ServletUtil.getCurrentUser());
        stageLog.setUpdateTime(DateUtil.getNow());
        stageLog.setUpdateBy(ServletUtil.getCurrentUser());
        vehicleStageLogMapper.update(stageLog);
    }


    private String validaModel(VehicleModel model) {
        // 验证VIN码是否存在
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", model.getVin());
        Vehicle entry = unique("uniqueFindByVin", params);
        if (entry != null && !StringUtils.equals(entry.getId(), model.getId())) {
            return "VIN码已存在";
        }
        // 验证内部编号是否存在
        params.put("interNo", model.getInterNo());
        Vehicle v = unique("findByInterNo", params);
        if (v != null && !StringUtils.equals(v.getId(), model.getId())) {
            return "内部编号已存在";
        }
        // 验证车辆型号是否存在
        VehModelModel vehModelModel;
        try {
            vehModelModel = vehModelService.get(model.getVehModelId());
        } catch (Exception e) {
            return "车辆型号不存在";
        }
        // 验证终端编号是否存在
        TermModelUnitModel unitModel;
        Vehicle vehicle = findByTermId(model.getTermId());
        if (vehicle != null && !vehicle.getId().equals(model.getId())) {
            return "终端编号已被关联选择";
        }
        try {
            unitModel = termModelUnitService.get(model.getTermId());
        } catch (Exception e) {
            return "终端编号不存在";
        }
        // 验证车型中协议类型与终端中协议类型是否一致
        RuleModel ruleModel = ruleService.get(unitModel.getSupportProtocol());
        if (!ruleModel.getRuleTypeId().equals(vehModelModel.getRuleId())) {
            return "协议类型不一致";
        }
        // 验证生产企业是否存在
        if (StringUtils.isNotBlank(model.getManuUnitId())) {
            try {
                unitService.get(model.getManuUnitId());
            } catch (Exception e) {
                return "车辆生产企业不存在";
            }
        }
        return null;
    }

    @Override
    public void update(VehicleModel model) {
        String errorInfo = validaModel(model);
        if (errorInfo != null) {
            throw new BusinessException(errorInfo);
        }
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id", model.getId());
        Vehicle entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("车辆记录不存在");
        }

        Vehicle obj = new Vehicle();
        BeanUtils.copyProperties(model, obj);
        // 处理vin码转换为大写
        obj.setVin(StringUtils.upperCase(obj.getVin()));
        String updateTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        obj.setUpdateTime(updateTime);
        obj.setUpdateBy(updateBy);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        Map<String, Object> delMap = ImmutableMap.of("vehicleId", model.getId());
        engeryDeviceLkService.deleteByMap(delMap);
        this.batchAddEngeryDeviceLk(model.getEngeryDeviceIds(), updateBy, updateTime, model.getId());
        powerDeviceLkService.deleteByMap(delMap);
        this.batchAddPowerDeviceLK(model.getPowerDeviceIds(), updateBy, updateTime, model.getId());
        driveDeviceLkService.deleteByMap(delMap);
        this.batchAddDriveDeviceLk(model.getDriveDeviceIds(), updateBy, updateTime, model.getId());

        // 更新生产阶段记录
        this.updateStageLog(model.getId(), Vehicle.STAGE_TYPE_ENUM.PRODUCTION.getValue(), obj.getProduceDate());
        // 更新入库阶段记录
        this.updateStageLog(model.getId(), Vehicle.STAGE_TYPE_ENUM.STORAGE.getValue(), obj.getFactoryDate());

        if (res == 0) {
            throw new BusinessException("更新失败");
        }

    }

    /**
     * 删除多个
     *
     * @param ids id集
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            // 逻辑删除 is_delete=1, VIN后面加上(已删除) 清空终端,可充电储能系统编码, 驱动装置编码, 发电装置编码关联
            int r = super.deleteByMap(params);
            if (r > 0) {
                Map<String, Object> delMap = ImmutableMap.of("vehicleId", id);
                engeryDeviceLkService.deleteByMap(delMap);
                powerDeviceLkService.deleteByMap(delMap);
                driveDeviceLkService.deleteByMap(delMap);
                // 停止车辆报警
                alarmInfoService.stopAlarmByVehicleId(id, "(删除车辆结束)");
            }
            count += r;
        }
        if (count > 0) {
            //车辆删除后，同步更新规则类型为“列表选择”的规则明细val值
            forwardRuleService.updateForwardRuleRuleItemVal(ids);
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<Vehicle>(this, "findPagerModel", params, "sys/res/vehicle/export.xls", "车辆列表") {
        }.work();
    }

    @Override
    public void nationExport(PagerInfo pagerInfo) {
        try {
            //获取权限sql
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
            params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
            long startTime = System.currentTimeMillis();
            // 获取车辆数据
            List<Vehicle> entries = findBySqlId("findPagerModel", params);
            // A-1 运营单位信息
            ExcelData unitExcelData = new ExcelData();
            unitExcelData.setListRowIndex(3);
            // A-2车型参数与配置
            ExcelData modelExcelData = new ExcelData();
            modelExcelData.setListRowIndex(7);
            // A-3储能装置电池包（箱）型号
            ExcelData engeryExcelData = new ExcelData();
            engeryExcelData.setListRowIndex(3);
            // A-4驱动电机型号
            ExcelData motorExcelData = new ExcelData();
            motorExcelData.setListRowIndex(3);
            // A-5终端型号
            ExcelData termModelExcelData = new ExcelData();
            termModelExcelData.setListRowIndex(3);
            // B-1车辆信息
            ExcelData vehicleExcelData = new ExcelData();
            vehicleExcelData.setListRowIndex(4);
            // B-2储能装置电池包
            ExcelData batteryPackExcelData = new ExcelData();
            batteryPackExcelData.setListRowIndex(3);
            // B-3驱动电机
            ExcelData driveExcelData = new ExcelData();
            driveExcelData.setListRowIndex(3);
            // B-4终端
            ExcelData termExcelData = new ExcelData();
            termExcelData.setListRowIndex(3);
            /** 组装数据 **/
            Map<String, String> map = Maps.newHashMap();
            StringBuilder operUnitIds = new StringBuilder();
            StringBuilder vehModelIds = new StringBuilder();
            StringBuilder termIds = new StringBuilder();
            for (Vehicle e : entries) {
                // A-1 运营单位信息
                if (StringUtils.isNotBlank(e.getOperUnitId()) && !map.containsKey(e.getOperUnitId())) {
                    operUnitIds.append(e.getOperUnitId()).append(",");
                    map.put(e.getOperUnitId(), e.getOperUnitId());
                }
                // A-2车型参数与配置Data
                if (StringUtils.isNotBlank(e.getVehModelId()) && !map.containsKey(e.getVehModelId())) {
                    vehModelIds.append(e.getVehModelId()).append(",");
                    map.put(e.getVehModelId(), e.getVehModelId());
                }
                // A-3储能装置电池包（箱）型号Data   // B-2储能装置电池包（箱）Data
                List<VehicleEngeryDeviceLkModel> deviceLkModels = engeryDeviceLkService.listByVehicleId(e.getId());
                for (VehicleEngeryDeviceLkModel lk : deviceLkModels) {
                    EngeryExcelData d = new EngeryExcelData(lk.getDeviceModelName(), e.getConfigName(), e.getBatteryPackageCount());
                    engeryExcelData.getData().add(d);
                    EngeryExcelData ee = new EngeryExcelData(e.getVin(), lk.getDeviceModelName(), lk.getEngeryDeviceName(), lk.getInstallPostion());
                    batteryPackExcelData.getData().add(ee);
                }
                // A-4驱动电机型号Data  // B-3驱动电机Data
                List<VehicleDriveDeviceLkModel> lkModels = driveDeviceLkService.listByVehicleId(e.getId(), 1);
                for (VehicleDriveDeviceLkModel lk : lkModels) {
                    DataLoader.loadNames(lk);
                    DriveMotorModelModel d = driveMotorModelService.get(lk.getDriveModelId());
                    DataLoader.loadNames(d);
                    DriveMotorExcelData data = DriveMotorExcelData.fromEntry(d, e.getConfigName(), lk.getInstallPositionDisplay());
                    motorExcelData.getData().add(data);
                    DriveMotorExcelData dmed = new DriveMotorExcelData(e.getVin(), lk.getDriveMotorModelName(),
                            lk.getDrvieDeviceCode(), lk.getInstallPositionDisplay(), lk.getSequenceNumber());
                    driveExcelData.getData().add(dmed);
                }
                // A-5终端型号Data  // B-4终端Data
                if (StringUtils.isNotBlank(e.getTermId()) && !map.containsKey(e.getTermId())) {
                    termIds.append(e.getTermId()).append(",");
                    map.put(e.getTermId(), e.getConfigName());
                }
                // B-1车辆信息Data
                VehicleModel vm = VehicleModel.fromEntry(e);
                DataLoader.loadNames(vm);
                VehicleExcelData ved = VehicleExcelData.fromEntry(vm, e.getSellForField(), e.getOperUnitName());
                vehicleExcelData.getData().add(ved);
            }
            if (CollectionUtils.isEmpty(engeryExcelData.getData())) {
                engeryExcelData.getData().add(new EngeryExcelData());
            }
            if (CollectionUtils.isEmpty(batteryPackExcelData.getData())) {
                batteryPackExcelData.getData().add(new EngeryExcelData());
            }
            if (CollectionUtils.isEmpty(motorExcelData.getData())) {
                motorExcelData.getData().add(new DriveMotorExcelData());
            }
            if (CollectionUtils.isEmpty(driveExcelData.getData())) {
                driveExcelData.getData().add(new DriveMotorExcelData());
            }
            if (CollectionUtils.isEmpty(vehicleExcelData.getData())) {
                vehicleExcelData.getData().add(new VehicleExcelData());
            }
            // A-1 运营单位信息DATA
            if (operUnitIds.length() > 0) {
                Map<String, Object> up = new HashMap<>(2);
                up.put("unitIds", operUnitIds.substring(0, operUnitIds.length() - 1));
                List<UnitModel> list = unitService.findList(up);
                for (UnitModel unitModel : list) {
                    unitExcelData.getData().add(unitModel);
                }
            }
            if (CollectionUtils.isEmpty(unitExcelData.getData())) {
                unitExcelData.getData().add(new UnitModel());
            }
            // A-2车型参数与配置DATA
            if (vehModelIds.length() > 0) {
                Map<String, Object> mp = new HashMap<>(2);
                mp.put("vehModelIds", vehModelIds.substring(0, vehModelIds.length() - 1));
                List<VehModelModel> list = vehModelService.findList(mp);
                DataLoader.loadNames(list);
                for (VehModelModel vehModelModel : list) {
                    VehModelExcelData excelData = VehModelExcelData.fromEntry(vehModelModel);
                    modelExcelData.getData().add(excelData);
                }
            }
            if (CollectionUtils.isEmpty(modelExcelData.getData())) {
                modelExcelData.getData().add(new VehModelExcelData());
            }
            // A-5终端型号DATA  // B-4终端DATA
            if (termIds.length() > 0) {
                Map<String, Object> tp = new HashMap<>(2);
                tp.put("termIds", termIds.substring(0, termIds.length() - 1));
                List<TermModelUnit> list = termModelUnitService.list(tp);
                for (TermModelUnit term : list) {
                    TermModelModel t = termModelService.get(term.getSysTermModelId());
                    termModelExcelData.getData().add(TermModelExcelData.fromEntry(t, map.get(term.getId())));
                    TermModelExcelData etd = new TermModelExcelData(term.getVin(), t.getTermModelName(), term.getIccid());
                    termExcelData.getData().add(etd);
                }
            }
            if (CollectionUtils.isEmpty(termModelExcelData.getData())) {
                termModelExcelData.getData().add(new TermModelExcelData());
            }
            if (CollectionUtils.isEmpty(termExcelData.getData())) {
                termExcelData.getData().add(new TermModelExcelData());
            }
            // 1.国家平台先导入-类型信息
            List<ExcelData> typeExcelDataList = new ArrayList<>();
            typeExcelDataList.add(unitExcelData);
            typeExcelDataList.add(modelExcelData);
            typeExcelDataList.add(engeryExcelData);
            typeExcelDataList.add(motorExcelData);
            typeExcelDataList.add(termModelExcelData);
            // 2.国家平台后导入-车辆及零部件信息
            List<ExcelData> vehicleExcelDataList = new ArrayList<>();
            vehicleExcelDataList.add(vehicleExcelData);
            vehicleExcelDataList.add(batteryPackExcelData);
            vehicleExcelDataList.add(driveExcelData);
            vehicleExcelDataList.add(termExcelData);

            String templatePath = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();

            // 生成1.国家平台先导入-类型信息excel
            String typeTemplateFile = templatePath + "sys/res/vehicle/nationType.xlsx";
            String typeOutName = String.format("1.国家平台先导入-类型信息-%s.xlsx", DateUtil.getShortDate());
            File typeFile = EasyExcel.createSheetFile(typeExcelDataList, typeTemplateFile, typeOutName);
            // 生成2.国家平台后导入-车辆及零部件信息excel
            String vehTemplateFile = templatePath + "sys/res/vehicle/nationVehicle.xlsx";
            String vehOutName = String.format("2.国家平台后导入-车辆及零部件信息-%s.xlsx", DateUtil.getShortDate());
            File vehFile = EasyExcel.createSheetFile(vehicleExcelDataList, vehTemplateFile, vehOutName);

            log.info("国家平台导出耗时：" + (System.currentTimeMillis() - startTime));
            // 打包ZIP 响应
            String date = DateUtil.formatTime(new Date(), "yyyyMMddHHmmss");
            String outName = String.format("%s-导出-%s.zip", "国家平台信息", date);
            EasyExcel.createZipResponse(Lists.newArrayList(typeFile, vehFile), outName, null, false);
        } catch (Exception ex) {
            log.error("国家平台导出异常：", ex);
            throw new BusinessException("国家平台导出失败");
        }


    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHICLE" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<VehicleModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model  数据model
             * @return 校验信息结果
             */
            @Override
            public List<String> extendValidate(VehicleModel model) {
                List<String> errors = Lists.newArrayList();
                // 验证VIN码是否存在
                if (StringUtils.isNotBlank(model.getVin())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
                    params.put("vin", model.getVin());
                    Vehicle entry = unique("findByVin", params);
                    if (entry != null) {
                        errors.add("VIN码已存在");
                    }
                }
                errors.addAll(extendValidateModel(model));
                if (errors.size() > 0) {
                    log.info("batchImport === " + Joiner.on("-").skipNulls().join(errors));
                }
                return errors;
            }

            /**
             *  保存实体
             * @param model 数据model
             */
            @Override
            public void saveObject(VehicleModel model) {
                insert(model);
            }
        }.work();

    }

    private List<String> extendValidateModel(VehicleModel model) {
        List<String> errors = Lists.newArrayList();
        VehModelModel vehModel = null;
        // 车辆型号
        if (StringUtils.isNotBlank(model.getVehModelName())) {
            try {
                vehModel = vehModelService.getByName(model.getVehModelName());
                if (vehModel != null) {
                    if ((vehModel.getPowerMode() == 6)){
                        if (StringUtils.isNotBlank(model.getEngeryDeviceNames())) {
                            errors.add("动力方式为传统燃油车，可充电储能系统编码需为空");
                        }
                        if (StringUtils.isNotBlank(model.getPowerDeviceCodes())) {
                            errors.add("动力方式为传统燃油车，发电装置编码需为空");
                        }
                    }
                    model.setVehModelId(vehModel.getId());
                }
            } catch (BusinessException e) {
                errors.add(e.getMessage());
            }
        }
        // 内部编号
        if (StringUtils.isNotBlank(model.getInterNo())) {
            Map<String, Object> params = ImmutableMap.of("interNo", model.getInterNo());
            Vehicle entry = unique("findByInterNo", params);
            if (entry != null && !entry.getVin().equals(model.getVin())) {
                errors.add("内部编号已存在");
            }
        }
        // 车辆颜色
        if (StringUtils.isNotBlank(model.getColorName())) {
            Map<String, Object> colorParams = ImmutableMap.of("type", "VEHICLE_COLOR", "name", model.getColorName());
            Dict dict = dictMapper.getByTypeAndName(colorParams);
            if (dict != null) {
                model.setColor(dict.getValue());
            } else {
                errors.add("车辆颜色填写不正确");
            }
        }

        // 终端编号
        if (StringUtils.isNotBlank(model.getSerialNumber())) {
            try {
                TermModelUnitModel termModel = termModelUnitService.findBySerialNumber(model.getSerialNumber());
                // 验证终端编号是否被关联选择
                Vehicle vehicle = findByTermId(termModel.getId());
                if (vehicle != null && !vehicle.getVin().equals(model.getVin())) {
                    errors.add("终端编号已被关联选择");
                }
                // 验证车型中协议类型与终端中协议类型是否一致
                RuleModel ruleModel = ruleService.get(termModel.getSupportProtocol());
                if (vehModel != null && !ruleModel.getRuleTypeId().equals(vehModel.getRuleId())) {
                    errors.add("车型协议类型与终端编号通讯协议中协议类型不一致");
                }

                model.setTermId(termModel.getId());
            } catch (BusinessException e) {
                errors.add(e.getMessage());
            }
        }
        // 可充电储能系统编码
        if (StringUtils.isNotBlank(model.getEngeryDeviceNames())) {
            String[] engeryDeviceNames = StringUtils.split(model.getEngeryDeviceNames(), ",");
            StringBuilder engeryDeviceIds = new StringBuilder();
            for (int i = 0; i < engeryDeviceNames.length; i++) {
                try {
                    EngeryDeviceModel engeryDevice = engeryDeviceService.findByName(engeryDeviceNames[i]);
                    engeryDeviceIds.append(engeryDevice.getId());
                    if (i < engeryDeviceNames.length - 1) {
                        engeryDeviceIds.append(",");
                    }
                } catch (BusinessException e) {
                    errors.add("(" + engeryDeviceNames[i] + ")" + e.getMessage());
                }
            }
            model.setEngeryDeviceIds(engeryDeviceIds.toString());
        }
        // 驱动装置编码
        if (StringUtils.isNotBlank(model.getDriveDeviceCodes())) {
            String[] driveDeviceCodes = StringUtils.split(model.getDriveDeviceCodes(), ",");
            StringBuilder driveDeviceIds = new StringBuilder();
            for (int i = 0; i < driveDeviceCodes.length; i++) {
                try {
                    DriveDeviceModel driveDevice = driveDeviceService.findByCode(driveDeviceCodes[i]);
                    driveDeviceIds.append(driveDevice.getId());
                    if (i < driveDeviceCodes.length - 1) {
                        driveDeviceIds.append(",");
                    }
                } catch (BusinessException e) {
                    errors.add("(" + driveDeviceCodes[i] + ")" + e.getMessage());
                }
            }
            model.setDriveDeviceIds(driveDeviceIds.toString());
        }
        // 发电装置编码
        if (StringUtils.isNotBlank(model.getPowerDeviceCodes())) {
            String[] powerDeviceCodes = StringUtils.split(model.getPowerDeviceCodes(), ",");
            StringBuilder powerDeviceIds = new StringBuilder();
            for (int i = 0; i < powerDeviceCodes.length; i++) {
                try {
                    PowerDeviceModel powerDevice = powerDeviceService.getByCode(powerDeviceCodes[i]);
                    powerDeviceIds.append(powerDevice.getId());
                    if (i < powerDeviceCodes.length - 1) {
                        powerDeviceIds.append(",");
                    }
                } catch (BusinessException e) {
                    errors.add("(" + powerDeviceCodes[i] + ")" + e.getMessage());
                }
            }
            model.setPowerDeviceIds(powerDeviceIds.toString());
        }
        // 制造工厂
        if (StringUtils.isNotBlank(model.getManuUnitName())) {
            try {
                String manuUnitId = unitService.validateNameCode(model.getManuUnitName().trim(), "1001");
                model.setManuUnitId(manuUnitId);
            }catch (BusinessException e){
                if(e.getCode() == 1001){
                    errors.add("制造工厂不存在，请确认");
                }
                if(e.getCode() == 1002){
                    errors.add("制造工厂的单位类型须为车辆制造工厂，请确认");
                }
            }
        }

        return errors;
    }

    @Override
    public void batchUpdate(MultipartFile file) {

        String messageType = "VEHICLE" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<VehicleModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model 数据model
             * @return 校验信息结果
             */
            @Override
            public List<String> extendValidate(VehicleModel model) {
                List<String> errors = Lists.newArrayList();
                try {
                    VehicleModel vinModel = getByVin(model.getVin());
                    model.setId(vinModel.getId());
                } catch (BusinessException e) {
                    errors.add(e.getMessage());
                }
                errors.addAll(extendValidateModel(model));
                log.info("batchUpdate === " + Joiner.on("-").skipNulls().join(errors));
                return errors;
            }

            /**
             *  保存实体
             * @param model 数据model
             */
            @Override
            public void saveObject(VehicleModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 可充电储能装置
     */
    private void batchAddEngeryDeviceLk(String engeryDeviceIds, String createBy, String createTime, String vehicleId) {

        if (StringUtil.isNotEmpty(engeryDeviceIds)) {
            String[] edIds = StringUtil.split(engeryDeviceIds, ",");
            for (String engeryDeviceId : edIds) {
                if (!engeryDeviceService.isExist(engeryDeviceId)) {
                    continue;
                }
                VehicleEngeryDeviceLk lk = new VehicleEngeryDeviceLk();
                lk.setId(UtilHelper.getUUID());
                lk.setCreateBy(createBy);
                lk.setCreateTime(createTime);
                lk.setVehicleId(vehicleId);
                lk.setEngeryDeviceId(engeryDeviceId);
                this.engeryDeviceLkService.insert(lk);
            }
        }

    }

    /**
     * 发电装置信息
     **/
    private void batchAddPowerDeviceLK(String powerDeviceIds, String createBy, String createTime, String vehicleId) {
        if (StringUtil.isNotEmpty(powerDeviceIds)) {
            String[] pdIds = StringUtil.split(powerDeviceIds, ",");
            for (String powerDeviceId : pdIds) {
                if (!powerDeviceService.isExist(powerDeviceId)) {
                    continue;
                }
                VehiclePowerDeviceLk lk = new VehiclePowerDeviceLk();
                lk.setId(UtilHelper.getUUID());
                lk.setCreateBy(createBy);
                lk.setCreateTime(createTime);
                lk.setVehicleId(vehicleId);
                lk.setPowerDeviceId(powerDeviceId);
                this.powerDeviceLkService.insert(lk);
            }
        }

    }

    /**
     * 驱动装置信息
     */
    private void batchAddDriveDeviceLk(String driveDeviceIds, String createBy, String createTime, String vehicleId) {
        if (StringUtil.isNotEmpty(driveDeviceIds)) {
            String[] ddIds = StringUtil.split(driveDeviceIds, ",");
            for (String driveDeviceId : ddIds) {
                if (!driveDeviceService.isExist(driveDeviceId)) {
                    continue;
                }
                VehicleDriveDeviceLk lk = new VehicleDriveDeviceLk();
                lk.setId(UtilHelper.getUUID());
                lk.setCreateBy(createBy);
                lk.setCreateTime(createTime);
                lk.setVehicleId(vehicleId);
                lk.setDrvieDeviceId(driveDeviceId);
                this.driveDeviceLkService.insert(lk);
            }
        }
    }

    @Override
    public String generateInterNo() {
        String interNo = null;
        int count = 0;
        int forNumber = 100;
        while (count < forNumber) {
            String randomNo = RandomUtil.generateRandom("临", 7);
            Map<String, Object> params = ImmutableMap.of("interNo", randomNo);
            Vehicle entry = unique("findByInterNo", params);
            if (entry == null) {
                interNo = randomNo;
                break;
            } else {
                count++;
            }
        }
        return interNo;
    }

    @Override
    public List<String> getVehicleIdsByUserId(String userId) {

        String authSql = DataAccessKit.getUserAuthSql(userId, "sys_vehicle", "v");
        List<String> ids = vehicleMapper.findIdsAuthSql(authSql);
        return ids;
    }


    @Override
    public Integer getVehicleOnlineStatus(String vin) {

        Vehicle vehicle = findByVin(vin);
        if (null == vehicle) {
            return OFFLINE;
        }

        DataRequest dataRequest = new DataRequest();
        dataRequest.setVid(vehicle.getUuid());
        dataRequest.setColumns(DataItemKey.getOnlineStatus());
        dataRequest.setReadMode(DataReadMode.TRANSLATE);


        Map<String, String> statusMap = realDataClient.findByUuid(dataRequest);

        Integer vehState = null;
        if (null != statusMap && !statusMap.isEmpty() && StringUtils.isNotEmpty(statusMap.get("10002"))) {
            vehState = Integer.valueOf(statusMap.get("10002"));
        }

        if (vehState == null || vehState == 0 || vehState == 2) {
            return OFFLINE;
        }
        return ONLINE;
    }

    public Vehicle findByVin(String vin) {
        Map<String, Object> params = new HashMap<>();
        params.put("vin", vin);
        return vehicleMapper.findByVin(params);
    }

    @Override
    public List<Vehicle> findByVins(List<String> vins) {
        Map<String, Object> params = new HashMap<>();
        params.put("vins", vins);
        return vehicleMapper.findByVins(params);
    }

    @Override
    public Map<String, Integer> getVehicleOnlineStatusMap(List<String> vins) {

        List<Vehicle> vehicles = findByVins(vins);

        //vid和vin映射表
        Map<String, String> vinToVid = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVin, Vehicle::getUuid));

        String[] vids = vehicles.stream().map(Vehicle::getUuid).toArray(String[]::new);

        Map<String, Map<String, String>> vidStatusMap = new HashMap<>();

        DataRequest dataRequest = new DataRequest();
        dataRequest.setVids(vids);
        dataRequest.setReadMode(DataReadMode.TRANSLATE);
        dataRequest.setColumns(Collections.singletonList("10002"));

        String[] g6Vids = vehicles.stream().filter(Vehicle::isG6)
                .map(Vehicle::getUuid).toArray(String[]::new);

        String[] gbVids = vehicles.stream().filter(Vehicle::isGb)
                .map(Vehicle::getUuid).toArray(String[]::new);

        if (g6Vids.length > 0) {
            dataRequest.setVids(g6Vids);
            dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
            vidStatusMap.putAll(realDataClient.findByUuids(dataRequest));
        }

        if (gbVids.length > 0) {
            dataRequest.setVids(gbVids);
            dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            vidStatusMap.putAll(realDataClient.findByUuids(dataRequest));
        }

        Map<String, Integer> onlineStatusMap = new HashMap<>();

        vins.forEach(vin -> {

            String vid = vinToVid.get(vin);
            if (!vidStatusMap.containsKey(vid)) {
                onlineStatusMap.put(vin, OFFLINE);
            } else {
                Map<String, String> statusMap = vidStatusMap.get(vid);
                Integer vehState = null;
                if (null != statusMap && !statusMap.isEmpty() && StringUtils.isNotEmpty(statusMap.get("10002"))) {
                    vehState = Integer.valueOf(statusMap.get("10002"));
                }

                if (vehState == null || vehState == 0 || vehState == 2) {
                    onlineStatusMap.put(vin, OFFLINE);
                } else {
                    onlineStatusMap.put(vin, ONLINE);
                }
            }
        });

        return onlineStatusMap;
    }

    /**
     * 下载车辆导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆导入模板.xls", VehicleModel.class);
    }

    /**
     * 生成批量更新模板文件
     *
     * @param pagerInfo PagerInfo
     */
    @Override
    public void getBatchUpdateTemplateFile(PagerInfo pagerInfo) {

        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Vehicle> vehicles = findBySqlId("findPagerModel", params);

        //2、将entity转为model, 具体参考多条件查询里的处理
        List<VehicleModel> models = new ArrayList<>();
        for (Vehicle entry : vehicles) {
            VehicleModel model = VehicleModel.fromEntry(entry);
            // 驱动装置
            List<VehicleDriveDeviceLkModel> list1 = this.driveDeviceLkService.listByVehicleId(entry.getId(), null);
            if (list1 != null && list1.size() > 0) {
                String driveDeviceIdCodes = list1.stream().map(VehicleDriveDeviceLkModel::getDrvieDeviceCode).collect(Collectors.joining(","));
                if (!"null".equals(driveDeviceIdCodes)) {
                    model.setDriveDeviceCodes(driveDeviceIdCodes);
                }

            }
            // 可充电储能装置
            List<VehicleEngeryDeviceLkModel> list2 = this.engeryDeviceLkService.listByVehicleId(entry.getId());
            if (list2 != null && list2.size() > 0) {
                String engeryDeviceNames = list2.stream().map(VehicleEngeryDeviceLkModel::getEngeryDeviceName).collect(Collectors.joining(","));
                if (!"null".equals(engeryDeviceNames)) {
                    model.setEngeryDeviceNames(engeryDeviceNames);
                }

            }
            // 发电装置
            List<VehiclePowerDeviceLkModel> list3 = this.powerDeviceLkService.listByVehicleId(entry.getId(), null);
            if (list3 != null && list3.size() > 0) {
                String powerDeviceCodes = list3.stream().map(VehiclePowerDeviceLkModel::getPowerDeviceCode).collect(Collectors.joining(","));
                if (!"null".equals(powerDeviceCodes)) {
                    model.setPowerDeviceCodes(powerDeviceCodes);
                }
            }
            models.add(model);
        }

        //3、调用DataLoader渲染Model里的linkName之类的数据
        DataLoader.loadNames(models);

        //调用EasyExcel生成文件并下载
        EasyExcel.renderBatchUpdateDemoFile("车辆批量更新模板.xls", VehicleModel.class, models);
    }

    @Override
    public String exportOffline(@NotNull PagerInfo pagerInfo) {
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "车辆列表";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        log.info("执行车辆离线导出任务:{}", exportMethodName);

        // 创建离线导出任务
        return offlineExportService.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );


    }

    @Override
    public List<VehicleInfoModel> vehicles(String vid, String updateTime) {
        // 初始化Area
        Map<String, Area> areaMap = new HashMap<>();
        areaMapper.findAllArea().forEach(area -> areaMap.put(area.getId(), area));
        // 填充code和name的路径
        areaMap.forEach((key, value) -> {
            String[] pathArray = value.getPath().split("/");
            List<String> codePath = new ArrayList<>(10);
            List<String> namePath = new ArrayList<>(10);
            List<String> idPath = new ArrayList<>(10);
            for (final String areaId : pathArray) {
                if (StringUtils.isEmpty(areaId)) {
                    continue;
                }
                Area area = areaMap.get(areaId);
                idPath.add(areaId);
                if (area == null) {
                    codePath.add("");
                    namePath.add("");
                    continue;
                }
                codePath.add(area.getCode());
                namePath.add(area.getName());
            }
            value.setCodePath(codePath);
            value.setNamePath(namePath);
            value.setIdPath(idPath);
        });

        // 更新model字段
        long start = System.currentTimeMillis();
        if (StringUtils.isNotBlank(updateTime)) {
            updateTime += " 00:00:00";
        }
        List<VehicleInfoModel> result = vehicleMapper.vehicles(vid, updateTime);
        //log.info("耗时：" + (System.currentTimeMillis() - start) + " ms");
        result.forEach(model -> {
            Area operArea = areaMap.get(model.getOperAreaId());
            if (operArea != null) {
                model.setOperAreaName(operArea.getNamePath());
                model.setOperAreaCode(operArea.getCodePath());
                model.setOperAreaPath(operArea.getIdPath());
            }
            Area licentCity = areaMap.get(model.getOperLicenseCityId());
            if (licentCity != null) {
                model.setOperLicenseCityName(licentCity.getNamePath());
                model.setOperLicenseCityCode(licentCity.getCodePath());
                model.setOperLicenseCityPath(licentCity.getIdPath());
            }
            if (StringUtils.isNotBlank(model.getWhoCanSeeMe())) {
                model.setWhoCanSeeMes(model.getWhoCanSeeMe().split("\\s+"));
            }
        });
        return result;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void exportOfflineProcessor(
            @NotNull final String taskId,
            @NotNull final String createBy,
            @NotNull final Date createTime,
            @NotNull final String exportFileName,
            @NotNull final String exportMethodParams) throws Exception {

        log.trace("执行离线导出任务:{}", exportFileName);
        //1、先通过pagerInfo查询数据，类似于导出处理
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        final String excelTemplateFile = "sys/res/vehicle/export.xls";

        MybatisOfflineExportHandler.csv(
                taskId,
                createBy,
                createTime,
                exportFileName,
                excelTemplateFile,
                this.vehicleMapper::findPagerModel,
                params,
                this::fromEntityToModel,
                this.vehicleMapper::findPagerModel,
                redis,
                ws
        );
    }

    @NotNull
    private List<VehicleModel> fromEntityToModel(final @NotNull List<Vehicle> entities) {

        final ArrayList<VehicleModel> models = Lists.newArrayList();

        for (final Vehicle entity : entities) {

            final VehicleModel model = VehicleModel.fromEntry(entity);

            models.add(model);
        }

        DataLoader.loadNames(models);

        return models;
    }

    @Override
    public List<Vehicle> findAllForLk(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Vehicle> entries = findBySqlId("findForLk", params);
        return entries;
    }

    @Override
    public PagerResult findForLk(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        PagerResult pr = findPagerModel("findForLk", params, pagerInfo.getStart(), pagerInfo.getLimit());
        if (CollectionUtils.isNotEmpty(pr.getData())) {
            List<VehicleModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                Vehicle obj = (Vehicle) entry;
                int count = notifierVehicleLkMapper.allocateCount(obj.getId());
                VehicleModel model = VehicleModel.fromEntry(obj);
                model.setFaultNoticesCount(count);
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
        }
        return pr;
    }


    @Override
    public String getChipModelId(String id) {
        return vehicleMapper.getChipModelId(id);
    }

    @Override
    public Map<String, Object> getSellInfo(String id) {
        return vehicleMapper.getSellInfo(id);
    }

}
