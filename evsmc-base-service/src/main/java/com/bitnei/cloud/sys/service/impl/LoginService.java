package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.CsrfInfo;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.config.RunEnv;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.ModuleMapper;
import com.bitnei.cloud.sys.dao.PersonalCenterMapper;
import com.bitnei.cloud.sys.dao.UserMapper;
import com.bitnei.cloud.sys.domain.Module;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.domain.UserPersonalSetting;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.ILoginService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.util.Ip2CityUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.SecurityUtil;
import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.RandomString;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-module <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2018-${MOTH}-19</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Service
@Slf4j
public class LoginService implements ILoginService {


    @Value("${app.security.rsa.privateKey}")
    private String rsaPriKey;
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;
    @Value("${app.security.aes.key}")
    private String aesKey;

    @Resource
    private ModuleMapper moduleMapper;
    @Autowired
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource(name = "webRedisKit")
    private RedisKit redisKit;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Autowired
    private RunEnv runEnv;

    @Autowired
    private PersonalCenterService personalCenterService;

    @Override
    public void createCsrf() {


        String csrf = ServletUtil.createCsrf(aesKey);
        CookieUtils.addCookie("CSRF", csrf);
        ServletUtil.getResponse().setHeader("x-api-csrf", csrf);

    }

    @Override
    public Map<String, Object> login(String username, String password, String randCode, String isApp) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("username", username);
        User user = userMapper.findByUsername(queryMap);


//        if (!"admin".equals(username)){

        CsrfInfo csrfInfo = ServletUtil.getCsrf(aesKey);
        if (csrfInfo == null) {
            throw new BusinessException("CSRF值为空，请刷新页面重新获取", -2);
        }
        if (System.currentTimeMillis() > (csrfInfo.getTime() + csrfInfo.getValidTime())) {
            throw new BusinessException("CSRF值失效，请刷新页面重新获取", -2);
        }

        //非
        if (runEnv.getAppMode().equalsIgnoreCase(RunEnv.PROD_MODE) || (runEnv.getAppMode().equalsIgnoreCase(RunEnv.DEV_MODE) && "bitnei".equalsIgnoreCase(randCode))
                || (runEnv.getAppMode().equalsIgnoreCase(RunEnv.TEST_MODE) && "bitnei".equalsIgnoreCase(randCode))) {
            // 处理验证码
            if (StringUtil.isEmpty(randCode)) {
                throw new BusinessException("验证码不能为空");
            }
            String csrf = getCsrf();
            String srvrRandCode = redisKit.get("kaptcha.code." + csrf, "");
            log.info("服务端验证码:{},前端验证码{}", srvrRandCode, randCode);
            if (StringUtil.isEmpty(srvrRandCode)) {
                throw new BusinessException("验证码输入不正确");
            }
            redisKit.del("kaptcha.code." + csrf);
            if (!srvrRandCode.equalsIgnoreCase(randCode)) {
                throw new BusinessException("验证码输入不正确");
            }
        }


        String decryptPassword = "";
        try {
            decryptPassword = RSAUtil.decryptByPrivateKey(password, rsaPriKey);
        } catch (Exception e) {
            throw new BusinessException("密码解密失败，请检查加密程序是否正常", -2);
        }


        if (user == null) {
            throw new BusinessException("用户名或密码不正确，请重新确认");
        }

        if (StringUtils.isNotBlank(isApp)){
            // isApp 1 app登录  0 平台登录
            if ("1".equals(isApp)){
                if (0 == user.getOpenApp()){
                    throw new BusinessException("该账户未分配APP权限，请联系管理员");
                }
            }
        }

        if (StringUtil.isEmpty(user.getDefRoleId())) {
            //填充默认角色
            String defRoleId = userMapper.getRoleId(user.getId());
            if (StringUtil.isEmpty(defRoleId)) {
                throw new BusinessException("用户还没分配角色，请联系管理员");
            }
            userMapper.updateDefRoleId(user.getId(), defRoleId);
            user.setDefRoleId(defRoleId);

        }
        UserPersonalSetting userPersonalSetting = personalCenterService.getUserSetting(user.getId());
        int loginType = userPersonalSetting.getLoginType();
        int isAccountDeprecatedActive = userPersonalSetting.getIsAccountDeprecatedActive();
        int isForceChangePasswordActive = userPersonalSetting.getIsForceChangePasswordActive();

        String lastLoginTime = userPersonalSetting.getLastLoginTime();
        String lastChangePasswordTime = userPersonalSetting.getLastChangePasswordTime();

        if (redisKit.exists(Constant.redisKeyPrefix_user_lock + user.getId()) && (!"admin".equals(user.getUsername()))) {
            Long ttl = redisKit.ttl(Constant.redisKeyPrefix_user_lock + user.getId());
            if (-1 == ttl) {
                throw new BusinessException("账户已锁定，请联系管理员");
            } else {
                throw new BusinessException("该账户登录异常，账户已锁定，请两小时后再次尝试或联系管理员");
            }
        }

        //连续输入错误密码
        String sPassword = SecurityUtil.getMd5(decryptPassword);
        if (!sPassword.equals(user.getPassword())) {
            long tryCount = redisKit.incr(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");
            if (tryCount == 1) {
                redisKit.expire(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay", 60 * 60 * 12);
            }
            if (tryCount >= 7) {
                redisKit.setex(Constant.redisKeyPrefix_user_lock + user.getId(), "12小时内连续7次密码错误", 60 * 60 * 2);
                //计数清零
                redisKit.del(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");

                throw new BusinessException("连续7次密码错误，账号锁定2小时");
            }

            long rest = 7 - tryCount;
            throw new BusinessException("账号或密码输入有误，剩余" + rest + "次机会");

        } else {
            //计数清0
            redisKit.del(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");
        }

        HttpServletRequest httpServletRequest = ServletUtil.getRequest();
        String realIp = Ip2CityUtil.getRealIp(httpServletRequest);
        String loginRegion = "";

        if (StringUtil.isNotEmpty(realIp)) {
            try {
                loginRegion = Ip2CityUtil.getCityInfo(realIp);
            } catch (Exception e) {
                loginRegion = "";
            }
        }

        log.info("用户:" + user.getUsername().replaceAll("[\r\n]", "") + "在" + DateUtil.getNow().replaceAll("[\r\n]", "") + "登录的ip是:" + realIp.replaceAll("[\r\n]", ""));
        if (StringUtil.isNotEmpty(loginRegion)) {
            Set<String> region = redisKit.smembers(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay");
            if (region.size() == 0) {
                redisKit.sadd(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", loginRegion);
                redisKit.expire(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", 60 * 60 * 24);
            } else if (!region.contains(loginRegion)) {
                redisKit.sadd(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", loginRegion);
            }
            if ((region.size() >= 4) && (!region.contains(loginRegion))) {

                redisKit.setex(Constant.redisKeyPrefix_user_lock + user.getId(), "一天内5个不同地级地区登录", 60 * 60 * 2);
                redisKit.del(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay");
                throw new BusinessException("一天内2次异地登录，账号已经被锁定，请两小时后再次尝试或联系管理员");
            }

        }

        if (user.getIsValid() == 0) {
            throw new BusinessException("用户无效，请联系管理员");
        }
        if (user.getIsNeverExpire() == 0) {

            String now = DateUtil.getNow();
            if (user.getPermitStartDate().compareTo(now) > 0) {
                throw new BusinessException(String.format("您的权限于%s开始有效，有疑问请联系管理员", user.getPermitStartDate()));
            }
            if (user.getPermitEndDate().compareTo(now) < 0) {
                throw new BusinessException(String.format("您的权限于%s过期，有疑问请联系管理员", user.getPermitEndDate()));
            }
        }

//        }

        //90天未登录回收账号启用，则回收账号
        if (StringUtil.isNotEmpty(lastLoginTime) && (isAccountDeprecatedActive == 1)) {
            long noLoginCount = DateUtil.getTimesDiff(1, lastLoginTime, DateUtil.getNow());
            if (noLoginCount >= 90) {
                Map<String, Object> map = new HashMap<>(4);
                // 账号状态失效，登录日期改为当前
                map.put("isValid", 0);
                map.put("userId", user.getId());
                userMapper.deValidUser(map);
                map.put("lastLoginTime", DateUtil.getNow());
                personalCenterMapper.updateUserSetting(map);
                throw new BusinessException("该账户90天未登录，账户已回收");

            }

        }
        //初始密码已经修改过,强制改密码启用，90天未改密码则提示修改密码
        if (loginType != 0) {
            long timeCount = 0L;
            if (StringUtil.isNotEmpty(lastChangePasswordTime) && (isForceChangePasswordActive == 1)) {
                timeCount = DateUtil.getTimesDiff(1, lastChangePasswordTime, DateUtil.getNow());
                if (timeCount >= 90) {
                    loginType = 1;
                }
            }
        }


        long timeout = NumberUtils.toLong(userPersonalSetting.getKeepOnLineTime(), 60);

        Map<String, Object> resultMap = new HashMap<>();

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIsAdmin(user.getIsAllPermissions());
        tokenInfo.setRoleId(user.getDefRoleId());
        tokenInfo.setCreateTime(System.currentTimeMillis());
        tokenInfo.setTimeout(timeout * 1000 * 60);

        String token = JwtUtil.createJWT(1000 * 60 * 60 * 24 * 10, tokenInfo, jwtSecret);
        resultMap.put("token", token);
        resultMap.put("loginType", loginType);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("userId", user.getId());
        updateMap.put("photoId", user.getPhotoId());
        updateMap.put("lastLoginTime", DateUtil.getNow());

        personalCenterMapper.updateUserSetting(updateMap);

        ServletUtil.getResponse().addHeader("Authorization", "Bearer " + token);
        return resultMap;

    }


    @Override
    public Map<String, Object> me() {

        Map<String, Object> result = new HashMap<>();

        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        UserModel userModel = userService.get(userId);
        DataLoader.loadNames(userModel);
        result.putAll(MapperUtil.Object2Map(userModel));

        String roleId = ServletUtil.getCurrentTokenInfo().getRoleId();
        Map<String, Object> userRole = userMapper.findUserRole(roleId);

        //默认不合法，查到数据合法才是合法
        int isRoleValid = 0;

        if (null == userRole || userRole.size() == 0) {
            Map<String, Object> tParams = new HashMap<>();
            tParams.put("userId", ServletUtil.getCurrentTokenInfo().getUserId());
            List<Map<String, Object>> tRoleList =
                    userService.findBySqlId("findListUserRolesByUserId", tParams);

            if (CollectionUtils.isEmpty(tRoleList)) {
                throw new BusinessException("该用户未绑定任何角色，请联系管理员");
            }

            //获取第一个有效角色
            for (Map<String, Object> role : tRoleList) {

                userRole = role;
                if (userRole.containsKey("isValid") && null != userRole.get("isValid")) {
                    if (1 == (int) userRole.get("isValid")) {
                        //找到有效的角色，更新用户表
                        userModel.setDefRoleId((String) userRole.get("roleId"));
                        userService.update(userModel);
                        isRoleValid = 1;
                        break;
                    }
                } else {
                    userRole.put("isValid", 0);
                }
            }
        } else if (userRole.containsKey("isValid") && null != userRole.get("isValid")) {

            isRoleValid = (int) userRole.get("isValid");
        }

        if (0 == isRoleValid) {
            result.put("curRoleIsValid", 0);
            result.put("curRoleIsValidName", "否");
        } else {
            result.put("curRoleIsValid", 1);
            result.put("curRoleIsValidName", "是");
        }

        if (null != userRole && userRole.containsKey("roleId")) {

            String curRoleId = userRole.get("roleId").toString();
            result.put("curRoleId", curRoleId);
            String indexUrl = moduleMapper.findIndexUrl(curRoleId);
            if (StringUtil.isEmpty(indexUrl)) {
                result.put("indexUrl", "");
            } else {
                result.put("indexUrl", indexUrl);
            }

        } else {
            throw new BusinessException("该用户关联角色不存在，请联系管理员");
        }

        return result;
    }


    @Override
    public void logout() {

        //从redis中清除
        CookieUtils.removeCookie("CSRF");
        CookieUtils.removeCookie("R_SESS");
    }


    @Override
    public TreeNode menus() {

        //获取当权限的map
        String roleId = ServletUtil.getCurrentTokenInfo().getRoleId();

        Map<String, Object> params = new HashMap<>();
        params.put("isFun", 0);
        if (!ServletUtil.getCurrentTokenInfo().getUsername().equals("admin")) {
            params.put("roleId", ServletUtil.getCurrentTokenInfo().getRoleId());
        }
        //生产模式下隐藏
        if (runEnv.getAppMode().equalsIgnoreCase(RunEnv.PROD_MODE)) {
            params.put("isHidden", 0);
        }

        Map<String, Object> userRole = userMapper.findUserRole(roleId);
        if (null == userRole || userRole.size() == 0) {
            throw new BusinessException("用户关联角色信息错误，请重新登录");
        }

        int isRoleValid = (int) userRole.get("isValid");

        List<Module> entries = Lists.newArrayList();

        if (isRoleValid == 1) {
            entries = moduleMapper.pagerModel(params);
        } else {
            entries = moduleMapper.findRootAndIndex();
        }
        for (Module m : entries) {
            boolean isLeaf = true;
            for (Module sm : entries) {
                if (StringUtil.isNotEmpty(sm.getParentId()) && m.getId().equals(sm.getParentId())) {
                    if (sm.getIsFun() != null && sm.getIsFun() == 0) {
                        if (StringUtil.isEmpty(sm.getCode())) {
                            isLeaf = false;
                            break;
                        } else if (!sm.getCode().contains("@tab")) {
                            isLeaf = false;
                            break;
                        }
                    }

                }
            }
            m.set("isLeaf", isLeaf);
        }

        TreeNode root = new TreeHandler<Module>(entries) {
            @Override
            protected TreeNode beanToTreeNode(Module bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                tn.setIcon(StringUtil.isNotEmpty(bean.getIcon()) ? bean.getIcon() : "fa fa-book");
                Map<String, Object> attr = new HashMap<>();
                attr.put("is_fun", bean.getIsFun());
                attr.put("action", bean.getAction());
                attr.put("order_num", bean.getOrderNum());
                attr.put("is_root", bean.getIsRoot());
                attr.put("isLeaf", bean.get("isLeaf"));
                tn.setAttributes(attr);
                return tn;
            }

            @Override
            protected boolean isRoot(TreeNode node) {
                Object isRoot = node.getAttributes().get("is_root");
                if (isRoot != null && isRoot.toString().equals("1")) {
                    return true;
                } else {
                    return false;
                }
            }

        }.toTree();

        return root;
    }

    /**
     * 生成随机验证码
     *
     * @return
     */
    @Override
    public String randCode() {

        //TODO 分布式支持
        String scope = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ";
        String randCode = RandomString.getInstance().random(4, scope);
        String csrf = getCsrf();
        redisKit.setex("kaptcha.code." + csrf, randCode, 60 * 5);
        try {
            return RandomCodeImageUtil.createRandCode(randCode);
        } catch (Exception e) {
            log.error("error", e);
            throw new BusinessException("随机码生成失败");
        }
    }

    /**
     * 生成随机验证码
     *
     * @param mobile
     * @return
     */
    @Override
    public String randCodeToMobile(String mobile) {
        String seed = "0123456789";
        String randCode = RandomString.getInstance().random(6, seed);
        redisKit.setex("kaptcha.code." + mobile, randCode, 60 * 5);
        return randCode;
    }


    /**
     * 获取csrf值
     *
     * @return
     */
    private String getCsrf() {
        String csrf = "";
        try {
            csrf = ServletUtil.getCookieValue("CSRF");
        } catch (Exception e) {
            log.warn("获取不到CSRF值");
        }
        return StringUtils.isEmpty(csrf) ? "" : csrf;

    }

    @Override
    public Map<String, Object> loginByMobile(String mobile, String randCode) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("mobile", mobile);
        User user = userMapper.findByUserMobile(queryMap);

        CsrfInfo csrfInfo = ServletUtil.getCsrf(aesKey);
        if (csrfInfo == null) {
            throw new BusinessException("CSRF值为空，请刷新页面重新获取", -2);
        }
        if (System.currentTimeMillis() > (csrfInfo.getTime() + csrfInfo.getValidTime())) {
            throw new BusinessException("CSRF值失效，请刷新页面重新获取", -2);
        }

        if (user == null) {
            throw new BusinessException("电话号码不存在或者还未添加平台账号，请重新确认");
        }

        if (0 == user.getOpenApp()){
            throw new BusinessException("该账户未开通APP权限，请联系管理员");
        }

        if (StringUtil.isEmpty(user.getDefRoleId())) {
            //填充默认角色
            String defRoleId = userMapper.getRoleId(user.getId());
            if (StringUtil.isEmpty(defRoleId)) {
                throw new BusinessException("用户还没分配角色，请联系管理员");
            }
            userMapper.updateDefRoleId(user.getId(), defRoleId);
            user.setDefRoleId(defRoleId);
        }
        UserPersonalSetting userPersonalSetting = personalCenterService.getUserSetting(user.getId());
        int loginType = userPersonalSetting.getLoginType();
        int isAccountDeprecatedActive = userPersonalSetting.getIsAccountDeprecatedActive();
        int isForceChangePasswordActive = userPersonalSetting.getIsForceChangePasswordActive();

        String lastLoginTime = userPersonalSetting.getLastLoginTime();
        String lastChangePasswordTime = userPersonalSetting.getLastChangePasswordTime();

        if (redisKit.exists(Constant.redisKeyPrefix_user_lock + user.getId()) && (!"admin".equals(user.getUsername()))) {
            Long ttl = redisKit.ttl(Constant.redisKeyPrefix_user_lock + user.getId());
            if (-1 == ttl) {
                throw new BusinessException("账户已锁定，请联系管理员");
            } else {
                throw new BusinessException("该账户登录异常，账户已锁定，请两小时后再次尝试或联系管理员");
            }
        }

        if (StringUtil.isEmpty(randCode)) {
            throw new BusinessException("验证码不能为空");
        }
        String redisRandCode = redisKit.get("kaptcha.code." + mobile, "");
        if (StringUtil.isEmpty(redisRandCode)) {
            throw new BusinessException("验证码输入不存在或已过期");
        }
        //连续输入错误验证码
        if (!redisRandCode.equals(randCode)) {
            long tryCount = redisKit.incr(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");
            if (tryCount == 1) {
                redisKit.expire(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay", 60 * 60 * 12);
            }
            if (tryCount >= 7) {
                redisKit.setex(Constant.redisKeyPrefix_user_lock + user.getId(), "12小时内连续7次验证码错误", 60 * 60 * 2);
                //计数清零
                redisKit.del(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");
                throw new BusinessException("连续7次密码或验证码错误，账号锁定2小时");
            }
            long rest = 7 - tryCount;
            throw new BusinessException("密码或验证码输入有误，剩余" + rest + "次机会");
        } else {
            //计数清0
            redisKit.del(Constant.redisKeyPrefix + user.getId() + "_straightWrongPasswordTryCountInHalfDay");
            //验证码失效
            redisKit.del("kaptcha.code." + mobile);
        }
        HttpServletRequest httpServletRequest = ServletUtil.getRequest();
        String realIp = Ip2CityUtil.getRealIp(httpServletRequest);
        String loginRegion = "";

        if (StringUtil.isNotEmpty(realIp)) {
            try {
                loginRegion = Ip2CityUtil.getCityInfo(realIp);
            } catch (Exception e) {
                loginRegion = "";
            }
        }
        log.info("用户:" + user.getUsername().replaceAll("[\r\n]", "") + "在" + DateUtil.getNow().replaceAll("[\r\n]", "") + "登录的ip是:" + realIp.replaceAll("[\r\n]", ""));
        if (StringUtil.isNotEmpty(loginRegion)) {
            Set<String> region = redisKit.smembers(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay");
            if (region.size() == 0) {
                redisKit.sadd(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", loginRegion);
                redisKit.expire(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", 60 * 60 * 24);
            } else if (!region.contains(loginRegion)) {
                redisKit.sadd(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay", loginRegion);
            }
            if ((region.size() >= 4) && (!region.contains(loginRegion))) {

                redisKit.setex(Constant.redisKeyPrefix_user_lock + user.getId(), "一天内5个不同地级地区登录", 60 * 60 * 2);
                redisKit.del(Constant.redisKeyPrefix + user.getId() + "_loginRegionInSingleDay");
                throw new BusinessException("一天内2次异地登录，账号已经被锁定，请两小时后再次尝试或联系管理员");
            }
        }

        if (user.getIsValid() == 0) {
            throw new BusinessException("用户无效，请联系管理员");
        }
        if (user.getIsNeverExpire() == 0) {

            String now = DateUtil.getNow();
            if (user.getPermitStartDate().compareTo(now) > 0) {
                throw new BusinessException(String.format("您的权限于%s开始有效，有疑问请联系管理员", user.getPermitStartDate()));
            }
            if (user.getPermitEndDate().compareTo(now) < 0) {
                throw new BusinessException(String.format("您的权限于%s过期，有疑问请联系管理员", user.getPermitEndDate()));
            }
        }
        //90天未登录回收账号启用，则回收账号
        if (StringUtil.isNotEmpty(lastLoginTime) && (isAccountDeprecatedActive == 1)) {
            long noLoginCount = DateUtil.getTimesDiff(1, lastLoginTime, DateUtil.getNow());
            if (noLoginCount >= 90) {
                Map<String, Object> map = new HashMap<>(4);
                // 账号状态失效，登录日期改为当前
                map.put("isValid", 0);
                map.put("userId", user.getId());
                userMapper.deValidUser(map);
                map.put("lastLoginTime", DateUtil.getNow());
                personalCenterMapper.updateUserSetting(map);
                throw new BusinessException("该账户90天未登录，账户已回收");
            }
        }
        //初始密码已经修改过,强制改密码启用，90天未改密码则提示修改密码
        if (loginType != 0) {
            long timeCount = 0L;
            if (StringUtil.isNotEmpty(lastChangePasswordTime) && (isForceChangePasswordActive == 1)) {
                timeCount = DateUtil.getTimesDiff(1, lastChangePasswordTime, DateUtil.getNow());
                if (timeCount >= 90) {
                    loginType = 1;
                }
            }
        }

        long timeout = NumberUtils.toLong(userPersonalSetting.getKeepOnLineTime(), 60);
        Map<String, Object> resultMap = new HashMap<>();

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIsAdmin(user.getIsAllPermissions());
        tokenInfo.setRoleId(user.getDefRoleId());
        tokenInfo.setCreateTime(System.currentTimeMillis());
        tokenInfo.setTimeout(timeout * 1000 * 60);

        String token = JwtUtil.createJWT(1000 * 60 * 60 * 24 * 10, tokenInfo, jwtSecret);
        resultMap.put("token", token);
        resultMap.put("loginType", loginType);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("userId", user.getId());
        updateMap.put("lastLoginTime", DateUtil.getNow());

        personalCenterMapper.updateUserSetting(updateMap);

        ServletUtil.getResponse().addHeader("Authorization", "Bearer " + token);
        return resultMap;
    }
}
