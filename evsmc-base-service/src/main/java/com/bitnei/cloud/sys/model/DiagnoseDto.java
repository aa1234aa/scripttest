package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DiagnoseDto {

    @ApiModelProperty(value = "诊断车辆vin")
    @NotEmpty(message = "诊断车辆vin不能为空")
    private String vin;

    @ApiModelProperty(value = "诊断时间起（只有历史诊断要传该值）")
    private String beginTime;

    @ApiModelProperty(value = "诊断时间止（只有历史诊断要传该值）")
    private String endTime;
}
