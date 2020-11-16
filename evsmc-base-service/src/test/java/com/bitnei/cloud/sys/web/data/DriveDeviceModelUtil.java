package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 14:34
*/

import com.bitnei.cloud.sys.domain.DriveDevice;
import com.bitnei.cloud.sys.model.DriveDeviceModel;
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
public class DriveDeviceModelUtil {

    /** 驱动装置编码 **/
    private String code="bitneiqdqz"+ RandomValue.getNum(1000,9999);
    /** 驱动装置类型 1:驱动电机 2:发动机 **/
    private Integer modelType=1;
    /** 驱动电机型号 **/
    private String driveModelId;
    /** 驱动装置序号 **/
    private String sequenceNumber="qdzzxh"+RandomValue.getNum(1000,9999);
    /** 安装位置 1:前置 2:后置 3:轮边 4:轮毂内 **/
    private Integer installPosition=RandomValue.getNum(1,4);
    /** 生产日期 **/
    private String produceDate=RandomValue.getNowTimeWithOutHMS();
    /** 驱动装置发票号 **/
    private String invoiceNo="qdzzfp"+RandomValue.getNum(1000,9999);


    private DriveDevice driveDevice;
    private DriveDeviceModel driveDeviceModel;

    public DriveDeviceModel createModel(){

        driveDevice = new DriveDevice();
        driveDevice.setCode(code);
        driveDevice.setModelType(modelType);
        driveDevice.setDriveModelId(driveModelId);
        driveDevice.setSequenceNumber(sequenceNumber);
        driveDevice.setInstallPosition(installPosition);
        driveDevice.setProduceDate(produceDate);
        driveDevice.setInvoiceNo(invoiceNo);

        driveDeviceModel=new DriveDeviceModel();
        driveDeviceModel=DriveDeviceModel.fromEntry(driveDevice);
        return driveDeviceModel;


    }
}
