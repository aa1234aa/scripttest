package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.VehNotice;
import com.bitnei.cloud.sys.model.VehBrandModel;
import com.bitnei.cloud.sys.model.VehNoticeModel;
import com.bitnei.cloud.sys.model.VehSeriesModel;
import com.bitnei.cloud.sys.model.VehTypeModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.service.IVehNoticeService;
import com.bitnei.cloud.sys.service.IVehSeriesService;
import com.bitnei.cloud.sys.service.IVehTypeService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNoticeService实现<br>
* 描述： VehNoticeService实现<br>
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
* <td>2018-11-12 14:50:08</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehNoticeMapper" )
public class VehNoticeService extends BaseService implements IVehNoticeService {

    @Resource
    private IVehSeriesService vehSeriesService;
    @Resource
    private IVehTypeService vehTypeService;
    @Resource
    private IVehBrandService vehBrandService;
    @Resource
    private DictMapper dictMapper;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_notice", "vn");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<VehNotice> entries = findBySqlId("pagerModel", params);
            List<VehNoticeModel> models = new ArrayList<>();
            for(VehNotice entry: entries){
                models.add(VehNoticeModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehNoticeModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                VehNotice obj = (VehNotice)entry;
                models.add(VehNoticeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<VehNotice> findList(Map<String, Object> map) {
        return findBySqlId("pagerModel", map);
    }


    @Override
    public VehNoticeModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_notice", "vn");
        params.put("id",id);
        VehNotice entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehNoticeModel.fromEntry(entry);
    }


    @Override
    public VehNoticeModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_notice", "vn");
        params.put("name",name);
        VehNotice entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("车辆公告型号不存在");
        }
        return VehNoticeModel.fromEntry(entry);
    }


    @Override
    public void insert(VehNoticeModel model) {

        VehNotice obj = new VehNotice();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehNoticeModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_notice", "vn");
        VehSeriesModel seriesModel = vehSeriesService.get(model.getSeriesId());
        if(!seriesModel.getBrandId().equals(model.getBrandId())) {
            throw new BusinessException("品牌与车型系列无关联关系");
        }
        VehNotice obj = new VehNotice();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids id集合
    * @return 影响行数
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_notice", "vn");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehNotice>(this, "pagerModel", params, "sys/res/vehNotice/export.xls", "车辆公告") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHNOTICE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<VehNoticeModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehNoticeModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(VehNoticeModel model) {
                List<String> errors = Lists.newArrayList();
                // 车辆种类
                if(StringUtils.isNotBlank(model.getVehTypeName())) {
                    try {
                        VehTypeModel vehType = vehTypeService.getByName(model.getVehTypeName());
                        model.setVehTypeId(vehType.getId());
                    } catch (BusinessException e) {
                        errors.add("车辆种类不存在");
                    }
                }

                // 品牌
                if(StringUtils.isNotBlank(model.getBrandName())) {
                    try {
                        VehBrandModel brand = vehBrandService.getByName(model.getBrandName());
                        model.setBrandId(brand.getId());
                    } catch (BusinessException e) {
                        errors.add("品牌不存在");
                    }
                }
                // 车型系列
                if(StringUtils.isNotBlank(model.getSeriesName())) {
                    try {
                        VehSeriesModel series = vehSeriesService.getByName(model.getSeriesName());
                        model.setSeriesId(series.getId());
                        if(StringUtils.isNotBlank(model.getBrandId()) && !StringUtils.equals(series.getBrandId(), model.getBrandId())) {
                            errors.add("品牌没有该车型系列");
                        }
                    } catch (BusinessException e) {
                        errors.add("车型系列不存在");
                    }
                }

                // 免征
                if(StringUtils.isNotBlank(model.getIsExemptDisplay())) {
                    Map<String, Object> params = ImmutableMap.of("type", "BOOL_TYPE", "name", model.getIsExemptDisplay());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setIsExempt(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("免征填写错误");
                    }
                } else {
                    model.setIsExempt(Constant.TrueAndFalse.TRUE);
                }
                // 燃油
                if(StringUtils.isNotBlank(model.getIsFuelDisplay())) {
                    Map<String, Object> params = ImmutableMap.of("type", "BOOL_TYPE", "name", model.getIsFuelDisplay());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setIsFuel(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("燃油填写错误");
                    }
                } else {
                    model.setIsFuel(Constant.TrueAndFalse.TRUE);
                }
                // 环保
                if(StringUtils.isNotBlank(model.getIsProtectionDisplay())) {
                    Map<String, Object> params = ImmutableMap.of("type", "BOOL_TYPE", "name", model.getIsProtectionDisplay());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setIsProtection(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("环保填写错误");
                    }
                } else {
                    model.setIsProtection(Constant.TrueAndFalse.TRUE);
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model VehNoticeModel
             */
            @Override
            public void saveObject(VehNoticeModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "VEHNOTICE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<VehNoticeModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehNoticeModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(VehNoticeModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model VehNoticeModel
             */
            @Override
            public void saveObject(VehNoticeModel model) {
                update(model);
            }
        }.work();

    }


    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆公告导入模板.xls" , VehNoticeModel.class);
    }



}
