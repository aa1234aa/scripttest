package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * Created by Lijiezhou on 2019/3/13.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayreportSummaryModel", description = "里程分布统计Model")
public class MileageMonitorModel extends BaseModel {

    @ApiModelProperty(value = "截止日期")
    private String endTime;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "监控车车辆总数（辆）")
    private String allMonitorVehicle;

    @ApiModelProperty(value = "本月车辆运营数（辆）")
    private String allOperateVehicle;

    @ApiModelProperty(value = "≥3万公里（辆）")
    private String moreThanThree;

    @ApiModelProperty(value = "2~3万公里（辆）")
    private String betweenTwoAndThree;

    @ApiModelProperty(value = "1~2万公里（辆）")
    private String betweenOneAndTwo;

    @ApiModelProperty(value = "0.05~1万公里（辆）")
    private String betweenHalfAndOne;

    @ApiModelProperty(value = "＜0.05万公里（辆）")
    private String lessThanHalf;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

}
