package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehicleDriveDeviceLk;
import com.bitnei.cloud.sys.model.VehicleDriveDeviceLkModel;
import com.bitnei.cloud.sys.service.IVehicleDriveDeviceLkService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
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
* 功能： VehicleDriveDeviceLkService实现<br>
* 描述： VehicleDriveDeviceLkService实现<br>
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
* <td>2018-12-13 17:09:47</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleDriveDeviceLkMapper" )
public class VehicleDriveDeviceLkService extends BaseService implements IVehicleDriveDeviceLkService {

   @Override
   public List<VehicleDriveDeviceLkModel> listByVehicleId(String vehicleId, Integer modelType) {
       Map<String, Object> params = Maps.newHashMap();
       params.put("vehicleId",vehicleId);
       params.put("modelType",modelType);
       List<VehicleDriveDeviceLk> entries = this.findBySqlId("pagerModel", params);
       List<VehicleDriveDeviceLkModel> models = new ArrayList<>();
       Iterator var5 = entries.iterator();
       while(var5.hasNext()) {
           VehicleDriveDeviceLk entry = (VehicleDriveDeviceLk)var5.next();
           models.add(VehicleDriveDeviceLkModel.fromEntry(entry));
       }

       return models;
   }

    @Override
    public List<VehicleDriveDeviceLk> findByVehicleIdAndModelType(String vehicleId, Integer modelType) {
        Map<String, Object> params = ImmutableMap.of("vehicleId", vehicleId, "modelType", modelType);
        List<VehicleDriveDeviceLk> entries = this.findBySqlId("pagerModel", params);
        return entries;
    }


    @Override
    public VehicleDriveDeviceLkModel get(String id) {

        Map<String, String> params = ImmutableMap.of("id", id);
        VehicleDriveDeviceLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehicleDriveDeviceLkModel.fromEntry(entry);
    }




    @Override
    public void insert(VehicleDriveDeviceLkModel model) {

        VehicleDriveDeviceLk obj = new VehicleDriveDeviceLk();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehicleDriveDeviceLkModel model) {

        Map<String, Object> params = Maps.newHashMap();
        VehicleDriveDeviceLk obj = new VehicleDriveDeviceLk();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public List<VehicleDriveDeviceLkModel> findDriveListByVehParams(Map<String, Object> params) {
        List<VehicleDriveDeviceLk> entries = this.findBySqlId("pagerModelByVehParams", params);
        List<VehicleDriveDeviceLkModel> models = new ArrayList<>();
        Iterator var5 = entries.iterator();
        while(var5.hasNext()) {
            VehicleDriveDeviceLk entry = (VehicleDriveDeviceLk)var5.next();
            models.add(VehicleDriveDeviceLkModel.fromEntry(entry));
        }

        return models;
    }


}
