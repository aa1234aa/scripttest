package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 按车型统计结果
 */
@Data
public class DataCheckVehModelStatistic {

    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ApiModelProperty(value = "检测未通过数")
    private Integer errorNum;

    @ApiModelProperty(value = "检测已通过数")
    private Integer passNum;
}
