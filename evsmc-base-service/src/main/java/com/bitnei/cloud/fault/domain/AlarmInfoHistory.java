package com.bitnei.cloud.fault.domain;

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
* 功能： AlarmInfoHistory实体<br>
* 描述： AlarmInfoHistory实体<br>
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
* <td>2019-03-04 20:02:06</td>
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
@AllArgsConstructor
@NoArgsConstructor
public class AlarmInfoHistory extends TailBean {

    /** 主键 **/
    private String id;
    /** 车辆表uuid **/
    private String uuid;
    /** 车辆id **/
    private String vehicleId;
    /** 故障类型：1 = 平台, 2 = 故障码 **/
    private Integer faultType;
    /**父规则id：当=时2( 故障码) 取fault_code_rule表的id **/
    private String parentRuleId;
    /** 规则id：当fault_type = 1 ( 平台) 时取 fault_parameter_rule表的id,  当=时2( 故障码) 取fault_code_rule_item表的id **/
    private String ruleId;
    /** 规则名称 **/
    private String ruleName;
    /** 报警级别 0：无、1：1级、2：2级、3：3级 **/
    private Integer alarmLevel;
    /** 报警开始时间 **/
    private String faultBeginTime;
    /** 报警结束时间 **/
    private String faultEndTime;
    /** 故障状态  1:未结束, 2已结束 **/
    private Integer faultStatus;
    /** 经度 **/
    private Double longitude;
    /** 纬度 **/
    private Double latitude;
    /** 响应方式：0= 无、1＝系统弹窗、2＝系统弹窗+声音提醒、3＝APP弹窗提醒、4＝短信通知 **/
    private String responseMode;
    /** 故障触发项 **/
    private String triggerItem;

    /** 处理状态  1:未处理, 2:处理中, 3:已处理 **/
    private Integer procesStatus;
    /** 处理时间 **/
    private String procesTime;
    /** 推送状态 1未推送，2已推送 **/
    private Integer pushStatus;
    /** 推送时间 **/
    private String pushTime;
    /** 创建时间 **/
    private String createTime;

    /** 车辆vin **/
    private String vin;

    /** 车牌 **/
    private String licensePlate;

    /** 车辆型号 **/
    private String vehModelName;

    /**购车领域**/
    private String sellForField;

    /** 个人名称 **/
    private String sellPriVehOwnerId;

    /** 购车单位**/
    private String sellPubUnitId;

    /**
     * kafka消息id
     **/
    private String msgId;

    /**
     * 车辆删除状态
     **/
    private Integer vehDelete;

    /**
     * 围栏类型  圆形 多边形
     */
    private String chartType;

    /**
     * 形状的经纬度
     */
    private String lonlatRange;

    /**
     * 权限字段
     */
    private String whoCanSeeMe;

    private String[] whoCanSeeMes;
}
