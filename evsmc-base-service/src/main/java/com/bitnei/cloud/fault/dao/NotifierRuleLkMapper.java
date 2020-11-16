package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.NotifierRuleLk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierRuleLkMapper接口<br>
 * 描述： NotifierRuleLkMapper接口，在xml中引用<br>
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
 * <td>2019-07-04 11:22:26</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author huangweimin
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface NotifierRuleLkMapper {

    /**
     * 批量插入
     *
     * @param notifierRuleLkList 规则ID集合
     * @return
     */
    int insertBatch(List<NotifierRuleLk> notifierRuleLkList);

    /**
     * 删除车辆负责人指定规则类型的关联信息
     *
     * @param notifierId 车辆负责人ID
     * @param ruleType   规则类型
     * @return
     */
    int delete(@Param("notifierId") String notifierId, @Param("ruleType") Integer ruleType);

    /**
     * 查询关联参数规则
     *
     * @param params
     * @return
     */
    List<NotifierRuleLk> findParameterRuleList(Map<String, Object> params);

    /**
     * 查询关联故障码规则
     *
     * @param params
     * @return
     */
    List<NotifierRuleLk> findCodeRuleList(Map<String, Object> params);

    /**
     * 查询关联围栏
     *
     * @param params
     * @return
     */
    List<NotifierRuleLk> findEnclosureRuleList(Map<String, Object> params);

    /**
     * 根据故障规则id删除
     * @param params
     * @return
     */
    int deleteByRuleId(Map<String, Object> params);

    /**
     * 根据旧的故障规则id更新成新的故障规则id
     * @param params
     * @return
     */
    int updateRuleIdByOld(Map<String, Object> params);
}
