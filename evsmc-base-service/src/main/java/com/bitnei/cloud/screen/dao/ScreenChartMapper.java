package com.bitnei.cloud.screen.dao;

import com.bitnei.cloud.screen.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大屏图表查询
 *
 * @author xuzhiie
 */
@Mapper
public interface ScreenChartMapper {

    /**
     * 获取充电坐标
     *
     * @param areaId    区域ID
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return 坐标列表
     */
    List<Gps> getChargeGeo(
        @Param("areaId") String areaId,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取充电信息，充电次数、充电量等
     *
     * @param areaId    区域ID
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    ChargeInfo getChargeInfo(
        @Param("areaId") String areaId,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取行驶轨迹热力图
     *
     * @param areaId    区域ID
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    List<RunGeo> getRunGeo(
        @Param("areaId") String areaId,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取在线运营单位
     * 注：先保留
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     * @see ScreenChartMapper.getOnlineVehicle
     */
    @Deprecated
    List<UnitVehicleCount> getOnlineByUnit(
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取在线运营单位
     *
     * @param units     单位id
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    List<UnitVehicleCount> getOnlineVehicle(
        @Param("units") String[] units,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取所有运营单位
     *
     * @return
     * @see
     */
    List<UnitVehicleCount> getUnitVehicleCountTop10();

    /**
     * 获取在线运营单位
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    ChargeVehicleCount getChargeVehicleCount(
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * SOC充电起始状态
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    SocChargeStartState getSocChargeStartState(
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取所有车型每天的累计里程
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    List<ModelDailyKilometre.DailyKilometre> getDailyKilometre(
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取所有车型每天的平均里程
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    List<ModelDailyKilometre.DailyKilometre> getDailyAvgKilometre(
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 获取运营单位列表
     *
     * @return
     */
    List<UnitVehicleCount> unitList();

    /**
     * 获得车辆每日里程
     *
     * @param unitId       运营单位ID
     * @param areaId       区域ID
     * @param vin
     * @param licensePlate 车牌号
     * @param startTime    开始日期
     * @param endTime      结束日期
     * @return
     */
    List<VehicleDailyKilometre.DailyKilometre> getVehicleDailyKilometre(
        @Param("unitId") String unitId,
        @Param("areaId") String areaId,
        @Param("vin") String vin,
        @Param("licensePlate") String licensePlate,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime);
}
