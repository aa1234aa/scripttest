package com.bitnei.cloud.sms.domain;

import lombok.Data;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/30
 */
@Data
public class SmsVehicle  {
    private String id;
    private String vehicleId;
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
    private String failMsg;
}
