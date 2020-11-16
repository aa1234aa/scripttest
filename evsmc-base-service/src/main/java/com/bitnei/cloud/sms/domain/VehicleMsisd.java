package com.bitnei.cloud.sms.domain;

import lombok.Data;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/22
 */
@Data
public class VehicleMsisd {

    private String vehicleId;

    private String vin;

    private String msisd;

    private String iccid;
}
