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

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportAbnormalSituation实体<br>
* 描述： DayreportAbnormalSituation实体<br>
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
* <td>2019-03-22 11:01:58</td>
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
public class DayreportAbnormalSituation extends TailBean {

    /** id **/
    private Integer id;
    /** uuid **/
    private String vid;
    /** vin **/
    private String vin;
    /** 报表日期 **/
    private Timestamp reportDate;
    /** 异常类型(1：速度，2：里程，3：经纬度，4：时间，5：电压，6：电流，7：soc) **/
    private Integer type;
    /** 异常类别(1：有效性，2：数值) **/
    private Integer category;
    /** 异常数据条数 **/
    private Integer abnormalNum;

    /** 前端展示最近异常时间 **/
    private String lastDate;
    /** 时间异常（条）**/
    private String timeNumber;
}
