package com.bitnei.cloud.veh.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.veh.dao.DayreportSummaryMapper;
import com.bitnei.cloud.veh.domain.DayreportSummary;
import com.bitnei.cloud.veh.model.*;
import com.bitnei.cloud.veh.service.IDayreportSummaryService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportSummaryService实现<br>
* 描述： DayreportSummaryService实现<br>
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
* <td>2019-03-11 09:40:45</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DayreportSummaryMapper" )
public class DayreportSummaryService extends BaseService implements IDayreportSummaryService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Autowired
    private IOfflineExportService offlineExportService;
    @Autowired
    private IUserService userService;
    @Autowired
    private DayreportSummaryMapper dayreportSummaryMapper;
    @Autowired
    private IDictService dictService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
        params.put("endDate",end);
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DayreportSummary> entries = findBySqlId("pagerModel", params);
            List<DayreportSummaryModel> models = new ArrayList();
            for(DayreportSummary entry: entries){
                DayreportSummary obj = (DayreportSummary)entry;
                DayreportSummaryModel data = DayreportSummaryModel.fromEntry(obj);
                models.add(data);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DayreportSummaryModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DayreportSummary obj = (DayreportSummary)entry;
                DayreportSummaryModel data = DayreportSummaryModel.fromEntry(obj);
                models.add(data);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object mileageMonitorList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //单位按月统计分析
        if (pagerInfo.getConditions().size() == 1 && pagerInfo.getConditions().get(0).getName().equals("operUnitId")){
            //非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
                List<MileageMonitorModel> models = findBySqlId("countMileageMonthlyByUnit",params);
                return models;
            }
            //分页查询
            else {
                PagerResult pr = findPagerModel("countMileageMonthlyByUnit", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<MileageMonitorModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    MileageMonitorModel obj = (MileageMonitorModel)entry;
                    models.add(obj);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
        //所有单位车辆里程分布统计分析
        else{
            if (params.get("endTime") == null || params.get("endTime")== ""){
                String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
                params.put("endTime", now);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH );
                if(month==0){
                    year--;
                    month=12;
                }
                params.put("date2",getdate(year,month));

            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date end = sdf.parse(String.valueOf(params.get("endTime")));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(end);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH );
                    if(month==0){
                        year--;
                        month=12;
                    }
                    params.put("date2",getdate(year,month));
                } catch (ParseException e) {
                    return "请检查传入的日期格式是否为yyyy-MM-dd";
                }
            }
            //非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
                List<MileageMonitorModel> models = findBySqlId("countMileageMonthly", params);
                return models;
            }
            //分页查询
            else {
                PagerResult pr = findPagerModel("countMileageMonthly", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<MileageMonitorModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    MileageMonitorModel obj = (MileageMonitorModel)entry;
                    models.add(obj);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
    }

    @Override
    public Object vehicleDetailsList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //处理单位月份车辆详情 传报表时间endTime yyyy-mm
        if (String.valueOf(params.get("endTime")).length()==7){
            //有小许bug 传入的yyyy-mm  还需带dd（随便哪天都行），目前后端处理下
            params.put("endTime",String.valueOf(params.get("endTime"))+"-01");
            if (params.get("data2") != null && String.valueOf(params.get("data2")).equals("500")){
                params.put("data55","data55");
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("500")){
                params.put("data44","data44");
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("10000")){
                params.put("data33","data33");
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("20000")){
                params.put("data22","data22");
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("30000")){
                params.put("data11","data11");
            }
            //非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
                List<DetailsVehicleModel> models = findBySqlId("detailsVehicleByUnit", params);
                for(DetailsVehicleModel model : models){
                    model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
                }
                return models;
            }
            //分页查询
            else {
                PagerResult pr = findPagerModel("detailsVehicleByUnit", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<DetailsVehicleModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    DetailsVehicleModel obj = (DetailsVehicleModel)entry;
                    obj.setVin(obj.getIsDelete() == null || "1".equals(obj.getIsDelete()) ? obj.getVin() == null ? "(已删除)" : obj.getVin() + "(已删除)" : obj.getVin());
                    models.add(obj);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }

        else{

            if (params.get("endTime") == null || params.get("endTime")== "") {
                String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
                params.put("endTime", now);
            }
            if("500".equals(String.valueOf(params.get("data2")))){
                params.put("data3","1");
                params.remove("data1");
            }
            if ("30000".equals(String.valueOf(params.get("data1")))){
                params.remove("data2");
            }
            //非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
                List<DetailsVehicleModel> models = findBySqlId("detailsVehicle", params);
                for(DetailsVehicleModel model : models){
                    model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
                }
                return models;
            }
            //分页查询
            else {
                PagerResult pr = findPagerModel("detailsVehicle", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<DetailsVehicleModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    DetailsVehicleModel obj = (DetailsVehicleModel)entry;
                    obj.setVin(obj.getIsDelete() == null || "1".equals(obj.getIsDelete()) ? obj.getVin() == null ? "(已删除)" : obj.getVin() + "(已删除)" : obj.getVin());
                    models.add(obj);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
    }


    @Override
    public Object vehicleDetailsByUnitList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("type").equals("1")){
            params.put("data1","1");
        }
        else if (params.get("type").equals("2")){
            params.put("data2","2");
        }
        else if (params.get("type").equals("3")){
            params.put("data3","3");
        }
        else if (params.get("type").equals("4")){
            params.put("data4","4");
        }
        else if (params.get("type").equals("5")){
            params.put("data5","5");
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<DetailsVehicleModel> models = findBySqlId("detailsVehicleByUnit", params);
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("detailsVehicleByUnit", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DetailsVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DetailsVehicleModel obj = (DetailsVehicleModel)entry;
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public Object vehicleTravelList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        if ((params.get("startDate") == null || params.get("startDate")== "") && (params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 10*86400000));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
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
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<VehicleTravelModel> models = findBySqlId("pagerMode2", params);
            //处理小数点后两位
            DecimalFormat d = new DecimalFormat();
            d.applyPattern("#.00");
            double onceRunTimeAvg = 0,onceRunKmAvg = 0,newOnceRunTimeAvg = 0,newOnceRunKmAvg = 0,daySpeedAvg = 0,newDaySpeedAvg = 0;
            for (VehicleTravelModel vehicleTravelModel:models){
                if (vehicleTravelModel.getVehOnlineCount() != null && vehicleTravelModel.getVehOnlineCount()>0 && vehicleTravelModel.getDayTravelTime() != null){
                    onceRunTimeAvg = vehicleTravelModel.getDayTravelTime()/vehicleTravelModel.getVehOnlineCount();
                    newOnceRunTimeAvg = Double.parseDouble(d.format(onceRunTimeAvg));
                    //单车平均行驶时间
                    vehicleTravelModel.setOnceRunTimeAvg(newOnceRunTimeAvg);
                }
                if (vehicleTravelModel.getVehOnlineCount() != null && vehicleTravelModel.getVehOnlineCount()>0 && vehicleTravelModel.getDayRunKm() != null){
                    onceRunKmAvg = vehicleTravelModel.getDayRunKm()/vehicleTravelModel.getVehOnlineCount();
                    newOnceRunKmAvg = Double.parseDouble(d.format(onceRunKmAvg));
                    //单车平均行驶里程
                    vehicleTravelModel.setOnceRunKmAvg(newOnceRunKmAvg);
                }
                if (vehicleTravelModel.getDayTravelTime()!=null&&vehicleTravelModel.getDayTravelTime()>0 && vehicleTravelModel.getDayRunKm() != null){
                    //日总行驶时间
                    daySpeedAvg = vehicleTravelModel.getDayRunKm()/vehicleTravelModel.getDayTravelTime();
                    newDaySpeedAvg = Double.parseDouble(d.format(daySpeedAvg));
                    vehicleTravelModel.setDaySpeedAvg(newDaySpeedAvg);
                    vehicleTravelModel.setDayTravelTime(Double.parseDouble(d.format(vehicleTravelModel.getDayTravelTime())));
                }
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerMode2", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehicleTravelModel> models = new ArrayList();
            //处理小数点后两位
            DecimalFormat d = new DecimalFormat();
            d.applyPattern("#.00");
            double onceRunTimeAvg = 0,onceRunKmAvg = 0,newOnceRunTimeAvg = 0,newOnceRunKmAvg = 0,daySpeedAvg = 0,newDaySpeedAvg = 0;
            for(Object entry: pr.getData()){
                VehicleTravelModel obj = (VehicleTravelModel)entry;
                if (obj.getVehOnlineCount() != null && obj.getVehOnlineCount()>0 && obj.getDayTravelTime() != null){
                    onceRunTimeAvg = obj.getDayTravelTime()/obj.getVehOnlineCount();
                    newOnceRunTimeAvg = Double.parseDouble(d.format(onceRunTimeAvg));
                    //单车平均行驶时间
                    obj.setOnceRunTimeAvg(newOnceRunTimeAvg);
                }
                if (obj.getVehOnlineCount() != null && obj.getVehOnlineCount()>0 && obj.getDayRunKm() != null){
                    onceRunKmAvg = obj.getDayRunKm()/obj.getVehOnlineCount();
                    newOnceRunKmAvg = Double.parseDouble(d.format(onceRunKmAvg));
                    //单车平均行驶里程
                    obj.setOnceRunKmAvg(newOnceRunKmAvg);
                }
                if (obj.getDayTravelTime()!=null&&obj.getDayTravelTime()>0 && obj.getDayRunKm() != null){
                    //日总行驶时间
                    daySpeedAvg = obj.getDayRunKm()/obj.getDayTravelTime();
                    newDaySpeedAvg = Double.parseDouble(d.format(daySpeedAvg));
                    obj.setDaySpeedAvg(newDaySpeedAvg);
                    obj.setDayTravelTime(Double.parseDouble(d.format(obj.getDayTravelTime())));
                }
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object idleVehicleCountsList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate") == null || params.get("startDate")== "")&&(params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        if (params.get("idleMileageValue") == null || params.get("idleMileageValue") == ""){
            params.put("idleMileageValue", 100);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<IdleVehicleCountModel> models = findBySqlId("idleVehicleDetails", params);
            for(IdleVehicleCountModel model : models){
                model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("idleVehicleDetails", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<IdleVehicleCountModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                IdleVehicleCountModel obj = (IdleVehicleCountModel)entry;
                obj.setVin(obj.getIsDelete() == null || "1".equals(obj.getIsDelete()) ? obj.getVin() == null ? "(已删除)" : obj.getVin() + "(已删除)" : obj.getVin());
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object idleAndRatioList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate") == null || params.get("startDate")== "")&&(params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        if (params.get("idleMileageValue") == null || params.get("idleMileageValue") == ""){
            params.put("idleMileageValue", 100);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<IdleVehicleCountModel> models = findBySqlId("idleAndMonitorVehicle", params);
            for (IdleVehicleCountModel ivc:models){
                if (ivc.getAllMonitorVehicle()!=null && ivc.getAllMonitorVehicle()>0 && ivc.getIdleVehNum() != null){
                    double num = ivc.getIdleVehNum()*100.00/ivc.getAllMonitorVehicle();
                    String newNum = String.format("%.2f",num);
                    ivc.setIdleVehRatio(newNum+"%");
                }
                else{
                    ivc.setIdleVehRatio("0%");
                }
                ivc.setStartDate(String.valueOf(params.get("startDate")));
                long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
                long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
                String endDate = new SimpleDateFormat("yyyy年MM月dd日").format(end);
                String startDate = new SimpleDateFormat("yyyy年MM月dd日").format(start);
                ivc.setStartDate(startDate);
                ivc.setEndDate(endDate);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("idleAndMonitorVehicle", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<IdleVehicleCountModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                IdleVehicleCountModel obj = (IdleVehicleCountModel)entry;
                if (obj.getAllMonitorVehicle()!=null && obj.getAllMonitorVehicle()>0 && obj.getIdleVehNum() != null){
                    double num = obj.getIdleVehNum()*100.00/obj.getAllMonitorVehicle();
                    String newNum = String.format("%.2f",num);
                    obj.setIdleVehRatio(newNum+"%");
                }
                else{
                    obj.setIdleVehRatio("0%");
                }
                obj.setStartDate(String.valueOf(params.get("startDate")));
                long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
                long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
                String endDate = new SimpleDateFormat("yyyy年MM月dd日").format(end);
                String startDate = new SimpleDateFormat("yyyy年MM月dd日").format(start);
                obj.setStartDate(startDate);
                obj.setEndDate(endDate);
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object vehicleMonitoringList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate" )== null || params.get("startDate")=="") && (params.get("endDate") == null || params.get("endDate") == "")){
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<VehicleMonitoringModel> models = findBySqlId("vehicleMonitoring", params);
            for (VehicleMonitoringModel vehicleMonitoringModel : models){
                if (vehicleMonitoringModel.getAllVehicle()!=null && vehicleMonitoringModel.getAllVehicle()>0){
                    if (vehicleMonitoringModel.getDayActiveCount()==null){
                        vehicleMonitoringModel.setDayActiveCount(0);
                    }
                    if (vehicleMonitoringModel.getNotOnlineCount()==null){
                        vehicleMonitoringModel.setNotOnlineCount(0);
                    }
                    if (vehicleMonitoringModel.getAbnormalMileageCount()==null){
                        vehicleMonitoringModel.setAbnormalMileageCount(0);
                    }
                    double activeRatio = vehicleMonitoringModel.getDayActiveCount()*100.00 / vehicleMonitoringModel.getAllVehicle();
                    double monitoringRatio = (vehicleMonitoringModel.getAllVehicle()- vehicleMonitoringModel.getNotOnlineCount())*100.00/vehicleMonitoringModel.getAllVehicle();
                    double abnormalMileageRatio = vehicleMonitoringModel.getAbnormalMileageCount()*100.00/ vehicleMonitoringModel.getAllVehicle();
                    //活跃率
                    vehicleMonitoringModel.setActiveRatio(String.format("%.2f",activeRatio)+"%");
                    //正常监控比例
                    vehicleMonitoringModel.setMonitoringRatio(String.format("%.2f",monitoringRatio)+"%");
                    //里程异常比例

                    vehicleMonitoringModel.setAbnormalMileageRatio(String.format("%.2f",abnormalMileageRatio)+"%");
                }
                if (vehicleMonitoringModel.getDayActiveCount()!=null && vehicleMonitoringModel.getDayActiveCount()>0){
                    //单车日均活跃时间
                    double onceDailyActiveTotalTime =Double.parseDouble(String.format("%.2f",vehicleMonitoringModel.getAllDailyActiveTotalTime()*1.0/vehicleMonitoringModel.getDayActiveCount()));
                    vehicleMonitoringModel.setOnceDailyActiveTotalTime(onceDailyActiveTotalTime);
                }
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("vehicleMonitoring", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehicleMonitoringModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                VehicleMonitoringModel obj = (VehicleMonitoringModel)entry;
                if (obj.getAllVehicle()!=null && obj.getAllVehicle()>0){
                    if (obj.getDayActiveCount()==null){
                        obj.setDayActiveCount(0);
                    }
                    if (obj.getNotOnlineCount()==null){
                        obj.setNotOnlineCount(0);
                    }
                    if (obj.getAbnormalMileageCount()==null){
                        obj.setAbnormalMileageCount(0);
                    }
                    double activeRatio = obj.getDayActiveCount()*100.00 / obj.getAllVehicle();
                    double monitoringRatio = (obj.getAllVehicle()- obj.getNotOnlineCount())*100.00/obj.getAllVehicle();
                    double abnormalMileageRatio = obj.getAbnormalMileageCount()*100.00/ obj.getAllVehicle();
                    //活跃率
                    obj.setActiveRatio(String.format("%.2f",activeRatio)+"%");
                    //正常监控比例
                    obj.setMonitoringRatio(String.format("%.2f",monitoringRatio)+"%");
                    //里程异常比例
                    obj.setAbnormalMileageRatio(String.format("%.2f",abnormalMileageRatio)+"%");
                }
                if (obj.getDayActiveCount()!=null && obj.getDayActiveCount()>0){
                    //单车日均活跃时间
                    double onceDailyActiveTotalTime =Double.parseDouble(String.format("%.2f",obj.getAllDailyActiveTotalTime()*1.0/obj.getDayActiveCount()));
                    obj.setOnceDailyActiveTotalTime(onceDailyActiveTotalTime);
                }
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object vehicleByMileageAbnormalsList (PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit()<0){
            List<VehicleDetailsModel> models = new ArrayList<>();
            if (String.valueOf(params.get("type")).equals("1")){
                models = findBySqlId("vehicleByAll", params);
            }
            else if(String.valueOf(params.get("type")).equals("2")){
                models = findBySqlId("vehicleByNew", params);
            }
            else if(String.valueOf(params.get("type")).equals("3")){
                models = findBySqlId("vehicleByActive", params);
            }
            else if(String.valueOf(params.get("type")).equals("4")){
                models = findBySqlId("vehicleByOnlineAbnormal", params);
            }
            else if(String.valueOf(params.get("type")).equals("5")){
                models = findBySqlId("vehicleByMileageAbnormal", params);
            }
            for (VehicleDetailsModel model : models){
                model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = new PagerResult();
            if (String.valueOf(params.get("type")).equals("1")){
                pr = findPagerModel("vehicleByAll", params, pagerInfo.getStart(), pagerInfo.getLimit());
            }
            else if(String.valueOf(params.get("type")).equals("2")){
                pr = findPagerModel("vehicleByNew", params, pagerInfo.getStart(), pagerInfo.getLimit());
            }
            else if(String.valueOf(params.get("type")).equals("3")){
                pr = findPagerModel("vehicleByActive", params, pagerInfo.getStart(), pagerInfo.getLimit());
            }
            else if(String.valueOf(params.get("type")).equals("4")){
                pr = findPagerModel("vehicleByOnlineAbnormal", params, pagerInfo.getStart(), pagerInfo.getLimit());
            }
            else if(String.valueOf(params.get("type")).equals("5")){
                pr = findPagerModel("vehicleByMileageAbnormal", params, pagerInfo.getStart(), pagerInfo.getLimit());
            }
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

    /**
     * 通过年份和月份获取每月最后一天的日期
     * @return
     */
    public static String getdate(int year,int month){
        int day=0;
        switch (month){
            case  1:
                day=31;
                break;
            case  2:
                if(year%4==0){
                    if(year%100==0&&year%400!=0){
                        day=28;
                    }else{
                        day=29;
                    }
                }else{
                    day=28;
                }

                break;
            case  3:
                day=31;
                break;
            case  4:
                day=30;
                break;
            case  5:
                day=31;
                break;
            case  6:
                day=30;
                break;
            case  7:
                day=31;
                break;
            case  8:
                day=31;
                break;
            case  9:
                day=30;
                break;
            case  10:
                day=31;
                break;
            case  11:
                day=30;
                break;
            case  12:
                day=31;
                break;

            default:
                throw new BusinessException("获取月份错误");
        }

        return year+"-"+month+"-"+day;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
        params.put("endDate",end);
        DecimalFormat d = new DecimalFormat();
        d.applyPattern("#######.##");
        new ExcelExportHandler<DayreportSummary>(this, "pagerModel", params, "veh/res/dayreportSummary/export.xls", "总里程统计") {
            @Override
            public Object process(DayreportSummary entity){
                entity.setLastEndMileageExport(entity.getLastEndMileage() == null ? null : d.format(entity.getLastEndMileage()));
                entity.setTotalOnlineMileageExport(entity.getTotalOnlineMileage() == null ? null : d.format(entity.getTotalOnlineMileage()));
                entity.setTotalGpsMileageExport(entity.getTotalGpsMileage() == null ? null : d.format(entity.getTotalGpsMileage()));
                entity.setTotalValidMileageExport(entity.getTotalValidMileage() == null ? null : d.format(entity.getTotalValidMileage()));
                entity.setTotalCheckMileageExport(entity.getTotalCheckMileage() == null ? null : d.format(entity.getTotalCheckMileage()));
                return super.process(entity);
            }
        }.work();
        return;


    }

    @Override
    public void mileageMonitorExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (pagerInfo.getConditions().size() == 1 && pagerInfo.getConditions().get(0).getName().equals("operUnitId")){
            new ExcelExportHandler<MileageMonitorModel>(this, "countMileageMonthlyByUnit", params, "veh/res/dayreportSummary/mileageMonitorExport.xls", "单位里程分布统计") {
                @Override
                public Object process(MileageMonitorModel entity) {
                    return entity;
                }
            }.work();
            return;
        }
        else{
            if (params.get("endTime") == null || params.get("endTime")== ""){
                String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
                params.put("endTime", now);
                String[] time1 = now.split("-");
                String time2 = "";
                for(int i = 0;i < 2;i++){
                    time2 += (time1[i]+"-");
                }
                time2+="01 00:00:00";
                String date2 = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(time2).getTime() - 86400000);
                params.put("date2",date2);
            }
            new ExcelExportHandler<MileageMonitorModel>(this, "countMileageMonthly", params, "veh/res/dayreportSummary/mileageMonitorExport.xls", "里程分布统计") {
                @Override
                public Object process(MileageMonitorModel entity) {
                    return entity;
                }
            }.work();
            return;
        }

    }

    @Override
    public void mileageMonitorByUnitListExport(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<MileageMonitorModel>(this, "countMileageMonthlyByUnit", params, "veh/res/dayreportSummary/mileageMonitorExport.xls", "单位里程分布统计") {
            @Override
            public Object process(MileageMonitorModel entity) {
                return entity;
            }
        }.work();
        return;
    }


    @Override
    public void vehicleDetailsExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //单位月车辆详情
        if (String.valueOf(params.get("endTime")).length()==7 && params.get("operUnitId") != null) {
            //有小许bug 传入的yyyy-mm  还需带dd（随便哪天都行），目前后端处理下
            params.put("endTime",String.valueOf(params.get("endTime"))+"-01");
            String name = "";
            if (params.get("data2") != null && String.valueOf(params.get("data2")).equals("500")) {
                params.put("data55", "data55");
                name = "小于0.05";
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("500")) {
                params.put("data44", "data44");
                name = "0.05~1";
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("10000")) {
                params.put("data33", "data33");
                name = "1~2";
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("20000")) {
                params.put("data22", "data22");
                name = "2~3";
            }
            else if (params.get("data1") != null && String.valueOf(params.get("data1")).equals("30000")) {
                params.put("data11", "data11");
                name="大于等于3";
            }
            Map<String,Object> headerData = new HashMap<>();
            headerData.put("stageName", params.get("stage") == null ? "全部" : dictService.getDictName(String.valueOf(params.get("stage")), "VEHICLE_STAGE_TYPE"));
            headerData.put("lastEndMileage",params.get("lastEndMileage") == null ? null : String.valueOf(params.get("lastEndMileage")));
            headerData.put("totalOnlineMileage",params.get("totalOnlineMileage") == null ? null : String.valueOf(params.get("totalOnlineMileage")));
            headerData.put("totalGpsMileage",params.get("totalGpsMileage") == null ? null : String.valueOf(params.get("totalGpsMileage")));
            headerData.put("totalValidMileage",params.get("totalValidMileage") == null ? null : String.valueOf(params.get("totalValidMileage")));
            headerData.put("totalCheckMileage",params.get("totalCheckMileage") == null ? null : String.valueOf(params.get("totalCheckMileage")));
            ExcelExportHandler handler = new ExcelExportHandler<DetailsVehicleModel>(this, "detailsVehicleByUnit", params, "veh/res/dayreportSummary/vehicleDetailsExport.xls", name+"万公里车辆详情") {
                @Override
                public Object process(DetailsVehicleModel entity) {
                    entity.setVin(entity.getIsDelete() == null || "1".equals(entity.getIsDelete()) ? entity.getVin() == null ? "(已删除)" : entity.getVin() + "(已删除)" : entity.getVin());
                    return entity;
                }
            };
            handler.putHeader(headerData);
            handler.setListRowIndex(2);
            handler.work();
            return;
        }
//        if (params.get("type") != null && params.get("operUnitId") != null){
//            String name = "";
//            if (params.get("type").equals("1")){
//                params.put("data1","1");
//                name="大于等于3";
//            }
//            else if (params.get("type").equals("2")){
//                params.put("data2","2");
//                name = "2~3";
//            }
//            else if (params.get("type").equals("3")){
//                params.put("data3","3");
//                name = "1~2";
//            }
//            else if (params.get("type").equals("4")){
//                params.put("data4","4");
//                name = "0.05~1";
//            }
//            else if (params.get("type").equals("5")){
//                params.put("data5","5");
//                name = "小于0.05";
//            }
//            new ExcelExportHandler<DetailsVehicleModel>(this, "detailsVehicleByUnit", params, "veh/res/dayreportSummary/vehicleDetailsExport.xls", name+"万公里车辆详情") {
//                @Override
//                public Object process(DetailsVehicleModel entity) {
//                    return entity;
//                }
//            }.work();
//            return;
//        }
        else{

            String name = "";
            if (params.get("endTime") == null || params.get("endTime")== "") {
                String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
                params.put("endTime", now);
            }
            if("500".equals(String.valueOf(params.get("data2")))){
                params.put("data3","1");
                params.remove("data1");
                name = "小于0.05";
            }
            if ("30000".equals(String.valueOf(params.get("data1")))){
                params.remove("data2");
                name = "大于等于3";
            }
            if ((name.equals("")) &&((params.get("data1") !=null && !String.valueOf(params.get("data1")).equals("")) || (params.get("data2") !=null && !String.valueOf(params.get("data2")).equals(""))) ){
                if (String.valueOf(params.get("data1")).equals("500")){
                    name = "0.05~1";
                }else if (String.valueOf(params.get("data1")).equals("10000")){
                    name = "1~2";
                }else{
                    name = "2~3";
                }
            }
            Map<String,Object> headerData = new HashMap<>();
            headerData.put("stageName", params.get("stage") == null ? "全部" : dictService.getDictName(String.valueOf(params.get("stage")), "VEHICLE_STAGE_TYPE"));
            headerData.put("lastEndMileage",params.get("lastEndMileage") == null ? null : String.valueOf(params.get("lastEndMileage")));
            headerData.put("totalOnlineMileage",params.get("totalOnlineMileage") == null ? null : String.valueOf(params.get("totalOnlineMileage")));
            headerData.put("totalGpsMileage",params.get("totalGpsMileage") == null ? null : String.valueOf(params.get("totalGpsMileage")));
            headerData.put("totalValidMileage",params.get("totalValidMileage") == null ? null : String.valueOf(params.get("totalValidMileage")));
            headerData.put("totalCheckMileage",params.get("totalCheckMileage") == null ? null : String.valueOf(params.get("totalCheckMileage")));
            ExcelExportHandler handler = new ExcelExportHandler<DetailsVehicleModel>(this, "detailsVehicle", params, "veh/res/dayreportSummary/vehicleDetailsExport.xls", name+"万公里车辆详情") {
                @Override
                public Object process(DetailsVehicleModel entity) {
                    entity.setVin(entity.getIsDelete() == null || "1".equals(entity.getIsDelete()) ? entity.getVin() == null ? "(已删除)" : entity.getVin() + "(已删除)" : entity.getVin());
                    return entity;
                }
            };
            handler.putHeader(headerData);
            handler.setListRowIndex(2);
            handler.work();
            return;
        }

    }

    @Override
    public void vehicleDetailsByUnitListExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String name = "";
        if (params.get("type").equals("1")){
            params.put("data1","1");
            name="大于等于3";
        }
        else if (params.get("type").equals("2")){
            params.put("data2","2");
            name = "2~3";
        }
        else if (params.get("type").equals("3")){
            params.put("data3","3");
            name = "1~2";
        }
        else if (params.get("type").equals("4")){
            params.put("data4","4");
            name = "0.05~1";
        }
        else if (params.get("type").equals("5")){
            params.put("data5","5");
            name = "小于0.05";
        }
        new ExcelExportHandler<DetailsVehicleModel>(this, "detailsVehicleByUnit", params, "veh/res/dayreportSummary/vehicleDetailsExport.xls", name+"万公里车辆详情") {
            @Override
            public Object process(DetailsVehicleModel entity) {
                return entity;
            }
        }.work();
        return;
    }


    @Override
    public void vehicleTravelExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate") == null || params.get("startDate")== "") && (params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 10*86400000));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        new ExcelExportHandler<VehicleTravelModel>(this, "pagerMode2", params, "veh/res/dayreportSummary/vehicleTravelExport.xls", "车辆行驶情况统计") {
            @Override
            public Object process(VehicleTravelModel entity) {
                //处理小数点后两位
                DecimalFormat d = new DecimalFormat();
                d.applyPattern("#.00");
                double onceRunTimeAvg = 0,onceRunKmAvg = 0,daySpeedAvg = 0;
                if (entity.getVehOnlineCount() != null && entity.getVehOnlineCount()>0 && entity.getDayTravelTime() != null){
                    onceRunTimeAvg = entity.getDayTravelTime()/entity.getVehOnlineCount();
                    //单车平均行驶时间
                    entity.setOnceRunTimeAvgExport(d.format(onceRunTimeAvg));
                }
                if (entity.getVehOnlineCount() != null && entity.getVehOnlineCount()>0 && entity.getDayRunKm() != null){
                    onceRunKmAvg = entity.getDayRunKm()/entity.getVehOnlineCount();
                    //单车平均行驶里程
                    entity.setOnceRunKmAvgExport(d.format(onceRunKmAvg));
                }
                if (entity.getDayTravelTime()!=null && entity.getDayTravelTime()>0 && entity.getDayRunKm() != null){
                    daySpeedAvg = entity.getDayRunKm()/entity.getDayTravelTime();
                    //日总行驶时间
                    entity.setDaySpeedAvgExport(d.format(daySpeedAvg));
                    entity.setDayTravelTimeExport(d.format(entity.getDayTravelTime()));
                }
                return entity;
            }
        }.work();
        return;
    }

    @Override
    public void idleVehicleCountsExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate") == null || params.get("startDate")== "")&&(params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        if (params.get("idleMileageValue") == null || params.get("idleMileageValue") == ""){
            params.put("idleMileageValue", 100);
        }
        new ExcelExportHandler<IdleVehicleCountModel>(this, "idleVehicleDetails", params, "veh/res/dayreportSummary/idleVehicleCountsExport.xls", "闲置车辆详情") {
            DecimalFormat d = new DecimalFormat("#######.##");
            @Override
            public Object process(IdleVehicleCountModel entity) {
                entity.setVin(entity.getIsDelete() == null || "1".equals(entity.getIsDelete()) ? entity.getVin() == null ? "(已删除)" : entity.getVin() + "(已删除)" : entity.getVin());
                entity.setIdleMileageExport(entity.getIdleMileage() == null ? null : d.format(entity.getIdleMileage()));
                if (entity.getLastEndMileage() != null){
                    entity.setLastEndMileageExport(d.format(entity.getLastEndMileage()));
                }
                return entity;
            }
        }.work();
        return;
    }

    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "闲置车辆详情";

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
        if ((params.get("startDate") == null || params.get("startDate")== "")&&(params.get("endDate") == null || params.get("endDate")== "")) {
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate", endDate);
            }
        }
        if (params.get("idleMileageValue") == null || params.get("idleMileageValue") == ""){
            params.put("idleMileageValue", 100);
        }
        final String excelTemplateFile = "veh/res/dayreportSummary/idleVehicleCountsExport.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.dayreportSummaryMapper::idleVehicleDetails,
            params,
            this::fromEntityToModel,
            this.dayreportSummaryMapper::idleVehicleDetails,
            redis,
            ws
        );
    }

    @NotNull
    private List<IdleVehicleCountModel> fromEntityToModel(@NotNull final List<IdleVehicleCountModel> entities) {

        final List<IdleVehicleCountModel> models = Lists.newArrayList();

        for (final IdleVehicleCountModel entity : entities) {

            final IdleVehicleCountModel model = entity;

            model.setVin(model.getIsDelete() == null || "1".equals(model.getIsDelete()) ? model.getVin() == null ? "(已删除)" : model.getVin() + "(已删除)" : model.getVin());
            DataLoader.loadNames(model);
            DecimalFormat d = new DecimalFormat("#######.##");
            model.setIdleMileageExport(model.getIdleMileage() == null ? null : d.format(model.getIdleMileage()));
            if (model.getLastEndMileage() != null){
                model.setLastEndMileageExport(d.format(model.getLastEndMileage()));
            }

            models.add(model);
        }

        DataLoader.loadNames(models);

        return models;
    }

    @Override
    public void vehicleMonitoringExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate" )== null || params.get("startDate")=="") && (params.get("endDate") == null || params.get("endDate") == "")){
            String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000*10));
            params.put("startDate", start);
            params.put("endDate", end);
        }
        //判断时间跨度是否超过31天，超过31天默认查询前10天
        else if (params.get("startDate")!=null && params.get("endDate") != null){
            long end = DateUtil.strToDate_ex_full(String.valueOf(params.get("endDate"))+" 00:00:00").getTime();
            long start = DateUtil.strToDate_ex_full(String.valueOf(params.get("startDate"))+" 00:00:00").getTime();
            if (((end - start)/86400000) >= 31){
                String startDate = String.valueOf(params.get("startDate"))+" 00:00:00";
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.strToDate_ex_full(startDate).getTime() + 86400000*9);
                params.put("endDate",endDate);
            }
        }
        new ExcelExportHandler<VehicleMonitoringModel>(this, "vehicleMonitoring", params, "veh/res/dayreportSummary/vehicleMonitoringExport.xls", "车辆监控情况统计") {
            @Override
            public Object process(VehicleMonitoringModel entity) {
                if (entity.getAllVehicle()!=null && entity.getAllVehicle()>0){
                    if (entity.getDayActiveCount()==null){
                        entity.setDayActiveCount(0);
                    }
                    if (entity.getNotOnlineCount()==null){
                        entity.setNotOnlineCount(0);
                    }
                    if (entity.getAbnormalMileageCount()==null){
                        entity.setAbnormalMileageCount(0);
                    }
                    double activeRatio = entity.getDayActiveCount()*100.00 / entity.getAllVehicle();
                    double monitoringRatio = (entity.getAllVehicle()- entity.getNotOnlineCount())*100.00/entity.getAllVehicle();
                    double abnormalMileageRatio = entity.getAbnormalMileageCount()*100.00/ entity.getAllVehicle();
                    //活跃率
                    entity.setActiveRatio(String.format("%.2f",activeRatio)+"%");
                    //正常监控比例
                    entity.setMonitoringRatio(String.format("%.2f", monitoringRatio) + "%");
                    //里程异常比例
                    entity.setAbnormalMileageRatio(String.format("%.2f", abnormalMileageRatio) + "%");
                }
                if (entity.getDayActiveCount()!=null && entity.getDayActiveCount()>0){
                    //单车日均活跃时间
                    double onceDailyActiveTotalTime =Double.parseDouble(String.format("%.2f",entity.getAllDailyActiveTotalTime()*1.0/entity.getDayActiveCount()));
                    entity.setOnceDailyActiveTotalTime(onceDailyActiveTotalTime);
                }
                return entity;
            }
        }.work();
        return;
    }


    @Override
    public void vehicleByMileageAbnormalsExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String sqlName = "";
        String name = "";
        if (String.valueOf(params.get("type")).equals("1")){
            sqlName = "vehicleByAll";
            name = "车辆录入总数";
        }
        else if (String.valueOf(params.get("type")).equals("2")){
            sqlName = "vehicleByNew";
            name = "新增录入";
        }
        else if (String.valueOf(params.get("type")).equals("3")){
            sqlName = "vehicleByActive";
            name = "当天活跃";
        }
        else if (String.valueOf(params.get("type")).equals("4")){
            sqlName = "vehicleByOnlineAbnormal";
            name = "通讯异常";
        }
        else if (String.valueOf(params.get("type")).equals("5")){
            sqlName = "vehicleByMileageAbnormal";
            name = "里程异常";
        }
        new ExcelExportHandler<VehicleDetailsModel>(this, sqlName, params, "veh/res/dayreportSummary/vehicleByMileageAbnormalsExport.xls", name+"车辆详情") {
            @Override
            public Object process(VehicleDetailsModel entity) {
                entity.setVin(entity.getIsDelete() == null || "1".equals(entity.getIsDelete()) ? entity.getVin() == null ? "(已删除)" : entity.getVin() + "(已删除)" : entity.getVin());
                return entity;
            }
        }.work();
        return;
    }


    /**
     *
     * @param type
     * @param reportDate
     * @return
     */
    @Override
    public Object get(String type,String reportDate){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.put("endDate",reportDate);
        List<VehicleDetailsModel> models = null;
        if (type.equals("1")){
            models = findBySqlId("vehicleByAll", params);
        }
        else if(type.equals("2")){
            models = findBySqlId("vehicleByNew", params);
        }
        else if(type.equals("3")){
            models = findBySqlId("vehicleByActive", params);
        }
        else if(type.equals("4")){
            models = findBySqlId("vehicleByOnlineAbnormal", params);
        }
        return models;
    }

    @Override
    public Object findByUnitId(String operUnitId){

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("operUnitId",operUnitId);
        List<MileageMonitorModel> models = findBySqlId("countMileageMonthlyByUnit",params);
        return models;
    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("总里程统计导入模板.xls", "VIN", new String[]{"LSB123214124214"});
    }
}
