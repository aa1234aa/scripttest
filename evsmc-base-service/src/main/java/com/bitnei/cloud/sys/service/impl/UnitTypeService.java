package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.UnitTypeMapper;
import com.bitnei.cloud.sys.domain.UnitType;
import com.bitnei.cloud.sys.model.UnitTypeModel;
import com.bitnei.cloud.sys.service.IUnitTypeService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UnitTypeService实现<br>
 * 描述： UnitTypeService实现<br>
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
 * <td>2018-12-20 11:48:35</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UnitTypeMapper")
public class UnitTypeService extends BaseService implements IUnitTypeService {

    @Resource
    private UnitTypeMapper unitTypeMapper;

    @Override
    public Object tree(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UnitType> entries = findBySqlId("pagerModel", params);
            List<UnitTypeModel> models = new ArrayList<>();
            for (UnitType entry : entries) {
                models.add(UnitTypeModel.fromEntry(entry));
            }
            return new TreeHandler<UnitTypeModel>(models) {
                @Override
                protected TreeNode beanToTreeNode(UnitTypeModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    Map<String, Object> attr = Maps.newHashMap();
                    attr.put("name", bean.getName());
                    attr.put("code", bean.getCode());
                    tn.setAttributes(attr);
                    return tn;
                }

                @Override
                protected boolean isRoot(TreeNode node) {
                    return StringUtil.isEmpty(node.getParentId()) || "0".equals(node.getId());
                }
            }.toTree();
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UnitTypeModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                UnitType obj = (UnitType) entry;
                models.add(UnitTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UnitType> entries = findBySqlId("pagerModel", params);
            List<UnitTypeModel> models = Lists.newArrayList();
            for (UnitType entry : entries) {
                models.add(UnitTypeModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<UnitTypeModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                UnitType obj = (UnitType) entry;
                models.add(UnitTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public UnitTypeModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.put("id", id);
        UnitType entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        //if(entry.getIsRoot()==1)
        // ;
        return UnitTypeModel.fromEntry(entry);
    }


    @Override
    public UnitTypeModel getByName(String name) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.put("name", name);
        UnitType entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("名称不存在");
        }
        return UnitTypeModel.fromEntry(entry);
    }

    @Override
    public UnitTypeModel getByCode(String code) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.put("code", code);
        UnitType entry = unique("findByCode", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UnitTypeModel.fromEntry(entry);
    }


    @Override
    public void insert(UnitTypeModel model) {
        model.setCode(StringUtils.upperCase(model.getCode()));
        setParentId(model);
        UnitType obj = new UnitType();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        UnitTypeModel unitParent = this.get(model.getParentId());
        obj.setId(id);
        obj.setPath(unitParent.getPath() + id + "/");
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(UnitTypeModel model) {
        model.setCode(StringUtils.upperCase(model.getCode()));
        setParentId(model);
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        UnitType obj = new UnitType();
        BeanUtils.copyProperties(model, obj);
        UnitTypeModel parentUnit;
        try {
            parentUnit = this.get(model.getParentId());
        } catch (Exception e) {
            parentUnit = new UnitTypeModel();
            parentUnit.setPath("/");
        }

        UnitTypeModel old = this.get(model.getId());
        String newPath = parentUnit.getPath() + obj.getId() + "/";
        String oldPath = old.getPath();
        obj.setPath(newPath);

        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        if (!model.getParentId().equals(old.getParentId())) {
            unitTypeMapper.updateChildPath(oldPath, newPath);
        }


    }

    private void setParentId(UnitTypeModel model) {
        if(StringUtils.isBlank(model.getParentId())) {
            UnitType u = unique("findByRoot");
            if(u == null) {
                throw new BusinessException("单位类型根目录不存在,请联系管理员");
            }
            model.setParentId(u.getId());
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);

            List<UnitType> child = unitTypeMapper.findNext(id);
            if (child.size() > 0) {
                throw new BusinessException("该单位类型下存在子类型，不能删除");
            } else {
                int r = super.deleteByMap(params);
                count += r;
            }
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit_type", "ut");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<UnitType>(this, "pagerModel", params, "sys/res/unitType/export.xls", "单位类型") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "UNITTYPE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UnitTypeModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model UnitTypeModel
             * @return 错误列表集合
             */
            @Override
            public List<String> extendValidate(UnitTypeModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model UnitTypeModel
             */
            @Override
            public void saveObject(UnitTypeModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "UNITTYPE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UnitTypeModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model 错误列表集合
             * @return 错误列表集合
             */
            @Override
            public List<String> extendValidate(UnitTypeModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model UnitTypeModel
             */
            @Override
            public void saveObject(UnitTypeModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public int findByUnitTypeIdCount(String unitTypeId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("unitTypeId",unitTypeId);
        return unique("findByUnitTypeIdCount", params);
    }
}
