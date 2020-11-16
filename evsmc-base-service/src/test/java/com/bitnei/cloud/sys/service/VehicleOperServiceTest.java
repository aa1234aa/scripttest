package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.VehicleDriveDeviceLkModel;
import com.bitnei.cloud.sys.model.VehicleOperModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class VehicleOperServiceTest {

    @Autowired
    private IVehicleOperService vehicleOperService;
    @Autowired
    private IVehicleDriveDeviceLkService vehicleDriveDeviceLkService;

    @Test
    @Rollback(false)
    public void save() {
        VehicleOperModel model = new VehicleOperModel();
        model.setVin("zxz201812192222");
        model.setLicensePlate("粤C77C77");
        model.setLicenseType(1);
        model.setOperLicenseCityId("402882d25c0eeeed015c0f4c87f007de");
        model.setOperUseFor(1);
        model.setOperUseType(1);
        model.setOperVehOwnerId("d130d7234fa24031997eaeac3dd3a227");
        model.setOperInterNo("oper_inter_no_0001");
        model.setOperAreaId("402882d25c0eeeed015c0f4c87f007de");
        model.setOperTime("2018-12-21");
        model.setOperSupportOwnerId("bf3f069cc5f9406387b02e4b5fde2def");
        model.setOperSaveCityId("402882d25c0eeeed015c0f4c87f007de");
        model.setOperSaveAddress("大洲科技园");
        model.setOperChgCityId("402882d25c0eeeed015c0f4c87f007de");
        model.setOperChgAddress("大洲科技园A区1606");
        vehicleOperService.save(model);
        Assert.assertEquals(model.getVin(),"zxz201812192222");
    }

    @Test
    public void list() {
        PagerInfo pagerInfo = new PagerInfo();
        pagerInfo.setStart(0);
        pagerInfo.setLimit(10);
        List<Condition> conditions = Lists.newArrayList();
        Condition c1 = new Condition();
        conditions.add(c1);
        Condition c2 = new Condition();
        conditions.add(c2);
        pagerInfo.setConditions(conditions);
        PagerResult result = (PagerResult) vehicleOperService.list(pagerInfo);
        System.out.println("result.getTotal() : " + result.getTotal());
        List<Object> data = result.getData();
        for (Object obj : data) {
            if(obj instanceof ArrayList) {
                ((ArrayList) obj).forEach(m -> System.out.println(m));
            }
        }
    }

    @Test
    public void listByVehicleId() throws Exception{
        List<VehicleDriveDeviceLkModel> models = vehicleDriveDeviceLkService.listByVehicleId("5eae80283a8b45cea1d162a172f9540f",null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writeValueAsString(models);
        System.out.println(json);
    }


}
