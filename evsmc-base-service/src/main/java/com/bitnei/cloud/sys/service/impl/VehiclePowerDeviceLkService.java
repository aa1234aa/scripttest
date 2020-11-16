package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehiclePowerDeviceLk;
import com.bitnei.cloud.sys.model.VehiclePowerDeviceLkModel;
import com.bitnei.cloud.sys.service.IVehiclePowerDeviceLkService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehiclePowerDeviceLkService实现<br>
* 描述： VehiclePowerDeviceLkService实现<br>
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
* <td>2018-12-13 17:14:00</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehiclePowerDeviceLkMapper" )
public class VehiclePowerDeviceLkService extends BaseService implements IVehiclePowerDeviceLkService {

    @Override
    public List<VehiclePowerDeviceLkModel> listByVehicleId(String vehicleId, Integer modelType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("vehicleId",vehicleId);
        params.put("modelType",modelType);
        List<VehiclePowerDeviceLk> entries = this.findBySqlId("pagerModel", params);
        List<VehiclePowerDeviceLkModel> models = new ArrayList<>();
        Iterator var5 = entries.iterator();
        while(var5.hasNext()) {
            VehiclePowerDeviceLk entry = (VehiclePowerDeviceLk)var5.next();
            models.add(VehiclePowerDeviceLkModel.fromEntry(entry));
        }

        return models;
    }

    @Override
    public List<VehiclePowerDeviceLk> findByVehicleIdAndModelType(String vehicleId, Integer modelType) {
        Map<String, Object> params = ImmutableMap.of("vehicleId", vehicleId, "modelType", modelType);
        return this.findBySqlId("pagerModel", params);
    }

    @Override
    public VehiclePowerDeviceLkModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle_power_device_lk", "svedlk");
        params.put("id",id);
        VehiclePowerDeviceLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehiclePowerDeviceLkModel.fromEntry(entry);
    }


    @Override
    public void insert(VehiclePowerDeviceLkModel model) {

        VehiclePowerDeviceLk obj = new VehiclePowerDeviceLk();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehiclePowerDeviceLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle_power_device_lk", "svedlk");

        VehiclePowerDeviceLk obj = new VehiclePowerDeviceLk();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public List<VehiclePowerDeviceLkModel> findPowerListByVehParams(Map<String, Object> params) {
        List<VehiclePowerDeviceLk> entries = this.findBySqlId("pagerModelByVehParams", params);
        List<VehiclePowerDeviceLkModel> models = new ArrayList<>();
        Iterator var5 = entries.iterator();
        while(var5.hasNext()) {
            VehiclePowerDeviceLk entry = (VehiclePowerDeviceLk)var5.next();
            models.add(VehiclePowerDeviceLkModel.fromEntry(entry));
        }

        return models;
    }


}
