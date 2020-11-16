package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 车辆安全风险通知Model
 * @author lijiezhou
 * @date 2019年7月15日 16:19:46
 */
@Setter
@Getter
public class SafeRiskUpModel {

    /** 消息编号 */
    @NotEmpty(message = "消息编号")
    private String code;

    /** 车辆vin */
    @NotEmpty(message = "车辆vin")
    private String vin;

    /** 消息状态 0:未读 1:已读 3:未回复 4:已回复 5:处理中 6:已处理 7:已上报 */
    @NotEmpty(message = "消息状态")
    private String state;

    /** 处理意见 */
    @NotEmpty(message = "处理意见")
    private String dealNote;

    /** 风险等级 */
    @NotEmpty(message = "风险等级")
    private String dangerLevel;

    /** 更新时间 */
    @NotEmpty(message = "更新时间")
    private String updateTime;

}
