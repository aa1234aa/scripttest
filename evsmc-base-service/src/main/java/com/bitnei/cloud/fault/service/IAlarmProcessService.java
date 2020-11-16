package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.model.AlarmProcessModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmProcessService接口<br>
 * 描述： AlarmProcessService接口，在xml中引用<br>
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
 * <td>2019-03-04 17:13:13</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */

public interface IAlarmProcessService extends IBaseService {

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(AlarmProcessModel model);

    /**
     * 推送再次提醒报警信息
     */
    void pushAlarmInfo();

    /**
     * 根据分表名查询全部处理记录
     *
     * @param tableName 分表名
     * @return
     */
    Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> findAllByTableName(String tableName);

    Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> findByTableNameAndFaultId(
            String tableName, List<String> faultAlarmIds);

    /**
     * 批量删除
     *
     * @param tableName            分表名
     * @param alarmInfoHistoryList 待删除集合
     */
    void deleteBatch(String tableName, List<AlarmInfoHistory> alarmInfoHistoryList);

    /**
     * 删除报警处理记录
     *
     * @param faultBeginTime 故障开始时间
     * @param faultAlarmId   报警ID
     */
    void deleteByFaultAlarmId(String faultBeginTime, String faultAlarmId);

}
