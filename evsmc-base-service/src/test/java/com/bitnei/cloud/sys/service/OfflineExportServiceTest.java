package com.bitnei.cloud.sys.service;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.service.impl.AlarmInfoHistoryService;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.dao.OfflineExportMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.google.common.collect.ImmutableList;
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
public class OfflineExportServiceTest {

    @Autowired
    private IOfflineExportService service;

    @Autowired
    private OfflineExportMapper mapper;

    @Disabled
    @Test
    public void createTask() throws InterruptedException {

        final Condition condition1 = new Condition();
        condition1.setName("vin");
        condition1.setValue("LA9G3MBD3JSWXB050");

        final Condition condition2 = new Condition();
        condition2.setName("licensePlate");
        condition2.setValue("浙A05575D");

        final PagerInfo pagerInfo = new PagerInfo();
        pagerInfo.setConditions(
            ImmutableList.of(condition1, condition2)
        );
        pagerInfo.setStart(0);
        pagerInfo.setLimit(10);

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = AlarmInfoHistoryService.class.getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "历史故障信息";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        for (int i=0; i<3; ++i) {

            if (i > 0) {
                // 文件名精确到秒
                Thread.sleep(1000);
            }

            // 创建离线导出任务
            final String entityId = service.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams);

            final OfflineExport entity = new OfflineExport();
            entity.setId(entityId);
            entity.setUpdateBy(ServletUtil.getCurrentUser());
            entity.setUpdateTime(DateUtil.getNow());
            entity.setStateMachine(OfflineExportStateMachine.CANCELED);
            mapper.update(entity);
        }
    }
}