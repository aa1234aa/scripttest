package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.HexUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.CodeRuleItemMapper;
import com.bitnei.cloud.fault.domain.CodeRuleItem;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleItemService实现<br>
* 描述： CodeRuleItemService实现<br>
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
* <td>2019-02-26 11:18:23</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.CodeRuleItemMapper" )
public class CodeRuleItemService extends BaseService implements ICodeRuleItemService {

    @Resource
    private CodeRuleItemMapper codeRuleItemMapper;

    @Autowired
    private IDictService dictService;

    @Autowired
    private NotifierRuleLkService notifierRuleLkService;

    @Override
    public List<CodeRuleItemModel> list(String faultCodeRuleId, Boolean containDelete) {
        Map<String,Object> params = new HashMap<>();
        params.put("faultCodeRuleId", faultCodeRuleId);
        params.put("containDelete", containDelete);
        List<CodeRuleItem> entries = findBySqlId("pagerModel", params);
        List<CodeRuleItemModel> models = new ArrayList<>();
        for(CodeRuleItem entry: entries){
            CodeRuleItemModel model = CodeRuleItemModel.fromEntry(entry);
            model.setResponseModeDisplay(dictService.getDictNames(entry.getResponseMode(), "RESPONSE_MODE"));
            models.add(model);
        }
        return models;
    }

    @Override
    public List<CodeRuleItemModel> list(String faultCodeRuleId) {
        return list(faultCodeRuleId, false);
    }

    @Override
    public CodeRuleItemModel get(String id) {
        CodeRuleItem entry = codeRuleItemMapper.findById(id);
        if (entry == null){
            throw new BusinessException("故障码规则项已不存在");
        }
        CodeRuleItemModel model = CodeRuleItemModel.fromEntry(entry);
        model.setResponseModeDisplay(dictService.getDictNames(entry.getResponseMode(), "RESPONSE_MODE"));
        return model;
    }

    @Override
    public void insert(CodeRuleItemModel model) {

        CodeRuleItem obj = new CodeRuleItem();
        BeanUtils.copyProperties(model, obj);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());

        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        // 统一格式化十六进制码
        obj.setExceptionCode(HexUtil.format(obj.getExceptionCode()));
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(CodeRuleItemModel model) {
        CodeRuleItem obj = new CodeRuleItem();
        BeanUtils.copyProperties(model, obj);
        codeRuleItemMapper.update(obj);
    }

    @Override
    public void deleteByFaultCodeRuleId(String faultCodeRuleId) {
        Map<String,Object> params = new HashMap<>();
        params.put("faultCodeRuleId", faultCodeRuleId);
        List<CodeRuleItem> entries = findBySqlId("pagerModel", params);
        for (CodeRuleItem item : entries) {
            codeRuleItemMapper.delete(item.getId());
            notifierRuleLkService.deleteByRuleId(item.getId());
        }
    }

    @Override
    public List<CodeRuleItemModel> getByTypeCodeAndVehModelId(String typeCode, String vehModelId) {
        Map<String,Object> params = new HashMap<>();
        params.put("typeCode", typeCode);
        List<CodeRuleItem> entries = findBySqlId("pagerModel", params);
        List<CodeRuleItemModel> models = new ArrayList<>();
        Map<String,String> cacheMap= Maps.newHashMap();
        for(CodeRuleItem entry: entries){
            if (StringUtils.isNotEmpty(entry.getVehModelId())) {
                String[] vehModelIds = entry.getVehModelId().split(",");
                for (String item: vehModelIds) {

                    //只获取符合车辆公告型号的规则项 或 符合全部车型的规则项目
                    if (item.equals(vehModelId) || "all".equals(item)) {
                        CodeRuleItemModel model = CodeRuleItemModel.fromEntry(entry);
                        String key=String.format("%s_RESPONSE_MODE",entry.getResponseMode());
                        if (!cacheMap.containsKey(key)){
                            String value=  dictService.getDictNames(entry.getResponseMode(),
                                    "RESPONSE_MODE");
                            cacheMap.put(key,value);
                        }

                        model.setResponseModeDisplay(cacheMap.get(key));
                        models.add(model);
                        break;
                    }
                }
            }
        }
        return models;
    }

    @Override
    public List<CodeRuleItemModel> getByTypeCodeAndVehModelIdFast(String typeCode, String vehModelId,List<CodeRuleItem> codeRuleItemList) {

        List<CodeRuleItemModel> models = new ArrayList<>();
       if(CollectionUtils.isEmpty(codeRuleItemList)){
           return models;
       }
        Map<String,String> cacheMap= Maps.newHashMap();
        for(CodeRuleItem entry: codeRuleItemList){
            if (StringUtils.isNotEmpty(entry.getVehModelId())) {
                String[] vehModelIds = entry.getVehModelId().split(",");
                for (String item: vehModelIds) {

                    //只获取符合车辆公告型号的规则项 或 符合全部车型的规则项目
                    if (item.equals(vehModelId) || "all".equals(item)) {
                        CodeRuleItemModel model = CodeRuleItemModel.fromEntry(entry);
                        String key=String.format("%s_RESPONSE_MODE",entry.getResponseMode());
                        if (!cacheMap.containsKey(key)){
                            String value=  dictService.getDictNames(entry.getResponseMode(),
                                    "RESPONSE_MODE");
                            cacheMap.put(key,value);
                        }

                        model.setResponseModeDisplay(cacheMap.get(key));
                        models.add(model);
                        break;
                    }
                }
            }
        }
        return models;
    }
    @Override
    public List<CodeRuleItemModel> getByFaultCodeTypeId(String faultCodeTypeId) {
        List<CodeRuleItem> entries = codeRuleItemMapper.getByFaultCodeTypeId(faultCodeTypeId);
        List<CodeRuleItemModel> models = new ArrayList<>();
        for (CodeRuleItem entry : entries) {
            CodeRuleItemModel model = CodeRuleItemModel.fromEntry(entry);
            model.setResponseModeDisplay(dictService.getDictNames(entry.getResponseMode(), "RESPONSE_MODE"));
            models.add(model);
        }
        return models;
    }

    @Override
    public List<CodeRuleItem> getAllFault() {
        Map<String, Object> params = new HashMap<>();
        List<CodeRuleItem> entries = findBySqlId("pagerModel", params);
        return entries;
    }


}
