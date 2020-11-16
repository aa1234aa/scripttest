package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.model.NotifierRuleLkModel;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierRuleLkService接口<br>
 * 描述： NotifierRuleLkService接口，在xml中引用<br>
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
public interface INotifierRuleLkService extends IBaseService {

    /**
     * 根据车辆负责人ID查询关联规则集合
     *
     * @param notifierId 负责人ID
     * @param ruleType   规则类型枚举
     * @return 返回关联故障码规则
     */
    List<NotifierRuleLkModel> list(String notifierId, FaultTypeEnum ruleType);

    /**
     * 保存车辆负责人关联规则
     * @param notifierId  负责人ID
     * @param ruleType  规则类型
     * @param ruleIdList  规则ID集合
     * @return
     */
    void save(String notifierId, Integer ruleType, List<String> ruleIdList);

    /**
     * 删除车辆负责人所有关联规则
     * @param notifierId 负责人ID
     */
    void deleteByNotifierId(String notifierId);

    /**
     * 根据故障规则id删除
     * @param ruleId
     */
    void deleteByRuleId(String ruleId);

    /**
     * 根据旧的故障规则id更新成新的故障规则id
     * @param oldRuleId
     * @param newRuleId
     */
    void updateRuleIdByOld(String oldRuleId, String newRuleId);
}
