package com.bitnei.cloud.screen.service;

import com.bitnei.cloud.screen.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xuzhijie
 */
public interface ScreenVehicleService {

    /**
     * 获取所有省份的区域ID和名称
     *
     * @return 省份区域列表
     */
    List<Area> getProvinceArea();

    /**
     * 获取所有省份总车辆数和在线车辆数
     *
     * @param provinceArea 全部省份
     * @return 排序后的省份和数量
     */
    List<Area> getProvinceCarCount(List<Area> provinceArea);

    /**
     * 获取总车辆数
     *
     * @param areaId 区域ID
     * @return 数量
     */
    int getTotalCarCount(String areaId);

    /**
     * 获取监控车辆数
     *
     * @param areaId 区域ID
     * @return 数量
     */
    int getMonitoringCarCount(String areaId);

    /**
     * 获取在线车辆数
     *
     * @param areaId 区域ID
     * @return 数量
     */
    int getOnlineCarCount(String areaId);

    /**
     * 获取某个区域碳减排信息
     *
     * @param areaId 区域ID
     * @return 碳减排
     */
    EmissionReduction getEmissionReductionByArea(String areaId);

    /**
     * 根据区域ID获取该区域的车辆信息
     *
     * @param areaId 区域ID
     * @return 车辆信息
     */
    List<VehicleGeo> getVehiclesByAreaId(String areaId);

    /**
     * 根据条件模糊匹配车辆信息
     *
     * @param vin          车架号
     * @param licensePlate 车牌号
     * @return
     */
    List<VehicleInfo> getVehicleInfo(String vin, String licensePlate);

    /**
     * 查询车辆动力方式
     *
     * @param vid 车辆ID
     * @return
     */
    PowerMode findVehiclePowerMode(String vid);

    /**
     * 查询车辆动力方式
     *
     * @param vids 车辆ID
     * @return
     */
    List<PowerMode> findVehiclePowerModes(List<String> vids);

    /**
     * 查询单车信息
     *
     * @param vid
     * @return
     */
    Map<String, Object> getVehicleItem(String vid);

    /**
     * 获取可监控车辆的经纬度
     *
     * @return
     */
    Collection<VehicleGeo> getGeo();
}
