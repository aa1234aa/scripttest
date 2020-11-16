package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuzhijie
 */
@Getter
@Setter
@ToString
public class PowerMode {
    /**
     * 车辆VID
     */
    private String vid;

    /**
     * 动力方式 - 值
     */
    private String val;

    /**
     * 动力方式 - 描述
     */
    private String name;

}
