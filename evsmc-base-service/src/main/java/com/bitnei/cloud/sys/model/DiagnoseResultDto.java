package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DiagnoseResultDto {

    @ColumnHeader(title = "故障规则项id")
    @ApiModelProperty(value = "故障规则项id")
    private String id;

    @ColumnHeader(title = "故障码规则名称")
    @ApiModelProperty(value = "故障码规则名称")
    private String faultName;

    @ColumnHeader(title = "故障诊断结果 0正常，1故障")
    @ApiModelProperty(value = "故障诊断结果 0正常，1故障")
    private String result;
}
