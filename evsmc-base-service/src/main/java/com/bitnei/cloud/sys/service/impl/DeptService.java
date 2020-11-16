package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultObjMsg;
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
import com.bitnei.cloud.sys.domain.Dept;
import com.bitnei.cloud.sys.model.DeptModel;
import com.bitnei.cloud.sys.service.IDeptService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DeptService实现<br>
 * 描述： DeptService实现<br>
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
 * <td>2018-11-07 14:11:13</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.DeptMapper")
public class DeptService extends BaseService implements IDeptService {


    /**
     * 全部查询
     * @return 结果集
     */
    @Override
    public Object list(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<Dept> entries = findBySqlId("pagerModel", params);
            List<DeptModel> models = new ArrayList<>();
            for (Dept entry : entries) {
                models.add(DeptModel.fromEntry(entry));
            }
            return models;
        }
        // 分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DeptModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                Dept obj = (Dept) entry;
                models.add(DeptModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    @Override
    public Object tree(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<Dept> entries = findBySqlId("findTreeList", params);
            List<DeptModel> models = new ArrayList<>();
            for (Dept entry : entries) {
                models.add(DeptModel.fromEntry(entry));
            }

            //如果为空，转化树会报错再此处判断
            if(CollectionUtils.isEmpty(models)){
                ResultObjMsg msg = new ResultObjMsg();
                msg.setData(null);
                return msg;
            }
            return new TreeHandler<DeptModel>(models) {
                @Override
                protected TreeNode beanToTreeNode(DeptModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    return tn;
                }
                @Override
                protected boolean isRoot(TreeNode treeNode) {
                    return "0".equals(treeNode.getId());
                }
            }.toTree();
        }
        // 分页查询
        else {
            PagerResult pr = findPagerModel("findTreeList", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DeptModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                Dept obj = (Dept) entry;
                models.add(DeptModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据id获取
     * @return DeptModel
     */
    @Override
    public DeptModel get(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("id", id);
        Dept entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        DeptModel deptModel = DeptModel.fromEntry(entry);
        //统计用户数量
        deptModel.setUserCount(statDeptUserCount(deptModel.getId()));
        return deptModel;
    }

    /**
     * 新增
     * @param model 新增model
     */
    @Override
    public void insert(DeptModel model) {
        Dept obj = new Dept();
        BeanUtils.copyProperties(model, obj);
        String message = checkDeptName(obj.getName(), "", model.getUnitId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        try {
            this.get("0");
        }catch (Exception e){
            Dept dept = new Dept();
            dept.setId("0");
            dept.setPath("/0/");
            dept.setName("全部机构");
            dept.setUnitId("");
            insert(dept);
        }
        //获取parentId的path
        DeptModel deptModel = this.get(model.getParentId());
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setPath(deptModel.getPath()+id+"/");
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    /**
     * 编辑
     * @param model 编辑model
     */
    @Override
    public void update(DeptModel model) {
        String message = checkDeptName(model.getName(), model.getId(), model.getUnitId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        Dept obj = new Dept();
        BeanUtils.copyProperties(model, obj);

        //查询原始的父级path
        DeptModel parentUnit = this.get(model.getParentId());
        DeptModel old = this.get(model.getId());

        String newPath = parentUnit.getPath()+obj.getId()+"/";
        obj.setPath(newPath);

        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        if(!model.getParentId().equals(old.getParentId())){
            Map<String, Object> pathMap = Maps.newHashMap();
            pathMap.put("oldpath",old.getPath());
            pathMap.put("newPath",newPath);
            int pathRes = super.sessionTemplate.update(getSqlId("updateChildPath"), pathMap);
            if (pathRes == 0) {
                throw new BusinessException("修改节点路径失败!");
            }
        }
    }


    /**
     * 导出
     * count
     *
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {
        // 获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<Dept>(this, "pagerModel", params, "sys/res/dept/export.xls", "组织机构") {
        }.work();
        return;
    }

    /**
     * 批量导入
     *
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "DEPT" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<DeptModel>(file, messageType, GroupExcelImport.class) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(DeptModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(DeptModel model) {
                insert(model);
            }
        }.work();
    }

    /**
     * 批量更新
     *
     * @param file 文件
     */
    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "DEPT" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<DeptModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model model
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(DeptModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(DeptModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 自定义参数获取组织架构信息-无权限
     * @param params 参数
     * @return deptModel
     */
    @Override
    public DeptModel queryDeptByParam(Map<String, Object> params) {
        List<Dept> list = findBySqlId("pagerModel", params);
        if (null != list && list.size() > 0 && null != list.get(0)) {
            return DeptModel.fromEntry(list.get(0));
        }
        return null;
    }

    /**
     * 校验组织架构名称
     * @param name 名称
     * @param id id
     * @return 错误信息. null为校验通过
     */
    private String checkDeptName(String name, String id, String unitId) {
        if (!StringUtils.isEmpty(name)) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("nameEq", name);
            params.put("unitId", unitId);
            DeptModel dept = queryDeptByParam(params);
            boolean existed = (null != dept) && (StringUtils.isEmpty(id) || !dept.getId().equals(id));
            if (existed) {
                return "节点名称已存在";
            }
        }
        return "";
    }

    /**
     * 查询联系人单位下组织架构
     * @param opId 联系人id
     * @return List<DeptModel>
     */
    @Override
    public List<DeptModel> queryDeptsByOpid(String opId) {
        if (StringUtils.isEmpty(opId)) {
            return null;
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("opId", opId);
        // 非分页查询
        List<Dept> entries = findBySqlId("queryDeptsByOpid", params);
        if (null != entries && entries.size() > 0 && null != entries.get(0)) {
            List<DeptModel> models = new ArrayList<>();
            for (Dept entry : entries) {
                models.add(DeptModel.fromEntry(entry));
            }
            return models;
        }
        return null;
    }


    /**
     * 根据单位名称或简称查询组织架构
     * @param unitName 单位名称或简称
     * @return TreeNode
     */
    @Override
    public TreeNode queryDeptsByUnitName(String unitName) {
        if (StringUtils.isEmpty(unitName)) {
            return null;
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("unitName", unitName);
        // 非分页查询
        List<Dept> entries = findBySqlId("queryDeptsByUnitName", params);
        if (null != entries && entries.size() > 0 && null != entries.get(0)) {
            List<DeptModel> models = new ArrayList<>();
            for (Dept entry : entries) {
                models.add(DeptModel.fromEntry(entry));
            }
            return listToTree(models);
        }
        return null;
    }

    /**
     * 组织结构列表转树形结构
     * @param models 组织结构列表
     * @return TreeNode
     */
    private TreeNode listToTree(List<DeptModel> models) {
        return new TreeHandler<DeptModel>(models) {
            @Override
            protected TreeNode beanToTreeNode(DeptModel bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                Map<String, Object> attr = new HashMap<>();
                attr.put("name", bean.getName());
                attr.put("unitName", bean.getUnitName());
                attr.put("note", bean.getNote());
                tn.setAttributes(attr);
                return tn;
            }

            @Override
            protected boolean isRoot(TreeNode node) {
                return (StringUtil.isEmpty(node.getParentId()) || "-1".equalsIgnoreCase(node.getParentId()));
            }
        }.toTree();
    }

    /**
     * 统计部门及子节点用户数量
     * @param deptId 部门ID
     * @return 部门用户数量
     */
    @Override
    public Integer statDeptUserCount(String deptId) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("deptId", deptId);
        return unique("statDeptUserCount", params);
    }

    /**
     * 删除单个节点
     * @param id 节点id
     */
    @Override
    public void deleteDept(String id) {
        // 删除当前节点
        deleteDeptById(id);
        //查询所有子节点
        List<Dept> depts = queryChildrenById(id);
        if (!CollectionUtils.isEmpty(depts)) {
            for (Dept dt : depts) {
                deleteDeptById(dt.getId());
            }
        }
    }

    /**
     * 查询子节点
     * @param deptId 部门ID
     * @return List<Dept>
     */
    private List<Dept> queryChildrenById(String deptId) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("deptId", deptId);
        return findBySqlId("queryChildren", params);
    }

    /**
     * 删除单个节点
     * @param id 节点id
     * @return 影响行数
     */
    private int deleteDeptById(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_dept", "dept");
        params.put("id", id);
        return super.deleteByMap(params);
    }
}
