package com.bitnei.cloud.sys.util.mock;


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.common.bean.CoreResource;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.sys.model.CoreResourceModel;
import com.bitnei.cloud.sys.service.ICoreResourceService;
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

//import com.bitnei.cloud.report.model.Demo1Model;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoreResourceControllerMockTest extends BaseControllerTest{

    private static final String MODULE_URL_ADD = "/"+Version.VERSION_V1+"/sys/coreResource";


    @Autowired
    private MockMvc mvc;

    @MockBean
    private ICoreResourceService coreResourceService;

    private CoreResourceModel model;
    private CoreResource encry;

    private String url=MODULE_URL_ADD;
    private String uuid=UtilHelper.getUUID();
    private String id= "UNIT" + getClass().getSimpleName();
    private String name="car";
    private String tableName="car_test";
    private String note="this is a note";

    @Before
    public void before() {
        model=new CoreResourceModel();
        model.setId(uuid);
        model.setName(name);
        model.setTableName(tableName);
        model.setNote(note);
        doNothing().when(coreResourceService).insert(model);

    }

    @Test
    public void step1_insert()   throws Exception {

        ResultActions actions=mvc.perform(
                MockMvcRequestBuilders.post(url).headers(httpHeader())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());


    }

}
