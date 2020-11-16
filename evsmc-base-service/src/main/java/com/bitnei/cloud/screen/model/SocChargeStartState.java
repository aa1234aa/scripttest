package com.bitnei.cloud.screen.model;

import lombok.Getter;
import lombok.Setter;

/**
 * SOC充电起始状态
 *
 * @author xuzhijie
 */
@Getter
@Setter
public class SocChargeStartState {

    /**
     * 充电起始SOC状态0-10%
     */
    private int chargeSoc0;
    /**
     * 充电起始SOC状态11-20%
     */
    private int chargeSoc1;
    /**
     * 充电起始SOC状态21-30%
     */
    private int chargeSoc2;
    /**
     * 充电起始SOC状态31-40%
     */
    private int chargeSoc3;
    /**
     * 充电起始SOC状态41-50%
     */
    private int chargeSoc4;
    /**
     * 充电起始SOC状态51-60%
     */
    private int chargeSoc5;
    /**
     * 充电起始SOC状态61-70%
     */
    private int chargeSoc6;
    /**
     * 充电起始SOC状态71-80%
     */
    private int chargeSoc7;
    /**
     * 充电起始SOC状态81-90%
     */
    private int chargeSoc8;
    /**
     * 充电起始SOC状态91-100%
     */
    private int chargeSoc9;


}
