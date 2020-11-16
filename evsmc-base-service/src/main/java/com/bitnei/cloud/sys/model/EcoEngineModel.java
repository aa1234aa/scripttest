package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.sys.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 环保国家平台发动机model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class EcoEngineModel {

    private String id;

    /** 发动机生产企业统一社会信用代码 1 **/
    private String engineFirmId;

    /** 发动机生产企业名称 1 **/
    private String engineFirmName;

    /** 发动机系族名称 1 **/
    private String engineFamilyName;

    /** 发动机型号 1 **/
    private String engineModel;

    /** 发动机类别 **/
    private String engineType;

    /** 排放水平：0全部1国六2国五3国四4国三5国二6排气后处理系统改装车辆  1**/
    private String emissionLevel;

    /** 环保信息公开编号 1 **/
    private String epaPublicInfoNum;

    /** 发动机排量（L）1 **/
    private String engineSweptVolume;

    /** 气缸排列型式 1**/
    private String engineCylinderArrangement;

    /** 最大功率/转速（kW/r/min）1 **/
    private String engineMaxPowerSpeed;

    /** 最大扭矩/转速（Nm/r/min）1 **/
    private String engineMaxTorqueSpeed;

    /** 排气后处理系统形式 **/
    private String pcdExhaustType;

    /** ETC循环功 **/
    private String etcCycleWork;

    /** WTHC循环功 **/
    private String wthcCycleWork;

    /** 参考扭矩 **/
    private String engineTorque;

    /** 软件标定识别号（CALID）1 **/
    private String engineCalidSoftwareSn;

    /** 标定验证码（CVN） 1**/
    private String engineCVNCalibrationCode;

    /** 发动机额定功率 **/
    private String engineRatedPower;

    /** 发动机布置方式 **/
    private String engineArrangement;

    /** 是否有副发动机 1 **/
    private String engineAuxiliaryEngine;

    /** 副发动机排放标准 1 **/
    private String engineAuxiliaryEmission;

    /** 副发动机生产企业 1 **/
    private String engineAuxiliaryFirm;

    /** 副发动机燃料类型 1**/
    private String engineAuxiliaryFuelType;

    /** 燃烧室结构 **/
    private String engineCombustionChamber;

    /** 容积压缩比 **/
    private String engineCompressionRatio;

    /** 冷却方式 **/
    private String engineCoolingMethod;

    /** 缸数-缸径×行程（mm） **/
    private String engineCylinderBoreStroke;

    /** 发动机缸心距（mm） **/
    private String engineCylinderPitch;

    /** 缸体构造 **/
    private String engineCylinderStructure;

    /** ECU电控单元生产厂 **/
    private String engineECUHardwareFirm;

    /** ECU电控单元（硬件）型号 **/
    private String engineECUHardwareModel;

    /** ECU软体生产厂 **/
    private String engineECUSoftwareFirm;

    /** ECU软体型号 **/
    private String engineECUSoftwareModel;

    /** EGR生产厂 **/
    private String engineEGRFirm;

    /** EGR型号 **/
    private String engineEGRModel;

    /** 排气背压（kPa） **/
    private String engineExhaustBackPressure;

    /** 燃料及硫含量（10-6） **/
    private String engineFuelSulfur;

    /** 供油系统形式 **/
    private String engineFuelSupplyForm;

    /** 调速器生产厂 **/
    private String engineGovernorFirm;

    /** 调速器型号 **/
    private String engineGovernorModel;

    /** 怠速转速（r/min） **/
    private String engineIdleSpeed;

    /** 发动机点火方式 **/
    private String engineIgnitionMode;

    /** 喷油器生产厂 **/
    private String engineInjectionFirm;

    /** 喷油器型号 **/
    private String engineInjectionModel;

    /** 喷油器喷射压力（MPa） **/
    private String engineInjectorPressure;

    /** 喷油泵生产厂 **/
    private String engineInjectionPumpFirm;

    /** 喷油泵型号 **/
    private String engineInjectionPumpModel;

    /** 进气方式 **/
    private String engineInletMode;

    /** 进气阻力(kPa) **/
    private String engineIntakeResistance;

    /** 中冷器生产厂 **/
    private String engineIntercoolerModelFirm;

    /** 中冷器型号 **/
    private String engineIntercoolerModel;

    /** 中冷器形式 **/
    private String engineIntercoolerType;

    /** 中冷器最高出口温度（℃） **/
    private String engineIntercoolerOutTem;

    /** 最高空车转速（r/min） **/
    private String engineMaxEmptySpeed;

    /** 最大净功率/转速 **/
    private String maximumNetPower;

    /** 最大扭矩转速时每冲程燃料供给量（ml） **/
    private String engineRatedFuelSupply;

    /** 单缸进/排气阀数 **/
    private String engineSingleValveNum;

    /** 额定功率转速时每冲程燃料供给量（ml） **/
    private String engineStrokeFuelSupply;

    /** 增压器生产厂 **/
    private String engineTurboChargerFirm;

    /** 增压器型号 **/
    private String engineTurboChargerModel;

    /** 颗粒捕集器型号 **/
    private String pcdParticulateModel;

    /** 颗粒捕集器安装位置 **/
    private String pcdParticulatePosition;

    /** 颗粒物捕集器结构 **/
    private String pcdParticulateStructure;

    /** 颗粒物捕集器型式 **/
    private String pcdParticulateType;

    /** 颗粒物捕集器容积（ml） **/
    private String pcdParticulateVolume;

    /** 后处理控制器生产厂 **/
    private String pcdPostProcessingFirm;

    /** 后处理控制器型号 **/
    private String pcdPostProcessingModel;

    /** 反应剂正常工作温度范围（K） **/
    private String pcdReactantTemperature;

    /** 反应剂类型和浓度 **/
    private String pcdReactantType;

    /** 再生的温度（K）及压力范围（kPa） **/
    private String pcdRegenerationTemperature;

    /** 尿素可用里程理论值 **/
    private String pcdUreaMileage;

    /** 尿素可用里程实际值 **/
    private String pcdUreaMileageReal;

    /** 尿素消耗比例理论值 **/
    private String pcdUreaValue;

    /** 尿素消耗比例实际值 **/
    private String pcdUreaValueReal;

    /** 空滤器生产企业 **/
    private String pcdAirFilterFirm;

    /** 空滤器型号 **/
    private String pcdAirFilterModel;

    /** 空气喷射系统型式 **/
    private String pcdAirInjectionType;

    /** 载体孔密度 **/
    private String pcdCarrierDensity;

    /** 载体生产厂 **/
    private String pcdCarrierFirm;

    /** 载体材料及结构 **/
    private String pcdCarrierMaterial;

    /** 催化转化器生产厂 **/
    private String pcdCatalyticFirm;

    /** 催化转化器作用形式/催化器载体材料 */
    private String pcdCatalyticFormMaterial;

    /** 催化转化器型号 **/
    private String pcdCatalyticModel;

    /** 催化器载体材料 **/
//    private String pcdCatalyticMaterial;

    /** 催化转化器作用形式 **/
//    private String pcdCatalyticForm;

    /** 催化转化器装车数量 **/
    private String pcdCatalyticNum;

    /** 催化转化器正常温度范围（K） **/
    private String pcdCatalyticTemperature;


    /** 贵金属（催化剂）种类、总含量（g） **/
    private String pcdCatalyticType;


    /** 贵金属（催化剂）总含量（g） **/
//    private String pcdCatalyticTotal;

    /** 贵金属（催化剂）种类 **/
//    private String pcdCatalyticType;

    /** 催化单元数目 **/
    private String pcdCatalyticUnitsNum;

    /** 贵金属（催化剂）比例 **/
//    private String pcdCatalyticRatio;

    /** 催化转化器容积（ml） **/
    private String pcdCatalyticVolume;

    /** 再生系统2次再生之间的ETC循环次数 **/
    private String pcdETCCyclesNum;

    /** 后处理进气口据发动机排气口距离 **/
    private String pcdExhaustPortDistance;

    /** 保温材料 **/
    private String pcdHeatInsulator;

    /** 是否与发动机认证一致 **/
    private String pcdIsConsistent;

    /** 后处理是否保温 **/
    private String pcdIsInsulation;

    /** 排气消声器生产厂 **/
    private String pcdMufflerFirm;

    /** 排气消声器型号 **/
    private String pcdMufflerModel;

    /** 氮氧传感器生产厂 **/
    private String pcdNoSensorFirm;

    /** 氮氧传感器型号 **/
    private String pcdNoSensorType;

    /** 涂层生产厂 **/
    private String pcdPaintCoatFirm;

    /** 颗粒捕集器生产厂 **/
    private String pcdParticulateFirm;

    /** 颗粒捕集器再生方法 **/
    private String pcdParticulateMethod;

    /** OBD生产厂 **/
    private String engineOBDFirm;

    /** OBD型号 **/
    private String engineOBDModel;

    /** 总线协议 **/
    private String obdBusProtocol;

    /** 总线类型 **/
    private String obdBusType;

    /** 诊断协议 **/
    private String obdDiagnosticProtocol;

    /** 接口位置 **/
    private String obdInterfaceLocation;

    /** 接口照片 **/
    private String obdInterfacePhoto;

    /** 接口类型 **/
    private String obdInterfaceType;

    /** 是否加密 **/
    private String obdIsEncryption;

    /** 外特性-转速(r/min) **/
    private String externalSpeed;

    /** 外特性-扭矩(N.m) **/
    private String externalTorsion;

    /** ETC试验循环功（kWh） **/
    private String testCycleWork;

    /** 联系电话 **/
    private String contactorPhone;

    /** 联系人姓名 **/
    private String contactorName;


    /**
     * 将实体转为前台model
     * @param e EngineModelModel
     * @return EcoEngineModel
     */
    public static EcoEngineModel fromEntry(EngineModelModel e) {
        EcoEngineModel m = new EcoEngineModel();
        DataLoader.loadNames(e);
        Map<String, Object> ecMap = e.getExtendColumns();
        m.id = e.getId();
        m.engineFirmId = e.getProdUnitOrgCode();
        m.engineFirmName = e.getProdUnitDisplay();
        m.engineFamilyName = e.getName();
        m.engineModel = e.getName();
        m.engineType = StringUtils.ts(ecMap.get("engineCategory"));
        m.emissionLevel = StringUtils.ts(ecMap.get("emissionLevel"));
        m.epaPublicInfoNum = StringUtils.ts(ecMap.get("environmentalInfoNo"));
        m.engineSweptVolume = StringUtils.ts(ecMap.get("sweptVolume"));
        m.engineCylinderArrangement = StringUtils.ts(ecMap.get("cylinderArrangement"));
        m.engineMaxPowerSpeed = StringUtils.ts(ecMap.get("maxPowerSpeed"));
        m.engineMaxTorqueSpeed = StringUtils.ts(ecMap.get("maxTorqueSpeed"));
        m.pcdExhaustType = StringUtils.ts(ecMap.get("pcdExhaustType"));
        m.etcCycleWork = StringUtils.ts(ecMap.get("etcCycleWork"));
        m.wthcCycleWork = StringUtils.ts(ecMap.get("wthcCycleWork"));
        m.engineTorque = StringUtils.ts(ecMap.get("engineTorque"));
        m.engineCalidSoftwareSn = StringUtils.ts(ecMap.get("calidSoftwareSn"));
        m.engineCVNCalibrationCode = StringUtils.ts(ecMap.get("cvnCalibrationCode"));
        m.engineRatedPower = StringUtils.ts(ecMap.get("engineRatedPower"));
        m.engineArrangement = StringUtils.ts(ecMap.get("engineArrangement"));
        m.engineAuxiliaryEngine = StringUtils.ts(ecMap.get("auxiliaryEngine"));
        m.engineAuxiliaryEmission = StringUtils.ts(ecMap.get("auxiliaryEmission"));
        m.engineAuxiliaryFirm = StringUtils.ts(ecMap.get("auxiliaryFirm"));
        m.engineAuxiliaryFuelType = StringUtils.ts(ecMap.get("auxiliaryFuelType"));
        m.engineCombustionChamber = StringUtils.ts(ecMap.get("combustionChamber"));
        m.engineCompressionRatio = StringUtils.ts(ecMap.get("compressionRatio"));
        m.engineCoolingMethod = StringUtils.ts(ecMap.get("coolingMethod"));
        m.engineCylinderBoreStroke = StringUtils.ts(ecMap.get("cylinderBoreStroke"));
        m.engineCylinderPitch = StringUtils.ts(ecMap.get("cylinderPitch"));
        m.engineCylinderStructure = StringUtils.ts(ecMap.get("cylinderStructure"));
        m.engineECUHardwareFirm = StringUtils.ts(ecMap.get("ecuHardwareFirm"));
        m.engineECUHardwareModel = StringUtils.ts(ecMap.get("ecuHardwareModel"));
        m.engineECUSoftwareFirm = StringUtils.ts(ecMap.get("ecuSoftwareFirm"));
        m.engineECUSoftwareModel = StringUtils.ts(ecMap.get("ecuSoftwareModel"));
        m.engineEGRFirm = StringUtils.ts(ecMap.get("egrFirm"));
        m.engineEGRModel = StringUtils.ts(ecMap.get("egrModel"));
        m.engineExhaustBackPressure = StringUtils.ts(ecMap.get("exhaustBackPressure"));
        m.engineFuelSulfur = StringUtils.ts(ecMap.get("fuelSulfur"));
        m.engineFuelSupplyForm = StringUtils.ts(ecMap.get("fuelSupplyForm"));
        m.engineGovernorFirm = StringUtils.ts(ecMap.get("governorFirm"));
        m.engineGovernorModel = StringUtils.ts(ecMap.get("governorModel"));
        m.engineIdleSpeed = StringUtils.ts(ecMap.get("idleSpeed"));
        m.engineIgnitionMode = StringUtils.ts(ecMap.get("engineIgnitionMode"));
        m.engineInjectionFirm = StringUtils.ts(ecMap.get("injectionFirm"));
        m.engineInjectionModel = StringUtils.ts(ecMap.get("injectionModel"));
        m.engineInjectorPressure = StringUtils.ts(ecMap.get("injectorPressure"));
        m.engineInjectionPumpFirm = StringUtils.ts(ecMap.get("injectionPumpFirm"));
        m.engineInjectionPumpModel = StringUtils.ts(ecMap.get("injectionPumpModel"));
        m.engineIntakeResistance = StringUtils.ts(ecMap.get("intakeResistance"));
        m.engineIntercoolerModelFirm = StringUtils.ts(ecMap.get("intercoolerFirm"));
        m.engineIntercoolerModel = StringUtils.ts(ecMap.get("intercoolerModel"));
        m.engineIntercoolerType = StringUtils.ts(ecMap.get("intercoolerType"));
        m.engineIntercoolerOutTem = StringUtils.ts(ecMap.get("maxOutletTemperature"));
        m.engineMaxEmptySpeed = StringUtils.ts(ecMap.get("maxEmptySpeed"));
        m.maximumNetPower = StringUtils.ts(ecMap.get("maximumNetPower"));
        m.engineRatedFuelSupply = StringUtils.ts(ecMap.get("ratedFuelSupply"));
        m.engineSingleValveNum = StringUtils.ts(ecMap.get("singleValveNum"));
        m.engineStrokeFuelSupply = StringUtils.ts(ecMap.get("strokeFuelSupply"));
        m.engineTurboChargerFirm = StringUtils.ts(ecMap.get("turboChargerFirm"));
        m.engineTurboChargerModel = StringUtils.ts(ecMap.get("turboChargerModel"));
        m.pcdParticulateModel = StringUtils.ts(ecMap.get("pcdParticulateModel"));
        m.pcdParticulatePosition = StringUtils.ts(ecMap.get("pcdParticulatePosition"));
        m.pcdParticulateStructure = StringUtils.ts(ecMap.get("pcdParticulateStructure"));
        m.pcdParticulateType = StringUtils.ts(ecMap.get("pcdParticulateType"));
        m.pcdParticulateVolume = StringUtils.ts(ecMap.get("pcdParticulateVolume"));
        m.pcdPostProcessingFirm = StringUtils.ts(ecMap.get("postProcessingFirm"));
        m.pcdPostProcessingModel = StringUtils.ts(ecMap.get("postProcessingModel"));
        m.pcdReactantTemperature = StringUtils.ts(ecMap.get("pcdReactantTemperature"));
        m.pcdReactantType = StringUtils.ts(ecMap.get("pcdReactantType"));
        m.pcdRegenerationTemperature = StringUtils.ts(ecMap.get("pcdCatalyticTemperature"));
        m.pcdUreaMileage = StringUtils.ts(ecMap.get("mileageTheoreticalVal"));
        m.pcdUreaMileageReal = StringUtils.ts(ecMap.get("mileageActualVal"));
        m.pcdUreaValue = StringUtils.ts(ecMap.get("consumeTheoreticalVal"));
        m.pcdUreaValueReal = StringUtils.ts(ecMap.get("consumeActualVal"));
        m.pcdAirFilterFirm = StringUtils.ts(ecMap.get("airFilterFirm"));
        m.pcdAirFilterModel = StringUtils.ts(ecMap.get("airFilterModel"));
        m.pcdAirInjectionType = StringUtils.ts(ecMap.get("airInjectionType"));
        m.pcdCarrierDensity = StringUtils.ts(ecMap.get("pcdCarrierDensity"));
        m.pcdCarrierFirm = StringUtils.ts(ecMap.get("pcdCarrierFirm"));
        m.pcdCarrierMaterial = StringUtils.ts(ecMap.get("pcdCarrierMaterial"));
        m.pcdCatalyticFirm = StringUtils.ts(ecMap.get("pcdCatalyticFirm"));
        m.pcdCatalyticFormMaterial = StringUtils.ts(ecMap.get("pcdCatalyticMaterial"));
        m.pcdCatalyticModel = StringUtils.ts(ecMap.get("pcdCatalyticModel"));
        m.pcdCatalyticNum = StringUtils.ts(ecMap.get("pcdCatalyticNum"));
        m.pcdCatalyticTemperature = StringUtils.ts(ecMap.get("pcdCatalyticTemperature"));
        m.pcdCatalyticType = StringUtils.ts(ecMap.get("pcdCatalyticType"));
        m.pcdCatalyticUnitsNum = StringUtils.ts(ecMap.get("catalyticUnitsNum"));
        m.pcdCatalyticVolume = StringUtils.ts(ecMap.get("pcdCatalyticVolume"));
        m.pcdETCCyclesNum = StringUtils.ts(ecMap.get("etcCyclesNum"));
        m.pcdExhaustPortDistance = StringUtils.ts(ecMap.get("exhaustPortDistance"));
        m.pcdHeatInsulator = StringUtils.ts(ecMap.get("heatInsulator"));
        m.pcdIsConsistent = StringUtils.ts(ecMap.get("isConsistent"));
        m.pcdIsInsulation = StringUtils.ts(ecMap.get("isInsulation"));
        m.pcdMufflerFirm = StringUtils.ts(ecMap.get("mufflerFirm"));
        m.pcdMufflerModel = StringUtils.ts(ecMap.get("mufflerModel"));
        m.pcdNoSensorFirm = StringUtils.ts(ecMap.get("noSensorFirm"));
        m.pcdNoSensorType = StringUtils.ts(ecMap.get("noSensorType"));
        m.pcdPaintCoatFirm = StringUtils.ts(ecMap.get("paintCoatFirm"));
        m.pcdParticulateFirm = StringUtils.ts(ecMap.get("pcdParticulateFirm"));
        m.pcdParticulateMethod = StringUtils.ts(ecMap.get("pcdParticulateMethod"));
        m.obdBusProtocol = StringUtils.ts(ecMap.get("busProtocol"));
        m.obdBusType = StringUtils.ts(ecMap.get("busType"));
        m.obdDiagnosticProtocol = StringUtils.ts(ecMap.get("diagnosticProtocol"));
        m.obdInterfaceLocation = StringUtils.ts(ecMap.get("interfaceLocation"));
        m.obdInterfaceType = StringUtils.ts(ecMap.get("interfaceType"));
        m.obdIsEncryption = StringUtils.ts(ecMap.get("isEncrypt"));
        m.externalSpeed = StringUtils.ts(ecMap.get("externalSpeed"));
        m.externalTorsion = StringUtils.ts(ecMap.get("externalTorque"));
        m.testCycleWork = StringUtils.ts(ecMap.get("testCycleWork"));
        m.contactorPhone = StringUtils.ts(ecMap.get("contactNumber"));
        m.contactorName = StringUtils.ts(ecMap.get("contactName"));
        m.engineInletMode = e.getIntakeWay() == null ? null : e.getIntakeWay().toString();
        m.engineOBDFirm = e.getObdUnitDisplay();
        m.engineOBDModel = e.getObdModelDisplay();
        m.obdInterfacePhoto = e.getInterfacePhoto();
        return m;
    }
}
