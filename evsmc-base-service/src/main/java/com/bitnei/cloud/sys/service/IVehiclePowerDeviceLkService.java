package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.VehiclePowerDeviceLk;
import com.bitnei.cloud.sys.model.VehiclePowerDeviceLkModel;

import java.util.List;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehiclePowerDeviceLkService接口<br>
* 描述： VehiclePowerDeviceLkService接口，在xml中引用<br>
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

public interface IVehiclePowerDeviceLkService extends IBaseService {


    /**
     * 获取发电装置信息
     * @param vehicleId 车辆id
     * @param modelType 类型:1:燃油发电机型号 2:燃料电池系统型号
     * @return model
     */
    List<VehiclePowerDeviceLkModel> listByVehicleId(String vehicleId, Integer modelType);

    /**
     * 获取发电装置信息
     * @param vehicleId 车辆id
     * @param modelType 类型:1:燃油发电机型号 2:燃料电池系统型号
     * @return 实体
     */
    List<VehiclePowerDeviceLk> findByVehicleIdAndModelType(String vehicleId, Integer modelType);

    /**
     * 根据id获取
     * @param id id
     * @return model
     */
     VehiclePowerDeviceLkModel get(String id);

    /**
     * 新增
     * @param model  新增model
     */
    void insert(VehiclePowerDeviceLkModel model);

    /**
     * 编辑
     * @param model  编辑model
     */
    void update(VehiclePowerDeviceLkModel model);


    List<VehiclePowerDeviceLkModel> findPowerListByVehParams(Map<String, Object> params);
}
