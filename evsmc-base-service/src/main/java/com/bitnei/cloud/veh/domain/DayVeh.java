package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayVeh实体<br>
* 描述： DayVeh实体<br>
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
public class DayVeh extends TailBean {

    /** 报表日期 **/
    private String reportDate;
    /** 内部编号 **/
    private String interNo;
    /** VIN **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 车辆型号 **/
    private String vehModelName;
    /** 车辆公告型号 **/
    private String noticeId;
    /** 车辆厂商 **/
    private String manuUnitId;
    /** 车运营单位 **/
    private String operUnitId;
    /** 上牌城市 **/
    private String operLicenseCityId;
    /** 激活时间 **/
    private String firstRegTime;
    /** 销售日期 **/
    private String sellDate;

    /** 日活跃总时间(h) **/
    private Double dailyActiveTotalTime;
    /** 日总行驶时间(h) **/
    private Double runTimeSum;
    /** 日行驶次数 **/
    private Double runTimes;
    /** 日总行驶里程(Km) **/
    private Double runKm;
    /** 单次运行最大里程(Km) **/
    private Double runKmMax;
    /** 总里程 **/
    private Double lastEndMileage;
    /** 动力电池累积能量 **/
    private Double chargeConsume;
    /** 实际百公里耗电量 **/
    private Double chargeCon100km;
    /** 百公里额定耗电量 **/
    private Double ratedChargeCon100km;
    /** 单次充电后最大耗电量(Kw/h) **/
    private Double chargeSconsumeMax;
    /** 充电总次数 **/
    private Double chargeTimes;
    /** 快充次数 **/
    private Double fastTimes;
    /** 慢充次数 **/
    private Double lowTimes;
    /** 充电总时长 **/
    private Double chargeTimeSum;
    /** 单次最长充电时间(h) **/
    private Double chargeTimeMax;
    /** 单次充电最大行驶里程(km) **/
    private Double singleChargeMaxMileage;
    /** 日最高速度(Km/h) **/
    private Double runSpeedMax;
    /** 日平均速度(Km/h) **/
    private Double runSpeedAvg;
    /** 数据最后上传时间 **/
    private String lastCommitTime;
    /** 录入时间 **/
    private String createTime;

    /** 车辆公告型号名称显示**/
    private String noticeDisplay;
    /** 车辆厂商名称显示**/
    private String manuUnitDisplay;
    /** 车运营单位名称显示**/
    private String operUnitDisplay;
    /** 上牌城市名称显示**/
    private String operLicenseCityDisplay;

}
