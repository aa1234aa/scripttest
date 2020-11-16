package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.model.AlarmInfoModel;
import com.bitnei.cloud.fault.model.AlarmMakerModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AlarmInfoService接口<br>
* 描述： AlarmInfoService接口，在xml中引用<br>
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
* <td>2019-03-01 16:23:08</td>
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

public interface IAlarmInfoService extends IBaseService {

    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     AlarmInfoModel get(String id);

    /**
     * 新增
     * @param alarmInfo  新增model
     * @return
     */
    AlarmInfo insert(AlarmInfo alarmInfo);


    /**
     * 通过车辆的vid和电子围栏规则id 结束报警
     * @param vehilceId
     * @param ruleId
     */
    void stopByFenceAlarm(String vehilceId,String ruleId);
    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 处理报警更新
     * @param model  编辑model
     * @return
     */
    void update(AlarmInfoModel model);

    /**
     * 更新报警信息: 故障名称, 报警等级, 响应方式
     * @param updateMap
     */
    void updateAlarm(Map<String, Object> updateMap);

    /**
     * 批量更新报警处理状态
     * @param ids 报警ID集合
     * @param procesStatus 处理状态
     */
    void updateProcesStatus(String[]  ids, Integer procesStatus);

    /**
     * 更新报警等级
     * @param id 报警ID
     * @param alarmLevel 报警等级
     */
    void updateAlarmLevel(String id, int alarmLevel);

    /**
     * 通过消息ID结束报警
     * @param kafkaMsgId kafka消息ID
     */
    void stopAlarmByMsgId(String kafkaMsgId,String faultEndTime);

    /**
     *  通过故障规则ID结束报警
     * @param ruleId 规则ID
     * @param msg 结束原因
     */
    void stopAlarmByRuleId(String ruleId, String msg);

    /**
     * 通过故障规则父ID结束报警
     * @param parentRuleId 故障规则父ID
     * @param msg 结束原因
     */
    void stopAlarmByParentRuleId(String parentRuleId, String msg);

    /**
     * 结束车辆报警
     * @param vehicleId 车辆ID
     * @param msg 结束原因
     */
    void stopAlarmByVehicleId(String vehicleId, String msg);

    /**
     * 结束车型故障码报警
     * @param vehModelId 车型ID
     * @param parentRuleId 故障规则父ID
     * @param msg 结束原因
     */
    void stopCodeAlarmByVehModelId(String vehModelId, String parentRuleId, String msg);

    /**
     * 结束车型参数报警
     * @param vehModelId 车型ID
     * @param ruleId 故障规则ID
     * @param msg 结束原因
     */
    void stopParamAlarmByVehModelId(String vehModelId, String ruleId, String msg);

    /**
     * 统计数据
     * @return
     */
    Map<String, Integer> statistic();

    /**
     * 推送故障信息
     * @return
     */
    void pushAlarmInfo();

    /**
     * 推送报警信息
     * @param alarmInfo
     */
    void sendFaultAlarmInfo(AlarmInfo alarmInfo);

    /**
     * 根据报警的id获取点击地图的信息
     * @param id
     * @return
     */
    AlarmMakerModel getAlarmMakerInfo(String id);

    /**
     * 通过msgId查询
     *
     * @param msgId
     * @return
     */
    AlarmInfo findByMsgId(String msgId);

    /**
     * 查询已结束报警数据集合
     *
     * @param params
     * @return
     */
    List<AlarmInfoHistory> findStopAlarm(Map<String, Object> params);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<String> ids);
}
