package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.client.das.AbnormalClient;
import com.bitnei.cloud.common.client.model.AbnormalDetail;
import com.bitnei.cloud.common.client.model.AbnormalDetailModel;
import com.bitnei.cloud.common.client.model.PageResult;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.veh.domain.DayreportAbnormalSituation;
import com.bitnei.cloud.veh.model.AbnormalRecordModel;
import com.bitnei.cloud.veh.model.DayreportAbnormalSituationModel;
import com.bitnei.cloud.veh.service.IDayreportAbnormalSituationService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportAbnormalSituationService实现<br>
* 描述： DayreportAbnormalSituationService实现<br>
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
* <td>2019-03-22 11:01:58</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DayreportAbnormalSituationMapper" )
public class DayreportAbnormalSituationService extends BaseService implements IDayreportAbnormalSituationService {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private AbnormalClient abnormalClient;

    @Autowired
    private IVehicleService vehicleService;

    /**
     * 异常车辆统计
     * @param pagerInfo 查询条件
     * @return 统计结果
     */
    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        final Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        params.put(Constants.AUTH_SQL, DataAccessKit.getAuthSQL("sys_vehicle", "sv"));

        if ((params.get("startDate") == null || "".equals(params.get("startDate").toString())) && (params.get("endDate") == null || "".equals(params.get("endDate").toString()))){
            try{
                final Date date = DateUtils.parseDate(DateUtil.getShortDate(), DATE_PATTERN);
                final Date startDate = DateUtils.addDays(date, -7);
                final Date endDate = DateUtils.addDays(date, -1);
                params.put("startDate", DateUtil.formatTime(startDate, DATE_PATTERN));
                params.put("endDate", DateUtil.formatTime(endDate, DATE_PATTERN));
                } catch (final Exception ignore) {
            throw new BusinessException("string类型转换为date类型失败");
            }
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            final List<DayreportAbnormalSituation> entities = findBySqlId("pagerModel", params);

            final List<DayreportAbnormalSituationModel> models = Lists.newArrayList();
            for (DayreportAbnormalSituation entity : entities) {
                final DayreportAbnormalSituationModel model = DayreportAbnormalSituationModel.fromEntry(entity);
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            final PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            final List<DayreportAbnormalSituationModel> models = Lists.newArrayList();
            for (Object obj : pr.getData()) {
                final DayreportAbnormalSituation entity = (DayreportAbnormalSituation) obj;
                final DayreportAbnormalSituationModel model = DayreportAbnormalSituationModel.fromEntry(entity);
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    /**
     * 需传参数type，vin，beginTime，firstRegTime，endTime
     * @param pagerInfo
     * @return
     */
    @Override
    public Object list2(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("vin",String.valueOf(params.get("vin")).replaceFirst("\\(已删除\\)", ""));
        List<AbnormalRecordModel> lists = new ArrayList<>();
        //参数 vin,type,startDate,endDate 格式： 年-月-日
        if ((params.get("type") != null && !String.valueOf(params.get("type")).equals("4"))){
            String vin = String.valueOf(params.get("vin"));
            VehicleModel vehicle = vehicleService.getByVin(vin);
            String vid = vehicle.getUuid();
            String type = String.valueOf(params.get("type"));
            if ((params.get("startDate") == null || "".equals(params.get("startDate").toString())) && (params.get("endDate") == null || "".equals(params.get("endDate").toString()))){
                try{
                    final Date date = DateUtils.parseDate(DateUtil.getShortDate(), DATE_PATTERN);
                    final Date startDate = DateUtils.addDays(date, -7);
                    final Date endDate = DateUtils.addDays(date, -1);
                    params.put("startDate",DateUtil.formatTime(startDate, TIME_PATTERN));
                    params.put("endDate",DateUtil.formatTime(endDate, DATE_PATTERN) + " 23:59:59");
                } catch (final Exception ignore) {
                    throw new BusinessException("string类型转换为date类型失败");
                }
            }
            else{
                params.put("startDate",params.get("startDate").toString()+" 00:00:00");
                params.put("endDate",params.get("endDate").toString()+" 23:59:59");
            }

            AbnormalDetailModel abnormalDetailModel = new AbnormalDetailModel();
            abnormalDetailModel.setVid(vid);
            abnormalDetailModel.setType(type);
            abnormalDetailModel.setStartTime(params.get("startDate").toString());
            abnormalDetailModel.setEndTime(params.get("endDate").toString());
            // 每页条数
            int size = Integer.MAX_VALUE;
            if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

                abnormalDetailModel.setPageSize(size);
                PageResult<AbnormalDetail> pageResult = abnormalClient.details(abnormalDetailModel);
                List<AbnormalDetail> list = pageResult.getData();
                for (AbnormalDetail abnormalDetail:list){
                    String lng = abnormalDetail.getLon();
                    String lat = abnormalDetail.getLat();
                    String address = GpsUtil.getAddress(lng,lat);
                    String reportDate = DateUtil.getDate(DateUtil.strToDate_ex(abnormalDetail.getUploadTime()).getTime());
                    AbnormalRecordModel model = new AbnormalRecordModel();
                    model.setAddress(address);
                    model.setReportDate(reportDate);
                    lists.add(model);
                }
                return lists;
            }
            //分页查询
            else{
                size = pagerInfo.getLimit();
                abnormalDetailModel.setPageNo(pagerInfo.getStart()/size+1);
                abnormalDetailModel.setPageSize(size);
                PageResult<AbnormalDetail> pageResult = abnormalClient.details(abnormalDetailModel);
                List<AbnormalDetail> list = pageResult.getData();
                for (AbnormalDetail abnormalDetail:list){
                    String lng = abnormalDetail.getLon();
                    String lat = abnormalDetail.getLat();
                    String address = GpsUtil.getAddress(lng,lat);
                    String reportDate = DateUtil.getDate(DateUtil.strToDate_ex(abnormalDetail.getUploadTime()).getTime());
                    AbnormalRecordModel model = new AbnormalRecordModel();
                    model.setAddress(address);
                    model.setReportDate(reportDate);
                    lists.add(model);
                }
                PagerResult pr = new PagerResult();
                pr.setData(Collections.singletonList(lists));
                pr.setTotal(pageResult.getTotalCount());
                return pr;
            }
        }
        else{
            //type=4,时间异常详情不通过hbas获取
            if ((params.get("startDate") == null || "".equals(params.get("startDate").toString())) && (params.get("endDate") == null || "".equals(params.get("endDate").toString()))){
                try{
                    final Date date = DateUtils.parseDate(DateUtil.getShortDate(), DATE_PATTERN);
                    final Date startDate = DateUtils.addDays(date, -7);
                    final Date endDate = DateUtils.addDays(date, -1);
                    params.put("startDate",DateUtil.formatTime(startDate, TIME_PATTERN));
                    params.put("endDate",DateUtil.formatTime(endDate, DATE_PATTERN) + " 23:59:59");
                } catch (final Exception ignore) {
                    throw new BusinessException("string类型转换为date类型失败");
                }
            }
            else{
                params.put("startDate",params.get("startDate").toString()+" 00:00:00");
                params.put("endDate",params.get("endDate").toString()+" 23:59:59");
            }
            if ((pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0) && (params.get("type") != null && String.valueOf(params.get("type")).equals("4") )){

                List<AbnormalRecordModel> models = findBySqlId("pagerModel3", params);
                for (AbnormalRecordModel data:models){
                    Object mapObject = data.getLngLat();
                    if (mapObject !=null && !mapObject.equals("")){
                        String[] arrays = String.valueOf(mapObject).split(",");
                        //处理位置问题
                        String address = GpsUtil.getAddress(arrays[0],arrays[1]);
                        data.setAddress(address);
                    }
                }
                return models;
            }
            //分页查询
            else {
                PagerResult pr = findPagerModel("pagerModel3", params, pagerInfo.getStart(), pagerInfo.getLimit());
                List<AbnormalRecordModel> models = new ArrayList();
                for (Object entry:pr.getData()){
                    AbnormalRecordModel obj = (AbnormalRecordModel) entry;
                    Object mapObject = obj.getLngLat();
                    if (mapObject !=null && !mapObject.equals("")){
                        String[] arrays = String.valueOf(mapObject).split(",");
                        //处理位置问题
                        String address = GpsUtil.getAddress(arrays[0],arrays[1]);
                        obj.setAddress(address);
                        models.add(obj);
                    }
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }

    }

    //异常车辆位置详情导出
    @Override
    public void export2(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //初始的vin
        String baseVin = String.valueOf(params.get("vin"));
        params.put("vin",String.valueOf(params.get("vin")).replaceFirst("\\(已删除\\)", ""));
        String vin = String.valueOf(params.get("vin"));
        String licensePlate = String.valueOf(params.get("licensePlate"));
        String noticeName = String.valueOf(params.get("noticeName"));
        String typeName = "";
        if (params.get("type").equals("4")){
            typeName = "时间";
            if ((params.get("startDate") == null || "".equals(params.get("startDate").toString())) && (params.get("endDate") == null || "".equals(params.get("endDate").toString()))){
                try{
                    final Date date = DateUtils.parseDate(DateUtil.getShortDate(), DATE_PATTERN);
                    final Date startDate = DateUtils.addDays(date, -7);
                    final Date endDate = DateUtils.addDays(date, -1);
                    params.put("startDate",DateUtil.formatTime(startDate, TIME_PATTERN));
                    params.put("endDate",DateUtil.formatTime(endDate, DATE_PATTERN) + " 23:59:59");
                } catch (final Exception ignore) {
                    throw new BusinessException("string类型转换为date类型失败");
                }
            }
            else{
                params.put("startDate",params.get("startDate").toString()+" 00:00:00");
                params.put("endDate",params.get("endDate").toString()+" 23:59:59");
            }
            Map<String, Object> headerData = new HashMap<>();
            headerData.put("vin",vin);
            headerData.put("licensePlate",licensePlate);
            headerData.put("noticeName",noticeName);
            ExcelExportHandler handler = new ExcelExportHandler<AbnormalRecordModel>(this, "pagerModel3", params, "veh/res/dayreportAbnormalSituation/abnormalRecordExport.xls", typeName+"异常记录详情") {
                @Override
                public Object process(AbnormalRecordModel entity) {
                    return entity;
                }
            };
            handler.putHeader(headerData);
            handler.setListRowIndex(2);
            handler.work();
        }
        else{
            if(params.get("type").equals("1")){
                typeName = "车速";
            }
            else if(params.get("type").equals("2")){
                typeName = "里程";
            }
            else if(params.get("type").equals("3")){
                typeName = "经纬度";
            }
            else if(params.get("type").equals("5")){
                typeName = "总电压";
            }
            else if(params.get("type").equals("6")){
                typeName = "总电流";
            }
            else if(params.get("type").equals("7")){
                typeName = "SOC";
            }
            String name = typeName+"异常记录详情";
            Map<String, Object> headerData = new HashMap<>();
            headerData.put("vin",baseVin);
            headerData.put("licensePlate",licensePlate);
            headerData.put("noticeName",noticeName);
            pagerInfo.setLimit(-1);
            List<AbnormalRecordModel> data = (List<AbnormalRecordModel>)this.list2(pagerInfo);
            ArrayList objects = new ArrayList();
            if (data != null && data.size() != 0){
                for (AbnormalRecordModel model : data) {
                    objects.add(model);
                }
            }
            DataLoader.loadNames(objects);
            String var7 = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
            String var8 = var7 + "veh/res/dayreportAbnormalSituation/abnormalRecordExport.xls";
            ExcelData ed = new ExcelData();
            ed.setTitle(name);
            ed.setExportTime(DateUtil.getNow());
            ed.setHeaderData(headerData);
            ed.setListRowIndex(2);
            ed.setData(objects);
            String outName = String.format(name+"-导出-%s.xls", DateUtil.getShortDate());
            EasyExcel.renderResponse(var8, outName, ed);
        }
    }

    @Override
    public DayreportAbnormalSituationModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_abnormal_situation", "vdas");
        params.put("id",id);
        DayreportAbnormalSituation entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DayreportAbnormalSituationModel.fromEntry(entry);
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        if ((params.get("startDate") == null || "".equals(params.get("startDate").toString())) && (params.get("endDate") == null || "".equals(params.get("endDate").toString()))){
            try{
                final Date date = DateUtils.parseDate(DateUtil.getShortDate(), DATE_PATTERN);
                final Date startDate = DateUtils.addDays(date, -7);
                final Date endDate = DateUtils.addDays(date, -1);
                params.put("startDate", DateUtil.formatTime(startDate, DATE_PATTERN));
                params.put("endDate", DateUtil.formatTime(endDate, DATE_PATTERN));
            } catch (final Exception ignore) {
                throw new BusinessException("string类型转换为date类型失败");
            }
        }
        new ExcelExportHandler<DayreportAbnormalSituation>(this, "pagerModel", params, "veh/res/dayreportAbnormalSituation/export.xls", "异常车辆统计") {
            @Override
            public Object process(DayreportAbnormalSituation entity) {
                if (entity.getReportDate() !=null ){
                    entity.setLastDate(DateUtil.getDate(entity.getReportDate().getTime()));
                }
                return super.process(entity);
            }
        }.work();

        return;
    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("异常车辆统计导入模板.xls", "VIN", new String[]{"LSB123214124214"});
    }
}
