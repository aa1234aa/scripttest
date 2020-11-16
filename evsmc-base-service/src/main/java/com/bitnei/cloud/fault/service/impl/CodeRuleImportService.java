package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.HexUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.fault.dao.CodeRuleMapper;
import com.bitnei.cloud.fault.domain.CodeRule;
import com.bitnei.cloud.fault.enums.AlarmLevelEnum;
import com.bitnei.cloud.fault.enums.AnalyzeTypeEnum;
import com.bitnei.cloud.fault.enums.EnabledStatusEnum;
import com.bitnei.cloud.fault.enums.ResponseModeEnum;
import com.bitnei.cloud.fault.model.CodeRuleByteImportModel;
import com.bitnei.cloud.fault.model.CodeRuleImportModel;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.fault.model.CodeTypeModel;
import com.bitnei.cloud.fault.service.ICodeRuleImportService;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.fault.service.ICodeTypeService;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeRuleImportService实现<br>
 * 描述： CodeRuleImportService实现<br>
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
 * <td>2019-05-07 09:57:07</td>
 * <td>huangweimin</td>
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
public class CodeRuleImportService implements ICodeRuleImportService {

    @Autowired
    private ICodeTypeService codeTypeService;

    @Autowired
    private ICodeRuleItemService codeRuleItemService;

    @Autowired
    private IVehModelService vehModelService;

    @Autowired
    private ICodeRuleService codeRuleService;

    @Autowired
    private CodeRuleMapper codeRuleMapper;

    @Autowired
    private IDictService dictService;

    /**
     * 全部车型标记
     */
    private static final String ALL_VEH_MODEL = "all";

    /**
     * 生成导入模板文件
     *
     * @param analyzeType 解析方式: 1=按字节解析, 2=按bit位解析
     */
    @Override
    public void getImportTemplateFile(int analyzeType) {
        if (AnalyzeTypeEnum.BIT.getValue() == analyzeType) {
            EasyExcel.renderImportDemoFile("故障码报警规则(按bit位解析)导入模板.xls", CodeRuleImportModel.class);
        } else if (AnalyzeTypeEnum.BYTE.getValue() == analyzeType) {
            EasyExcel.renderImportDemoFile("故障码报警规则(按字节解析)导入模板.xls", CodeRuleByteImportModel.class);
        }
    }

    /**
     * 批量导入
     *
     * @param analyzeType 解析方式:1=按字节解析 , 2=按bit位解析
     * @param file        文件
     */
    @Override
    public void batchImport(int analyzeType, MultipartFile file) {
        String messageType = "CODERULE" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        List<DictModel> dictModels = dictService.findByDictType("SUBORDINATE_PARTS");
        Map<String, DictModel> dictModelMap = new HashMap<>(dictModels.size());
        dictModels.forEach(dictModel -> dictModelMap.put(dictModel.getName(), dictModel));
        if (!(analyzeType == 1 || analyzeType == 2)) {
            throw new BusinessException("解析方式有误");
        }

        new ExcelBatchHandler<CodeRuleImportModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            @Override
            public List<String> extendValidate(final CodeRuleImportModel model) {
                List<String> errorMsgList = new ArrayList<>();

                // 校验解析方式
                AnalyzeTypeEnum analyzeTypeEnum = analyzeType == 1 ? AnalyzeTypeEnum.BYTE : AnalyzeTypeEnum.BIT;
                if (analyzeTypeEnum.getDesc().equals(model.getAnalyzeTypeDisplay())) {
                    model.setAnalyzeType(analyzeTypeEnum.getValue());
                } else {
                    errorMsgList.add("解析方式有误, 应为'" + analyzeTypeEnum.getDesc() + "'");
                    return errorMsgList;
                }

                // 按位解析检查 起始位 非空
                if (analyzeType == AnalyzeTypeEnum.BIT.getValue()) {
                    if (model.getStartPoint() == null) {
                        errorMsgList.add("起始位不能为空");
                        return errorMsgList;
                    }
                }

                // 字典项、种类、故障码长度、起始位长度等校验
                errorMsgList.addAll(baseCheck(model, dictModelMap));
                if (!errorMsgList.isEmpty()) {
                    return errorMsgList;
                }

                CodeRule existCodeRule = codeRuleService.findByName(model.getFaultName());

                String normalCode = existCodeRule == null ? model.getNormalCode() : existCodeRule.getNormalCode();
                if (null != existCodeRule) {
                    // 解析方式不同，提示规则已存在，如果同一种解析方式，则追加故障码项
                    if (existCodeRule.getAnalyzeType() != analyzeType) {
                        errorMsgList.add("故障规则:'" + model.getFaultName() + "'已存在");
                        return errorMsgList;
                    }
                    model.setId(existCodeRule.getId());
                } else {
                    // 新增规则校验
                    try {
                        ruleCheck(model);
                    } catch (BusinessException e) {
                        errorMsgList.add(e.getMessage());
                    }
                }
                if (HexUtil.compare(model.getExceptionCode(), normalCode)) {
                    errorMsgList.add("无故障状态故障码与故障码重复冲突");
                }
                List<CodeRuleItemModel> existRuelItemList = codeRuleItemService.getByFaultCodeTypeId(model.getFaultCodeTypeId());
                for (CodeRuleItemModel existRuleItem : existRuelItemList) {
                    if (HexUtil.compare(existRuleItem.getExceptionCode(), model.getExceptionCode())) {
                        errorMsgList.add("故障码[" + existRuleItem.getExceptionCode() + "]已存在");
                    }
                }

                // 至少启用一个阈值
                if(!"启用".equals(model.getEnableTimeThresholdDesc()) &&
                        !"启用".equals(model.getEnableCountThresholdDesc())){
                    errorMsgList.add("时间或者帧数至少启用一个阈值");
                }

                if ("启用".equals(model.getEnableTimeThresholdDesc())) {
                    if (null == model.getBeginThreshold() || null == model.getEndThreshold()) {
                        errorMsgList.add("已启用的阈值类型，开始阈值和结束阈值不能为空");
                    }
                }
                if ("启用".equals(model.getEnableCountThresholdDesc())) {
                    if (null == model.getBeginCountThreshold() || null == model.getEndCountThreshold()) {
                        errorMsgList.add("已启用的阈值类型，开始阈值和结束阈值不能为空");
                    }
                }

                return errorMsgList;
            }

            @Override
            public void saveObject(final CodeRuleImportModel model) {
                // 判断是否只是新增故障码
                String id;
                if (StringUtils.isBlank(model.getId())) {
                    CodeRule obj = new CodeRule();
                    BeanUtils.copyProperties(model, obj);
                    id = UtilHelper.getUUID();
                    obj.setId(id);
                    obj.setCreateTime(DateUtil.getNow());
                    obj.setCreateBy(ServletUtil.getCurrentUser());
                    // 统一格式化十六进制码
                    obj.setNormalCode(HexUtil.format(obj.getNormalCode()));
                    // 如果阈值的启用状态为null， 则设置为禁用
                    if( obj.getEnableCountThreshold() == null ){
                        obj.setEnableCountThreshold(0);
                    }
                    if( obj.getEnableTimeThreshold() == null ){
                        obj.setEnableTimeThreshold(0);
                    }
                    int res = codeRuleMapper.insert(obj);
                    if (res == 0) {
                        throw new BusinessException("新增失败");
                    }
                } else {
                    id = model.getId();
                }

                CodeRuleItemModel itemModel = new CodeRuleItemModel();
                BeanUtils.copyProperties(model, itemModel);
                itemModel.setFaultCodeRuleId(id);
                codeRuleItemService.insert(itemModel);
            }
        }.work();
    }

    /**
     * 检查故障码长度， 起始位/起始字节
     *
     * @param analyzeType         解析方式
     * @param exceptionCodeLength 故障码长度
     * @param startPoint          起始位/起始字节
     * @param errorCallback       回调
     */
    @Override
    public void checkStartPointAndExceptionCodeLength(int analyzeType, Integer exceptionCodeLength, Integer startPoint, Consumer<String> errorCallback) {
        // 按字节解析限制
        int exceptionCodeByteLengthMin = 1;
        int exceptionCodeByteLengthMax = 4;
        // 按位解析限制
        int exceptionCodeBitLengthMin = 1;
        int exceptionCodeBitLengthMax = 32;
        int startPointBitLengthMin = 0;
        int startPointBitLengthMax = 99;
        switch (analyzeType) {
            case 1:
                if (exceptionCodeLength > exceptionCodeByteLengthMax || exceptionCodeLength < exceptionCodeByteLengthMin) {
                    errorCallback.accept("按字节解析时，故障码长度为自然数" + exceptionCodeByteLengthMin + "-" + exceptionCodeByteLengthMax);
                    return;
                }
                break;
            case 2:
                if (exceptionCodeLength > exceptionCodeBitLengthMax || exceptionCodeLength < exceptionCodeBitLengthMin) {
                    errorCallback.accept("按位解析时，故障码长度为自然数" + exceptionCodeBitLengthMin + "-" + exceptionCodeBitLengthMax);
                    return;
                }
                if (startPoint < startPointBitLengthMin || startPoint > startPointBitLengthMax) {
                    errorCallback.accept("按位解析时，起始位为自然数" + startPointBitLengthMin + "-" + startPointBitLengthMax);
                    return;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 导入数据基础校验
     *
     * @param model 校验对象
     * @return 校验信息
     */
    private List<String> baseCheck(CodeRuleImportModel model, Map<String, DictModel> modelMap) {
        List<String> errorMsgList = new ArrayList<>();

        // 车型校验
        StringBuffer sb = new StringBuffer();
        // 'all'代表全部车型, 则不需要校验获取具体车型ID
        if (!model.getVehModelName().equalsIgnoreCase(ALL_VEH_MODEL)) {
            VehModelModel vehModelModel;
            String[] vehNameArray = model.getVehModelName().split(",");
            for (String vehName : vehNameArray) {
                try {
                    vehModelModel = vehModelService.getByName(vehName.trim());
                    sb.append(vehModelModel.getId()).append(",");
                } catch (BusinessException e) {
                    errorMsgList.add("车型:'" + vehName + "'不存在");
                }
            }
            if (0 < sb.length()) {
                model.setVehModelId(sb.substring(0, sb.length() - 1));
            }
        } else {
            model.setVehModelId(ALL_VEH_MODEL);
        }

        // 故障种类校验
        try {
            CodeTypeModel codeTypeModel = codeTypeService.findByName(model.getFaultCodeTypeName());
            model.setFaultCodeTypeId(codeTypeModel.getId());
        } catch (BusinessException e) {
            errorMsgList.add("故障种类:'" + model.getFaultCodeTypeName() + "'不存在");
        }

        // 所属零部件校验
        DictModel dict = modelMap.get(model.getSubordinateParts());
        if (dict != null) {
            model.setSubordinatePartsId(dict.getValue());
        } else {
            errorMsgList.add("所属零部件:'" + model.getSubordinateParts() + "'不存在");
        }

        // 启用状态校验
        Integer intValue = EnabledStatusEnum.getValueByDesc(model.getEnabledStatusDisplay().trim());
        if (null != intValue) {
            model.setEnabledStatus(intValue);
        } else {
            errorMsgList.add("启用状态内容有误");
        }

        // 至少启用一个阈值
        if(!"启用".equals(model.getEnableTimeThresholdDesc()) &&
                !"启用".equals(model.getEnableCountThresholdDesc())){
            errorMsgList.add("时间或者帧数至少启用一个阈值");
        }

        if (StringUtils.isNotEmpty(model.getEnableCountThresholdDesc())) {
            model.setEnableCountThresholdDesc(model.getEnableCountThresholdDesc().trim());
            if ("禁用".equals(model.getEnableCountThresholdDesc())) {
                model.setBeginCountThreshold(null);
                model.setEndCountThreshold(null);
            }
        } else {
            model.setBeginCountThreshold(null);
            model.setEndCountThreshold(null);
        }
        if (StringUtils.isNotEmpty(model.getEnableTimeThresholdDesc())) {
            model.setEnableTimeThresholdDesc(model.getEnableTimeThresholdDesc().trim());
            if ("禁用".equals(model.getEnableTimeThresholdDesc())) {
                model.setBeginThreshold(null);
                model.setEndThreshold(null);
            }
        } else {
            model.setBeginThreshold(null);
            model.setEndThreshold(null);
        }

        // 时间阈值启用状态校验
        Integer enableTimeThreshold = EnabledStatusEnum.getValueByDesc(model.getEnableTimeThresholdDesc());
        if (null != enableTimeThreshold) {
            model.setEnableTimeThreshold(enableTimeThreshold);
        } else {
            errorMsgList.add("是否启用持续时间内容有误");
        }

        // 帧数阈值启用状态校验
        Integer enableCountThreshold = EnabledStatusEnum.getValueByDesc(model.getEnableCountThresholdDesc());
        if (null != enableCountThreshold) {
            model.setEnableCountThreshold(enableCountThreshold);
        } else {
            errorMsgList.add("是否启用持续帧数内容有误");
        }

        // 报警级别校验
        intValue = AlarmLevelEnum.getValueByDesc(model.getAlarmLevelDisplay());
        if (null != intValue) {
            model.setAlarmLevel(intValue);
        } else {
            errorMsgList.add("报警级别内容有误");
        }

        // 响应方式校验
        sb = new StringBuffer();
        String[] responseModeArray = model.getResponseModeDisplay().split(",");
        for (String responseMode : responseModeArray) {
            String strValue = ResponseModeEnum.getValueByDesc(responseMode);
            if (null != strValue) {
                sb.append(strValue).append(",");
            } else {
                errorMsgList.add("响应方式有误");
            }
        }
        if (0 < sb.length()) {
            model.setResponseMode(sb.substring(0, sb.length() - 1));
        }

        // 故障码长度校验
        checkStartPointAndExceptionCodeLength(model.getAnalyzeType(), model.getExceptionCodeLength(), model.getStartPoint(), errorMsg -> errorMsgList.add(errorMsg));

        return errorMsgList;
    }

    /**
     * 导入bit位解析故障规则校验
     *
     * @param newRuleModel 校验对象
     */
    private void ruleCheck(CodeRuleImportModel newRuleModel) {
        List<CodeRule> existRuleList = codeRuleService.findByTypeId(newRuleModel.getFaultCodeTypeId());
        for (CodeRule existRule : existRuleList) {
            // 如果是修改,则不与自身做相关比较,进入下次循环
            if (existRule.getId().equals(newRuleModel.getId())) {
                continue;
            }

            // 全部车型不需要校验
            if (!newRuleModel.getVehModelId().equalsIgnoreCase(ALL_VEH_MODEL)) {
                String[] vehModeIds = newRuleModel.getVehModelId().split(",");
                for (String id : vehModeIds) {
                    if (existRule.getVehModelId().contains(id) && !existRule.getAnalyzeType().equals(newRuleModel.getAnalyzeType())) {
                        throw new BusinessException("所选车型在此故障种类下已设置有另外的解析方式");
                    }
                }
            }
            if (existRule.getAnalyzeType().equals(2) && newRuleModel.getAnalyzeType().equals(2) && existRule.getStartPoint().equals(newRuleModel.getStartPoint())) {
                throw new BusinessException("按Bit解析的无故障状态故障码，同一故障种类下，起始位不允许重复");
            }
        }
    }

}
