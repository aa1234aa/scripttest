//package com.bitnei.cloud.sys.web.data;
//
///*
//@author 黄永雄
//@create 2019/11/30 21:39
//*/
//
//import com.bitnei.cloud.common.BaseControllerTest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.FixMethodOrder;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class ResultUtill extends BaseControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    private ResultActions actions;
//
//
//    public ResultActions getAddResult(String url,Object obj){
//
//        actions = mvc.perform(
//                MockMvcRequestBuilders.post(url).headers(httpHeader())
//                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString()))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("新增成功"))
//                .andDo(print());
//          return actions;
//    }
//}
