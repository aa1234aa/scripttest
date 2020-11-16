package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.dao.DataItemMapper;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.DataItemGroup;
import com.bitnei.cloud.dc.model.DataItemGroupModel;
import com.bitnei.cloud.dc.service.IDataItemGroupService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemGroupService实现<br>
* 描述： DataItemGroupService实现<br>
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
* <td>2019-01-30 11:25:14</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.DataItemGroupMapper" )
public class DataItemGroupService extends BaseService implements IDataItemGroupService {

    @Resource
    private DataItemMapper dataItemMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if(params.get("searchType") != null && "tree".equals(params.get("searchType").toString())){
            List<DataItemGroup> entries = findBySqlId("treeModel", params);
            List<DataItemGroupModel> models = new ArrayList();
            for(DataItemGroup entry: entries){
                DataItemGroup obj = (DataItemGroup)entry;
                models.add(DataItemGroupModel.fromEntry(obj));
            }

            TreeNode root = new TreeHandler<DataItemGroupModel>(models) {

                @Override
                protected TreeNode beanToTreeNode(DataItemGroupModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    Map<String, Object> attr = new HashMap<>();
                    attr.put("name", bean.getName());
                    attr.put("code", bean.getCode());
                    attr.put("note", bean.getNote());
                    attr.put("ruleTypeDisplay", bean.getRuleTypeDisplay());
                    attr.put("port", bean.getPort());
                    attr.put("isNationalStandard", bean.getIsNationalStandard());
                    tn.setAttributes(attr);
                    return tn;
                }

                @Override
                protected boolean isRoot(TreeNode node) {
                    return "0".equals(node.getId());
                }
            }.toTree();

            return root;
        }else {
            //非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

                List<DataItemGroup> entries = findBySqlId("pagerModel", params);
                List<DataItemGroupModel> models = new ArrayList();
                for (DataItemGroup entry : entries) {
                    DataItemGroup obj = (DataItemGroup) entry;
                    models.add(DataItemGroupModel.fromEntry(obj));
                }
                return models;
            }
            //分页查询
            else {
                if(params.get("excludeDataItem") != null && params.get("excludeDataItem").toString().equals("1")){
                    Map<String, Object> diParams = DataAccessKit.getAuthMap("dc_data_item", "di");
                    diParams.putAll(ServletUtil.pageInfoToMap(pagerInfo));
                    List<String> groupIds = findBySqlId("findItemGroupIds", diParams);
                    String groupIdStr = Joiner.on(",").join(groupIds);
                    params.put("excludeIds", groupIdStr.split(","));
                }
                PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

                List<DataItemGroupModel> models = new ArrayList();
                for (Object entry : pr.getData()) {
                    DataItemGroup obj = (DataItemGroup) entry;
                    models.add(DataItemGroupModel.fromEntry(obj));
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
    }



    @Override
    public DataItemGroupModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        params.put("id",id);
        DataItemGroup entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataItemGroupModel.fromEntry(entry);
    }


    @Override
    public DataItemGroupModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        params.put("name",name);
        DataItemGroup entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataItemGroupModel.fromEntry(entry);
    }


    @Override
    public void insert(DataItemGroupModel model) {

        Map<String, Object> map = new HashMap<>();
        //判断是否有根节点
        map.put("id", "0");
        DataItemGroup root = unique("findById", map);
        if(root == null) {
            DataItemGroup obj = new DataItemGroup();
            obj.setName("全部类型");
            obj.setId("0");
            obj.setPath("/0/");
            int res = super.insert(obj);
            if (res == 0) {
                throw new BusinessException("还未有根节点，增加根节点失败");
            }
        }
        //获取当权限的map
        Map<String,Object> params = new HashMap<>();
        params.put("name", model.getName());
        params.put("ruleTypeId", model.getRuleTypeId());
        DataItemGroup dig = unique("findByName", params);
        if (dig != null){
            throw new BusinessException(String.format("相同协议类型的类型名称 %s 已存在", model.getName()));
        }

        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId("0");
        }
        if(!model.getCode().startsWith("0x")){
            model.setCode("0x" + model.getCode());
        }
        params.clear();
        params.put("code", model.getCode());
        params.put("parentId", model.getParentId());
        params.put("ruleTypeId", model.getRuleTypeId());
        DataItemGroup entry = unique("findByCode", params);
        if (entry != null){
            throw new BusinessException(String.format("同一协议类型同一层级的类型编码 %s 已存在", model.getCode()));
        }
        DataItemGroupModel groupModel = this.get(model.getParentId());

        DataItemGroup obj1 = new DataItemGroup();
        BeanUtils.copyProperties(model, obj1);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj1.setId(id);
        obj1.setPath(groupModel.getPath() + id + "/");
        String pathName = "";
        if(StringUtils.isEmpty(groupModel.getPathName())){
            pathName = obj1.getName();
        }else{
            pathName = groupModel.getPathName() + "-" + obj1.getName();
        }
        obj1.setPathName(pathName);
        obj1.setCreateTime(DateUtil.getNow());
        obj1.setCreateBy(ServletUtil.getCurrentUser());
        int res1 = super.insert(obj1);
        if (res1 == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(DataItemGroupModel model) {
        // root根节点=0
        if(!model.getId().equals("0") && StringUtils.isBlank(model.getParentId())) {
            throw new BusinessException("上一级不能为空");
        }
        if(!model.getId().equals("0") && StringUtils.isBlank(model.getName())) {
            throw new BusinessException("数据项类型名称不能为空");
        }
        //获取当权限的map
        Map<String,Object> validParams = new HashMap<>();
        validParams.put("id", model.getId());
        validParams.put("name", model.getName());
        validParams.put("ruleTypeId", model.getRuleTypeId());

        DataItemGroup dig = unique("findByName", validParams);
        if (dig != null){
            throw new BusinessException(String.format("相同协议类型的类型名称 %s 已存在", model.getName()));
        }

        if(!model.getCode().startsWith("0x")){
            model.setCode("0x" + model.getCode());
        }
        validParams.clear();
        validParams.put("id", model.getId());
        validParams.put("code", model.getCode());
        validParams.put("parentId", model.getParentId());
        validParams.put("ruleTypeId", model.getRuleTypeId());
        DataItemGroup entry = unique("findByCode", validParams);
        if (entry != null){
            throw new BusinessException(String.format("同一协议类型同一层级的类型编码 %s 已存在", model.getCode()));
        }

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        DataItemGroup obj = new DataItemGroup();
        BeanUtils.copyProperties(model, obj);
        //把父类的path+当前的id
        String path = obj.getId() + "/";
        if(!model.getId().equals("0")) {
            DataItemGroupModel groupModel = this.get(model.getParentId());
            path = groupModel.getPath() + path;

            String pathName = "";
            if(StringUtils.isEmpty(groupModel.getPathName())){
                pathName = obj.getName();
            }else{
                pathName = groupModel.getPathName() + "-" + obj.getName();
            }
            obj.setPathName(pathName);
        }
        obj.setPath(path);
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
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            //判断是否国标，是国标的数据项类型不允许删除
            DataItemGroupModel itemGroup = get(id);
            if (itemGroup != null && Constants.YES.equals(itemGroup.getIsNationalStandard())){
                throw new BusinessException("该数据项类型是国标，不允许删除");
            }
            params.put("id",id);
            int r = super.deleteByMap(params);
            //判断当前数据项类型是否有子节点
            Map<String,Object> digParams = new HashMap<>();
            digParams.put("parentId", id);
            List<DataItemGroup> entries = findBySqlId("pagerModel", digParams);
            if(entries != null && entries.size() > 0){
                for(DataItemGroup dig : entries){
                    params.clear();
                    params.put("id",dig.getId());
                    r += super.deleteByMap(params);
                    id += "," + dig.getId();
                }
            }
            if (r > 0) {
                //删除所属数据项
                String[] groupIds = id.split(",");
                List<String> list = new ArrayList<>();
                for (String gid : groupIds){
                    list.add(gid);
                }
                dataItemMapper.deleteByGroupId(list);
            }
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DataItemGroup>(this, "pagerModel", params, "dc/res/dataItemGroup/export.xls", "数据项分组") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DATAITEMGROUP"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DataItemGroupModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemGroupModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataItemGroupModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DATAITEMGROUP"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DataItemGroupModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemGroupModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataItemGroupModel model) {
                update(model);
            }
        }.work();

    }



}
