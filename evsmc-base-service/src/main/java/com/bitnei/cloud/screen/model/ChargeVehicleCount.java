package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 充电车辆数按时间分段
 *
 * @author xuzhijie
 */
@Getter
@Setter
public class ChargeVehicleCount {

    /**
     * 00:00-02:00
     */
    private int chargeVehicleCount1;
    /**
     * 02:00-04:00
     */
    private int chargeVehicleCount2;
    /**
     * 04:00-06:00
     */
    private int chargeVehicleCount3;
    /**
     * 06:00-08:00
     */
    private int chargeVehicleCount4;
    /**
     * 08:00-10:00
     */
    private int chargeVehicleCount5;
    /**
     * 10:00-12:00
     */
    private int chargeVehicleCount6;
    /**
     * 12:00-14:00
     */
    private int chargeVehicleCount7;
    /**
     * 14:00-16:00
     */
    private int chargeVehicleCount8;
    /**
     * 16:00-18:00
     */
    private int chargeVehicleCount9;
    /**
     * 18:00-20:00
     */
    private int chargeVehicleCount10;
    /**
     * 20:00-22:00
     */
    private int chargeVehicleCount11;
    /**
     * 22:00-24:00
     */
    private int chargeVehicleCount12;

}
