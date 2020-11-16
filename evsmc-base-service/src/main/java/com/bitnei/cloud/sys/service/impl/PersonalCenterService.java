package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.OwnerPeopleMapper;
import com.bitnei.cloud.sys.dao.PersonalCenterMapper;
import com.bitnei.cloud.sys.dao.UploadFileMapper;
import com.bitnei.cloud.sys.dao.UserMapper;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.HiddenColumnModel;
import com.bitnei.cloud.sys.model.PersonalCenterInfoModel;
import com.bitnei.cloud.sys.model.PersonalCenterInfoQueryModel;
import com.bitnei.cloud.sys.service.IPersonalCenterService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class PersonalCenterService extends BaseService implements IPersonalCenterService {

    @Value("${app.security.rsa.privateKey}")
    private String rsaPriKey;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private OwnerPeopleMapper ownerPeopleMapper;
    @Resource
    private UploadFileMapper uploadFileMapper;
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.sendto}")
    private String emailTo;

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;
    @Value("${app.security.token.timeout:86400}")
    private long tokenTimeout;
    @Resource
    private RedisKit webRedisKit;

    /**
     * 增加用户的通用报警设置
     *
     * @param map 参数map
     */
    private void addUserAlarmSetting(Map<String, Object> map) {
        personalCenterMapper.addAlarmSetting(map);
    }


    @Override
    public UserPersonalSetting getUserSetting(String userId) {

        UserPersonalSetting userPersonalSetting = personalCenterMapper.getUserSetting(userId);
        if (userPersonalSetting == null) {
            userPersonalSetting = new UserPersonalSetting();
            //Map<String,Object> newSetting = new HashMap<>();
            userPersonalSetting.setId(UtilHelper.getUUID());
            userPersonalSetting.setUserId(userId);
            userPersonalSetting.setKeepOnLineTime("60");
            userPersonalSetting.setLoginType(0);
            userPersonalSetting.setLastLoginTime(DateUtil.getNow());
            userPersonalSetting.setIsAccountDeprecatedActive(0);
            userPersonalSetting.setIsForceChangePasswordActive(0);
            userPersonalSetting.setIsRepeatLoginAllow(0);

            Map<String, Object> newSetting = new HashMap<>();
            newSetting = MapperUtil.Object2Map(userPersonalSetting);
            addUserSetting(newSetting);

            return userPersonalSetting;
        }


        return userPersonalSetting;
    }

    public void addUserSetting(Map<String, Object> map) {

        personalCenterMapper.addUserSetting(map);
    }


    @Override
    public List<Map<String, Object>> alarmRingList() {
        List<Map<String, Object>> ringList = personalCenterMapper.alarmRingList();

        return ringList;

    }

    @Override
    public Map<String, Object> getUserAlarmRing() {

//        String userId = "1";
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        Map<String, Object> map = personalCenterMapper.getUserAlarmRingSet(userId);
        if (map == null) {
            Map<String, Object> addMap = new HashMap<>();
            addMap.put("userId", userId);
            addMap.put("id", UtilHelper.getUUID());
            addMap.put("isAllowRing", 1);
            addMap.put("faultRingId", 1);
            addMap.put("isAllowDialog", 1);
            addMap.put("isAllowSms", 1);
            addUserAlarmSetting(addMap);

            Map<String, Object> defMap = new HashMap<>(4);
            defMap.put("isAllowRing", 1);
            defMap.put("isAllowDialog", 1);
            defMap.put("isAllowSms", 1);
            defMap.put("ringId", "1");
            defMap.put("ringFile", "1.mp3");
            return defMap;
        } else {
            if(!map.containsKey("isAllowRing")) {
                map.put("isAllowRing", 0);
            }
            if(!map.containsKey("isAllowDialog")) {
                map.put("isAllowDialog", 0);
            }
            if(!map.containsKey("isAllowSms")) {
                map.put("isAllowSms", 0);
            }
            return map;
        }

    }

    @Override
    public void setRingAllowed(Map<String, Object> params) {
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
//        String userId = "1";
        params.put("userId", userId);
        personalCenterMapper.updateUserAlarmRing(params);

    }

    @Override
    public void setUerAlarmRing(String ringId) {
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
//        String userId = "1";
        Map<String, Object> map = new HashMap<>(4);
        map.put("faultRingId", ringId);
        map.put("userId", userId);
        personalCenterMapper.updateUserAlarmRing(map);
    }

    @Override
    public Map<String, Object> getKeepConnTime() {

        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        // String userId = "eb9f8014b7784ecdafa7c7adf34630a5";
        Map<String, Object> time = personalCenterMapper.getUserKeepConnTime(userId);
        if (time == null) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("userId", userId);
            map.put("id", UtilHelper.getUUID());
            map.put("keepOnLineTime", "10");
            personalCenterMapper.setUserKeepOnLineTime(map);
            //默认登录保持10min
            Map<String, Object> reMap = new HashMap<>(2);
            reMap.put("keepOnLineTime", "10");
            reMap.put("keepOnLineTimeDisplay", "10分钟");
            return reMap;
        }

        int timeLength = Integer.parseInt(time.get("keepOnLineTime").toString());
        if (timeLength >= 1440) {
            time.put("keepOnLineTimeDisplay", "一直保持");
        } else if (timeLength >= 60) {
            time.put("keepOnLineTimeDisplay", timeLength / 60 + "小时");

        }

        return time;

    }

    @Override
    public void updateKeepConnTime(String time) {
        // String userId = "eb9f8014b7784ecdafa7c7adf34630a5";

        String userId = ServletUtil.getCurrentTokenInfo().getUserId();

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("keepOnLineTime", time);
        personalCenterMapper.updateUserSetting(param);

        Map<String, Object> map = new HashMap<>(6);
        //  String userId = "eb9f8014b7784ecdafa7c7adf34630a5";
        map.put("id", userId);
        User user = userMapper.personalCenterGetUser(userId);

        long timeout = NumberUtils.toLong(time, 60);

        String roleId = ServletUtil.getCurrentTokenInfo().getRoleId();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIsAdmin(user.getIsAllPermissions());
        tokenInfo.setRoleId(roleId);
        tokenInfo.setCreateTime(System.currentTimeMillis());
        tokenInfo.setTimeout(timeout * 1000 * 60);

        String token = JwtUtil.createJWT(1000 * 60 * 60 * 24 * 10, tokenInfo, jwtSecret);
        ServletUtil.getResponse().addHeader("Authorization", "Bearer " + token);
        CookieUtils.updateCookie("R_SESS", token);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {


        String decryptOldPassword = "";
        String decryNewPassword = "";
        String decryConfirmPassword = "";
        try {
            decryptOldPassword = RSAUtil.decryptByPrivateKey(oldPassword, rsaPriKey);
            decryNewPassword = RSAUtil.decryptByPrivateKey(newPassword, rsaPriKey);
            decryConfirmPassword = RSAUtil.decryptByPrivateKey(confirmPassword, rsaPriKey);
        } catch (Exception e) {
            throw new BusinessException("密码解密失败，请检查加密程序是否正常", -2);
        }

        if (!decryNewPassword.equals(decryConfirmPassword)) {
            throw new BusinessException("新密码和确认密码不一致");
        }


        String pattern = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\\\W~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]+$)(?![a-z0-9]+$)(?![a-z\\\\W~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]+$)(?![0-9\\\\~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]+$)[a-zA-Z0-9\\\\W~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]{6,64}$";
        if (!decryNewPassword.matches(pattern)) {
            throw new BusinessException("新密码应符合强密码要求，大小写字母，数字，特殊字符，四种至少有三种，且长度为6-64");

        }
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();

        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        User user = userMapper.personalCenterGetUser(userId);

        if (!SecurityUtil.getMd5(decryptOldPassword).equals(user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }


        map.put("password", SecurityUtil.getMd5(decryNewPassword));
        map.put("userId", user.getId());
        userMapper.personalCenterModifyPsw(map);
        map.put("loginType", 8);
        map.put("lastChangePasswordTime", DateUtil.getNow());
        personalCenterMapper.updateUserSetting(map);

    }

    @Override
    public List<Map<String, String>> getUserRoleList() {


        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        List<Map<String, String>> roleList = personalCenterMapper.getUserRoleList(userId);
        if (roleList == null) {
            throw new BusinessException("该用户未设置角色");
        }

        if (roleList.isEmpty() || roleList.size() < 1) {
            throw new BusinessException("该用户未设置角色");
        }

        return roleList;
    }

    @Override
    public void changRole(int isDefaultRole, String roleId, String password) {

        String userId = ServletUtil.getCurrentTokenInfo().getUserId();

        Map<String, Object> map = new HashMap<>(6);

        String decryptPassword = "";
        try {
            decryptPassword = RSAUtil.decryptByPrivateKey(password, rsaPriKey);
        } catch (Exception e) {
            throw new BusinessException("密码解密失败，请检查加密程序是否正常", -2);
        }
        map.put("id", userId);
        User user = userMapper.personalCenterGetUser(userId);
        if (!SecurityUtil.getMd5(decryptPassword).equals(user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        if (isDefaultRole == 1) {
            //切换角色并设置为用户的默认登录角色，则修改用户表
            map.put("userId", user.getId());
            map.put("defaultRoleId", roleId);
            userMapper.updateUserRole(map);
        }

        UserPersonalSetting userPersonalSetting = getUserSetting(user.getId());

        long timeout = NumberUtils.toLong(userPersonalSetting.getKeepOnLineTime(), 60);

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setUsername(user.getUsername());
        tokenInfo.setIsAdmin(user.getIsAllPermissions());
        tokenInfo.setRoleId(roleId);
        tokenInfo.setCreateTime(System.currentTimeMillis());
        tokenInfo.setTimeout(timeout * 1000 * 60);

        String token = JwtUtil.createJWT(1000 * 60 * 60 * 24 * 10, tokenInfo, jwtSecret);
        ServletUtil.getResponse().addHeader("Authorization", "Bearer " + token);
        CookieUtils.updateCookie("R_SESS", token);

    }

    @Override
    public Map<String, Object> userInfo() {
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        String roleId = ServletUtil.getCurrentTokenInfo().getRoleId();

        PersonalCenterInfo personalCenterInfo = personalCenterMapper.personalInformation(userId, roleId);
        if (personalCenterInfo == null) {
            throw new BusinessException("查询单位联系人信息失败");
        }

        if (personalCenterInfo.getIsNeverExpire() == 1) {
            personalCenterInfo.setLastValidDate("长期有效");
        }
        PersonalCenterInfoQueryModel p = PersonalCenterInfoQueryModel.fromEntry(personalCenterInfo);
        DataLoader.loadNames(p);
        Map<String, Object> map = MapperUtil.Object2Map(p);
        return map;

    }

    @Override
    public void updateInfo(PersonalCenterInfoModel personalCenterInfoModel) {

        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        if (personalCenterInfoModel.getCardType() != null && StringUtil.isNotEmpty(personalCenterInfoModel.getCardNo())) {
            int cardType = personalCenterInfoModel.getCardType();
            String cardNo = personalCenterInfoModel.getCardNo();
            if (cardType == 1 || cardType == 4) {
                if (!cardNo.matches("^[xX0-9]{18,18}$")) {
                    throw new BusinessException("身份证或驾驶证为18位数字或字母");
                }
            } else if (cardType == 2 || cardType == 3 || cardType == 5 || cardType == 6) {
                if (!cardNo.matches("^[a-zA-Z0-9]{3,30}$")) {
                    throw new BusinessException("证件格式不正确");
                }
            }

            if (StringUtil.isNotEmpty(cardNo)) {
                Set<String> users = ownerPeopleMapper.checkCardNo(cardNo);
                if (users.size() > 0) {
                    if (!users.contains(userId)) {
                        throw new BusinessException("证件号码不能重复");
                    }
                }

            }

        }
        OwnerPeople ownerPeople = ownerPeopleMapper.findOwnerByUserId(userId);
        String ownerId = ownerPeople.getId();


        Map<String, Object> map = new HashMap<>(36);
        map.put("userId", userId);
        map.put("ownerId", ownerId);
        map.putAll(MapperUtil.Object2Map(personalCenterInfoModel));
        map.put("updateBy", ServletUtil.getCurrentUser());
        map.put("updateTime", DateUtil.getNow());
        ownerPeopleMapper.personalCenterUpdateInfo(map);

    }

    @Override
    public void userSuggestionSubmit(String targetEmail, String detail, String attachmentId) {

        if ((detail.length() < 1) && (attachmentId.length() < 1)) {
            throw new BusinessException("请输入意见内容");
        }
        //String userId = "eb9f8014b7784ecdafa7c7adf34630a5";
        String userId = ServletUtil.getCurrentTokenInfo().getUserId();
        String roleId = ServletUtil.getCurrentTokenInfo().getRoleId();
        PersonalCenterInfo personalCenterInfo = personalCenterMapper.personalInformation(userId, roleId);

        if (personalCenterInfo == null) {
            throw new BusinessException("查询用户信息失败");
        }
        String mailTitle = "来自" + personalCenterInfo.getUnitName() + personalCenterInfo.getJobPost() + personalCenterInfo.getOwnerName() + "的意见反馈";
        String[] fileArray = attachmentId.split(",");
        Map<String, Object> map = new HashMap<>(2);
        map.put("imageIds", fileArray);

        List<UploadFile> imageList = uploadFileMapper.getImage(map);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(mailTitle);
            helper.setText(detail);

            if (imageList.size() > 0) {
                for (UploadFile image : imageList) {
                    InputStream inputStream = new ByteArrayInputStream(image.getContent());
                    helper.addAttachment(image.getName(), new ByteArrayResource(IOUtils.toByteArray(inputStream)));
                }
            }
            javaMailSender.send(message);
            log.info("意见反馈邮件发送成功");
            uploadFileMapper.deleteMailAttachment(map);
        } catch (Exception e) {
            log.error("error", e);
            log.info("意见反馈发送邮件失败");
            uploadFileMapper.deleteMailAttachment(map);
        }

    }

    /**
     * 保存隐藏列
     *
     * @param hiddenColumnModel
     */
    @Override
    public void saveHiddenColumn(HiddenColumnModel hiddenColumnModel) {

        webRedisKit.hset(5, "hidden.columns.user." + ServletUtil.getCurrentUser(), hiddenColumnModel.getTableId(), new JsonSerializer().deep(true).serialize(hiddenColumnModel.getColumns()));
    }


    /**
     * 获取当前登录用户的所有隐藏列配置
     *
     * @return
     */
    @Override
    public Map<String, String> getAllHiddenConfig() {


        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> config = webRedisKit.hgetAll(5, "hidden.columns.user." + ServletUtil.getCurrentUser());
        if (config != null) {

            for (Map.Entry<String, String> entry : config.entrySet()) {
                if (StringUtil.isNotEmpty(entry.getValue())) {
                    List<String> list = new JsonParser().parse(entry.getValue(), List.class);
                    if (list != null) {
                        resultMap.put(entry.getKey(), StringUtils.join(list, ","));
                    }

                }

            }
        }

        return resultMap;
    }

}
