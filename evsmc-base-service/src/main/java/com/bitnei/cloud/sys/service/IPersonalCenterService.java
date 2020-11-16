package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.UserPersonalSetting;
import com.bitnei.cloud.sys.model.HiddenColumnModel;
import com.bitnei.cloud.sys.model.PersonalCenterInfoModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CoreResourceService接口<br>
 * 描述： CoreResourceService接口，在xml中引用<br>
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
 * <td>2019-03-15 09:32:22</td>
 * <td>chenhuafeng</td>
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

public interface IPersonalCenterService extends IBaseService {

    /**
     * 获取用户的登录相关设置
     * @param userId
     * @return
     */
    UserPersonalSetting getUserSetting(String userId);

    /**
     * 报警铃声列表
     */
    List<Map<String,Object>> alarmRingList();

    /**
     * 返回用户报警铃声设置
     * @param
     * @return
     */
    Map<String,Object> getUserAlarmRing();

    /**
     * 设置是否允许播放声音
     * @param params 参数
     */
    void setRingAllowed(Map<String,Object> params);

    /**
     * 用户设置故障报警播放铃声
     * @param ringId
     */
    void setUerAlarmRing(String ringId);

    /**
     * 获取用户登录保持时长
     * @return
     */
    Map<String,Object> getKeepConnTime();

    /**
     * 更新用户的保持在线时间
     * @param time
     */
    void updateKeepConnTime(String time);

    /**
     *  用户修改密码
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    void changePassword(String oldPassword,String newPassword,String confirmPassword);

    /**
     * 获取用户角色列表
     *
     * @return
     */
    List<Map<String,String>> getUserRoleList();

    /**
     * 修改用户的默认角色
     * @param isDefaultRole
     * @param roleId
     * @param password
     */
    void changRole(int isDefaultRole,String roleId,String password);


    /**
     * 个人中心用户自查个人信息
     * @return
     */
    Map<String,Object> userInfo();

    /**
     * 更新个人信息
     * @param personalCenterInfoModel
     */
    void updateInfo(PersonalCenterInfoModel personalCenterInfoModel);

    /**
     * 个人中心提交意见反馈
     * @param targetEmail
     * @param detail
     * @param attachmentId
     */
    void userSuggestionSubmit(String targetEmail,String detail,String attachmentId);

    /**
     * 保存隐藏列
     * @param hiddenColumnModel
     */
    void saveHiddenColumn(HiddenColumnModel hiddenColumnModel);


    /**
     * 获取当前登录用户的所有隐藏列配置
     * @return
     */
    Map<String, String> getAllHiddenConfig();
}
