package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 车辆安全风险通知Model
 * @author zhouxianzhou
 * @date 2019年7月15日 16:19:46
 */
@Setter
@Getter
public class SafeRiskModel {

    /** 消息编号 */
    @NotEmpty(message = "消息编号")
    private String code;

    /** 消息种类 <br>
     * 0|1:国标三级报警 2:自定义报警 3:阈值三级报警 4:一级报警 5:二级报警 6:事故报警 <br>
     * 风险等级规则 三级报警为2级风险，事故处理为5级风险
     */
    @NotEmpty(message = "消息种类")
    private String type;

    /** 车辆vin */
    @NotEmpty(message = "车辆vin")
    private String vin;

    /** 报警时间yyyy-MM-dd hh:mm:ss */
    @NotEmpty(message = "报警时间")
    private String alarmTime;

    /** 报警名称 */
    @NotEmpty(message = "报警名称")
    private String alarmName;

    /** 消息状态 0:未读 1:已读 3:未回复 4:已回复 5:处理中 6:已处理 7:已上报 */
    private String state;

    /** 处理意见 */
    private String dealNote;

    /** 风险等级 **/
    private String dangerLevel;

    /** 更新时间 */
    private String updateTime;

}
