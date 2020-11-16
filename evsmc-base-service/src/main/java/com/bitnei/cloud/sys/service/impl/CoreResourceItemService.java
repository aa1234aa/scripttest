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
import com.bitnei.cloud.sys.dao.CoreResourceItemMapper;
import com.bitnei.cloud.sys.domain.CoreResourceItem;
import com.bitnei.cloud.sys.domain.HashValue;
import com.bitnei.cloud.sys.model.CoreResourceItemModel;
import com.bitnei.cloud.sys.model.CoreSimpleResourceItemModel;
import com.bitnei.cloud.sys.service.ICoreResourceItemService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreResourceItemService实现<br>
* 描述： CoreResourceItemService实现<br>
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
* <td>2018-11-05 09:34:18</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.CoreResourceItemMapper" )
public class CoreResourceItemService extends BaseService implements ICoreResourceItemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private CoreResourceItemMapper resourceItemMapper;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_resource_item", "cri");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<CoreResourceItem> entries = findBySqlId("pagerModel", params);
            List<CoreResourceItemModel> models = new ArrayList();
            for(CoreResourceItem entry: entries){
                CoreResourceItem obj = (CoreResourceItem)entry;
                models.add(CoreResourceItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<CoreResourceItemModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                CoreResourceItem obj = (CoreResourceItem)entry;
                models.add(CoreResourceItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public CoreResourceItemModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_resource_item", "cri");
        params.put("id",id);
        CoreResourceItem entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return CoreResourceItemModel.fromEntry(entry);
    }




    @Override
    public void insert(CoreResourceItemModel model) {

        CoreResourceItem obj = new CoreResourceItem();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        String path = "";
        if (StringUtils.isEmpty(model.getParentId())){
            path = "/"+id + "/";
        }
        else {
            //获取父亲的path
            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("id", model.getParentId());
            CoreResourceItem parentItem =  resourceItemMapper.findById(objectMap);
            path = parentItem.getPath() +id + "/";

        }
        obj.setPath(path);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CoreResourceItemModel model) {

        //获取更新前的实体
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("id", model.getId());
        CoreResourceItem oldItem =  resourceItemMapper.findById(objectMap);

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_resource_item", "cri");

        CoreResourceItem obj = new CoreResourceItem();
        BeanUtils.copyProperties(model, obj);

        String newPath = "";
        {
            if (StringUtils.isEmpty(model.getParentId())){
                newPath = "/"+model.getId() + "/";
            }
            else {
                //获取新父亲的path
                Map<String,Object> parentMap = new HashMap<>();
                parentMap.put("id", model.getParentId());
                CoreResourceItem parentItem =  resourceItemMapper.findById(parentMap);
                newPath = parentItem.getPath() +model.getId() + "/";
            }

        }
        obj.setPath(newPath);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }

        //更新子节点的path
        String oldPath = "";
        {
            if (StringUtils.isEmpty(oldItem.getParentId())){
                oldPath = "/" +model.getId() + "/";
            }
            else {
                //获取新父亲的path
                Map<String,Object> parentMap = new HashMap<>();
                parentMap.put("id", oldItem.getParentId());
                CoreResourceItem parentItem =  resourceItemMapper.findById(parentMap);
                oldPath = parentItem.getPath() +model.getId() + "/";
            }

        }
        resourceItemMapper.updatePath(newPath, oldPath);

    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_core_resource_item", "cri");

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

        new ExcelExportHandler<CoreResourceItem>(this, "pagerModel", params, "sys/res/coreResourceItem/export.xls", "核心资源属性") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "CORERESOURCEITEM"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<CoreResourceItemModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreResourceItemModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreResourceItemModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "CORERESOURCEITEM"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<CoreResourceItemModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(CoreResourceItemModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(CoreResourceItemModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public List<CoreSimpleResourceItemModel> findVehicleResourceItems() {
        //查找车辆相关的资源项
        List<CoreResourceItem> items = resourceItemMapper.findVehicleItems();
        List<CoreSimpleResourceItemModel> simpleResourceItems = new ArrayList<>();
        for (CoreResourceItem item: items) {
            CoreSimpleResourceItemModel simpleResourceItem = new CoreSimpleResourceItemModel();
            simpleResourceItem.setId(item.getId());
            simpleResourceItem.setName(item.getName());
            simpleResourceItem.setResouceId(item.getResouceId());
            simpleResourceItems.add(simpleResourceItem);
        }
        return simpleResourceItems;
    }

    @Override
    public List<HashValue> findHashValuesByResItemIdAndIds(String resourceItemId, String val) {

        //查询属性项
        String[] ids = val.split(",");
        Map<String, Object> params = new HashMap<>();
        params.put("id", resourceItemId);
        CoreResourceItem item = resourceItemMapper.findById(params);

        //查找hashValue
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("tableName", item.getObjectTableName());
        valueMap.put("ids", ids);
        valueMap.put("columnName", item.getIdentifyColumnName());

        List<HashValue> hashValues = resourceItemMapper.findHashValuesByResItemIdAndIds(valueMap);
        return hashValues;
    }

    @Override
    public String findNameByResItemIdAndIds(String resourceItemId, String val) {

        List<HashValue> hashValues = findHashValuesByResItemIdAndIds(resourceItemId, val);
        //将list转为hash
        Map<String, String> map =  new HashMap<>();
        for (HashValue hv: hashValues){
            map.put(hv.getKey(), hv.getValue());
        }

        String[] vals = val.trim().split(",");
        List<String> strings = new ArrayList<>();
        for (String v: vals){
            String desc = map.get(v);
            if (StringUtil.isNotEmpty(desc)){
                strings.add(desc);
            }
        }

        return StringUtils.join(strings, ",");

    }


    @Override
    public CoreResourceItem getVehicleSelfItem() {
        return resourceItemMapper.getVehicleSelfItem();
    }


}
