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
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.dao.TermModelUnitMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.SimManagement;
import com.bitnei.cloud.sys.domain.TermModelUnit;
import com.bitnei.cloud.sys.model.SimManagementModel;
import com.bitnei.cloud.sys.service.ISimManagementService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
* 功能： SimManagementService实现<br>
* 描述： SimManagementService实现<br>
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
* <td>2018-11-05 10:01:32</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.SimManagementMapper" )
public class SimManagementService extends BaseService implements ISimManagementService {


    @Resource
    private TermModelUnitMapper termModelUnitMapper;
    @Resource
    private DictMapper dictMapper;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<SimManagement> entries = findBySqlId("pagerModel", params);
            List<SimManagementModel> models = new ArrayList<>();
            for(SimManagement entry: entries){
                models.add(SimManagementModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<SimManagementModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                SimManagement obj = (SimManagement)entry;
                models.add(SimManagementModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }




    @Override
    public SimManagementModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");
        params.put("id",id);
        SimManagement entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("SIM卡不存在");
        }
        return SimManagementModel.fromEntry(entry);
    }


    @Override
    public SimManagementModel findByIccId(String iccid){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");
        params.put("iccid",iccid);
        SimManagement entry = unique("findByIccId", params);
        if (entry == null){
            throw new BusinessException("SIM卡不存在");
        }
        return SimManagementModel.fromEntry(entry);
    }


    @Override
    public void insert(SimManagementModel model) {
        if(StringUtils.isEmpty(model.getMsisd())) {
            model.setMsisd(null);
        }
        SimManagement obj = new SimManagement();
        BeanUtils.copyProperties(model, obj);
        obj.setIsActive(Constant.TrueAndFalse.FALSE);
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
    public void update(SimManagementModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");
        if(StringUtils.isEmpty(model.getMsisd())) {
            model.setMsisd(null);
        }
        SimManagement obj = new SimManagement();
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
     * 卡号注册
     * @param ids 多个用，分隔
     */
    @Override
    public void register(String ids) {
        String[] arr = ids.split(",");
        Map<String,Object> params = Maps.newHashMap();
        for (String id:arr){
            params.put("id",id);
            params.put("updateTime",DateUtil.getNow());
            params.put("updateBy",ServletUtil.getCurrentUser());
            super.update("register",params);
        }
    }

    /**
    * 删除多个
    * @param ids id集合
    * @return 影响行数
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            SimManagementModel model = this.get(id);
            if(model == null) {
                throw new BusinessException("SIM卡记录不存在");
            }
            TermModelUnit term = termModelUnitMapper.findByIccid(model.getIccid());
            if(term != null) {
               throw new BusinessException("当前选项中包含已关联终端的SIM卡，请解除关联后再行删除。");
            }
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

        new ExcelExportHandler<SimManagement>(this, "pagerModel", params, "sys/res/simManagement/export.xls", "SIM卡管理") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "SIMMANAGEMENT"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<SimManagementModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model SimManagementModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(SimManagementModel model) {
                List<String> errors = Lists.newArrayList();
                // 运营商
                if(StringUtils.isNotBlank(model.getGlobalSimTypeDisplay())) {
                    Map<String, Object> typeParams = ImmutableMap.of("type", "CARRIER_TYPE", "name", model.getGlobalSimTypeDisplay());
                    Dict dict = dictMapper.getByTypeAndName(typeParams);
                    if(dict != null) {
                        model.setGlobalSimType(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("运营商填写错误");
                    }
                }
                // ICCID
                Map<String,Object> params = DataAccessKit.getAuthMap("sys_sim_management", "sim");
                if(StringUtils.isNotBlank(model.getIccid())) {
                    params.put("iccid",model.getIccid());
                    SimManagement entry = unique("findByIccId", params);
                    if(entry != null) {
                        errors.add("ICCID已存在");
                    }
                }
                // MSISD
                if(StringUtils.isNotEmpty(model.getMsisd())) {
                    params.put("msisd",model.getMsisd());
                    SimManagement sim = unique("findByMsisd", params);
                    if(sim != null) {
                        errors.add("MSISD已存在");
                    }
                }

                return errors;
            }

            /**
             *  保存实体
             *
             * @param model SimManagementModel
             */
            @Override
            public void saveObject(SimManagementModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "SIMMANAGEMENT"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<SimManagementModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model SimManagementModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(SimManagementModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model SimManagementModel
             */
            @Override
            public void saveObject(SimManagementModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("SIM卡导入模板.xls" , SimManagementModel.class);
    }

    @Override
    public void importFind() {
        EasyExcel.renderImportSearchDemoFile("SIM卡管理导入查询模板.xls", "iccid", new String[]{"BITNEIYPX00000000012"});
    }


}
