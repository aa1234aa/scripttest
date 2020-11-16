package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.service.INotifierVehicleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.*;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.util.RegexUtil;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UserService实现<br>
 * 描述： UserService实现<br>
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
 * <td>2018-11-12 10:33:47</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UserMapper")
public class UserService extends BaseService implements IUserService {

    @Value("${app.security.rsa.privateKey}")
    private String rsaPriKey;

    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private UserBlockResourceMapper userBlockResourceMapper;
    @Resource
    private UserGroupMapper userGroupMapper;
    @Resource
    private GroupResourceRuleMapper groupResourceRuleMapper;

    @Resource
    private INotifierVehicleLkService notifierVehicleLkService;

    @Resource(name = "webRedisKit")
    private RedisKit redisKit;

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    @Override
    public Object list(PagerInfo pagerInfo) {

        Map<String, String> lockDetail = new HashMap<>(5);
        Set<String> lockUsers = redisKit.keys(Constant.redisKeyPrefix_user_lock + "*");
        if (lockUsers.size() > 0) {
            for (String each : lockUsers) {
                String userId = each.substring(each.lastIndexOf("_") + 1);
                String reason = redisKit.get(each, "");
                lockDetail.put(userId, reason);
            }
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_user", "u");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<User> entries = findBySqlId("pagerModel", params);
            List<UserModel> models = new ArrayList<>();
            for (User entry : entries) {
                UserModel userModel = UserModel.fromEntry(entry);
                if (lockUsers.contains(Constant.redisKeyPrefix_user_lock + userModel.getId())) {
                    userModel.setIsFrozen(Constant.TrueAndFalse.TRUE);
                    userModel.setIsFrozenDisplay("是");
                    userModel.setFrozenReason(lockDetail.get(userModel.getId()));
                } else {
                    userModel.setIsFrozen(Constant.TrueAndFalse.FALSE);
                    userModel.setIsFrozenDisplay("否");
                }
                models.add(userModel);
            }
            return models;
        }
        // 分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<UserModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                User obj = (User) entry;
                UserModel userModel = UserModel.fromEntry(obj);
                if (lockUsers.contains(Constant.redisKeyPrefix_user_lock + userModel.getId())) {
                    userModel.setIsFrozen(1);
                    userModel.setIsFrozenDisplay("是");
                    userModel.setFrozenReason(lockDetail.get(userModel.getId()));
                } else {
                    userModel.setIsFrozen(0);
                    userModel.setIsFrozenDisplay("否");
                }
                models.add(userModel);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<User> find(Map<String, Object> params) {
        // 获取当权限的map
        Map<String, Object> param = DataAccessKit.getAuthMap("sys_user", "u");
        param.putAll(params);
        return findBySqlId("pagerModel", param);
    }


    @Override
    public UserModel get(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_user", "u");
        params.put("id", id);
        User entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }

        UserModel userModel = UserModel.fromEntry(entry);
        String lockReason = redisKit.get(Constant.redisKeyPrefix_user_lock + id, "默认原因");

        if (StringUtil.isNotEmpty(lockReason)) {
            userModel.setIsFrozen(1);
            userModel.setIsFrozenDisplay("是");
            userModel.setFrozenReason(lockReason);
        } else {
            userModel.setIsFrozen(0);
            userModel.setIsFrozenDisplay("否");
        }

        return userModel;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(UserModel model) {
        User obj = new User();
        BeanUtils.copyProperties(model, obj);
        String message = checkUserName(obj.getUsername(), "");
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }

        String decryPassword;
        try {
            decryPassword = RSAUtil.decryptByPrivateKey(obj.getPassword(), rsaPriKey);

        } catch (Exception e) {
            throw new BusinessException("密码解密失败，请检查加密程序是否正常", -2);
        }
        String userId = UtilHelper.getUUID();
        obj.setId(userId);
        obj.setPassword(SecurityUtil.getMd5(decryPassword));
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());

        if (model.getIsNeverExpire() == 0) {
            if (model.getPermitStartDate().compareTo(model.getPermitEndDate()) > 0) {
                throw new BusinessException("账号的有效起始日期不能大于截止日期");
            }
        }

        if (!StringUtils.isEmpty(model.getRoleIds())) {
            String[] str = model.getRoleIds().split(",");
            if (str.length > 0) {
                obj.setDefRoleId(str[0]);
            }
        }

        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        if (!StringUtils.isEmpty(model.getDeptIds())) {
            insertUserDepts(model.getDeptIds(), obj);
        }

        if (!StringUtils.isEmpty(model.getGroupIds())) {
            insertUserGroups(model.getGroupIds(), obj);
        }

        if (!StringUtils.isEmpty(model.getRoleIds())) {
            insertUserRoles(model.getRoleIds(), obj);
        }

        //创建用户权限组
        String groupId = UtilHelper.getUUID();
        Group group = new Group();
        group.setId(groupId);
        group.setName(obj.getUsername() + "@group");
        group.setRuleType(2);
        group.setResourceTypeId("1111");
        group.setIsValid(1);
        group.setDescription("默认用户权限组");
        group.setCreateBy(ServletUtil.getCurrentUser());
        group.setCreateTime(DateUtil.getNow());
        group.setUserId(userId);
        groupMapper.insertDefault(group);
//
//        //创建用户和组的关联
//        UserGroupLk userGroupLk = new UserGroupLk();
//        userGroupLk.setGroupId(groupId);
//        userGroupLk.setUserId(userId);
//        userGroupLk.setId(DateUtil.getNow());
//        userGroupLk.setCreateBy(ServletUtil.getCurrentUser());
//        userGroupLk.setCreateTime(DateUtil.getNow());
//        userGroupMapper.insert(userGroupLk);

        //创建组的规则配置
        GroupResourceRule groupResourceRule = new GroupResourceRule();
        groupResourceRule.setId(UtilHelper.getUUID());
        groupResourceRule.setCreateBy(ServletUtil.getCurrentUser());
        groupResourceRule.setCreateTime(DateUtil.getNow());
        groupResourceRule.setResourceItemId("2ac0576787ee4742adb61cb0e6e0e98d");
        groupResourceRule.setGroupId(groupId);
        groupResourceRule.setOrderNum(0);
        groupResourceRule.setPreLogicOp(1);
        groupResourceRule.setOp(0);
        groupResourceRuleMapper.insert(groupResourceRule);


//        //创建黑名单组
//        UserBlockResource userBlockResource = new UserBlockResource();
//        userBlockResource.setId(UtilHelper.getUUID());
//        userBlockResource.setCreateBy(ServletUtil.getCurrentUser());
//        userBlockResource.setCreateTime(DateUtil.getNow());
//        userBlockResource.setUserId(userId);
//        userBlockResource.setResourceItemId("2ac0576787ee4742adb61cb0e6e0e98d");
//        userBlockResourceMapper.insert(userBlockResource);


    }


    /**
     * 插入用户-组织架构信息
     *
     * @param deptIds deptIds
     * @param user    User
     * @return 影响行数
     */
    private int insertUserDepts(String deptIds, User user) {
        if (null != user && !StringUtils.isEmpty(deptIds)) {
            List<UserDeptLk> list = new ArrayList<UserDeptLk>();
            String[] str = deptIds.split(",");
            for (String dId : str) {
                UserDeptLk lk = new UserDeptLk();
                lk.setId(UtilHelper.getUUID());
                lk.setUserId(user.getId());
                lk.setDeptId(dId);
                lk.setCreateTime(user.getCreateTime());
                lk.setCreateBy(user.getCreateBy());
                list.add(lk);
            }
            return super.sessionTemplate.insert(getSqlId("insertUserDeptLk"), list);
        }
        return 0;
    }

    /**
     * 插入用户-组织架构信息
     *
     * @param groupIds groupIds
     * @param user     User
     * @return 影响行数
     */
    private int insertUserGroups(String groupIds, User user) {
        if (null != user && !StringUtils.isEmpty(groupIds)) {
            List<UserGroupLk> list = new ArrayList<UserGroupLk>();
            String[] str = groupIds.split(",");
            for (String gId : str) {
                UserGroupLk lk = new UserGroupLk();
                lk.setId(UtilHelper.getUUID());
                lk.setUserId(user.getId());
                lk.setGroupId(gId);
                lk.setCreateTime(user.getCreateTime());
                lk.setCreateBy(user.getCreateBy());
                list.add(lk);
            }
            return super.sessionTemplate.insert(getSqlId("insertUserGroupLk"), list);
        }
        return 0;
    }

    /**
     * 插入用户-组织架构信息
     *
     * @param roleIds 用户id集
     * @param user    用户
     * @return 影响行数
     */
    private int insertUserRoles(String roleIds, User user) {
        if (null != user && !StringUtils.isEmpty(roleIds)) {
            List<UserRoleLk> list = new ArrayList<UserRoleLk>();
            String[] str = roleIds.split(",");
            for (String rId : str) {
                UserRoleLk lk = new UserRoleLk();
                lk.setId(UtilHelper.getUUID());
                lk.setUserId(user.getId());
                lk.setRoleId(rId);
                list.add(lk);
            }
            return super.sessionTemplate.insert(getSqlId("insertUserRoleLk"), list);
        }
        return 0;
    }

    /**
     * 删除用户-组织架构信息
     *
     * @param userId 用户id
     * @return 删除行数
     */
    private int deleteUserDepts(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            return super.delete("deleteUserDeptsByUserId", userId);
        }
        return 0;
    }

    /**
     * 删除用户-数据权限组信息信息
     *
     * @param userId 用户id
     * @return 删除行数
     */
    private int deleteUserGroups(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            return super.delete("deleteUserGroupsByUserId", userId);
        }
        return 0;
    }

    /**
     * 删除用户-数据权限组信息信息
     *
     * @param userId 用户id
     * @return 删除行数
     */
    private int deleteUserRoles(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            return super.delete("deleteUserRolesByUserId", userId);
        }
        return 0;
    }

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    @Override
    public void update(UserModel model) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_user", "u");
        User obj = new User();
        BeanUtils.copyProperties(model, obj);
        String message = checkUserName(obj.getUsername(), obj.getId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        obj.setPassword(SecurityUtil.getMd5(obj.getPassword()));
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());

        if (model.getIsNeverExpire() == 0) {
            if (model.getPermitStartDate().compareTo(model.getPermitEndDate()) > 0) {
                throw new BusinessException("账号的有效起始日期不能大于截止日期");
            }
        }

        if (StringUtils.isEmpty(model.getDefRoleId()) && StringUtils.isNotEmpty(model.getRoleIds())) {
            List<String> roleIds = Arrays.asList(model.getRoleIds().split(","));
            if (roleIds.size() > 0) {
                obj.setDefRoleId(roleIds.get(0));

                Map<String, Object> roleParams = new HashMap<>();
                roleParams.put("roleIds", roleIds);
                List<Map<String, Object>> rolesMapList = findBySqlId("findRoleByRoleIds", roleParams);

                //必须验证是否有效，以有效的为准
                for (Map<String, Object> rolesMap : rolesMapList) {
                    if (rolesMap.containsKey("isValid") && null != rolesMap.get("isValid")) {
                        if (1 == (int) rolesMap.get("isValid")) {
                            obj.setDefRoleId((String) rolesMap.get("roleId"));
                            break;
                        }
                    }
                }
            }
        }

        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        // 修改成功后先删除用户-组织架构信息再添加
        deleteUserDepts(obj.getId());
        if (!StringUtils.isEmpty(model.getDeptIds())) {
            insertUserDepts(model.getDeptIds(), obj);
        }

        deleteUserGroups(obj.getId());
        if (!StringUtils.isEmpty(model.getGroupIds())) {
            insertUserGroups(model.getGroupIds(), obj);
        }

        deleteUserRoles(obj.getId());
        if (!StringUtils.isEmpty(model.getRoleIds())) {
            insertUserRoles(model.getRoleIds(), obj);
        }
    }

    /**
     * 删除多个
     *
     * @param ids id集合
     * @return 删除行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_user", "u");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            deleteUserDepts(id);
            deleteUserGroups(id);
            deleteUserRoles(id);
            count += r;

            //同时删除账号关系的负责人分配的车辆
            notifierVehicleLkService.deleteByUserId(id);
            //删除相关组
            groupMapper.deleteByUserId(id);
            //删除黑名单资源
            userBlockResourceMapper.deleteByUserId(id);

        }
        return count;
    }

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {
        // 获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<User>(this, "pagerModel", params, "sys/res/user/export.xls", "用户管理") {
        }.work();
        return;
    }

    /**
     * 批量导入
     *
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "USER" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<UserModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model UserModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(UserModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model UserModel
             */
            @Override
            public void saveObject(UserModel model) {
                insert(model);
            }
        }.work();
    }

    /**
     * 批量更新
     *
     * @param file 文件
     */
    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "USER" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<UserModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model UserModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(UserModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model UserModel
             */
            @Override
            public void saveObject(UserModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 重置密码
     *
     * @param username 用户名
     * @param password 新密码
     */
    @Override
    public void resetPwd(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessException("用户名不可为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new BusinessException("密码不可为空");
        }
        String decryPassword;
        try {
            decryPassword = RSAUtil.decryptByPrivateKey(password, rsaPriKey);
        } catch (Exception e) {
            throw new BusinessException("密码解密失败，请检查加密程序是否正常", -2);
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("username", username);
        params.put("password", SecurityUtil.getMd5(decryPassword));
        int res = super.update("resetPwd", params);
        if (res == 0) {
            throw new BusinessException("重置失败，未匹配到此用户名的数据");
        }
    }

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    @Override
    public UserModel findByUsername(String username) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_user", "u");
        params.put("username", username);
        User entry = unique("findByUsername", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UserModel.fromEntry(entry);
    }

    /**
     * 复制用户权限和角色
     *
     * @param sourceUserId 源用户id
     * @param targetUserId 目标用户id
     */
    @Override
    public void copyGroupAndRole(String sourceUserId, String targetUserId) {

        //1、查询源用户的权限和角色
        Map<String, Object> params = new HashMap<>();
        params.put("userId", sourceUserId);
        List<Map<String, Object>> sGroupList = super.findBySqlId("findListUserGroupsByUserId", params);
        List<Map<String, Object>> sRoleList = super.findBySqlId("findListUserRolesByUserId", params);

        //2、查询目标用户的权限和角色
        Map<String, Object> tParams = new HashMap<>();
        tParams.put("userId", targetUserId);
        List<Map<String, Object>> tGroupList = super.findBySqlId("findListUserGroupsByUserId", tParams);
        List<Map<String, Object>> tRoleList = super.findBySqlId("findListUserRolesByUserId", tParams);
        //3、比较目标用户的权限和角色是否已经存在要复制的
        for (Map<String, Object> tMap : tGroupList) {
            sGroupList.removeIf(sMap -> tMap.get("groupId").equals(sMap.get("groupId")));
        }

        for (Map<String, Object> tMap : tRoleList) {
            sRoleList.removeIf(sMap -> tMap.get("roleId").equals(sMap.get("roleId")));
        }

        String groupIds = "";
        if (CollectionUtils.isNotEmpty(sGroupList)) {
            for (Map<String, Object> map : sGroupList) {
                if (!StringUtils.isEmpty(groupIds)) {
                    groupIds += ",";
                }
                groupIds += map.get("groupId");
            }
        }

        String roleIds = "";
        if (CollectionUtils.isNotEmpty(sRoleList)) {
            for (Map<String, Object> map : sRoleList) {
                if (!StringUtils.isEmpty(roleIds)) {
                    roleIds += ",";
                }
                roleIds += map.get("roleId");
            }
        }

        User user = new User();
        user.setId(targetUserId);
        if (!StringUtils.isEmpty(groupIds)) {
            insertUserGroups(groupIds, user);
        }

        if (!StringUtils.isEmpty(roleIds)) {
            insertUserRoles(roleIds, user);
        }

    }

    @Override
    public TokenInfo createTokenByUserId(String userId) {

        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("id", userId);

        User user = userMapper.findById(queryMap);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIsAdmin(user.getIsAllPermissions());
        tokenInfo.setRoleId(user.getDefRoleId());
        return tokenInfo;
    }

    /**
     * 自定义搜索
     *
     * @param params 自定义搜索条件
     * @return UserModel
     */
    @Override
    public UserModel queryUserByParam(Map<String, Object> params) {
        List<User> entries = findBySqlId("pagerModel", params);
        if (null != entries && entries.size() > 0 && null != entries.get(0)) {
            return UserModel.fromEntry(entries.get(0));
        }
        return null;
    }

    /**
     * 校验用户名
     *
     * @param username 用户名
     * @param id       数据id
     * @return 错误信息
     */
    private String checkUserName(String username, String id) {
        if (!StringUtils.isEmpty(username)) {
            // 数字和大写字母的正则校验需要加
            boolean bl = username.matches(RegexUtil.C_3_30);
            if (!bl) {
                return "用户名长度为3-30位字母或数字";
            }
            Map<String, Object> params = Maps.newHashMap();
            params.put("usernameEq", username);
            UserModel user = queryUserByParam(params);
            // 如果id不为空则为更新校验
            if (null != user && !StringUtils.equals(user.getId(), id)) {
                // 如果id为空则表示为添加校验，如果id不为空则表示为更新校验，如何查询结果id不同则表示数据库已存在此证件号，返回错误
                return "登录账号已存在";
            }
        }
        return "";
    }


    @Override
    public void adminFrozenUser(String userId, String reason) {

        if (StringUtil.isEmpty(userId)) {
            throw new BusinessException("用户id不能为空");
        }
        User user = userMapper.personalCenterGetUser(userId);
        if (user == null) {
            throw new BusinessException("登录账户不存在");
        }

        redisKit.set(Constant.redisKeyPrefix_user_lock + userId, reason);

    }

    @Override
    public void adminUnfrozenUser(String userId) {

        if (StringUtil.isEmpty(userId)) {
            throw new BusinessException("用户id不能为空");
        }

        User user = userMapper.personalCenterGetUser(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (redisKit.exists(Constant.redisKeyPrefix_user_lock + user.getId())) {
            redisKit.del(Constant.redisKeyPrefix_user_lock + user.getId());

        } else {
            throw new BusinessException("该账号锁定已解除");
        }

    }

    @Override
    public void resetPhoto(String userId, String photoId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", userId);
        params.put("photoId", photoId);
        userMapper.resetPhoto(params);
    }

    /**
     * 开通app权限
     * @param id
     */
    @Override
    public void openAppPermissions(String id) {
        UserModel userModel = get(id);
        if (userModel.getOpenApp() == 1){
            throw new BusinessException("该账户APP权限已开通");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("openApp", 1);
        int row = update("updateOpenApp", param);
        if (row == 0){
            throw new BusinessException("开通APP权限失败");
        }
    }

    /**
     * 关闭app权限
     * @param id
     */
    @Override
    public void turnOffAppPermissions(String id) {
        UserModel userModel = get(id);
        if (userModel.getOpenApp() == 0){
            throw new BusinessException("该账户APP权限已关闭");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("openApp", 0);
        int row = update("updateOpenApp", param);
        if (row == 0){
            throw new BusinessException("关闭APP权限失败");
        }
    }


}
