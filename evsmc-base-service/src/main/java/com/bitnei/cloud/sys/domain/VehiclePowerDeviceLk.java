package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehiclePowerDeviceLk实体<br>
* 描述： VehiclePowerDeviceLk实体<br>
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
* <td>2018-12-13 17:14:00</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiclePowerDeviceLk extends TailBean {

    /** 主键 **/
    private String id;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 车辆id **/
    private String vehicleId;
    /** 发电装置id **/
    private String powerDeviceId;
    /** 发电装置code **/
    private String powerDeviceCode;
    /** 安装位置 1：前置 2：中置 3：后置 **/
    private Integer installPosition;
    /** 燃油发电机型号id **/
    private String fuelGeneratorModelId;

    /** 燃料电池型号名称 */
    private String fuelBatteryModelName;
    /** 燃料电池系统额定功率(kW) */
    private Double ratedPower;
    /** 燃料电池系统生产企业 */
    private String prodUnitId;
    /** 发票号 */
    private String invoiceNo;


    /** 额定功率(KW) **/
    private String ratedPowerStr;
    /** 系统生产企业名称 **/
    private String prodUnitName;


}
