package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.dc.model.RuleModel;
import com.bitnei.cloud.dc.service.IRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.TermModelUnitMapper;
import com.bitnei.cloud.sys.domain.TermModel;
import com.bitnei.cloud.sys.domain.TermModelUnit;
import com.bitnei.cloud.sys.model.EncryptionChipModel;
import com.bitnei.cloud.sys.model.SimManagementModel;
import com.bitnei.cloud.sys.model.TermModelModel;
import com.bitnei.cloud.sys.model.TermModelUnitModel;
import com.bitnei.cloud.sys.service.IEncryptionChipService;
import com.bitnei.cloud.sys.service.ISimManagementService;
import com.bitnei.cloud.sys.service.ITermModelService;
import com.bitnei.cloud.sys.service.ITermModelUnitService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermModelUnitService实现<br>
 * 描述： TermModelUnitService实现<br>
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
 * <td>2018-11-05 10:01:48</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */

@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.TermModelUnitMapper")
public class TermModelUnitService extends BaseService implements ITermModelUnitService {

    @Resource
    private ITermModelService termModelService;
    @Resource
    private IRuleService ruleService;
    @Resource
    private ISimManagementService simManagementService;
    @Autowired
    private IEncryptionChipService encryptionChipService;
    @Resource
    private TermModelUnitMapper termModelUnitMapper;

    private final String termIds = "termIds";

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.containsKey(termIds)) {
            String ids = (String) params.get(termIds);
            params.put("termIds", StringUtils.split(ids, ","));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<TermModelUnit> entries = findBySqlId("pagerModel", params);
            List<TermModelUnitModel> models = new ArrayList<>();
            for (TermModelUnit entry : entries) {
                models.add(TermModelUnitModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<TermModelUnitModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                TermModelUnit obj = (TermModelUnit) entry;
                models.add(TermModelUnitModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public List<TermModelUnit> list(Map<String, Object> map) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.putAll(map);
        if (params.containsKey(termIds)) {
            String ids = (String) params.get(termIds);
            params.put("termIds", StringUtils.split(ids, ","));
        }
        return findBySqlId("pagerModel", params);
    }

    @Override
    public TermModelUnitModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.put("id", id);
        TermModelUnit entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException(id + ":终端不存在");
        }
        return TermModelUnitModel.fromEntry(entry);
    }

    @Override
    public TermModelUnitModel getOrNull(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.put("id", id);
        TermModelUnit entry = unique("findById", params);
        if (entry == null) {
            return null;
        }
        return TermModelUnitModel.fromEntry(entry);
    }


    @Override
    public TermModelUnitModel findBySerialNumber(String serialNumber) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.put("serialNumber", serialNumber);
        TermModelUnit entry = unique("findBySerialNumber", params);
        if (entry == null) {
            throw new BusinessException("终端编号不存在");
        }
        return TermModelUnitModel.fromEntry(entry);
    }

    @Override
    public void insert(TermModelUnitModel model) {

        TermModelUnit obj = new TermModelUnit();
        BeanUtils.copyProperties(model, obj);
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.put("id", obj.getSysTermModelId());
        TermModel entry = termModelService.unique("findById", params);
        //加密芯片 支持加密芯片（是/否），若为是，则在录入终端时，必须选择加密芯片ID；
        if (entry != null) {
            if (entry.getSupportEncryptionChip() == 1) {
                validateChip(model.getEncryptionChipId(), null);
            } else {
                obj.setEncryptionChipId(null);
            }
        }
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(TermModelUnitModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");

        TermModelUnit obj = new TermModelUnit();
        BeanUtils.copyProperties(model, obj);
        //获取当权限的map
        Map<String, Object> modelParams = DataAccessKit.getAuthMap("sys_term_model", "tm");
        modelParams.put("id", obj.getSysTermModelId());
        TermModel entry = termModelService.unique("findById", modelParams);
        //加密芯片 支持加密芯片（是/否），若为是，则在录入终端时，必须选择加密芯片ID；
        if (entry != null) {
            if (entry.getSupportEncryptionChip() == 1) {
                validateChip(model.getEncryptionChipId(), model.getId());
            } else {
                obj.setEncryptionChipId(null);
            }
        }
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 校验加密芯片必填，并且不能是已被别的终端绑定的
     *
     * @param encryptionChipId
     * @param termModelUnitId
     */
    private void validateChip(String encryptionChipId, String termModelUnitId) {
        if (StringUtils.isBlank(encryptionChipId)) {
            throw new BusinessException("终端型号的支持加密芯片为是，加密芯片ID不能为空");
        } else {
            String error = validateChipBinding(encryptionChipId, termModelUnitId);
            if (StringUtils.isNotEmpty(error)) {
                throw new BusinessException(error, 300);
            }
        }
    }

    private String validateChipBinding(String encryptionChipId, String termModelUnitId) {
        Map<String, Object> params = new HashMap<>();
        params.put("encryptionChipId", encryptionChipId);
        if (StringUtils.isNotEmpty(termModelUnitId)) {
            //编辑的时候计数要排除自己
            params.put("termModelUnitId", termModelUnitId);
        }
        Integer tmuCount = termModelUnitMapper.findByEncryptionChipIdCount(params);
        if (tmuCount > 0) {
            return "该加密芯片ID已被终端使用";
        }
        return null;
    }

    /**
     * 删除多个
     *
     * @param ids id集合
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<TermModelUnit>(this, "pagerModel", params, "sys/res/termModelUnit/export.xls", "终端管理") {
        }.work();

        return;


    }

    public static final DateTimeFormatter FULL_DATE_0 = DateTimeFormatter.ofPattern("yyyy-M-d");
    public static final DateTimeFormatter FULL_DATE_1 = DateTimeFormatter.ofPattern("yyyy/M/d");

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "TERMMODELUNIT" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<TermModelUnitModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model TermModelUnitModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(TermModelUnitModel model) {
                List<String> errors = Lists.newArrayList();
                // 终端厂商自定义编号
                if (StringUtils.isNotBlank(model.getSerialNumber())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
                    params.put("serialNumber", model.getSerialNumber());
                    TermModelUnit entry = unique("findBySerialNumber", params);
                    if (entry != null) {
                        errors.add("终端厂商自定义编号已存在");
                    }
                }
                // 终端型号
                if (StringUtils.isNotBlank(model.getTermModelName())) {
                    TermModelModel termModel = null;
                    try {
                        termModel = termModelService.findByTermModelName(model.getTermModelName());
                        model.setSysTermModelId(termModel.getId());
                    } catch (BusinessException e) {
                        errors.add("终端型号不存在");
                    }
                    //加密芯片 支持加密芯片（是/否），若为是，则在录入终端时，必须选择加密芯片ID；
                    if (termModel != null) {
                        if (termModel.getSupportEncryptionChip() == 1) {
                            if (StringUtils.isNotBlank(model.getEncryptionChipName())) {
                                try {
                                    EncryptionChipModel encryptionChipModel =
                                            encryptionChipService.getByIdentificationId(model.getEncryptionChipName());

                                    if (encryptionChipModel != null) {

                                        String error = validateChipBinding(
                                                encryptionChipModel.getId(), null);

                                        if (StringUtils.isNotEmpty(error)) {
                                            errors.add(error);
                                        }
                                        model.setEncryptionChipId(encryptionChipModel.getId());
                                    }
                                } catch (BusinessException e) {
                                    errors.add("加密芯片ID不存在");
                                }
                            } else {
                                errors.add("终端型号的支持加密芯片为是，加密芯片ID不能为空");
                            }
                        } else {
                            if (StringUtils.isNotBlank(model.getEncryptionChipName())) {
                                errors.add(String.format("终端型号%s的支持加密芯片为否，加密芯片ID应为空",
                                        termModel.getTermModelName()));
                            }
                        }
                    }
                }
                // 支持协议
                if (StringUtils.isNotBlank(model.getSupportProtocolName())) {
                    try {
                        RuleModel rule = ruleService.getByName(model.getSupportProtocolName());
                        model.setSupportProtocol(rule.getId());
                    } catch (BusinessException e) {
                        errors.add("支持协议不存在");
                    }
                }
                // ICCID
                if (StringUtils.isNotBlank(model.getIccid())) {
                    try {
                        SimManagementModel sim = simManagementService.findByIccId(model.getIccid());
                        TermModelUnit term = unique("findByIccid", sim.getIccid());
                        if (term != null) {
                            errors.add("ICCID已被终端使用");
                        }
                    } catch (BusinessException e) {
                        errors.add("ICCID不存在");
                    }
                }
                // 校验出厂日期有效性
                if (StringUtils.isNotBlank(model.getFactoryDate())) {
                    if (!com.bitnei.cloud.sys.util.DateUtil.validate(model.getFactoryDate())) {
                        errors.add("出厂日期不合法");
                    }
                }
                return errors;
            }

            /**
             *  保存实体
             * @param model TermModelUnitModel
             */
            @Override
            public void saveObject(TermModelUnitModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "TERMMODELUNIT" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<TermModelUnitModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model TermModelUnitModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(TermModelUnitModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model TermModelUnitModel
             */
            @Override
            public void saveObject(TermModelUnitModel model) {
                update(model);
            }
        }.work();

    }


    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("终端导入查询模板.xls", "ICCID", new String[]{"89860403101890200000"});
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("终端编号导入模板.xls", TermModelUnitModel.class);
    }

    @Override
    public void importFind() {
        EasyExcel.renderImportSearchDemoFile("车辆终端信息导入查询模板.xls", "iccid", new String[]{"BITNEIYPX00000000012"});
    }
}
