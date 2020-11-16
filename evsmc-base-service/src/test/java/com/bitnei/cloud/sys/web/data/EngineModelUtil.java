package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 13:21
*/

import com.bitnei.cloud.sys.domain.EngineModel;
import com.bitnei.cloud.sys.model.EngineModelModel;
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
public class EngineModelUtil {

    /** 型号名称 **/
    private String name="bitneifdj"+ RandomValue.getNum(1000,9999);
    /** 进气方式 **/
    private Integer intakeWay=1;
    /** 循环方式 **/
    private Integer circulationWay=1;
    /** 燃料形式 **/
    private Integer fuelForm=1;
    /** 油箱容量(L) **/
    private Double tankCapacity=35D;
    /** 储气罐个数(个) **/
    private Integer gasholderNumber=12;
    /** 储气罐类型 **/
    private Integer gasholderType=1;
    /** 储气罐容量 **/
    private Double gasholderCapacity=12D;
    /** 品牌名称 **/
    private String brandName="bitnei品牌"+RandomValue.getNum(100,999);
    /** 发动机控制器型号 **/
    private String controllerModel="bitnei控制型号"+RandomValue.getNum(100,999);
    /** 峰值功率(KW) **/
    private Double peakPower=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值功率转速(r/min) **/
    private Double peakRotateSpeed=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值转矩(N.m) **/
    private Double peakTorque=Double.valueOf(RandomValue.getNum(1000,1500));;
    /** 峰值扭矩转速(r/min) **/
    private Double peakTorqueRotateSpeed=Double.valueOf(RandomValue.getNum(1000,1500));;
    /** 发动机生产企业 **/
    private String prodUnitId;
    /** 发动机控制器生产企业 **/
    private String controllerProdUnitId;

    private EngineModel engineModel;
    private EngineModelModel engineModelModel;

    public EngineModelModel createModel(){
        engineModel = new EngineModel();
        engineModel.setName(name);
        engineModel.setIntakeWay(intakeWay);
        engineModel.setCirculationWay(circulationWay);
        engineModel.setFuelForm(fuelForm);
        engineModel.setTankCapacity(tankCapacity);
        engineModel.setGasholderCapacity(gasholderCapacity);
        engineModel.setGasholderNumber(gasholderNumber);
        engineModel.setGasholderType(gasholderType);
        engineModel.setBrandName(brandName);
        engineModel.setPeakPower(peakPower);
        engineModel.setPeakRotateSpeed(peakRotateSpeed);
        engineModel.setPeakTorque(peakTorque);
        engineModel.setPeakTorqueRotateSpeed(peakTorqueRotateSpeed);
        engineModel.setControllerModel(controllerModel);
        engineModel.setProdUnitId(prodUnitId);
        engineModel.setControllerProdUnitId(controllerProdUnitId);

        engineModelModel=new EngineModelModel();
        engineModelModel=EngineModelModel.fromEntry(engineModel);
        return engineModelModel;

    }
}
