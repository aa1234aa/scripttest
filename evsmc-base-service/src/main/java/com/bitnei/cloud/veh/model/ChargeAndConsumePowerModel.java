package com.bitnei.cloud.veh.model;

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
@ApiModel(value = "ChargeAndConsumePowerModel", description = "充耗电情况统计")
public class ChargeAndConsumePowerModel extends BaseModel {

    @ApiModelProperty(value = "报表日期 年月日 ")
    private String reportTime;

    @ApiModelProperty(value = "充电车辆总数")
    private Double chargeVehCount;

    @ApiModelProperty(value = "充电总次数")
    private Double chargeCount;

    @ApiModelProperty(value = "充电总时长（h）")
    private Double chargeTimeCount;

    @ApiModelProperty(value = "单车平均充电次数")
    private Double chargeAvg;

    @ApiModelProperty(value = "单次充电平均时长")
    private Double chargeTimeOnceAvg;

    @ApiModelProperty(value = "最长充电时间(h)")
    private Double longChargeTimeMax;

    @ApiModelProperty(value = "累计耗电量")
    private Double allChargeConsume;

    @ApiModelProperty(value = "平均单车耗电量")
    private Double consumePowerOnceAvg;


    //以下暂时为String类型并且值为空

    @ApiModelProperty(value = "累计充电量")
    private String allChargePower;


    @ApiModelProperty(value = "充电总时长（h）")
    private String chargeTimeCountExport;

    @ApiModelProperty(value = "单车平均充电次数")
    private String chargeAvgExport;

    @ApiModelProperty(value = "累计耗电量")
    private String allChargeConsumeExport;

    @ApiModelProperty(value = "平均单车耗电量")
    private String consumePowerOnceAvgExport;

    @ApiModelProperty(value = "单次充电平均时长")
    private String chargeTimeOnceAvgExport;

    /** 快慢充 **/
    @ApiModelProperty(value = "快充次数")
    private Double fastChargeNumber;

    @ApiModelProperty(value = "慢充次数")
    private Double trickleChargeNumber;

    @ApiModelProperty(value = "快充时长(h)")
    private Double fastChargeTime;

    @ApiModelProperty(value = "慢充时长(h)")
    private Double trickleChargeTime;

    @ApiModelProperty(value = "单车平均快充次数")
    private Double fastChargeNumberAvg;

    @ApiModelProperty(value = "单车平均慢充次数")
    private Double trickleChargeNumberAvg;

    @ApiModelProperty(value = "单车平均快充时长")
    private Double fastChargeTimeAvg;

    @ApiModelProperty(value = "单车平均慢充时长")
    private Double trickleChargeTimeAvg;

}