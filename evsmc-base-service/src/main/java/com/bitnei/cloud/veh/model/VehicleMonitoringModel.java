package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Lijiezhou on 2019/3/20.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleMonitoringModel", description = "车辆监控情况统计")
public class VehicleMonitoringModel extends BaseModel {

    @ApiModelProperty(value = "日期")
    private String reportDate;

    @ApiModelProperty(value = "车辆录入总数（辆）")
    private Integer allVehicle;

    @ApiModelProperty(value = "日新录入车辆数（辆）")
    private String newVehCount;

    @ApiModelProperty(value = "当天车辆活跃总数")
    private Integer dayActiveCount;

    @ApiModelProperty(value = "全部车辆日活跃总时间")
    private Double allDailyActiveTotalTime;

    @ApiModelProperty(value = "通讯异常车辆数（辆）")
    private Integer notOnlineCount;

    @ApiModelProperty(value = "活跃率（%）")
    private String activeRatio;

    @ApiModelProperty(value = "单车日均活跃时间")
    private Double onceDailyActiveTotalTime;

    @ApiModelProperty(value = "正常监控比例（%）")
    private String monitoringRatio;

    @ApiModelProperty(value = "里程异常数（辆）")
    private Integer abnormalMileageCount;

    @ApiModelProperty(value = "里程异常比例(%)")
    private String abnormalMileageRatio;

    @ApiModelProperty(value = "核查数据总条数")
    private String checkDataTotalNum;
}
