package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.NotifierRuleLkMapper;
import com.bitnei.cloud.fault.domain.NotifierRuleLk;
import com.bitnei.cloud.fault.enums.FaultTypeEnum;
import com.bitnei.cloud.fault.model.NotifierRuleLkModel;
import com.bitnei.cloud.fault.service.INotifierRuleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierRuleLkService实现<br>
 * 描述： NotifierRuleLkService实现<br>
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
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.NotifierRuleLkMapper")
public class NotifierRuleLkService extends BaseService implements INotifierRuleLkService {

    @Autowired
    private NotifierRuleLkMapper notifierRuleLkMapper;

    @Override
    public List<NotifierRuleLkModel> list(@NotBlank String notifierId, @NotNull FaultTypeEnum ruleType) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_notifier_rule_lk", "fnrl");
        params.put("notifierId", notifierId);
        params.put("ruleType", ruleType);
        List<NotifierRuleLk> lkList = null;
        switch (ruleType) {
            case PARAMETER:
                lkList = notifierRuleLkMapper.findParameterRuleList(params);
                break;
            case CODE:
                lkList = notifierRuleLkMapper.findCodeRuleList(params);
                break;
            case ENCLOSURE:
                lkList = notifierRuleLkMapper.findEnclosureRuleList(params);
                break;
            default:
                break;
        }
        List<NotifierRuleLkModel> models = new ArrayList<>();
        for (NotifierRuleLk entry : lkList) {
            models.add(NotifierRuleLkModel.fromEntry(entry));
        }
        return models;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String notifierId, Integer ruleType, List<String> ruleIdList) {
        List<NotifierRuleLk> notifierRuleLkList = new ArrayList<>();
        ruleIdList.stream().forEach(ruleId -> {
            NotifierRuleLk lk = new NotifierRuleLk();
            lk.setId(UtilHelper.getUUID());
            lk.setNotifierId(notifierId);
            lk.setRuleType(ruleType);
            lk.setRuleId(ruleId);
            lk.setCreateBy(ServletUtil.getCurrentUser());
            lk.setCreateTime(DateUtil.getNow());
            notifierRuleLkList.add(lk);
        });
        notifierRuleLkMapper.insertBatch(notifierRuleLkList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByNotifierId(String notifierId) {
        notifierRuleLkMapper.delete(notifierId, null);
    }

    @Override
    public void deleteByRuleId(String ruleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleId", ruleId);
        notifierRuleLkMapper.deleteByRuleId(params);
    }

    @Override
    public void updateRuleIdByOld(String oldRuleId, String newRuleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("oldRuleId", oldRuleId);
        params.put("newRuleId", newRuleId);
        notifierRuleLkMapper.updateRuleIdByOld(params);
    }
}
