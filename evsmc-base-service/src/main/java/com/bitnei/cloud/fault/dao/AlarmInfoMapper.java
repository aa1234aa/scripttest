package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmInfoMapper接口<br>
 * 描述： AlarmInfoMapper接口，在xml中引用<br>
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
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface AlarmInfoMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    AlarmInfo findById(Map<String, Object> params);


    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(AlarmInfo obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    int update(AlarmInfo obj);

    /**
     * 更新报警信息: 故障名称, 报警等级, 响应方式
     *
     * @param params
     * @return
     */
    int updateAlarm(Map<String, Object> params);

    /**
     * 结束
     **/
    int updateByMsgId(Map<String, Object> params);

    /**
     * 批量结束
     **/
    int updateByMsgIds(Map<String, Object> params);

    /**
     * 批量更新处理状态
     *
     * @param params
     * @return
     */
    int updateProcesStatus(Map<String, Object> params);

    /**
     * 更新报警等级
     * @param params
     * @return
     */
    int updateAlarmLevel(Map<String, Object> params);

    /**
     * 删除
     *
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     *
     * @param params
     * @return
     */
    List<AlarmInfo> pagerModel(Map<String, Object> params);

    /**
     * 通过msgId查询
     *
     * @param msgId
     * @return
     */
    AlarmInfo findByMsgId(String msgId);

    /**
     * 统计处理的数量
     *
     * @param params
     * @return
     */
    Map<String, BigDecimal> processTotal(Map<String, Object> params);

    /**
     * 统计故障类型数
     *
     * @param params
     * @return
     */
    Map<String, BigDecimal> faultTypeTotal(Map<String, Object> params);

    /**
     * 未推送的信息
     *
     * @return
     */
    List<AlarmInfo> list();

    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(String id);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 查询已结束报警数据集合
     *
     * @param params
     * @return
     */
    List<AlarmInfoHistory> findStopAlarm(Map<String, Object> params);

}
