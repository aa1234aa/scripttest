package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehIdleRecord实体<br>
* 描述： VehIdleRecord实体<br>
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
* <td>2019-03-06 14:44:04</td>
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
public class VehIdleRecord extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 车辆id **/
    private String vehicleId;
    /** 车辆标识uuid **/
    private String vehicleUuid;
    /** 恢复上线时间 **/
    private String regainOnlineTime;
    /** 更新时间 **/
    private String updateTime;
    /** 创建时间 **/
    private String createTime;
    /** 是否结束 0: 已结束 1：未结束(实时) **/
    private Integer state;
    /** 提醒状态（0进行中，1已结束） **/
    private String remindState;
    /** 备注 **/
    private String remarks;
    /** 当前数据阈值 **/
    private String threshold;
    /** 最后上线时间 **/
    private String lastOnlineTime;
    /** 离线时里程，单位公里 **/
    private Double lastOnlineMileage;
    /** 恢复上线时里程,单位km **/
    private Double regainOnlineMileage;

    /** 离线天数 **/
    private String days;
    /** 离线秒数 **/
    private String seconds;
    /** 离线时里程用于导出显示**/
    private String lastOnlineMileageExport;
}
