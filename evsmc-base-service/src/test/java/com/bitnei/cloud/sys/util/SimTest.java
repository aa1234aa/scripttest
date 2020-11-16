package com.bitnei.cloud.sys.util;

/*
@author 黄永雄
@create 2019/11/20 1:03
*/


import com.bitnei.cloud.sys.domain.SimManagement;
import com.bitnei.cloud.sys.model.SimManagementModel;
import com.bitnei.cloud.sys.service.ISimManagementService;
import com.bitnei.cloud.sys.web.data.SimManagmentUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimTest {

    private String simId;
    private SimManagement simManagement;
    private SimManagementModel simManagementModel;

    @Resource
    ISimManagementService iSimManagementService;

    @Resource
    SimManagmentUtil simManagmentUtil;

    @Test
    public void insertVehModel() throws Exception {


        simManagementModel= simManagmentUtil.createModel();
        iSimManagementService.insert(simManagementModel);
        simManagementModel=iSimManagementService.findByIccId(simManagementModel.getIccid());
        String str=simManagementModel.getId();
        System.out.println(str);

    }
}
