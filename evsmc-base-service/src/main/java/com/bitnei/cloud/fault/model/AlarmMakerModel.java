package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlarmMakerModel extends BaseModel {

    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    @ApiModelProperty(value = "车辆vin")
    private String vin;

    @ApiModelProperty(value = "处理状态  1:未处理, 2:处理中, 3:已处理")
    private Integer procesStatus;

    @ApiModelProperty(value = "故障处理状态")
    @DictName(code = "PROCES_STATUS", joinField = "procesStatus")
    private String procesStatusDisplay;

    @ApiModelProperty(value = "故障类型：1 = 平台, 2 = 故障码")
    private Integer faultType;

    @ApiModelProperty(value = "故障类型")
    @DictName(code = "FAULT_TYPE", joinField = "faultType")
    private String faultTypeDisplay;

    @ApiModelProperty(value = "报警级别 0：无、1：1级、2：2级、3：3级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警级别")
    @DictName(code = "ALARM_LEVEL", joinField = "alarmLevel")
    private String alarmLevelDisplay;

    @ApiModelProperty(value = "报警开始时间")
    private String faultBeginTime;

    @ApiModelProperty(value = "报警结束时间")
    private String faultEndTime;

    @ApiModelProperty(value = "报警持续时间")
    private String faultTime;

    @ApiModelProperty(value = "开始位置")
    private String address;

    @ApiModelProperty(value = "故障经度")
    private Double longitude;

    @ApiModelProperty(value = "故障纬度")
    private Double latitude;

    @ApiModelProperty(value = "围栏类型名称（本期暂未实现）")
    private String fenceTypeName;
}
