package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class UnitVehicleCount {

    /**
     * 运营单位ID
     */
    private String unitId;
    /**
     * 运营单位名称
     */
    private String unitName;
    /**
     * 车辆数
     */
    private Integer vehicleCount;
    /**
     * 在线车辆数
     */
    private Integer onlineCount;

}
