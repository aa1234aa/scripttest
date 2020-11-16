package com.bitnei.cloud.sys.dao;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.PageRowBoundsUtil;
import com.bitnei.cloud.orm.bean.Sort;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class OfflineExportMapperTest {

    @Autowired
    private OfflineExportMapper offlineExportMapper;

    @Autowired
    private Environment env;

    @Disabled
    @Test
    public void deleteByEntity() {

        final OfflineExport id = new OfflineExport();
        id.setId("f981ef240b4840f890f1f5e2a5364bb7");
        offlineExportMapper.delete(id);

        final OfflineExport createBy = new OfflineExport();
        createBy.setCreateBy("delete");
        offlineExportMapper.delete(createBy);
    }

    @Disabled
    @Test
    public void deleteByMap() {
        offlineExportMapper.delete(ImmutableMap.of(
            "id", "f98854fd1c4b40caa946e132dde46007"
        ));
        offlineExportMapper.delete(ImmutableMap.of(
            "createBy", "delete"
        ));
    }

    @Disabled
    @Test
    public void updateByEntity() {

        final OfflineExport entity = new OfflineExport();
        entity.setId("0981ef240b4840f890f1f5e2a5364bb7");
        entity.setExportNote("单元测试1->" + DateUtil.getNow());
        offlineExportMapper.update(entity);
    }

    @Disabled
    @Test
    public void updateByMap() {
        offlineExportMapper.update(ImmutableMap.of(
            "id", "098854fd1c4b40caa946e132dde46007",
            "exportNote", "单元测试2->" + DateUtil.getNow()
        ));
    }

    @Disabled
    @Test
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void selectByEntity() throws IOException {

        final OfflineExport entity = new OfflineExport();
        entity.setStateMachine(3);
        entity.setCreateBy("admin");

        @Cleanup final Cursor<OfflineExport> result = offlineExportMapper.select(entity);
        for (final OfflineExport offlineExport : result) {
            // do something...
        }
    }

    @Disabled
    @Test
    public void selectByMap() {

        final Sort sort1 = new Sort();
        sort1.setName(OfflineExport.Fields.createTime);
        sort1.setOrder("asc");

        final Sort sort2 = new Sort();
        sort2.setName(OfflineExport.Fields.createBy);
        sort2.setOrder("desc");

        final Sort sort3 = new Sort();
        sort3.setName(OfflineExport.Fields.updateTime);
        sort3.setOrder("ignore");

        offlineExportMapper.select(
            ImmutableMap.<String, Object>of(
                OfflineExport.Fields.stateMachine, 4,
                OfflineExport.Fields.createBy, "admin",
                OfflineExport.Fields.sorts, ImmutableList.of(sort1, sort2, sort3)
            ),
            (@NotNull final ResultContext<? extends OfflineExport> context) -> {
                // do something...
            }
        );

    }

    @Disabled
    @Test
    public void selectByPageRowBounds() throws IOException {

        final OfflineExport entity = new OfflineExport();

        final int pageNum = 3;
        final int pageSize = 2;

        final PageRowBounds pageRowBounds = PageRowBoundsUtil.create(pageNum, pageSize);


//        final PagerInfo pagerInfo = new PagerInfo();
//        pagerInfo.setStart(4);
//        pagerInfo.setLimit(2);
//
//        final PageRowBounds pageRowBounds = PageRowBoundsUtil.fromPagerInfo(pagerInfo);

        final Page<OfflineExport> page = offlineExportMapper.select(entity, pageRowBounds);
        log.trace("total={}", pageRowBounds.getTotal());

        final List<OfflineExport> result = page.getResult();
        for (final OfflineExport offlineExport : result) {
            // do something...
        }
        log.trace("PageSize={}", page.getPageSize());
        log.trace("getPageNum={}", page.getPageNum());
        log.trace("getPages={}", page.getPages());
        log.trace("Total={}", page.getTotal());
        log.trace("StartRow={}", page.getStartRow());
        log.trace("EndRow={}", page.getEndRow());
    }

    @Disabled
    @Test
    public void selectSharding() {

        // 分片总数
        final int shardingTotalCount = 12;
        // 分配于本作业实例的分片项
        final int shardingItem = 6;

        offlineExportMapper.selectSharding(
            shardingTotalCount,
            shardingItem,
            (final ResultContext<? extends OfflineExport> context) -> {
                final OfflineExport export = context.getResultObject();
                log.trace("{}", JSON.toJSONString(export));
            }, env.getProperty("spring.application.name", "evsmc-base-service")
        );
    }

    @Disabled
    @Test
    public void findByIds() {

        final List<OfflineExport> tasks = offlineExportMapper.findByIds(
            ImmutableSet.of(
                "a8bc5300132f4325ab4e32880c133a98",
                "a8bc5300132f4325ab4e32880c133a99"
            )
        );
        for (OfflineExport task : tasks) {
            log.trace(JSON.toJSONString(task));
        }
    }

    @Disabled
    @Test
    public void retryExceptionalTask() {

        offlineExportMapper.retryExceptionalTask(
            ImmutableSet.of(
                "a8bc5300132f4325ab4e32880c133a98",
                "a8bc5300132f4325ab4e32880c133a99"
            )
        );
    }

    @Disabled
    @Test
    public void cancelExportingTask() {

        offlineExportMapper.cancelExportingTask(
            ImmutableSet.of(
                "a8bc5300132f4325ab4e32880c133a98",
                "a8bc5300132f4325ab4e32880c133a99"
            )
        );
    }
}