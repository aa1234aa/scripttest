package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 国家平台EXCEL终端型号Model
 * @author zxz
 */
@Setter
@Getter
@NoArgsConstructor
public class TermModelExcelData {

    /** 终端型号 Y */
    private String termModelName;

    /** 车辆配置名称 Y */
    private String configurationNum;

    /** 终端品牌 */
    private String brandName;

    /** iccid */
    private String iccid;

    /** vin */
    private String vin;

    public TermModelExcelData(String vin, String termModelName, String iccid) {
        this.termModelName = termModelName;
        this.iccid = iccid;
        this.vin = vin;
    }

    /**
     * 将实体转为前台model
     * @param t TermModelModel
     * @param configurationNum 车辆配置名称
     * @return DriveMotorExcelData
     */
    public static TermModelExcelData fromEntry(TermModelModel t, String configurationNum){
        TermModelExcelData m = new TermModelExcelData();
        BeanUtils.copyProperties(t, m);
        m.setConfigurationNum(configurationNum);
        return m;
    }


}
