package com.bitnei.cloud.screen.protocol;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuzhijie
 */
public class VehicleItemResolver {

    /**
     * 解析2003，电压,温度
     *
     * @param item2003 原始值
     * @return 解析之后值
     */
    public static double[] parse2003And2103(String item2003) {
        List<Double> battery = new ArrayList<>();
        if (StringUtils.isBlank(item2003)) {
            return new double[0];
        }
        String[] dataArr = item2003.split("\\|");
        for (String s : dataArr) {
            if( !s.contains(":") ){
                continue;
            }
            String[] strings = s.split(":")[1].split("_");
            for (String string : strings) {
                battery.add(Double.valueOf(string));
            }
        }
        double[] result = new double[battery.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = battery.get(i);
        }
        return result;
    }

    /**
     * 解析2114，燃料电池温度值列表
     *
     * @param item2114 原始值
     * @return 解析之后值
     */
    public static double[] parse2114(String item2114) {
        List<Double> battery = new ArrayList<>();
        if (StringUtils.isBlank(item2114)) {
            return new double[0];
        }
        String[] dataArr = item2114.split("\\|");
        for (String s : dataArr) {
            if( StringUtils.isEmpty(s) ){
                continue;
            }
            battery.add(Double.valueOf(s));
        }
        double[] result = new double[battery.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = battery.get(i);
        }
        return result;
    }

    private static final Map<String, String> DEFAULT_2308 = Maps.newHashMap();
    static {
        DEFAULT_2308.put(DataItemKey.POWER_LOSS_STATE, "关闭状态");
        DEFAULT_2308.put(DataItemKey.MAX_ROTATING_SPEED, "0");
        DEFAULT_2308.put(DataItemKey.MAX_ROTATING_TORQUE, "0");
    }
    /**
     * 解析 2308 总成信息 电量损耗 转速
     * motor
     * @param item2308 2308总成信息
     * @return 总成里的key/value值
     */
    public static Map<String, String> parse2308(String item2308) {
        if (StringUtils.isEmpty(item2308)) {
            return DEFAULT_2308;
        }
        List<Map<String, String>> cache = new ArrayList<>(10);
        // 驱动电机列表
        String[] motors = item2308.split("\\|");
        if( motors.length == 0 ){
            return DEFAULT_2308;
        }
        // 如果其中一个驱动电机耗电，其状态就是耗电
        boolean powerLoss = false;
        // 最大转速
        double maxRotatingSpeed = 0;
        double maxRotatingTorque = 0;
        double max2302 = 0;
        double max2304 = 0;
        for (final String motor : motors) {
            // 解析单个驱动电机key/val
            Map<String, String> motorItem = new HashMap<>(10);
            String[] itemArray = motor.split(",");
            for (final String item : itemArray) {
                String[] keyVal = item.split(":");
                if( keyVal.length == 1 ){
                    motorItem.put(keyVal[0], null);
                }else{
                    motorItem.put(keyVal[0], keyVal[1]);
                }
            }

            // 判断驱动电机状态
            if( !powerLoss && motorItem.containsKey(DataItemKey.v2310) ){
                if( "1".equals(motorItem.get(DataItemKey.v2310)) ){
                    powerLoss = true;
                }
            }
            // 取大转速
            if( motorItem.containsKey(DataItemKey.v2303) && StringUtils.isNotEmpty(motorItem.get(DataItemKey.v2303))){
                double rotatingSpeed = Double.valueOf(motorItem.get(DataItemKey.v2303));
                if( rotatingSpeed > maxRotatingSpeed ){
                    maxRotatingSpeed = rotatingSpeed;
                }
            }
            // 取大转矩
            if( motorItem.containsKey(DataItemKey.v2311) && StringUtils.isNotEmpty(motorItem.get(DataItemKey.v2311))){
                double rotatingTorque = Double.valueOf(motorItem.get(DataItemKey.v2311));
                if( rotatingTorque > maxRotatingTorque ){
                    maxRotatingTorque = rotatingTorque;
                }
            }
            // 取驱动电机控制器温度-最大温度
            if( motorItem.containsKey(DataItemKey.v2302) && StringUtils.isNotEmpty(motorItem.get(DataItemKey.v2302))){
                double v2302 = Double.valueOf(motorItem.get(DataItemKey.v2302));
                if( v2302 > max2302 ){
                    max2302 = v2302;
                }
            }
            // 取驱动电机温度-最大温度
            if( motorItem.containsKey(DataItemKey.v2304) && StringUtils.isNotEmpty(motorItem.get(DataItemKey.v2304))){
                double v2304 = Double.valueOf(motorItem.get(DataItemKey.v2304));
                if( v2304 > max2304 ){
                    max2304 = v2304;
                }
            }
            cache.add(motorItem);
        }
        for (final Map<String, String> item : cache) {
            if( powerLoss ){
                item.put(DataItemKey.POWER_LOSS_STATE, "耗电状态");
            }else{
                item.put(DataItemKey.POWER_LOSS_STATE, "关闭状态");
            }
            item.put(DataItemKey.MAX_ROTATING_SPEED, maxRotatingSpeed + "");
            item.put(DataItemKey.MAX_ROTATING_TORQUE, maxRotatingTorque + "");
            item.put(DataItemKey.MAX_2302, max2302 + "");
            item.put(DataItemKey.MAX_2304, max2304 + "");
        }
        return cache.get(0);
    }

}
