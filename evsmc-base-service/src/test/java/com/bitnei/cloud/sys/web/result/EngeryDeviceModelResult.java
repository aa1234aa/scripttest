package com.bitnei.cloud.sys.web.result;

/*
@author 黄永雄
@create 2019/12/12 17:07
*/

import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EngeryDeviceModelResult extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    public ResultActions getAddResult(String url, EngeryDeviceModel engeryDeviceModel) throws Exception {

        ResultActions   actions = mvc.perform(
                MockMvcRequestBuilders.post(url).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(engeryDeviceModel)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("新增成功"))
                .andDo(print());
        return actions;
    }

    public ResultActions getAllResult(String url, PagerInfo pagerInfo) throws Exception {

        ResultActions  actions = mvc.perform(
                MockMvcRequestBuilders.post(url).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pagerInfo)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pagination.total").isNotEmpty())
                .andDo(print());
        return actions;
    }

    public ResultActions getUpdateResult(String url, String id,EngeryDeviceModel engeryDeviceModel) throws Exception {

        ResultActions   actions = mvc.perform(
                MockMvcRequestBuilders.put(url,id).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(engeryDeviceModel)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("更新成功"))
                .andDo(print());
        return actions;
    }

    public ResultActions getModelByNameResult(String url, PagerInfo pagerInfo) throws Exception {

        ResultActions  actions =mvc.perform(
                MockMvcRequestBuilders.post(url).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pagerInfo)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());
        return actions;
    }

    public ResultActions getDetailByIdResult(String url, String id) throws Exception {

        ResultActions  actions =mvc.perform(
                MockMvcRequestBuilders.get(url,id).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id))
                .andDo(print());
        return actions;
    }

    public ResultActions getDeleteByIdResult(String url, String id) throws Exception {

        ResultActions  actions =mvc.perform(
                MockMvcRequestBuilders.delete(url,id).headers(httpHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("删除成功，共删除了1条记录"))
                .andDo(print());
        return actions;
    }

}
