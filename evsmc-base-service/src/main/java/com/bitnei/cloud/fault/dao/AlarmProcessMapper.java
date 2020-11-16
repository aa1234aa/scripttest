package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.domain.AlarmProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmProcessMapper接口<br>
 * 描述： AlarmProcessMapper接口，在xml中引用<br>
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
@Mapper
public interface AlarmProcessMapper {

    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(AlarmProcess obj);

    /**
     * 查询
     *
     * @param params
     * @return
     */
    List<AlarmProcess> pagerModel(Map<String, Object> params);

    /**
     * 获取再次提醒时间范围内的报警ID集合
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<String> findFaultAlarmId(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 根据分表名查询全部处理记录
     *
     * @param tableName 分表名
     * @return
     */
    List<AlarmProcess> findAllByTableName(@Param("tableName") String tableName);

    /**
     * 根据分表名和故障id查询全部处理记录
     * @param params
     * @return
     */
    List<AlarmProcess> findByTableNameAndFaultId(Map<String, Object> params);

    /**
     * 批量删除
     *
     * @param tableName            分表名
     * @param alarmInfoHistoryList 待删除集合
     */
    void deleteBatch(@Param("tableName") String tableName, @Param("alarmInfoHistoryList") List<AlarmInfoHistory> alarmInfoHistoryList);

    /**
     * 删除报警处理记录
     *
     * @param faultBeginTime 故障开始时间
     * @param faultAlarmId   报警ID
     */
    void deleteByFaultAlarmId(@Param("faultBeginTime") String faultBeginTime, @Param("faultAlarmId") String faultAlarmId);

}