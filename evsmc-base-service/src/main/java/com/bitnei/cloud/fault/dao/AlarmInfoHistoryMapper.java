package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmInfoHistoryMapper接口<br>
 * 描述： AlarmInfoHistoryMapper接口，在xml中引用<br>
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
@Mapper
public interface AlarmInfoHistoryMapper extends IMapper {

    /**
     * 查询故障历史的处理记录
     *
     * @param faultBeginTime      分片参数 - 年月
     * @param alarmInfoHistoryIds 故障报警历史 Id
     * @return 故障处理表
     */
    List<Map<String, String>> processRecords(
        @Param("faultBeginTime") @NotNull final String faultBeginTime,
        @Param("alarmInfoHistoryIds") @NotNull final List<String> alarmInfoHistoryIds);

    /**
     * 根据分表名查询所有历史报警数据
     * @param tableName 分表名
     * @return
     */
    List<AlarmInfoHistory> findAllByTableName(@Param("tableName") String tableName);

    /**
     * 根据分表名、故障开始时间和limit查询历史报警数据
     * @param params
     * @return
     */
    List<AlarmInfoHistory> findPageByTableName(Map<String, Object> params);

    /**
     * 根据分表名删除数据(用于迁移至大数据)
     * @param tableName 分表名
     */
    void deleteByTable(@Param("tableName") String tableName);

    /**
     * 批量删除
     * @param tableName 分表名
     * @param alarmInfoHistoryList 待删除集合
     */
    void deleteBatch(@Param("tableName") String tableName, @Param("alarmInfoHistoryList") List<AlarmInfoHistory> alarmInfoHistoryList);

}
