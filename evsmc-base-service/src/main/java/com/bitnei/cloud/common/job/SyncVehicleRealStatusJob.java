package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.sys.model.DictModel;
import com.bitnei.cloud.sys.service.IDictService;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Desc： 同步車輛实时状态
 * @Author: dx
 * @Date: 2019/3/18
 */
@Slf4j
@RequiredArgsConstructor
@ElasticJobConf(name = "SyncVehicleRealStatusJob", shardingTotalCount = 2, cron = "0 0/3 * * * ?",
        description = "车辆实时状态同步", eventTraceRdbDataSource = "druidDataSource")

@Component
public class SyncVehicleRealStatusJob implements SimpleJob {

    private final RealDataClient realDataClient;
    @Value("${vehicleRealStatus.sync.pageSize:100}")
    private int pageSize;
    private String gaode = ".gd";
    //车辆gps归属区域
    private String gpsRegion = "GPS_REGION";
    private final IVehicleRealStatusService vehicleRealStatusService;
    private final IDictService dictService;

    @Value("${SyncVehicleRealStatusJob.log:0}")
    private int myLog;

    public void processSync(int shardingItem) {
        try {
            if (!JobCheckUtil.getJobEnableStatus(getClass())) {
                return;
            }

            long begin = System.currentTimeMillis();

            int index = 0;
            List<Map<String, Object>> vehList;
            List<Map<String, Object>> g6VehList;
            int total = 0;
            List<DictModel> dictModels = dictService.findByDictType("GPS_IS_LOCATE");
            do {
                Map<String, Object> params = Maps.newHashMap();
                params.put("limit", pageSize);
                params.put("sharding", shardingItem);
                params.put("offset", index * pageSize);

                vehList = vehicleRealStatusService.listSimple(params);
                if (CollectionUtils.isNotEmpty(vehList)) {
                    total += vehList.size();
                }

                //国六的车辆
                g6VehList = vehList.stream().filter(it -> {
                    if (it.containsKey("ruleTypeName") && null != it.get("ruleTypeName")) {
                        String ruleTypeName = it.get("ruleTypeName").toString();
                        return ruleTypeName.contains("国六") || ruleTypeName.contains("17691");
                    }
                    return false;
                }).collect(Collectors.toList());

                //国标的车辆
                vehList.removeAll(g6VehList);



                //同步国标的车辆状态信息
                processGbVehList(vehList, dictModels);

                //同步国六的车辆状态信息
                processG6VehList(g6VehList, dictModels);

                index++;
            } while (CollectionUtils.isNotEmpty(vehList) || CollectionUtils.isNotEmpty(g6VehList));

            long end = System.currentTimeMillis();
            log.info("同步车辆状态完成，数量{} 耗时{}s", total, (end - begin) / 1000);

        } catch (Exception e) {
            log.error("同步车辆实时状态出错：", e);
        }
    }

    /**
     * 同步国六的车辆状态信息
     * @param g6VehList
     */
    private void processG6VehList(List<Map<String, Object>> g6VehList, List<DictModel> dictModels) {

        if (CollectionUtils.isNotEmpty(g6VehList)) {

            String[] uuids = g6VehList.stream().map(t -> t.get("uuid").toString()).toArray(String[]::new);

            DataRequest dataRequest = new DataRequest();
            dataRequest.setReadMode(DataReadMode.GPS_OFFSET_GD | DataReadMode.TRANSLATE);
            dataRequest.setColumns(Arrays.asList(gpsRegion,
                    DataItemKey.G6_ITEM_ServerTime, DataItemKey.OnlineState, DataItemKey.G6_ITEM_60043,
                    DataItemKey.G6_ITEM_60042, DataItemKey.G6_ITEM_60041, DataItemKey.G6_ITEM_60040,
                    DataItemKey.desc(DataItemKey.G6_ITEM_60040)));
            dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
            dataRequest.setVids(uuids);
            Map<String, Map<String, String>> redisDatas = realDataClient.findByUuids(dataRequest);

            if (redisDatas != null && redisDatas.size() > 0) {

                List<Object> paraList = new ArrayList<>();
                Map<String,String> regionCodes=Maps.newHashMap();

                g6VehList.forEach(vehicleMap -> {

                    String uuid = vehicleMap.get("uuid").toString();

                    Map<String, String> valMap = redisDatas.get(uuid);

                    if (valMap != null && valMap.size() > 0 &&
                            StringUtils.isNotEmpty(valMap.get(DataItemKey.G6_ITEM_ServerTime))) {

                        Map<String, Object> para = new HashMap<>();
                        para.put("vehicleId", vehicleMap.get("id"));
                        String updateTime = null;

                        //最后通讯时间
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.G6_ITEM_ServerTime))) {
                            String timeVal=valMap.get(DataItemKey.G6_ITEM_ServerTime);
                            if (!timeVal.contains("-")){
                                //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                                timeVal= DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                            }
                            updateTime = timeVal;
                        }

                        para.put("updateTime", updateTime);
                        Double mils = null, lng = null, lat = null;
                        Integer onlineStatus = 2;

                        //里程
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.G6_ITEM_60043))) {
                            mils = Double.valueOf(valMap.get(DataItemKey.G6_ITEM_60043));
                        }

                        //在线状态
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.OnlineState))) {
                            onlineStatus = Integer.valueOf(valMap.get(DataItemKey.OnlineState));
                            if (onlineStatus == 0) {
                                //离线
                                onlineStatus = 2;
                            }
                        }

                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.G6_ITEM_60041 + gaode))) {
                            lng = Double.valueOf(valMap.get(DataItemKey.G6_ITEM_60041 + gaode));
                        }
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.G6_ITEM_60042 + gaode))) {
                            lat = Double.valueOf(valMap.get(DataItemKey.G6_ITEM_60042 + gaode));
                        }

                        //String region = syncGpsRegion(valMap.get(gpsRegion));
                        if (StringUtils.isNotBlank(valMap.get(gpsRegion))){
                            String r=getRegionCode(valMap.get(gpsRegion));
                            if (StringUtils.isNotBlank(r)){
                                regionCodes.put(r,"");
                                para.put("region", r);
                            }

                        }
                        Integer onlined = (Integer) vehicleMap.getOrDefault("onlined", 0);
                        String vehUpdateTime = vehicleMap.getOrDefault("update_time", "").toString();

                        boolean onlinedButNullRegTime = onlined == 1 && StringUtils.isBlank(
                                vehicleMap.getOrDefault("firstRegTime", "").toString());

                        boolean notOnlinedButHasUpdatetime = onlined == 0 && (StringUtils.isNotBlank(vehUpdateTime)
                                || StringUtils.isNotBlank(valMap.get(DataItemKey.OnlineState))
                                || StringUtils.isNotBlank(updateTime)
                                || (lng != null && lng > 0)
                                || (mils != null && mils > 0)
                        );

                        if (onlinedButNullRegTime || notOnlinedButHasUpdatetime) {
                            //说明改车辆其实已经上过线了
                            //para.put("onlined", 1);
                            if (StringUtils.isBlank(vehUpdateTime)) {
                                vehUpdateTime = updateTime;
                                if (StringUtils.isBlank(vehUpdateTime)) {
                                    vehUpdateTime = DateUtil.getNow();
                                }
                            }
                            //para.put("firstRegTime", vehUpdateTime);

                        }

                        //同步GPS是否定位说明
                        if (valMap.containsKey(DataItemKey.desc(DataItemKey.G6_ITEM_60040)) &&
                                StringUtils.isNotEmpty(valMap.get(DataItemKey.desc(DataItemKey.G6_ITEM_60040)))) {

                            String desc = valMap.get(DataItemKey.desc(DataItemKey.G6_ITEM_60040));
                            if (CollectionUtils.isNotEmpty(dictModels)) {
                                for (DictModel dictModel: dictModels) {
                                    if (dictModel.getName().equals(desc)) {
                                        para.put("isGps", dictModel.getValue());
                                    }
                                }
                            }
                        }

                        //para.put("region", region);
                        para.put("mils", mils);
                        para.put("onlineStatus", onlineStatus);
                        para.put("lng", lng);
                        para.put("lat", lat);
                        paraList.add(para);
                        if (myLog == 1 && para.get("updateTime") != null) {
                            log.info("vehicleId:{},uuid:{},redis:{},sql:{}", vehicleMap.get("id"), uuid, redisDatas.get(uuid), para);

                        }


                    } else if (StringUtils.isNotBlank((String) vehicleMap.get("update_time"))) {
                        //数据库中有值,但是cto缓存里没有,说明出现了某种错误(北京现代发现有这种情况),将数据库中的实时状态数据重置
                        resetVehStatus(vehicleMap);
                    }
                });
                if (paraList.size() > 0) {
                    if (regionCodes.size()>0){
                        Map<String,String>  batchRegion= regionMap(Stream.of(regionCodes.keySet().toArray(new String[0])).collect(Collectors.toList()));

                        paraList.forEach(f->{
                            Object value=((Map<String,Object>)f).get("region");
                            if ( null!=value&&StringUtils.isNotBlank(value.toString())){
                                String realRegion=batchRegion.get(value.toString());
                                if (StringUtils.isNotBlank(realRegion)){
                                    ((Map<String,Object>)f).put("region",realRegion);
                                }
                            }

                        });
                    }
                    vehicleRealStatusService.batchUpdate("updateByVehicleId", paraList);
                }


            }
        }
    }

    /**
     * 重置车辆状态
     * @param vehicleMap
     */
    private void resetVehStatus(Map<String, Object> vehicleMap) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("vehicleId", vehicleMap.get("id"));
            param.put("updateTime", "");
            //param.put("firstRegTime", "");
            //param.put("onlined", 0);
            param.put("region", "null");
            param.put("mils", 0);
            param.put("onlineStatus", 2);
            param.put("lng", 0);
            param.put("lat", 0);
            vehicleRealStatusService.update("updateByVehicleId", param);
//            log.info("vehicleId:{},重置了实时状态数据", vehicleMap.get("id"));
        } catch (Exception e) {
            log.error("重置车辆实时状态出错：", e);
        }
    }

    /**
     * 同步国标的车辆状态信息
     * @param vehList
     */
    private void processGbVehList(List<Map<String, Object>> vehList, List<DictModel> dictModels) {

        if (CollectionUtils.isNotEmpty(vehList)) {

            String[] uuids = vehList.stream().map(t -> t.get("uuid").toString()).toArray(String[]::new);

            DataRequest dataRequest = new DataRequest();
            dataRequest.setReadMode(DataReadMode.GPS_OFFSET_GD | DataReadMode.TRANSLATE);
            dataRequest.setColumns(Arrays.asList(gpsRegion,
                    DataItemKey.ServerTime, DataItemKey.OnlineState, DataItemKey.VehicleDistance,
                    DataItemKey.Lat, DataItemKey.Lng, DataItemKey.LocateState,
                    DataItemKey.desc(DataItemKey.LocateState)));
            dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            dataRequest.setVids(uuids);
            Map<String, Map<String, String>> redisDatas = realDataClient.findByUuids(dataRequest);

            if (redisDatas != null && redisDatas.size() > 0) {
                Map<String,String> regionCodes=Maps.newHashMap();

                List<Object> paraList = new ArrayList<>();
                vehList.forEach(vehicleMap -> {

                    String uuid = vehicleMap.get("uuid").toString();

                    Map<String, String> valMap = redisDatas.get(uuid);

                    if (valMap != null && valMap.size() > 0 &&
                            StringUtils.isNotEmpty(valMap.get(DataItemKey.ServerTime))) {

                        Map<String, Object> para = new HashMap<>();
                        para.put("vehicleId", vehicleMap.get("id"));
                        String updateTime = null;

                        //最后通讯时间
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.ServerTime))) {
                            String timeVal=valMap.get(DataItemKey.ServerTime);
                            if (!timeVal.contains("-")){
                                //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                                timeVal= DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                            }
                            updateTime = timeVal;
                        }

                        para.put("updateTime", updateTime);
                        Double mils = null, lng = null, lat = null;
                        Integer onlineStatus = 2;

                        //里程
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.VehicleDistance))) {
                            mils = Double.valueOf(valMap.get(DataItemKey.VehicleDistance));
                        }

                        //在线状态
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.OnlineState))) {
                            onlineStatus = Integer.valueOf(valMap.get(DataItemKey.OnlineState));
                            if (onlineStatus == 0) {
                                //离线
                                onlineStatus = 2;
                            }
                        }

                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.Lng + gaode))) {
                            lng = Double.valueOf(valMap.get(DataItemKey.Lng + gaode));
                        }
                        if (StringUtils.isNotEmpty(valMap.get(DataItemKey.Lat + gaode))) {
                            lat = Double.valueOf(valMap.get(DataItemKey.Lat + gaode));
                        }

                        //String region = syncGpsRegion(valMap.get(gpsRegion));
                        if (StringUtils.isNotBlank(valMap.get(gpsRegion))){
                            String r=getRegionCode(valMap.get(gpsRegion));
                            if (StringUtils.isNotBlank(r)){
                                regionCodes.put(r,"");
                                para.put("region", r);
                            }

                        }
                        Integer onlined = (Integer) vehicleMap.getOrDefault("onlined", 0);
                        String vehUpdateTime = vehicleMap.getOrDefault("update_time", "").toString();

                        boolean onlinedButNullRegTime = onlined == 1 && StringUtils.isBlank(vehicleMap.getOrDefault("firstRegTime", "").toString());
                        boolean notOnlinedButHasUpdatetime = onlined == 0 && (StringUtils.isNotBlank(vehUpdateTime)
                                || StringUtils.isNotBlank(valMap.get(DataItemKey.OnlineState))
                                || StringUtils.isNotBlank(updateTime)
                                || (lng != null && lng > 0)
                                || (mils != null && mils > 0)
                        );

                        if (onlinedButNullRegTime || notOnlinedButHasUpdatetime) {
                            //说明改车辆其实已经上过线了
                            //para.put("onlined", 1);
                            if (StringUtils.isBlank(vehUpdateTime)) {
                                vehUpdateTime = updateTime;
                                if (StringUtils.isBlank(vehUpdateTime)) {
                                    vehUpdateTime = DateUtil.getNow();
                                }
                            }
                            //para.put("firstRegTime", vehUpdateTime);

                        }

                        //同步GPS是否定位说明
                        if (valMap.containsKey(DataItemKey.desc(DataItemKey.LocateState)) &&
                                StringUtils.isNotEmpty(valMap.get(DataItemKey.desc(DataItemKey.LocateState)))) {

                            String desc = valMap.get(DataItemKey.desc(DataItemKey.LocateState));
                            if (CollectionUtils.isNotEmpty(dictModels)) {
                                for (DictModel dictModel: dictModels) {
                                    if (dictModel.getName().equals(desc)) {
                                        para.put("isGps", dictModel.getValue());
                                    }
                                }
                            }

                        }

                        //para.put("region", region);
                        para.put("mils", mils);
                        para.put("onlineStatus", onlineStatus);
                        para.put("lng", lng);
                        para.put("lat", lat);
                        paraList.add(para);
                        if (myLog == 1 && para.get("updateTime") != null) {
                            log.info("vehicleId:{},uuid:{},redis:{},sql:{}", vehicleMap.get("id"), uuid, redisDatas.get(uuid), para);

                        }


                    } else if (StringUtils.isNotBlank((String) vehicleMap.get("update_time"))) {
                        //数据库中有值,但是cto缓存里没有,说明出现了某种错误(北京现代发现有这种情况),将数据库中的实时状态数据重置
                        resetVehStatus(vehicleMap);
                    }
                });
                if (paraList.size() > 0) {
                    if (regionCodes.size()>0){
                        Map<String,String>  batchRegion= regionMap(Stream.of(regionCodes.keySet().toArray(new String[0])).collect(Collectors.toList()));

                        paraList.forEach(f->{
                            Object value=((Map<String,Object>)f).get("region");
                            if ( null!=value&&StringUtils.isNotBlank(value.toString())){
                                String realRegion=batchRegion.get(value.toString());
                                if (StringUtils.isNotBlank(realRegion)){
                                    ((Map<String,Object>)f).put("region",realRegion);
                                }
                            }

                        });
                    }
                    vehicleRealStatusService.batchUpdate("updateByVehicleId", paraList);
                }


            }
        }
    }

    private String getRegionCode(String r){
        String tmp=r;
        if (StringUtils.isBlank(r)) {
            tmp = "null";
        }
        String[] regionArr = r.split("\\|");
        if (regionArr.length > 0 && StringUtils.isNumeric(regionArr[0])) {
            tmp = regionArr[0];
            if (regionArr.length > 1 && StringUtils.isNumeric(regionArr[1])) {
                //优先保存市级别区域码
                tmp = regionArr[1];
            }
        }
        return tmp;
    }
    private Map<String,String> regionMap(List<String> regionCode){

        Map<String,String> map=Maps.newHashMap();

        try {
            //long begin=System.currentTimeMillis();
            List<Map<String,String>> results=vehicleRealStatusService.findAreaByCodes(regionCode);
            //long end=System.currentTimeMillis();
            //log.info("获取区域关系，数量{} 耗时{}s", results!=null?results.size():0, (end - begin) / 1000);
            if (CollectionUtils.isNotEmpty(results)){
                results.forEach(f->{
                    if (StringUtils.isNotBlank(f.get("code"))){
                        map.put(f.get("code"),f.get("id"));
                    }

                });
            }

        }
        catch (Exception ex) {
            log.error("findAreaByCodes error:", ex);

        }
        return map;
    }

    private String syncGpsRegion(String regionCode) {
        if (StringUtils.isBlank(regionCode)) {
            regionCode = "null";
        }
        String[] regionArr = regionCode.split("\\|");
        if (regionArr.length > 0 && StringUtils.isNumeric(regionArr[0])) {
            regionCode = regionArr[0];
            if (regionArr.length > 1 && StringUtils.isNumeric(regionArr[1])) {
                //优先保存市级别区域码
                regionCode = regionArr[1];
            }
            try {
                Map<String, String> regionMap = vehicleRealStatusService.findAreaByCode(regionCode);
                if (regionMap != null) {
                    regionCode = regionMap.getOrDefault("id", regionCode);
                }
            } catch (Exception ex) {
                log.error("findAreaByCode error:", ex);
            }
        }
        return regionCode;
    }


    @Override
    public void execute(ShardingContext shardingContext) {

        log.info("车辆实时状态同步分片参数:{}", shardingContext.getShardingItem());
        processSync(shardingContext.getShardingItem());
        // processSync(1);
        syncVehicle();


    }

    /***
     * 定时同步基础车辆信息到实时状态表
     */
    private void syncVehicle() {
        vehicleRealStatusService.insert("syncVehicle", null);
    }
}
