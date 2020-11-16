package com.bitnei.cloud.screen.service;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.screen.model.*;

import java.util.List;

/**
 * 大屏图表查询
 *
 * @author xuzhijie
 */
public interface ScreenChartService {

    /**
     * 获取充电坐标
     *
     * @param areaId 区域ID
     * @param date   日期
     * @return 坐标列表
     * @throws BusinessException 日期解析异常
     */
    List<Gps> getChargeGeo(String areaId, String date) throws BusinessException;

    /**
     * 获取充电信息，充电次数、充电量等
     *
     * @param areaId 区域ID
     * @param date   日期
     * @return ChargeInfo
     * @throws BusinessException 日期解析异常
     */
    ChargeInfo getChargeInfo(String areaId, String date) throws BusinessException;

    /**
     * 获取行驶轨迹热力图
     *
     * @param areaId 区域ID
     * @param date   日期
     * @return 经纬度和权重列表
     * @throws BusinessException 日期解析异常
     */
    List<RunGeo> getRunGeo(String areaId, String date) throws BusinessException;

    /**
     * 获取每个运营单位的车辆数和在线车辆数
     *
     * @param date 日期
     * @return 车辆数信息
     * @throws BusinessException 日期解析异常
     */
    List<UnitVehicleCount> getVehicleCountByUnit(String date) throws BusinessException;

    /**
     * 根据日期时间段获取充电车辆数
     *
     * @param date 日期
     * @return 各时间段充电车辆数
     * @throws BusinessException 日期解析异常
     */
    ChargeVehicleCount getChargeVehicleCount(String date) throws BusinessException;

    /**
     * SOC充电起始状态
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 各SOC段次数
     * @throws BusinessException 日期解析异常
     */
    SocChargeStartState getSocChargeStartState(String startDate, String endDate) throws BusinessException;

    /**
     * 不同车型每日累计行驶里程
     *
     * @param date 月份
     * @return 排序后数据
     * @throws BusinessException 日期解析异常
     */
    List<ModelDailyKilometre> getDailyKilometre(String date) throws BusinessException;

    /**
     * 不同车型每日平均行驶里程
     *
     * @param date 月份
     * @return 排序后数据
     * @throws BusinessException 日期解析异常
     */
    List<ModelDailyKilometre> getDailyAvgKilometre(String date) throws BusinessException;

    /**
     * 获取运营单位列表
     *
     * @return 列表
     */
    List<UnitVehicleCount> unitList();

    /**
     * 多车每日里程
     *
     * @param unitId       运营单位ID
     * @param areaId       区域Id
     * @param vin          vin
     * @param licensePlate 车牌号
     * @param date         日期
     * @return 多车每日里程
     * @throws BusinessException 日期解析异常
     */
    List<VehicleDailyKilometre> vehicleDailyKilometre(String unitId, String areaId, String vin, String licensePlate, String date) throws BusinessException;
}
