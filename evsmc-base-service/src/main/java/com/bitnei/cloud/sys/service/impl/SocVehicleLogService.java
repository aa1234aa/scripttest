package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.SocVehicleLogMapper;
import com.bitnei.cloud.sys.domain.SocVehicleLog;
import com.bitnei.cloud.sys.model.SocVehicleLogModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.ISocVehicleLogService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.commons.util.MapperUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： SocVehicleLogService实现<br>
 * 描述： SocVehicleLogService实现<br>
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
 * <td>2019-03-06 19:00:14</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.SocVehicleLogMapper")
public class SocVehicleLogService extends BaseService implements ISocVehicleLogService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Autowired
    private SocVehicleLogMapper socVehicleLogMapper;
    @Autowired
    private IOfflineExportService offlineExportService;
    @Autowired
    private IUserService userService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //历史加载页面时不显示数据
        if (pagerInfo.getConditions() == null || pagerInfo.getConditions().size() == 0) {
            return new ArrayList<>();
        }
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))) {
            long beginTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0) {
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        else{
            throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<SocVehicleLog> entries = findBySqlId("pagerModel", params);
            List<SocVehicleLogModel> models = new ArrayList();
            for (SocVehicleLog entry : entries) {
                SocVehicleLog obj = (SocVehicleLog) entry;
                models.add(SocVehicleLogModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else{
            // 构建分页参数
            final PageRowBounds pageRowBounds = PageRowBoundsUtil.fromPagerInfo(pagerInfo);
            // 进行分页查询
            final Page<SocVehicleLog> page = socVehicleLogMapper.pagerModel(params, pageRowBounds);

            final PagerResult pr = new PagerResult();
            // 设置结果集
            pr.setData(
                    page
                        .stream()
                        .map(SocVehicleLogModel::fromEntry)
                        .collect(Collectors.toList())
            );
            // 设置记录总数
            pr.setTotal(page.getTotal());

            return pr;
        }
    }

    @Override
    public Object realTimeList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime") != null && !String.valueOf(params.get("beginTime")).equals("") && params.get("endTime") != null && !params.get("endTime").equals("")) {
            long beginTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0) {
                throw new BusinessException("请输入正确的时间");
            }
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<SocVehicleLog> entries = findBySqlId("findByRealTime", params);
            List<SocVehicleLogModel> models = new ArrayList();
            for (SocVehicleLog entry : entries) {
                SocVehicleLog obj = (SocVehicleLog) entry;
                models.add(SocVehicleLogModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findByRealTime", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<SocVehicleLogModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                SocVehicleLog obj = (SocVehicleLog) entry;
                models.add(SocVehicleLogModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public SocVehicleLogModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_soc_vehicle_log", "svl");
        params.put("id", id);
        SocVehicleLog entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return SocVehicleLogModel.fromEntry(entry);
    }

    @Override
    public SocVehicleLogModel getByVid(String vid) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_soc_vehicle_log", "svl");
        params.put("uuid", vid);
        SocVehicleLog entry = unique("getByVid", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return SocVehicleLogModel.fromEntry(entry);
    }


    @Override
    public void insert(SocVehicleLogModel model) {

        SocVehicleLog obj = new SocVehicleLog();
        BeanUtils.copyProperties(model, obj);
//        //单元测试指定id，如果是单元测试，那么就不使用uuid
//        String id = UtilHelper.getUUID();
//        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
//            id = model.getId();
//        }
//        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        if (res == 1) {
            log.info("车辆SOC告警过低产生，VID：" + model.getVid() + ",车牌号:" + model.getLicensePlate());
        }
    }

    @Override
    public void update(SocVehicleLogModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_soc_vehicle_log", "svl");

        SocVehicleLog obj = new SocVehicleLog();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public void updateByVid(SocVehicleLogModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_soc_vehicle_log", "svl");

        SocVehicleLog obj = new SocVehicleLog();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.update("updateByVid", params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_soc_vehicle_log", "svl");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (pagerInfo.getConditions() == null || pagerInfo.getConditions().size() == 0) {
            throw new BusinessException("列表无数据，请输入查询条件");
        } else {
            //判断时间是否合理
            if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))) {
                long beginTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
                long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
                if (endTime - beginTime < 0) {
                    throw new BusinessException("请输入正确的时间");
                }
                else if (endTime - beginTime >= (long)86400000*30){
                    throw new BusinessException("查询时间跨度不能超过30天");
                }
            }
            else{
                throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
            }
            new ExcelExportHandler<SocVehicleLog>(this, "pagerModel", params, "sys/res/socVehicleLog/export.xls", "历史SOC过低车辆") {
            }.work();
        }
        return;
    }


    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {
        if (pagerInfo.getConditions() == null || pagerInfo.getConditions().size() <= 0){
            throw new BusinessException("列表无数据，请输入查询条件");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))) {
            long beginTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0) {
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        else{
            throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
        }
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "历史SOC过低车辆";

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

        log.trace("执行离线导出任务:{}", exportFileName);

        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        final UserModel userModel = userService.findByUsername(createBy);
        final String userId = userModel.getId();
        final String tableName = "sys_vehicle";
        final String tableAlias = "sv";
        final String authSql = DataAccessKit.getUserAuthSql(userId, tableName, tableAlias);
        if (authSql != null) {
            builder.put(Constants.AUTH_SQL, authSql);
        }

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        ServletUtil.pageInfoToMap(pagerInfo).forEach((k, v) -> {
            if (null != k && null != v) {
                builder.put(k, v);
            }
        });
        final Map<String, Object> params = new HashMap<>(builder.build());
        final String excelTemplateFile = "sys/res/socVehicleLog/export.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.socVehicleLogMapper::pagerModel,
            params,
            this::fromEntityToModel,
            this.socVehicleLogMapper::pagerModel,
            redis,
            ws
        );
    }

    @NotNull
    private List<SocVehicleLogModel> fromEntityToModel(final @NotNull List<SocVehicleLog> entities) {
        final ArrayList<SocVehicleLogModel> models = Lists.newArrayList();
        for (final SocVehicleLog entity : entities) {

            final SocVehicleLogModel model = new SocVehicleLogModel();
            BeanUtils.copyProperties(entity, model);
            model.setLicensePlate(entity.get("licensePlate") == null ? null : entity.get("licensePlate").toString());
            model.setVin(entity.get("vin") == null ? null : entity.get("vin").toString());
            model.setInterNo(entity.get("interNo") == null ? null : entity.get("interNo").toString());
            model.setVehModelId(entity.get("vehModelId") == null ? null : entity.get("vehModelId").toString());
            model.setOperLicenseCityId(entity.get("operLicenseCityId") == null ? null : entity.get("operLicenseCityId").toString());
            model.setOperUnitId(entity.get("operUnitId") == null ? null : entity.get("operUnitId").toString());
            model.setVin(entity.get("isDelete") == null || "1".equals(entity.get("isDelete").toString()) ? model.getVin() == null ? "(已删除)" : model.getVin()+"(已删除)" : model.getVin());

            models.add(model);
        }

        DataLoader.loadNames(models);

        return models;
    }


    @Override
    public void realTimeExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime") != null && !String.valueOf(params.get("beginTime")).equals("") && params.get("endTime") != null && !params.get("endTime").equals("")) {
            long beginTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0) {
                throw new BusinessException("请输入正确的时间");
            }
        }
        new ExcelExportHandler<SocVehicleLog>(this, "findByRealTime", params, "sys/res/socVehicleLog/realTimeExport.xls", "实时SOC过低车辆") {
        }.work();

        return;


    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "SOCVEHICLELOG" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<SocVehicleLogModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(SocVehicleLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(SocVehicleLogModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "SOCVEHICLELOG" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<SocVehicleLogModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(SocVehicleLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(SocVehicleLogModel model) {
                update(model);
            }
        }.work();

    }


}
