package com.bitnei.cloud.compent.kafka.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KafkaSearchDto {

    @ApiModelProperty(value = "主题")
    private String topic;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "正则匹配")
    private String regex;
}
