package com.bitnei.cloud.sys.util.mock;

/*
@author 黄永雄
@create 2019/11/8 17:05
*/

import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.commons.util.UtilHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OwnerPeopleControllerMockTest extends BaseControllerTest {

    private static final String MODULE_URL_ADD = "/"+ Version.VERSION_V1+"/sys/ownerPeople";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IOwnerPeopleService ownerPeopleService;

    private OwnerPeopleModel model;
    private OwnerPeople entry;

    String add_url=MODULE_URL_ADD;
    private String uuid= UtilHelper.getUUID();
    private String ownerName="黄大熊";
    private String unitId="5847b30fa1854fd694790d4ed017b61f";
    private String unitName="高尔夫汽车售后服务有限公司";
    private String jobPost="销售员";
    private String jobNumber="7788661";
    private Integer sex=1;
    private String telPhone="15992667873";
    private String address="广东省珠海市";
    private String email="13532440708@qq.com";
    private Integer cardType=1;
    private String cardNo="445302197209262640";
    private String cardAddress="珠海香洲区48948785";
    private String frontCardImgId="2d2a1fef19304ce2a158420152fa9216";
    private String backCardImgId="2d2a1fef19304ce2a158420152fa9216";
    private String faceCardImgId="2d2a1fef19304ce2a158420152fa9216";
//    private String createTime;
//    private String createBy;
//    private String updateTime;
//    private String updateBy;

    @Before
    public void before() {
        model=new OwnerPeopleModel();
        model.setId(uuid);
        model.setOwnerName(ownerName);
        model.setUnitId(unitId);
        model.setUnitName(unitName);
        model.setJobPost(jobPost);
        model.setJobNumber(jobNumber);
        model.setSex(sex);
        model.setTelPhone(telPhone);
        model.setAddress(address);
        model.setEmail(email);
        model.setCardType(cardType);
        model.setCardNo(cardNo);
        model.setCardAddress(cardAddress);
        model.setFrontCardImgId(frontCardImgId);
        model.setBackCardImgId(backCardImgId);
        model.setFaceCardImgId(faceCardImgId);
        doNothing().when(ownerPeopleService).insert(model);

    }

    @Test
    public void owner_insert() throws Exception {

        ResultActions actions=mvc.perform(
                MockMvcRequestBuilders.post(add_url).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());
    }

}
