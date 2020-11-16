package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.dao.TermModelUnitMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.TermModel;
import com.bitnei.cloud.sys.model.TermModelModel;
import com.bitnei.cloud.sys.service.ITermModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermModelService实现<br>
 * 描述： TermModelService实现<br>
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
 * <td>2018-11-05 10:01:40</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.TermModelMapper")
public class TermModelService extends BaseService implements ITermModelService {

    @Resource
    private DictMapper dictMapper;
    @Resource
    private IUnitService unitService;
    @Resource
    private TermModelUnitMapper termModelUnitMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<TermModel> entries = findBySqlId("pagerModel", params);
            List<TermModelModel> models = new ArrayList<>();
            for (TermModel entry : entries) {
                models.add(TermModelModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<TermModelModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                TermModel obj = (TermModel) entry;
                models.add(TermModelModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public TermModelModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.put("id", id);
        TermModel entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("终端型号不存在");
        }
        return TermModelModel.fromEntry(entry);
    }

    @Override
    public TermModelModel findByTermModelName(String termModelName){
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.put("termModelName", termModelName);
        TermModel entry = unique("findByTermModelName", params);
        if (entry == null) {
            throw new BusinessException("车载终端型号不存在");
        }
        return TermModelModel.fromEntry(entry);
    }

    @Override
    public TermModelModel getOrNull(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.put("id", id);
        TermModel entry = unique("findById", params);
        if (entry == null) {
            return null;
        }
        return TermModelModel.fromEntry(entry);
    }


    @Override
    public void insert(TermModelModel model) {
        TermModel obj = new TermModel();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TermModelModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        TermModel obj = new TermModel();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        //支持加密芯片 改为 否 时，清空该终端型号对应车载终端的 加密芯片ID
        if (Constants.NO.equals(model.getSupportEncryptionChip())){
            //获取权限sql
            Map<String, Object> tmuParams = DataAccessKit.getAuthMap("sys_term_model_unit", "tmu");
            tmuParams.put("encryptionChipId", null);
            tmuParams.put("sysTermModelId", model.getId());
            tmuParams.put("updateTime", DateUtil.getNow());
            termModelUnitMapper.updateEncryptionChipId(tmuParams);
        }
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<TermModel>(this, "pagerModel", params, "sys/res/termModel/export.xls", "终端型号管理") {
        }.work();

        return;
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "TERMMODEL" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<TermModelModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(TermModelModel model) {
                List<String> errors = Lists.newArrayList();
                // 终端型号
                if(StringUtils.isNotBlank(model.getTermModelName())) {
                    Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
                    params.put("termModelName", model.getTermModelName());
                    TermModel entry = unique("findByTermModelName", params);
                    if(entry != null) {
                        errors.add("终端型号已存在");
                    }
                }
                // 终端种类
                if(StringUtils.isNotBlank(model.getTermCategoryDisplay())) {
                    Map<String, Object> typeParams = ImmutableMap.of("type", "TERM_TYPE", "name", model.getTermCategoryDisplay());
                    Dict dict = dictMapper.getByTypeAndName(typeParams);
                    if(dict != null) {
                        model.setTermCategory(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("终端种类填写错误");
                    }
                }
                // 支持加密芯片
                if(StringUtils.isNotBlank(model.getSupportEncryptionChipDisplay())) {
                    Map<String, Object> chipParams = ImmutableMap.of("type", "BOOL_TYPE", "name", model.getSupportEncryptionChipDisplay());
                    Dict chipDict = dictMapper.getByTypeAndName(chipParams);
                    if(chipDict != null) {
                        model.setSupportEncryptionChip(Integer.parseInt(chipDict.getValue()));
                    } else {
                        errors.add("支持加密芯片填写错误");
                    }
                }
                // 终端生产企业
                if(StringUtils.isNotBlank(model.getUnitName())) {
                    try {
                        String id = unitService.validateNameCode(model.getUnitName(), Constant.UNIT_TYPE_CODE.PROVIDER);
                        model.setUnitId(id);
                    } catch (BusinessException e) {
                        errors.add("终端生产企业不存在或类型不一致");
                    }
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model model
             */
            @Override
            public void saveObject(TermModelModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "TERMMODEL" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<TermModelModel>(file, messageType, GroupExcelImport.class, 0 , 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(TermModelModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(TermModelModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("终端型号导入模板.xls" , TermModelModel.class);
    }

    @Override
    public TermModel getByVehicleId(String vehicleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_model", "tm");
        params.put("vehicleId", vehicleId);
        return unique("getByVehicleId", params);
    }
}
