package com.bitnei.cloud.mock.screen;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.mock.BaseMockTest;
import com.bitnei.cloud.screen.model.PowerMode;
import com.bitnei.cloud.screen.service.ScreenVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author xuzhijie
 */
@Slf4j
@Disabled
@DisplayName("大屏车辆接口测试")
public class ScreenVehicleControllerTest extends BaseMockTest {

    public ScreenVehicleControllerTest(){
        super("/" + Version.VERSION_V1 + "/screen/vehicle");
    }

    @Autowired
    private ScreenVehicleService vehicleService;

    @Test
    @DisplayName("【service】测试查找车型")
    public void findVehiclePowerMode() {
        PowerMode mode = vehicleService.findVehiclePowerMode("c203af08-a26d-415d-a5aa-f7eb81889cb2");
        if (mode != null) {
            log.info(mode.toString());
        }
    }

    @Test
    @DisplayName("【controller】获取全国监控和在线车辆数")
    public void carCount() throws Exception {

        MvcResult result = mockMvc
            .perform(request("/carCount"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        ResultListMsg resultMsg = JSON.parseObject(result.getResponse().getContentAsString(), ResultListMsg.class);
        Assertions.assertEquals(200, resultMsg.getCode().intValue(), "接口返回code不是200");
        log.info("接口返回内容：{}", resultMsg.getData());
    }

}
