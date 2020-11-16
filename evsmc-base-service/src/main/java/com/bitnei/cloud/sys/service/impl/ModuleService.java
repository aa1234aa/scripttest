package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.config.RunEnv;
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
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.ModuleMapper;
import com.bitnei.cloud.sys.domain.Module;
import com.bitnei.cloud.sys.model.ModuleModel;
import com.bitnei.cloud.sys.service.IModuleDataItemService;
import com.bitnei.cloud.sys.service.IModuleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
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
 * 功能： ModuleService实现<br>
 * 描述： ModuleService实现<br>
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
 * <td>2018-12-10 17:33:28</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.ModuleMapper")
public class ModuleService extends BaseService implements IModuleService {


    @Resource
    private IModuleDataItemService moduleDataItemService;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private RunEnv runEnv;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<Module> entries = findBySqlId("pagerModel", params);
            List<ModuleModel> models = new ArrayList<>();
            for (Module entry : entries) {
                models.add(ModuleModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<ModuleModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                Module obj = (Module) entry;
                models.add(ModuleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public ModuleModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        params.put("id", id);
        Module entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return ModuleModel.fromEntry(entry);
    }


    @Override
    public ModuleModel getByCode(String code) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        params.put("code", code);
        Module entry = unique("findByCode", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return ModuleModel.fromEntry(entry);
    }


    @Override
    public void insert(ModuleModel model) {

        boolean isRoot = model.getIsRoot() == null || model.getIsRoot() == 0;
        boolean isParent = model.getParentId() == null || "".equalsIgnoreCase(model.getParentId());
        if (isRoot && isParent) {
            throw new BusinessException("上级节点不能为空");
        }
        validateModel(model);
        Module obj = new Module();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        //获取上级节点的path
        String path;
        if (StringUtil.isNotEmpty(model.getParentId())) {
            //获取当权限的map
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
            params.put("id", model.getParentId());
            Module parentModel = moduleMapper.findById(params);
            path = parentModel.getPath() + id + "/";
        } else {
            path = "/" + id + "/";
        }
        obj.setPath(path);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    private void validateModel(ModuleModel model) {
        boolean isFun = model.getIsFun().equals(Constant.TrueAndFalse.FALSE);
        if(isFun) {
            Module entry = moduleMapper.getByNamaAndParentId(model.getParentId(),model.getName());
            if(entry != null && !entry.getId().equals(model.getId())) {
                throw new BusinessException("同一级别不能存在名称相同的节点");
            }
        }
        if(StringUtils.isNotBlank(model.getCode())) {
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
            params.put("code", model.getCode());
            Module entry = unique("findByCode", params);
            if(entry != null && !entry.getId().equals(model.getId())) {
                throw new BusinessException("权限编码已存在");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ModuleModel model) {

        boolean isRoot = model.getIsRoot() == null || model.getIsRoot() == 0;
        boolean isParent = model.getParentId() == null || "".equalsIgnoreCase(model.getParentId());
        if (isRoot && isParent) {
            throw new BusinessException("上级节点不能为空");
        }
        validateModel(model);
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        Module obj = new Module();
        BeanUtils.copyProperties(model, obj);

        //设置自身path
        String newPath = "";
        if (StringUtil.isNotEmpty(model.getParentId())) {
            //获取当权限的map
            Map<String, Object> queryParams = DataAccessKit.getAuthMap("sys_module", "m");
            queryParams.put("id", model.getParentId());
            Module parentModel = moduleMapper.findById(queryParams);
            newPath = parentModel.getPath() + model.getId() + "/";
        }
        //获取旧path
        String oldPath;
        {
            //获取当权限的map
            Map<String, Object> queryParams = DataAccessKit.getAuthMap("sys_module", "m");
            queryParams.put("id", model.getId());
            Module oldModule = moduleMapper.findById(queryParams);
            oldPath = oldModule.getPath();
        }

        obj.setPath(newPath);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        } else {
            if (StringUtil.isNotEmpty(oldPath)) {
                Map<String, Object> updateMap = Maps.newHashMap();
                updateMap.put("oldPath", oldPath);
                updateMap.put("newPath", newPath);
                moduleMapper.replacePath(updateMap);
            }

        }
    }

    /**
     * 删除多个
     *
     * @param ids ids
     * @return int
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Module>(this, "pagerModel", params, "sys/res/module/export.xls", "模块管理") {
        }.work();

    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "MODULE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<ModuleModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model ModuleModel
             * @return 错误信息集合
             */
            @Override
            public List<String> extendValidate(ModuleModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model ModuleModel
             */
            @Override
            public void saveObject(ModuleModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "MODULE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<ModuleModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model ModuleModel
             * @return 错误信息集合
             */
            @Override
            public List<String> extendValidate(ModuleModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model ModuleModel
             */
            @Override
            public void saveObject(ModuleModel model) {
                update(model);
            }
        }.work();

    }


    /**
     * 删除模块
     *
     * @param id 节点id
     */
    @Override
    public void deleteModuleById(String id) {
        deleteChildrenModule(id);
        delete(id);
        //删除关联列数据项
        moduleDataItemService.deleteMulti(id);
    }

    /**
     * 删除子节点
     *
     * @param id 节点id
     */
    private void deleteChildrenModule(String id) {
        //查询所有子节点
        List<Module> modules = queryChildrenById(id);
        if (modules == null) {
            deleteChildrenModule(id);
        } else {
            for (Module md : modules) {
                delete(md.getId());
                //删除关联列数据项
                moduleDataItemService.deleteMulti(md.getId());
            }
        }
    }

    /**
     * 查询子节点
     * @param id id
     * @return List<Module>
     */
    private List<Module> queryChildrenById(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "md");
        params.put("id", id);
        return findBySqlId("queryChildren", params);
    }

    /**
     * 查询系统所有模块
     * @param pagerInfo PagerInfo
     * @return TreeNode
     */
    @Override
    public TreeNode queryAllModules(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_module", "m");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //生产现场隐藏菜单
        if (runEnv.getAppMode().equalsIgnoreCase(RunEnv.PROD_MODE)){
            params.put("isHidden", 0);
        }
        List<Module> entries = findBySqlId("pagerModel", params);
        if (null != entries && entries.size() > 0 && null != entries.get(0)) {
            List<ModuleModel> models = new ArrayList<>();
            for (Module entry : entries) {
                models.add(ModuleModel.fromEntry(entry));
            }
            return listToTree(models);
        }
        return null;
    }

    /**
     * 系统模块转树形结构
     *
     * @param models List<ModuleModel>
     * @return TreeNode
     */
    private TreeNode listToTree(List<ModuleModel> models) {
        return new TreeHandler<ModuleModel>(models) {
            @Override
            protected TreeNode beanToTreeNode(ModuleModel bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                Map<String, Object> attr = Maps.newHashMap();
                attr.put("name", bean.getName());
                attr.put("parent_name", bean.getParentName());
                attr.put("code", bean.getCode());
                attr.put("is_root", bean.getIsRoot());
                attr.put("is_fun", bean.getIsFun());
                attr.put("path", bean.getPath());
                attr.put("icon", bean.getIcon());
                attr.put("action", bean.getAction());
                attr.put("is_fullscreen", bean.getIsFullscreen());
                attr.put("order_num", bean.getOrderNum());
                attr.put("is_hidden", bean.getIsHidden());
                tn.setAttributes(attr);
                return tn;
            }
            @Override
            protected boolean isRoot(TreeNode node) {
                return StringUtil.isEmpty(node.getParentId()) || "-1".equalsIgnoreCase(node.getParentId());
            }
        }.toTree();
    }

}
