package com.bitnei.cloud.screen.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.feginInterface.DataAccessInterface;
import com.bitnei.cloud.screen.protocol.DataItemKey;
import com.bitnei.cloud.screen.service.IRedisVehicleCache;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xuzhijie
 */
@Slf4j
@Service
public class RedisVehicleCacheImpl implements IRedisVehicleCache {

    @Resource
    private RealDataClient realDataClient;

    @Resource
    private DataAccessInterface dataAccessInterface;
    private static final String[] DATA_ITEM_ARRAY = new String[]{
        DataItemKey.v2301,
        DataItemKey.v10001,
            DataItemKey.v2201
    };

    /**
     * 按以前的逻辑，缓存5分钟redis的车辆数据，供大屏使用
     *
     * @return
     */
    @Override
    @Cached(cacheType = CacheType.LOCAL, expire = 5, timeUnit = TimeUnit.MINUTES)
    public Map<String, Map<String, String>> redisAllVehicle() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setColumns(Arrays.asList(DATA_ITEM_ARRAY));
        dataRequest.setReadMode(DataReadMode.NONE);
        dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());

        Map<String, Map<String, String>> g6= dataAccessInterface.findRealDataByAllVehicle(dataRequest);
        dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
        Map<String, Map<String, String>> gb= dataAccessInterface.findRealDataByAllVehicle(dataRequest);
        if (gb==null){
            gb= Maps.newHashMap();
        }
        else {
            log.info("gb redisAllVehicle {}",gb.size());
        }
        if (g6==null){
            g6=Maps.newHashMap();
        }
        else {
            log.info("g6 redisAllVehicle {}",g6.size());
        }
        final Map<String, Map<String, String>> tmp=gb;
        g6.forEach((k,v)->{
            tmp.put(k,v);
        });

        return gb;
    }
}
