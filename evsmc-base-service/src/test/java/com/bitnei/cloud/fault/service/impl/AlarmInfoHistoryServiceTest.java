package com.bitnei.cloud.fault.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.AlarmInfoHistoryMapper;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.model.AlarmInfoHistoryModel;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AlarmInfoHistoryServiceTest {

    @Autowired
    private AlarmInfoHistoryMapper mapper;

    @Autowired
    private AlarmInfoHistoryService service;

    @Disabled
    @Test
    public void fromEntityToModel() {


        final Condition vin = new Condition();
        vin.setName("vin");
        vin.setValue("test");

        final PagerInfo pagerInfo = new PagerInfo();
        pagerInfo.setConditions(ImmutableList.of(
            vin
        ));
        pagerInfo.setSort(ImmutableList.of(

        ));

        final Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);

        // admin
        final String userId = "1";
        final String tableName = "fault_alarm_info_history";
        final String tableAlias = "faih";
        final String authSql = DataAccessKit.getUserAuthSql(userId, tableName, tableAlias);
        params.put(Constants.AUTH_SQL, authSql);

        final PageRowBounds pageRowBounds = new PageRowBounds(0, 10);

        final Page<AlarmInfoHistory> entities = mapper.pagerModel(params, pageRowBounds);

        log.trace("total={}", entities.getTotal());

        final List<AlarmInfoHistoryModel> models = service.fromEntityToModel(entities);

        log.trace("models={}", JSON.toJSONString(models));
    }
}