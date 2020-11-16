package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.GroupRuleInfo;
import com.bitnei.cloud.sys.domain.UserBlockResource;
import com.bitnei.cloud.sys.model.VehicleForGroupModel;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.GroupResourceRule;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupResourceRuleMapper接口<br>
* 描述： GroupResourceRuleMapper接口，在xml中引用<br>
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
* <td>2019-03-07 09:15:35</td>
* <td>hzr</td>
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
public interface GroupResourceRuleMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    GroupResourceRule findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(GroupResourceRule obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(GroupResourceRule obj);

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
    List<GroupResourceRule> pagerModel(Map<String,Object> params);


    /**
     * 通过组id和资源类型id删除
     * @param params
     * @return
     */
    int deleteByGroupAndResource(Map<String,Object> params);


    /**
     * 批量插入规则
     * @param rules
     * @return
     */
    int saveRules(List<GroupResourceRule> rules);


    /**
     * 通过资源属性id和组去查询已选资源
     * @param params
     * @return
     */
    GroupResourceRule findByResourceItemIdAndGroupId(Map<String,Object> params);


    /**
     * 查找规则，通过用户id和表名
     * @param params
     * @return
     */
    List<GroupRuleInfo> findRuleByUserIdAndTable(Map<String,Object> params);

    /**
     * 查找规则，通过组和资源属性id
     * @param params
     * @return
     */
    List<GroupRuleInfo> findRuleByGroupIdAndResourceId(Map<String, Object> params);


    /**
     * 获取数据权限组已选车辆
     * @param params
     * @return
     */
    List<VehicleForGroupModel> groupVehicleChecked(Map<String,Object> params);

    /**
     * 根据用户获取
     * @param userId
     * @param resourceItemId
     * @return
     */
    String getGroupRuleValByUser(@Param("userId") String userId, @Param("resourceItemId") String resourceItemId);


}
