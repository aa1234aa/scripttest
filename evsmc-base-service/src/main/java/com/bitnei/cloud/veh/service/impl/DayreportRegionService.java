package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.veh.domain.DayreportRegion;
import com.bitnei.cloud.veh.model.DayreportRegionModel;
import com.bitnei.cloud.veh.service.IDayreportRegionService;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportRegionService实现<br>
* 描述： DayreportRegionService实现<br>
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
* <td>2019-03-11 15:18:50</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.mapper.DayreportRegionMapper" )
public class DayreportRegionService extends BaseService implements IDayreportRegionService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("endTime") == null || params.get("endTime")== ""){
            String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("endTime", now);
        }
        if (null != params.get("city")){
            params.replace("city", params.get("city").toString().split(","));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            if (pagerInfo.getConditions().size() == 1 && params.get("operUnitId") != null && !"".equals(String.valueOf(params.get("operUnitId")))){
                List<DayreportRegion> entries = findBySqlId("pagerModel2", params);
                List<DayreportRegionModel> models = new ArrayList();
                for(DayreportRegion entry: entries){
                    DayreportRegion obj = (DayreportRegion)entry;
                    this.getData(obj);
                    models.add(DayreportRegionModel.fromEntry(obj));
                }
                return models;
            }
            else{
                List<DayreportRegion> entries = findBySqlId("pagerModel", params);
                List<DayreportRegionModel> models = new ArrayList();
                for(DayreportRegion entry: entries){
                    DayreportRegion obj = (DayreportRegion)entry;
                    this.getData(obj);
                    models.add(DayreportRegionModel.fromEntry(obj));
                }
                return models;
            }
        }
        //分页查询
        else {
            if (pagerInfo.getConditions().size() == 1 && params.get("operUnitId") != null && !"".equals(String.valueOf(params.get("operUnitId")))){
                PagerResult pr = findPagerModel("pagerModel2", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<DayreportRegionModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    DayreportRegion obj = (DayreportRegion)entry;
                    this.getData(obj);
                    models.add(DayreportRegionModel.fromEntry(obj));
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
            else{
                PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<DayreportRegionModel> models = new ArrayList();
                for(Object entry: pr.getData()){
                    DayreportRegion obj = (DayreportRegion)entry;
                    this.getData(obj);
                    models.add(DayreportRegionModel.fromEntry(obj));
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }

        }
    }

    public void getData(DayreportRegion data){

        DecimalFormat d =new DecimalFormat("#######.##");
        data.setAllAreaMileage(data.getAllAreaMileage() == null ? null : Double.valueOf(d.format(data.getAllAreaMileage())));
        data.setAllGpsMileage(data.getAllGpsMileage() == null ? null : Double.valueOf(d.format(data.getAllGpsMileage())));
        data.setRatio(data.getRatio() == null ? null : Double.valueOf(d.format(data.getRatio())));
        data.setAllMileage(data.getAllMileage() == null ? null : Double.valueOf(d.format(data.getAllMileage())));
    }

    @Override
    public DayreportRegionModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_region", "dreg");
        params.put("id",id);
        DayreportRegion entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DayreportRegionModel.fromEntry(entry);
    }




    @Override
    public void insert(DayreportRegionModel model) {

//        DayreportRegion obj = new DayreportRegion();
//        BeanUtils.copyProperties(model, obj);
//        //单元测试指定id，如果是单元测试，那么就不使用uuid
//        String id = UtilHelper.getUUID();
//        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
//            id = model.getId();
//        }
//        obj.setId(id);
//        int res = super.insert(obj);
//        if (res == 0 ){
//            throw new BusinessException("新增失败");
//        }
    }

    @Override
    public void update(DayreportRegionModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_region", "dreg");

        DayreportRegion obj = new DayreportRegion();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_dayreport_region", "dreg");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("endTime") == null || params.get("endTime")== ""){
            String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 86400000));
            params.put("endTime", now);
        }
        if (pagerInfo.getConditions().size() == 1 && params.get("operUnitId") != null && !"".equals(String.valueOf(params.get("operUnitId")))){
            new ExcelExportHandler<DayreportRegion>(this, "pagerModel2", params, "veh/res/dayreportRegion/export.xls", "区域里程统计") {
                @Override
                public Object process(DayreportRegion entity) {
                    getData(entity);
                    return super.process(entity);
                }

            }.work();
        }
        else{
            new ExcelExportHandler<DayreportRegion>(this, "pagerModel", params, "veh/res/dayreportRegion/export.xls", "区域里程统计") {
                @Override
                public Object process(DayreportRegion entity) {
                    getData(entity);
                    return super.process(entity);
                }
            }.work();
        }
        return;
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DAYREPORTREGION"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DayreportRegionModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DayreportRegionModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DayreportRegionModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DAYREPORTREGION"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DayreportRegionModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DayreportRegionModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DayreportRegionModel model) {
                update(model);
            }
        }.work();

    }



}
