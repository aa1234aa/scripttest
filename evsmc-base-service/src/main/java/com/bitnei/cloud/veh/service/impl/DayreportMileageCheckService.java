package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.domain.DayreportMileageCheck;
import com.bitnei.cloud.veh.model.DayreportMileageCheckModel;
import com.bitnei.cloud.veh.service.IDayreportMileageCheckService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportMileageCheckService实现<br>
* 描述： DayreportMileageCheckService实现<br>
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
* <td>2019-03-14 16:46:26</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.mapper.DayreportMileageCheckMapper" )
public class DayreportMileageCheckService extends BaseService implements IDayreportMileageCheckService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate" )== null || params.get("startDate")=="") && (params.get("endDate") == null || params.get("endDate") == "")){
            String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("startDate", now);
            params.put("endDate", now);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DayreportMileageCheck> entries = findBySqlId("pagerModel", params);
            List<DayreportMileageCheckModel> models = new ArrayList();
            DecimalFormat d = new DecimalFormat("#######.##%");
            for(DayreportMileageCheck entry: entries){
                DayreportMileageCheck obj = (DayreportMileageCheck)entry;
                if (obj.getReportDate() != null){
                    obj.setShowReportDate(new SimpleDateFormat("yyyy-MM-dd").format(obj.getReportDate().getTime()));
                }
                obj.setShowValidGpsDeviation(obj.getValidGpsDeviation() == null ? null : d.format(obj.getValidGpsDeviation()));
                obj.setShowOnlineValidDeviation(obj.getOnlineValidDeviation() == null ? null : d.format(obj.getOnlineValidDeviation()));
                models.add(DayreportMileageCheckModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DayreportMileageCheckModel> models = new ArrayList();
            DecimalFormat d = new DecimalFormat("#######.##%");
            for(Object entry: pr.getData()){
                DayreportMileageCheck obj = (DayreportMileageCheck)entry;
                if (obj.getReportDate() != null){
                    obj.setShowReportDate(new SimpleDateFormat("yyyy-MM-dd").format(obj.getReportDate().getTime()));
                }
                obj.setShowValidGpsDeviation(obj.getValidGpsDeviation() == null ? null : d.format(obj.getValidGpsDeviation()));
                obj.setShowOnlineValidDeviation(obj.getOnlineValidDeviation() == null ? null : d.format(obj.getOnlineValidDeviation()));
                models.add(DayreportMileageCheckModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public DayreportMileageCheckModel dataFindById(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_mileage_check", "vdmc");
        params.put("id",id);
        DayreportMileageCheck entry = unique("dataFindById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DayreportMileageCheckModel.fromEntry(entry);
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if ((params.get("startDate" )== null || params.get("startDate")=="") && (params.get("endDate") == null || params.get("endDate") == "")){
            String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("startDate", now);
            params.put("endDate", now);
        }
        new ExcelExportHandler<DayreportMileageCheck>(this, "pagerModel", params, "veh/res/dayreportMileageCheck/export.xls", "单日里程核算日报") {
            DecimalFormat d = new DecimalFormat("#######.##");
            DecimalFormat d2 = new DecimalFormat("#######.##%");
            @Override
            public Object process(DayreportMileageCheck entity) {
                if (entity.getReportDate() != null){
                    entity.setShowReportDate(new SimpleDateFormat("yyyy-MM-dd").format(entity.getReportDate().getTime()));
                }
                entity.setDayGpsMileageExport(entity.getDayGpsMileage() == null ? null : d.format(entity.getDayGpsMileage()));
                entity.setDayCheckMileageExport(entity.getDayCheckMileage() == null ? null : d.format(entity.getDayCheckMileage()));
                entity.setShowValidGpsDeviation(entity.getValidGpsDeviation() == null ? null : d2.format(entity.getValidGpsDeviation()));
                entity.setShowOnlineValidDeviation(entity.getOnlineValidDeviation() == null ? null : d2.format(entity.getOnlineValidDeviation()));
                return super.process(entity);
            }
        }.work();
        return;
    }

    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("单日里程核算日报导入模板.xls", "VIN", new String[]{"LSB123214124214"});
    }
}
