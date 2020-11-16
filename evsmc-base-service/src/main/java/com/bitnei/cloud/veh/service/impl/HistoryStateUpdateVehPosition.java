package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.system.interceptor.YearMonthStrategy;
import com.bitnei.cloud.veh.dao.HistoryStateMapper;
import com.bitnei.cloud.veh.model.HistoryStateModel;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @version 1.0
* @author hzr
* @since JDK1.8
*/

@Slf4j
@Configuration
@EnableAsync
public class HistoryStateUpdateVehPosition {

    @Autowired
    private HistoryStateMapper historyStateMapper;
    @Autowired
    private YearMonthStrategy yearMonthStrategy;

    @Async
    public void updateVehPositionAsync(@NotNull final List<HistoryStateModel> updateVehPositionList) {

        final ImmutableSet<String> shardingTables = yearMonthStrategy.getShardingTables("veh_history_state");

        final List<HistoryStateModel> list = new ArrayList<>();

        for (final HistoryStateModel hs : updateVehPositionList) {

            final String shardingTable = YearMonthStrategy.getShardingTableAndIdx("veh_history_state", hs.getReportDate());

            if (shardingTables.contains(shardingTable)) {
                if (StringUtils.isNotBlank(hs.getVehPosition())) {
                    hs.setTableName(shardingTable);
                    list.add(hs);
                }
            } else {
                log.info(String.format("该车辆历史状态记录找不到分表，VIN：%s ; 日期：%s ", hs.getVin(), hs.getReportDate()));
            }
        }

        log.trace("更新车辆地址开始-------------------------------");
        if (list.size() > 0) {

            final Map<String, List<HistoryStateModel>> tables = list.stream().collect(Collectors.groupingBy(hs -> hs.getTableName()));

            historyStateMapper.updateVehPositionInManyShardingTables(tables);
        }
        log.trace("更新车辆地址结束-------------------------------");
    }

}