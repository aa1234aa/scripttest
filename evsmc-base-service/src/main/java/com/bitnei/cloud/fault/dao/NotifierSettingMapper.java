package com.bitnei.cloud.fault.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.fault.domain.NotifierSetting;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierSettingMapper接口<br>
* 描述： NotifierSettingMapper接口，在xml中引用<br>
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
* <td>2019-03-06 11:31:31</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Mapper
public interface NotifierSettingMapper {


    /**
     * 通过ID查询
     *
     * @param params 查询参数
     * @return
     */
    NotifierSetting findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj 新增对象
     * @return
     */
    int insert(NotifierSetting obj);

    /**
     * 更新
     * @param obj 更新对象
     * @return
     */
    int update(NotifierSetting obj);

	/**
     * 删除
     * @param params 查询参数
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params 查询参数
     * @return
    */
    List<NotifierSetting> pagerModel(Map<String,Object> params);

    NotifierSetting getNotifierByUserIdAndVehicleId(Map<String,Object> params);

    /**
     * 统计数量（查询表中符合条件的未删除记录数量，可用于查重和统计）
     * @param params 查询参数
     * @return
     */
    int count(Map<String, Object> params);

    List<NotifierSetting> notifiers(Map<String,Object> params);

    /**
     * 查询车辆报警所需推送负责人列表
     * @param params 查询参数
     * @return
     */
    List<String> findForAlarm(Map<String,Object> params);

    /**
     * 查询车辆安全风险通知报警所需推送负责人用户列表
     * @param params 查询参数
     * @return user集合
     */
    List<String> findForRisk(Map<String,Object> params);

}
