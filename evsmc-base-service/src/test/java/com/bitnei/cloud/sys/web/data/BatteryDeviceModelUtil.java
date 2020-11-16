package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 15:24
*/

import com.bitnei.cloud.sys.domain.BatteryDeviceModel;
import com.bitnei.cloud.sys.model.BatteryDeviceModelModel;
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
public class BatteryDeviceModelUtil {

    /** 型号名称 **/
    private String name="bitneidlcdc"+RandomValue.getNum(1000,9999);
    /** 电池类型(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) **/
    private Integer batteryType=RandomValue.getNum(1,6);
    /** 总成标称电压(V) **/
    private Double fixedVol=Double.valueOf(RandomValue.getNum(120,200));
    /** 总储电量(kW.h) **/
    private Double totalElecCapacity=Double.valueOf(RandomValue.getNum(1200,2000));
    /** 系统能量密度(Wh/kg) **/
    private Double capacityDensity=Double.valueOf(RandomValue.getNum(1200,2000));
    /** 额定容量(Ah) **/
    private Double fixedAh=Double.valueOf(RandomValue.getNum(1200,2000));
    /** 额定总重量(kg) **/
    private Double fixedKg=Double.valueOf(RandomValue.getNum(1200,2000));
    /** 额定输出电流(A) **/
    private Double fixedOutputA=Double.valueOf(RandomValue.getNum(5,12));
    /** 最高允许温度 **/
    private Double maxTempC=Double.valueOf(RandomValue.getNum(90,200));
    /** 串并联方式 1:串联 2:并联 **/
    private Integer linkMode=RandomValue.getNum(1,2);
    /** 充电循环次数 **/
    private Integer chgCycleCount=RandomValue.getNum(10000,90000);
    /** 充电倍率 **/
    private String chgRate=String.valueOf(RandomValue.getNum(1000,8000));
    /** 最大允许充电容量(kWh) **/
    private Double chgMaxKwh=Double.valueOf(RandomValue.getNum(1200,2000));
    /** 最高允许充电总电压(V) **/
    private Double chgMaxV=Double.valueOf(RandomValue.getNum(120,200));
    /** 最大允许充电电流(A) **/
    private Double chgMaxA=Double.valueOf(RandomValue.getNum(2,12));
    /** 探针个数(个) **/
    private Integer tempMonitorCount=RandomValue.getNum(6,12);
    /** 车载能源管理系统型号 **/
    private String bmsModel="cznyglxtxh"+RandomValue.getNum(1000,9999);
    /** 单体数量 **/
    private Integer batteryCount=RandomValue.getNum(6,12);
    /** 单体型号 **/
    private String batteryCellModel="dtxh"+RandomValue.getNum(1000,9999);
    /** 封装方式 1:圆柱形电池 2:方形电池 3:软包电池 4:固态电池 **/
    private Integer cellPackageMode=RandomValue.getNum(1,4);
    /** 生产厂商 **/
    private String unitId;
    /** 单体生产企业 **/
    private String batteryCellUnitId;
    /** 车载能源管理系统生产企业 **/
    private String bmsUnitId;
    /** 尺寸(长/宽/高[mm]) **/
    private String appearanceSize=String.valueOf(RandomValue.getNum(100,200));

    private BatteryDeviceModel batteryDeviceModel;
    private BatteryDeviceModelModel batteryDeviceModelModel;

    public BatteryDeviceModelModel createModel(){
        batteryDeviceModel = new BatteryDeviceModel();
        batteryDeviceModel.setName(name);
        batteryDeviceModel.setUnitId(unitId);
        batteryDeviceModel.setBatteryType(batteryType);
        batteryDeviceModel.setFixedVol(fixedVol);
        batteryDeviceModel.setTotalElecCapacity(totalElecCapacity);
        batteryDeviceModel.setCapacityDensity(capacityDensity);
        batteryDeviceModel.setFixedAh(fixedAh);
        batteryDeviceModel.setFixedOutputA(fixedOutputA);
        batteryDeviceModel.setFixedKg(fixedKg);
        batteryDeviceModel.setMaxTempC(maxTempC);
        batteryDeviceModel.setLinkMode(linkMode);
        batteryDeviceModel.setChgCycleCount(chgCycleCount);
        batteryDeviceModel.setChgMaxA(chgMaxA);
        batteryDeviceModel.setChgMaxV(chgMaxV);
        batteryDeviceModel.setChgRate(chgRate);
        batteryDeviceModel.setChgMaxKwh(chgMaxKwh);
        batteryDeviceModel.setTempMonitorCount(tempMonitorCount);
        batteryDeviceModel.setBmsModel(bmsModel);
        batteryDeviceModel.setBatteryCount(batteryCount);
        batteryDeviceModel.setBatteryCellModel(batteryCellModel);
        batteryDeviceModel.setCellPackageMode(cellPackageMode);
        batteryDeviceModel.setBatteryCellUnitId(batteryCellUnitId);
        batteryDeviceModel.setBmsModel(bmsModel);
        batteryDeviceModel.setAppearanceSize(appearanceSize);
        batteryDeviceModel.setBmsUnitId(bmsUnitId);

        batteryDeviceModelModel=new BatteryDeviceModelModel();
        batteryDeviceModelModel=BatteryDeviceModelModel.fromEntry(batteryDeviceModel);
        return batteryDeviceModelModel;

    }
}
