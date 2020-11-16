package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.fault.domain.AlarmInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Desc：
 * @Author: zy
 * @Date: 2019/5/24
 */
@Getter
@Setter
public class KafkaFenceAlarmModel {

    // 车辆的vid
    private String vehicleId;
    // 对应ruleid 电子围栏id
    private String 	fenceId;

    // 报警时间
    private String noticeTime;

    private String endTime;
    /**BEGIN：开始;  END：结束**/
    private String eventStage;
    // 经度
    private String longitude;
    //纬度
    private String latitude;

    /**kafka消息id**/
    private String messageId;

    private  String dataTime;

    public AlarmInfo fromEntry(){
        AlarmInfo entry = new AlarmInfo();
        // uuid
        entry.setUuid(vehicleId);
        //entry.setVehicleId(vid);
        entry.setRuleId(fenceId);
        // 经度
        entry.setLongitude(convertLatOrLng(longitude));
        //纬度
        entry.setLatitude(convertLatOrLng(latitude));
        entry.setMsgId(messageId);
        return entry;
    }

    private Double convertLatOrLng(String latOrLng) {
        BigDecimal b = new BigDecimal(latOrLng).setScale(6, BigDecimal.ROUND_HALF_UP);
        if (b.compareTo(new BigDecimal(180)) > 0) {
            b = b.divide(new BigDecimal(1000000));
        }
        return b.doubleValue();
    }
}
