package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/30 15:48
*/

import com.bitnei.cloud.sys.domain.FuelSystemModel;
import com.bitnei.cloud.sys.model.FuelSystemModelModel;
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
public class FuelSystemModelUtil{


    /** 型号名称 **/
    private String name="bitneirldc"+ RandomValue.getNum(1000,9999);
    /** 额定功率(KW) **/
    private Double ratedPower=100D;
    /** 峰值功率(KW) **/
    private Double peakPower=110D;
    /** 体积功率密度(kw/L) **/
    private Double volumePowerDensity=50D;
    /** 燃料电池升压器电高输出电压(V) **/
    private Double maxOutputVoltage=8D;
    /** 储氢罐个数 **/
    private Integer hydrogenStorageNumber=6;
    /** 储氢罐容量(L) **/
    private Double hydrogenStorageCapacity=4D;
    /** 燃料电池系统控制器型号 **/
    private String controllerModel="rldccontroller";
    /** 燃料电池系统生产企业 **/
    private String prodUnitId;
    /** 燃料电池系统控制器生产企业 **/

    private FuelSystemModel fuelSystemModel;
    private FuelSystemModelModel fuelSystemModelModel;

    public FuelSystemModelModel createModel(){

        fuelSystemModel = new FuelSystemModel();
        fuelSystemModel.setName(name);
        fuelSystemModel.setRatedPower(ratedPower);
        fuelSystemModel.setPeakPower(peakPower);
        fuelSystemModel.setVolumePowerDensity(volumePowerDensity);
        fuelSystemModel.setMaxOutputVoltage(maxOutputVoltage);
        fuelSystemModel.setHydrogenStorageNumber(hydrogenStorageNumber);
        fuelSystemModel.setHydrogenStorageCapacity(hydrogenStorageCapacity);
        fuelSystemModel.setControllerModel(controllerModel);

        fuelSystemModelModel=new FuelSystemModelModel();
        fuelSystemModelModel=FuelSystemModelModel.fromEntry(fuelSystemModel);
        return fuelSystemModelModel;
    }

}
