package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计车辆检测异常分布
 */
@Data
public class DataCheckErrorDistribution {

    @ApiModelProperty(value = "报文类型异常数量")
    private Integer packetTypeErrorNum;

    @ApiModelProperty(value = "丢包率异常数量")
    private Integer lossCheckErrorNum;

    @ApiModelProperty(value = "数据项异常数量")
    private Integer dataErrorNum;

    @ApiModelProperty(value = "报文类型异常比例")
    private BigDecimal packetTypeErrorRate;

    @ApiModelProperty(value = "丢包率异常比例")
    private BigDecimal lossCheckErrorRate;

    @ApiModelProperty(value = "数据项异常比例")
    private BigDecimal dataErrorRate;

    @ApiModelProperty(value = "有无检测异常，无检测异常时三个比例均为0")
    private Boolean existException;
}
