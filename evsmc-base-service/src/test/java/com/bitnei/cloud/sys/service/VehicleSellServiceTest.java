package com.bitnei.cloud.sys.service;


import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.VehicleSellModel;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class VehicleSellServiceTest {

    @Autowired
    private IVehicleSellService vehicleSellService;

    @Test
    @Rollback(false)
    public void save() {
        VehicleSellModel model = new VehicleSellModel();
        model.setVin("zxz201812192222");
        model.setSellDate("2018-12-20");
        model.setSellPrice(11.6);
        model.setSellForField(1);
        model.setSellPriVehOwnerId("ab9be7309a2f4c64ac926a0e967471db");
        model.setSellPubUnitId("d53110140a5d4474bb2f604a9e0898e8");
        model.setSellCityId("402882d25c0eeeed015c0f4c87f007de");
        model.setSell4sUnitId("d53110140a5d4474bb2f604a9e0898e8");
        model.setSellSellerId("bf3f069cc5f9406387b02e4b5fde2def");
        model.setSellSecureDate("2018-12-20");
        model.setSellFirstCheckDate("2018-12-20");
        model.setSellInvoiceNo("fapiao-00001");
        model.setSellInvoiceDate("2018-12-20");
        model.setSellInvoiceImgId("1");
        model.setSellLicenseNo("xingshi-0001");
        model.setSellLicenseRegDate("2018-12-20");
        model.setSellLicenseGrantDate("2018-12-20");
        model.setSellLicenseImgId("1");
        vehicleSellService.save(model);
        System.out.println("vehicle sell save success ! ! !");
    }

    @Test
    public void list() {
        PagerInfo pagerInfo = new PagerInfo();
        pagerInfo.setStart(0);
        pagerInfo.setLimit(10);
        List<Condition> conditions = Lists.newArrayList();
        Condition c1 = new Condition();
        conditions.add(c1);
        pagerInfo.setConditions(conditions);
        PagerResult result = (PagerResult) vehicleSellService.list(pagerInfo);
        System.out.println("result.getTotal() : " + result.getTotal());
        List<Object> data = result.getData();
        for (Object obj : data) {
            if(obj instanceof ArrayList) {
                ((ArrayList) obj).forEach(m -> System.out.println(m));
            }
        }

    }

}
