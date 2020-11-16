package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.DictCategory;
import com.bitnei.cloud.sys.model.DictCategoryModel;
import com.bitnei.cloud.sys.service.IDictCategoryService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.service.IDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DictCategoryService实现<br>
* 描述： DictCategoryService实现<br>
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
* <td>2018-12-22 10:25:37</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.DictCategoryMapper" )
public class DictCategoryService extends BaseService implements IDictCategoryService {

    @Autowired
    private IDictService dictService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DictCategory> entries = findBySqlId("pagerModel", params);
            List<DictCategoryModel> models = new ArrayList();
            for(DictCategory entry: entries){
                DictCategory obj = (DictCategory)entry;
                models.add(DictCategoryModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DictCategoryModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DictCategory obj = (DictCategory)entry;
                models.add(DictCategoryModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public DictCategoryModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");
        params.put("id",id);
        DictCategory entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DictCategoryModel.fromEntry(entry);
    }


    @Override
    public DictCategoryModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");
        params.put("name",name);
        DictCategory entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DictCategoryModel.fromEntry(entry);
    }
    @Override
    public DictCategoryModel getByCode(String code){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");
        params.put("code",code);
        DictCategory entry = unique("findByCode", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DictCategoryModel.fromEntry(entry);
    }


    @Override
    public void insert(DictCategoryModel model) {

        DictCategory obj = new DictCategory();
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
    public void update(DictCategoryModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");

        DictCategory obj = new DictCategory();
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
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");

        String[] arr = ids.split(",");

        int count = 0;
        if(arr != null && arr.length > 0){
            HashMap<String, Object> map = new HashMap<>();
            map.put("ids", arr);
            List<DictCategory> entries = findBySqlId("findByIds", map);
            for(DictCategory entry: entries){
                DictCategory obj = (DictCategory)entry;

                //根据type查询Dict
                map.clear();
                map.put("dictType",obj.getCode());
                List<Dict> dicts = dictService.findBySqlId("findByType", map);
                String dictIds = "";
                for(Dict dict: dicts){
                    Dict d = (Dict)dict;
                    dictIds += d.getId() + ",";
                }
                if(dictIds.length() > 0){
                    dictIds = dictIds.substring(0, dictIds.length()-1);
                }
                //删除字典项表数据
                dictService.deleteMulti(dictIds);

                //删除类别表数据
                params.put("id",obj.getId());
                int r = super.deleteByMap(params);
                count+=r;
            }
        }
        /*int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }*/
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_dict_category", "dc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DictCategory>(this, "pagerModel", params, "sys/res/dictCategory/export.xls", "字典类别") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DICTCATEGORY"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DictCategoryModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DictCategoryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DictCategoryModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DICTCATEGORY"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DictCategoryModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DictCategoryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DictCategoryModel model) {
                update(model);
            }
        }.work();

    }



}
