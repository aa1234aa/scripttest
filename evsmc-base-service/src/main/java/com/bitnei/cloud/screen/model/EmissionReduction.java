package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class EmissionReduction {

    /**
     * 累计里程
     */
    private BigDecimal mil;

    /**
     * 累计里程 单位
     */
    private String milUnit = "公里";

    /**
     * 累计节油量
     */
    private BigDecimal oil;

    /**
     * 累计节油量 单位
     */
    private String oilUnit = "升";

    /**
     * 累计耗电量
     */
    private BigDecimal ele;

    /**
     * 累计耗电量 单位
     */
    private String eleUnit = "度";

    /**
     * 累计碳减排
     */
    private BigDecimal co2;

    /**
     * 累计碳减排 单位
     */
    private String co2Unit = "吨";

}
