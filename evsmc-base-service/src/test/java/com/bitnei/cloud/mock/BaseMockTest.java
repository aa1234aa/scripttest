package com.bitnei.cloud.mock;

import com.bitnei.cloud.Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mock 基类
 * 整个junit测试过程，全局只会启动一个容器，然后再进行所有单元测试，容器启动过程稍慢，需要等待
 *
 * @author xuzhijie
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
@Transactional
public abstract class BaseMockTest {

    @Autowired
    protected MockMvc mockMvc;

    private String prefixUrl;

    public BaseMockTest(String prefixUrl){
        this.prefixUrl = prefixUrl;
    }

    protected MockHttpServletRequestBuilder request(String url){
        return MockMvcRequestBuilders.get(prefixUrl + "/carCount");
    }

}
