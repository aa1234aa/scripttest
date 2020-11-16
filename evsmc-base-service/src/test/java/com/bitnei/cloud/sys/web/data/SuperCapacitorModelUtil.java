package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 15:03
*/

import com.bitnei.cloud.sys.domain.SuperCapacitorModel;
import com.bitnei.cloud.sys.model.SuperCapacitorModelModel;
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
public class SuperCapacitorModelUtil {

    /** 型号名称 **/
    private String name="bitneicjdr"+ RandomValue.getNum(1000,9999);
    /** 超级电容生产企业 **/
    private String unitId;
    /** 额定电压(V) **/
    private Double fixedVol=Double.valueOf(RandomValue.getNum(150,220));
    /** 额定电流(A) **/
    private Double fixedCurrent=Double.valueOf(RandomValue.getNum(5,12));
    /** 总储电量(kW.h) **/
    private Double totalCapacity=Double.valueOf(RandomValue.getNum(10000,99999));
    /** 能量密度(Wh/kg) **/
    private Double capacityDensity=Double.valueOf(RandomValue.getNum(10000,99999));
    /** 功率密度(W/kg) **/
    private Double powerCapacity=Double.valueOf(RandomValue.getNum(10000,99999));

    private SuperCapacitorModel superCapacitorModel;
    private SuperCapacitorModelModel superCapacitorModelModel;

    public SuperCapacitorModelModel createModel(){

        superCapacitorModel = new SuperCapacitorModel();
        superCapacitorModel.setName(name);
        superCapacitorModel.setUnitId(unitId);
        superCapacitorModel.setFixedCurrent(fixedCurrent);
        superCapacitorModel.setFixedVol(fixedVol);
        superCapacitorModel.setTotalCapacity(totalCapacity);
        superCapacitorModel.setCapacityDensity(capacityDensity);
        superCapacitorModel.setPowerCapacity(powerCapacity);;

        superCapacitorModelModel=new SuperCapacitorModelModel();
        superCapacitorModelModel=SuperCapacitorModelModel.fromEntry(superCapacitorModel);
        return superCapacitorModelModel;

    }
}
