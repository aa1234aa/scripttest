//package com.bitnei.cloud.sys.web;
//
//import com.bitnei.cloud.common.BaseControllerTest;
//import com.bitnei.cloud.common.constant.Version;
//import com.bitnei.cloud.orm.bean.PagerInfo;
//import com.bitnei.cloud.report.model.Demo1Model;
//import com.bitnei.cloud.report.web.Demo1Controller;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.FixMethodOrder;
//import org.junit.SimTest;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * <p>
// * ----------------------------------------------------------------------------- <br>
// * 工程名 ： evsmc-module <br>
// * 功能： 请完善功能说明 <br>
// * 描述： 这个人很懒，什么都没有留下 <br>
// * 授权 : (C) Copyright (c) 2018 <br>
// * 公司 : 北京理工新源信息技术有限公司<br>
// * ----------------------------------------------------------------------------- <br>
// * 修改历史 <br>
// * <table width="432" border="1">
// * <tr>
// * <td>版本</td>
// * <td>时间</td>
// * <td>作者</td>
// * <td>改变</td>
// * </tr>
// * <tr>
// * <td>1.0</td>
// * <td>2018-${MOTH}-17</td>
// * <td>chenpeng</td>
// * <td>创建</td>
// * </tr>
// * </table>
// * <br>
// * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
// *
// * @author chenpeng
// * @version 1.0
// * @since JDK1.8
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class Demo1ControllerTest extends BaseControllerTest {
//
//
//    private static final String MODULE_URL = "/" + Version.VERSION_V1 + "/report/demo1s";
//    private static final String MODULE_URL_ADD = "/" + Version.VERSION_V1 + "/report/demo1";
//
//    /**
//     * 默认id
//     **/
//    private String id;
//
//    @Autowired
//    private MockMvc mvc;
//
//    public Demo1ControllerTest() {
//        id = "UNIT" + getClass().getSimpleName();
//    }
//
//    /**
//     * 新增
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step01_add() throws Exception {
//        //请求路径
//        String url = MODULE_URL_ADD;
//        //设置domain
//        Demo1Model model = new Demo1Model();
//        model.setId(id);
//        model.setDictField(1);
//        model.setName("test");
//        model.setNameField("test");
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(model));
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//
//    /**
//     * 编辑
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step02_update() throws Exception {
//        //请求路径
//        String url = MODULE_URL + "/" + id;
//        //设置domain
//        Demo1Model model = new Demo1Model();
//        model.setId(id);
//        model.setName("name_test");
//        model.setDictField(2);
//        model.setNameField("field_name");
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(model));
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//
//    /**
//     * 查询单个实体
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step03_get() throws Exception {
//        //请求路径
//        String url = MODULE_URL + "/" + id;
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON);
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//    /**
//     * 查询所有
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step04_list() throws Exception {
//        //请求路径
//        String url = MODULE_URL;
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON);
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//
//    /**
//     * 分页查询
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step05_pageQuery() throws Exception {
//        //请求路径
//        String url = MODULE_URL + "/pageQuery";
//        PagerInfo pagerInfo = new PagerInfo();
//        pagerInfo.setStart(0);
//        pagerInfo.setLimit(10);
////        //查询条件
////        pagerInfo.().put("dictField","2");
////        pagerInfo.getParams().put("nameField","field_name");
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pagerInfo));
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//
//    /**
//     * 导出
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step06_export() throws Exception {
//        //请求路径
//        String url = MODULE_URL + "/export";
//        PagerInfo pagerInfo = new PagerInfo();
//        pagerInfo.setStart(0);
//        pagerInfo.setLimit(10);
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pagerInfo));
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andDo(print());
//
//    }
//
//
//    /**
//     * 删除
//     *
//     * @throws Exception
//     * @see Demo1Controller
//     */
//    @SimTest
//    public void step07_delete() throws Exception {
//        //请求路径
//        String url = MODULE_URL + "/" + id;
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url);
//        builder.headers(httpHeader());
//        builder.contentType(MediaType.APPLICATION_JSON);
//
//        ResultActions actions = mvc.perform(builder)
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
//
//
//}
