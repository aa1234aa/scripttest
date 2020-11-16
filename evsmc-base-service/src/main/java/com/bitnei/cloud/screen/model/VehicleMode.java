package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 车型
 *
 * @author xuzhijie
 */
@Getter
@Setter
public class VehicleMode {

    /**
     * 车型ID
     */
    private String id;

    /**
     * 百公里耗油
     */
    private double l100km;

    /**
     * 百公里耗电
     */
    private double kwh100h;

}
