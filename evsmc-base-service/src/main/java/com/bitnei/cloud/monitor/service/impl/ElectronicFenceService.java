package com.bitnei.cloud.monitor.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.INotifierRuleLkService;
import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.bitnei.cloud.monitor.domain.FenceVehLk;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.monitor.domain.ElectronicFence;
import com.bitnei.cloud.monitor.model.ElectronicFenceModel;
import com.bitnei.cloud.monitor.service.IElectronicFenceService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ElectronicFenceService实现<br>
 * 描述： ElectronicFenceService实现<br>
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
 * <td>2019-05-17 11:04:12</td>
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
@Mybatis(namespace = "com.bitnei.cloud.monitor.dao.ElectronicFenceMapper")
public class ElectronicFenceService extends BaseService implements IElectronicFenceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Autowired
    private INotifierRuleLkService notifierRuleLkService;

    @Autowired
    private ThreadPoolExecutor normalExecutor;

    /**
     * 查询电子围栏列表
     *
     * @param pagerInfo 分页参数
     * @return
     */
    @Override
    public Object list(final PagerInfo pagerInfo) {
        // 获取当权限的map
        final Map<String, Object> params = DataAccessKit.getAuthMap("monitor_electronic_fence", "efe");
        // 获取页面传递的查询参数
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            final List<ElectronicFence> entries = findBySqlId("pagerModel", params);
            final List<ElectronicFenceModel> models = new ArrayList();
            models.addAll(entries.stream().map(entry -> ElectronicFenceModel.fromEntry(entry)).collect(Collectors.toList()));
            return models;
        }
        // 分页查询
        else {
            final PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            final List<ElectronicFenceModel> models = new ArrayList();
            models.addAll(pr.getData().stream().map(entry -> ElectronicFenceModel.fromEntry((ElectronicFence) entry)).collect(Collectors.toList()));
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 通过id查询电子围栏
     *
     * @param id 电子围栏id
     * @return
     */
    @Override
    public ElectronicFenceModel get(final String id) {
        // 获取当权限的map
        final Map<String, Object> params = DataAccessKit.getAuthMap("monitor_electronic_fence", "efe");
        params.put("id", id);
        final ElectronicFence entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return ElectronicFenceModel.fromEntry(entry);
    }

    /**
     * 通过id查询电子围栏
     *
     * @param name 电子围栏名称
     * @return
     */
    private List<ElectronicFence> queryByName(final String name) {
        final Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("name", name);
        return findBySqlId("queryByName", params);
    }

    /**
     * 电子围栏新增
     *
     * @param model 新增model
     */
    @Override
    @Transactional
    public void insert(final ElectronicFenceModel model) {
        // 校验电子围栏名称是否存在
        if (!StringUtils.isEmpty(model.getName())) {
            final List<ElectronicFence> nameList = queryByName(model.getName());
            if (null != nameList && nameList.size() > 0) {
                logger.info("新增电子围栏：");
                logger.info("围栏名称：" + nameList.get(0).getName());
                logger.info("同名围栏id：" + nameList.get(0).getId() + "=======");
                throw new BusinessException("电子围栏名称已存在，添加失败");
            }
        }
        final ElectronicFence obj = new ElectronicFence();
        BeanUtils.copyProperties(model, obj);
        // 单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setGroupCode(String.valueOf(System.currentTimeMillis()));
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        obj.setUpdateBy(obj.getCreateBy());
        obj.setUpdateTime(obj.getCreateTime());
        obj.setStatusFlag(1);
        final int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    /**
     * 电子围栏编辑
     *
     * @param model 编辑model
     */
    @Override
    @Transactional
    public void update(final ElectronicFenceModel model) {
        // 校验电子围栏名称是否存在
        if (!StringUtils.isEmpty(model.getName())) {
            final List<ElectronicFence> nameList = queryByName(model.getName());
            if (null != nameList && nameList.size() > 0) {
                for (final ElectronicFence ef : nameList) {
                    logger.info("围栏名称：" + ef.getName());
                    logger.info("同名围栏id：" + ef.getId() + "=======");
                    logger.info("当前围栏id：" + model.getId() + "=======");
                    if (!StringUtils.isEmpty(ef.getId()) && !model.getId().equals(ef.getId())) {
                        throw new BusinessException("电子围栏名称已存在，编辑失败");
                    }
                }
            }
        }
        // 获取当权限的map
//        Map<String, Object> params = DataAccessKit.getAuthMap("monitor_electronic_fence", "efe");
        // 查询更新前电子围栏信息
        final ElectronicFenceModel oldModel = get(model.getId());
        final ElectronicFence obj = new ElectronicFence();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        // 判断若围栏规则发生变化则将当前围栏置为无效另外新增一条并终止正在触发中的围栏报警
        if (!oldModel.getRuleStatus().equals(obj.getRuleStatus()) || !oldModel.getRuleType().equals(obj.getRuleType()) ||
                !oldModel.getPeriodType().equals(obj.getPeriodType()) || (2 == obj.getPeriodType() && !oldModel.getRuleWeek().equals(obj.getRuleWeek())) ||
                !DateUtil.formatTime(oldModel.getStartDate(), DateUtil.DAY_FORMAT).equals(DateUtil.formatTime(obj.getStartDate(), DateUtil.DAY_FORMAT)) ||
                !DateUtil.formatTime(oldModel.getEndDate(), DateUtil.DAY_FORMAT).equals(DateUtil.formatTime(obj.getEndDate(), DateUtil.DAY_FORMAT)) ||
                !oldModel.getStartTime().equals(obj.getStartTime()) || !oldModel.getEndTime().equals(obj.getEndTime()) ||
                !oldModel.getChartType().equals(obj.getChartType()) || !oldModel.getLonlatRange().equals(obj.getLonlatRange())) {
            updateFenceRule(oldModel, obj);
        } else {
            final int res = super.update("update", obj);
            if (res == 0) {
                throw new BusinessException("更新失败");
            }
            // 判断如果围栏名称或者响应方式发生变化则更新围栏实时报警和历史报警表的rule_name和response_mode字段
            if (!oldModel.getName().equals(obj.getName()) || !oldModel.getResponseMode().equals(obj.getResponseMode())) {
                final Map<String, Object> aParam = new HashMap<String, Object>(3);
                aParam.put("fenceId", obj.getId());
                aParam.put("fenceName", obj.getName());
                aParam.put("responseMode", obj.getResponseMode());
                // 更新实时报警表围栏名称、响应方式
                super.update("updateRealFenceAlarmNameAndResponseMode", aParam);
            }
        }
    }

    /**
     * 当修改围栏规则的时候将围栏置为无效再插入新的围栏记录，同时将原围栏关联车辆记录置为无效，查询新的围栏id与车辆vid关联记录
     *
     * @param oldModel
     * @param nowFence
     * @return
     */
    private int updateFenceRule(final ElectronicFenceModel oldModel, final ElectronicFence nowFence) {
        if (null == oldModel || null == nowFence) {
            return 0;
        }
        // 修改前围栏信息
        final ElectronicFence oldFence = new ElectronicFence();
        BeanUtils.copyProperties(oldModel, oldFence);
        oldFence.setUpdateBy(nowFence.getUpdateBy());
        oldFence.setUpdateTime(nowFence.getUpdateTime());
        // 原纪录置为无效
        oldFence.setStatusFlag(0);
        int result = super.update("updateStatus", oldFence);
        if (result > 0) {

            normalExecutor.submit(() -> {
                // 停止当前正在触发的围栏报警记录
                // 改成批量
                stopRealFenceAlarm(oldFence.getId(), null);
            });

            // 插入新的围栏信息记录
            final String nowFenceId = UtilHelper.getUUID();
            nowFence.setId(nowFenceId);
            result = super.insert("insert", nowFence);
            if (result > 0) {
                // 查询围栏修改前关联的全部车辆数据
                final FenceVehLk lkParam = new FenceVehLk();
                lkParam.setElectronicFenceId(oldFence.getId());
                final List<FenceVehLk> lkList = super.findBySqlId("queryLkByFenceIdAndVid", lkParam);
                if (null != lkList && lkList.size() > 0) {
                    // 遍历插入新关联记录
                    for (FenceVehLk fenceVehLk : lkList) {
                        fenceVehLk.setId(UtilHelper.getUUID());
                        fenceVehLk.setElectronicFenceId(nowFenceId);
                        fenceVehLk.setState(1);
                        fenceVehLk.setUpdateTime(nowFence.getUpdateTime());
                        super.insert("insertOrUpdateFenceVehLk", fenceVehLk);
                    }

                    // 原记录置为无效
                    FenceVehLk oldLk = new FenceVehLk();
                    oldLk.setElectronicFenceId(oldFence.getId());
                    oldLk.setState(0);
                    oldLk.setUpdateTime(nowFence.getUpdateTime());
                    super.update("updateFenceVehLkByFenceId", oldLk);
                }

                //更新推送规则
                notifierRuleLkService.updateRuleIdByOld(oldFence.getId(), nowFenceId);
            }
        }
        return result;
    }

    /**
     * 删除多个电子围栏
     *
     * @param id 电子围栏id
     * @return
     */
    @Override
    public int deleteMulti(final String id) {

        final ElectronicFence ef = new ElectronicFence();
        ef.setId(id);
        ef.setStatusFlag(0);
        ef.setUpdateBy(ServletUtil.getCurrentUser());
        ef.setUpdateTime(DateUtil.getNow());
        // 修改电子围栏状态为无效
        final int result = super.update("updateStatus", ef);
        if (result > 0) {
            normalExecutor.submit(() -> {
                // 停止当前正在触发的围栏报警记录
                stopRealFenceAlarm(id, null);
            });

            final String now = DateUtil.getNow();
            // 原记录置为无效
            FenceVehLk oldLk = new FenceVehLk();
            oldLk.setElectronicFenceId(id);
            oldLk.setState(0);
            oldLk.setUpdateTime(now);
            super.update("updateFenceVehLkByFenceId", oldLk);

            // 删除推送规则
            notifierRuleLkService.deleteByRuleId(id);
        }
        return result;
    }

    /**
     * 删除电子围栏与车关联信息
     *
     * @param fenceId 电子围栏id
     * @param fenceId 电子围栏id
     * @return
     */
    private List<Map<String, Object>> queryVidByFenceId(final String fenceId) {
        if (StringUtils.isEmpty(fenceId)) {
            return null;
        }
        return super.findBySqlId("queryVidByFenceId", fenceId);
    }

    /**
     * 分页查询未关联电子围栏的车辆
     *
     * @param pagerInfo 分页参数
     * @param fenceId   围栏id
     * @return
     */
    @Override
    public Object queryVehsNotLk(final PagerInfo pagerInfo, final String fenceId) {
        // 获取当权限的map
        final Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (!params.containsKey("fenceId") && !StringUtils.isEmpty(fenceId)) {
            params.put("fenceId", fenceId);
        }
        if (!params.containsKey("fenceId") || StringUtils.isEmpty(params.get("fenceId"))) {
            throw new BusinessException("电子围栏id不可为空");
        }
        final List<VehicleModel> models = Lists.newArrayList();
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            final List<Vehicle> entries = findBySqlId("queryVehsNotLk", params);
            if (null != entries && entries.size() > 0) {
                models.addAll(entries.stream().map(VehicleModel::fromEntry).collect(Collectors.toList()));
            }
            return models;
        } else {
            // 分页查询
            final PagerResult pr = findPagerModel("queryVehsNotLk", params, pagerInfo.getStart(), pagerInfo.getLimit());
            models.addAll(pr.getData().stream().map(entry -> VehicleModel.fromEntry((Vehicle) entry)).collect(Collectors.toList()));
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 分页查询已关联电子围栏的车辆
     *
     * @param pagerInfo 分页参数
     * @param fenceId   围栏id
     * @return
     */
    @Override
    public Object queryVehsLk(final PagerInfo pagerInfo, final String fenceId) {
        // 获取当权限的map
        final Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (!params.containsKey("fenceId") && !StringUtils.isEmpty(fenceId)) {
            params.put("fenceId", fenceId);
        }
        if (!params.containsKey("fenceId") || StringUtils.isEmpty(params.get("fenceId"))) {
            throw new BusinessException("电子围栏id不可为空");
        }
        final List<VehicleModel> models = Lists.newArrayList();
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            final List<Vehicle> entries = findBySqlId("queryVehsLk", params);
            if (null != entries && entries.size() > 0) {
                models.addAll(entries.stream().map(vehicle -> VehicleModel.fromEntry(vehicle)).collect(Collectors.toList()));
            }
            return models;
        } else {
            // 分页查询
            final PagerResult pr = findPagerModel("queryVehsLk", params, pagerInfo.getStart(), pagerInfo.getLimit());
            models.addAll(pr.getData().stream().map(entry -> VehicleModel.fromEntry((Vehicle) entry)).collect(Collectors.toList()));
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 添加电子围栏与车辆绑定关系
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     */
    @Override
    public void insertVehLk(final PagerInfo pagerInfo) {
        final Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        final String id = (String) Optional.ofNullable(params.get("id")).orElse("");
        final String vehVids = (String) Optional.ofNullable(params.get("vehVids")).orElse("");
        final String nowDate = DateUtil.getNow();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException("电子围栏id不可为空");
        }
        // 如果车辆uuid为空，则认为绑定全部查询结果车辆
        if (StringUtils.isEmpty(vehVids)) {
//            PagerInfo pagerInfo = new PagerInfo();
            // 查询全部结果
            pagerInfo.setLimit(-1);
            final Object object = queryVehsNotLk(pagerInfo, id);
            if (null != object) {
                final List<VehicleModel> models = (List<VehicleModel>) object;

                ForkJoinPool customThreadPool = new ForkJoinPool(8);

                List<List<VehicleModel>> modelsList = Lists.partition(models, 1000);
                ForkJoinTask task = customThreadPool.submit(() ->
                        modelsList.parallelStream().forEach(partitionModels -> {

                    List<FenceVehLk> lks = partitionModels.stream().map(it -> {
                        FenceVehLk lk = new FenceVehLk();
                        lk.setId(UtilHelper.getUUID());
                        lk.setState(1);
                        lk.setElectronicFenceId(id);
                        lk.setVid(it.getUuid());
                        lk.setUpdateTime(nowDate);
                        lk.setCreateTime(lk.getUpdateTime());
                        return lk;
                    }).collect(Collectors.toList());

                    operationFenceVehLkBatch(lks);
                }));

                task.join();

//                for (VehicleModel model : models) {
//                    final FenceVehLk lk = new FenceVehLk();
//                    lk.setElectronicFenceId(id);
//                    lk.setVid(model.getUuid());
//                    lk.setUpdateTime(nowDate);
//                    operationFenceVehLk(lk);
//                }
            }
            return;
        }
        final String[] ids = vehVids.split(",");
        if (null != ids && ids.length > 0) {
            for (String vid : ids) {
                final FenceVehLk lk = new FenceVehLk();
                lk.setElectronicFenceId(id);
                lk.setVid(vid);
                lk.setUpdateTime(nowDate);
                operationFenceVehLk(lk);
            }
        }
    }

    /**
     * 操作围栏与车辆绑定关系，先通过围栏id、vid查询记录是否存在，1.不存在则插入；2.存在状态是无效则修改为有效；3.存在且状态有效则跳过
     *
     * @param param
     */
    private void operationFenceVehLk(final FenceVehLk param) {
//        final List<FenceVehLk> lkList = super.findBySqlId("queryLkByFenceIdAndVid", param);
//        if (null != lkList && lkList.size() > 0) {
//            FenceVehLk fenceVehLk = lkList.get(0);
//            if (fenceVehLk.getState() == 1) {
//                return;
//            }
//            fenceVehLk.setState(1);
//            fenceVehLk.setUpdateTime(param.getUpdateTime());
//            super.update("updateFenceVehLkById", fenceVehLk);
//        } else {
//            param.setId(UtilHelper.getUUID());
//            param.setState(1);
//            param.setCreateTime(param.getUpdateTime());
//            super.insert("insertOrUpdateFenceVehLk", param);
//        }
        param.setId(UtilHelper.getUUID());
        param.setState(1);
        param.setCreateTime(param.getUpdateTime());
        super.insert("insertOrUpdateFenceVehLk", param);
    }

    /**
     * 批量处理围栏和车辆的绑定关系
     * @param params
     */
    private void operationFenceVehLkBatch(final List<FenceVehLk> params) {

        Map<String, Object> map = new HashMap<>();
        map.put("list", params);

        this.insert("insertOrUpdateFenceVehLkBatch", map);
    }

    /**
     * 删除围栏关联车辆信息
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     * @param id        电子围栏id
     * @param vehVids   车辆uuid
     */
    @Override
    public void deleteVehLk(final PagerInfo pagerInfo, String id, String vehVids) {
        final Map<String, Object> map = new HashMap<String, Object>(3);
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(vehVids)) {
            map.putAll(ServletUtil.pageInfoToMap(pagerInfo));
            id = (String) Optional.ofNullable(map.get("id")).orElse("");
            vehVids = (String) Optional.ofNullable(map.get("vehVids")).orElse("");
        }
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(vehVids)) {
            throw new BusinessException("围栏id或车辆uuid不可为空");
        }
        map.put("fenceId", id);
        map.put("nowTime", DateUtil.getNow());
        String[] vehidArr = vehVids.split(",");
        map.put("vehIdList", new ArrayList(Arrays.asList(vehidArr)));
        final int result = super.delete("deleteFenceVehLk", map);
        // 将正在触发中的该围栏、车辆报警状态改为已结束
        if (result > 0 && null != vehidArr && vehidArr.length > 0) {
            for (String s : vehidArr) {
                stopRealFenceAlarm(id, s);
            }
        }
    }

    /**
     * 终止正在触发中的围栏报警
     *
     * @param fenceId 电子围栏id
     * @param vid     关联车辆uuid
     */
    private void stopRealFenceAlarm(final String fenceId, final String vid) {
        if (!StringUtils.isEmpty(fenceId)) {
            alarmInfoService.stopByFenceAlarm(vid, fenceId);
        }
    }

    /**
     * 下载电子围栏关联车辆导入查询模版
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("电子围栏关联车辆导入查询模版.xls", "VIN", new String[]{"LSB123214124214"});
    }

    //<editor-fold desc="无用代码">

    /**
     * 导入
     *
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(final PagerInfo pagerInfo) {
        // 获取权限sql
        final Map<String, Object> params = DataAccessKit.getAuthMap("monitor_electronic_fence", "efe");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<ElectronicFence>(this, "pagerModel", params, "monitor/res/electronicFence/export.xls", "电子围栏") {
        }.work();
        return;
    }

    /**
     * 批量导入
     *
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        final String messageType = "ELECTRONICFENCE" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<ElectronicFenceModel>(file, messageType, GroupExcelImport.class) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ElectronicFenceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ElectronicFenceModel model) {
                insert(model);
            }
        }.work();
    }

    /**
     * 批量更新
     *
     * @param file 文件
     */
    @Override
    public void batchUpdate(MultipartFile file) {
        final String messageType = "ELECTRONICFENCE" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<ElectronicFenceModel>(file, messageType, GroupExcelUpdate.class) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ElectronicFenceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ElectronicFenceModel model) {
                update(model);
            }
        }.work();
    }
    //</editor-fold>


}
