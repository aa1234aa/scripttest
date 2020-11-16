package com.bitnei.cloud.veh.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.veh.dao.DayreportDataQualityMapper;
import com.bitnei.cloud.veh.domain.DayreportDataQuality;
import com.bitnei.cloud.veh.model.DayreportDataQualityModel;
import com.bitnei.cloud.veh.service.IDayreportDataQualityService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportDataQualityService实现<br>
* 描述： DayreportDataQualityService实现<br>
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
* <td>2019-09-19 18:48:27</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DayreportDataQualityMapper" )
public class DayreportDataQualityService extends BaseService implements IDayreportDataQualityService, IOfflineExportCallback {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Resource(name = "webRedisKit")
    private RedisKit redis;
    @Resource
    private WsServiceClient ws;
    @Resource
    private IUserService userService;
    @Resource
    private IOfflineExportService offlineExportService;
    @Resource
    private DayreportDataQualityMapper dayreportDataQualityMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        checkTime(params);
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DayreportDataQuality> entries = findBySqlId("pagerModel", params);
            List<DayreportDataQualityModel> models = new ArrayList();
            for(DayreportDataQuality entry: entries){
                DayreportDataQuality obj = (DayreportDataQuality)entry;
                format(obj);
                models.add(DayreportDataQualityModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DayreportDataQualityModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DayreportDataQuality obj = (DayreportDataQuality)entry;
                format(obj);
                models.add(DayreportDataQualityModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    public void format(DayreportDataQuality entry){

        DecimalFormat d = (DecimalFormat) NumberFormat.getPercentInstance();
        d.applyPattern("###.##%");
        entry.setExistForwardToString(entry.getExistForward() == null ? null : entry.getExistForward().toString().replaceAll("\\.0",""));
        entry.setReportDateEx(entry.getReportDate() == null ? null : DateUtil.formatTime(new Date(entry.getReportDate().getTime()),DATE_PATTERN));
        entry.setStartTime(entry.getStartTime() == null ? null : entry.getStartTime().replaceAll("\\.0",""));
        entry.setEndTime(entry.getEndTime() == null ? null : entry.getEndTime().replaceAll("\\.0",""));
        entry.setAnbormalRateEx(entry.getAnbormalRate() == null ? null : d.format(entry.getAnbormalRate()));
        entry.setMissRateEx(entry.getMissRate() == null ? null : d.format(entry.getMissRate()));
        if(entry.getExistForwardToString() != null && "0".equals(entry.getExistForwardToString())){
            entry.setMissForwardRateEx("--");
        }
        else{
            entry.setMissForwardRateEx(entry.getMissForwardRate() == null ? null : d.format(entry.getMissForwardRate()));
        }

    }

    public void checkTime(Map<String, Object> params){

        if (params.get("startDate") != null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(params.get("endDate") +" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(params.get("startDate") +" 00:00:00").getTime();
            if (((end - start)/86400000) >= 30){
                throw new ExportErrorException("查询时间范围不能超过30天");
            }
        }
        else{
            String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("startDate",yesterday);
            params.put("endDate",yesterday);
        }
    }

    @Override
    public DayreportDataQualityModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.put("id",id);
        DayreportDataQuality entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DayreportDataQualityModel.fromEntry(entry);
    }

    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        checkTime(params);
        new ExcelExportHandler<DayreportDataQuality>(this, "pagerModel", params, "veh/res/dayreportDataQuality/export.xls", "车辆数据质量日报") {

            @Override
            public Object process(DayreportDataQuality entity){
                format(entity);
                return super.process(entity);
            }
        }.work();

        return;

    }

    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        checkTime(params);
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "车辆数据质量日报";

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
        final Map<String, Object> params = new HashMap<>();
        params.putAll(builder.build());
        final String excelTemplateFile = "veh/res/dayreportDataQuality/export.xls";

        MybatisOfflineExportHandler.csv(
                taskId,
                createBy,
                createTime,
                exportFileName,
                excelTemplateFile,
                this.dayreportDataQualityMapper::pagerModel,
                params,
                this::fromEntityToModel,
                this.dayreportDataQualityMapper::pagerModel,
                redis,
                ws
        );
    }

    @NotNull
    private List<DayreportDataQualityModel> fromEntityToModel(final @NotNull List<DayreportDataQuality> entities) {

        final ArrayList<DayreportDataQualityModel> models = Lists.newArrayList();
        for (final DayreportDataQuality entity : entities) {
            format(entity);
            final DayreportDataQualityModel model = new DayreportDataQualityModel();
            BeanUtils.copyProperties(entity, model);
            models.add(model);
        }
        DataLoader.loadNames(models);
        return models;
    }
}
