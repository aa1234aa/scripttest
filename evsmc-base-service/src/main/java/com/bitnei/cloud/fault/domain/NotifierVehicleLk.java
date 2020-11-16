package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierVehicleLk实体<br>
* 描述： NotifierVehicleLk实体<br>
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
* <td>2019-03-06 17:37:36</td>
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotifierVehicleLk extends TailBean {

    /** 主键 **/
    private String id;
    /** 故障通知人id **/
    private String notifierId;
    /** 车辆id **/
    private String vehicleId;
    /** 分配人 **/
    private String createBy;
    /** 分配时间 **/
    private String createTime;

    private String vin;

    /** 车牌 **/
    private String licensePlate;

    /** 车辆型号 **/
    private String vehModelName;

    /** 上牌城市 **/
    private String operLicenseCity;

    /** 内部编号 **/
    private String interNo;
}
