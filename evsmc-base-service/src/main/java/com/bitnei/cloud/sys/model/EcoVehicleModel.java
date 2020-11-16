package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.sys.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 环保平台车辆信息model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class EcoVehicleModel {

    private String id;

    /** 车牌号码 **/
    private String vehicleLicense;

    /** 车牌颜色	 **/
    private String licenseColor;

    /** 车体结构 **/
    private String vehicleStructure;

    /** 车辆颜色	 **/
    private String vehicleColor;

    /** 核定载重	 **/
    private String vehicleLoad;

    /** 车辆尺寸（长） **/
    private String vehicleLong;

    /** 车辆尺寸（宽） **/
    private String vehicleWide;

    /** 车辆尺寸（高）	 **/
    private String vehicleHigh;

    /** 总质量 **/
    private String grossVehicleMass;

    /** 车辆类型	 **/
    private String vehicleType;

    /** 行业类型	 **/
    private String industryType;

    /** 车辆型号	 **/
    private String vehicleModel;

    /** 购买时间	 **/
    private String buyingDate;

    /** 车架号VIN	 **/
    private String vehicleFrameNo;

    /** 行驶证号	**/
    private String drivingLicenseNo;

    /** 发动机型号 **/
    private String engineModel;

    /** 发动机编号 **/
    private String engineNo;

    /** 车籍地 **/
    private String vehiclePlace;

    /** 车辆技术等级 **/
    private String technicalLevel;

    /** 出厂日期 **/
    private String productionDate;

    /** 等级评定日期 **/
    private String gradeAssessmentDate;

    /** 二级维护日期 **/
    private String twoMaintenanceDate;

    /** 二级维护状态  **/
    private String twoMaintenanceStatus;

    /** 年审状态 **/
    private String yearEvaluationStatus;

    /** 年检有效期 **/
    private String yearInspectionPeriod;

    /** 保险有效期 **/
    private String insurancePeriod;

    /** 保养有效期 **/
    private String maintenancePeriod;

    /** 所属单位名称 **/
    private String enterpriseName;

    /** 车辆联系人 **/
    private String contactsName;

    /** 车辆联系电话 **/
    private String contactPhone;

    /** 车辆sim卡号 **/
    private String terminalSim;

    /** 车辆注册时间 **/
    private String registerDate;

    /** 所属组织ID **/
    private String organizationId;

    /** 环保局车辆类型 **/
    private String epaVehicleType;

    /** 运输局车辆类型 **/
    private String transVehicleType;

    /** 所有绑定的sim卡 **/
    private String terminalAllSim;

    /** 所有者地址 **/
    private String ownerAddress;

    /** 车牌型号 **/
    private String licenseModel;

    /** 行政区划 **/
    private String administrativeArea;

    /** 行政地址 **/
    private String administrativeAddress;

    /** 总客数 **/
    private String totalNumberGuest;

    /** 整备质量 **/
    private String curbWeight;

    /** 列车最大总质量 **/
    private String maximumTotalMassOfTrain;

    /** 入网证号 **/
    private String netNumber;

    /** 初次登记日期 **/
    private String initialRegistrationDate;

    /** 年检日期 **/
    private String annualInspectionDate;

    /** 强制报废日期 **/
    private String mandatoryScrapDate;

    /** 所属企业简称 **/
    private String enterpriseShortName;

    /** 车辆SN **/
    private String vehicleSN;

    /** 安全芯片型号 **/
    private String chipType;

    /** 车载终端型号 **/
    private String tboxType;

    /** 备案激活模式：0无需备案激活1需要备案激活3其它 **/
    private String vehRegisterMode;


    /**
     * 将实体转为前台model
     * @param v 车辆信息
     * @param vm 车型信息
     * @param om 车辆运营信息
     * @param sm 车辆销售信息
     * @param tm 车辆终端信息
     * @param lk 发动机信息
     * @param iccid iccid
     * @param networkAccessNumber 终端入网证号
     * @return EcoVehModel
     */
    public static EcoVehicleModel fromEntry(VehicleModel v, VehModelModel vm, VehicleOperModel om, VehicleSellModel sm,
                                            EcoTBoxModel tm, VehicleDriveDeviceLkModel lk, String iccid, String networkAccessNumber){
        // TODO 缺少信息
        DataLoader.loadNames(v);
        DataLoader.loadNames(vm);
        DataLoader.loadNames(om);
        DataLoader.loadNames(sm);
        if(om == null) { om = new VehicleOperModel(); }
        if(sm == null) { sm = new VehicleSellModel(); }
        if(lk == null) { lk = new VehicleDriveDeviceLkModel(); }
        if(tm == null) { tm = new EcoTBoxModel(); }
        EcoVehicleModel m = new EcoVehicleModel();
        m.setId(v.getId());
        m.setVehicleLicense(om.getLicensePlate());
        m.setLicenseColor(om.getLicenseColorName());
        m.setVehicleStructure(StringUtils.ts(vm.getCarBodyStructure()));
        m.setVehicleColor(v.getColor());
        m.setVehicleLoad(StringUtils.ts(vm.getApprovedLoad()));
        m.setVehicleLong(StringUtils.ts(vm.getVehLang()));
        m.setVehicleWide(StringUtils.ts(vm.getVehWide()));
        m.setVehicleHigh(StringUtils.ts(vm.getVehHigh()));
        m.setGrossVehicleMass(StringUtils.ts(vm.getVehWeight()));
        m.setVehicleType(vm.getVehicleType());
        m.setIndustryType(StringUtils.ts(om.getOperUseFor()));
        m.setVehicleModel(vm.getVehModelName());
        m.setBuyingDate(sm.getSellDate());
        m.setVehicleFrameNo(v.getVin());
        m.setDrivingLicenseNo(om.getSellLicenseNo());
        m.setEngineModel(lk.getEngineModelName());
        m.setEngineNo(lk.getDrvieDeviceCode());
        m.setVehiclePlace(om.getOperLicenseCityName());
        m.setTechnicalLevel(StringUtils.ts(vm.getVehTechLevel()));
        m.setProductionDate(v.getFactoryDate());
        m.setGradeAssessmentDate(vm.getRatingDate());
        m.setTwoMaintenanceDate(om.getSecondaryMtcTime());
        m.setTwoMaintenanceStatus(StringUtils.ts(om.getSecondaryMtcStatus()));
        m.setYearEvaluationStatus(StringUtils.ts(om.getAnnualInspectionStatus()));
        m.setYearInspectionPeriod(om.getAnnualInspectionDate());
        m.setInsurancePeriod(om.getSellSecureDate());
        m.setMaintenancePeriod(om.getMaintenancePeriod());
        m.setEnterpriseName(v.getManuUnitName());
        m.setContactsName(om.getOperSupportOwnerName());
        m.setContactPhone(om.getOperSupportOwnerPhone());
        m.setTerminalSim(iccid);
        m.setRegisterDate(v.getProduceDate());
        m.setOrganizationId(vm.getVehUnitName());
        m.setEpaVehicleType(vm.getEpaVehType());
        m.setTransVehicleType(vm.getTransportBureauVehType());
        m.setTerminalAllSim(null);
        m.setOwnerAddress(null);
        m.setLicenseModel(om.getLicenseName());
        m.setAdministrativeArea(om.getOperSaveCityName());
        m.setAdministrativeAddress(om.getOperSaveAddress());
        m.setTotalNumberGuest(vm.getTotalGuestsNum());
        m.setCurbWeight(StringUtils.ts(vm.getCurbWeight()));
        m.setMaximumTotalMassOfTrain(StringUtils.ts(vm.getMaxTotalMass()));
        m.setNetNumber(networkAccessNumber);
        m.setInitialRegistrationDate(v.getFactoryDate());
        m.setAnnualInspectionDate(om.getAnnualInspectionDate());
        m.setMandatoryScrapDate(om.getForcedScrapDate());
        m.setEnterpriseShortName(v.getManuUnitName());
        m.setVehicleSN(v.getVehCertificateNumber());
        m.setChipType(tm.getChipModel());
        m.setTboxType(tm.getTboxModel());
        String vehRegisterMode = "";
        if(vm.getRecordActivationMode() != null) {
            if(vm.getRecordActivationMode() == 1) {
                vehRegisterMode = "0";
            } else if(vm.getRecordActivationMode() == 2) {
                vehRegisterMode = "1";
            }
        }
        m.setVehRegisterMode(vehRegisterMode);
        return m;
    }





}
