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
* 功能： VehModelAlarm实体<br>
* 描述： VehModelAlarm实体<br>
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
* <td>2019-02-20 13:45:57</td>
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
public class VehModelAlarm extends TailBean {

    /** 主键标识|车型ID **/
    private String id;
    /** 温度差异报警值 **/
    private String temperatureDifferenceAlarm;
    /** 电池高温报警值 **/
    private String batteryHighTemperaturAlarm;
    /** 车载储能装置类型过压报警值 **/
    private String energyDeviceOvervoltageAlarm;
    /** 车载储能装置类型欠压报警值 **/
    private String energyDeviceUndervoltageAlarm;
    /** 驱动电机控制器温度报警值 **/
    private String driveMotorControllerTemperaAlarm;
    /** 驱动电机温度报警值 **/
    private String driveMotorTemperatureAlarm;
    /** 单体电池过压报警值 **/
    private String singleBatteryOvervoltageAlarm;
    /** 单体电池欠压报警值 **/
    private String singleBatteryUndervoltageAlarm;
    /** SOC低报警值 **/
    private String socLowAlarm;
    /** SOC跳变报警值 **/
    private String socJumpAlarm;
    /** SOC过高报警值 **/
    private String socHighAlarm;
    /** 电池单体一致性差报警值 **/
    private String batteryConsistencyAlarm;
    /** 车载储能装置过充报警值 **/
    private String vehicularDeviceOverchargeAlarm;
    /** 绝缘故障报警值 **/
    private String insulationFaultAlarm;
    /** 是否启用 **/
    private int enable;
    /** 平台响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知 **/
    private String responseMode;
    /** 开始时间阈值(秒) **/
    private Integer beginThreshold;
    /** 结束时间阈值(秒) **/
    private Integer endThreshold;

}
