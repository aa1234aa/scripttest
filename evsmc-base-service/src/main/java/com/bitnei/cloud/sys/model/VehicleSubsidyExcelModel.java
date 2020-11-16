package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 车辆补贴导出model
 * @author zxz
 */
@Setter
@Getter
@Slf4j
public class VehicleSubsidyExcelModel extends BaseModel {
    
    /** VIN */
    private String vin;

    /** 车辆生产单位id */
    private String manuUnitId;

    /** 车辆生产单位名称 */
    @LinkName(table = "sys_unit", joinField = "manuUnitId")
    private String manuUnitName;

    /** 车牌号 */
    private String licensePlate;

    /** 车辆用途 */
    private Integer operUseFor;

    /** 车辆用途名称 */
    @DictName(code = "VEH_USE_FOR",joinField = "operUseFor")
    private String operUseForName;

    /** 上牌城市id */
    private String operLicenseCityId;

    /** 上牌城市名称 */
    @LinkName(table = "sys_area", joinField = "operLicenseCityId")
    private String operLicenseCityName;

    /** 车辆种类 */
    private String vehTypeName;

    /** 车辆型号id */
    private String vehModelId;

    /** 车辆型号 */
    private String vehModelName;

    /** 申报状态 */
    private Integer subsidyApplyStatus;

    /** 申报状态名称**/
    @DictName(code = "SUBSIDY_APPLY_STATUS",joinField = "subsidyApplyStatus")
    private String subsidyApplyStatusName;

    /** 申报次数 */
    private Integer susidyApplyCount;

    /** 累计行驶里程 */
    private String accumulatedMileage;

    /** 纯电续驶里程 */
    private String electricalMileage;

    /** 充电倍率 */
    private String chargingRate;

    /** 燃料消耗量 */
    private String fuelConsumption;

    /** 车辆发票 */
    private String sellInvoiceNo;

    /** 行驶证注册日期 */
    private String sellLicenseRegDate;

    /** 首次上线时间 */
    private String firstTimeOnLine;

    /** 累计行驶里程 */
    private Double lastEndMileage;


    /** 储能装置单体型号 */
    private String batteryCellModel;
    /** 储能装置单体生产企业名称 */
    private String batteryCellUnitName;
    /** 储能装置成箱型号 */
    private String modelName;
    /** 储能装置系统生产企业名称 */
    private String unitName;
    /** 储能装置可充电储能装置种类 */
    private String modelType;
    /** 储能装置电池类型名称(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) */
    private String batteryTypeName;
    /** 储能装置系统总能量(kWh) */
    private String totalElecCapacity;
    /** 储能装置系统能量密度（Wh/kg) */
    private String capacityDensity;
    /** 储能装置发票号 **/
    private String invoiceNo;

    /** 驱动电机型号名称 **/
    private String driveMotorModelName;
    /** 驱动电机额定功率(KW) **/
    private String ratedPower;
    /** 驱动电机系统生产企业名称 **/
    private String prodUnitName;
    /** 驱动电机驱动电机发票号 **/
    private String driveInvoiceNo;

    /** 燃料电池型号名称 **/
    private String fuelModelName;
    /** 燃料电池额定功率(KW) **/
    private String fuelRatedPower;
    /** 燃料电池系统生产企业名称 **/
    private String fuelProdUnitName;
    /** 燃料电池发票号 **/
    private String fuelInvoiceNo;

    /** efficiencyJson **/
    private String efficiencyJson;
    private String vehicleId;

    private static String join(String srcStr, Object appendStr) {
        if(appendStr != null) {
            if(StringUtils.isNotBlank(srcStr)) {
                return srcStr + "/" + appendStr.toString();
            } else {
                return appendStr.toString();
            }
        }
        return srcStr;
    }

    /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehicleSubsidyExcelModel fromEntry(Vehicle entry, List<VehicleEngeryDeviceLkModel> engeryList,
                                                List<VehicleDriveDeviceLkModel> deviceList, List<VehiclePowerDeviceLkModel> powerList,
                                                     VehModelModel vehModel){
        VehicleSubsidyExcelModel m = new VehicleSubsidyExcelModel();
//        DecimalFormat d = new DecimalFormat("#######.##");
        BeanUtils.copyProperties(entry, m);
        DataLoader.loadNames(m);
//        m.setFirstTimeOnLine(entry.get("firstTimeOnLine") == null ? null : entry.get("firstTimeOnLine").toString());
//        m.setAccumulatedMileage(entry.get("lastEndMileage") == null ? null : d.format(entry.get("lastEndMileage")));
        if(engeryList != null && !engeryList.isEmpty()) {
            for (VehicleEngeryDeviceLkModel lk : engeryList) {
                DataLoader.loadNames(lk);
                m.setBatteryCellModel(join(m.getBatteryCellModel(), lk.getBatteryCellModel()));
                m.setModelType(join(m.getModelType(), lk.getModelTypeDisplay()));
                m.setBatteryCellUnitName(join(m.getBatteryCellUnitName(), lk.getBatteryCellUnitName()));
                if(Constant.EnergyDeviceType.BATTERY.equals(lk.getModelType())) {
                    m.setModelName(join(m.getModelName(), lk.getBatteryModelName()));
                    m.setTotalElecCapacity(join(m.getTotalElecCapacity(), lk.getTotalElecCapacity()));
                    m.setCapacityDensity(join(m.getCapacityDensity(), lk.getCapacityDensity()));
                    m.setUnitName(join(m.getUnitName(),lk.getUnitName()));
                } else if(Constant.EnergyDeviceType.CAPACITY.equals(lk.getModelType())) {
                    m.setModelName(join(m.getModelName(),lk.getCapacityModelName()));
                    m.setTotalElecCapacity(join(m.getTotalElecCapacity(),lk.getTotalCapacity()));
                    m.setCapacityDensity(join(m.getCapacityDensity(),lk.getScmCapacityDensity()));
                    m.setUnitName(join(m.getUnitName(),lk.getScmUnitName()));
                }
                m.setBatteryTypeName(join(m.getBatteryTypeName(),lk.getBatteryTypeName()));
                m.setInvoiceNo(join(m.getInvoiceNo(), lk.getInvoiceNo()));
            }
        }
        if(deviceList != null && !deviceList.isEmpty()) {
            for (VehicleDriveDeviceLkModel lk : deviceList) {
                DataLoader.loadNames(lk);
                m.setDriveMotorModelName(join(m.getDriveMotorModelName(),lk.getDriveMotorModelName()));
                m.setDriveInvoiceNo(join(m.getDriveInvoiceNo(), lk.getInvoiceNo()));
                m.setProdUnitName(join(m.getProdUnitName(),lk.getProdUnitName()));
                m.setRatedPower(join(m.getRatedPower(),lk.getRatedPower()));
            }
        }
        if(powerList != null && !powerList.isEmpty()) {
            for (VehiclePowerDeviceLkModel lk : powerList) {
                DataLoader.loadNames(lk);
                m.setFuelModelName(join(m.getFuelModelName(),lk.getFuelBatteryModelName()));
                m.setFuelInvoiceNo(join(m.getFuelInvoiceNo(),lk.getInvoiceNo()));
                m.setFuelProdUnitName(join(m.getFuelProdUnitName(),lk.getProdUnitName()));
                m.setFuelRatedPower(join(m.getFuelRatedPower(),lk.getRatedPower()));
            }
        }
        if(vehModel != null && vehModel.getEfficiency() != null) {
            if(vehModel.getEfficiency().getFastChargeRatio() != null) {
                m.setChargingRate(vehModel.getEfficiency().getFastChargeRatio().toString());
            }

        }
        return m;
    }









}
