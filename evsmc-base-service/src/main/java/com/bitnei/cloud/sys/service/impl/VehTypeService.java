package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.VehNotice;
import com.bitnei.cloud.sys.domain.VehType;
import com.bitnei.cloud.sys.model.VehTypeModel;
import com.bitnei.cloud.sys.service.IVehNoticeService;
import com.bitnei.cloud.sys.service.IVehTypeService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
 * 功能： VehTypeService实现<br>
 * 描述： VehTypeService实现<br>
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
 * <td>2018-11-14 19:24:11</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehTypeMapper")
public class VehTypeService extends BaseService implements IVehTypeService {

    @Resource
    private IVehNoticeService vehNoticeService;
    @Resource
    private DictMapper dictMapper;

    /** 根节点id **/
    private final String rootId = "0";


    @Override
    public Object tree(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehType> entries = findBySqlId("pagerModel", params);
            List<VehTypeModel> models = new ArrayList<>();
            for (VehType entry : entries) {
                models.add(VehTypeModel.fromEntry(entry));
            }

            return new TreeHandler<VehTypeModel>(models) {

                @Override
                protected TreeNode beanToTreeNode(VehTypeModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    Map<String, Object> attr = Maps.newHashMap();
                    attr.put("name", bean.getName());
                    attr.put("code", bean.getCode());
                    attr.put("orderNum", bean.getOrderNum());
                    attr.put("note", bean.getNote());
                    tn.setAttributes(attr);
                    return tn;
                }

                @Override
                protected boolean isRoot(TreeNode node) {
                    return "0".equals(node.getId());
                }
            }.toTree();

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehTypeModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                VehType obj = (VehType) entry;
                models.add(VehTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehType> entries = findBySqlId("pagerModel", params);
            List<VehTypeModel> models = new ArrayList<>();
            for (VehType entry : entries) {
                models.add(VehTypeModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehTypeModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                VehType obj = (VehType) entry;
                models.add(VehTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public VehTypeModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.put("id", id);
        VehType entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehTypeModel.fromEntry(entry);
    }


    @Override
    public VehTypeModel getByName(String name) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.put("name", name);
        VehType entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("车辆种类不存在");
        }
        return VehTypeModel.fromEntry(entry);
    }

    @Override
    public VehTypeModel getByCode(String code) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.put("code", code);
        VehType entry = unique("findByCode", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehTypeModel.fromEntry(entry);
    }


    @Override
    public void insert(VehTypeModel model) {
        model.setCode(StringUtils.upperCase(model.getCode()));
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", model.getName());
        VehType entry = unique("findByName", map);
        if (entry != null) {
            throw new BusinessException("已有相同名称的车辆类型");
        }
        //判断是否有根节点
        try {
            this.get(rootId);
        } catch (Exception e){
            VehType obj = new VehType();
            obj.setName("全部种类");
            obj.setId("0");
            obj.setOrderNum(1);
            obj.setPath("/0/");
            int res = super.insert(obj);
            if (res == 0) {
                throw new BusinessException("还未有根节点，增加根节点失败");
            }
        }
        if(StringUtils.isBlank(model.getParentId())) {
            model.setParentId(rootId);
        }
        VehTypeModel vehTypeModel = this.get(model.getParentId());
        VehType obj = new VehType();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setPath(vehTypeModel.getPath()+id+"/");
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehTypeModel model) {
        if(StringUtils.equals(model.getId(), model.getParentId())) {
            throw new BusinessException("上一级车辆种类不允许为当前种类");
        }
        model.setCode(StringUtils.upperCase(model.getCode()));
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", model.getName());
        VehType entry = unique("findByName", map);
        if (entry != null && !model.getId().equals(entry.getId())) {
            throw new BusinessException("已有相同名称的车辆类型");
        }
        // root根节点=0
        if(!rootId.equals(model.getId()) && StringUtils.isBlank(model.getParentId())) {
            model.setParentId(rootId);
        }

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        VehType obj = new VehType();
        BeanUtils.copyProperties(model, obj);
        //把父类的path+当前的id
        String path = obj.getId() + "/";
        if(!rootId.equals(model.getId())) {
            VehTypeModel vehTypeModel = this.get(model.getParentId());
            path = vehTypeModel.getPath() + path;
        }
        obj.setPath(path);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            count += deleteChild(id);
        }
        return count;
    }

    private int deleteChild(String id) {
        checkNoticeJoinType(id);
        // 删除单位类型
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.put("id", id);
        int count = super.deleteByMap(params);
        // 查询是否有字节点
        Map<String, Object> p = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        p.put("parentId", id);
        List<VehType> lists = findBySqlId("pagerModel", p);
        for (VehType vehType : lists) {
            count += deleteChild(vehType.getId());
        }
        return count;
    }

    private void checkNoticeJoinType(String id) {
        // 检查单位类型是否被关联
        Map<String, Object> map = Maps.newHashMap();
        map.put("vehTypeId", id);
        List<VehNotice> list = vehNoticeService.findList(map);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BusinessException("当前选择中包含已关联车辆种类的车辆公告，不可删除");
        }
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehType>(this, "pagerModel", params, "sys/res/vehType/export.xls", "车辆种类") {
        }.work();


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHTYPE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<VehTypeModel>(file, messageType, GroupExcelImport.class, 0 , 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehTypeModel
             * @return 错误信息
             */
            @Override
            public List<String> extendValidate(VehTypeModel model) {
                List<String> errors = Lists.newArrayList();
                // 上一级
                if(StringUtils.isNotBlank(model.getParentName())) {
                    try {
                        VehTypeModel v = getByName(model.getParentName());
                        model.setParentId(v.getId());
                    } catch (BusinessException e) {
                        errors.add("上一级车辆种类不存在");
                    }
                } else {
                    model.setParentId(rootId);
                }
                // 种类名称
                Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_type", "vt");
                if(StringUtils.isNotBlank(model.getName())) {
                    params.put("name", model.getName());
                    VehType entry = unique("findByName", params);
                    if(entry != null) {
                        errors.add("种类名称已存在");
                    }
                }
                // 值(CODE)
                if(StringUtils.isNotBlank(model.getCode())) {
                    params.put("code", model.getCode());
                    VehType t = unique("findByCode", params);
                    if(t != null) {
                        errors.add("值(CODE)已存在");
                    }
                }
                // 种类性质
                if(StringUtils.isNotBlank(model.getAttrClsDisplay())) {
                    Map<String, Object> typeParams = ImmutableMap.of("type", "VEH_TYPE_CLS", "name", model.getAttrClsDisplay());
                    Dict dict = dictMapper.getByTypeAndName(typeParams);
                    if(dict != null) {
                        model.setAttrCls(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("种类性质填写错误");
                    }
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model VehTypeModel
             */
            @Override
            public void saveObject(VehTypeModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "VEHTYPE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<VehTypeModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model VehTypeModel
             * @return 错误信息
             */
            @Override
            public List<String> extendValidate(VehTypeModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model VehTypeModel
             */
            @Override
            public void saveObject(VehTypeModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("车辆种类导入模板.xls" , VehTypeModel.class);
    }


}
