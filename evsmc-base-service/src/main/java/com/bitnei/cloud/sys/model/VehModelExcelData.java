package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 国家平台导出excel车型与配置Model
 * @author zxz
 */
@Setter
@Getter
public class VehModelExcelData {

    /** 车辆型号 Y */
    private String vehModelNum;

    /** 车辆配置名称 Y */
    private String configurationNum;

    /** 规约号 Y */
    private String rule;

    /** 最高车速(Km/h) Y */
    private Double maxVehSpeed;

    /** 电池种类 Y */
    private String anodeMaterial;

    /** 动力方式 Y */
    private String powerModeId;

    /** 驱动电机种类 Y */
    private String configMotorType;

    /** 续驶里程检测方式 Y */
    private String isGkOrDs;

    /** 电动汽车续驶里程(Km) Y */
    private Double pureElectriceMileage;

    /** 额定电压(V) Y */
    private Double energyStorageRateVol;

    /** 总储电量(Kwh) Y */
    private Double energyStorageTotalCap;

    /** 电池包(箱)串并联方式 Y */
    private String seriesParallel;

    /**
     * 将实体转为前台model
     * @param e VehModelModel
     * @return VehModelExcelData
     */
    public static VehModelExcelData fromEntry(VehModelModel e) {
        VehModelExcelData m = new VehModelExcelData();
        m.setVehModelNum(e.getVehModelName());
        m.setConfigurationNum(e.getConfigName());
        m.setRule(e.getRuleIdName());
        m.setAnodeMaterial(e.getBattTypeDisplay());
        m.setPowerModeId(e.getPowerModeDisplay());
        m.setConfigMotorType(e.getDriveTypeDisplay());
        if(e.getEfficiency() != null) {
            m.setMaxVehSpeed(e.getEfficiency().getMaximumSpeed());
            if(e.getEfficiency().getDrivingMileageUnderWorkingConditions() != null) {
                m.setIsGkOrDs("工况法");
                m.setPureElectriceMileage(e.getEfficiency().getDrivingMileageUnderWorkingConditions());
            } else if (e.getEfficiency().getConstantSpeedRange() != null) {
                m.setIsGkOrDs("等速法");
                m.setPureElectriceMileage(e.getEfficiency().getConstantSpeedRange());
            }
            m.setEnergyStorageRateVol(e.getEfficiency().getMotorRatedVoltage());
            m.setEnergyStorageTotalCap(e.getEfficiency().getTotalStorageCapacity());
        }
        m.setSeriesParallel(e.getBatteryLinkModeName());
        return m;
    }




}
