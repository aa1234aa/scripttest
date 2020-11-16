package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.client.das.RunStateClient;
import com.bitnei.cloud.common.client.model.*;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.veh.model.DayReportG6ReqModel;
import com.bitnei.cloud.veh.model.DayVehDriveModel;
import com.bitnei.cloud.veh.service.IDayVehDriveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class DayVehDriveService extends BaseService implements IDayVehDriveService {

    @Autowired
    private RunStateClient runStateClient;

    @Resource
    private IVehicleService vehicleService;

    @Override
    public ResultMsg list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("reportDate") == null || String.valueOf(params.get("reportDate")).equals("")){
            String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("reportDate",reportDate);
        }
        if(params.get("importSearchValues") != null){
            Set<String> vins = (Set<String>)params.get("importSearchValues");
            String vin = StringUtils.strip(vins.toString(),"[]");
            params.put("vin",vin);
        }
        Integer maxSize = 50;
        DayReportG6ReqModel dayReportG6Req = new DayReportG6ReqModel();
        //每页条数
        if (pagerInfo.getLimit() != null && pagerInfo.getLimit()>0){
            maxSize = pagerInfo.getLimit();
            dayReportG6Req.setLimit(pagerInfo.getLimit());
            dayReportG6Req.setPageSize(maxSize);
        }
        else if (pagerInfo.getLimit() == null){
            dayReportG6Req.setLimit(-1);
            dayReportG6Req.setPageSize(200);
        }
        if (pagerInfo.getStart() == null) {
            dayReportG6Req.setStart(0);
            dayReportG6Req.setPageNo(1);
        }
        else {
            dayReportG6Req.setStart(pagerInfo.getStart());
            dayReportG6Req.setPageNo(pagerInfo.getStart()/maxSize+1);
        }
        String vin = params.get("vin") != null && !"".equals(String.valueOf(params.get("vin"))) ? String.valueOf(params.get("vin")) : null;
        dayReportG6Req.setVin(vin == null ? null : vin.split(", "));
        dayReportG6Req.setReportDate(String.valueOf(params.get("reportDate")));
        dayReportG6Req.setLicensePlate(params.get("licensePlate") != null && !"".equals(String.valueOf(params.get("licensePlate"))) ? String.valueOf(params.get("licensePlate")) : null);
        dayReportG6Req.setOperUnitId(params.get("operUnitId") != null && !"".equals(String.valueOf(params.get("operUnitId"))) ? String.valueOf(params.get("operUnitId")) : null);
        dayReportG6Req.setVehModelId(params.get("vehModelId") != null && !"".equals(String.valueOf(params.get("licensePlate"))) ? String.valueOf(params.get("vehModelId")) : null);
        dayReportG6Req.setOperLicenseCityId(params.get("operLicenseCityId") != null && !"".equals(String.valueOf(params.get("licensePlate"))) ? String.valueOf(params.get("operLicenseCityId")) : null);
        dayReportG6Req.setOilRemain(params.get("lastOilLocationRemain") != null ? Float.parseFloat(String.valueOf(params.get("lastOilLocationRemain"))) : null);
        dayReportG6Req.setReactantRemain(params.get("lastReactantRemain") != null ? Float.parseFloat(String.valueOf(params.get("lastReactantRemain"))) : null);
        GlobalResponse<ElasticsearchPageResult<DayReport>> globalResponse =  runStateClient.findDayReportPage(dayReportG6Req);
        if (globalResponse != null && globalResponse.getData() != null && !CollectionUtils.isEmpty(globalResponse.getData().getData())) {

            List<DayVehDriveModel> modelList = new ArrayList<DayVehDriveModel>();
            for (DayReport data : globalResponse.getData().getData()) {
                DayVehDriveModel model = new DayVehDriveModel();
                model.setVin(data.getVin());
                model.setLicensePlate(data.getLicensePlate());
                model.setVehModelName(data.getVehModelName());
                model.setOperUnitName(data.getOperUnitName());
                model.setOperLicenseCityId(data.getOperLicenseCityId());
                model.setEndMileage(data.getEndMileage());
                model.setMinuteLong(data.getMinuteLong());
                model.setOilConsumer(data.getOilConsumer());
                model.setMaxSpeed(data.getMaxSpeed());
                model.setAvgSpeed(data.getAvgSpeed());
                model.setLastOilLocationRemain(data.getLastOilLocationRemain());
                model.setLastReactantRemain(data.getLastReactantRemain());
                model.setCreateTime(data.getCreateTime());

                if (data.getReportDate() != null && !"".equals(data.getReportDate())){
                    model.setReportDate(newDate(data.getReportDate()));
                }
                if (data.getEndMileage() != null && data.getStartMileage() != null){
                    // 当日行驶里程
                    Float dayMileage = data.getEndMileage() - data.getStartMileage();
                    model.setDayMileage(dayMileage);
                }
                if (data.getVid() != null){
                    Map<String,String> vid = new HashMap<String,String>();
                    vid.put("vid",data.getVid());
                    Vehicle vehicle = vehicleService.unique("getInterNoByVid",vid);
                    if (vehicle != null){
                        model.setStage(vehicle.getStage());
                        model.setInterNo(vehicle.getInterNo());
                        model.setFirstTimeOnLine(vehicle.getFirstRegTime());
                    }
                }
                modelList.add(model);
            }
            PagerModel p = new PagerModel();
            p.setTotal(globalResponse.getData().getTotalCount());
            p.setRows(modelList);
            return p.toResultMsg(dayReportG6Req);
        }
        else {
            PagerModel p = new PagerModel();
            p.setTotal(0);
            p.setRows(new ArrayList());

            return p.toResultMsg(dayReportG6Req);
        }
    }

    public String newDate (String oldDate){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        String newDate = "";
        try {
            date = df.parse(oldDate);
            newDate = DateUtil.formatTime(date,"yyyy-MM-dd");
        } catch (ParseException e) {
            throw new BusinessException("String转Date类型失败");
        }
        return newDate;
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        pagerInfo.setStart(0);
        pagerInfo.setLimit(200);
        ResultListMsg resultMsg = (ResultListMsg) this.list(pagerInfo);
        ArrayList objects = new ArrayList();
        for (DayVehDriveModel model : (List<DayVehDriveModel>) resultMsg.getData()) {
            objects.add(model);
        }
        DataLoader.loadNames(objects);
        String var7 = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String var8 = var7 + "veh/res/dayVehDrive/export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("车辆行驶日报");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(objects);
        String outName = String.format("车辆行驶日报-导出-%s.xls", DateUtil.getShortDate());
        EasyExcel.renderResponse(var8, outName, ed);

    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆行驶日报导入模板.xls", "VIN", new String[]{"LSB123214124214"});
    }
}
