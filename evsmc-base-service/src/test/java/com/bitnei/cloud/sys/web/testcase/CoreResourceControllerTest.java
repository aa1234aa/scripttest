package com.bitnei.cloud.sys.web.testcase;


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.common.constant.Version;
//import com.bitnei.cloud.report.model.Demo1Model;
import com.bitnei.cloud.sys.model.CoreResourceModel;
import com.bitnei.commons.util.UtilHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoreResourceControllerTest extends BaseControllerTest{

    private static final String MODULE_URL_ADD = "/"+Version.VERSION_V1+"/sys/coreResource";

    @Autowired
    private MockMvc mvc;




    @Test
    public void step1_insert()   throws Exception
    {

        String url=MODULE_URL_ADD;
        CoreResourceModel crm=new CoreResourceModel();
        String uuid = UtilHelper.getUUID();
        crm.setId(uuid);
        crm.setName("car");
        crm.setTableName("car_test");
        crm.setNote("This is note");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
        builder.headers(httpHeader());
        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(crm));

        ResultActions actions = mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());



        //crm.set


    }






}
