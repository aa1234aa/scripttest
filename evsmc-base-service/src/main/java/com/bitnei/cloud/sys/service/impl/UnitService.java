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
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.UnitMapper;
import com.bitnei.cloud.sys.domain.Unit;
import com.bitnei.cloud.sys.domain.UnitTypeLk;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.model.UnitTypeModel;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.service.IUnitTypeService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UnitService实现<br>
 * 描述： UnitService实现<br>
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
 * <td>2018-11-05 17:33:20</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author zxz
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UnitMapper")
public class UnitService extends BaseService implements IUnitService {

    @Resource
    private UnitMapper unitMapper;
    @Resource
    private IUnitTypeService unitTypeService;

    private final String rootId = "0";

    private final String unitIds = "unitIds";

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    @Override
    public Object list(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.containsKey(unitIds)) {
            String vehModelIds = (String) params.get(unitIds);
            params.put("unitIds", StringUtils.split(vehModelIds, ","));
        }
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() <=0) {
            List<Unit> entries = findBySqlId("pagerModel", params);
            List<UnitModel> models = new ArrayList<>();
            for (Unit entry : entries) {
                models.add(UnitModel.fromEntry(entry));
            }
            return models;
        }
        // 分页查询
        else {
            return findPagerModel(pagerInfo, params);
        }
    }

    @Override
    public List<UnitModel> findList(Map<String, Object> map) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.putAll(map);
        if (params.containsKey(unitIds)) {
            String vehModelIds = (String) params.get(unitIds);
            params.put("unitIds", StringUtils.split(vehModelIds, ","));
        }
        List<Unit> entries = findBySqlId("pagerModel", params);
        List<UnitModel> models = new ArrayList<>();
        for (Unit entry : entries) {
            models.add(UnitModel.fromEntry(entry));
        }
        return models;
    }

    private PagerResult findPagerModel(PagerInfo pagerInfo, Map<String, Object> params) {
        PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
        List<UnitModel> models = new ArrayList<>();
        for (Object entry : pr.getData()) {
            Unit obj = (Unit) entry;
            models.add(UnitModel.fromEntry(obj));
        }
        pr.setData(Collections.singletonList(models));
        return pr;
    }

    @Override
    public Object tree(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<Unit> entries = findBySqlId("findTreeList", params);
            List<UnitModel> models = new ArrayList<>();
            for (Unit entry : entries) {
                models.add(UnitModel.fromEntry(entry));
            }

            //如果为空，转化树会报错再此处判断
            if (CollectionUtils.isEmpty(models)) {
                ResultObjMsg msg = new ResultObjMsg();
                msg.setData(null);
                return msg;
            }
            return new TreeHandler<UnitModel>(models) {
                @Override
                protected TreeNode beanToTreeNode(UnitModel bean) {
                    TreeNode tn = new TreeNode();
                    tn.setId(bean.getId());
                    tn.setParentId(bean.getParentId());
                    tn.setLabel(bean.getName());
                    Map<String, Object> attr = new HashMap<>(5);
                    attr.put("nickName", bean.getNickName());
                    tn.setAttributes(attr);
                    return tn;
                }

                @Override
                protected boolean isRoot(TreeNode treeNode) {
                    return rootId.equals(treeNode.getId());
                }
            }.toTree();
        }
        // 分页查询
        else {
            return findPagerModel(pagerInfo, params);
        }
    }

    /**
     * 根据id获取
     *
     * @return UnitModel
     */
    @Override
    public UnitModel get(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("id", id);
        Unit entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UnitModel.fromEntry(entry);
    }

    /**
     * 根据单位名称获取
     *
     * @return UnitModel
     */
    @Override
    public UnitModel getByName(String name) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("name", name);
        Unit entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("单位名称不存在");
        }
        return UnitModel.fromEntry(entry);
    }

    /**
     * 根据单位名称获取单位列表,支持模糊查询
     *
     * @return UnitModel集合
     */
    @Override
    public List<UnitModel> getListByName(String name) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("name", name);
        List<Unit> entries = findBySqlId("findListByName", params);
        List<UnitModel> models = new ArrayList<>();
        for (Unit entry : entries) {
            models.add(UnitModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 根据单位ID获取单位列表,支持模糊查询
     *
     * @return UnitModel集合
     */
    @Override
    public List<UnitModel> getListById(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("id", id);
        List<Unit> entries = findBySqlId("findListById", params);
        List<UnitModel> models = new ArrayList<>();
        for (Unit entry : entries) {
            models.add(UnitModel.fromEntry(entry));
        }
        return models;
    }

    /**
     * 根据单位简称获取
     *
     * @return UnitModel
     */
    @Override
    public UnitModel getByNickName(String nickName) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("nickName", nickName);
        Unit entry = unique("findByNickName", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UnitModel.fromEntry(entry);
    }

    /**
     * 根据统一社会信用代码获取
     *
     * @return UnitModel
     */
    @Override
    public UnitModel getByOrganizationCode(String organizationCode) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("organizationCode", organizationCode);
        Unit entry = unique("findByOrganizationCode", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UnitModel.fromEntry(entry);
    }

    /**
     * 新增
     *
     * @param model 新增model
     */
    @Override
    public void insert(UnitModel model) {
        //校验坐标
        String message = lngAndlatVerify(model.getLng(), model.getLat());
        if(!message.equals("")){
            throw new BusinessException(message);
        }

        Unit obj = new Unit();
        if(StringUtils.isEmpty(model.getOrganizationCode())) {
            model.setOrganizationCode(null);
        }
        if(StringUtils.isEmpty(model.getParentId())) {
            model.setParentId(rootId);
        }
        Long countOfName = unitMapper.countOfName("", model.getName());
        if (countOfName > 0){
            throw new BusinessException("名称已存在");
        }
        // 单位简称
        if(validationNickName(null, model.getNickName())){
            throw new BusinessException("单位简称已存在");
        }
        // 统一社会信用代码
        if(validationCode(null, model.getOrganizationCode())) {
            throw new BusinessException("统一社会信用代码已存在");
        }
        BeanUtils.copyProperties(model, obj);
        try {
            this.get(rootId);
        } catch (Exception e) {
            Unit unit = new Unit();
            unit.setId(rootId);
            unit.setName("全部单位");
            unit.setPath("/"+rootId+"/");
            unit.setIsRoot(Constant.TrueAndFalse.TRUE);
            unit.setAddress("默认地址");
            unit.setContactorName("默认值");
            unit.setContactorPhone("123456");
            unit.setContactorAddress("默认地址");
            insert(unit);
        }
        //获取parentId的path
        UnitModel unitModel = this.get(model.getParentId());
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setPath(unitModel.getPath() + id + "/");
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setIsRoot(Constant.TrueAndFalse.FALSE);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        // 插入单位-单位类型关联
        model.setId(obj.getId());
        insertUnitTypeLks(model);
    }

    private boolean validationNickName(String id, String nickName){
        boolean flag = false;
        if(StringUtils.isNotEmpty(nickName)) {
            Long count = unitMapper.countOfNickName(id, nickName);
            if(count > 0) {
                flag = true;
            }
        }
        return flag;
    }

    private boolean validationCode(String id, String code){
        boolean flag = false;
        if(StringUtils.isNotEmpty(code)) {
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
            params.put("organizationCode", code);
            Unit entry = unique("findByOrganizationCode", params);
            if(entry != null && !entry.getId().equals(id)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    @Override
    public void update(UnitModel model) {
        if(StringUtils.equals(model.getId(), model.getParentId())) {
            throw new BusinessException("上级单位不允许为当前单位");
        }
        //判断名称是否存在
        Long countOfName = unitMapper.countOfName(model.getId(), model.getName());
        if (countOfName > 0){
            throw new BusinessException("名称已存在");
        }
        //校验坐标
        String message = lngAndlatVerify(model.getLng(), model.getLat());
        if(!message.equals("")){
            throw new BusinessException(message);
        }
        // 单位简称
        if(validationNickName(model.getId(), model.getNickName())){
            throw new BusinessException("单位简称已存在");
        }
        // 统一社会信用代码
        if(validationCode(model.getId(), model.getOrganizationCode())) {
            throw new BusinessException("统一社会信用代码已存在");
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        Unit obj = new Unit();
        if(StringUtils.isEmpty(model.getOrganizationCode())) {
            model.setOrganizationCode(null);
        }
        if(StringUtils.isEmpty(model.getParentId())) {
            model.setParentId(rootId);
        }
        BeanUtils.copyProperties(model, obj);

        //查询原始的父级path
        UnitModel parentUnit = this.get(model.getParentId());
        UnitModel old = this.get(model.getId());

        String newPath = parentUnit.getPath() + obj.getId() + "/";
        obj.setPath(newPath);

        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        // 删除单位-单位类型关联
        delUnitTypeLks(obj);
        // 插入单位-单位类型关联
        if (StringUtils.isEmpty(model.getCreateBy())) {
            model.setCreateBy(obj.getUpdateBy());
        }
        if (StringUtils.isEmpty(model.getCreateTime())) {
            model.setCreateTime(model.getUpdateTime());
        }
        insertUnitTypeLks(model);

        if (!model.getParentId().equals(old.getParentId())) {

            Map<String, Object> pathMap = Maps.newHashMap();
            pathMap.put("oldPath", old.getPath());
            pathMap.put("newPath", newPath);
            int pathRes = super.sessionTemplate.update(getSqlId("updateChildPath"), pathMap);
            //有修改但返回0
            if (pathRes < 0) {
                throw new BusinessException("修改节点路径失败");
            }
        }
    }

    /**
     * 删除多个
     *
     * @param ids id集合
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        Map<String, Object> delParams = DataAccessKit.getAuthMap("sys_unit", "un");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("path", id);
            List<Unit> entries = findBySqlId("findTreeList", params);
            for (Unit e : entries) {
                delParams.put("id", e.getId());
                int r = super.deleteByMap(delParams);
                count += r;
            }
        }
        return count;
    }

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<Unit>(this, "pagerModel", params, "sys/res/unit/export.xls", "单位列表") {
        }.work();
    }

    /**
     * 批量导入
     *
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "UNIT" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<UnitModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model demol
             * @return 错误集合
             */
            @Override
            public List<String> extendValidate(UnitModel model) {
                List<String> errors = Lists.newArrayList();
                // 单位名称
                Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
                if(StringUtils.isNotBlank(model.getName())) {
                    params.put("name", model.getName());
                    Unit entry = unique("findByName", params);
                    if(entry != null) {
                        errors.add("单位名称已存在");
                    }
                }
                // 单位简称
                if(validationNickName(null, model.getNickName())){
                    errors.add("单位简称已存在");
                }
                // 单位类型
                if(StringUtils.isNotBlank(model.getUnitTypeNames())) {
                    String[] arr = StringUtils.split(model.getUnitTypeNames(), ",");
                    for (String s : arr) {
                        try {
                            UnitTypeModel m = unitTypeService.getByName(s);
                            if(StringUtils.isNotBlank(model.getUnitTypeIds())) {
                                model.setUnitTypeIds(model.getUnitTypeIds() + "," + m.getId());
                            } else {
                                model.setUnitTypeIds(m.getId());
                            }

                        } catch (BusinessException e) {
                            errors.add(String.format("单位类型[%s]不存在", s));
                        }
                    }
                }
                // 上级单位
                if(StringUtils.isNotBlank(model.getParentName())) {
                    try {
                        UnitModel parentUnit = getByName(model.getParentName());
                        model.setParentId(parentUnit.getId());
                    } catch (BusinessException e) {
                        errors.add("上级单位不存在");
                    }
                } else {
                    model.setParentId("0");
                }
                // 统一社会信用码
                if(validationCode(null, model.getOrganizationCode())) {
                    errors.add("统一社会信用代码已存在");
                }
                //校验坐标
                String message = lngAndlatVerify(model.getLng(), model.getLat());
                if(!"".equals(message)){
                    errors.add(message);
                }
                return errors;
            }

            /**
             *  保存实体
             * @param model model
             */
            @Override
            public void saveObject(UnitModel model) {
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
        String messageType = "UNIT" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<UnitModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             */
            @Override
            public List<String> extendValidate(UnitModel model) {
                return null;
            }

            /**
             *  保存实体
             */
            @Override
            public void saveObject(UnitModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 自定义参数查询联系人-无权限
     *
     * @param params 参数
     * @return Unit
     */
    @Override
    public Unit queryUnitByParam(Map<String, Object> params) {
        List<Unit> list = findBySqlId("pagerModel", params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 插入单位类型关联
     * @param model model
     */
    private void insertUnitTypeLks(UnitModel model) {
        if (!StringUtils.isEmpty(model.getUnitTypeIds())) {
            List<UnitTypeLk> list = new ArrayList<>();
            String splitChar = ",";
            for (String s : model.getUnitTypeIds().split(splitChar)) {
                UnitTypeLk lk = new UnitTypeLk();
                lk.setId(UtilHelper.getUUID());
                lk.setUnitId(model.getId());
                lk.setUnitTypeId(s);
                lk.setCreateBy(model.getCreateBy());
                lk.setCreateTime(model.getCreateTime());
                list.add(lk);
            }
            int res = super.sessionTemplate.insert(getSqlId("insertUnitTypeLk"), list);
            if (res == 0) {
                throw new BusinessException("新增失败");
            }
        }
    }

    /**
     * 删除单位-单位类型关联
     *
     * @param unit Unit
     */
    private void delUnitTypeLks(Unit unit) {
        if (null != unit) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("unitId", unit.getId());
            super.delete("delUnitTypeLkByUnitId", params);
        }
    }

    @Override
    public String validateNameCode(String name, String code) {
        // 校验单位是否存在
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
        params.put("name", name);
        Unit entry = unique("findByName", params);
        if(entry == null) {
            throw new BusinessException("单位名称不存在", 1001);
        }
        // 校验单位类型编码是否匹配
        Map<String, Object> p = ImmutableMap.of("unitId", entry.getId(), "code", code);
        int count = unitMapper.findByUintIdCodeCount(p);
        if(count > 0) {
            return entry.getId();
        } else {
            throw new BusinessException("单位类型不一致", 1002);
        }
    }


    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("单位导入模板.xls" , UnitModel.class);
    }

    /**
     * lng 经度
     * lat 纬度
     * 经纬度格式校验，因为更新操作时不能修改经纬度。所以不能使用spring的校验
     * @return 如果失败：返回错误信息。如果检验成功 返回空字符串
     */
    public String lngAndlatVerify(String lng,String lat){
        StringBuffer error=new StringBuffer();
        if(StringUtils.isBlank(lng) && StringUtils.isBlank(lat)){
            return error.toString();
        }
        if(StringUtils.isBlank(lng) || StringUtils.isBlank(lat)){
            error.append(" 经纬度必须同时存在。 ");
            return error.toString();
        }
        try {
            Double ln = Double.valueOf(lng);
            if(ln==null||ln>180||ln<-180){
                error.append("  经度只能为数字并且范围在-180到180之间。");
            }
        }catch (Exception e){
            error.append("  经度只能为数字并且范围在-180到180之间。");
        }
        try {
            Double la = Double.valueOf(lat);
            if(la==null||la>90||la<-90){
                error.append("  纬度只能为数字并且范围在-90到90之间。");
            }
        }catch (Exception e){
            error.append("  纬度只能为数字并且范围在-90到90之间。");
        }
        return error.toString();
    }
}
