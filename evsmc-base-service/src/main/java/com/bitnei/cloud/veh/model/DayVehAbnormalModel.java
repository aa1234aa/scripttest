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
@ApiModel(value = "DayVehAbnormalModel", description = "车辆异常数据日报")
public class DayVehAbnormalModel extends BaseModel {

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "vid")
    private String vid;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "当日丢包率")
    private String packetLoss;

    @ApiModelProperty(value = "当日异常报文条数")
    private Integer exceptionCodeNum;

    @ApiModelProperty(value = "当日异常报文条数占比")
    private String abnormalRatio;

    @ApiModelProperty(value = "反应剂液位是否过低")
    private Integer reactantLow;

    @ApiModelProperty(value = "油量是否过低")
    private Integer oilLow;

    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private String seriesName;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "汽车厂商")
    private String manuUnitName;

    @ApiModelProperty(value = "运营单位")
    private String operUnitName;

    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityId;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    @DictName(code = "BOOL_TYPE", joinField = "reactantLow")
    @ApiModelProperty(value = "反应剂液位是否过低字典显示")
    private String reactantLowName;

    @DictName(code = "BOOL_TYPE", joinField = "oilLow")
    @ApiModelProperty(value = "油量是否过低字典显示")
    private String oilLowName;

}
