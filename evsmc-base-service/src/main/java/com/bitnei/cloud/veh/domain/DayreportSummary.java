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
* 功能： DayreportSummary实体<br>
* 描述： DayreportSummary实体<br>
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
* <td>2019-03-11 09:40:45</td>
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
public class DayreportSummary extends TailBean {

    /** id **/
    private Integer id;
    /** vid **/
    private String vid;
    /** vin **/
    private String vin;
    /** 报表日期 **/
    private Timestamp reportDate;
    /** 是否是新增的车辆 **/
    private Integer append;
    /** 最后通讯时间 **/
    private String lastCommitTime;
    /** 最后有效里程 **/
    private Double lastEndMileage;
    /** 是否可监控 **/
    private Integer monitor;
    /** 总上线里程 **/
    private Double totalOnlineMileage;
    /** 	总轨迹里程 **/
    private Double totalGpsMileage;
    /** 总有效里程 **/
    private Double totalValidMileage;
    /** 总核算里程 **/
    private Double totalCheckMileage;

    //用于导出
    /** 最后有效里程 **/
    private String lastEndMileageExport;
    /** 总上线里程 **/
    private String totalOnlineMileageExport;
    /** 总轨迹里程 **/
    private String totalGpsMileageExport;
    /** 总有效里程 **/
    private String totalValidMileageExport;
    /** 总核算里程**/
    private String totalCheckMileageExport;
}
