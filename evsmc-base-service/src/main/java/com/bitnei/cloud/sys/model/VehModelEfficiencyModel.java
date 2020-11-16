package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * 车辆型号-整体性能信息
 */
@Data
public class VehModelEfficiencyModel extends BaseModel {

    //开始设置json中的key传入前台
    @NotBlank(message = "驱动电机峰值功率/转速/转矩不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Pattern(regexp = "^.{5,128}$", message = "驱动电机峰值功率/转速/转矩为5-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "驱动电机峰值功率/转速/转矩")
    private String peakPowerDrivingMotor;

    @PositiveOrZero(message = "电机额定电压(V)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "电机额定电压(V)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "电机额定电压(V)")
    private Double motorRatedVoltage;

    @Range(min = 0L, max = 99999L, message = "总储电量(Kwh)数值范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "总储电量(Kwh)")
    private Double totalStorageCapacity;

    @ApiModelProperty(value = "电池系统能量密度(Wh/kg)")
    @PositiveOrZero(message = "电池系统能量密度(Wh/kg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "电池系统能量密度(Wh/kg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    private Double energyDensityBatterySystem;

    @DecimalMin(value = "0", message = "最高车速(km/h)数值范围0~999,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 3, fraction = 2, message = "最高车速(km/h)范围0~999,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "最高车速(km/h)")
    private Double maximumSpeed;

    @DecimalMin(value = "0", message = "百公里加速(S)数值范围0~999,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 3, fraction = 2, message = "百公里加速(S)范围0~999,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "百公里加速(S)")
    private Double hundredKmAcceleration;

    @Range(max = 999, message = "单体电池总数最大取值999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单体电池总数")
    private Integer numberIndividualBatteries;

    @Range(max = 999, message = "温度探针总数最大取值999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "温度探针总数")
    private Integer totalNumberTemperatureProbes;

    @Range(min = 0, max = 99999, message = "工况续驶里程(Km)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "工况续驶里程(Km)")
    private Double drivingMileageUnderWorkingConditions;

    @Range(min = 0, max = 99999, message = "等速续驶里程(Km)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "等速续驶里程(Km)")
    private Double constantSpeedRange;

    @Range(min = 0, max = 99999, message = "百公里耗电量(kWh/100km)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "百公里耗电量(kWh/100km)")
    private Double kilometresElectricityConsumption;

    @PositiveOrZero(message = "吨百公里耗电量(kWh)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "吨百公里耗电量(kWh)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "吨百公里耗电量(kWh)")
    private Double kilometersTonElectricityConsumption;

    @PositiveOrZero(message = "能量消耗率（wh/kmkg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "能量消耗率（wh/kmkg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "能量消耗率（wh/kmkg)")
    private Double energyConsumptionRate;

    @PositiveOrZero(message = "单位载质量能量消耗量(Ekg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "单位载质量能量消耗量(Ekg)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单位载质量能量消耗量(Ekg)")
    private Double energyConsumptionPerUnitLoadMass;

    @PositiveOrZero(message = "30分钟最高车速(km/h)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "30分钟最高车速(km/h)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "30分钟最高车速(km/h)")
    private Double halfhourMaximumSpeed;

    @PositiveOrZero(message = "快充倍率(C)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "快充倍率(C)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "快充倍率(C)")
    private Double fastChargeRatio;

    @PositiveOrZero(message = "各档位传动比长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "各档位传动比长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "各档位传动比")
    private Double ratioOfEachGear;

    @ApiModelProperty(value = "环保类型")
    private Integer environmentalProtectionType;

    //设置linkname获取数据字典，上面4个
    @DictName(code = "ENV_PROTECT_TYPE", joinField = "environmentalProtectionType")
    @ApiModelProperty(value = "环保类型名称")
    private String environmentalProtectionTypeDisplay;

    @Pattern(regexp = "^.{0,128}|$", message = "传统燃油车燃料类型长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "传统燃油车燃料类型")
    private String normalFuelType;

    @ApiModelProperty(value = "REV燃料类型")
    private Integer revFuelType;

    @ApiModelProperty(value = "HEV燃料类型")
    private Integer hevFuelType;

    @ApiModelProperty(value = "PHEV燃料类型")
    private Integer phevFuelType;

    @DictName(code = "REV_FUEL_TYPE", joinField = "revFuelType")
    @ApiModelProperty(value = "REV燃料类型名称")
    private String revFuelTypeDisplay;

    @DictName(code = "HEV_FUEL_TYPE", joinField = "hevFuelType")
    @ApiModelProperty(value = "HEV燃料类型名称")
    private String hevFuelTypeDisplay;

    @DictName(code = "PHEV_FUEL_TYPE", joinField = "phevFuelType")
    @ApiModelProperty(value = "PHEV燃料类型名称")
    private String phevFuelTypeDisplay;

    @ApiModelProperty(value = "REV燃料形式")
    private Integer revFuelForm;

    @ApiModelProperty(value = "FCV燃料形式")
    private Integer fcvFuelForm;

    @ApiModelProperty(value = "HEV燃料形式")
    private Integer hevFuelForm;

    @ApiModelProperty(value = "PHEV燃料形式")
    private Integer phevFuelForm;

    @ApiModelProperty(value = "传统燃料形式")
    private Integer traditionalFuelForm;

    //设置linkname获取数据字典，上面4个
    @DictName(code = "REV_FUEL_FORM", joinField = "revFuelForm")
    @ApiModelProperty(value = "REV燃料形式")
    private String revFuelFormDisplay;

    @DictName(code = "FCV_FUEL_FORM", joinField = "fcvFuelForm")
    @ApiModelProperty(value = "FCV燃料形式")
    private String fcvFuelFormDisplay;

    @DictName(code = "HEV_FUEL_FORM", joinField = "hevFuelForm")
    @ApiModelProperty(value = "HEV燃料形式")
    private String hevFuelFormDisplay;

    @DictName(code = "PHEV_FUEL_FORM", joinField = "phevFuelForm")
    @ApiModelProperty(value = "PHEV燃料形式")
    private String phevFuelFormDisplay;

    @DictName(code = "PHEV_FUEL_FORM", joinField = "traditionalFuelForm")
    @ApiModelProperty(value = "传统燃料形式")
    private String traditionalFuelFormDisplay;

    @PositiveOrZero(message = "百公里燃料消耗量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "百公里燃料消耗量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "百公里燃料消耗量(L)")
    private Double fuelConsumptionHundredKm;

    @PositiveOrZero(message = "燃料电池经济性长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "燃料电池经济性长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "燃料电池经济性")
    private Double fuelCellEconomy;

    @PositiveOrZero(message = "燃料电池效率(L/Wh)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "燃料电池效率(L/Wh)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "燃料电池效率(L/Wh)")
    private Double fuelCellEfficiency;

    @PositiveOrZero(message = "燃料电池额定功率长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "燃料电池额定功率长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "燃料电池额定功率")
    private Double fuelCellRatedPower;

    @PositiveOrZero(message = "油箱容量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "油箱容量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "油箱容量(L)")
    private Double tankCapacity;

    @PositiveOrZero(message = "储气罐容量(KG)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "储气罐容量(KG)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "储气罐容量(KG)")
    private Double gasStorageTankCapacityKG;

    @PositiveOrZero(message = "储气罐容量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "储气罐容量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "储气罐容量(L)")
    private Double gasStorageTankCapacityL;

    @PositiveOrZero(message = "节油率水平长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "节油率水平长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "节油率水平")
    private Double oilSavingRateLevel;
    //结束设置json的key
}