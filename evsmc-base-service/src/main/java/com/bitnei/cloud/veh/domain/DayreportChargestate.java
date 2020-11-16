package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.Integer;
import java.lang.String;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportChargestate实体<br>
* 描述： DayreportChargestate实体<br>
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
* <td>2019-03-15 16:33:37</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayreportChargestate extends TailBean {

    /** id **/
    private Integer id;
    /** 报表日期 年月日  **/
    private String reportTime;
    /** 车辆编码 **/
    private String vid;
    /** 日总充电时长h **/
    private Double chargeTimeSum;
    /** 充电次数 **/
    private Double chargeTimes;
    /** 日总耗电量(Kw.h)
 **/
    private Double chargeConsume;
    /** 百公里耗电量(Kw.h)
 **/
    private Double chargeCon100km;
    /** 日最大充电时长h **/
    private Double chargeTimeMax;
    /** 日平均充电时长 **/
    private Double chargeTimeAvg;
    /** 充电状态最高总电压(v)
 **/
    private Double chargeVolMax;
    /** 充电状态最低总电压(v)
 **/
    private Double chargeVolMin;
    /** 充电状态最高总电流(A)
 **/
    private Double chargeCurMax;
    /** 充电状态最低总电流(A)
 **/
    private Double chargeCurMin;
    /** 充电状态最大SOC(%)
 **/
    private Double chargeSocMax;
    /** 充电状态最小SOC(%)
 **/
    private Double chargeSocMin;
    /** 充电状态单体最高电压(V)
 **/
    private Double chargeSvolMax;
    /** 充电状态单体最低电压(V)
 **/
    private Double chargeSvolMin;
    /** 充电状态采集点最高温度(°C)
 **/
    private Double chargeCptempMax;
    /** 充电状态采集点最低温度(°C)
 **/
    private Double chargeCptempMin;
    /** 充电状态电机最高温度(°C) **/
    private Double chargeEngtempMax;
    /** 充电状态电机最低温度(°C)
 **/
    private Double chargeEngtempMin;
    /** 单次充电最大耗电量(Kw.h)
 **/
    private Double chargeSconsumeMax;
    /** 日均单次耗电量(Kw.h)
 **/
    private Double chargeSconsumeAvg;

}
