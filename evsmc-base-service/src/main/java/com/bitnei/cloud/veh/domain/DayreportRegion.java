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
* 功能： DayreportRegion实体<br>
* 描述： DayreportRegion实体<br>
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
* <td>2019-03-11 15:18:50</td>
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
public class DayreportRegion extends TailBean {

    /** id **/
    private Integer id;
    /** vid **/
    private String vid;
    /** vin **/
    private String vin;
    /** 报表日期 **/
    private Timestamp reportDate;
    /** 省份 **/
    private String province;
    /** 城市 **/
    private String city;
    /** 该区域首次上线时间 **/
    private String firstOnlineTime;
    /** 该区域开始里程 **/
    private Double firstStartMileage;
    /** 该区域最后通讯时间 **/
    private String lastCommitTime;
    /** 该区域结束里程 **/
    private Double lastEndMileage;
    /** 该区域核查数据总条数 **/
    private Integer checkDataTotalNum;
    /** 该区域无效条数 **/
    private Integer invalidNum;
    /** 该区域异常条数 **/
    private Integer abnormalNum;
    /** 该区域内的一天的仪表的总里程 **/
    private Double meterTotalMileage;
    /** 里程跳变扣除里程 **/
    private Double deductJumpMileage;
    /** 总连续电流扣除里程 **/
    private Double deductCurrentMileage;
    /** 该区域内的有效里程 **/
    private Double validTotalMileage;
    /** 该区域内一天行驶的gps的总里程 **/
    private Double gpsTotalMileage;
    /** 有效里程和轨迹里程相对误差 **/
    private Double validGpsDeviation;
    /** 上线里程和有效里程相对误差 **/
    private Double onlineValidDeviation;
    /** 该区域内的核查里程 **/
    private Double checkTotalMileage;
    /** 该区域行驶时长（分钟） **/
    private Integer runTimeSum;
    /** 该区域充电时长 （分钟） **/
    private Integer chargeTimeSum;
    /** 区域内日充电状态最小SOC **/
    private Integer chargeSocMin;
    /** 该区域的充电量 **/
    private Double power;
    /** 区域内日充电状态最大SOC **/
    private Integer chargeSocMax;
    /** 区域在线总时长（分钟） **/
    private Integer onlineTimeSum;
    /** 运行状态下最大速度 **/
    private Double speedMax;
    /** 运行平均速度 **/
    private Double speedAvg;
    /** 行驶区域总里程（km） **/
    private Double allAreaMileage;
    /** 行驶区域GPS总里程（km） **/
    private Double allGpsMileage;
    /** 仪表总里程（km） **/
    private Double allMileage;
    /** 行驶区域里程占比（%） **/
    private Double ratio;

    //用于导出文件
    /** 行驶区域总里程（km） **/
    private String allAreaMileageExport;
    /** 行驶区域GPS总里程（km） **/
    private String allGpsMileageExport;
    /** 仪表总里程（km） **/
    private String allMileageExport;
    /** 行驶区域里程占比（%） **/
    private String ratioExport;

}
