package com.bitnei.cloud.compent.kafka.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.druid.util.StringUtils;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.GpsUtil;
import com.bitnei.cloud.screen.protocol.StringUtil;
import com.bitnei.cloud.sys.dao.*;
import com.bitnei.cloud.sys.domain.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import jodd.json.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 异常车辆通知处理
 *
 * ██╗  ██╗███████╗██████╗
 * ╚██╗██╔╝╚══███╔╝██╔══██╗
 *  ╚███╔╝   ███╔╝ ██████╔╝
 *  ██╔██╗  ███╔╝  ██╔═══╝
 * ██╔╝ ██╗███████╗██║
 * ╚═╝  ╚═╝╚══════╝╚═╝
 *
 * @author Lijiezhou
 * @date 2019-03-02
 */
@Slf4j
@Service
public class AbnormalVehicleHandler extends AbstractKafkaHandler  {

    private final @NotNull VehicleMapper vehicleMapper;
    private final @NotNull VehNotCanMapper vehNotCanMapper;
    private final @NotNull VehNotPositionMapper vehNotPositionMapper;
    private final @NotNull SocVehicleLogMapper socVehicleLogMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AbnormalVehicleHandler(
        final @NotNull VehicleMapper vehicleMapper,
        final @NotNull VehNotCanMapper vehNotCanMapper,
        final @NotNull VehNotPositionMapper vehNotPositionMapper,
        final @NotNull SocVehicleLogMapper socVehicleLogMapper) {

        this.vehicleMapper = vehicleMapper;
        this.vehNotCanMapper = vehNotCanMapper;
        this.vehNotPositionMapper = vehNotPositionMapper;
        this.socVehicleLogMapper = socVehicleLogMapper;
    }

    @Override
    public boolean handle(final @Nullable String message) {

        if (org.apache.commons.lang3.StringUtils.isBlank(message)) {
            return true;
        }

        try {
            final Map<String, Object> map = new JsonParser().parse(message);
            final String msgType = map
                .getOrDefault(
                    "msgType",
                    ""
                )
                .toString()
                .trim();
            switch (msgType) {
                case "SOC_ALARM":
                    //SOC过低消息
                    socAlarm(map);
                    break;
                case "NO_CAN_VEH":
                    //无CAN车辆
                    notCanVehCustomer(map);
                    break;
                case "NO_POSITION_VEH":
                    //未定位车辆
                    notPositionVehCustomer(
                        map,
                        msgType
                    );
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(
                "error",
                e
            );
        }

        return true;
    }

    /**
     * SOC过低消息
     */
    private void socAlarm(final @NotNull Map<String, Object> map) {
        //消息ID，作主键ID用
        final String msgId = map.getOrDefault("msgId","").toString().trim();
        //vid
        final String vid = map.getOrDefault("vid", "").toString().trim();
        //开始时间
        final String startTime = map.getOrDefault("beginTime", "").toString().trim();
        //结束时间
        final String endTime = map.getOrDefault("endTime", "").toString().trim();
        //开始soc
        final String startSoc = map.getOrDefault("beginSoc", "").toString().trim();
        //结束soc
        final String endSoc = map.getOrDefault("endSoc", "").toString().trim();
        //状态,1：警开始 2：持续中 3：结束 0保留状态
        final int status = Integer.parseInt(map.getOrDefault("status", "0").toString().trim());
        //位置，经度在前，纬度在后
        final String location = map.getOrDefault("beginLocation", "").toString().trim();
        //SOC过低阈值
        final String lowSocThreshold = map.getOrDefault("beginThreshold", "").toString().trim();

        final Vehicle vehicle = vehicleMapper.findByUuid(ImmutableMap.of("uuid",vid));
        if (vehicle ==null){
            log.warn("该车辆在系统中不存在,vid : {}", vid);
            return;
        }
        if (StringUtils.isEmpty(startTime)) {
            log.warn("SOC低电量通知开始时间为空,车牌号: {}", vehicle.getLicensePlate());
            return;
        }
        //告警开始
        final Date startDate = DateUtil.strToDate_ex(startTime);
        final String beginTime = DateUtil.getDate(startDate.getTime());
        if (status == 1 || status == 2) {
            final SocVehicleLog records = socVehicleLogMapper.getByVid(
                ImmutableMap.of(
                    "uuid",
                    vid,
                    "beginTime",
                    beginTime
                ));
            if (records != null){
                log.info("SOC低电量车辆出现重复通知不作处理,车牌号: {}", vehicle.getLicensePlate());
                return;
            }

            final SocVehicleLog svl = new SocVehicleLog();
            svl.setId(IdUtil.simpleUUID());
            svl.setVid(vid);
            svl.setStartTime(beginTime);
            svl.setCreateTime(DateUtil.getNow());
            svl.setStartSoc(startSoc);
            svl.setSocThreshold(Float.parseFloat(lowSocThreshold));
            svl.setStatus(0);
            final String[] lntLat = location.split(",");
            if (lntLat.length >= 2) {
                svl.setLng(lntLat[0]);
                svl.setLat(lntLat[1]);
                if (NumberUtils.isCreatable(lntLat[0]) && NumberUtils.isCreatable(lntLat[1])){
                    final double lng = NumberUtils.toDouble(lntLat[0]);
                    final double lat = NumberUtils.toDouble(lntLat[1]);
                    svl.setLocation(GpsUtil.getAddress(String.valueOf(lng),String.valueOf(lat)));
                }
                else{
                    svl.setLocation("");
                }
            }
            socVehicleLogMapper.insert(svl);

            //todo app的soc只提醒开始的
//            if (status == 1) {
//                socVehicleLogService.addSocRemind(sysSocVehicleLog);
//            }

        }
        else if (status ==3){
            if (StringUtils.isEmpty(endTime)) {
                log.warn("SOC低电量通知结束时间为空,车牌号: {}", vehicle.getLicensePlate());
                return;
            }
            final SocVehicleLog records = socVehicleLogMapper.getByVid(
                ImmutableMap.of(
                    "uuid",
                    vid,
                    "beginTime",
                    beginTime
                ));
            if (records == null){
                log.info("无法找到SOC低电量车辆无须结束,车牌号: {}", vehicle.getLicensePlate());
                return;
            }
            final Date endDate = DateUtil.strToDate_ex(endTime);
            final SocVehicleLog svl = new SocVehicleLog();
            svl.setVid(vid);
            svl.setEndTime(DateUtil.getDate(endDate.getTime()));
            svl.setStatus(1);
            svl.setEndSoc(endSoc);
            svl.setStartTime(beginTime);
            socVehicleLogMapper.updateByVid(svl);
            log.info("车辆结束SOC低电量通知,车牌号: {}", vehicle.getLicensePlate());
        }
        log.info("处理SOC低电量车辆通知结束");
    }

    /**
     * 无can车辆统计
     */

    private void notCanVehCustomer(@NotNull Map<String, Object> map) {
        log.info("处理无can车辆通知开始");
        final String vid = map.getOrDefault("vid", "").toString().trim();
        final HashMap<String,Object> param = Maps.newHashMap();
        param.put("uuid",vid);
        Vehicle vehicle = vehicleMapper.findByUuid(param);
        if (vehicle ==null){
            log.warn("该车辆在系统中不存在,vid : {}", vid);
            return;
        }
        final int status = Integer.parseInt(map.getOrDefault("status", "0").toString().trim());
        String startTime = map.getOrDefault("beginTime", "").toString().trim();
        if (StringUtils.isEmpty(startTime)) {
            log.warn("无can车辆通知开始时间为空,车牌号: {}", vehicle.getLicensePlate());
            return;
        }
        final Date startDate = DateUtil.strToDate_ex(startTime);
        param.put("beginTime",new Timestamp(startDate.getTime()));
        final String endTime = map.getOrDefault("endTime", "").toString().trim();
        //无can开始时里程
        final String startMileage = map.getOrDefault("beginMileage", "").toString().trim();
        //恢复时里程
        final String endMileage = map.getOrDefault("endMileage", "").toString().trim();

        if (status == 1 || status == 2) {

            final VehNotCan records = vehNotCanMapper.getByVid(param);
            if (records != null){
                log.info("无can车辆出现重复通知不作处理,车牌号: {}", vehicle.getLicensePlate());
                return;
            }

            final VehNotCan vcm = new VehNotCan();
            vcm.setId(IdUtil.simpleUUID());
            vcm.setVehUuid(vid);
            vcm.setLastUploadTime(new Timestamp(startDate.getTime()));
            vcm.setState(0);
            vcm.setCreateTime(new Timestamp(System.currentTimeMillis()));
            vcm.setUpdateTime(vcm.getCreateTime());
            vcm.setVehId(vehicle.getId());
            if (NumberUtils.isCreatable(startMileage)) {
                vcm.setLastUploadMileage(NumberUtils.toDouble(startMileage));
            }
            vehNotCanMapper.insert(vcm);
            log.info("车辆开始无CAN,车牌号: {}", vehicle.getLicensePlate());
        }else if (status == 3) {
            if (StringUtils.isEmpty(endTime)) {
                log.warn("无can车辆通知结束时间为空,车牌号: {}", vehicle.getLicensePlate());
                return;
            }
            final VehNotCan record = vehNotCanMapper.getByVid(param);
            if (record == null){
                log.info("无法找到无can车辆记录无须结束,车牌号: {}", vehicle.getLicensePlate());
                return;
            }
            final Date dateTemp = DateUtil.strToDate_ex(endTime);
            if (dateTemp != null){
                record.setRegainUploadTime(new Timestamp(dateTemp.getTime()));
            }else {
                record.setRegainUploadTime(new Timestamp(System.currentTimeMillis()));
            }
            record.setState(1);
            record.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (NumberUtils.isCreatable(endMileage)){
                record.setRegainUploadMileage(NumberUtils.toDouble(endMileage));
            }
            vehNotCanMapper.update(record);
            log.info("车辆结束无can,车牌号: {}", vehicle.getLicensePlate());
        }
        log.info("处理无can车辆通知结束");
    }

    /**
     * 未定位车辆统计
     */
    private void notPositionVehCustomer(final @NotNull Map<String, Object> map, String msgType) {

        log.info("未定位车辆统计开始");
        final String vid = map.getOrDefault("vid", "").toString().trim();
        final HashMap<String,Object> param = Maps.newHashMap();
        param.put("uuid",vid);
        final Vehicle vehicle = vehicleMapper.findByUuid(param);
        if (vehicle ==null){
            log.warn("该车辆在系统中不存在,vid : {}", vid);
            return;
        }
        final int status = Integer.parseInt(map.getOrDefault("status", "0").toString().trim());
        final String stime = map.getOrDefault("beginTime", "").toString().trim();
        final String etime = map.getOrDefault("endTime", "").toString().trim();
        param.put("beginTime",new Timestamp(DateUtil.strToDate_ex(stime).getTime()));
        if (!StringUtils.isEmpty(stime) && !StringUtils.isEmpty(etime)) {
            Timestamp stimestamp = new Timestamp(DateUtil.strToDate_ex(stime).getTime());
            Timestamp etimestamp = new Timestamp(DateUtil.strToDate_ex(etime).getTime());
            if (stimestamp.after(etimestamp)) {
                return;
            }
        }
        if (!StringUtils.isEmpty(vid) && ((status == 1 && !StringUtils.isEmpty(stime)) || status == 2 || (status == 3 && !StringUtils.isEmpty(etime)))) {
            final VehNotPosition vpm = vehNotPositionMapper.getByVid(param);
            if (vpm != null){
                if (status == 3){
                    log.info("结束状态开始" + status);
                    final Date dateTemp = DateUtil.strToDate_ex(etime);
                    if (dateTemp != null){
                        vpm.setRegainUploadTime(new Timestamp(dateTemp.getTime()));
                    }else {
                        vpm.setRegainUploadTime(new Timestamp(System.currentTimeMillis()));
                    }
                    vpm.setState(0);
                    vpm.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    vehNotPositionMapper.update(vpm);
                }
            }else {
                if (StringUtils.isEmpty(stime)) {
                    log.info("未定位车辆数据记录:" + vehicle.getLicensePlate() + ",无上报时间");
                    return;
                }
                final VehNotPosition nvpm = new VehNotPosition();
                nvpm.setId(IdUtil.simpleUUID());
                nvpm.setVehUuid(vid);
                nvpm.setLastUploadTime(new Timestamp(DateUtil.strToDate_ex(stime).getTime()));
                nvpm.setState(1);
                nvpm.setCreateTime(new Timestamp(System.currentTimeMillis()));
                nvpm.setUpdateTime(nvpm.getCreateTime());
                nvpm.setVehId(vehicle.getId());
                log.info("未定位车辆统计开始" + nvpm.getState());
                vehNotPositionMapper.insert(nvpm);
            }
        }
    }
}

