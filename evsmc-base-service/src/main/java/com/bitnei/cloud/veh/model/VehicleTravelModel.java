package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Lijiezhou on 2019/3/18.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleDetailsModel", description = "车辆行驶情况统计")
public class VehicleTravelModel extends BaseModel {

    @ApiModelProperty(value = "日期")
    private String reportDate;

    @ApiModelProperty(value = "日总行驶时间(h)")
    private Double dayTravelTime;

    @ApiModelProperty(value = "日总行驶里程")
    private Double dayRunKm;

    @ApiModelProperty(value = "日活跃车辆")
    private Integer vehOnlineCount;

    @ApiModelProperty(value = "单次充电最大里程(Km)")
    private Double onceChargeMaxMileage;



    @ApiModelProperty(value = "单车平均行驶时间")
    private Double onceRunTimeAvg;

    @ApiModelProperty(value = "单车平均行驶里程")
    private Double onceRunKmAvg;

    @ApiModelProperty(value = "日平均车速（km/h）")
    private Double daySpeedAvg;

    @ApiModelProperty(value = "单车平均行驶时间")
    private String onceRunTimeAvgExport;

    @ApiModelProperty(value = "单车平均行驶里程")
    private String onceRunKmAvgExport;

    @ApiModelProperty(value = "日平均车速（km/h）")
    private String daySpeedAvgExport;

    @ApiModelProperty(value = "日总行驶时间(h)")
    private String dayTravelTimeExport;
}
