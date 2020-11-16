package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.sys.domain.Vehicle;
import lombok.Getter;
import lombok.Setter;

/**
 * 国家平台EXCEL终端型号Model
 * @author zxz
 */
@Setter
@Getter
public class VehicleExcelData {


    /** 车辆厂商 **/
    private String manuUnitName;

    /** 车辆型号 **/
    private String vehModelName;

    /** VIN **/
    private String vin;

    /** 车牌号 **/
    private String licensePlate;

    /** 车辆颜色 **/
    private String colorName;

    /** 车辆配置名称 **/
    private String configName;

    /** 生产批次 **/
    private String produceBatch;

    /** 车辆用途 **/
    private String operUseForName;

    /** 购车领域（单位用车,私人用车） **/
    private String sellForFieldName;

    /** 运营单位 **/
    private String operUnitName;

    /** 购买车主/购买单位 **/
    private String sellOwnerOrUnit;

    /** 出厂日期 **/
    private String factoryDate;

    /**
     * 将实体转为前台model
     * @param v VehicleModel
     * @return VehicleExcelData
     */
    public static VehicleExcelData fromEntry(VehicleModel v, Integer sellForField, String operUnitName) {
        VehicleExcelData m = new VehicleExcelData();
//        m.setManuUnitName(v.getManuUnitName());
        m.setManuUnitName(v.getVehUnitName()); 
        m.setVehModelName(v.getVehModelName());
        m.setVin(v.getVin());
        m.setLicensePlate(v.getLicensePlate());
        m.setColorName(v.getColorName());
        m.setConfigName(v.getConfigName());
        m.setProduceBatch(v.getProduceBatch());
        m.setOperUseForName(v.getOperUseForName());
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PRIVATE_SPHERE.getValue().equals(sellForField)) {
            m.setSellForFieldName("私人用车");
            m.setOperUnitName("私人");
            m.setSellOwnerOrUnit(v.getOwnerName());
        } else if(Vehicle.SEEL_FOR_FIELD_ENUM.PUBLIC_SPHERE.getValue().equals(sellForField)) {
            m.setSellForFieldName("单位用车");
            m.setOperUnitName(operUnitName);
            m.setSellOwnerOrUnit(v.getOwnerName());
        }
        m.setFactoryDate(v.getFactoryDate());
        return m;
    }




}
