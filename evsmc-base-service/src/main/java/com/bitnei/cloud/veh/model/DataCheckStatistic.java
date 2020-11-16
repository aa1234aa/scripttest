package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 车辆检测结果统计
 */
@Data
public class DataCheckStatistic {

    @ApiModelProperty(value = "检测车辆数变化趋势")
    private List<DataCheckNumStatistic> numStatistics;

    @ApiModelProperty(value = "各车辆型号中检测结果分布")
    private List<DataCheckVehModelStatistic> vehModelStatistics;

    @ApiModelProperty(value = "检测未通过车辆异常分布")
    private DataCheckErrorDistribution errorDistribution;
}
