package com.bitnei.cloud.veh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新能源车辆异常数据项报表model
 * @author zhouxianzhou
 * @date 2019/9/17 16:16
 **/
@Data
public class AbnormalDataItemEnergyModel {

    private String name;

    private String sex;

    @ApiModelProperty(value = "VIN码")
    private String vin;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆厂商")
    private String vehUnitName;

    @ApiModelProperty(value = "运营单位名称")
    private String operUnitName;

    @ApiModelProperty(value = "车辆状态异常")
    private String vehStatusCount;

    @ApiModelProperty(value = "充电状态异常")
    private String chargeStateCount;

    @ApiModelProperty(value = "车速异常")
    private String speedCount;

    @ApiModelProperty(value = "累计里程异常")
    private String distanceCount;

    @ApiModelProperty(value = "总电压异常")
    private String batTotalVCount;

    @ApiModelProperty(value = "总电流异常")
    private String batTotalICount;

    @ApiModelProperty(value = "SOC异常")
    private String socCount;

    @ApiModelProperty(value = "驱动电机异常")
    private String driveMotorCount;

    @ApiModelProperty(value = "燃料电池异常")
    private String fuelBatteryCount;

    @ApiModelProperty(value = "发动机数据异常")
    private String engineDataCount;

    @ApiModelProperty(value = "车辆位置数据异常")
    private String positionDataCount;

    @ApiModelProperty(value = "极值数据异常")
    private String limitDataCount;

    @ApiModelProperty(value = "报警数据异常")
    private String alarmDataCount;





}
