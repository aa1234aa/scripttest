package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.DictCategory;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.service.IDictCategoryService;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DictService实现<br>
 * 描述： DictService实现<br>
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
 * <td>2018-10-31 16:54:54</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.DictMapper")
public class DictService extends BaseService implements IDictService {


    @Autowired
    private IDictCategoryService dictCategoryService;

    @Override
    public List<DictModel> findByDictType(String dictType) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        params.put("dictType", dictType);

        List<Dict> entries = findBySqlId("findByType", params);
        List<DictModel> models = new ArrayList<>();
        for (Dict entry : entries) {
            DictModel m = new DictModel();
            BeanUtils.copyProperties(entry, m);
            models.add(m);
        }
        return models;
    }

    public Map<String, DictModel> findCacheByDictType(String dictType) {
        List<DictModel> dictModels = findByDictType(dictType);
        if (CollectionUtils.isNotEmpty(dictModels)) {
            return dictModels.stream().collect(Collectors.toMap(DictModel::getValue, it -> it));
        }
        return new HashMap<>();
    }

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<Dict> entries = findBySqlId("pagerModel", params);
            List<DictModel> models = new ArrayList<>();
            for (Dict entry : entries) {
                models.add(DictModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DictModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                Dict obj = (Dict) entry;
                models.add(DictModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public DictModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        params.put("id", id);
        Dict entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return DictModel.fromEntry(entry);
    }


    @Override
    public void insert(DictModel model) {
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("code", model.getType());
        DictCategory dc = dictCategoryService.unique("findByCode", params);
        if (dc == null) {
            throw new BusinessException(String.format("字典类别%s不存在，请确认", model.getType()));
        } else {
            Dict obj = new Dict();
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
    }

    @Override
    public void update(DictModel model) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("code", model.getType());
        DictCategory dc = dictCategoryService.unique("findByCode", map);
        if (dc == null) {
            throw new BusinessException(String.format("字典类别%s不存在，请确认", model.getType()));
        } else {
            //获取当权限的map
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
            Dict obj = new Dict();
            BeanUtils.copyProperties(model, obj);
            obj.setUpdateTime(DateUtil.getNow());
            obj.setUpdateBy(ServletUtil.getCurrentUser());
            params.putAll(MapperUtil.Object2Map(obj));
            int res = super.updateByMap(params);
            if (res == 0) {
                throw new BusinessException("更新失败");
            }
        }
    }

    /**
     * 删除多个
     *
     * @param ids ids
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Dict>(this, "pagerModel", params, "sys/res/dict/export.xls", "字典管理") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DICT" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DictModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model DictModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(DictModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model DictModel
             */
            @Override
            public void saveObject(DictModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DICT" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DictModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model DictModel
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(DictModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model DictModel
             */
            @Override
            public void saveObject(DictModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 获取所有字典项
     *
     * @return List<DictModel>
     */
    @Override
    public List<DictModel> findAllDicts() {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        List<Dict> entries = findBySqlId("all", params);
        List<DictModel> models = new ArrayList<>();
        for (Dict entry : entries) {
            DictModel m = new DictModel();
            BeanUtils.copyProperties(entry, m);
            models.add(m);
        }
        return models;
    }

    /**
     * 通过值和类型 获取字典项名称
     *
     * @param value    值
     * @param dictType 类型
     * @return 字典项名称
     */
    @Override
    public String getDictName(String value, String dictType) {
        List<DictModel> dictModels = findByDictType(dictType);
        String display = "";
        for (DictModel dictModel : dictModels) {
            if (value.equals(dictModel.getValue())) {
                display = dictModel.getName();
                break;
            }
        }
        return display;
    }

    @Override
    public String getDictNames(String values, String dictType) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dict", "d");
        params.put("dictType", dictType);
        List<Dict> entries = findBySqlId("findByType", params);
        String[] strValues = values.split(",");
        StringBuilder dictNames = new StringBuilder();
        for (String value : strValues) {
            for (Dict entity : entries) {
                if (value.equals(entity.getValue())) {
                    dictNames.append(entity.getName());
                    dictNames.append(",");
                    break;
                }
            }
        }
        String dictNameDisplay = dictNames.toString();
        dictNameDisplay = dictNameDisplay.endsWith(",") ? dictNameDisplay.substring(0, dictNameDisplay.length() - 1) : dictNameDisplay;
        return dictNameDisplay;
    }
}
