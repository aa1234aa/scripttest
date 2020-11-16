package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DownloadTaskServiceTest {

    @Autowired
    private DownloadTaskService downloadTaskService;

    @Disabled
    @Test
    public void list() {
        final PagerInfo pagerInfo = new PagerInfo();
        final PagerResult result = downloadTaskService.list(pagerInfo);
    }

    @Disabled
    @Test
    public void delete() {
        log.trace("count={}", downloadTaskService.delete("id-1", "id-2"));
        log.trace("clear={}", downloadTaskService.delete());
    }
}