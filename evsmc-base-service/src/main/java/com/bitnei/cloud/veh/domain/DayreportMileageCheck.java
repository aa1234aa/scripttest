package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportMileageCheck实体<br>
* 描述： DayreportMileageCheck实体<br>
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
* <td>2019-03-14 16:46:26</td>
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
public class DayreportMileageCheck extends TailBean {

    /** id **/
    private Integer id;
    /** vid **/
    private String vid;
    /** vin **/
    private String vin;
    /** 统计日期 **/
    private Timestamp reportDate;
    /** 当日首次上线时间 **/
    private String firstOnlineTime;
    /** 当日开始里程 **/
    private Double firstStartMileage;
    /** 当日最后通讯时间 **/
    private String lastCommitTime;
    /** 当日结束里程 **/
    private Double lastEndMileage;
    /** 核查数据总条数 **/
    private Integer checkDataTotalNum;
    /** 无效数据条数 **/
    private Integer invalidNum;
    /** 异常数据条数 **/
    private Integer abnormalNum;
    /** 当日上线里程 **/
    private Double dayOnlineMileage;
    /** 总跳变扣除里程 **/
    private Double deductJumpMileage;
    /** 总连续电流扣除里程 **/
    private Double deductCurrentMileage;
    /** 当日有效里程 **/
    private Double dayValidMileage;
    /** 当日轨迹里程 **/
    private Double dayGpsMileage;
    /** 有效里程和轨迹里程相对误差 **/
    private Double validGpsDeviation;
    /** 上线里程和有效里程相对误差 **/
    private Double onlineValidDeviation;
    /** 单日核算里程 **/
    private Double dayCheckMileage;
    /** 运行时长（分钟） **/
    private Integer runTimeSum;
    /** 日充电时长（分钟） **/
    private Integer chargeTimeSum;
    /** 日充电状态最小SOC **/
    private Integer chargeSocMin;
    /** 日充电量（kw.h） **/
    private Double power;
    /** 日充电状态最大SOC **/
    private Integer chargeSocMax;
    /** 日在线总时长（分钟） **/
    private Integer onlineTimeSum;
    /** 日运行最大速度 **/
    private Double speedMax;
    /** 日运行平均速度 **/
    private Double speedAvg;

    /** （前端显示）统计日期 **/
    private String showReportDate;
    /** 有效里程和轨迹里程相对误差 **/
    private String showValidGpsDeviation;
    /** 上线里程和有效里程相对误差 **/
    private String showOnlineValidDeviation;
    /** （用于导出文件）当日轨迹里程 **/
    private String dayGpsMileageExport;
    /** （用于导出文件）单日核算里程 **/
    private String dayCheckMileageExport;
}
