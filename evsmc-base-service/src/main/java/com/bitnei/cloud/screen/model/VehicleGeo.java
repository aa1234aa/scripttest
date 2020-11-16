package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class VehicleGeo {

    /**
     * 车辆VID
     */
    private String vid;

    /**
     * 车牌号
     */
    private String vin;
    /**
     * 车牌号
     */
    private String licensePlate;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 状态 1：在线、2：离线、3：充电、4：故障
     */
    private Integer status;
    private Integer powerMode;
    private Double speed;

}
