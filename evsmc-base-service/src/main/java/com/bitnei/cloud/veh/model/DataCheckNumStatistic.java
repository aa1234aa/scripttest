package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计车辆检测数量
 */
@Data
public class DataCheckNumStatistic {

    @ApiModelProperty(value = "统计横坐标点")
    private String statisticItem;

    @ApiModelProperty(value = "检测车辆总数")
    private Integer checkNum = 0;

    @ApiModelProperty(value = "检测未通过数")
    private Integer errorNum = 0;

    @ApiModelProperty(value = "检测已通过数")
    private Integer passNum = 0;
}
