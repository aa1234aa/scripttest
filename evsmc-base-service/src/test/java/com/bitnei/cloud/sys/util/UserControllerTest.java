//package com.bitnei.cloud.sys.web;
//
///*
//@author 黄永雄
//@create 2019/11/7 19:02
//*/
//
//
//import com.alibaba.fastjson.JSON;
//import com.bitnei.cloud.sys.domain.User;
//import com.bitnei.cloud.sys.model.UserModel;
//import com.bitnei.cloud.sys.service.IDeptService;
//import com.bitnei.cloud.sys.service.IUserService;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Before;
//import org.junit.SimTest;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Date;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
////@WebMvcTest
//@SpringBootTest
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @MockBean
//    private IUserService userService;
////    @MockBean(name="articleMybatisRestService")
////    private IDeptService deptService;
//
//    private UserModel model;
//    private User entry;
//
//    private String username="huangdaxiong";
//    private String password="123456";
//    private String ownerId="07468bfff62a44b185f5e11643a55dff";
//    private String permitStartDate="2019-11-01";
//    private String permitEndDate="2019-11-17";
//    private Integer isNeverExpire=0;
//    private Integer isValid=0;
//    private String defRoleId="a5ddb0f0b1c34902aba68fc202898605";
//    private Integer openApp=0
//    private Integer openWx=0
//    private Integer isAllPermissions=0
//    private String createTime="2019-08-07 11:22:40";
//    private String createBy="admin";
//    private String updateTime="2019-09-16 17:17:11";
//    private String updateBy="admin";
////    private String deptIds;
////    private String deptNames;
////    private String groupIds;
////    private String groupNames;
////    private String roleIds;
////    private String roleNames;
//
//    @Before
//    public void before() {
//        entry.setUsername(username);
//        entry.setPassword(password);
//        entry.setOwnerId(ownerId);
//        entry.setPermitEndDate(permitEndDate);
//        entry.setPermitStartDate(permitStartDate);
//        entry.setIsValid(isValid);
//        entry.setIsNeverExpire(isNeverExpire);
//        entry.setIsValid(isValid);
//        entry.setDefRoleId(defRoleId);
//        entry.setOpenApp(openApp);
//        entry.setOpenWx(openWx);
//        entry.setCreateTime(createTime);
//        entry.setCreateBy(createBy);
//        entry.setIsAllPermissions(isAllPermissions);
//        entry.setUpdateTime(updateTime);
//        entry.setUpdateBy(updateBy);
//
//        Mockito.when(userService.insert(entry)).thenReturn(0);
////        Mockito.when(articleRestService.getAll()).thenReturn(Lists.newArrayList(articleVO));
////        Mockito.when(articleRestService.getArticle(id)).thenReturn(articleVO);
////        Mockito.when(articleRestService.deleteArticle(id)).thenReturn(id);
////        Mockito.when(articleRestService.updateArticle(articleVO)).thenReturn(id);
//////        Mockito.when(articleRestService.getArticleByAuthor(author)).thenReturn(articleVO);
//
//
//    }
//
//    @SimTest
//    public void addUserModel() throws Exception {
//
//
//        MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.post("/rest/article").contentType("application/json").content(JSON.toJSONString(entry)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.data.author").value("huangdada"))
//                .andDo(print())
//                .andReturn();
//
//        log.info(result.getResponse().getContentAsString());
//
//    }
//
//
//
////    @SimTest
////    public void deleteArticle() throws Exception {
////
////
////
////        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/article/{id}",id).contentType("application/json"))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(print())
////                .andReturn();
////        log.info(result.getResponse().getContentAsString());
////
////    }
////
////    @SimTest
////    public void getArticle() throws Exception {
////
////
////        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/article/{id}",id).contentType("application/json"))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.data.author").value("huangdada"))
////                .andDo(print())
////                .andReturn();
////        log.info(result.getResponse().getContentAsString());
////
////    }
////
////    @SimTest
////    public void updateArticle() throws Exception {
////
////
////        MvcResult result = mockMvc.perform(
////                MockMvcRequestBuilders.put("/rest/article/{id}",id).contentType("application/json").content(JSON.toJSONString(articleVO)))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.data.author").value("huangdada"))
//////              .andExpect(MockMvcResultMatchers.jsonPath("$.data.reader[0].age").value(35))
////                .andDo(print())
////                .andReturn();
////
////        log.info(result.getResponse().getContentAsString());
////
////    }
//}
