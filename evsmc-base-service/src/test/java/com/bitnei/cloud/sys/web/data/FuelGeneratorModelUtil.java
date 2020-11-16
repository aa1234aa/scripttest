package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 10:08
*/

import com.bitnei.cloud.sys.domain.FuelGeneratorModel;
import com.bitnei.cloud.sys.model.FuelGeneratorModelModel;
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
public class FuelGeneratorModelUtil {

    /** 燃油发电机型号 **/
    private String name="bitneiryfdj"+ RandomValue.getNum(1000,9999);
    /**  进气方式 **/
    private Integer airInlet=0;
    /** 燃料形式 **/
    private Integer fuelForm=1;
    /** 油箱容量(L) **/
    private Double tankCapacity=33D;
    /** 品牌名称 **/
    private String brandName="test发动机型号";
    /** 燃油发电机控制器型号 **/
    private String controllerModel="FDJXH"+RandomValue.getNum(1000,9999);
    /** 峰值功率(KW) **/
    private Double peakPower=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值功率转速(r/min) **/
    private Double peakPowerSpeed=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值转矩(N.m) **/
    private Double peakTorque=Double.valueOf(RandomValue.getNum(10000,15000));
    /** 峰值扭矩转速(r/min) **/
    private Double peakTorqueSpeed=Double.valueOf(RandomValue.getNum(10000,15000));
    /** 燃油发电机生产企业 **/
    private String prodUnitId;
    /** 燃油发电机控制器生产企业 **/
    private String controllerProdUnitId;

    private FuelGeneratorModel fuelGeneratorModel;
    private FuelGeneratorModelModel fuelGeneratorModelModel;

    public FuelGeneratorModelModel createModel(){

        fuelGeneratorModel = new FuelGeneratorModel();
        fuelGeneratorModel.setName(name);
        fuelGeneratorModel.setAirInlet(airInlet);
        fuelGeneratorModel.setFuelForm(fuelForm);
        fuelGeneratorModel.setTankCapacity(tankCapacity);
        fuelGeneratorModel.setBrandName(brandName);
        fuelGeneratorModel.setControllerModel(controllerModel);
        fuelGeneratorModel.setPeakPower(peakPower);
        fuelGeneratorModel.setPeakPowerSpeed(peakPowerSpeed);
        fuelGeneratorModel.setPeakTorque(peakTorque);
        fuelGeneratorModel.setPeakTorqueSpeed(peakTorqueSpeed);
        fuelGeneratorModel.setProdUnitId(prodUnitId);
        fuelGeneratorModel.setControllerProdUnitId(controllerProdUnitId);

        fuelGeneratorModelModel=new FuelGeneratorModelModel();
        fuelGeneratorModelModel=FuelGeneratorModelModel.fromEntry(fuelGeneratorModel);
        return fuelGeneratorModelModel;

    }
}
