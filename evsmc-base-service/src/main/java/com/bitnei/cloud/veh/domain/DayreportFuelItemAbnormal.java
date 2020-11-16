package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 燃油车辆异常数据项报表<br>
* 描述： 燃油车辆异常数据项报表<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信有限公司<br>
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
* <td>2019-09-24 13:57:58</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayreportFuelItemAbnormal extends TailBean {

    /** id **/
    private Integer id;
    /** 车辆UUID **/
    private String vid;
    /** 车辆vin **/
    private String vin;
    /** 报表时间 **/
    private Timestamp reportDate;
    /** 油箱液位异常条数 **/
    private Integer tankLevelNum;
    /** 车速异常条数 **/
    private Integer speedNum;
    /** 累计里程异常条数 **/
    private Integer mileageNum;
    /** 发动机数据异常条数 **/
    private Integer engineNum;
    /** 车辆位置数据异常条数 **/
    private Integer locationNum;
    /** 最后通讯时间 **/
    private String lastCommitTime;

}
