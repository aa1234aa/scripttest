package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 17:03
*/

import com.bitnei.cloud.sys.domain.EngeryDevice;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
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
public class EngeryDeviceModelUtil  {


    /** 可充电储能装置编码 **/
    private String name="bitneikcdcnzz"+ RandomValue.getNum(1000,9999);
    /** 可充电装置类型 1:动力蓄电池 2:超级电容
     **/
    private Integer modelType=1;
    /** 动力蓄电池型号id **/
    private String batteryModelId;
    /** 安装位置
     **/
    private String installPostion="前盘位置";
    /** 生产日期 **/
    private String produceDate=RandomValue.getNowTimeWithOutHMS();
    /** 发票号 **/
    private String invoiceNo="fph"+RandomValue.getNum(100000,999999);

    private EngeryDevice engeryDevice;
    private EngeryDeviceModel engeryDeviceModel;

    public EngeryDeviceModel  createModel(){

        engeryDevice = new EngeryDevice();
        engeryDevice.setName(name);
        engeryDevice.setModelType(modelType);
        engeryDevice.setBatteryModelId(batteryModelId);
        engeryDevice.setInstallPostion(installPostion);
        engeryDevice.setProduceDate(produceDate);
        engeryDevice.setInvoiceNo(invoiceNo);

        engeryDeviceModel=new EngeryDeviceModel();
        engeryDeviceModel=EngeryDeviceModel.fromEntry(engeryDevice);
        return engeryDeviceModel;

    }


}
