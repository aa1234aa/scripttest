package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.VehicleFiling;
import com.bitnei.cloud.sys.model.EncryptionChipModel;
import com.bitnei.cloud.sys.model.VehicleFilingModel;
import com.bitnei.cloud.sys.service.IEncryptionChipService;
import com.bitnei.cloud.sys.service.IVehicleFilingService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 车辆防篡改备案service impl
 * @author zhouxianzhou
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleFilingMapper")
public class VehicleFilingService extends BaseService implements IVehicleFilingService {

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private IEncryptionChipService encryptionChipService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_filing", "svf");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        String statusVal = "status";
        if (params.containsKey(statusVal)) {
            String ids = (String) params.get(statusVal);
            params.put("statusVal", StringUtils.split(ids, ","));
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<VehicleFiling> entries = findBySqlId("pagerModel", params);
            List<VehicleFilingModel> models = Lists.newArrayList();
            for (VehicleFiling entry : entries) {
                models.add(VehicleFilingModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<VehicleFilingModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                VehicleFiling obj = (VehicleFiling) entry;
                models.add(VehicleFilingModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public VehicleFilingModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_filing", "svf");
        params.put("id", id);
        VehicleFiling entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehicleFilingModel.fromEntry(entry);
    }


    @Override
    public void insert(VehicleFilingModel model) {

        VehicleFiling obj = new VehicleFiling();
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
    public void update(VehicleFilingModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_filing", "svf");
        VehicleFiling obj = new VehicleFiling();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
        // 同步更新芯片id的备案状态
        String chipId = vehicleMapper.getChipId(model.getVehicleId());
        if(StringUtils.isNotEmpty(chipId)) {
            EncryptionChipModel chipModel = encryptionChipService.get(chipId);
            if(chipModel != null) {
                chipModel.setFilingStatus(model.getStatus());
                encryptionChipService.update(chipModel);
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

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_filing", "svf");
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_filing", "svf");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        new ExcelExportHandler<VehicleFiling>(this, "pagerModel", params, "sys/res/vehicleFiling/export.xls", "车辆防篡改备案") {
        }.work();
    }


}
