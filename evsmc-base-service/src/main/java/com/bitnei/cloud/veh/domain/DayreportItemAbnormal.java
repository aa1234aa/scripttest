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
* 功能： DayreportItemAbnormal实体<br>
* 描述： DayreportItemAbnormal实体<br>
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
* <td>2019-09-20 09:28:41</td>
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
public class DayreportItemAbnormal extends TailBean {

    /** 主键 **/
    private Integer id;
    /** 车辆uuid **/
    private String vid;
    /** 车辆vin **/
    private String vin;
    /** 报表日期 **/
    private String reportDate;
    /** 车辆状态异常条数 **/
    private Integer vehStatusNum;
    /** 充电状态异常条数 **/
    private Integer chargeStatusNum;
    /** 速度异常条数 **/
    private Integer speedNum;
    /** 累计里程异常条数 **/
    private Integer mileageNum;
    /** 总电压异常条数 **/
    private Integer totalVoltageNum;
    /** 总电流异常条数 **/
    private Integer totalCurrentNum;
    /** soc异常条数 **/
    private Integer socNum;
    /** 驱动电机异常条数 **/
    private Integer driverMotorNum;
    /** 燃料电池异常条数 **/
    private Integer fuelCellNum;
    /** 发动机异常条数 **/
    private Integer engineNum;
    /** 车辆位置数据异常条数 **/
    private Integer locationNum;
    /** 极值数据异常条数 **/
    private Integer extremeNum;
    /** 报警数据异常条数 **/
    private Integer alarmNum;
    /** 最后通讯时间 **/
    private String lastCommitTime;

}
