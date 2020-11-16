package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.PersonalCenterInfo;
import com.bitnei.cloud.sys.domain.UserPersonalSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PersonalCenterMapper {

    /**
     * 增加用户的配置信息
     * @param map
     */
    void addUserSetting(Map<String,Object> map);

    /**
     * 更新用户的配置信息
     * @param map
     */
    void updateUserSetting(Map<String,Object> map);

    /**
     * 获取用户的配置信息
     * @param userId
     * @return
     */
    UserPersonalSetting getUserSetting(@Param("userId")String userId);

    /**
     * 保持用户的个人设置
     * @param map
     * @return
     */
    void addAlarmSetting(Map<String,Object> map);

    /**
     * 更新用户的个人设置
     * @param map
     * @return
     */

    int updateSetting(Map<String,Object> map);

    /**
     * 获取用户的个人设置
     * @param map
     * @return
     */

   Map<String,Object> getSetting(Map<String,Object> map);



    /**
     * 获取报警铃声列表信息
     * @return
     */
   List<Map<String,Object>> alarmRingList();

    /**
     *  获取用户的报警铃声设置
     * @param userId
     * @return
     */

   Map<String,Object> getUserAlarmRingSet(@Param("userId")String userId);

    /**
     * 更新用户设置的报警铃声
     * @param map
     */
   void updateUserAlarmRing(Map<String,Object> map);

    /**
     *  获取用户保持连接在线时长
     * @param userId
     * @return
     */
   Map<String,Object> getUserKeepConnTime(@Param("userId")String userId);

    /**
     * 增加用户的保持在线时长的设置
     * @param map
     */
   void setUserKeepOnLineTime(Map<String,Object> map);

    /**
     * 获取用户角色列表
     * @param userId
     * @return
     */
    List<Map<String,String>> getUserRoleList(@Param("userId")String userId);

    /**
     * 用户查看个人信息
     * @param userId
     * @param roleId
     * @return
     */
    PersonalCenterInfo personalInformation(@Param("userId")String userId,@Param("roleId")String roleId);



}
