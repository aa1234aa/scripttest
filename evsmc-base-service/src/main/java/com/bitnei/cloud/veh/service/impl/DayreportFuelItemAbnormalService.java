package com.bitnei.cloud.veh.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.veh.dao.DayreportFuelItemAbnormalMapper;
import com.bitnei.cloud.veh.domain.DayreportFuelItemAbnormal;
import com.bitnei.cloud.veh.model.DayreportFuelItemAbnormalModel;
import com.bitnei.cloud.veh.service.IDayreportFuelItemAbnormalService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DayreportFuelItemAbnormalService实现<br>
 * 描述： DayreportFuelItemAbnormalService实现<br>
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
 * <td>2019-09-24 13:57:58</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DayreportFuelItemAbnormalMapper")
public class DayreportFuelItemAbnormalService extends BaseService implements IDayreportFuelItemAbnormalService, IOfflineExportCallback {

    @Resource
    private IOfflineExportService offlineExportService;
    @Resource
    private DayreportFuelItemAbnormalMapper dayreportFuelItemAbnormalMapper;
    @Resource
    private IUserService userService;
    @Resource(name = "webRedisKit")
    private RedisKit redis;
    @Resource
    private WsServiceClient ws;


    @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<DayreportFuelItemAbnormal> entries = findBySqlId("pagerModel", params);
            List<DayreportFuelItemAbnormalModel> models = new ArrayList<>();
            for (DayreportFuelItemAbnormal entry : entries) {
                models.add(DayreportFuelItemAbnormalModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<DayreportFuelItemAbnormalModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                DayreportFuelItemAbnormal obj = (DayreportFuelItemAbnormal) entry;
                models.add(DayreportFuelItemAbnormalModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public DayreportFuelItemAbnormalModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_dayreport_fuel_item_abnormal", "vdfia");
        params.put("id", id);
        DayreportFuelItemAbnormal entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return DayreportFuelItemAbnormalModel.fromEntry(entry);
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("veh_dayreport_fuel_item_abnormal", "vdfia");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<DayreportFuelItemAbnormal>(this, "pagerModel", params, "veh/res/dayreportFuelItemAbnormal/export.xls", "燃油车数据项异常日报") {
        }.work();
        return;
    }

    @NotNull
    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "燃油车辆异常数据项报表";

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
        final String tableName = "veh_dayreport_fuel_item_abnormal";
        final String tableAlias = "vdfia";
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
        Map<String, Object> params = new HashMap<>(builder.build());
        final String excelTemplateFile = "veh/res/dayreportFuelItemAbnormal/export.xls";
        MybatisOfflineExportHandler.csv(
                taskId,
                createBy,
                createTime,
                exportFileName,
                excelTemplateFile,
                this.dayreportFuelItemAbnormalMapper::pagerModel,
                params,
                this::fromEntityToModel,
                this.dayreportFuelItemAbnormalMapper::pagerModel,
                redis,
                ws
        );
    }

    @NotNull
    private List<DayreportFuelItemAbnormalModel> fromEntityToModel(final @NotNull List<DayreportFuelItemAbnormal> entities) {
        final ArrayList<DayreportFuelItemAbnormalModel> models = Lists.newArrayList();
        for (final DayreportFuelItemAbnormal e : entities) {
            final DayreportFuelItemAbnormalModel model = DayreportFuelItemAbnormalModel.fromEntry(e);
            models.add(model);
        }
        DataLoader.loadNames(models);
        return models;
    }



}