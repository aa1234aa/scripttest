package com.bitnei.cloud.sys.model;


import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 环保平台车型信息model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class EcoVehModel extends BaseModel {

    private String id;

    /** 车辆型号 **/
    private String modelCn;

    /** 环保信息公开编号 **/
    private String epaPublicInfoNum;

    /** 车辆厂商id **/
    private String vehUnitId;

    /** 车辆厂家统一信用代码 **/
    @LinkName(table = "sys_unit", column = "organization_code", joinField = "vehUnitId")
    private String firmCn;

    /** 车载终端型号 **/
    private String tboxModel;

    /** 车载终端最终的芯片型号 **/
    private String chipModel;

    /** 发动机型号 **/
    private String engineType;

    /** 燃料类型 **/
    private String engineFuelType;

    /** 排放标准 **/
    private String engineEmissionLevel;

    /** 尿素箱容积(L) **/
    private String ureaTankCapacity;

    /** 整车生产企业 **/
    private String vehicleFirm;

    /** 厂牌品牌 **/
    private String factoryModel;

    /** 驱动桥主减速比 **/
    private String reductionRatio;

    /** 前进档个数 **/
    private String transmissionForwardNumber;

    /** 各档速比 **/
    private String transmissionGearRatio;

    /** 车型详情 **/
    private String modelCnDetail;

    /** 用途类型 **/
    private String modelType;

    /** 产地 **/
    private String productArea;

    /** 底盘型号 **/
    private String chassisModel;

    /** 底盘生产企业 **/
    private String chassisUnitId;

    /** 底盘生产企业 **/
    @LinkName(table = "sys_unit", column = "name", joinField = "chassisUnitId")
    private String chassisFirm;

    /** 最大允许装载质量 **/
    private String maxAllowMass;

    /** 半挂牵引车鞍座最大允许承载质量 **/
    private String maxAllowAbleBearingMass;

    /** 最高车速 **/
    private String maxSpeed;

    /** 最小离地间隙 **/
    private String minGroundClearance;

    /** 车辆滑行系数(A、B、C) **/
    private String vehicleSlidingCoefficient;


    /** 轴数/列车轴数 **/
    private String axlesNumber;

    /** 轴距 (mm) **/
    private String wheelBase1;

    /** 轮距(前/后) (mm) **/
    private String wheelBase2;

    /** 车身（或驾驶室）型式 **/
    private String bodyModel;

    /** 轮胎数 **/
    private String tireNumber;

    /** 轮胎生产企业 **/
    private String tireManufacturer;

    /** 轮胎规格 **/
    private String tireSpec;

    /** 轮胎气压 (kPa) **/
    private String tirePressure;

    /** 接近角/离去角(°) **/
    private String approachDepartureAngle;

    /** 变速箱 **/
    private String transmissionName;

    /** 变速器生产企业 **/
    private String transmissionFirm;

    /** 型式/操纵方式 **/
    private String transmissionType;

    /** 变速器型号 **/
    private String transmissionModel;

    /** 油箱容积 **/
    private String fuelTankCapacity;

    /** 由发动机驱动的附件允许吸收的最大功率(Kw)  **/
    private String allowedMaxPower;

    /** ESC试验ABC转速（r/min） **/
    private String abcSpeed;

    /** 设备监控版本号 **/
    private String tmsMonitorVersion;

    /** 驱动型式及位置 **/
    private String driveType;

    /** 车辆检测报告文件 **/
    private String detectionReport;


    /**
     * 将实体转为前台model
     * @param vm VehModel
     * @return EcoVehModel
     */
    public static EcoVehModel fromEntry(VehModelModel vm, EcoTBoxModel etm, VehNoticeModel nm, String engineModelName){
        // TODO 缺少信息
        if(nm == null) {
            nm = new VehNoticeModel();
        }
        if(etm == null) {
            etm = new EcoTBoxModel();
        }
        EcoVehModel m = new EcoVehModel();
        m.setId(vm.getId());
        m.setModelCn(vm.getVehModelName());
        m.setEpaPublicInfoNum(vm.getEnvironmentalInfoNo());
        m.setVehUnitId(vm.getVehUnitId());
        m.setTboxModel(etm.getTboxModel());
        m.setChipModel(etm.getChipModel());
        // 发动机型号
        m.setEngineType(engineModelName);
        m.setEngineFuelType(ts(vm.getEfficiency().getTraditionalFuelForm()));
        m.setEngineEmissionLevel(vm.getEmissionLevel());
        m.setUreaTankCapacity(vm.getUreaTankCapacity());
        m.setVehicleFirm(vm.getVehicleFirm());
        m.setFactoryModel(nm.getSeriesName());
        m.setReductionRatio(vm.getReductionRatio());
        m.setTransmissionForwardNumber(vm.getTransmissionForwardNum());
        m.setTransmissionGearRatio(vm.getTransmissionGearRatio());
        m.setModelCnDetail(vm.getModelDetails());
        m.setModelType(vm.getUseType());
        m.setProductArea(vm.getProductArea());
        m.setChassisModel(vm.getChassisModel());
        m.setChassisUnitId(vm.getChassisUnitId());
        m.setMaxAllowMass(vm.getMaxAllowMass());
        m.setMaxAllowAbleBearingMass(vm.getBearingMaxAllowMass());
        if(vm.getEfficiency().getMaximumSpeed() != null) {
            int i = vm.getEfficiency().getMaximumSpeed().intValue();
            m.setMaxSpeed(ts(i));
        }
        m.setMinGroundClearance(vm.getMinGroundClearance());
        m.setVehicleSlidingCoefficient(vm.getVehSlidingCoefficient());
        m.setAxlesNumber(vm.getAxlesNumber());
        m.setWheelBase1(vm.getWheelBase());
        m.setWheelBase2(vm.getWheelbaseDetail());
        m.setBodyModel(vm.getBodyModel());
        m.setTireNumber(vm.getTireNumber());
        m.setTireManufacturer(vm.getTireManufacturer());
        m.setTireSpec(vm.getTireSpec());
        m.setTirePressure(vm.getTirePressure());
        m.setApproachDepartureAngle(vm.getApproachDepartureAngle());
        m.setTransmissionName(vm.getTransmissionName());
        m.setTransmissionFirm(vm.getTransmissionFirm());
        m.setTransmissionType(vm.getTransmissionType());
        m.setTransmissionModel(vm.getTransmissionModel());
        m.setFuelTankCapacity(vm.getFuelTankCapacity());
        m.setAllowedMaxPower(vm.getAllowedMaxPower());
        m.setAbcSpeed(vm.getAbcSpeed());
        m.setTmsMonitorVersion(vm.getTmsMonitorVersion());
        m.setDriveType(vm.getDriveTypeLocation());
        m.setDetectionReport(vm.getVehInspectionReport());
        return m;
    }

    private static String ts(Object obj){
        return obj == null ? null : obj.toString();
    }

}
