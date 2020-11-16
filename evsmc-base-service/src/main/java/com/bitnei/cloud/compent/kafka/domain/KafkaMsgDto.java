package com.bitnei.cloud.compent.kafka.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMsgDto {

    @ApiModelProperty("主题")
    private String topic;

    @ApiModelProperty("分区")
    private Integer partition;

    @ApiModelProperty("偏移量")
    private Long offset;

    @ApiModelProperty("消息体")
    private String message;

    @ApiModelProperty("消息发生时间")
    private String msgTime;
}
