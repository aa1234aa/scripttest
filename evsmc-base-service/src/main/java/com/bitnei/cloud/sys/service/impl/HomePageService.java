package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.orm.bean.Sort;
import com.bitnei.cloud.sys.domain.HomePage;
import com.bitnei.cloud.sys.model.HomePageModel;
import com.bitnei.cloud.sys.service.IHomePageService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： HomePageService实现<br>
 * 描述： HomePageService实现<br>
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
 * <td>2019-03-19 14:43:56</td>
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
@RequiredArgsConstructor
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.HomePageMapper")
public class HomePageService extends CommonBaseService implements IHomePageService {

    private final IAlarmInfoService alarmInfoService;

    @Override
    public Object list(PagerInfo pagerInfo) {
        return list(pagerInfo, false);
    }

    @Override
    public Object list(final PagerInfo pagerInfo, final boolean ignorePermission) {
        HomePageModel homePageModel;
        Map<String, Object> params;
        if (ignorePermission) {
            params = new HashMap<>(10);
        } else {
            //获取当权限的map
            params = DataAccessKit.getAuthMap("sys_vehicle", "v");
            //log.info("{}#{}",ServletUtil.getCurrentUser(), params.get("authSQL"));
        }

        // 车辆统计
        String ignoreVehicleStatistics = getQueryCondition(pagerInfo, "ignoreVehicleStatistics");
        if (Boolean.valueOf(ignoreVehicleStatistics)) {
            homePageModel = new HomePageModel();
        } else {
            params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
            params.put("start_time", String.format("%s 00:00:00", DateUtil.getShortDate()));
            params.put("end_time", String.format("%s 23:59:59", DateUtil.getShortDate()));
            HomePage entry = unique("pagerModel", params);
            homePageModel = HomePageModel.fromEntry(entry);
           // log.info("车辆数:{}",homePageModel.getTotal());
        }

        // 车辆故障数
        HomePage vehFaultCount = unique("VehFaultCount", params);
        homePageModel.setFaultVehCount(vehFaultCount.getFaultVehCount());
        PagerInfo alarmPagerInfo = new PagerInfo();

        // 设置排序: 按等级倒序+报警时间倒序
        Sort descAlarmLevel = new Sort("alarm_level", Sort.DESC);
        Sort descFaultBeginTime = new Sort("fault_begin_time", Sort.DESC);
        alarmPagerInfo.setSort(Lists.newArrayList(descAlarmLevel, descFaultBeginTime));

        alarmPagerInfo.setStart(0);
        alarmPagerInfo.setLimit(10);

        PagerResult obj = (PagerResult) alarmInfoService.list(alarmPagerInfo);
        if (obj != null && obj.getTotal() > 0) {
            homePageModel.setAlarmInfos(obj.getData().get(0));
        }

//        List<Map<String,Object>> areaStat=findBySqlId("areaStat",params);
//        if (CollectionUtils.isNotEmpty(areaStat)){
//            //行驶区域top 5
//            homePageModel.setAreaStat(areaStat);
//        }

        return homePageModel;
    }

    @Override
    public HomePageModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_home_page", "home_p");
        params.put("id", id);
        HomePage entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return HomePageModel.fromEntry(entry);
    }


    @Override
    public void insert(HomePageModel model) {

        HomePage obj = new HomePage();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();

        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(HomePageModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_home_page", "home_p");

        HomePage obj = new HomePage();
        BeanUtils.copyProperties(model, obj);

        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_home_page", "home_p");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_home_page", "home_p");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<HomePage>(this, "pagerModel", params, "sys/res/homePage/export.xls", "首页统计") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "HOMEPAGE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<HomePageModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(HomePageModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(HomePageModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "HOMEPAGE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<HomePageModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(HomePageModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(HomePageModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public List<Map<String, Object>> top5Area(PagerInfo pagerInfo) {

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        ;

        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("start_time", String.format("%s 00:00:00", DateUtil.getShortDate()));
        params.put("end_time", String.format("%s 23:59:59", DateUtil.getShortDate()));
        if (pagerInfo.getStart() == null) {
            pagerInfo.setStart(0);
        }
        if (pagerInfo.getLimit() == null) {
            pagerInfo.setLimit(5);
        }
        params.put("offset", pagerInfo.getStart() * pagerInfo.getLimit());
        params.put("limit", pagerInfo.getLimit());
        List<Map<String, Object>> areaStat = findBySqlId("areaStat", params);
        //行驶区域top 5

        return areaStat;
    }


}
