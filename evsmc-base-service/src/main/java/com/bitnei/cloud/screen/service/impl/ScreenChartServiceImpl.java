package com.bitnei.cloud.screen.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.screen.dao.ScreenChartMapper;
import com.bitnei.cloud.screen.model.*;
import com.bitnei.cloud.screen.protocol.DateFormatEnum;
import com.bitnei.cloud.screen.protocol.DateUtil;
import com.bitnei.cloud.screen.service.ScreenChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiFunction;

/**
 * @author xuzhijie
 */
@Service
@Slf4j
public class ScreenChartServiceImpl implements ScreenChartService {

    @Resource
    private ScreenChartMapper mapper;

    @Override
    public List<Gps> getChargeGeo(String areaId, String date) throws BusinessException {
        return parseDate(
            date,
            new ArrayList<Gps>(0),
            (startTime, endTime) -> mapper.getChargeGeo(areaId, startTime, endTime));
    }

    @Override
    public ChargeInfo getChargeInfo(String areaId, String date) throws BusinessException {
        return parseDate(
            date,
            new ChargeInfo(),
            (startTime, endTime) -> mapper.getChargeInfo(areaId, startTime, endTime));
    }

    @Override
    public List<RunGeo> getRunGeo(String areaId, String date) throws BusinessException {
        return parseDate(
            date,
            new ArrayList<RunGeo>(0),
            (startTime, endTime) -> mapper.getRunGeo(areaId, startTime, endTime));
    }

    @Override
    public List<UnitVehicleCount> getVehicleCountByUnit(String date) throws BusinessException {
        // 查询车辆销量TOP 10
        List<UnitVehicleCount> vehicleCount = mapper.getUnitVehicleCountTop10();
        if( vehicleCount.isEmpty() ){
            // 单位为空，直接返回
            return null;
        }
        String[] units = new String[vehicleCount.size()];
        for (int i = 0; i < vehicleCount.size(); i++) {
            units[i] = vehicleCount.get(i).getUnitId();
        }
        // 查询TOP 10单位的在线车辆数
        List<UnitVehicleCount> onlineCount = parseDate(date, new ArrayList<UnitVehicleCount>(0), (startTime, endTime) -> mapper.getOnlineVehicle(units, startTime, endTime));
        Map<String, UnitVehicleCount> onlineMap = new HashMap<>(onlineCount.size());
        for (UnitVehicleCount online : onlineCount) {
            onlineMap.put(online.getUnitId(), online);
        }

        for (UnitVehicleCount unitVehicleCount : vehicleCount) {
            UnitVehicleCount count = onlineMap.get(unitVehicleCount.getUnitId());
            int onlineVehCount = 0;
            if (count != null) {
                onlineVehCount = count.getOnlineCount();
            }
            unitVehicleCount.setOnlineCount(onlineVehCount);
        }

        return vehicleCount;
    }

    @Override
    public ChargeVehicleCount getChargeVehicleCount(String date) throws BusinessException {
        return parseDate(
            date,
            new ChargeVehicleCount(),
            (startTime, endTime) -> mapper.getChargeVehicleCount(startTime, endTime));
    }

    @Override
    public SocChargeStartState getSocChargeStartState(String startDate, String endDate) throws BusinessException {
        return parseDate(
            startDate,
            endDate,
            new SocChargeStartState(),
            (startTime, endTime) -> mapper.getSocChargeStartState(startTime, endTime));
    }

    @Override
    public List<ModelDailyKilometre> getDailyKilometre(String date) throws BusinessException {
        // 获取所有车型每天的累计里程
        return parseMonth(date, new ArrayList<ModelDailyKilometre>(0), (startTime, endTime) -> {
            List<ModelDailyKilometre.DailyKilometre> modelDailyKilometreList = mapper.getDailyKilometre(startTime, endTime);
            return getModelDailyKilometres(modelDailyKilometreList);
        });
    }

    @Override
    public List<ModelDailyKilometre> getDailyAvgKilometre(String date) throws BusinessException {
        // 获取所有车型每天的平均里程
        return parseMonth(
            date,
            new ArrayList<ModelDailyKilometre>(0),
            (startTime, endTime) -> getModelDailyKilometres(mapper.getDailyAvgKilometre(startTime, endTime)));
    }

    @Override
    public List<UnitVehicleCount> unitList() {
        return mapper.unitList();
    }

    @Override
    public List<VehicleDailyKilometre> vehicleDailyKilometre(String unitId, String areaId, String vin, String licensePlate, String date) throws BusinessException {
        // 按车牌号分类
        Map<String, List<VehicleDailyKilometre.DailyKilometre>> result = new HashMap<>(16);
        parseMonth(
            date,
            new ArrayList<VehicleDailyKilometre.DailyKilometre>(0),
            (startTime, endTime) -> mapper.getVehicleDailyKilometre(unitId, areaId, vin, licensePlate, startTime, endTime))
            .forEach(dailyKilometre -> result.computeIfAbsent(dailyKilometre.getLicensePlate(), plate -> new ArrayList<>()).add(dailyKilometre));

        // 把分类之后数据放入实体List
        List<VehicleDailyKilometre> resList = new ArrayList<>();
        result.entrySet().forEach(entry -> {
            VehicleDailyKilometre vehicleDailyKilometre = new VehicleDailyKilometre();
            vehicleDailyKilometre.setLicensePlate(entry.getKey());

            List<VehicleDailyKilometre.DailyKilometre> value = entry.getValue();
            value.sort(Comparator.comparing(VehicleDailyKilometre.DailyKilometre::getDay));

            vehicleDailyKilometre.setKilometreList(value);
            resList.add(vehicleDailyKilometre);
        });
        return resList;
    }

    private List<ModelDailyKilometre> getModelDailyKilometres(List<ModelDailyKilometre.DailyKilometre> modelDailyKilometreList) {
        // 按车型分类
        Map<String, List<ModelDailyKilometre.DailyKilometre>> vehicleModelMap = new HashMap<>(16);
        modelDailyKilometreList.forEach(dailyKilometre -> vehicleModelMap.computeIfAbsent(dailyKilometre.getVehModelName(), vehModelName -> new ArrayList<>()).add(dailyKilometre));

        // 把分类之后数据放入实体List,以备排序
        List<ModelDailyKilometre> result = new ArrayList<>();
        vehicleModelMap.entrySet().forEach(entry -> {
            ModelDailyKilometre modelDailyKilometre = new ModelDailyKilometre();
            modelDailyKilometre.setVehModelName(entry.getKey());

            List<ModelDailyKilometre.DailyKilometre> value = entry.getValue();
            value.sort(Comparator.comparing(ModelDailyKilometre.DailyKilometre::getDay));

            modelDailyKilometre.setKilometreList(value);
            result.add(modelDailyKilometre);
        });

        // 根据里程数倒序
        result.sort((o1, o2) -> {
            Double kilometre1 = 0d;
            Double kilometre2 = 0d;
            for (ModelDailyKilometre.DailyKilometre dailyKilometre : o1.getKilometreList()) {
                kilometre1 += dailyKilometre.getAvgKilometre();
            }

            for (ModelDailyKilometre.DailyKilometre dailyKilometre : o2.getKilometreList()) {
                kilometre2 += dailyKilometre.getAvgKilometre();
            }
            // 倒序
            return kilometre2.compareTo(kilometre1);
        });
        return result;
    }

    private <T> T parseDate(String date, T defaultVal, BiFunction<String, String, T> consumer) throws BusinessException {
        return parseDate(date, date, defaultVal, consumer);
    }

    private <T> T parseDate(String inStartTime, String inEndTime, T defaultVal, BiFunction<String, String, T> consumer) throws BusinessException {
        try {
            if (consumer == null) {
                return defaultVal;
            }
            String startTime = DateUtil.getDateStartByDate(inStartTime, DateFormatEnum.DATE);
            String endTime = DateUtil.getDateEndByDate(inEndTime, DateFormatEnum.DATE);
            return consumer.apply(startTime, endTime);
        } catch (ParseException e) {
            if (inStartTime.equals(inEndTime)) {
                throw new BusinessException("日期解析异常【" + inStartTime + "】");
            }
            throw new BusinessException("日期解析异常【" + inStartTime + ", " + inEndTime + "】");
        }
    }

    private <T> T parseMonth(String date, T defaultVal, BiFunction<String, String, T> consumer) throws BusinessException {
        try {
            if (consumer == null) {
                return defaultVal;
            }
            String startTime = DateUtil.getMonthStartByDate(date, DateFormatEnum.MONTH);
            String endTime = DateUtil.getMonthEndByDate(date, DateFormatEnum.MONTH);
            return consumer.apply(startTime, endTime);
        } catch (ParseException e) {
            throw new BusinessException("日期解析异常【" + date + "】");
        }
    }

}
