package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 车辆安全事故分析报告/监管数据信息model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class TruUpAlarmDocModel {

    /** 报告号 **/
    @NotEmpty(message = "报告号")
    private String troCode;
    /** 监管数据文件 **/
    private String realState;
    /** 调查分析报告文件 **/
    private String analysisName;

}
