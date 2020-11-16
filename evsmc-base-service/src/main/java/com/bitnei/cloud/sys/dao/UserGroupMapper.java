package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.UserGroupLk;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.CoreResource;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CoreResourceMapper接口<br>
 * 描述： CoreResourceMapper接口，在xml中引用<br>
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
 * <td>2018-12-26 15:10:31</td>
 * <td>ccc</td>
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
@Mapper
public interface UserGroupMapper {
    /**
     * 获取资源组用户列表
     * @param groupId
     * @return
     */
    List<String> getGroupUsers(@Param("groupId") String groupId);

    /**
     * 将列表的用户赋予资源组权限
     * @param map
     */
    void saveGroudUsers(Map<String,Object> map);

    /**
     * 移除列表里用户的资源权限
     * @param map
     */

    void removeGroupUsers(Map<String,Object> map);

    /**
     * 清空资源组已经分配的用户
     * @param groupId
     */
    void clearGroupUser(@Param("groupId")String groupId);

    /**
     * 保存用户组关联关系
     * @param lk
     * @return
     */
    int insert(UserGroupLk lk);

}
