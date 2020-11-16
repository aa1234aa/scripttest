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
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.UppackageSend;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.enums.UpgradeLogAction;
import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
import com.bitnei.cloud.sys.model.UppackageSendModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IUpgradeLogService;
import com.bitnei.cloud.sys.service.IUppackageSendDetailsService;
import com.bitnei.cloud.sys.service.IUppackageSendService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UppackageSendService实现<br>
 * 描述： UppackageSendService实现<br>
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
 * <td>2019-03-05 14:50:32</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UppackageSendMapper")
@RequiredArgsConstructor
public class UppackageSendService extends BaseService implements IUppackageSendService {

    private final IUppackageSendDetailsService uppackageSendDetailsService;
    private final VehicleMapper vehicleMapper;
    private final IVehicleService vehicleService;
    private final IUpgradeLogService upgradeLogService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UppackageSend> entries = findBySqlId("pagerModel", params);
            List<UppackageSendModel> models = new ArrayList();
            for (UppackageSend entry : entries) {
                UppackageSend obj = (UppackageSend) entry;
                models.add(UppackageSendModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UppackageSendModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                UppackageSend obj = (UppackageSend) entry;
                models.add(UppackageSendModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public UppackageSendModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");
        params.put("id", id);
        UppackageSend entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UppackageSendModel.fromEntry(entry);
    }

    @Override
    public UppackageSendModel getByTaskName(String taskName) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");
        params.put("taskName", taskName);
        UppackageSend entry = unique("findByTaskName", params);
        if (entry == null) {
            return null;
        }
        return UppackageSendModel.fromEntry(entry);
    }


    @Override
    public void insert(UppackageSendModel model) {

        UppackageSend obj = new UppackageSend();
        BeanUtils.copyProperties(model, obj);
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

        List<String> vids;
        if (StringUtils.isEmpty(model.getVehIds()) && null != model.getPagerInfo()) {
            vids = getVehicleModelsByPagerInfo(model.getPagerInfo());
        } else {
            vids = Arrays.asList(model.getVehIds().split(","));
        }

        List<Vehicle> vehicles = vehicleMapper.getByIds(vids);

        StringBuilder licensePlates = new StringBuilder();

        for (Vehicle it : vehicles) {
            //            插入远升子任务表
            UppackageSendDetailsModel detailsModel = new UppackageSendDetailsModel();
            detailsModel.setVin(it.getVin());
            detailsModel.setLicensePlate(it.getLicensePlate());
            detailsModel.setVehicleTypeId(it.getVehTypeId());
            detailsModel.setVehicleTypeValue(it.getVehTypeName());
            detailsModel.setFileName(model.getFileName());
            detailsModel.setUppackageSendStatus(0);
            detailsModel.setUppackageInfoId(model.getUppackageId());
            detailsModel.setUpgradeSendState(0);
            detailsModel.setHistoryOnlineState(vehicleService.getVehicleOnlineStatus(it.getVin()));
            detailsModel.setOperatingArea(it.getOperAreaName());
            detailsModel.setInterNo(it.getInterNo());
            detailsModel.setUppackageSendId(obj.getId());
            detailsModel.setFileSendStatus(0);
            detailsModel.setUpgradeStatus(0);

            uppackageSendDetailsService.insert(detailsModel);

            if (StringUtils.isNotEmpty(detailsModel.getLicensePlate())) {
                licensePlates.append(detailsModel.getLicensePlate()).append(",");
            } else {
                licensePlates.append(detailsModel.getInterNo()).append(",");
            }
        }

        if (licensePlates.length() > 0) {
            licensePlates.deleteCharAt(licensePlates.length() - 1);
        }

        //操作日志
        upgradeLogService.save(UpgradeLogAction.CREATE_TASK.getValue(),
                model.getTaskName(), licensePlates.toString(), "新建升级任务", "");
    }


    private List<String> getVehicleModelsByPagerInfo(PagerInfo pagerInfo) {
        Object vehicleListResult = vehicleService.vehicleList(pagerInfo);
        if (vehicleListResult instanceof List) {
            List<VehicleModel> vehicleModels = (List<VehicleModel>) vehicleListResult;
            return vehicleModels.stream().map(VehicleModel::getId).collect(Collectors.toList());
        } else {
            PagerResult pr = (PagerResult) vehicleListResult;
            List<Object> data = pr.getData();
            List<VehicleModel> vehicleModels = (List<VehicleModel>) data.get(0);
            return vehicleModels.stream().map(VehicleModel::getId).collect(Collectors.toList());
        }
    }

    @Override
    public void update(UppackageSendModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");

        UppackageSend obj = new UppackageSend();
        BeanUtils.copyProperties(model, obj);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send", "ups");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<UppackageSend>(this, "pagerModel", params, "sys/res/uppackageSend/export.xls", "远程升级任务") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "UPPACKAGESEND" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UppackageSendModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageSendModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageSendModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "UPPACKAGESEND" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UppackageSendModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageSendModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageSendModel model) {
                update(model);
            }
        }.work();

    }

}
