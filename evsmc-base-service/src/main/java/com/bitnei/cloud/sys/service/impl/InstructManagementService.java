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
import com.bitnei.cloud.sys.domain.InstructManagement;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.IInstructManagementModelService;
import com.bitnei.cloud.sys.service.IInstructManagementService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructManagementService实现<br>
 * 描述： InstructManagementService实现<br>
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
 * <td>2019-03-11 15:53:11</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.InstructManagementMapper")
@RequiredArgsConstructor
public class InstructManagementService extends BaseService implements IInstructManagementService {

    private final IUserService userService;
    private final IInstructManagementModelService managementModelService;
    private final IVehModelService vehModelService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        Object vehModelId = params.getOrDefault("vehModelId", null);
        if (null != vehModelId) {
            List<InstructManagementModelModel> managementModelModels =
                    managementModelService.getByVehModelId(vehModelId.toString());
            if (CollectionUtils.isNotEmpty(managementModelModels)) {
                params.put("ids", managementModelModels.stream()
                        .map(InstructManagementModelModel::getInstructManagementId)
                        .collect(Collectors.toList()));
            } else params.put("ids", Collections.singletonList("-1"));
        }

        List<VehModelModel> vehModelModels = vehModelService.getAll();

        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<InstructManagement> entries = findBySqlId("pagerModel", params);
            List<InstructManagementModel> models = new ArrayList();
            for (InstructManagement entry : entries) {
                InstructManagement obj = (InstructManagement) entry;
                models.add(InstructManagementModel.fromEntry(obj));
                dealVehModelIdAndName(models, vehModelModels);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<InstructManagementModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                InstructManagement obj = (InstructManagement) entry;
                models.add(InstructManagementModel.fromEntry(obj));
                dealVehModelIdAndName(models, vehModelModels);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<InstructManagementModel> normalList(String instructCategoryId, List<VehModelModel> vehModelModels) {

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");
        params.put("instructCategoryId", instructCategoryId);

        List<InstructManagement> entries = findBySqlId("normalPagerModel", params);
        List<InstructManagementModel> models = new ArrayList();

        for (InstructManagement entry : entries) {
            InstructManagement obj = (InstructManagement) entry;
            models.add(InstructManagementModel.fromEntry(obj));
            dealVehModelIdAndName(models, vehModelModels);
        }
        return models;
    }


    /**
     * 补充vehModelName，车型的名称
     * @param models
     */
    private void dealVehModelIdAndName(List<InstructManagementModel> models, List<VehModelModel> vehModelModels) {

        if (CollectionUtils.isEmpty(vehModelModels)) {
            vehModelModels = vehModelService.getAll();
        }

        Map<String, VehModelModel> idVehModelMap = vehModelModels.stream()
                .collect(Collectors.toMap(VehModelModel::getId, it -> it));


        for (InstructManagementModel model: models) {
            if (StringUtils.isEmpty(model.getVehModelIdStrs())) continue;

            String[] idsStr = model.getVehModelIdStrs().split(",");
            List<String> ids = Arrays.asList(idsStr);
            model.setVehModelIds(idsStr);

            StringBuilder vehModelNameStrs = new StringBuilder();
            ids.forEach(id -> {
                if (idVehModelMap.containsKey(id))
                    vehModelNameStrs.append(idVehModelMap.get(id).getVehModelName()).append(",");
            });
            if (vehModelNameStrs.length() > 0) vehModelNameStrs.deleteCharAt(vehModelNameStrs.length() - 1);

            model.setVehModelNameStrs(vehModelNameStrs.toString());
            if (StringUtils.isNotBlank(model.getVehModelIdStrs())&&model.getVehModelIdStrs().contains("-1")){
                model.setVehModelNameStrs("全部车型");
                model.setVehModelIdStrs("");
            }

        }
    }

    @Override
    public InstructManagementModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");
        params.put("id", id);
        InstructManagement entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        InstructManagementModel model = InstructManagementModel.fromEntry(entry);
        dealVehModelIdAndName(Collections.singletonList(model), null);
        return model;
    }


    @Override
    public void insert(InstructManagementModel model) {

        if (model.getVehModelIds()==null||model.getVehModelIds().length==0||model.getVehModelIds()[0].equals("")){
            throw new BusinessException("请选择车型");
        }
        InstructManagement obj = new InstructManagement();
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
        int res = 0;
        try {
            res = super.insert(obj);
        } catch (DuplicateKeyException e) {
            if (StringUtils.isEmpty(e.getMessage())) {
               log.error("error", e);
                throw e;
            }
            if (e.getMessage().contains("uq_category_id_param_data")) {
                throw new BusinessException("参数数据存在重复");
            } else if (e.getMessage().contains("uq_name")) {
                throw new BusinessException("控制命令名称存在重复");
            }
        }
        if (res == 0) {
            throw new BusinessException("新增失败");
        }

        model.setId(obj.getId());
        dealInstructAndVehicleModel(model);
    }

    @Override
    public void update(InstructManagementModel model) {

        if (model.getVehModelIds()==null||model.getVehModelIds().length==0||model.getVehModelIds()[0].equals("")){
            throw new BusinessException("请选择车型");
        }
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");

        InstructManagement obj = new InstructManagement();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = 0;
        try {
            res = super.updateByMap(params);
        } catch (DuplicateKeyException e) {
            if (StringUtils.isEmpty(e.getMessage())) {
               log.error("error", e);
                throw e;
            }
            if (e.getMessage().contains("uq_category_id_param_data")) {
                throw new BusinessException("参数数据存在重复");
            } else if (e.getMessage().contains("uq_name")) {
                throw new BusinessException("控制命令名称存在重复");
            }
        }
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        dealInstructAndVehicleModel(model);
    }

    /**
     * 处理指令和适用车型的关系表
     *
     * @param model
     */
    private void dealInstructAndVehicleModel(InstructManagementModel model) {

        Map<String, Object> params = new HashMap<>(1);
        params.put("instructManagementId", model.getId());
        managementModelService.deleteBySqlId("deleteByManagementId", params);

        List<String> vehModelIds = new ArrayList<>();
        if (null != model.getVehModelIds()) {
            vehModelIds = Arrays.asList(model.getVehModelIds());
        }
        //如果没有选择，则默认全部车型
        if (CollectionUtils.isEmpty(vehModelIds) && null != model.getPagerInfo()) {

            Object vehicleListResult = vehModelService.list(model.getPagerInfo());
            if (vehicleListResult instanceof List) {
                List<VehModelModel> vehModelModels = (List<VehModelModel>) vehicleListResult;
                vehModelIds = vehModelModels.stream().map(VehModelModel::getId).collect(Collectors.toList());
            } else {
                PagerResult pr = (PagerResult) vehicleListResult;
                List<Object> data = pr.getData();
                List<VehModelModel> vehModelModels = (List<VehModelModel>)data.get(0);
                vehModelIds = vehModelModels.stream().map(VehModelModel::getId).collect(Collectors.toList());
            }
        }
        if (CollectionUtils.isNotEmpty(vehModelIds)&&vehModelIds.size()==1&&vehModelIds.get(0).equals("-1")){
            //全部车型
            managementModelService.insert(new InstructManagementModelModel(model.getId(), "-1"));
        }
        else {
            vehModelIds.forEach(it -> managementModelService.insert(new InstructManagementModelModel(model.getId(), it)));

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");
        Map<String, Object> detailParams = new HashMap<>(1);

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {

            //先删除关联的指令和车型数据
            params.put("instructManagementId", id);
            managementModelService.deleteBySqlId("deleteByManagementId", params);

            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_management", "iman");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<InstructManagement>(this, "pagerModel", params, "sys/res/instructManagement/export.xls", "控制命令管理") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "INSTRUCTMANAGEMENT" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<InstructManagementModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructManagementModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructManagementModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "INSTRUCTMANAGEMENT" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<InstructManagementModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructManagementModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructManagementModel model) {
                update(model);
            }
        }.work();

    }

    public Boolean checkPassword(CheckPasswordDto checkPasswordDto) {
        InstructManagementModel model = get(checkPasswordDto.getId());

        if (StringUtils.isEmpty(model.getPasswd())) return true;

        if (!model.getPasswd().equals(SecurityUtil.getMd5(checkPasswordDto.getPassword()))) {
            throw new BusinessException("密码错误");
        }

        return true;
    }

}
