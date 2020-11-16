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
import com.bitnei.cloud.veh.model.DayVehAbnormalModel;
import com.bitnei.cloud.veh.service.IDayVehAbnormalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class DayVehAbnormalService extends BaseService implements IDayVehAbnormalService {

    private static final Integer reactantThreshold = 20;
    private static final Integer oilThreshold = 10;

    @Resource
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
        Integer maxSize = 200;
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

        GlobalResponse<ElasticsearchPageResult<DayReport>> globalResponse =  runStateClient.findDayReportPage(dayReportG6Req);
        if (globalResponse != null && globalResponse.getData() != null && !CollectionUtils.isEmpty(globalResponse.getData().getData())) {

            List<DayVehAbnormalModel> models = new ArrayList<DayVehAbnormalModel>();
            List<DayReport> list = globalResponse.getData().getData();
            for (DayReport data : list){
                DayVehAbnormalModel model = new DayVehAbnormalModel();
                model.setVin(data.getVin());
                model.setVid(data.getVid());
                model.setLicensePlate(data.getLicensePlate());
                model.setVehModelName(data.getVehModelName());
                model.setManuUnitName(data.getVehUnitName());
                model.setOperUnitName(data.getOperUnitName());
                model.setOperLicenseCityId(data.getOperLicenseCityId());
                if (data.getReportDate() != null && !"".equals(data.getReportDate())){
                    model.setReportDate(newDate(data.getReportDate()));
                }
                // 最后通讯时间
                if (data.getEndTime() != null && !"".equals(data.getEndTime())){
                    model.setLastCommitTime(newTime( data.getEndTime()));
                }
                // 当日异常报文条数
                model.setExceptionCodeNum(data.getExceptionCodeNum() == null ? 0 : data.getExceptionCodeNum());
                if (data.getVid() != null){
                    Map<String,String> vid = new HashMap<String,String>();
                    vid.put("vid",data.getVid());
                    Vehicle vehicle = vehicleService.unique("getInterNoByVid",vid);
                    if (vehicle != null){
                        model.setInterNo(vehicle.getInterNo());
                        model.setBrandName(vehicle.getVehBrandName());
                        model.setSeriesName(vehicle.getVehSeriesName());
                    }
                }
                // 当日异常报文条数占比；exceptionCodeNum为异常报文条数，actualLoadPacketNum为实际上传报文条数
                if (data.getExceptionCodeNum() != null && data.getActualLoadPacketNum() != null && data.getActualLoadPacketNum() > 0){
                    double ratio = data.getExceptionCodeNum()*100.0/data.getActualLoadPacketNum();
                    String abnormalRatio = String.format("%.2f",ratio)+"%";
                    model.setAbnormalRatio(abnormalRatio);
                }
                else{
                    model.setAbnormalRatio("0%");
                }
                // 反应剂液位是否过低 是为1 否为0
                if (data.getLastReactantRemain() != null && data.getLastReactantRemain() < reactantThreshold){
                    model.setReactantLow(1);
                }
                else {
                    model.setReactantLow(0);
                }
                // 油量是否过低
                if (data.getLastOilLocationRemain() != null && data.getLastOilLocationRemain() < oilThreshold){
                    model.setOilLow(1);
                }
                else {
                    model.setOilLow(0);
                }
                // 车辆行驶时间分钟 转换为 秒即为需上传报文数（1秒1条）
                int allPacketNum = 0;
                if (data.getMinuteLong() != null && data.getMinuteLong() > 0){
                    Float mins = data.getMinuteLong();
                    allPacketNum = (int)(mins * 60);
                }
                // 丢包率；actualLoadPacketNum实际上传报文数,
                if (allPacketNum > 0 && data.getActualLoadPacketNum() != null){
                    int a = data.getActualLoadPacketNum();
                    double packetNum = (allPacketNum - a)*100.0/allPacketNum;
                    String packetLoss = String.format("%.2f",packetNum)+"%";
                    model.setPacketLoss(packetLoss);
                }
                else{
                    model.setPacketLoss("0%");
                }
                models.add(model);
            }
            PagerModel p = new PagerModel();
            p.setTotal(globalResponse.getData().getTotalCount());
            p.setRows(models);
            return p.toResultMsg(dayReportG6Req);
        }
        else{
            PagerModel p = new PagerModel();
            p.setTotal(0);
            p.setRows(new ArrayList());
            return p.toResultMsg(dayReportG6Req);
        }
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        pagerInfo.setStart(0);
        pagerInfo.setLimit(200);
        ResultListMsg resultMsg = (ResultListMsg) this.list(pagerInfo);
        ArrayList objects = new ArrayList();
        for (DayVehAbnormalModel model : (List<DayVehAbnormalModel>) resultMsg.getData()) {
            objects.add(model);
        }
        DataLoader.loadNames(objects);
        String var7 = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String var8 = var7 + "veh/res/dayVehAbnormal/export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("车辆异常数据日报");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(objects);
        String outName = String.format("车辆异常数据日报-导出-%s.xls", DateUtil.getShortDate());
        EasyExcel.renderResponse(var8, outName, ed);
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

    public String newTime (String oldDate){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        String newDate = "";
        try {
            date = df.parse(oldDate);
            newDate = DateUtil.formatTime(date,"yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            throw new BusinessException("String转Date类型失败");
        }
        return newDate;
    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("车辆异常数据日报导入模板.xls", "VIN", new String[]{"LSB123214124214"});
    }
}
