package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sms.domain.SmsVehicle;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/30
 */
@Data
public class SmsVehicleModel extends BaseModel {
    private String id;
    private String vin;
    private String licensePlate;
    private String interNo;
    private String vehModelName;
    private String termModelName;
    private String iccid;
    private String operUnitName;
    private String operAreaName;
    private String msisd;
    private String sendStatus;
    @DictName(code = "SMS_SEND_STATUS", joinField = "sendStatus")
    private String sendStatusDisplay;

    private String failMsg;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static SmsVehicleModel fromEntry(SmsVehicle entry) {
        SmsVehicleModel m = new SmsVehicleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
