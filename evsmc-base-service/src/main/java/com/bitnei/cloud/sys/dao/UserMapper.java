package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.sys.domain.Role;
import com.bitnei.cloud.sys.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserMapper接口<br>
* 描述： UserMapper接口，在xml中引用<br>
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
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Mapper
public interface UserMapper {

    /**
     * 通过
     * @param params
     * @return
     */
    User findByUsername(Map<String,Object> params);

    /**
     * 通过用户ID查询用户
     * @param params
     * @return
     */
    User findById(Map<String,Object> params);

    /**
     * 用户个人中心修改密码
     * @param map
     */
    void personalCenterModifyPsw(Map<String,Object> map);

    /**
     * 用户个人中心切换角色
     * @param map
     */
    void updateUserRole(Map<String,Object> map);

    /**
     * 个人中心获取包含密码的用户信息
     * @param userId
     * @return
     */
    User personalCenterGetUser(@Param("userId")String userId);

    /**
     * 强制改密码配置启用的话，90天未登录成功，账号失效
     * @param map
     */
    void deValidUser(Map<String,Object> map);

    /**
     * 查询用户当前角色信息
     * @param roleId
     * @return
     */
    Map<String,Object> findUserRole(@Param("roleId")String roleId);


    /**
     * 获取用户的一个角色Id
     * @param userId
     * @return
     */
    String getRoleId(@Param("userId")String userId);

    /**
     * 设置用户默认角色ID
     * @param userId
     * @param roleId
     * @return
     */
    int updateDefRoleId(@Param("userId")String userId, @Param("roleId")String roleId);

    /**
     * app上传头像
     * @param map
     */
    void resetPhoto(Map<String,Object> map);

    /**
     * 通过 电话 查询
     * @param params
     * @return
     */
    User findByUserMobile(Map<String,Object> params);
}
