package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.veh.domain.DayVeh;
import com.bitnei.cloud.veh.model.DayVehModel;
import com.bitnei.cloud.veh.service.IDayVehService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayVehService实现<br>
* 描述： DayVehService实现<br>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.mapper.DayVehMapper" )
public class DayVehService extends BaseService implements IDayVehService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //如果开始时间、结束时间为空，默认查询当天的数据
        if(params.get("startDate") == null && params.get("endDate") == null){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date yd = calendar.getTime();
            String sd = DateUtil.formatTime(yd, "yyyy-MM-dd");
            params.put("startDate", sd);
            params.put("endDate", sd);
        }
        //如果开始时间不为空，结束时间为空时，查询开始时间当天数据
        if(params.get("endDate") == null && params.get("startDate") != null){
            params.put("endDate", params.get("startDate"));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DayVeh> entries = findBySqlId("pagerModel", params);
            List<DayVehModel> models = new ArrayList();
            for(DayVeh entry: entries){
                DayVeh obj = (DayVeh)entry;
                models.add(DayVehModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DayVehModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DayVeh obj = (DayVeh)entry;
                models.add(DayVehModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //如果开始时间、结束时间为空，默认查询当天的数据
        if(params.get("startDate") == null && params.get("endDate") == null){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date yd = calendar.getTime();
            String sd = DateUtil.formatTime(yd, "yyyy-MM-dd");
            params.put("startDate", sd);
            params.put("endDate", sd);
        }
        //如果开始时间不为空，结束时间为空时，查询开始时间当天数据
        if(params.get("endDate") == null && params.get("startDate") != null){
            params.put("endDate", params.get("startDate"));
        }

        new ExcelExportHandler<DayVeh>(this, "pagerModel", params, "veh/res/dayVeh/export.xls", "单车日报表") {
            @Override
            public Object process(DayVeh entity){
                /** 百公里额定耗电量 **/
                entity.setRatedChargeCon100km(null);
                /** 快充次数 **/
                entity.setFastTimes(null);
                /** 慢充次数 **/
                entity.setLowTimes(null);
                return super.process(entity);
            }
        }.work();

        return;


    }

    /**
     * 下载单车日报表导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("单车日报表导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }


}
