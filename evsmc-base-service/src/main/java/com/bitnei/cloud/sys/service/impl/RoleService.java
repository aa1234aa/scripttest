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
        import com.bitnei.cloud.sys.domain.ModuleDataItem;
import com.bitnei.cloud.sys.domain.Role;
import com.bitnei.cloud.sys.domain.RoleDataItemLk;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.ModuleDataItemModel;
import com.bitnei.cloud.sys.model.RoleDataItemLkModel;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IRoleDataItemLkService;
import com.bitnei.cloud.sys.service.IRoleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
 * 功能： RoleService实现<br>
 * 描述： RoleService实现<br>
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
 * <td>2018-11-22 10:06:09</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.RoleMapper" )
public class RoleService extends BaseService implements IRoleService {


    @Resource
    private IRoleDataItemLkService roleDataItemLkService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<Role> entries = findBySqlId("pagerModel", params);
            List<RoleModel> models = new ArrayList<>();
            for(Role entry: entries){
                RoleModel model = RoleModel.fromEntry(entry);
                //统计关联账户数量
                model.setUserCount(statRoleUserCount(model.getId()));
                models.add(model);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RoleModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                Role obj = (Role)entry;
                RoleModel model = RoleModel.fromEntry(obj);
                //统计关联账户数量
                model.setUserCount(statRoleUserCount(model.getId()));
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public RoleModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("id",id);
        Role entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("角色不存在");
        }
        RoleModel model = RoleModel.fromEntry(entry);
        //统计关联账户数量
        model.setUserCount(statRoleUserCount(model.getId()));
        return model;
    }


    @Override
    public RoleModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("name",name);
        Role entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        RoleModel model = RoleModel.fromEntry(entry);
        //统计关联账户数量
        model.setUserCount(statRoleUserCount(model.getId()));
        return model;
    }


    @Override
    public RoleModel insert(RoleModel model) {

        if(roleNameIsExist(model.getName())){
            throw new BusinessException("角色名称已存在");
        }

        Role obj = new Role();
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
        return RoleModel.fromEntry(obj);

    }

    @Override
    public void update(RoleModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");

        Role obj = new Role();
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
     * @param ids ids
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Role>(this, "pagerModel", params, "sys/res/role/export.xls", "角色管理") {
        }.work();
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ROLE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<RoleModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model RoleModel
             * @return 错误集
             */
            @Override
            public List<String> extendValidate(RoleModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model RoleModel
             */
            @Override
            public void saveObject(RoleModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ROLE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<RoleModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model RoleModel
             * @return 错误集
             */
            @Override
            public List<String> extendValidate(RoleModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model RoleModel
             */
            @Override
            public void saveObject(RoleModel model) {
                update(model);
            }
        }.work();

    }


    /**
     * 查询关联账号
     */
    @Override
    public List<UserModel> queryAssociatedAccount(String roleId, String trueName, String loginName, String mobile){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId",roleId);
        params.put("trueName",trueName);
        params.put("loginName",loginName);
        params.put("mobile",mobile);
        List<User> entries = findBySqlId("queryAssociatedAccount", params);
        List<UserModel> models = new ArrayList<>();
        for(User entry: entries){
            models.add(UserModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 查询未关联账号
     */
    @Override
    public List<UserModel> queryUnassociatedAccount(String roleId, String trueName, String loginName, String mobile){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId",roleId);
        params.put("trueName",trueName);
        params.put("loginName",loginName);
        params.put("mobile",mobile);
        List<User> entries = findBySqlId("queryUnassociatedAccount", params);
        List<UserModel> models = new ArrayList<>();
        for(User entry: entries){
            models.add(UserModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 关联账号
     */
    @Override
    public void bindAccount(String roleId, List<String> accountIds){
        //删除所有已经绑定的
        unbindAccount(roleId);
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        for (String accountId : accountIds){
            String id = UtilHelper.getUUID();
            params.put("id", id);
            params.put("roleId", roleId);
            params.put("userId", accountId);
            super.insert("bindAccount", params);
        }
    }

    /**
     * 查询是否已经绑定 已绑定:true  未绑定:false
     */
    private boolean queryRoleAccountBinding(String userId, String roleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("userId",userId);
        params.put("roleId",roleId);
        Integer res = unique("queryRoleAccountBinding", params);
        return res != 0;
    }

    /**
     * 解除关联账号
     * @param roleId 文件
     */
    private void unbindAccount(String roleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId",roleId);
        super.delete("unbindAccount",params);
    }

    /**
     * 查询关联角色
     */
    @Override
    public List<RoleModel> queryAssociatedRole(String userId, String roleName){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("userId",userId);
        params.put("roleName",roleName);
        List<Role> entries = findBySqlId("queryAssociatedRole", params);
        List<RoleModel> models = new ArrayList<>();
        for(Role entry: entries){
            models.add(RoleModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 查询未关联角色
     */
    @Override
    public List<RoleModel> queryUnassociatedRole(String userId, String roleName){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("userId",userId);
        params.put("roleName",roleName);
        List<Role> entries = findBySqlId("queryUnassociatedRole", params);
        List<RoleModel> models = new ArrayList<>();
        for(Role entry: entries){
            models.add(RoleModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 关联角色
     */
    @Override
    public void bindRole(String userId, List<String> roleIds){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        for (String roleId : roleIds){
            String id = UtilHelper.getUUID();
            params.put("id",id);
            params.put("roleId",roleId);
            params.put("userId",userId);
            super.insert("bindRole", params);
        }
    }

    /**
     * 解除关联角色
     */
    @Override
    public void unbindRole(String userId, List<String> roleIds){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        for (String roleId : roleIds){
            String id = UtilHelper.getUUID();
            params.put("id",id);
            params.put("roleId",roleId);
            params.put("userId",userId);
            super.insert("unbindRole", params);
        }
    }

    /**
     * 拷贝角色
     *
     */
    @Override
    public void copyRole(String sourceRoleId, String targetRoleId){
        //  获取源角色
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("id", sourceRoleId);
        Role sourceRole = unique("findById", params);
        if(sourceRole == null) {
            throw new BusinessException("源角色不存在");
        }
        // 获取目标角色
        params.put("id", targetRoleId);
        Role targetRole =unique("findById", params);
        if(targetRole == null) {
            throw new BusinessException("目标角色不存在");
        }
        // 获取源角色模块集合
        List<String> sourceRoleModules = roleDataItemLkService.queryRoleModuleList(sourceRoleId);
        // 取源角色模块集合与目标角色模块集合的差集
        for (String moduleId : sourceRoleModules) {
            // 源角色模块数据项集
            List<RoleDataItemLk> sourceRoleDataItemLks = roleDataItemLkService.queryRoleModuleDateItemPermission(sourceRoleId, moduleId);
            List<String> hiddenIds = Lists.newArrayList();
            List<String> sensitiveIds = Lists.newArrayList();
            for (RoleDataItemLk d : sourceRoleDataItemLks) {
                if(Constant.TrueAndFalse.TRUE.equals(d.getIsHidden())) {
                    hiddenIds.add(d.getModuleDataItemId());
                }
                if(Constant.TrueAndFalse.TRUE.equals(d.getIsSensitive())) {
                    sensitiveIds.add(d.getModuleDataItemId());
                }
            }
            roleDataItemLkService.updateRoleDateItemPermission(targetRoleId,moduleId,hiddenIds,sensitiveIds);
        }

    }

    /**
     * 统计角色关联用户数
     * @param roleId  角色Id
     */
    private Integer statRoleUserCount(String roleId){
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId", roleId);
        Integer entry = unique("statRoleUserCount", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return entry;
    }

    /**
     * 查询角色模块数据项配置状态
     */
    @Override
    public List<ModuleDataItemModel> queryRoleModuleDataItemPermission(String roleId,String moduleId){
        //查询所有的可配置项
        List<ModuleDataItemModel> moduleDataItem = queryModuleDataItem(moduleId);
        List<RoleDataItemLkModel> roleDataItemLkModel = queryRoleModuleDataItem(roleId,moduleId);
//        if(moduleDataItem!=null && roleDataItemLkModel!=null){
//            for(ModuleDataItemModel dt:moduleDataItem){
//                for(RoleDataItemLkModel rdt:roleDataItemLkModel){
//                    if(dt.getModuleDataId().equalsIgnoreCase(rdt.getModuleDataId())){
//                        dt.setIsSensitive(rdt.getIsSensitive());
//                        dt.setIsHidden(rdt.getIsHidden());
//                    }
//                }
//            }
//        }
        return moduleDataItem;
    }

    /**
     * 查询模块数据项
     */
    private List<ModuleDataItemModel> queryModuleDataItem(String moduleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("moduleId", moduleId);
        List<ModuleDataItem> entries = findBySqlId("queryModuleDataItem", params);
        List<ModuleDataItemModel> models = new ArrayList<>();
        for(ModuleDataItem entry: entries){
            models.add(ModuleDataItemModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 查询角色模块数据项(配置脱敏隐藏的)
     */
    private List<RoleDataItemLkModel> queryRoleModuleDataItem(String roleId,String moduleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId", roleId);
        params.put("moduleId", moduleId);
        List<RoleDataItemLk> entries = findBySqlId("queryRoleModuleDataItem", params);
        List<RoleDataItemLkModel> models = new ArrayList<>();
        for(RoleDataItemLk entry: entries){
            models.add(RoleDataItemLkModel.fromEntry(entry));
        }
        return models;
    }


    /**
     * 设置角色数据项权限，是否脱敏，是否隐藏
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleModuleDateItemPermission(String roleId,String moduleId,Map<String,String> conf){
        String roleModuleId = queryRoleModuleId(roleId,moduleId);

        for (String moduleDataId : conf.keySet()) {
            String p[] = conf.get(moduleDataId).split(",");
            int res = updateSysRoleDataItemLk(roleModuleId,moduleDataId,p[0],p[1]);
            if(res==0){
                insertSysRoleDataItemLk(roleModuleId,moduleDataId,p[0],p[1]);
            }
        }
    }

    private String queryRoleModuleId(String roleId,String moduleId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleId",roleId);
        params.put("moduleId",moduleId);
        String roleModuleId = unique("queryRoleModuleId", params);
        if (roleModuleId == null){
            throw new BusinessException("对象已不存在");
        }
        return roleModuleId;
    }

    private int updateSysRoleDataItemLk(String roleModuleId,String moduleDataId,String isHidden,String isSensitive) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("roleModuleId",roleModuleId);
        params.put("moduleDataId",moduleDataId);
        params.put("isHidden",isHidden);
        params.put("isSensitive",isSensitive);
        params.put("updateTime",DateUtil.getNow());
        params.put("updateBy",ServletUtil.getCurrentUser());
        return super.update("updateSysRoleDataItemLk",params);
    }
    private void insertSysRoleDataItemLk(String roleModuleId,String moduleDataId,String isHidden,String isSensitive) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        String id = UtilHelper.getUUID();
        params.put("id",id);
        params.put("roleModuleId",roleModuleId);
        params.put("moduleDataId",moduleDataId);
        params.put("isHidden",isHidden);
        params.put("isSensitive",isSensitive);
        params.put("createTime",DateUtil.getNow());
        params.put("createBy",ServletUtil.getCurrentUser());
        super.insert("insertSysRoleDataItemLk",params);
    }

    /**
     * 检查角色名称是否存在
     * @param name 角色名称
     * @return true:存在 false:不存在
     */
    private boolean roleNameIsExist(String name){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_role", "role");
        params.put("name",name);
        Role entry = unique("findByName", params);
        return entry != null;
    }
}
