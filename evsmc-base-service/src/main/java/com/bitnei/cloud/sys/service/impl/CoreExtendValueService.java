package com.bitnei.cloud.sys.service.impl;

import cn.hutool.core.util.ReUtil;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.ExtendTable;
import com.bitnei.cloud.common.bean.*;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.CoreExtendTemplateMapper;
import com.bitnei.cloud.sys.dao.CoreExtendValueMapper;
import com.bitnei.cloud.sys.domain.CoreExtendTemplate;
import com.bitnei.cloud.sys.domain.CoreExtendValue;
import com.bitnei.cloud.sys.model.CoreExtendValueModel;
import com.bitnei.cloud.sys.service.ICoreExtendValueService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.util.CoreExtendUtil;
import com.google.common.collect.Maps;
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

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreExtendValueService实现<br>
* 描述： CoreExtendValueService实现<br>
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
* <td>2019-07-31 15:08:40</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.CoreExtendValueMapper" )
public class CoreExtendValueService extends BaseService implements ICoreExtendValueService {

    @Resource
    private CoreExtendValueMapper coreExtendValueMapper;
    @Resource
    private CoreExtendTemplateMapper coreExtendTemplateMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<CoreExtendValue> entries = findBySqlId("pagerModel", params);
            List<CoreExtendValueModel> models = new ArrayList();
            for(CoreExtendValue entry: entries){
                CoreExtendValue obj = (CoreExtendValue)entry;
                models.add(CoreExtendValueModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<CoreExtendValueModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                CoreExtendValue obj = (CoreExtendValue)entry;
                models.add(CoreExtendValueModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public CoreExtendValueModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");
        params.put("id",id);
        CoreExtendValue entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return CoreExtendValueModel.fromEntry(entry);
    }


    @Override
    public CoreExtendValueModel getByIdVal(String idVal){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");
        params.put("idVal",idVal);
        CoreExtendValue entry = unique("findByIdVal", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return CoreExtendValueModel.fromEntry(entry);
    }


    @Override
    public void insert(CoreExtendValueModel model) {

        CoreExtendValue obj = new CoreExtendValue();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(CoreExtendValueModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");

        CoreExtendValue obj = new CoreExtendValue();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }

    /**
    * 根据idVal删除
    * @param idVal
    * @return
    */
    @Override
    public int deleteByIdVal(String idVal) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");
        params.put("idVal", idVal);
        int count = super.deleteBySqlId("deleteByIdVal", params);
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_extend_value", "cev");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<CoreExtendValue>(this, "pagerModel", params, "sys/res/coreExtendValue/export.xls", "属性扩展表") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "COREEXTENDVALUE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<CoreExtendValueModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreExtendValueModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreExtendValueModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "COREEXTENDVALUE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<CoreExtendValueModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreExtendValueModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreExtendValueModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 更新自定义属性
     *
     * @param model
     * @return
     */
    @Override
    public void updateByModel(BaseExtendModel model, final String idVal) {

        if (model.getClass().isAnnotationPresent(ExtendTable.class)){
            ExtendTable extendTable = model.getClass().getAnnotation(ExtendTable.class);

            //先校验
            List<CoreExtendTemplate> coreExtendTemplates = coreExtendTemplateMapper.findByTable(extendTable.table());
            //获取配置的项
            Map<String,Object> map = Maps.newHashMap();
            for (CoreExtendTemplate template: coreExtendTemplates){
                if (StringUtil.isNotEmpty(template.getValidateRule()) && model.getExtendColumns().containsKey(template.getFieldName())){
                    Object val = model.getExtendColumns().get(template.getFieldName());
                    boolean res = ReUtil.isMatch(template.getValidateRule(), val == null ? "": val.toString());
                    if (!res){
                        throw new BusinessException(template.getValidateMessage());
                    }
                }
                if(model.getExtendColumns().containsKey(template.getFieldName())){
                    map.put(template.getFieldName(),model.getExtendColumns().get(template.getFieldName()));
                }

            }

            //重置一下extendColumns
            model.getExtendColumns().clear();
            model.setExtendColumns(map);

            CoreExtendValue coreExtendValue = coreExtendValueMapper.findByTableAndIdVal(extendTable.table(), idVal);
            if (coreExtendValue == null){
                coreExtendValue = CoreExtendUtil.fromModel(idVal, model);
                coreExtendValueMapper.insert(coreExtendValue);
            }
            else {
                CoreExtendUtil.updateExtendValue(coreExtendValue, model);
                coreExtendValueMapper.update(coreExtendValue);
            }
        }
    }


}
