package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataCheckStatisticParam {

    @ApiModelProperty(value = "统计维度 1:年,2:季度,3:月份")
    private Integer dimension = 1;

    @ApiModelProperty(value = "统计时间范围起")
    private String beginTime;

    @ApiModelProperty(value = "统计时间范围止")
    private String endTime;
}
