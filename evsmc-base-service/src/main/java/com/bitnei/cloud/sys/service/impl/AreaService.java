package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.AreaMapper;
import com.bitnei.cloud.sys.domain.Area;
import com.bitnei.cloud.sys.model.AreaModel;
import com.bitnei.cloud.sys.service.IAreaService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AreaService实现<br>
* 描述： AreaService实现<br>
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
* <td>2018-12-27 09:27:18</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.AreaMapper" )
public class AreaService extends BaseService implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;
    /**
     * 多条件查询
     * @param pagerInfo
     * @return
     */
   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<Area> entries = findBySqlId("pagerModel", params);
            List<AreaModel> models = new ArrayList();
            for(Area entry: entries){
                Area obj = (Area)entry;
                models.add(AreaModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AreaModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                Area obj = (Area)entry;
                models.add(AreaModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    /**
     * 查询所有区域
     *
     * @param pagerInfo
     * @return
     */
    @Override
    public TreeNode queryAllArea(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.get("parentId") != null && !String.valueOf(params.get("parentId")).equals("")){
            String path = areaMapper.findPathById(String.valueOf(params.get("parentId")));
            params.put("path",path);
        }
        List<Area> entries = findBySqlId("pagerModel2", params);
        if (null != entries && entries.size() > 0 && null != entries.get(0)) {
            List<AreaModel> models = new ArrayList();
            for (Area entry : entries) {
                Area obj = (Area) entry;
                models.add(AreaModel.fromEntry(obj));
            }
            if (params.get("parentId") == null || String.valueOf(params.get("parentId")).equals("-1")){
                TreeNode tree = listToTree(models);
                return tree;
            }
            else if (params.get("parentId") != null){
                TreeNode tree = listToTree(models, String.valueOf(params.get("parentId")));
                return tree;
            }

        }
        return null;
    }


    /**
     * 行政区域转树形结构
     *
     * @param models
     * @return
     */
    private TreeNode listToTree(List<AreaModel> models){
        TreeNode root = new TreeHandler<AreaModel>(models) {
            @Override
            protected TreeNode beanToTreeNode(AreaModel bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                Map<String, Object> attr = new HashMap<>();
                attr.put("name", bean.getName());
                attr.put("parent_name",bean.getParentName());
                attr.put("code", bean.getCode());
                attr.put("levels", bean.getLevels());
                attr.put("path", bean.getPath());
                attr.put("isRoot", bean.getIsRoot());
                attr.put("orderNum", bean.getOrderNum());
                tn.setAttributes(attr);
                return tn;
            }
            @Override
            protected boolean isRoot(TreeNode node) {
                if(StringUtil.isEmpty(node.getParentId()) || node.getParentId().equalsIgnoreCase("-1")){
                    return true;
                }
                else{
                    return false;
                }
            }
        }.toTree();
        return root;
    }

    /**
     * 行政区域转树形结构2
     *
     * @param models
     * @return
     */
    private TreeNode listToTree(List<AreaModel> models,String parentId){
        TreeNode root = new TreeHandler<AreaModel>(models) {
            @Override
            protected TreeNode beanToTreeNode(AreaModel bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                Map<String, Object> attr = new HashMap<>();
                attr.put("name", bean.getName());
                attr.put("parent_name",bean.getParentName());
                attr.put("code", bean.getCode());
                attr.put("levels", bean.getLevels());
                attr.put("path", bean.getPath());
                if (bean.getId().equals(parentId)){
                    attr.put("isRoot", 1);
                }
                else{
                    attr.put("isRoot", bean.getIsRoot());
                }
                attr.put("orderNum", bean.getOrderNum());
                tn.setAttributes(attr);
                return tn;
            }
            @Override
            protected boolean isRoot(TreeNode node) {
                if(StringUtil.isEmpty(node.getParentId()) || node.getId().equals(parentId)){
                    return true;
                }
                else{
                    return false;
                }
            }
        }.toTree();
        return root;
    }

    /**
     * 树结构
     * @return
     */
    @Override
    public Object tree(String parentId) {

        if (StringUtil.isEmpty(parentId)||parentId.equals("0")){
            parentId = "0";
            Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
            params.put("parentId", parentId);
            List<Area> entries = findBySqlId("pagerModel", params);
            List<AreaModel> models = new ArrayList();
            for(Area entry: entries){
                Area obj = (Area)entry;
                models.add(AreaModel.fromEntry(obj));
            }
            final String finalParentId = parentId;
            TreeNode root = new TreeHandler<AreaModel>(models) {
                @Override
                protected TreeNode beanToTreeNode(AreaModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    Map<String, Object> attr = new HashMap<>();
                    attr.put("name", bean.getName());
                    attr.put("code", bean.getCode());
                    attr.put("levels", bean.getLevels());
                    attr.put("path", bean.getPath());
                    attr.put("isRoot", bean.getIsRoot());
                    attr.put("orderNum", bean.getOrderNum());
                    tn.setAttributes(attr);
                    return tn;
                }
                @Override
                protected boolean isRoot(TreeNode node) {
                    return node.getId().equals(finalParentId);
                }
            }.toTree();


            return root;
        }
        else{
            Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
            params.put("parentId", parentId);
            List<Area> entries = findBySqlId("findByParentId", params);
            List<AreaModel> models = new ArrayList();
            for(Area entry: entries){
                Area obj = (Area)entry;
                models.add(AreaModel.fromEntry(obj));
            }
            return models;
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public AreaModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.put("id",id);
        Area entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return AreaModel.fromEntry(entry);
    }

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @Override
    public AreaModel getByName(String name) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.put("name", name);
        Area entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AreaModel.fromEntry(entry);
    }

    /**
     * 新增
     * @param model  新增model
     */
    @Override
    public void insert(AreaModel model) {

        // 校验行政区域名称,行政区域编码
        String message = checkAreaParams(model.getName(), model.getCode(), "");
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }

        Area obj = new Area();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        //序号默认为0
        if (obj.getOrderNum()==null){
            obj.setOrderNum(0);
        }
        //处理路径和层级
        if (obj.getParentId()!=null && !obj.getParentId().equals("") && !obj.getParentId().equals("-1")){
            AreaModel parentArea = this.get(obj.getParentId());
            obj.setIsRoot("0");
            obj.setPath(parentArea.getPath()+id+"/");
            obj.setLevels(parentArea.getLevels()+1);
        }
        else {
            obj.setParentId("-1");
            obj.setIsRoot("1");
            obj.setPath("/"+id+"/");
            obj.setLevels(0);
        }
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    /**
     * 编辑更新
     * @param model  编辑model
     */
    @Override
    public void update(AreaModel model) {

        AreaModel oldArea = this.get(model.getId());
        String oldPath=oldArea.getPath();
        int oldLevels=oldArea.getLevels();
        // 校验行政区域名称,行政区域编码
        String message = checkAreaParams(model.getName(), model.getCode(), model.getId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.put("oldPath",oldPath);
        Area obj = new Area();
        BeanUtils.copyProperties(model, obj);
        //序号默认为0
        if (obj.getOrderNum()==null){
            obj.setOrderNum(0);
        }
        if (obj.getParentId()!=null && !obj.getParentId().equals("")&& !obj.getParentId().equals("-1")){
            AreaModel parentArea = this.get(obj.getParentId());
            String p = parentArea.getPath()+obj.getId()+"/";
            int l = parentArea.getLevels()+1;
            obj.setIsRoot("0");
            obj.setPath(p);
            obj.setLevels(l);
            //修改父节点后的层级差
            int levels=l-oldLevels;
            //处理父节点更改后其子节点的path
            params.put("newPath",p);
            //处理父节点更改后其子节点的levels
            params.put("level",levels);
        } else {
            obj.setParentId("-1");
            obj.setIsRoot("1");
            obj.setPath("/"+obj.getId()+"/");
            obj.setLevels(0);
            //修改父节点后的层级差
            int levels=0-oldLevels;
            //处理父节点更改后其子节点的path
            params.put("newPath","/"+obj.getId()+"/");
            //处理父节点更改后其子节点的levels
            params.put("level",levels);
        }
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
        super.update("updateByPath",params);
        super.update("updateByLevels",params);
    }

    //校验唯一性
    private String checkAreaParams(String areaName, String areaCode ,String id) {

        String result="";
        Map<String, Object> params = new HashMap<String, Object>();
        //校验行政区域名称唯一性
        if (!areaName.isEmpty()){
            params.put("nameEq",areaName);
            Area area = queryAreaByParam(params);
            if (null != area && (StringUtils.isEmpty(id) || !id.equals(area.getId()))) {
                // 如果id为空则表示为添加校验，如果id不为空则表示为更新校验，如何查询结果id不同则表示数据库已存在此行政区域，返回错误
                result = "行政区域名称已存在";
            }
        }
        //校验行政区域编码唯一性
        if (!areaCode.isEmpty()){
            params.clear();
            params.put("codeEq",areaCode);
            Area area = queryAreaByParam(params);
            if (null != area && (StringUtils.isEmpty(id) || !id.equals(area.getId()))) {
                // 如果id为空则表示为添加校验，如果id不为空则表示为更新校验，如何查询结果id不同则表示数据库已存在此行政区域，返回错误
                if (!StringUtils.isEmpty(result)) {
                    result += "，";
                }
                result += "行政区域编码已存在";
            }
        }
        return result;
    }

    //检验参数查询
    public Area queryAreaByParam(Map<String, Object> params){
        List<Area> list = findBySqlId("pagerModel", params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_area", "a");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Area>(this, "pagerModel", params, "sys/res/area/export.xls", "行政区域") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "AREA"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AreaModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AreaModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AreaModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "AREA"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AreaModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AreaModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AreaModel model) {
                update(model);
            }
        }.work();

    }



}
