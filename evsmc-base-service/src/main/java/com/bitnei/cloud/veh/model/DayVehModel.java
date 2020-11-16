package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.veh.domain.DayVeh;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayVeh新增模型<br>
* 描述： DayVeh新增模型<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2019-03-09 11:23:08</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayVehModel", description = "单车日报表Model")
public class DayVehModel extends BaseModel {

    @ColumnHeader(title = "报表日期")
    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ColumnHeader(title = "VIN")
    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆公告型号")
    private String noticeId;
    /** 车辆公告型号名称显示**/
    /*@LinkName(table = "sys_veh_notice", column = "name", joinField = "noticeId",desc = "")*/
    @ApiModelProperty(value = "车辆公告型号名称")
    private String noticeDisplay;

    @ApiModelProperty(value = "车辆厂商")
    private String manuUnitId;
    /** 车辆厂商名称显示**/
    /*@LinkName(table = "sys_unit", column = "name", joinField = "manuUnitId",desc = "")*/
    @ApiModelProperty(value = "车辆厂商名称")
    private String manuUnitDisplay;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;
    /** 车运营单位名称显示**/
    /*@LinkName(table = "sys_unit", column = "name", joinField = "operUnitId",desc = "")*/
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitDisplay;

    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityId;
    /** 上牌城市名称显示**/
    /*@LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId",desc = "")*/
    @ApiModelProperty(value = "上牌城市名称")
    private String operLicenseCityDisplay;

    @ApiModelProperty(value = "激活时间")
    private String firstRegTime;
    @ApiModelProperty(value = "销售日期")
    private String sellDate;

    @ApiModelProperty(value = "日活跃总时间(h)")
    private Double dailyActiveTotalTime;
    @ApiModelProperty(value = "日总行驶时间(h)")
    private Double runTimeSum;
    @ApiModelProperty(value = "日行驶次数")
    private Double runTimes;
    @ApiModelProperty(value = "日总行驶里程(Km)")
    private Double runKm;
    @ApiModelProperty(value = "单次运行最大里程(Km)")
    private Double runKmMax;
    @ApiModelProperty(value = "总里程")
    private Double lastEndMileage;
    @ApiModelProperty(value = "动力电池累积能量")
    private Double chargeConsume;
    @ApiModelProperty(value = "实际百公里耗电量")
    private Double chargeCon100km;
    @ApiModelProperty(value = "百公里额定耗电量")
    private Double ratedChargeCon100km;
    @ApiModelProperty(value = "单次充电后最大耗电量(Kw/h)")
    private Double chargeSconsumeMax;
    @ApiModelProperty(value = "充电总次数")
    private Double chargeTimes;
    @ApiModelProperty(value = "快充次数")
    private Double fastTimes;
    @ApiModelProperty(value = "慢充次数")
    private Double lowTimes;
    @ApiModelProperty(value = "充电总时长")
    private Double chargeTimeSum;
    @ApiModelProperty(value = "单次最长充电时间(h)")
    private Double chargeTimeMax;
    @ApiModelProperty(value = "单次充电最大行驶里程(km)")
    private Double singleChargeMaxMileage;
    @ApiModelProperty(value = "日最高速度(Km/h)")
    private Double runSpeedMax;
    @ApiModelProperty(value = "日平均速度(Km/h)")
    private Double runSpeedAvg;
    @ApiModelProperty(value = "数据最后上传时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "录入时间")
    private String createTime;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayVehModel fromEntry(DayVeh entry){
        DayVehModel m = new DayVehModel();
        BeanUtils.copyProperties(entry, m);
        /** 百公里额定耗电量 **/
        m.setRatedChargeCon100km(null);
        /** 快充次数 **/
        m.setFastTimes(null);
        /** 慢充次数 **/
        m.setLowTimes(null);
        return m;
    }

}
