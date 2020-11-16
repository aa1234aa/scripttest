package com.bitnei.cloud.screen.service.impl;

import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.screen.dao.ScreenVehicleMapper;
import com.bitnei.cloud.screen.model.*;
import com.bitnei.cloud.screen.protocol.DataItemKey;
import com.bitnei.cloud.screen.protocol.VehicleItemResolver;
import com.bitnei.cloud.screen.service.IRedisVehicleCache;
import com.bitnei.cloud.screen.service.ScreenVehicleService;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.bitnei.cloud.sys.service.IVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 大屏接口查询服务
 *
 * @author xuzhijie
 */
@Slf4j
@Service
public class ScreenVehicleServiceImpl implements ScreenVehicleService {

    private static final int CAR_COUNT_DB = 9;

    @Resource
    private RealDataClient realDataClient;

    @Resource
    private ScreenVehicleMapper mapper;

    @Resource(name = "ctoRedisKit")
    private RedisKit redisKit;

    @Autowired
    private IVehicleService vehicleService;


    @Override
    public List<Area> getProvinceArea() {
        return mapper.getProvinceArea();
    }

    @Override
    public List<Area> getProvinceCarCount(List<Area> provinceArea) {
        if (provinceArea.isEmpty()) {
            return provinceArea;
        }
        provinceArea.forEach(area -> {
            area.setTotalCarCount(getTotalCarCount(area.getId()));
            area.setOnlineCarCount(getOnlineCarCount(area.getId()));
        });
        provinceArea.sort(Comparator.comparing(Area::getTotalCarCount, Collections.reverseOrder()));
        return provinceArea;
    }

    @Override
    public int getTotalCarCount(String areaId) {
        return mapper.getCarCount(areaId, null, null);
    }

    @Override
    public int getMonitoringCarCount(String areaId) {
        return mapper.getCarCount(areaId, 1, null);
    }

    @Override
    public int getOnlineCarCount(String areaId) {
        return mapper.getCarCount(areaId, null, 1);
    }

    /**
     * 里程系数
     */
    private static final BigDecimal MILE_COEFFICIENT = new BigDecimal("10000");

    /**
     * 节油系数
     */
    private static final BigDecimal OIL_COEFFICIENT = new BigDecimal("0.003225");

    /**
     * 耗电系数
     */
    private static final BigDecimal ELE_COEFFICIENT = new BigDecimal("0.0005");

    /**
     * 百公里系数
     */
    private static final BigDecimal MILE_100_COEFFICIENT = new BigDecimal("100");

    /**
     * 百公里耗电-默认值
     */
    private static final BigDecimal DEFAULT_KWH100H_COEFFICIENT = new BigDecimal("0.15");

    /**
     * 百公里油耗-默认值
     */
    private static final BigDecimal DEFAULT_L100H_COEFFICIENT = new BigDecimal("0.0833");

    @Override
    public EmissionReduction getEmissionReductionByArea(String areaId) {
        List<VehicleMode> dicts = mapper.getVehicleModes();
        BigDecimal mil = new BigDecimal(0);
        BigDecimal oil = new BigDecimal(0);
        BigDecimal ele = new BigDecimal(0);
        BigDecimal co2 = new BigDecimal(0);

        for (VehicleMode dict : dicts) {
            // 缓存中里程单位为0.1公里，除以10为公里数
            double mileage = 0;
            // 该里程统计依赖于storm大屏统计(原单机版大屏统计服务)统计结果，
            String milSum = redisKit.hget(CAR_COUNT_DB, "CARINFO.DISTRICTS." + areaId + "." + dict.getId(), "mileage_total");
            if (StringUtils.isNotBlank(milSum)) {
                mileage = Double.valueOf(milSum) / 10;
            }
            if (mileage == 0) {
                // 里程为0 就不需要计算了
                log.info("====SCREEN====> 区域ID:{}, 车型ID:{}, 里程为0", areaId, dict.getId());
                continue;
            }

            BigDecimal mileageBigDecimal = new BigDecimal(mileage + "");
            //耗电量，单位为度
            BigDecimal eleOfCarType = mileageBigDecimal.multiply(defaultCoefficient(dict.getKwh100h(), DEFAULT_KWH100H_COEFFICIENT));
            //节油量，单位升
            BigDecimal oilOfCarType = mileageBigDecimal.multiply(defaultCoefficient(dict.getL100km(), DEFAULT_L100H_COEFFICIENT));
            //累计碳减排，单位为吨
            BigDecimal co2OfCarType = oilOfCarType.multiply(OIL_COEFFICIENT).subtract(eleOfCarType.multiply(ELE_COEFFICIENT));
            if (co2OfCarType.compareTo(BigDecimal.ZERO) > 0) {
                // 暂时按东哥说的， 负数时，当作0处理
                co2 = co2.add(co2OfCarType);
            }

            mil = mil.add(mileageBigDecimal);
            oil = oil.add(oilOfCarType);
            ele = ele.add(eleOfCarType);
            log.info("====SCREEN====> 区域ID:{}, 车型ID:{}, 百公里油耗:{}, 百公里电耗:{}, 里程:{}公里, 耗电量:{}度, 节油量:{}升, 累计碳减排:{}吨", areaId, dict.getId(), dict.getL100km(), dict.getKwh100h(), mileage, eleOfCarType, oilOfCarType, co2OfCarType);
        }

        log.info("====SCREEN====> 总里程:{}公里, 耗电量:{}度, 节油量:{}升, 累计碳减排:{}吨", mil, ele, oil, co2);
        EmissionReduction emissionReduction = new EmissionReduction();
        // 累计里程
        emissionReductionUnitConvert(mil, (val, unitPrev) -> {
            emissionReduction.setMil(val);
            emissionReduction.setMilUnit(unitPrev + emissionReduction.getMilUnit());
        });
        // 累计节油量
        emissionReductionUnitConvert(oil, (val, unitPrev) -> {
            emissionReduction.setOil(val);
            emissionReduction.setOilUnit(unitPrev + emissionReduction.getOilUnit());
        });
        // 累计耗电量
        emissionReductionUnitConvert(ele, (val, unitPrev) -> {
            emissionReduction.setEle(val);
            emissionReduction.setEleUnit(unitPrev + emissionReduction.getEleUnit());
        });
        // 累计碳减排
        emissionReductionUnitConvert(co2, (val, unitPrev) -> {
            emissionReduction.setCo2(val);
            emissionReduction.setCo2Unit(unitPrev + emissionReduction.getCo2Unit());
        });
        return emissionReduction;
    }

    /**
     * 动态单位转换
     *
     * @param value
     * @param callback
     */
    private void emissionReductionUnitConvert(BigDecimal value, BiConsumer<BigDecimal, String> callback) {
        if (value.compareTo(MILE_COEFFICIENT) > 0) {
            callback.accept(value.divide(MILE_COEFFICIENT, 1, BigDecimal.ROUND_HALF_UP), "万");
        } else {
            callback.accept(value.setScale(1, BigDecimal.ROUND_HALF_UP), "");
        }
    }

    private BigDecimal defaultCoefficient(double val, BigDecimal defaultValue) {
        if (val <= 0) {
            return defaultValue;
        }
        return new BigDecimal(val + "").divide(MILE_100_COEFFICIENT);
    }

    @Override
    public List<VehicleGeo> getVehiclesByAreaId(String areaId) {
        return mapper.getVehiclesByAreaId(areaId);
    }

    @Override
    public List<VehicleInfo> getVehicleInfo(String vin, String licensePlate) {
        return mapper.getVehicleInfo(vin, licensePlate);
    }

    @Override
    public PowerMode findVehiclePowerMode(final String vid) {
        return mapper.findVehiclePowerMode(vid);
    }

    @Override
    public List<PowerMode> findVehiclePowerModes(final List<String> vids) {
        return mapper.findVehiclePowerModes(vids);
    }

    // region 单车选项卡

    /**
     * 单车监控-查询项
     */
    private static final String[] VEHICLE_ITEM_PARAMS = new String[]{
        DataItemKey.v2201,
        DataItemKey.v2615,
        DataItemKey.v2202,
        DataItemKey.v2203,
        DataItemKey.v2213,
        DataItemKey.v2003,
        DataItemKey.v2103,
        DataItemKey.v2308,
        DataItemKey.v2401,
        DataItemKey.v2411,
        DataItemKey.v2413,
        DataItemKey.v2502,
        DataItemKey.v2503,
        DataItemKey.v2502_GD,
        DataItemKey.v2503_GD,
        DataItemKey.ADDRESS,
        // 新增数据项 - 燃料电池车
        DataItemKey.v2114,
        DataItemKey.v2110,
        DataItemKey.v2111,
        DataItemKey.v2117,
        DataItemKey.v2121,
        // 新增数据项 - 燃油车
        DataItemKey.v60033,
        DataItemKey.v60039,
        DataItemKey.v60038,
        DataItemKey.v60028,
        DataItemKey.v60029,
        DataItemKey.v60035,
        DataItemKey.v60036,
    };

    @Override
    public Map<String, Object> getVehicleItem(final String vid) {
        Map<String, Object> item = new HashMap<>(28);

        VehicleModel vehicleModel = vehicleService.getByUuid(vid);

        DataRequest dataRequest = new DataRequest();
        dataRequest.setReadMode(DataReadMode.GPS_OFFSET_GD | DataReadMode.GPS_ADDRESS | DataReadMode.TRANSLATE);
        dataRequest.setColumns(Arrays.asList(VEHICLE_ITEM_PARAMS));
        dataRequest.setVids(new String[]{vid});

        if (vehicleModel.isG6()) {
            dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
        }
        if (vehicleModel.isGb()) {
            dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
        }

        Map<String, Map<String, String>> vehicleMaps = realDataClient.findByUuids(dataRequest);
        if (MapUtils.isEmpty(vehicleMaps)) {
            return item;
        }

        Map<String, String> vehicleItemMap = vehicleMaps.get(vid);
        // 正常填充，无需特殊处理的
        fillNormal(item, vehicleItemMap);
        // 档位解析
        fill2203(item, vehicleItemMap);
        // 解析驱动总成
        fill2308(item, vehicleItemMap);
        // 电压
        fill2003And2103(item, DataItemKey.v2003, vehicleItemMap.get(DataItemKey.v2003));
        // 温度
        fill2003And2103(item, DataItemKey.v2103, vehicleItemMap.get(DataItemKey.v2103));
        // 高压DC-DC状态解析
        fill2121(item, vehicleItemMap);
        // 查询动力方式
        fill2213(item, vid);
        // 解析车速 车辆状态
        fillVehicleStatus(item, vehicleItemMap);
        // 解析燃料电池温度
        fill2114(item, vehicleItemMap);
        return item;
    }

    /**
     * 捕捉异常，其中一个项异常时，不影响其他项解析
     *
     * @param exec
     */
    private void exceptionWrap(Runnable exec) {
        try {
            exec.run();
        } catch (Exception e) {
            log.error("解析异常：", e);
        }
    }

    // region 协议项填充

    /**
     * 直接填充，不需要特殊解析的
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fillNormal(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        // SOC
        item.put(DataItemKey.v2615, vehicleItemMap.get(DataItemKey.v2615));
        // 累计里程
        item.put(DataItemKey.v2202, vehicleItemMap.get(DataItemKey.v2202));
        // 发动机状态
        item.put(DataItemKey.v2401, vehicleItemMap.get(DataItemKey.translateDesc(DataItemKey.v2401)));
        // 曲轴转速
        item.put(DataItemKey.v2411, vehicleItemMap.get(DataItemKey.v2411));
        // 燃料消耗率
        item.put(DataItemKey.v2413, vehicleItemMap.get(DataItemKey.v2413));
        // 当前车辆位置
        item.put(DataItemKey.ADDRESS, vehicleItemMap.get(DataItemKey.ADDRESS));
        // 反应剂余量
        item.put(DataItemKey.v60033, vehicleItemMap.get(DataItemKey.v60033));
        // 发动机冷却液温度
        item.put(DataItemKey.v60038, vehicleItemMap.get(DataItemKey.v60038));
        // 油箱液位
        item.put(DataItemKey.v60039, vehicleItemMap.get(DataItemKey.v60039));
        // 发动机摩擦扭矩
        item.put(DataItemKey.v60028, vehicleItemMap.get(DataItemKey.v60028));
        // 发动机转速
        item.put(DataItemKey.v60029, vehicleItemMap.get(DataItemKey.v60029));
        // SCR 入口温度
        item.put(DataItemKey.v60035, vehicleItemMap.get(DataItemKey.v60035));
        // SCR 出口温度
        item.put(DataItemKey.v60036, vehicleItemMap.get(DataItemKey.v60036));
        // 燃料电池电压
        item.put(DataItemKey.v2110, vehicleItemMap.get(DataItemKey.v2110));
        // 燃料电池电流
        item.put(DataItemKey.v2111, vehicleItemMap.get(DataItemKey.v2111));
        // 氢气最高浓度
        item.put(DataItemKey.v2117, vehicleItemMap.get(DataItemKey.v2117));
        // 电机控制器温度
        item.put(DataItemKey.v2302, vehicleItemMap.get(DataItemKey.v2302));
        // 电机温度
        item.put(DataItemKey.v2304, vehicleItemMap.get(DataItemKey.v2304));

    }

    /**
     * 解析2003 2103数据列表
     *
     * @param item
     * @param key
     * @param value
     */
    private void fill2003And2103(Map<String, Object> item, String key, String value) {
        exceptionWrap(() -> {
            double[] item2103 = VehicleItemResolver.parse2003And2103(value);
            item.put(key, item2103);
        });
    }

    /**
     * 解析2114 燃料电池温度值列表
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fill2114(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        exceptionWrap(() -> {
            double[] item2114 = VehicleItemResolver.parse2114(vehicleItemMap.get(DataItemKey.v2114));
            item.put(DataItemKey.v2114, item2114);
        });
    }

    /**
     * 解析驱动总成
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fill2308(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        exceptionWrap(() -> {
            String value = vehicleItemMap.get(DataItemKey.v2308);
            Map<String, String> item2308Map = VehicleItemResolver.parse2308(value);
            item.put(DataItemKey.POWER_LOSS_STATE, item2308Map.get(DataItemKey.POWER_LOSS_STATE));
            item.put(DataItemKey.MAX_ROTATING_SPEED, item2308Map.get(DataItemKey.MAX_ROTATING_SPEED));
            item.put(DataItemKey.MAX_ROTATING_TORQUE, item2308Map.get(DataItemKey.MAX_ROTATING_TORQUE));
            item.put(DataItemKey.v2302, item2308Map.get(DataItemKey.MAX_2302));
            item.put(DataItemKey.v2304, item2308Map.get(DataItemKey.MAX_2304));
        });
    }

    /**
     * 2203 档位解析
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fill2203(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        exceptionWrap(() -> {
            String value = vehicleItemMap.get(DataItemKey.translateDesc(DataItemKey.v2203));
            if (StringUtils.isNotEmpty(value)) {
                if (value.contains(" ")) {
                    String[] tmp = value.split(" ");
                    item.put(DataItemKey.v2203, tmp[tmp.length - 1]);
                } else {
                    item.put(DataItemKey.v2203, value);
                }
            } else {
                item.put(DataItemKey.v2203, "");
            }
        });
    }

    /**
     * 高压DC-DC状态解析
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fill2121(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        exceptionWrap(() -> {
            String value = vehicleItemMap.get(DataItemKey.v2121);
            String desc2121 = "关闭";
            if (StringUtils.isNotBlank(value)) {
                if ("1".equals(value)) {
                    desc2121 = "工作";
                }
            }
            item.put(DataItemKey.v2121, desc2121);
        });
    }

    /**
     * 填充动力方式
     *
     * @param item
     * @param vid
     */
    private void fill2213(Map<String, Object> item, String vid) {
        PowerMode powerMode = findVehiclePowerMode(vid);
        if (powerMode != null) {
            item.put(DataItemKey.v2213, powerMode.getVal());
        }
    }

    /**
     * 填充车辆状态
     *
     * @param item
     * @param vehicleItemMap
     */
    private void fillVehicleStatus(Map<String, Object> item, Map<String, String> vehicleItemMap) {
        exceptionWrap(() -> {
            String speed = vehicleItemMap.get(DataItemKey.v2201);
            if (NumberUtils.isNumber(speed) && Double.parseDouble(speed) > 0) {
                item.put(DataItemKey.v2201, Double.parseDouble(speed));
                item.put("vehicleStatus", "行驶中");
            } else {
                item.put(DataItemKey.v2201, 0);
                item.put("vehicleStatus", "静止");
            }
        });
    }

    // endregion

    // region 格式转换

    /**
     * 单位转换
     *
     * @param value
     * @param unit
     * @return
     */
    private String unitConversion(String value, String unit) {
        if (StringUtils.isBlank(value)) {
            return "0" + unit;
        }
        try {
            return Double.valueOf(value).toString() + unit;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    // endregion

    // endregion

    // region 获取可监控经纬度

    @Autowired
    private IVehicleRealStatusService vehicleRealStatusService;

    @Autowired
    private IRedisVehicleCache redisVehicleCache;

    @Override
    public Collection<VehicleGeo> getGeo() {
        Map<String, VehicleGeo> res = new HashMap<>();
        List<Map<String, Object>> datas = vehicleRealStatusService.vehOnMap(new PagerInfo(), true);
        if (datas.isEmpty()) {
            return res.values();
        }
        String[] queryVids = new String[datas.size()];
        for (int idx = 0; idx < datas.size(); idx++) {
            Map<String, Object> item = datas.get(idx);
            VehicleGeo geo = new VehicleGeo();
            geo.setVid(convertString(item.get("uuid")));
            geo.setVin(convertString(item.get("vin")));
            geo.setLicensePlate(convertString(item.get("interNo")));
            geo.setLat(convertDouble(item.get("lat")));
            geo.setLon(convertDouble(item.get("lng")));
            geo.setStatus(convertInteger(item.get("onlneStatus")));
            geo.setPowerMode(item.get("powerMode")==null?1:Integer.parseInt(item.get("powerMode").toString()));
            geo.setSpeed(0d);
            res.put(geo.getVid(), geo);
            queryVids[idx] = geo.getVid();
        }
        // 从redis读取车辆状态填充
        try {
            Map<String, Map<String, String>> statusMap = redisVehicleCache.redisAllVehicle();
            if (statusMap.isEmpty()) {
                return res.values();
            }
            statusMap.forEach((key, value) -> {
                VehicleGeo geo = res.get(key);
                if (geo == null) {
                    return;
                }
                Object online = geo.getStatus();
                Object chargeState = value.get(DataItemKey.v2301);
                Object alarmState = value.get(DataItemKey.v10001);

                if (online != null && "1".equals(online.toString()) && alarmState != null && "1".equals(alarmState)) {
                    geo.setStatus(4);
                } else {
                    boolean isCharge = chargeState != null && ("1".equals(chargeState) || "2".equals(chargeState) || "4".equals(chargeState));
                    if (isCharge) {
                        geo.setStatus(3);
                    } else if (online != null && "1".equals(online.toString())) {
                        geo.setStatus(1);
                    } else {
                        geo.setStatus(2);
                    }
                }
                geo.setSpeed(StringUtils.isBlank(value.get(DataItemKey.v2201))?
                        0:Double.parseDouble(value.get(DataItemKey.v2201)));
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return res.values();
    }

    // region 格式转换

    private String convertString(Object src) {
        if (src == null) {
            return null;
        }
        return src + "";
    }

    private Double convertDouble(Object src) {
        if (src == null) {
            return null;
        }
        try {
            return Double.valueOf(src + "");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private Integer convertInteger(Object src) {
        if (src == null) {
            return null;
        }
        try {
            return Integer.valueOf(src + "");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    // endregion

    // endregion

}
