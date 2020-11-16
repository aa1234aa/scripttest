package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProtocolDataDto {

    @ApiModelProperty(value = "报文数据")
    private String data;

    @ApiModelProperty(value = "vin")
    private String vin;
}
