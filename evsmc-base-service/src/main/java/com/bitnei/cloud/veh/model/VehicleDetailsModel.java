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
@ApiModel(value = "VehicleDetailsModel", description = "充电车辆详情")
public class VehicleDetailsModel extends BaseModel {

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ApiModelProperty(value = "车辆厂商id")
    private String manuUnitId;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "销售日期")
    private String sellDate;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 车辆厂商名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "manuUnitId", desc = "")
    private String manuUnitName;

    /** 运营单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "录入时间")
    private String vehicleCreateTime;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "车辆是否已经删除")
    private String isDelete;

    /** 车辆阶段**/
    @DictName(code = "VEHICLE_STAGE_TYPE",joinField = "stage")
    private String stageName;
}
