package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 10:46
*/

import com.bitnei.cloud.sys.domain.PowerDevice;
import com.bitnei.cloud.sys.model.PowerDeviceModel;
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
public class PowerDeviceModelUtil {


    /** 发电装置编码 **/
    private String code="bitneifdzh"+ RandomValue.getNum(1000,9999);
    /** 发电装置类型 1:燃油发电机型号 2:燃料电池系统型号 **/
    private Integer modelType=1;
    /** 燃油发电机型号id **/
    private String fuelGeneratorModelId;
    /** 发电装置序号 **/
    private String sequenceNumber="bitnei"+RandomValue.getNum(1000,9999);
    /** 安装位置 1：前置 2：中置 3：后置 **/
    private Integer installPosition=RandomValue.getNum(1,3);
    /** 发电装置生产日期 **/
    private String produceDate=RandomValue.getNowTimeWithOutHMS();
    /** 发电装置发票号 **/
    private String invoiceNo="bitneiNo"+RandomValue.getNum(1000,9999);

    private PowerDevice powerDevice;
    private PowerDeviceModel powerDeviceModel;

    public PowerDeviceModel createModel(){

        powerDevice = new PowerDevice();
        powerDevice.setCode(code);
        powerDevice.setModelType(modelType);
        powerDevice.setFuelGeneratorModelId(fuelGeneratorModelId);
        powerDevice.setSequenceNumber(sequenceNumber);
        powerDevice.setInstallPosition(installPosition);
        powerDevice.setProduceDate(produceDate);
        powerDevice.setInvoiceNo(invoiceNo);

        powerDeviceModel=new PowerDeviceModel();
        powerDeviceModel=PowerDeviceModel.fromEntry(powerDevice);
        return powerDeviceModel;

    }
}
