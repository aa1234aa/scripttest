package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.HexUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.CodeRuleMapper;
import com.bitnei.cloud.fault.domain.CodeRule;
import com.bitnei.cloud.fault.domain.CodeRuleExport;
import com.bitnei.cloud.fault.enums.AnalyzeTypeEnum;
import com.bitnei.cloud.fault.enums.ChangeScopeEnum;
import com.bitnei.cloud.fault.model.CodeRuleExportModel;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.fault.model.CodeRuleModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.fault.service.INotifierRuleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeRuleService实现<br>
 * 描述： CodeRuleService实现<br>
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
 * <td>2019-02-25 16:55:47</td>
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
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.CodeRuleMapper")
public class CodeRuleService extends BaseService implements ICodeRuleService {

    @Autowired
    private ICodeRuleItemService codeRuleItemService;

    @Autowired
    private IVehModelService vehModelService;

    @Resource
    private IAlarmInfoService alarmInfoService;

    @Resource
    private CodeRuleMapper codeRuleMapper;

    @Autowired
    private IDictService dictService;

    @Autowired
    private INotifierRuleLkService notifierRuleLkService;

    @Override
    public Object list(PagerInfo pagerInfo) {
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<CodeRule> entries = findBySqlId("pagerModel", params);
            List<CodeRuleModel> models = new ArrayList<>();
            for (CodeRule entry : entries) {
                CodeRuleModel model = CodeRuleModel.fromEntry(entry);
                setVehModelName(model, "list");
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<CodeRuleModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                CodeRule obj = (CodeRule) entry;
                CodeRuleModel model = CodeRuleModel.fromEntry(obj);
                setVehModelName(model, "list");
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public CodeRuleModel get(String id) {
        CodeRule entry = codeRuleMapper.findById(id);
        if (entry == null) {
            throw new BusinessException("故障码规则已不存在");
        }
        CodeRuleModel model = CodeRuleModel.fromEntry(entry);
        setVehModelName(model, "getMethod");
        return model;
    }

    /**
     * 新增故障码规则
     *
     * @param model 新增Model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(CodeRuleModel model) {
        // 数据校验
        saveCheck(model);

        CodeRule obj = new CodeRule();
        BeanUtils.copyProperties(model, obj);
//        initDefaultValue(obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        // 统一格式化十六进制码
        obj.setNormalCode(HexUtil.format(obj.getNormalCode()));
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        insertItems(id, model.getItems());
    }

    private void initDefaultValue(CodeRule rule) {
        if (rule.getBeginThreshold() == null) {
            rule.setBeginThreshold(0);
        }
        if (rule.getEndThreshold() == null) {
            rule.setEndThreshold(0);
        }
    }

    /**
     * 更新规则
     *
     * @param newModel 规则对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleModel newModel) {
        // 数据校验
        saveCheck(newModel);

        CodeRule obj = new CodeRule();
        BeanUtils.copyProperties(newModel, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        // 统一格式化十六进制码
        obj.setNormalCode(HexUtil.format(obj.getNormalCode()));
//        initDefaultValue(obj);

        // 检查规则改动范围
        ChangeScopeEnum ruleChangeScope = ruleChangeScope(newModel);

        // 检查故障码项改动范围
        List<CodeRuleItemModel> oldItemList = codeRuleItemService.list(newModel.getId());
        Map<String, CodeRuleItemModel> oldItemMap = oldItemList.stream()
                .collect(Collectors.toMap(CodeRuleItemModel::getId, item -> item));
        ChangeScopeEnum ruleItemChangeScope = ruleItemChangeScope(newModel.getItems(), oldItemMap);

        // 若改动了影响实时计算的主要信息, 需要保存为新的规则并删除旧规则
        if (ChangeScopeEnum.MAIN == ruleChangeScope || ChangeScopeEnum.MAIN == ruleItemChangeScope) {
            // 保存为新的规则记录
            String oldRuleId = obj.getId();
            String newRuleId = UtilHelper.getUUID();
            obj.setId(newRuleId);
            obj.setCreateTime(DateUtil.getNow());
            obj.setCreateBy(ServletUtil.getCurrentUser());
            codeRuleMapper.insert(obj);
            insertItems(newRuleId, newModel.getItems());
            // 删除原规则
            deleteMulti(oldRuleId);

            // 故障码规则比较特殊，故障码类型下的规则被修改。删除所有原子规则的推送规则
            oldItemList.forEach(it -> notifierRuleLkService.deleteByRuleId(it.getId()));

        } else {
            // 更新规则
            codeRuleMapper.update(obj);
            updateItems(newModel.getItems(), oldItemMap);
            // 判断是否更新当前报警
            if (ChangeScopeEnum.COMMON == ruleChangeScope) {
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("ruleName", obj.getFaultName());
                updateMap.put("parentRuleId", obj.getId());
                alarmInfoService.updateAlarm(updateMap);
            }
        }
    }

    /**
     * 检查规则改动范围
     *
     * @param newModel 待保存故障码规则对象
     * @return 改动范围枚举
     */
    private ChangeScopeEnum ruleChangeScope(CodeRuleModel newModel) {
        CodeRuleModel oldModel = get(newModel.getId());

        // 适用车型由所有车型变为指定车型或原指定车型有变化, 需保存为新规则
        if (!oldModel.getVehModelId().equalsIgnoreCase(newModel.getVehModelId())) {
            return ChangeScopeEnum.MAIN;
        }
        // bit位解析规则起始位被修改, 需保存为新规则
        if (newModel.getAnalyzeType().equals(AnalyzeTypeEnum.BIT.getValue()) && !oldModel.getStartPoint().equals(newModel.getStartPoint())) {
            return ChangeScopeEnum.MAIN;
        }

        // 名称修改为基础信息修改, 后续需要同步更新当前报警信息
        if (!newModel.getFaultName().equals(oldModel.getFaultName())) {
            return ChangeScopeEnum.COMMON;
        }

        // 其他信息改动不对业务产生影响
        return ChangeScopeEnum.NORMAL;
    }

    /**
     * 检查故障码项改动范围
     *
     * @param newItemsList 待保存故障码项集合
     * @param oldItemMap   原故障码项Map
     * @return 改动范围枚举
     */
    private ChangeScopeEnum ruleItemChangeScope(List<CodeRuleItemModel> newItemsList, Map<String, CodeRuleItemModel> oldItemMap) {
        // 故障码项集合有变化, 需保存为新规则
        if (newItemsList.size() != oldItemMap.size()) {
            return ChangeScopeEnum.MAIN;
        }

        CodeRuleItemModel oldItem;
        ChangeScopeEnum changeScopeEnum = ChangeScopeEnum.NORMAL;
        for (CodeRuleItemModel newItem : newItemsList) {
            oldItem = oldItemMap.get(newItem.getId());
            if (null == oldItem || !HexUtil.compare(newItem.getExceptionCode(), oldItem.getExceptionCode())) {
                // 故障码被修改影响实时计算, 需保存为新规则, 立即退出
                return ChangeScopeEnum.MAIN;
            } else if (!newItem.getAlarmLevel().equals(oldItem.getAlarmLevel())) {
                changeScopeEnum = ChangeScopeEnum.COMMON;
            } else if (!newItem.getResponseMode().equals(oldItem.getResponseMode())) {
                changeScopeEnum = ChangeScopeEnum.COMMON;
            }
        }

        return changeScopeEnum;
    }

    /**
     * 新增故障码项
     *
     * @param ruleId      规则ID
     * @param newItemList 待新增故障码项集合
     */
    private void insertItems(String ruleId, List<CodeRuleItemModel> newItemList) {
        for (CodeRuleItemModel itemModel : newItemList) {
            // 清空旧故障码项ID
            itemModel.setId(null);
            // 设置规则ID
            itemModel.setFaultCodeRuleId(ruleId);
            codeRuleItemService.insert(itemModel);
        }
    }

    /**
     * 更新故障码项
     *
     * @param newItemsList 待更新故障码项集合
     * @param oldItemMap   原故障码项MAP
     */
    private void updateItems(List<CodeRuleItemModel> newItemsList, Map<String, CodeRuleItemModel> oldItemMap) {
        CodeRuleItemModel oldItemModel;
        for (CodeRuleItemModel newItemModel : newItemsList) {
            codeRuleItemService.update(newItemModel);
            oldItemModel = oldItemMap.get(newItemModel.getId());
            Map<String, Object> updateMap = new HashMap<>();
            // 报警级别变化
            if (!oldItemModel.getAlarmLevel().equals(newItemModel.getAlarmLevel())) {
                updateMap.put("alarmLevel", newItemModel.getAlarmLevel());
            }
            // 响应方式变化
            if (!oldItemModel.getResponseMode().equals(newItemModel.getResponseMode())) {
                updateMap.put("responseMode", newItemModel.getResponseMode());
            }
            if (MapUtils.isNotEmpty(updateMap)) {
                updateMap.put("ruleId", oldItemModel.getId());
                alarmInfoService.updateAlarm(updateMap);
            }
        }
    }


    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            int r = codeRuleMapper.delete(id);

            //删除规则项
            codeRuleItemService.deleteByFaultCodeRuleId(id);
            count += r;

            //删除关联推送信息
            notifierRuleLkService.deleteByRuleId(id);
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);

        new ExcelExportHandler<CodeRuleExport>(this, "codeRuleExportList", params, "fault/res/codeRule/export.xls", "故障码规则表", CodeRuleExportModel.class) {
            @Override
            public Object process(CodeRuleExport entity) {
                CodeRuleExportModel model = (CodeRuleExportModel) super.process(entity);
                setVehModelName(model);
                model.setResponseModeDisplay(dictService.getDictNames(entity.getResponseMode(), "RESPONSE_MODE"));
                model.setEnabledStatusDisplay(dictService.getDictNames(
                        String.valueOf(entity.getEnabledStatus()), "ENABLED_STATUS"));
                return model;
            }
        }.work();

        return;
    }

    /**
     * 统计数量
     *
     * @param params 查询条件
     * @return 数量
     */
    @Override
    public int count(Map<String, Object> params) {
        return codeRuleMapper.count(params);
    }

    /**
     * 通过故障种类查询
     *
     * @param faultCodeTypeId 故障种类ID
     * @return 规则集合
     */
    @Override
    public List<CodeRule> findByTypeId(String faultCodeTypeId) {
        return codeRuleMapper.findByTypeId(faultCodeTypeId);
    }

    /**
     * 通过名称查询
     *
     * @param faultName 规则名称
     * @return 规则对象
     */
    @Override
    public CodeRule findByName(String faultName) {
        return codeRuleMapper.findByName(faultName);
    }

    @Override
    public void deleteByVehModelId(String vehModelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehModelId", vehModelId);
        params.put("updateTime", DateUtil.getNow());
        params.put("updateBy", ServletUtil.getCurrentUser());
        codeRuleMapper.deleteByVehModelId(params);
    }

    /**
     * 保存数据校验
     *
     * @param newRuleModel 规则对象
     */
    private void saveCheck(CodeRuleModel newRuleModel) {
        // 名称重复校验
        nameCheck(newRuleModel.getId(), newRuleModel.getFaultName());

        // 同故障种类下故障规则校验
        ruleCheck(newRuleModel);
        // 起始位检查

        if (newRuleModel.getAnalyzeType() == 2 && newRuleModel.getStartPoint() == null) {
            throw new BusinessException("按位解析时，起始位不能为空");
        }

        // 根据解析方式对应校验故障码项
        if (newRuleModel.getAnalyzeType().equals(AnalyzeTypeEnum.BYTE.getValue())) {
            byteCodeRuleItemCheck(newRuleModel);
        } else if (newRuleModel.getAnalyzeType().equals(AnalyzeTypeEnum.BIT.getValue())) {
            bitCodeRuleItemCheck(newRuleModel);
        }

        // 至少启用一个阈值
        if(((null == newRuleModel.getEnableTimeThreshold() || newRuleModel.getEnableTimeThreshold() == 0) &&
                (null == newRuleModel.getEnableCountThreshold() || newRuleModel.getEnableCountThreshold() == 0))){
            throw new BusinessException("时间或者帧数至少启用一个阈值");
        }

        if (newRuleModel.getEnableTimeThreshold() == 1) {
            if (null == newRuleModel.getBeginThreshold() || null == newRuleModel.getEndThreshold()) {
                throw new BusinessException("已启用的阈值类型，开始阈值和结束阈值不能为空");
            }
        } else {
            newRuleModel.setBeginThreshold(null);
            newRuleModel.setEndThreshold(null);
        }
        if (newRuleModel.getEnableCountThreshold() == 1) {
            if (null == newRuleModel.getBeginCountThreshold() || null == newRuleModel.getEndCountThreshold()) {
                throw new BusinessException("已启用的阈值类型，开始阈值和结束阈值不能为空");
            }
        } else {
            newRuleModel.setBeginCountThreshold(null);
            newRuleModel.setEndCountThreshold(null);
        }

    }

    /**
     * 同故障种类下故障规则校验
     *
     * @param newRuleModel 规则对象
     */
    private void ruleCheck(CodeRuleModel newRuleModel) {
        List<CodeRule> existRuleList = findByTypeId(newRuleModel.getFaultCodeTypeId());
        for (CodeRule existRule : existRuleList) {
            // 如果是修改,则不与自身做相关比较,进入下次循环
            if (existRule.getId().equals(newRuleModel.getId())) {
                // 所属故障种类不允许修改
                if (!existRule.getFaultCodeTypeId().equals(newRuleModel.getFaultCodeTypeId())) {
                    throw new BusinessException("所属故障种类不允许修改，操作失败");
                }
                // 解析方式不允许修改
                if (!existRule.getAnalyzeType().equals(newRuleModel.getAnalyzeType())) {
                    throw new BusinessException("解析方式不允许修改，操作失败");
                }
                continue;
            }

            String[] vehModeIds = newRuleModel.getVehModelId().split(",");
            for (String id : vehModeIds) {
                if (existRule.getVehModelId().contains(id) && !existRule.getAnalyzeType().equals(newRuleModel.getAnalyzeType())) {
                    throw new BusinessException("所选车型在此故障种类下已设置有另外的解析方式，操作失败");
                }
            }
            /* 处理 QYUPJ-115 问题
            if (existRule.getAnalyzeType().equals(AnalyzeTypeEnum.BYTE.getValue())
                    && newRuleModel.getAnalyzeType().equals(AnalyzeTypeEnum.BYTE.getValue())
                    && HexUtil.compare(existRule.getNormalCode(), newRuleModel.getNormalCode())) {
                throw new BusinessException("按字节解析的无故障状态故障码在同一故障种类下不同故障码规则内不允许重复，操作失败");
            }
            */
            if (existRule.getAnalyzeType().equals(AnalyzeTypeEnum.BIT.getValue())
                    && newRuleModel.getAnalyzeType().equals(AnalyzeTypeEnum.BIT.getValue())
                    && existRule.getStartPoint().equals(newRuleModel.getStartPoint())) {
                throw new BusinessException("按Bit解析的无故障状态故障码，同一故障种类下，起始位不允许重复，操作失败");
            }
        }
    }

    /**
     * 名称重复检查
     *
     * @param id        规则ID
     * @param faultName 规则名称
     */
    private void nameCheck(String id, String faultName) {
        Map<String, Object> params = new HashMap<>();
        params.put("faultName", faultName);
        if (StringUtils.isNotBlank(id)) {
            params.put("skipId", id);
        }
        if (0 < count(params)) {
            throw new BusinessException("规则名称已存在，操作失败");
        }
    }

    /**
     * 按bit位解析故障码项校验
     *
     * @param newRuleModel 规则对象
     */
    private void bitCodeRuleItemCheck(CodeRuleModel newRuleModel) {
        List<CodeRuleItemModel> itemList = newRuleModel.getItems();
        String exceptionCode = itemList.get(0).getExceptionCode();
        exceptionCode = HexUtil.format(exceptionCode);
        //if (1 < itemList.size() || (!"0".equals(exceptionCode) && !"1".equals(exceptionCode))) {
        //    throw new BusinessException("按bit位解析方式故障码仅能设置0或1其中一种情况，操作失败");
        //}
        if (HexUtil.compare(exceptionCode, newRuleModel.getNormalCode())) {
            throw new BusinessException("无故障状态故障码与故障码重复冲突，操作失败");
        }
    }

    /**
     * 按字节解析故障码项校验
     *
     * @param newRuleModel 规则对象
     */
    private void byteCodeRuleItemCheck(CodeRuleModel newRuleModel) {
        List<CodeRuleItemModel> existRuelItemList = codeRuleItemService.getByFaultCodeTypeId(newRuleModel.getFaultCodeTypeId());
        for (CodeRuleItemModel item : newRuleModel.getItems()) {
            if (HexUtil.compare(item.getExceptionCode(), newRuleModel.getNormalCode())) {
                throw new BusinessException("无故障状态故障码与故障码重复冲突，操作失败");
            }
            for (CodeRuleItemModel existRuleItem : existRuelItemList) {
                // 如果是修改,则不与自身做相关比较,进入下次循环; 或者是bit位解析的故障码也跳过
                if (existRuleItem.getId().equals(item.getId())
                        || !existRuleItem.getAnalyzeType().equals(AnalyzeTypeEnum.BYTE.getValue())) {
                    continue;
                }
                if (HexUtil.compare(existRuleItem.getExceptionCode(), item.getExceptionCode())) {
                    throw new BusinessException("故障码[" + existRuleItem.getExceptionCode() + "]已存在，操作失败");
                }
            }
        }
    }

    private void setVehModelName(CodeRuleExportModel model) {
        Map<String, String> map = vehModelService.getVehModelModelNames(model.getVehModelId());
        model.setVehModelName(map.get("vehModelName"));
    }

    private void setVehModelName(CodeRuleModel model, String method) {
        Map<String, String> map = vehModelService.getVehModelModelNames(model.getVehModelId());
        model.setVehModelName(map.get("vehModelName"));
        String vehModelId = map.get("vehModelId");
        if ("getMethod".equals(method)) {
            if (model.getVehModelId().length() != vehModelId.length()) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", model.getId());
                params.put("vehModelId", vehModelId);
                params.put("updateBy", ServletUtil.getCurrentUser());
                params.put("updateTime", DateUtil.getNow());
                codeRuleMapper.updateVehModelId(params);
            }
        }
        model.setVehModelId(vehModelId);
    }

}
