package com.bitnei.cloud.dc.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardVehicle实体<br>
* 描述： ForwardVehicle实体<br>
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
* <td>2019-02-21 14:29:14</td>
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
public class ForwardVehicle extends TailBean {

    /** 推送状态: 待推送 */
    public static final Integer PUSH_STATUS_AWAIT = 1;
    /** 推送状态: 推送中 */
    public static final Integer PUSH_STATUS_ING = 2;
    /** 推送状态: 推送成功 */
    public static final Integer PUSH_STATUS_SUCCESS = 3;
    /** 推送状态: 待推失败 */
    public static final Integer PUSH_STATUS_FAIL = 4;

    /** 主键 **/
    private String id;
    /** 转发平台 **/
    private String platformId;
    /** 确认状态 **/
    private Integer confirmStatus;
    /** 确认时间 **/
    private String confirmTime;
    /** 确认人 **/
    private String confirmBy;
    /** 推送状态 **/
    private Integer pushStatus;
    /** 推送时间 **/
    private String pushTime;
    /** 动态数据转发状态 **/
    private Integer forwardStatus;
    /** 首次转发时间 **/
    private String forwardFirstTime;
    /** 推送错误信息 **/
    private String errorMessage;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 车辆ID **/
    private String vehicleId;

    /** 车牌号 **/
    private String licensePlate;
    /** VIN **/
    private String vin;
    /** 车辆阶段 **/
    private String stage;
    /** 车辆阶段变更时间 **/
    private String stageChangeDate;
    /** 车辆型号 **/
    private String vehModelId;
    /** 通讯协议 **/
    private String ruleId;
    /** 协议类型 **/
    private String ruleTypeId;
    /** 车辆厂商 **/
    private String manuUnitId;
    /** 运营单位 **/
    private String operUnitId;
    /** 在线状态 **/
    private String onlineStatus;
    /** 静态推送平台 **/
    private String staticForwardPlatform;
    /** 内部编号 **/
    private String interNo;
    /** ICCID **/
    private String iccid;

    /** 转发成功车辆 **/
    private Integer forwardSucVeh;
    /** 待确认车辆 **/
    private Integer unconfirmedVeh;
    /** 已确认车辆 **/
    private Integer confirmedVeh;
    /** 待推送车辆 **/
    private Integer toPushVeh;
    /** 推送失败车辆 **/
    private Integer pushFailVeh;
    /** 待转发车辆 **/
    private Integer toForwardVeh;
    /** 转发失败车辆 **/
    private Integer forwardFailVeh;

    /** 结果信息 **/
    private String resultMsg;


    /** 转发平台名称显示**/
    private String platformDisplay;
    /** 车辆型号名称显示**/
    private String vehModelDisplay;
    /** 通讯协议名称显示**/
    private String ruleDisplay;
    /** 协议类型名称显示**/
    private String ruleTypeDisplay;
    /** 车辆厂商名称显示**/
    private String manuUnitDisplay;
    /** 车运营单位名称显示**/
    private String operUnitDisplay;

    /**添加方式**/
    private Integer addMode;

    /**添加方式**/
    private String addModeName;

    /**车辆种类**/
    private String typeName;

    /**车辆品牌**/
    private String brandName;

    /**车型系列**/
    private String seriesName;

    /**车辆型号 -> 汽车厂商id **/
    private String vehUnitId;

}
