package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DriveMotorModel实体<br>
* 描述： DriveMotorModel实体<br>
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
* <td>2018-12-03 11:12:12</td>
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
@Setter
@Getter
public class DriveMotorModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 型号名称 **/
    private String name;
    /** 驱动电机种类 **/
    private Integer driveType;
    /** 驱动电机冷却方式 **/
    private Integer driveColdMode;
    /** 额定电压(V) **/
    private Double ratedVoltage;
    /** 最大工作电流(A) **/
    private Double maxElectricity;
    /** 驱动电机控制器型号 **/
    private String controllerModel;
    /** 额定功率(KW) **/
    private Double ratedPower;
    /** 峰值功率(KW) **/
    private Double peakPower;
    /** 额定转速(r/min) **/
    private Double ratedRotateSpeed;
    /** 峰值转速(r/min) **/
    private Double peakRotateSpeed;
    /** 额定转矩(N.m) **/
    private Double ratedTorque;
    /** 峰值转矩(N.m) **/
    private Double peakTorque;
    /** 驱动电机最大输出转矩(N.m) **/
    private Double maxOutputTorque;
    /** 驱动电机最高转速(r/min) **/
    private Double maxRotateSpeed;
    /** 驱动电机生产企业 **/
    private String prodUnitId;
    /** 驱动电机控制器生产企业 **/
    private String controllerProdUnitId;

    /** 有效比功率(KW/kg) */
    private Double validPower;

    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;



}
