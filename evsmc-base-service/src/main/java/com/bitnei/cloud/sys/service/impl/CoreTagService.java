package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.CoreTag;
import com.bitnei.cloud.sys.model.CoreTagModel;
import com.bitnei.cloud.sys.service.ICoreTagService;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreTagService实现<br>
* 描述： CoreTagService实现<br>
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
* <td>2019-03-25 15:57:04</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.CoreTagMapper" )
public class CoreTagService extends BaseService implements ICoreTagService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<CoreTag> entries = findBySqlId("pagerModel", params);
            List<CoreTagModel> models = new ArrayList();
            for(CoreTag entry: entries){
                CoreTag obj = (CoreTag)entry;
                models.add(CoreTagModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<CoreTagModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                CoreTag obj = (CoreTag)entry;
                models.add(CoreTagModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public CoreTagModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");
        params.put("id",id);
        CoreTag entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return CoreTagModel.fromEntry(entry);
    }


    @Override
    public CoreTagModel getByTagId(String tagId){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");
        params.put("tagId",tagId);
        CoreTag entry = unique("findByTagId", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return CoreTagModel.fromEntry(entry);
    }

    @Override
    public CoreTagModel getByTableAndValue(String tableName, String idValue) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");
        params.put("tableName", tableName);
        params.put("idValue", idValue);
        CoreTag entry = unique("findByTableAndValue", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return CoreTagModel.fromEntry(entry);
    }


    @Override
    public void insert(CoreTagModel model) {

        CoreTag obj = new CoreTag();
        BeanUtils.copyProperties(model, obj);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(CoreTagModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");

        CoreTag obj = new CoreTag();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_tag", "ct");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<CoreTag>(this, "pagerModel", params, "sys/res/coreTag/export.xls", "系统核心标签表") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "CORETAG"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<CoreTagModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreTagModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreTagModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "CORETAG"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<CoreTagModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreTagModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreTagModel model) {
                update(model);
            }
        }.work();

    }



}
