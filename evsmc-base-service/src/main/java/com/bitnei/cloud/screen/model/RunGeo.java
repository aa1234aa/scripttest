package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 行驶轨迹
 *
 * @author xuzhijie
 */
@Getter
@Setter
public class RunGeo {

    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    private Integer count;

}
