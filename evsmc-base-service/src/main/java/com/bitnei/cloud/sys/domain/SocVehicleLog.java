package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;
import java.lang.Float;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SocVehicleLog实体<br>
* 描述： SocVehicleLog实体<br>
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
* <td>2019-03-06 19:00:14</td>
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
public class SocVehicleLog extends TailBean {

    /** id **/
    private String id;
    /** vid--uuid **/
    private String vid;
    /** 开始soc **/
    private String startSoc;
    /** 提醒开始时间 **/
    private String startTime;
    /** 提醒结束时间 **/
    private String endTime;
    /** 结束soc **/
    private String endSoc;
    /** 创建日期 **/
    private String createTime;
    /** 当前位置 **/
    private String location;
    /** 经度 **/
    private String lng;
    /** 纬度 **/
    private String lat;
    /** 附近补电车(mysql--text,oracle-clob) **/
    private String nearBatteryTruck;
    /** 扩展字段1 **/
    private String extra1;
    /** 扩展字段2 **/
    private String extra2;
    /** 扩展字段3 **/
    private String extra3;
    /** 扩展字段4 **/
    private String extra4;
    /** 报警状态： 0报警中，1报警结束 **/
    private Integer status;
    /** SOC过低阈值,单位% **/
    private Float socThreshold;
    /** 时间阈值，单位ms **/
    private String timeThreshold;

}
