package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.fault.domain.AlarmInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/3/9
 */
@Getter
@Setter
public class KafkaFaultAlarmModel {

    /*{
            "uuid": "车辆uuid",
            "vin": "vin",
            "ruleId": "取 fault_parameter_rule表的id",
            "faultBeginTime": "故障开始时间",
            "faultEndTime": "故障结束时间",
            "faultStatus": "故障状态  1:未结束, 2已结束",
            "longitude": "经度",
            "latitude": "纬度"
    }
    */

    private String vid;
    private String vin;
    private String ruleId;
    private String beginTime;
    private String endTime;
    /**1：开始;  3：结束**/
    private String status;
    private String longitude;
    private String latitude;
    private String triggerItem;
    /**kafka消息id**/
    private String msgId;
    /**等级,  以tbox上传来的为准 **/
    private String level;

    public AlarmInfo fromEntry() {
        AlarmInfo entry = new AlarmInfo();
        entry.setUuid(vid);
        entry.setVin(vin);
        entry.setRuleId(ruleId);
        entry.setLongitude(convertLatOrLng(longitude));
        entry.setLatitude(convertLatOrLng(latitude));
        entry.setMsgId(msgId);
        entry.setTriggerItem(triggerItem);
        if(NumberUtils.isDigits(level)) {
            entry.setAlarmLevel(Integer.valueOf(level));
        }
        else {
            entry.setAlarmLevel(-1);
        }
        return entry;
    }

    private Double convertLatOrLng(String latOrLng) {
        if (StringUtils.isBlank(latOrLng)){
            return 0d;
        }
        BigDecimal b = new BigDecimal(latOrLng).setScale(6, BigDecimal.ROUND_HALF_UP);
        if (b.compareTo(new BigDecimal(180)) > 0) {
            b = b.divide(new BigDecimal(1000000));
        }
        return b.doubleValue();
    }
}
