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
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.RoleDataItemLkMapper;
import com.bitnei.cloud.sys.domain.RoleDataItemDetail;
import com.bitnei.cloud.sys.domain.RoleDataItemLk;
import com.bitnei.cloud.sys.model.RoleDataItemLkModel;
import com.bitnei.cloud.sys.service.IRoleDataItemLkService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RoleDataItemLkService实现<br>
* 描述： RoleDataItemLkService实现<br>
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
* <td>2018-11-22 10:28:44</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.RoleDataItemLkMapper" )
public class RoleDataItemLkService extends BaseService implements IRoleDataItemLkService {


    @Resource
    private RoleDataItemLkMapper roleDataItemLkMapper;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<RoleDataItemLk> entries = findBySqlId("pagerModel", params);
            List<RoleDataItemLkModel> models = new ArrayList<>();
            for(RoleDataItemLk entry: entries){
                models.add(RoleDataItemLkModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RoleDataItemLkModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                RoleDataItemLk obj = (RoleDataItemLk)entry;
                models.add(RoleDataItemLkModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public RoleDataItemLkModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("id",id);
        RoleDataItemLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RoleDataItemLkModel.fromEntry(entry);
    }




    @Override
    public void insert(RoleDataItemLkModel model) {

        RoleDataItemLk obj = new RoleDataItemLk();
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
    public void update(RoleDataItemLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");

        RoleDataItemLk obj = new RoleDataItemLk();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<RoleDataItemLk>(this, "pagerModel", params, "sys/res/roleDataItemLk/export.xls", "数据项角色数据项中间表") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ROLEDATAITEMLK"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<RoleDataItemLkModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model RoleDataItemLkModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(RoleDataItemLkModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model RoleDataItemLkModel
             */
            @Override
            public void saveObject(RoleDataItemLkModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ROLEDATAITEMLK"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<RoleDataItemLkModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model RoleDataItemLkModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(RoleDataItemLkModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model RoleDataItemLkModel
             */
            @Override
            public void saveObject(RoleDataItemLkModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 更新是否隐藏字段
     *
     */
    private boolean updateForHidden(String roleModuleId,String moduleDataId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        RoleDataItemLk obj = new RoleDataItemLk();
        obj.setRoleModuleId(roleModuleId);
        obj.setModuleDataId(moduleDataId);
        obj.setIsHidden(Constant.TrueAndFalse.TRUE);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.update("updateForHidden",params);
        return res != 0;
    }

    private boolean insertForHidden(String roleModuleId,String moduleDataId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        RoleDataItemLk obj = new RoleDataItemLk();
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setRoleModuleId(roleModuleId);
        obj.setModuleDataId(moduleDataId);
        obj.setIsHidden(1);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.insert(params);
        return res != 0;
    }

    /**
     * 更新是否脱敏字段
     *
     */
    private boolean updateForSensitive(String roleModuleId,String moduleDataId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        RoleDataItemLk obj = new RoleDataItemLk();
        obj.setRoleModuleId(roleModuleId);
        obj.setModuleDataId(moduleDataId);
        obj.setIsSensitive(1);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.update("updateForSensitive",params);
        return res != 0;
    }

    private boolean insertForSensitive(String roleModuleId,String moduleDataId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        RoleDataItemLk obj = new RoleDataItemLk();
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setRoleModuleId(roleModuleId);
        obj.setModuleDataId(moduleDataId);
        obj.setIsSensitive(1);
        obj.setCreateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.insert(params);
        return res != 0;
    }

    @Override
    public void deleteRoleModuleLk(String roleId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        super.deleteBySqlId("deleteRoleModuleLk",params);
    }

    @Override
    public void deleteRoleDataItem(String roleId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        super.deleteBySqlId("deleteRoleDataItem",params);
    }

    /**
     * 分配角色数据项权限，是否脱敏，是否隐藏
     * @param roleId 角色id
     * @param moduleId 模块id
     * @param hiddenIds 是否隐藏
     * @param sensitiveIds 是否脱敏
     */
    @Override
    public void updateRoleDateItemPermission(String roleId,String moduleId,List<String> hiddenIds,List<String> sensitiveIds){

        String roleModuleId = addRoleModuleLk(roleId,moduleId);
        //更新所有隐藏字段状态
        for (String hiddenId : hiddenIds) {
            String moduleDataId = queryModuleDataId(moduleId,hiddenId);
            if (moduleDataId == null || "".equalsIgnoreCase(moduleDataId)){
                throw new BusinessException("模块和列数据项未关联");
            }
            if(!updateForHidden(roleModuleId,moduleDataId)){
                insertForHidden(roleModuleId,moduleDataId);
            }
        }
        //更新所有脱敏字段状态
        for (String sensitiveId : sensitiveIds) {
            String moduleDataId = queryModuleDataId(moduleId,sensitiveId);
            if (moduleDataId == null || "".equalsIgnoreCase(moduleDataId)){
                throw new BusinessException("模块和列数据项未关联");
            }
            if(!updateForSensitive(roleModuleId,moduleDataId)){
                insertForSensitive(roleModuleId,moduleDataId);
            }
        }
    }

    /**
     * 查询角色模块表, 未查询到则新增
     * @param roleId 角色id
     * @param moduleId 模块id
     * @return id
     */
    private String addRoleModuleLk(String roleId,String moduleId){
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("roleId", roleId);
        queryParams.put("moduleId", moduleId);
        String roleModuleId = unique("queryRoleModuleId", queryParams);
        if(StringUtils.isNotBlank(roleModuleId)) {
            return roleModuleId;
        }
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        String id = UtilHelper.getUUID();
        params.put("id",id);
        params.put("roleId",roleId);
        params.put("moduleId",moduleId);
        params.put("createTime",DateUtil.getNow());
        params.put("createBy",ServletUtil.getCurrentUser());
        insert("addRoleModuleLk", params);
        return id;
    }

    /**
     * 查询模块数据表
     * @param moduleId 模块id
     * @param moduleDataId 角色数据id
     * @return id
     */
    private String queryModuleDataId(String moduleId,String moduleDataId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("moduleId",moduleId);
        params.put("moduleDataId",moduleDataId);
        return unique("queryModuleDataId", params);
    }

    /**
     * 查询角色模块列表
     * @param roleId 角色id
     * @return 模块id集合
     */
    @Override
    public List<String> queryRoleModuleList(String roleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        return findBySqlId("queryRoleModuleList", params);
    }
    /**
     * 查询角色数据项权限
     *
     * @param roleId 角色id
     */
    @Override
    public List<RoleDataItemLk> queryRoleModuleDateItemPermission(String roleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        return findBySqlId("queryRoleDateItemByRoleId", params);
    }

    /**
     * 查询角色数据项权限
     *
     * @param roleId
     * @param moduleId
     */
    @Override
    public List<RoleDataItemLk> queryRoleModuleDateItemPermission(String roleId, String moduleId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        params.put("moduleId", moduleId);
        return findBySqlId("queryRoleDateItem", params);
    }

    @Override
    public List<RoleDataItemDetail> queryByRoleIdAndCode(String roleId, String moduleCode) {

        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        params.put("roleId",roleId);
        params.put("code", moduleCode);
        return roleDataItemLkMapper.findByRoleIdAndResourceCode(params);
    }

    @Override
    public List<Map<String,Object>> queryRoleModuleByRoleId(String roleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role_data_item_lk", "rdik");
        //如果不是管理员，才用roleIc去查询
        if (!"admin".equals(ServletUtil.getCurrentUser())){
            params.put("roleId",roleId);
        }

        return findBySqlId("queryRoleModuleByRoleId", params);
    }

    /**
     * 查询角色模块
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryModuleByRoleId(String roleId) {
        //获取当权限的map
        Map<String,Object> params = new HashMap<>();
        params.put("roleId",roleId);
        return roleDataItemLkMapper.queryModuleByRoleId(params);
    }
}
