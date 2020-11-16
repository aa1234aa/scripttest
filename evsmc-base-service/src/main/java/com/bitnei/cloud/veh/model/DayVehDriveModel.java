package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayVehDriveModel", description = "车辆行驶日报")
public class DayVehDriveModel extends BaseModel {

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "仪表总里程")
    private Float endMileage;

    @ApiModelProperty(value = "当日行驶里程")
    private Float dayMileage;

    @ApiModelProperty(value = "行驶时长")
    private Float minuteLong;

    @ApiModelProperty(value = "当日油耗")
    private Integer oilConsumer;

    @ApiModelProperty(value = "最高车速")
    private Float maxSpeed;

    @ApiModelProperty(value = "平均车速")
    private Float avgSpeed;

    @ApiModelProperty(value = "当日剩余油箱液位")
    private Integer lastOilLocationRemain;

    @ApiModelProperty(value = "剩余反应剂余量")
    private Integer lastReactantRemain;

    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityId;

    @ApiModelProperty(value = "运营单位")
    private String operUnitName;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "录入时间")
    private String createTime;

    @ApiModelProperty(value = "首次上线时间")
    private String firstTimeOnLine;

    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 车辆阶段名称显示**/
    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")
    @ApiModelProperty(value = "车辆阶段名称")
    private String stageName;

}
