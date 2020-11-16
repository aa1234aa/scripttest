package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 车辆检测结果
 */
@Data
@AllArgsConstructor
public class DataCheckResult {

    @ApiModelProperty(value = "检测总结果")
    private DataCheckRecordModel dataCheckRecordModel;

    @ApiModelProperty(value = "数据项检测明细列表")
    private List<DataItemCheckResultModel> itemCheckResultModels;
}
