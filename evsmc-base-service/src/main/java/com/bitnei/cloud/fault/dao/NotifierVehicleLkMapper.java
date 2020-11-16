package com.bitnei.cloud.fault.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.fault.domain.NotifierVehicleLk;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierVehicleLkMapper接口<br>
* 描述： NotifierVehicleLkMapper接口，在xml中引用<br>
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
* <td>2019-03-06 17:37:36</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Mapper
public interface NotifierVehicleLkMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    NotifierVehicleLk findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(NotifierVehicleLk obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(NotifierVehicleLk obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<NotifierVehicleLk> pagerModel(Map<String,Object> params);

    /**
     * 分配的人数
     * @param vehicleId
     * @return
     */
    int allocateCount(String vehicleId);

    /**
     * 分配的车辆数
     * @param notifierId
     * @return
     */
    int allocateVehicleCount(String notifierId);

    /**
     *  查询分配的车辆
     * @param notifierId
     * @return
     */
    List<NotifierVehicleLk> allocateVehicle(String notifierId);

    /**
     * 根据用户id删除分配的车辆
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 根据负责人id删除分配的车辆
     * @param notifierId
     */
    void deleteByNotifierId(String notifierId);

    /**
     * 批量删除
     * @param notifierId 推送人ID
     * @param vehicleIds 车辆ID集合
     */
    void deleteBatch(@Param("notifierId") String notifierId, @Param("vehicleIds")List<String> vehicleIds);

    /**
     * 批量新增
     * @param items
     */
    void insertBatch(List<NotifierVehicleLk> items);

}
