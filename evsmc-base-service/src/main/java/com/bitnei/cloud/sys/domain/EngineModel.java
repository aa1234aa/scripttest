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
* 功能： EngineModel实体<br>
* 描述： EngineModel实体<br>
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
* <td>2018-12-03 11:16:29</td>
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
public class EngineModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 型号名称 **/
    private String name;
    /** 进气方式 **/
    private Integer intakeWay;
    /** 循环方式 **/
    private Integer circulationWay;
    /** 燃料形式 **/
    private Integer fuelForm;
    /** 油箱容量(L) **/
    private Double tankCapacity;
    /** 储气罐个数(个) **/
    private Integer gasholderNumber;
    /** 储气罐类型 **/
    private Integer gasholderType;
    /** 储气罐容量 **/
    private Double gasholderCapacity;
    /** 品牌名称 **/
    private String brandName;
    /** 发动机控制器型号 **/
    private String controllerModel;
    /** 峰值功率(KW) **/
    private Double peakPower;
    /** 峰值功率转速(r/min) **/
    private Double peakRotateSpeed;
    /** 峰值转矩(N.m) **/
    private Double peakTorque;
    /** 峰值扭矩转速(r/min) **/
    private Double peakTorqueRotateSpeed;
    /** 发动机生产企业 **/
    private String prodUnitId;
    /** 发动机控制器生产企业 **/
    private String controllerProdUnitId;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    /** OBD型号 **/
    private String obdModel;
    /** OBD生产厂商 **/
    private String obdUnitId;

    /** 接口照片 **/
    private String interfacePhoto;

}
