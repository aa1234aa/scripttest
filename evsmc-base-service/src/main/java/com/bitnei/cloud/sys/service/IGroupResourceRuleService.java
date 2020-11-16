package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.GroupResourceRule;
import com.bitnei.cloud.sys.domain.GroupRuleInfo;
import com.bitnei.cloud.sys.model.GroupListAddModel;
import com.bitnei.cloud.sys.model.GroupListAddModelResp;
import com.bitnei.cloud.sys.model.GroupResourceRuleModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.GroupRuleListModel;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupResourceRuleService接口<br>
* 描述： GroupResourceRuleService接口，在xml中引用<br>
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
* <td>2019-01-22 16:30:51</td>
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

public interface IGroupResourceRuleService extends IBaseService {


    /**
     * 选择资源
     * @return
     */
    ResultListMsg<Map<String,Object>> selectResources(String groupId, String resourceItemId, PagerInfo pagerInfo,  boolean hasFilter);


    /**
     * 选择对象自身的资源
     * @param pagerInfo
     * @return
     */
    ResultListMsg<Map<String, Object>> selectSelfResources(String groupId, String resourceId, PagerInfo pagerInfo);

    /**
     * 保存规则
     * @param listModel
     */
    void saveRules(GroupRuleListModel listModel);

    /**
     * 获取
     * @param groupId
     * @param resourceTypeId
     * @return
     */
    List<GroupResourceRuleModel> getRules(String groupId, String resourceTypeId);


    /**
     * 获取已选资源（列表模式）
     * @param pagerInfo
     * @return
     */
    ResultListMsg<Map<String, Object>> selectListResource(String groupId, String resourceTypeId, PagerInfo pagerInfo);


    /**
     * 列表模式下的新增
     * @param addModel
     * @return
     */
    GroupListAddModelResp listAdd(GroupListAddModel addModel);

    /**
     * 列表模式下的移除指定ids
     * @param groupId           权限组ID
     * @param resourceTypeId    资源类型ID
     * @param id                对象ID列表，用逗号分隔
     * @return
     */
    int listModeDelete(String groupId, String resourceTypeId, String id);

    /**
     * 列表模式下的移除，根据查询条件
     * @param groupId
     * @param resourceTypeId
     * @param pagerInfo
     * @return
     */
    int listModeDeleteByPager(String groupId, String resourceTypeId, PagerInfo pagerInfo);


    /**
     * 资源属性的资源查询
     * @param groupId
     * @param resourceItemId
     * @param pagerInfo
     * @return
     */
    ResultMsg resources(String groupId, String resourceItemId, PagerInfo pagerInfo);

    /**
     * 查找规则，通过用户id和表名
     * @param userId
     * @param tableName
     * @return
     */
    List<GroupRuleInfo> findRuleByUserIdAndTable(String userId, String tableName);

    /**
     * 查找规则，通过组和属性类型id
     * @param groupId
     * @param resourceId
     * @return
     */
    List<GroupRuleInfo> findRuleByGroupIdAndResourceId(String groupId, String resourceId);


    /**
     * 获取数据权限组已选车辆
     * @param groupId
     * @param pagerInfo
     * @return
     */
    PagerResult groupCheckedVehicles(String groupId, String resourceTypeId, PagerInfo pagerInfo);


    /**
     * 获取数据权限组未选车辆
     * @param groupId
     * @param pagerInfo
     * @return
     */
    Object groupUnCheckedVehicles(String groupId, String resourceTypeId, PagerInfo pagerInfo);
}
