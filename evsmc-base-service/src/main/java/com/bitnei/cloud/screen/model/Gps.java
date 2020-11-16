package com.bitnei.cloud.screen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xuzhijie
 */
@Getter
@Setter
@AllArgsConstructor
public class Gps {
    /**
     * 纬度
     */
    private double wgLat;
    /**
     * 经度
     */
    private double wgLon;
}