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

/**
 * Created by Lijiezhou on 2019/3/13.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayreportSummaryModel", description = "里程分布统计-车辆详情Model")
public class DetailsVehicleModel extends BaseModel {

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "录入时间")
    private String vehicleCreateTime;

    @ApiModelProperty(value = "首次上线时间")
    private String firstRegTime;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "总里程（仪表）km")
    private String lastEndMileage;

    @ApiModelProperty(value = "总在线里程（km）")
    private String totalOnlineMileage;

    @ApiModelProperty(value = "总轨迹里程（km）")
    private String totalGpsMileage;

    @ApiModelProperty(value = "总有效里程（km）")
    private String totalValidMileage;

    @ApiModelProperty(value = "最终核算里程（km）")
    private String totalCheckMileage;

    @ApiModelProperty(value = "车辆是否已经删除")
    private String isDelete;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 车辆阶段**/
    @DictName(code = "VEHICLE_STAGE_TYPE",joinField = "stage")
    private String stageName;
}
