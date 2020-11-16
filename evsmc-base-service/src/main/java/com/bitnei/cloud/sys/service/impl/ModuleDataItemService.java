package com.bitnei.cloud.sys.service.impl;

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
import com.bitnei.cloud.sys.domain.ModuleDataItem;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.ModuleDataItemModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IModuleDataItemService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ModuleDataItemService实现<br>
* 描述： ModuleDataItemService实现<br>
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
* <td>2018-11-22 11:43:02</td>
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
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.ModuleDataItemMapper" )
public class ModuleDataItemService extends BaseService implements IModuleDataItemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());




   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ModuleDataItem> entries = findBySqlId("pagerModel", params);
            List<ModuleDataItemModel> models = new ArrayList();
            for(ModuleDataItem entry: entries){
                ModuleDataItem obj = (ModuleDataItem)entry;
                models.add(ModuleDataItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ModuleDataItemModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ModuleDataItem obj = (ModuleDataItem)entry;
                models.add(ModuleDataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public ModuleDataItemModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");
        params.put("id",id);
        ModuleDataItem entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return ModuleDataItemModel.fromEntry(entry);
    }




    @Override
    public void insert(String moduleId,ModuleDataItemModel model) {

        ModuleDataItem obj = new ModuleDataItem();
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

        //插入关系LK表数据
        insertModuleDataItemLk(moduleId,id);
    }

    @Override
    public void update(ModuleDataItemModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");

        ModuleDataItem obj = new ModuleDataItem();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            //删除关联表数据
            deleteModuleDataItemLk(id);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ModuleDataItem>(this, "pagerModel", params, "sys/res/moduleDataItem/export.xls", "模块数据项列表") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "MODULEDATAITEM"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<ModuleDataItemModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ModuleDataItemModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ModuleDataItemModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "MODULEDATAITEM"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<ModuleDataItemModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ModuleDataItemModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ModuleDataItemModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 查询列数据项
     *
     * @param
     */
    @Override
    public List<ModuleDataItemModel> queryModuleDataItem(String moduleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "role");
        params.put("moduleId",moduleId);
        List<ModuleDataItem> entries = findBySqlId("queryModuleDataItem", params);
        List<ModuleDataItemModel> models = new ArrayList();
        for(ModuleDataItem entry: entries){
            ModuleDataItem obj = (ModuleDataItem)entry;
            models.add(ModuleDataItemModel.fromEntry(obj));
        }
        return models;
    }

    private void insertModuleDataItemLk(String moduleId,String moduleDataId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "role");

        String id = UtilHelper.getUUID();
        params.put("id",id);
        params.put("moduleId",moduleId);
        params.put("moduleDataId",moduleDataId);
        super.insert("insertModuleDataLk", params);
    }

    private void deleteModuleDataItemLk(String moduleDataItemId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "role");

        params.put("moduleDataItemId",moduleDataItemId);
        super.delete("deleteModuleDataLk", params);
    }

    /**
     * 更新是否启用
     *
     * @param
     */
    @Override
    public void updateEnable(String id,Integer isEnable) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_module_data_item", "mdc");
        params.put("id",id);
        params.put("isEnable",isEnable);
        int res = super.update("updateEnable",params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }
}
