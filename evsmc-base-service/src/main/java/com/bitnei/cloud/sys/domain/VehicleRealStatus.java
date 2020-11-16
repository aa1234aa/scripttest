package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleRealStatus实体<br>
* 描述： VehicleRealStatus实体<br>
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
* <td>2019-03-16 14:55:45</td>
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRealStatus extends TailBean {

    /** 主键 **/
    private String id;
    private String vin;
    private String licensePlate;
    /** 上牌城市名称 **/
    private String operLicenseCityName;
    /** 运行区域城市名称 **/
    private String gpsCityName;
    /** 车辆Id **/
    private String vehicleId;
    /** 是否上过线 **/
    private Integer onlined;
    /** 车辆首次上线时间 **/
    private String firstRegTime;
    /** 在线状态  1：在线 2:离线 **/
    private Integer onlineStatus;
    /** 仪表盘里程 **/
    private Double mils;
    /** 创建时间 **/
    private String createTime;
    /** 更新时间 **/
    private String updateTime;
    /** 锁止功能状态 **/
    private Integer lockStatus;
    /** 动力锁止状态 **/
    private Integer powerLockStatus;
    /** 防拆状态 **/
    private Integer preventDismantleStatus;
    /** 防拆功能码 **/
    private Integer preventDismantleFunc;


    //辅助字段
    /** 内部编码 **/
    private String interNo;

    /** 车型名称 **/
    private String vehModelName;

    /** 终端型号 **/
    private String termModelName;

    /** 运营单位名称 **/
    private String operUnitName;

    /** 协议类型名称 **/
    private String ruleTypeName;

    /** 动力方式 **/
    private Integer powerMode;

    /** 车型id **/
    private String vehModelId;

    /** iccid **/
    private String iccid;

    /** 终端型号id **/
    private String termId;

    /** 运营单位id **/
    private String operUnitId;
    //终端编号
    private String termSerialNumber;

    private String sellCityName;
    //终端厂商
    private String termUnitName;

    private String vehBrandName;

    private String vehSeriesName;

    //销售信息车主
    private String ownerName;
    private String operOwnerName;

    //售后负责人
    private String operSupportOwnerName;

    private Integer faultStatus;

    private String vehUnitName;

    private String termRuleTypeName;
    private String termRuleTypeCode;
    private String uuid;

    private String efficiencyJson;
    private String identificationId;
    private Integer filingStatus;
    private String termPubKey;
    private String filingTime;

    /**
     * 默认值从未上线
     */
    private String isGps = "9999";
}
