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
* 功能： DayreportDataQuality实体<br>
* 描述： DayreportDataQuality实体<br>
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
* <td>2019-09-19 18:48:27</td>
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
public class DayreportDataQuality extends TailBean {

    /** id **/
    private Integer id;
    /** vid **/
    private String vid;
    /** vin **/
    private String vin;
    /** 统计日期 **/
    private Timestamp reportDate;
    /** 车辆当日首次上线时间 **/
    private String startTime;
    /** 车辆当日最后通讯时间 **/
    private String endTime;
    /** 应该上传的报文 **/
    private Integer shouldUploadNum;
    /** 实际上传的报文总条数 **/
    private Integer actualUploadNum;
    /** 异常报文条数 **/
    private Integer abnormalNum;
    /** 异常报文比例 **/
    private Double anbormalRate;
    /** 实时数据丢包率 **/
    private Double missRate;
    /** 是否存在转发报文,0不存在，1存在 **/
    private Double existForward;
    /** 转发报文总条数 **/
    private Integer forwardNum;
    /** 数据转发丢包率 **/
    private Double missForwardRate;
    /** 数据上传的频率 **/
    private Integer frequency;

    /** 是否存在转发报文(double -> String) **/
    private String existForwardToString;
    /** 统计日期 **/
    private String reportDateEx;
    /** 异常报文比例 **/
    private String anbormalRateEx;
    /** 实时数据丢包率 **/
    private String missRateEx;
    /** 数据转发丢包率 **/
    private String missForwardRateEx;
}
