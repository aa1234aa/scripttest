package com.bitnei.cloud.screen.service;

import java.util.Map;

/**
 * @author xuzhijie
 */
public interface IRedisVehicleCache {

    /**
     * 缓存redis所有车辆
     *
     * @return
     */
    Map<String, Map<String, String>> redisAllVehicle();

}
