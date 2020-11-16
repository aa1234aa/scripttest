package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 9:56
*/

import com.bitnei.cloud.sys.domain.TermModelUnit;
import com.bitnei.cloud.sys.model.TermModelUnitModel;
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
public class TermModelUnitUtil {

    /** 终端厂商自定义编号 **/
    private String serialNumber= RandomValue.getSerialNum();
    /** 终端型号ID **/
    private String sysTermModelId;
    /** 支持通讯协议 **/
    private String supportProtocol;
    /** 协议版本号 **/
    private String protocolVersion="1.11";
    /** imei **/
    private String imei=RandomValue.getImei();
    /** 固件版本号 **/
    private String firewareVersion="1.12";
    /** 生产批次 **/
    private String produceBatch=String.valueOf(RandomValue.getNum(1000,9999));
    /** 出厂日期 **/
    private String factoryDate=RandomValue.getNowTimeWithOutHMS();


    private TermModelUnit termModelUnit;
    private TermModelUnitModel termModelUnitModel;

    public TermModelUnitModel createModel(){

        termModelUnit = new TermModelUnit();
        termModelUnit.setSerialNumber(serialNumber);
        termModelUnit.setSysTermModelId(sysTermModelId);
        termModelUnit.setSupportProtocol(supportProtocol);
        termModelUnit.setProtocolVersion(protocolVersion);
        termModelUnit.setImei(imei);
        termModelUnit.setFirewareVersion(firewareVersion);
        termModelUnit.setProtocolVersion(protocolVersion);
        termModelUnit.setProduceBatch(produceBatch);
        termModelUnit.setFactoryDate(factoryDate);
        termModelUnitModel=new TermModelUnitModel();
        termModelUnitModel=TermModelUnitModel.fromEntry(termModelUnit);
        return termModelUnitModel;

    }
}
