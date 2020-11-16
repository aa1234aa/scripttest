package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.Exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class UnitServiceTest {

    @Autowired
    private IUnitService unitService;


    @Test
    public void validateUnitType() {
        try {
            String id = unitService.validateNameCode("海南乐东沿海公交汽车有限公司", "3000");
            System.out.println("id:" + id);
        } catch (BusinessException e) {
           log.error("error", e);
            System.out.println("e.getMessage():" + e.getMessage() + "code===" + e.getCode());
        }

    }




}
