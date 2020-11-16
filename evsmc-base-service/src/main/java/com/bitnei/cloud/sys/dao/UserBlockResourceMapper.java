package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.UserBlockResourceVehicle;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.UserBlockResource;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;
import java.util.Set;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserBlockResourceMapper接口<br>
* 描述： UserBlockResourceMapper接口，在xml中引用<br>
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
* <td>2019-07-04 14:22:54</td>
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
public interface UserBlockResourceMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    UserBlockResource findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(UserBlockResource obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(UserBlockResource obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<UserBlockResource> pagerModel(Map<String,Object> params);

    /**
     * 查询车辆
     * @param params
     * @return
     */
    List<UserBlockResourceVehicle> pagerModelVehicle(Map<String,Object> params);

    /**
     * 批量插入
     * @param userBlockResources
     */
    void insertList(List<UserBlockResource> userBlockResources);

    /**
     * 获取锁定的id
     * @param userId
     * @return
     */
    Set<String> getBlockIds(@Param("userId") String userId);

    /**
     * 通过用户名去删除黑名单资源
     * @param userId
     */
    int deleteByUserId(@Param("userId") String userId);
}
