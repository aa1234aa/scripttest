package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.veh.domain.DayreportChargestate;
import com.bitnei.cloud.veh.model.ChargeAndConsumePowerModel;
import com.bitnei.cloud.veh.model.DayreportChargestateModel;
import com.bitnei.cloud.veh.model.VehicleDetailsModel;
import com.bitnei.cloud.veh.service.IDayreportChargestateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportChargestateService实现<br>
* 描述： DayreportChargestateService实现<br>
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
* <td>2019-03-15 16:33:37</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.mapper.DayreportChargestateMapper" )
public class DayreportChargestateService extends BaseService implements IDayreportChargestateService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
       //判断时间跨度是否超过31天，超过31天默认查询前10天
       if (params.get("startDate")!=null && params.get("endDate") != null){
           long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
           long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
           if (((end - start)/86400000) >= 31){
               String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
               String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
               params.put("endDate",endDate+" 23:59:59");
           }else{
               params.put("endDate",String.valueOf(params.get("endDate"))+" 23:59:59");
           }
       }
       else if (params.get("startDate")==null &&params.get("endDate")==null){
           String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
           String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 10*86400000));
           params.put("startDate", start);
           params.put("endDate", end + " 23:59:59");
       }
       // 上牌区域
       if (null != params.get("operAreaIds")){
           params.replace("operAreaIds", params.get("operAreaIds").toString().split(","));
       }
       // 车辆型号
       if (null != params.get("vehModelIds")){
           params.replace("vehModelIds", params.get("vehModelIds").toString().split(","));
       }
       // 车辆用途
       if (null != params.get("operUseFors")){
           params.replace("operUseFors", params.get("operUseFors").toString().split(","));
       }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<ChargeAndConsumePowerModel> entries = findBySqlId("pagerModel", params);
            entries.forEach(models -> {
                if (models.getChargeVehCount() != null && models.getChargeVehCount() != 0){
                    this.getAvg(models);
                }
            });
            return entries;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ChargeAndConsumePowerModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ChargeAndConsumePowerModel obj = (ChargeAndConsumePowerModel)entry;
                if (obj.getChargeVehCount() != null && obj.getChargeVehCount() != 0){
                    this.getAvg(obj);
                }
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private void  getAvg(ChargeAndConsumePowerModel obj){

        DecimalFormat d = new DecimalFormat();
        d.applyPattern("#.00");
        obj.setFastChargeNumberAvg(obj.getFastChargeNumber() == null || obj.getFastChargeNumber() == 0 ? 0 : Double.parseDouble(d.format(obj.getFastChargeNumber()/obj.getChargeVehCount())));
        obj.setTrickleChargeNumberAvg(obj.getTrickleChargeNumber() == null || obj.getTrickleChargeNumber() == 0 ? 0 : Double.parseDouble(d.format(obj.getTrickleChargeNumber()/obj.getChargeVehCount())));
        obj.setFastChargeTimeAvg(obj.getFastChargeTime() == null || obj.getFastChargeTime() == 0 ? 0 : Double.parseDouble(d.format(obj.getFastChargeTime()/obj.getChargeVehCount())));
        obj.setTrickleChargeTimeAvg(obj.getTrickleChargeTime() == null || obj.getTrickleChargeTime() == 0 ? 0 : Double.parseDouble(d.format(obj.getTrickleChargeTime()/obj.getChargeVehCount())));
    }

    @Override
    public Object vehDetailsList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        if (params.get("dateTime")!=null){
            params.put("startDate",String.valueOf(params.get("dateTime")));
            params.put("endDate",String.valueOf(params.get("dateTime"))+" 23:59:59");
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<VehicleDetailsModel> entries = findBySqlId("findVehicle", params);
            for(VehicleDetailsModel model : entries){
                model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
            }
            return entries;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findVehicle", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehicleDetailsModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                VehicleDetailsModel obj = (VehicleDetailsModel)entry;
                obj.setVin(obj.getIsDelete() == null || "1".equals(obj.getIsDelete()) ? obj.getVin() == null ? "(已删除)" : obj.getVin() + "(已删除)" : obj.getVin());
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public DayreportChargestateModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_chargestate", "vdch");
        params.put("id",id);
        DayreportChargestate entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DayreportChargestateModel.fromEntry(entry);
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate+" 23:59:59");
            }else{
                params.put("endDate",String.valueOf(params.get("endDate"))+" 23:59:59");
            }
        }
        else if (params.get("startDate")==null &&params.get("endDate")==null){
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 10*86400000));
            params.put("startDate", start);
            params.put("endDate", end + " 23:59:59");
        }
        new ExcelExportHandler<ChargeAndConsumePowerModel>(this, "pagerModel", params, "veh/res/dayreportChargestate/export.xls", "充耗电情况统计") {
            @Override
            public Object process(ChargeAndConsumePowerModel entity) {

                if (entity.getChargeVehCount() != null && entity.getChargeVehCount() != 0){
                    getAvg(entity);
                }
                //临时为空
                entity.setAllChargePower(null);
                return entity;
            }
        }.work();
        return;
    }

    @Override
    public void vehDetailsExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("dateTime")!=null){
            params.put("startDate",String.valueOf(params.get("dateTime")));
            params.put("endDate",String.valueOf(params.get("dateTime"))+" 23:59:59");
        }
        new ExcelExportHandler<VehicleDetailsModel>(this, "findVehicle", params, "veh/res/dayreportChargestate/vehDetailsExport.xls", "充耗电车辆详情") {
            @Override
            public Object process(VehicleDetailsModel entity) {
                entity.setVin(entity.getIsDelete() == null || "1".equals(entity.getIsDelete()) ? entity.getVin() == null ? "(已删除)" : entity.getVin() + "(已删除)" : entity.getVin());
                return  entity;
            }
        }.work();
        return;
    }
}
