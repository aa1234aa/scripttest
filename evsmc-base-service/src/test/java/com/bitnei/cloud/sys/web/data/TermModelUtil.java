package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 9:26
*/

import com.bitnei.cloud.sys.domain.TermModel;
import com.bitnei.cloud.sys.model.TermModelModel;
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
public class TermModelUtil {

    /** 终端型号 **/
    private String termModelName="bitneitermmodel"+ RandomValue.getNum(1000,9999);
    /** 种类[车机/T-BOX/OBD/智能后视镜等] **/
    private Integer termCategory=2;
    /** 品牌名称 **/
    private String brandName="Test品牌";
    /** 工作电压 **/
    private String workingVoltage=String.valueOf(RandomValue.getNum(110,220));
    /** 功耗 **/
    private String powerWaste=String.valueOf(RandomValue.getNum(100,300));
    /** 固件版本号 **/
    private String firmwareVersion="1.0";
    /** 生产厂商 **/
    private String unitId;
    /** 休眠电流 **/
    private String sleepElectricCurrent=String.valueOf(RandomValue.getNum(10,100));
    /** 内置电池容量 **/
    private String builtInBatteryCapacity=String.valueOf(RandomValue.getNum(1000,1500));
    /** 支持加密芯片 **/
    private Integer supportEncryptionChip=0;
//    /** 车载终端检测编号 **/
//    private String termDetectionNo;
//    /** 终端检测报告扫描件 **/
//    private String detectionReportImgId;
//    /** 终端零件号，用逗号分隔 **/
//    private String termPartFirmwareNumbers;


    private TermModel termModel;
    private TermModelModel termModelModel;

    public TermModelModel createModel(){

        termModel = new TermModel();
        termModel.setTermModelName(termModelName);
        termModel.setTermCategory(termCategory);
        termModel.setBrandName(brandName);
        termModel.setWorkingVoltage(workingVoltage);
        termModel.setPowerWaste(powerWaste);
        termModel.setFirmwareVersion(firmwareVersion);
        termModel.setUnitId(unitId);
        termModel.setSleepElectricCurrent(sleepElectricCurrent);
        termModel.setBuiltInBatteryCapacity(builtInBatteryCapacity);
        termModel.setSupportEncryptionChip(supportEncryptionChip);

        termModelModel=new TermModelModel();
        termModelModel=TermModelModel.fromEntry(termModel);
        return termModelModel;

    }

}
