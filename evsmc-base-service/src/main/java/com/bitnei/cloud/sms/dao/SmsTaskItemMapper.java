package com.bitnei.cloud.sms.dao;

import com.bitnei.cloud.sms.domain.SmsTaskItemDetail;
import com.bitnei.cloud.sms.domain.VehicleMsisd;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sms.domain.SmsTaskItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SmsTaskItemMapper接口<br>
* 描述： SmsTaskItemMapper接口，在xml中引用<br>
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
* <td>2019-08-17 15:45:24</td>
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
public interface SmsTaskItemMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    SmsTaskItem findById(Map<String, Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(SmsTaskItem obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(SmsTaskItem obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     * @param params
     * @return
    */

    List<SmsTaskItem> pagerModel(Map<String, Object> params);

    List<SmsTaskItem> listSmsTaskItemByTaskId(Map<String, Object> params);

    /**
     * 根据任务删除
     * @param taskId
     * @return
     */
    void deleteByTaskId(String taskId);

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    void deleteByIds(List<String> ids);

    /**
     * 车辆msisd
     * @param ids
     * @return
     */
    List<VehicleMsisd> listVehicleMsisd(@Param("ids") String[] ids);

    /**
     * 保存短信发送流水号
     * @param params
     */
    void updateBizId(Map<String, Object> params);


    /**
     * 保存短信发送状态、时间
     * @param params
     */
    void updateSendTimeAndStatus(Map<String, Object> params);

    SmsTaskItemDetail getSmsTaskItemDetail(@Param("id") String id);

    /***
     *  发送成功统计
     * @param taskId
     * @return
     */
    Map<String, BigDecimal> successTotal(@Param("taskId") String taskId);

    Integer totalByTaskId(@Param("taskId") String taskId);

    /**
     * 查询发送短信的车辆
     * @param params
     */
    List<SmsTaskItem> findSmsVehicle(Map<String, Object> params);


    /**
     * 发送信息后,保存bizid
     * @param params
     */
    void updateUniComBizId(Map<String, Object> params);

}
