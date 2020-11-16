package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.compent.kafka.util.KafkaMessageUtil;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleRealStatusModel;
import com.bitnei.cloud.sys.service.ITermParamRecordService;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleLoginKafkaHandler extends AbstractKafkaHandler {

    private final IVehicleRealStatusService vehicleRealStatusService;

    private final IVehicleService vehicleService;

    private final ITermParamRecordService termParamRecordService;

    @Override
    public boolean handle(String message) {
       Map<String,String> messageMap= KafkaMessageUtil.parserMsg(message);
        if (messageMap == null || messageMap.size() == 0){
            return false;
        }
        String uuid = messageMap.get("VID");
        String time=DateUtil.getKafkaDataSyncTime();
        if (StringUtils.isNotBlank(messageMap.get("TIME"))){
            time = DateUtil.converseStr(messageMap.get("TIME"));
        }
        String typeStr = messageMap.get("TYPE");
        VehicleModel vehicleModel = vehicleService.getByUuid(uuid);
        int type = Integer.parseInt(typeStr);
        if (type == 0) {
            //车辆首次上次
            handleFirstReg(vehicleModel,time);

            //车辆首次登入下发参数查询
            termParamRecordService.allQuery(vehicleModel.getId(), "admin",
                    1, "车辆首次上线|参数查询", "车辆首次上线|参数查询");
        } else if (type == 1) {
            //车辆登入
            handleReg(vehicleModel,time);

        } else if (type == 2) {
            //车辆登出
            handleVehLogout(vehicleModel,time);
        }
        return true;
    }



    private void handleFirstReg(VehicleModel vehicleModel,String time) {
        try {

            if (vehicleModel != null) {
                Map<String, Object> para = new HashMap<>();
                para.put("vehicleId", vehicleModel.getId());
               // para.put("updateTime",time);
                para.put("onlined", 1);
                para.put("onlineStatus", 1);
                para.put("firstRegTime", time);
              int count=  vehicleRealStatusService.update("updateByVehicleId", para);
                if (count == 0) {
                    //首次注册生成车辆实时状态记录
                    VehicleRealStatusModel model = new VehicleRealStatusModel();
                    model.setCreateTime(time);
                   // model.setUpdateTime(time);
                    model.setFirstRegTime(time);
                    model.setVehicleId(vehicleModel.getId());
                    model.setId(UtilHelper.getUUID());
                    model.setOnlined(1);
                    model.setOnlineStatus(1);
                    vehicleRealStatusService.insert(model);
                    log.info("初始化了车辆实时状态：vin {}", vehicleModel.getVin());
                }

            }

        } catch (Exception e) {
            log.error("error:", e);
        }
    }

    private void handleReg(VehicleModel vehicleModel, String time) {
        try {

            Map<String, Object> para = new HashMap<>();
            para.put("vehicleId", vehicleModel.getId());
            //para.put("updateTime",time);
            para.put("onlineStatus", 1);
            para.put("onlined", 1);
           int count= vehicleRealStatusService.update("updateByVehicleId",para);


        } catch (Exception e) {
            log.error("error:", e);

        }
    }

    private void handleVehLogout(VehicleModel vehicleModel,String time) {
        try {

            Map<String, Object> para = new HashMap<>();
            para.put("vehicleId", vehicleModel.getId());
            para.put("updateTime", time);
            para.put("onlineStatus", 2);
            vehicleRealStatusService.update("updateByVehicleId",para); //车辆离线

        } catch (Exception e) {
            log.error("error:", e);

        }
    }


//    public void sendKafkaOnline(Vehicle vehicle) {
//
//        StringBuffer param = new StringBuffer();
//        param.append("NAME:SYS_VEHICLE,");
//        param.append("TYPE:2,");
//        param.append("TIME:" + DateUtil.formatTime(new Date(), DateUtil.DATA_FORMAT) + ",");
//
//        vehicle.setOnlined(0);
//        String delStr = Base64.encodeToString(vehicle.toString()).replaceAll("\r\n", "");
//        vehicle.setOnlined(1);
//        String addStr = Base64.encodeToString(vehicle.toString()).replaceAll("\r\n", "");
//
//        param.append("DEL:").append(delStr).append(",");
//        param.append("ADD:").append(addStr);
//        kafkaTemplate.send("SYNC_DATABASE_UPDATE", "VEH_CHANGE", param.toString());
//
//    }
}
