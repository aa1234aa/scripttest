package com.bitnei.cloud.fault.dao;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.model.AlarmInfoHistoryModel;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AlarmInfoHistoryMapperTest {

    @Autowired
    private AlarmInfoHistoryMapper mapper;

    @Disabled
    @Test
    public void deleteByEntity() throws InvocationTargetException, IllegalAccessException {

        final PagerInfo pagerInfo = new PagerInfo();
        pagerInfo.setConditions(ImmutableList.of(

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

        final Page<AlarmInfoHistory> entities = mapper.pagerModel(params, new PageRowBounds(0, 4096));

        for (final AlarmInfoHistory entity : entities) {

            final AlarmInfoHistoryModel model = new AlarmInfoHistoryModel();

            BeanUtils.copyProperties(entity, model);
            org.apache.commons.beanutils.BeanUtils.copyProperties(model, entity.getTails());

            model.setDuration(
                duration(
                    model.getFaultBeginTime(),
                    model.getFaultEndTime()
                )
            );

            //车辆是否删除
            if (entity.getVehDelete() == 1) {
                model.setVin(entity.getVin() + "(车辆已删除)");
            }

            log.trace(JSON.toJSONString(model));
        }
    }

    private String duration(String beginTime, String endTime) {
        String duration = "";
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            duration = DateUtil.duration(beginTime, endTime);
        }
        return duration;
    }

    @Disabled
    @Test
    public void pagerModel() {


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

        // limit 为 0 时, 表示不分页
        final PageRowBounds pageRowBoundsLimitZero = new PageRowBounds(0, 0);

        final Page<AlarmInfoHistory> limitZeroEntities = mapper.pagerModel(params, pageRowBoundsLimitZero);
        if (limitZeroEntities.getTotal() > 0) {
            Assertions.assertTrue(0 < limitZeroEntities.getResult().size());
        }

        // limit 为 1 时, 表示每页最多 1 条记录
        final PageRowBounds pageRowBoundsLimitOne = new PageRowBounds(0, 1);

        final Page<AlarmInfoHistory> limitOneEntities = mapper.pagerModel(params, pageRowBoundsLimitOne);
        if (limitOneEntities.getTotal() > 1) {
            Assertions.assertEquals(1, limitOneEntities.getResult().size());
        }
    }

    @Disabled
    @Test
    public void count() {


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

        log.trace("count={}, PageRowBounds", PageHelper.count(() -> mapper.pagerModel(params, new PageRowBounds(0, 0))));
    }
}