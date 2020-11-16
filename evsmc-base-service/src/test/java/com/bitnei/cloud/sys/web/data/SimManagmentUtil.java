package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/28 20:12
*/


import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.sys.domain.SimManagement;
import com.bitnei.cloud.sys.model.SimManagementModel;
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
public class SimManagmentUtil {


    /** 运营商   1.联通 2移动  3电信 **/
    private  Integer globalSimType=RandomValue.getNum(1,3);
    /** ICCID **/
    private String iccid=RandomValue.getIccid();
    /** 移动用户号 **/
    private String msisd=RandomValue.getTel();

    private SimManagement simManagement;
    private SimManagementModel simManagementModel;


    public  SimManagementModel createModel(){
        simManagement = new SimManagement();
        simManagement.setGlobalSimType(globalSimType);
        simManagement.setIccid(iccid);
        simManagement.setMsisd(msisd);
        simManagementModel = new SimManagementModel();
        simManagementModel = SimManagementModel.fromEntry(simManagement);
        return simManagementModel;
    }
}
