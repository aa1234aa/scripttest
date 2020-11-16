package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.dc.model.RuleTypeModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.dc.service.IRuleTypeService;
import com.bitnei.cloud.fault.dao.ParameterManageMapper;
import com.bitnei.cloud.fault.domain.ParameterManage;
import com.bitnei.cloud.fault.model.ParameterManageModel;
import com.bitnei.cloud.fault.service.IParameterManageService;
import com.bitnei.cloud.fault.service.IParameterRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ParameterManageService实现<br>
 * 描述： ParameterManageService实现<br>
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
 * <td>2019-03-01 09:17:04</td>
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
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.ParameterManageMapper")
public class ParameterManageService extends BaseService implements IParameterManageService {

    @Autowired
    private IRuleTypeService ruleTypeService;

    @Autowired
    private IDataItemService dataItemService;

    @Resource
    private ParameterManageMapper parameterManageMapper;

    @Autowired
    private IParameterRuleService parameterRuleService;

    @Override
    public Object list(PagerInfo pagerInfo) {
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<ParameterManage> entries = findBySqlId("pagerModel", params);
            List<ParameterManageModel> models = new ArrayList<>();
            for (ParameterManage entry : entries) {
                ParameterManageModel model = ParameterManageModel.fromEntry(entry);
                model.setDcRuleTypeName(getDcRuleTypeName(entry.getDcRuleTypeId()));
                models.add(model);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ParameterManageModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                ParameterManage obj = (ParameterManage) entry;
                ParameterManageModel model = ParameterManageModel.fromEntry(obj);
                model.setDcRuleTypeName(getDcRuleTypeName(obj.getDcRuleTypeId()));
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private String getDcRuleTypeName(String dcRuleTypeId) {
        try {
            RuleTypeModel model = ruleTypeService.get(dcRuleTypeId);
            return null != model ? model.getName() : "";
        } catch (BusinessException e) {

        }
        return "";
    }

    @Override
    public ParameterManageModel get(String id) {
        ParameterManage entry = parameterManageMapper.findById(id);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return ParameterManageModel.fromEntry(entry);
    }


    @Override
    public void insert(ParameterManageModel model) {
        DataItemModel itemModel = dataItemService.get(model.getDcDataItemId());
        if (!itemModel.getRuleTypeId().equals(model.getDcRuleTypeId())) {
            throw new BusinessException("关联数据项与协议类型不一致，操作失败");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("dcDataItemId", model.getDcDataItemId());
        params.put("dcRuleTypeId", model.getDcRuleTypeId());
        ParameterManage parameterManage = parameterManageMapper.findByParams(params);
        if (null != parameterManage) {
            throw new BusinessException("关联数据项-存在重复，操作失败");
        }

        ParameterManage obj = new ParameterManage();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        int res = parameterManageMapper.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(ParameterManageModel model) {
        ParameterManage obj = new ParameterManage();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        int res = parameterManageMapper.update(obj);
        if (res == 0) {
            throw new BusinessException("更新失败");
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
        Map<String, Object> params = new HashMap<>();
        String[] arr = ids.split(",");
        ParameterManage parameterManage = null;
        int count = 0;
        Map<String, Object> tempParam = null;
        for (String id : arr) {
            params.put("id", id);
            parameterManage = parameterManageMapper.getDataInfoById(params);
            tempParam = new HashMap<>();
            // 目前所有参数项默认带前缀'd',代表本次
            tempParam.put("existDataItem", "d"+parameterManage.getSeqNo());
            // 存在参数异常规则公式中关联此参数项, 不允许删除
            if(0 < parameterRuleService.count(tempParam)){
                throw new BusinessException("参数异常提醒规则中存在使用关联数据项["+parameterManage.getDcDataItemName()+"]的公式, 删除已取消");
            }
            int r = parameterManageMapper.delete(id);
            count += r;
        }
        return count;
    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "PARAMETERMANAGE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<ParameterManageModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ParameterManageModel model) {
                List<String> externErrorStrings = new ArrayList<>();
                if (StringUtils.isNotBlank(model.getDcRuleTypeName()) && StringUtils.isNotBlank(model.getDcDataItemName())) {
                    // 协议类型有效性校验
                    RuleTypeModel ruleTypeModel = null;
                    try {
                        ruleTypeModel = ruleTypeService.getByName(model.getDcRuleTypeName().trim());
                        model.setDcRuleTypeId(ruleTypeModel.getId());
                    } catch (Exception e) {
                        externErrorStrings.add("协议类型不存在");
                    }
                    // 协议数据项有效性校验
                    DataItemModel dataItemModel = null;
                    try {
                        dataItemModel = dataItemService.getByName(model.getDcDataItemName(), model.getDcRuleTypeId());
                        model.setDcDataItemId(dataItemModel.getId());
                    } catch (Exception e) {
                        externErrorStrings.add("协议类型下不存在" + model.getDcDataItemName() + "数据项");
                    }
                    // 重复校验
                    Map<String, Object> params = new HashMap<>();
                    params.put("dcDataItemId", model.getDcDataItemId());
                    params.put("dcRuleTypeId", model.getDcRuleTypeId());
                    ParameterManage parameterManage = parameterManageMapper.findByParams(params);
                    if (null != parameterManage) {
                        externErrorStrings.add("关联数据项-存在重复");
                    }
                }

                return externErrorStrings;
            }

            /**
             * 保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ParameterManageModel model) {
                insert(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("报警参数项导入模板.xls", ParameterManageModel.class);
    }

}
