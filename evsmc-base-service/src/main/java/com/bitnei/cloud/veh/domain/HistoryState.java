package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.Integer;
import java.lang.String;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HistoryState实体<br>
* 描述： HistoryState实体<br>
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
* <td>2019-03-09 11:23:08</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryState extends TailBean {

    /** 主键 **/
    private Integer id;
    /** 车辆表uuid **/
    private String vid;
    /** VIN **/
    private String vin;
    /** 报表日期 **/
    private String reportDate;
    /** 仪表里程(km) **/
    private Double gaugesMileage;
    /** 车辆状态 **/
    private Integer state;
    /** 总电压 **/
    private Double totalVoltage;
    /** 总电流 **/
    private Double totalCurrent;
    /** 车速 **/
    private Double speed;
    /** SOC **/
    private Integer soc;
    /** 经度 **/
    private Double positionLon;
    /** 纬度 **/
    private Double positionLat;
    /** 最后通讯时间 **/
    private String lastCommunicationTime;
    /** 有效CAN数据最后上传时间 **/
    private String lastCanValidTime;
    /** 最后一次充电时间 **/
    private String lastChargeTime;
    /** 充放电状态 **/
    private Integer chargeDischargeState;

    /** 车牌号 **/
    private String licensePlate;
    /** 车辆阶段 **/
    private String stage;
    /** 车辆型号 **/
    private String vehModelName;
    /** 车辆厂商 **/
    private String manuUnitId;
    /** 车运营单位 **/
    private String operUnitId;
    /** 内部编号 **/
    private String interNo;
    /** ICCID **/
    private String iccid;
    /** 车辆公告型号 **/
    private String noticeId;
    /** 上牌城市 **/
    private String operLicenseCityId;
    /** 激活时间 **/
    private String firstRegTime;
    /** 销售日期 **/
    private String sellDate;
    /** 终端零件号 **/
    private String termPartFirmwareNumbers;
    /** 终端厂商自定义编号 **/
    private String serialNumber;
    /** 地理位置 **/
    private String vehPosition;
    /** 录入时间 **/
    private String createTime;

    /** 上牌城市名称显示**/
    private String operLicenseCityDisplay;
    /** 车辆厂商名称显示**/
    private String manuUnitDisplay;
    /** 车运营单位名称显示**/
    private String operUnitDisplay;
    /** 车辆公告型号名称显示**/
    private String noticeDisplay;

    /** 车辆状态名称显示**/
    private String stateDisplay;
    /** 充放电状态名称显示**/
    private String chargeDischargeStateDisplay;
    /** 车辆阶段名称显示**/
    private String stageDisplay;

}
