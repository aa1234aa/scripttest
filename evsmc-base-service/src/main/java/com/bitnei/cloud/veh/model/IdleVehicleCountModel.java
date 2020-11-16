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
 * Created by Lijiezhou on 2019/3/16.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleDetailsModel", description = "闲置车辆详情")
public class IdleVehicleCountModel extends BaseModel {

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "录入时间")
    private String vehicleCreateTime;

    @ApiModelProperty(value = "闲置里程")
    private Double idleMileage;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "最后通讯里程")
    private Double lastEndMileage;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "车辆是否已经删除")
    private String isDelete;


    @ApiModelProperty(value = "长期闲置车辆数")
    private Integer idleVehNum;

    @ApiModelProperty(value = "可监控车辆数")
    private Integer allMonitorVehicle;

    @ApiModelProperty(value = "长期闲置车辆比率（%）")
    private String idleVehRatio;

    @ApiModelProperty(value = "日期")
    private String reportTime;




    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 运营单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 车辆阶段**/
    @DictName(code = "VEHICLE_STAGE_TYPE",joinField = "stage")
    private String stageName;

    /** 用于导出文件显示 **/
    @ApiModelProperty(value = "闲置里程")
    private String idleMileageExport;

    @ApiModelProperty(value = "最后通讯里程")
    private String lastEndMileageExport;
}
