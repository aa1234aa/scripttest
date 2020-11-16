package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class ChargeInfo {

    /**
     * 充电次数
     */
    private int chargeCount;

    /**
     * 充电时长(h)
     */
    private double chargeDuration;

    /**
     * 充电量(Kw.h)
     */
    private double chargeSum;

    /**
     * 充电车辆数
     */
    private int chargeVehicleCount;

}
