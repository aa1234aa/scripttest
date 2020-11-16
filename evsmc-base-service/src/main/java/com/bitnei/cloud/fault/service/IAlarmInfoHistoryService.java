package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.common.handler.IServiceBase;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.model.AlarmInfoHistoryModel;
import com.bitnei.cloud.fault.model.AlarmProcessModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmInfoHistoryService接口<br>
 * 描述： AlarmInfoHistoryService接口，在xml中引用<br>
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
 * <td>2019-03-04 20:02:06</td>
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
public interface IAlarmInfoHistoryService extends IServiceBase {


    /**
     * 全部查询
     *
     * @return 返回所有
     */
    PagerResult list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @param id             ID
     * @param faultBeginTime 分表原因,需要附带报警开始时间
     * @return
     */
    AlarmInfoHistoryModel get(String id, String faultBeginTime);

    /**
     * 获取大数据历史报警处理记录
     * @param id 报警ID
     * @return
     */
    List<AlarmProcessModel> getAlarmProcess(String id);

    /**
     * 根据分表名查询所有历史报警数据
     *
     * @param tableName 分表名
     * @return
     */
    List<AlarmInfoHistory> findAllByTableName(String tableName);

    List<AlarmInfoHistory> findPageByTableName(String tableName, String faultBeginTime, int limit,  String faultId);

    /**
     * 新增
     *
     * @param alarmInfoHistoryList 新增集合
     * @return
     */
    boolean insert(List<AlarmInfoHistory> alarmInfoHistoryList);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 离线导出
     *
     * @param pagerInfo 查询参数
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);

    /**
     * 批量处理
     *
     * @param model 处理记录
     * @return
     */
    boolean alarmProcess(AlarmProcessModel model);

    /**
     * 根据分表名删除数据(用于迁移至大数据)
     *
     * @param tableName
     */
    void deleteByTable(String tableName);

    /**
     * 批量删除
     *
     * @param tableName            分表名
     * @param alarmInfoHistoryList 待删除集合
     */
    void deleteBatch(String tableName, List<AlarmInfoHistory> alarmInfoHistoryList);
}
