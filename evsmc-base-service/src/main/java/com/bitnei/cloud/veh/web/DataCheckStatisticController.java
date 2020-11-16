package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.veh.model.DataCheckRecordModel;
import com.bitnei.cloud.veh.model.DataCheckStatistic;
import com.bitnei.cloud.veh.model.DataCheckStatisticParam;
import com.bitnei.cloud.veh.service.IDataCheckRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(value = "车辆检测统计结果", description = "车辆检测统计结果", tags = {"车辆检测统计结果"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/veh")
@RequiredArgsConstructor
public class DataCheckStatisticController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "DATACHECKSTATISTIC";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";

    private final IDataCheckRecordService dataCheckRecordService;

    @ApiOperation(value = "获取统计结果", notes = "获取统计结果：请求参数：dimension：统计维度 1:年,2:季度,3:月份，" +
            "beginTime：统计时间范围起， endTime：统计时间范围止")
    @PostMapping(value = "/dataCheckStatistic/statistic")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg statistic(@RequestBody DataCheckStatisticParam param) {

        DataCheckStatistic dataCheckStatistic = dataCheckRecordService.statistic(param);
        return ResultMsg.getResult(dataCheckStatistic);
    }
}
