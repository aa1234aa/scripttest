package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 16:13
*/

import com.bitnei.cloud.sys.domain.VehModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.util.RandomValue;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@Service
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehModelUtil {


    /** 车辆型号 **/
    private String vehModelName="bitneivehModel"+ RandomValue.getNum(1000,9999);
    /** 公告号id **/
    private String noticeId;
    /** 配置名称 **/
    private String configName="默认车辆配置名称";
    /** 检测机构名称 **/
    private String detectionMechanismName="默认检测机构";
    /** 规约id **/
    private String ruleId="bfd0c8de4878410088fb573e165d4a09";
    /** 车辆厂商id **/
    private String vehUnitId;
    /** 车型符合性报告编号 **/
    private String vehReportNumber=String.valueOf(RandomValue.getNum(100000,999999));
    /** 整车控制器型号 **/
    private String controllerModel="默认整车控制型号";
    /** 整车控制器厂商 **/
    private String controllerUnitId;
    /** 底盘型号 **/
    private String chassisModel="默认底盘型号";
    /** 底盘生产企业 **/
    private String chassisUnitId;
    /** 车重(kg) **/
    private Double vehWeight=1000D;
    /** 整备质量 **/
    private Double curbWeight=1200D;
    /** 车长(mm) **/
    private Double vehLang=6000D;
    /** 车长(mm) **/
    private Double vehWide=1500D;
    /** 车高(mm) **/
    private Double vehHigh=1500D;
    /** 额定载客(人) **/
    private Integer ratedCapacity=6;
    /** 节油率水平 **/
    private Integer saveFuelStandard=20;
    /** 电池种类 **/
    private Integer battType=1;
    /** 电池包数量 **/
    private Integer batteryPackageCount=888;
    /** 电池包串并联方式 **/
    private Integer batteryLinkMode=1;
    /** 驱动电机数量 **/
    private Integer driveDeviceCount=12;
    /** 驱动电机种类 **/
    private Integer driveType=1;
    /** 传动系统结构 **/
    private Integer driveStructType=1;
    /** 驱动方式 **/
    private Integer driveMode=1;
    /** 动力方式 **/
    private Integer powerMode=1;
    /** 整体性能信息(Json) **/
    private String efficiencyJson="{\"constantSpeedRange\":122,\"drivingMileageUnderWorkingConditions\":112,\"energyConsumptionPerUnitLoadMass\":\"\",\"energyConsumptionRate\":\"\",\"energyDensityBatterySystem\":112,\"environmentalProtectionType\":\"\",\"environmentalProtectionTypeDisplay\":\"\",\"fastChargeRatio\":\"\",\"fcvFuelForm\":\"\",\"fcvFuelFormDisplay\":\"\",\"fuelCellEconomy\":\"\",\"fuelCellEfficiency\":\"\",\"fuelConsumptionHundredKm\":\"\",\"gasStorageTankCapacityKG\":\"\",\"gasStorageTankCapacityL\":\"\",\"halfhourMaximumSpeed\":\"\",\"hevFuelForm\":\"\",\"hevFuelFormDisplay\":\"\",\"hevFuelType\":\"\",\"hevFuelTypeDisplay\":\"\",\"hundredKmAcceleration\":\"\",\"kilometersTonElectricityConsumption\":\"\",\"kilometresElectricityConsumption\":119,\"maximumSpeed\":112,\"motorRatedVoltage\":\"\",\"numberIndividualBatteries\":\"\",\"oilSavingRateLevel\":\"\",\"peakPowerDrivingMotor\":\"11211\",\"phevFuelForm\":\"\",\"phevFuelFormDisplay\":\"\",\"phevFuelType\":\"\",\"phevFuelTypeDisplay\":\"\",\"ratioOfEachGear\":\"\",\"revFuelForm\":\"\",\"revFuelFormDisplay\":\"\",\"revFuelType\":\"\",\"revFuelTypeDisplay\":\"\",\"tankCapacity\":\"\",\"totalNumberTemperatureProbes\":\"\",\"totalStorageCapacity\":112}";


    private VehModel vehModel;
    private VehModelModel vehModelModel;

    public VehModelModel createModel(){

        vehModel=new VehModel();
        vehModel.setVehModelName(vehModelName);
        vehModel.setNoticeId(noticeId);
        vehModel.setConfigName(configName);
        vehModel.setDetectionMechanismName(detectionMechanismName);
        vehModel.setRuleId(ruleId);
        vehModel.setVehUnitId(vehUnitId);
        vehModel.setVehReportNumber(vehReportNumber);
        vehModel.setControllerModel(controllerModel);
        vehModel.setControllerUnitId(controllerUnitId);
        vehModel.setChassisModel(chassisModel);
        vehModel.setChassisUnitId(chassisUnitId);
        vehModel.setVehWeight(vehWeight);
        vehModel.setCurbWeight(curbWeight);
        vehModel.setVehLang(vehLang);
        vehModel.setVehWide(vehWide);
        vehModel.setVehHigh(vehHigh);
        vehModel.setRatedCapacity(ratedCapacity);
        vehModel.setSaveFuelStandard(saveFuelStandard);
        vehModel.setBattType(battType);
        vehModel.setBatteryPackageCount(batteryPackageCount);
        vehModel.setBatteryLinkMode(batteryLinkMode);
        vehModel.setDriveDeviceCount(driveDeviceCount);
        vehModel.setDriveType(driveType);
        vehModel.setDriveMode(driveMode);
        vehModel.setDriveStructType(driveStructType);
        vehModel.setPowerMode(powerMode);
        vehModel.setEfficiencyJson(efficiencyJson);

        vehModelModel = new VehModelModel();
        vehModelModel = VehModelModel.fromEntry(vehModel);
        return vehModelModel;


    }

}
