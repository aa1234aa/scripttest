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
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.InstructCategory;
import com.bitnei.cloud.sys.model.CategorysAndInstructsDto;
import com.bitnei.cloud.sys.model.InstructCategoryModel;
import com.bitnei.cloud.sys.model.InstructManagementModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.service.IInstructCategoryService;
import com.bitnei.cloud.sys.service.IInstructManagementService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructCategoryService实现<br>
 * 描述： InstructCategoryService实现<br>
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
 * <td>2019-03-11 14:20:16</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.InstructCategoryMapper")
@RequiredArgsConstructor
public class InstructCategoryService extends BaseService implements IInstructCategoryService {

    private final IUserService userService;

    private final IInstructManagementService instructManagementService;

    private final IVehModelService vehModelService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<InstructCategory> entries = findBySqlId("pagerModel", params);
            List<InstructCategoryModel> models = new ArrayList();
            for (InstructCategory entry : entries) {
                InstructCategory obj = (InstructCategory) entry;
                models.add(InstructCategoryModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<InstructCategoryModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                InstructCategory obj = (InstructCategory) entry;
                models.add(InstructCategoryModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<CategorysAndInstructsDto> categorysAndInstructs() {

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");

        List<InstructCategory> entries = findBySqlId("pagerModel", params);

        List<VehModelModel> vehModelModels = vehModelService.getAll();

        //一次查全部，在内存分组，避免多次访问数据库
        List<InstructManagementModel> instructManagementModels = instructManagementService.normalList(
                null, vehModelModels);

        Map<String, List<InstructManagementModel>> categoryIdManagementMap = new HashMap<>();
        instructManagementModels.forEach(it -> {
            List<InstructManagementModel> currentList;
            if (categoryIdManagementMap.containsKey(it.getInstructCategoryId())) {
                currentList = categoryIdManagementMap.get(it.getInstructCategoryId());
            } else {
                currentList = new ArrayList<>();
                categoryIdManagementMap.put(it.getInstructCategoryId(), currentList);
            }
            currentList.add(it);
        });

        return entries.stream().map(it -> {
            CategorysAndInstructsDto categorysAndInstructsDto = new CategorysAndInstructsDto();
            BeanUtils.copyProperties(it, categorysAndInstructsDto);
            if (categoryIdManagementMap.containsKey(it.getId())) {
                categorysAndInstructsDto.setInstructs(categoryIdManagementMap.get(it.getId()));
            }
            return categorysAndInstructsDto;
        }).collect(Collectors.toList());

    }

    @Override
    public InstructCategoryModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");
        params.put("id", id);
        InstructCategory entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return InstructCategoryModel.fromEntry(entry);
    }


    @Override
    public void insert(InstructCategoryModel model) {

        InstructCategory obj = new InstructCategory();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateById(userService.findByUsername(ServletUtil.getCurrentUser()).getId());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(InstructCategoryModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");

        InstructCategory obj = new InstructCategory();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
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
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");

        String[] arr = ids.split(",");
        int count = 0;

        List<VehModelModel> vehModelModels = vehModelService.getAll();

        for (String id : arr) {

            InstructCategoryModel instructCategoryModel = get(id);

            //删除关联的控制命令
            List<InstructManagementModel> managementModels =
                    instructManagementService.normalList(instructCategoryModel.getId(), vehModelModels);
            managementModels.forEach(it -> instructManagementService.deleteMulti(it.getId()));

            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_category", "icate");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<InstructCategory>(this, "pagerModel", params, "sys/res/instructCategory/export.xls", "控制命令种类") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "INSTRUCTCATEGORY" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<InstructCategoryModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructCategoryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructCategoryModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "INSTRUCTCATEGORY" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<InstructCategoryModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructCategoryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructCategoryModel model) {
                update(model);
            }
        }.work();

    }


}
