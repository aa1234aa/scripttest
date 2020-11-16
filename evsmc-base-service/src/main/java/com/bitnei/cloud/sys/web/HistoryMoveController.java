package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.fault.service.impl.AlarmInfoMoveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Slf4j
@Api(value = "故障报警-历史数据迁移", description = "故障报警-历史数据迁移", tags = {"故障报警-历史数据迁移"})
@RestController
@RequestMapping(value = "/" + Version.VERSION_V1 + "/history/move")
@RequiredArgsConstructor
public class HistoryMoveController {

    private final AlarmInfoMoveService alarmInfoMoveService;

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @ApiOperation(value = "迁移故障报警历史数据" , notes = "迁移故障报警历史数据")
    @PostMapping("/moveFaultInfoHistory")
    public void moveFaultInfoHistory() {

        alarmInfoMoveService.moveHistory();
    }

    @ApiOperation(value = "重置迁移位点" , notes = "重置迁移位点（" +
            "lastTime:上次迁移数据最后的报警开始时间，lastFaultId:上次迁移数据最后的历史报警数据id），不传则从头开始")
    @GetMapping("/resetMovePosition")
    public void resetMovePosition(@RequestParam(required = false) String lastTime,
                                  @RequestParam(required = false) String lastFaultId) {

        if (StringUtils.isNotEmpty(lastTime)) {
            redis.set(5, "MOVE_ALARM_HISTORY_LAST_TIME", lastTime);
        } else {
            redis.set(5, "MOVE_ALARM_HISTORY_LAST_TIME", "0000-00-00 00:00:00");
        }

        if (StringUtils.isNotEmpty(lastFaultId)) {
            redis.set(5, "MOVE_ALARM_HISTORY_LASH_FAULT_ID", lastFaultId);
        } else {
            redis.set(5, "MOVE_ALARM_HISTORY_LASH_FAULT_ID", "");
        }
    }
}
