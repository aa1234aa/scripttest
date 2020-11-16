package com.bitnei.cloud.veh.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.handler.MybatisOnlineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.veh.dao.HistoryStateMapper;
import com.bitnei.cloud.veh.domain.HistoryState;
import com.bitnei.cloud.veh.model.HistoryStateModel;
import com.bitnei.cloud.veh.service.IHistoryStateService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HistoryStateService实现<br>
* 描述： HistoryStateService实现<br>
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
* <td>2019-03-09 11:23:08</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.HistoryStateMapper" )
public class HistoryStateService extends BaseService implements IHistoryStateService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Autowired
    private IOfflineExportService offlineExportService;
    @Autowired
    private IUserService userService;
    @Autowired
    private HistoryStateMapper historyStateMapper;
    @Autowired
    private HistoryStateUpdateVehPosition historyStateUpdateVehPosition;

   @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_history_state", "hs");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String sql = "pagerModel";
        //默认查询
        if(params.get("defaultQueryStatus") != null && Integer.parseInt(params.get("defaultQueryStatus").toString())==0){
            sql = "pagerModelAllVehNew";
        }
        params = initParams(params);
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<HistoryState> entries = findBySqlId(sql, params);
            List<HistoryStateModel> models = new ArrayList<>();
            List<HistoryStateModel> updateVehPositionList = new ArrayList<>();
            for (HistoryState entry : entries) {
                HistoryState obj = (HistoryState) entry;
                HistoryStateModel model = HistoryStateModel.fromEntry(obj);
                if (StringUtils.isBlank(model.getVehPosition())) {
                    if(model.getPositionLon() != null && model.getPositionLat() != null) {
                        String lng = String.valueOf(model.getPositionLon());
                        String lat = String.valueOf(model.getPositionLat());
                        model.setVehPosition(GpsUtil.getAddress(lng, lat));
                        updateVehPositionList.add(model);
                    }
                }
                if(model.getStage() != null) {
                    model.setStageDisplay(Constants.VehicleStage.getDesc(Integer.parseInt(model.getStage())));
                }
                if(model.getChargeDischargeState() != null) {
                    model.setChargeDischargeStateDisplay(Constants.ChargeDischargeState.getDesc(model.getChargeDischargeState()));
                }
                if(model.getState() != null) {
                    model.setStateDisplay(Constants.VehicleStatus.getDesc(model.getState()));
                }
                models.add(model);
            }
            historyStateUpdateVehPosition.updateVehPositionAsync(updateVehPositionList);
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel(sql, params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<HistoryStateModel> models = new ArrayList<>();
            List<HistoryStateModel> updateVehPositionList = new ArrayList<>();
            for (Object entry : pr.getData()) {
                HistoryState obj = (HistoryState) entry;
                HistoryStateModel model = HistoryStateModel.fromEntry(obj);
                if (StringUtils.isBlank(model.getVehPosition())) {
                    if(model.getPositionLon() != null && model.getPositionLat() != null) {
                        String lng = String.valueOf(model.getPositionLon());
                        String lat = String.valueOf(model.getPositionLat());
                        model.setVehPosition(GpsUtil.getAddress(lng, lat));
                        updateVehPositionList.add(model);
                    }
                }
                if(model.getStage() != null) {
                    model.setStageDisplay(Constants.VehicleStage.getDesc(Integer.parseInt(model.getStage())));
                }
                if(model.getChargeDischargeState() != null) {
                    model.setChargeDischargeStateDisplay(Constants.ChargeDischargeState.getDesc(model.getChargeDischargeState()));
                }
                if(model.getState() != null) {
                    model.setStateDisplay(Constants.VehicleStatus.getDesc(model.getState()));
                }
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            historyStateUpdateVehPosition.updateVehPositionAsync(updateVehPositionList);
            return pr;
        }
    }

    @Override
    public HistoryStateModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_history_state", "hs");
        params.put("id",id);
        HistoryState entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return HistoryStateModel.fromEntry(entry);
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_history_state", "hs");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String sql = "pagerModel";
        //默认查询
        if(params.get("defaultQueryStatus") != null && Integer.parseInt(params.get("defaultQueryStatus").toString())==0){
            sql = "pagerModelAllVehNew";
        }
        params = initParams(params);

        final int batchSize = 4096;
        final List<List<HistoryStateModel>> updateVehPositionList = new ArrayList<>(1);
        updateVehPositionList.add(new ArrayList<>(batchSize));

        new ExcelExportHandler<HistoryState>(this, sql, params, "veh/res/historyState/export.xls", "车辆历史状态") {
            @Override
            public Object process(HistoryState entity){
                boolean requireUpdateVehPosition = false;
                if (StringUtils.isBlank(entity.getVehPosition())) {
                    if (entity.getPositionLon() != null && entity.getPositionLat() != null) {
                        String lng = String.valueOf(entity.getPositionLon());
                        String lat = String.valueOf(entity.getPositionLat());
                        entity.setVehPosition(GpsUtil.getAddress(lng, lat));
                        requireUpdateVehPosition = true;
                    }
                }
                if(entity.getStage() != null) {
                    entity.setStageDisplay(Constants.VehicleStage.getDesc(Integer.parseInt(entity.getStage())));
                }
                if(entity.getChargeDischargeState() != null) {
                    entity.setChargeDischargeStateDisplay(Constants.ChargeDischargeState.getDesc(entity.getChargeDischargeState()));
                }
                if(entity.getState() != null) {
                    entity.setStateDisplay(Constants.VehicleStatus.getDesc(entity.getState()));
                }
                final HistoryStateModel model = (HistoryStateModel)super.process(entity);
                if (requireUpdateVehPosition) {
                    final List<HistoryStateModel> historyStateModels = updateVehPositionList.get(0);
                    historyStateModels.add(model);
                    if (historyStateModels.size() >= batchSize) {
                        historyStateUpdateVehPosition.updateVehPositionAsync(historyStateModels);
                        updateVehPositionList.set(0, new ArrayList<>(batchSize));
                    }
                }
                return model;
            }
        }.work();

        final List<HistoryStateModel> historyStateModels = updateVehPositionList.get(0);
        historyStateUpdateVehPosition.updateVehPositionAsync(historyStateModels);

        return;
    }

    /**
     * 下载车辆历史状态报表导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆历史状态报表导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
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
        final String tableName = "veh_history_state";
        final String tableAlias = "hs";
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

//        final ImmutableMap<String, Object> params = builder.build();
        Map<String,Object> params = new HashMap<>();
        params.putAll(builder.build());
        params = initParams(params);
        final String excelTemplateFile = "veh/res/historyState/export.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.historyStateMapper::pagerModel,
            params,
            this::fromEntityToModel,
            this.historyStateMapper::pagerModel,
            redis,
            ws
        );
    }

    @NotNull
    private List<HistoryStateModel> fromEntityToModel(final @NotNull List<HistoryState> entities) {

        final ArrayList<HistoryStateModel> models = Lists.newArrayList();

        final List<HistoryStateModel> updateVehPositionList = Lists.newArrayList();

        for (final HistoryState entity : entities) {

            final HistoryStateModel model = HistoryStateModel.fromEntry(entity);

            if (StringUtils.isBlank(entity.getVehPosition())) {
                if (entity.getPositionLon() != null && entity.getPositionLat() != null) {
                    String lng = String.valueOf(entity.getPositionLon());
                    String lat = String.valueOf(entity.getPositionLat());
                    model.setVehPosition(GpsUtil.getAddress(lng, lat));
                    updateVehPositionList.add(model);
                }
            }
            if(entity.getStage() != null) {
                model.setStageDisplay(Constants.VehicleStage.getDesc(Integer.parseInt(entity.getStage())));
            }
            if(entity.getChargeDischargeState() != null) {
                model.setChargeDischargeStateDisplay(Constants.ChargeDischargeState.getDesc(entity.getChargeDischargeState()));
            }
            if(entity.getState() != null) {
                model.setStateDisplay(Constants.VehicleStatus.getDesc(entity.getState()));
            }
            if (model.getIccid() != null) {
                model.setIccid(CsvUtil.encode("`" + model.getIccid()));
            }
            models.add(model);
        }

        historyStateUpdateVehPosition.updateVehPositionAsync(updateVehPositionList);

        DataLoader.loadNames(models);

        return models;
    }

    /**
     * 离线导出
     * @param pagerInfo
     * @return
     */
    @NotNull
    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "车辆历史状态报表";

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

    @NotNull
    @Override
    public ResponseEntity<StreamingResponseBody> exportOnline(@NotNull final PagerInfo pagerInfo) throws IOException {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_history_state", "hs");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params = initParams(params);

        final String createBy = ServletUtil.getCurrentUser();
        final Date createTime = new Date();
        final String exportFileName = String.format(
            "车辆历史状态报表-%s-%s.csv",
            createBy,
            DateFormatUtils.format(
                createTime,
                "yyyyMMddHHmmss"
            )
        );
        final String excelTemplateFile = "veh/res/historyState/export.xls";

        return MybatisOnlineExportHandler.csv(
                createBy,
                createTime,
                exportFileName,
                excelTemplateFile,
                this.historyStateMapper::pagerModel,
                params,
                this::fromEntityToModel
        );
    }

    public Map<String,Object> initParams(Map<String,Object> params){
        //默认查询
        if(params.get("defaultQueryStatus") != null && Integer.parseInt(params.get("defaultQueryStatus").toString())==0){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 6);
            Date yd = calendar.getTime();
            String sd = DateUtil.formatTime(yd, "yyyy-MM-dd HH:mm:ss");
            String endTime = DateUtil.formatTime(new Date(), "yyyy-MM-dd HH:mm:ss");
            params.put("beginTime", sd);
            params.put("endTime", endTime);
        }else{
            //如果开始时间和结束时间都为空
            if (params.get("endTime") == null && params.get("beginTime") == null) {
                String beginTime = DateUtil.formatTime(new Date(), "yyyy-MM-dd") + " 00:00:00";
                Date startT = DateUtil.strToDate_ex_full(beginTime);
                String endTime = DateUtil.formatTime(startT, "yyyy-MM-dd") + " 23:59:59";
                params.put("beginTime", beginTime);
                params.put("endTime", endTime);
            }
            //如果开始时间不为空，结束时间为空时，查询开始时间当天数据
            if(params.get("endTime") == null && params.get("beginTime") != null){
                String beginTime = params.get("beginTime").toString();
                Date startT = DateUtil.strToDate_ex_full(beginTime);
                String endTime = DateUtil.formatTime(startT, "yyyy-MM-dd") + " 23:59:59";
                params.put("endTime", endTime);
            }
            //如果结束时间与开始时间一致，就查询一天的
            if(params.get("beginTime") != null && params.get("beginTime") == params.get("endTime")){
                Date startT = DateUtil.strToDate_ex_full(params.get("beginTime").toString());
                String beginTime = DateUtil.formatTime(startT, "yyyy-MM-dd") + " 00:00:00";
                String endTime = DateUtil.formatTime(startT, "yyyy-MM-dd") + " 23:59:59";
                params.put("beginTime", beginTime);
                params.put("endTime", endTime);
            }
        }
        return params;
    }
}
