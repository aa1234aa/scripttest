package com.bitnei.cloud.screen.dao;

import com.bitnei.cloud.screen.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大屏车辆查询
 *
 * @author xuzhiie
 */
@Mapper
public interface ScreenVehicleMapper {

    /**
     * 获取所有省份的区域ID和名称
     *
     * @return 省份区域列表
     */
    List<Area> getProvinceArea();

    /**
     * 获取总车辆数
     *
     * @param areaId       区域ID
     * @param onlined      是否上过线
     * @param onlineStatus 车辆实时状态
     * @return 数量
     */
    int getCarCount(
        @Param("areaId") String areaId,
        @Param("onlined") Integer onlined,
        @Param("onlineStatus") Integer onlineStatus);

    /**
     * 根据区域ID获取该区域的车辆信息
     *
     * @param areaId 区域ID
     * @return 车辆信息
     */
    List<VehicleGeo> getVehiclesByAreaId(@Param("areaId") String areaId);

    /**
     * 根据条件模糊匹配车辆信息
     *
     * @param vin          车架号
     * @param licensePlate 车牌号
     * @return
     */
    List<VehicleInfo> getVehicleInfo(
        @Param("vin") String vin,
        @Param("licensePlate") String licensePlate);

    /**
     * 获取所有车辆类别的油耗，电耗
     *
     * @return
     */
    List<VehicleMode> getVehicleModes();

    /**
     * 查询车辆动力方式
     *
     * @param vid 车辆VID
     * @return
     */
    PowerMode findVehiclePowerMode(@Param("vid") String vid);

    /**
     * 查询车辆动力方式
     *
     * @param vids 车辆VID
     * @return
     */
    List<PowerMode> findVehiclePowerModes(@Param("vids") List<String> vids);
}
