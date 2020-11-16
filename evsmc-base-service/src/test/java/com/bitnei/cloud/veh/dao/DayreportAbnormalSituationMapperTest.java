package com.bitnei.cloud.veh.dao;

import com.bitnei.cloud.veh.domain.DayreportAbnormalSituation;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableMap;
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
public class DayreportAbnormalSituationMapperTest {

    @Autowired
    private DayreportAbnormalSituationMapper mapper;

    @Disabled
    @Test
    public void pagerModel() {

        final Page<DayreportAbnormalSituation> entities_1 = mapper.pagerModel(
            ImmutableMap.of(),
            new PageRowBounds(0, 0)
        );

        final Page<DayreportAbnormalSituation> entities_2 = mapper.pagerModel(
            ImmutableMap.of(
                "startDate", "2019-05-08"
            ),
            new PageRowBounds(0, 0)
        );

        final Page<DayreportAbnormalSituation> entities_3 = mapper.pagerModel(
            ImmutableMap.of(
                "endDate", "2019-05-10"
            ),
            new PageRowBounds(0, 0)
        );

        final Page<DayreportAbnormalSituation> entities_4 = mapper.pagerModel(
            ImmutableMap.of(
                "startDate", "2019-05-08",
                "endDate", "2019-05-10"
            ),
            new PageRowBounds(0, 0)
        );

        final Page<DayreportAbnormalSituation> entities_5 = mapper.pagerModel(
            ImmutableMap.of(
                "vin", "TBF"
            ),
            new PageRowBounds(0, 0)
        );
    }
}