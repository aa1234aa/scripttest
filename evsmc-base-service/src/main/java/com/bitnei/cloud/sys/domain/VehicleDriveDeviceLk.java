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
* 功能： VehicleDriveDeviceLk实体<br>
* 描述： VehicleDriveDeviceLk实体<br>
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
* <td>2018-12-13 17:09:47</td>
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
public class VehicleDriveDeviceLk extends TailBean {

    /** 主键 **/
    private String id;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 车辆ID **/
    private String vehicleId;
    /** 可充电储能装置id **/
    private String drvieDeviceId;

    /** 可充电储能装置code **/
    private String drvieDeviceCode;

    /** 驱动电机型号ID **/
    private String driveModelId;

    /** 安装位置(1:前置 2:后置 3:轮边 4:轮毂内) **/
    private String installPosition;

    /** 驱动装置序号 **/
    private String sequenceNumber;

    /** 驱动电机型号名称 **/
    private String driveMotorModelName;

    /** 额定功率(KW) **/
    private Double ratedPower;

    /** 驱动电机生产企业 **/
    private String prodUnitId;

    /** 发票号 **/
    private String invoiceNo;

    /** 发动机型号ID **/
    private String engineModelId;

    /** 发动机型号名称 **/
    private String engineModelName;

    /** 发动机燃料形式 **/
    private Integer fuelForm;

    /** 额定功率(KW) **/
    private String ratedPowerStr;
    /** 系统生产企业名称 **/
    private String prodUnitName;

}
