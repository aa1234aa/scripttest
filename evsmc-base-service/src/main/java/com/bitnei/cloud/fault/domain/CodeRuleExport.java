package com.bitnei.cloud.fault.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Desc： 故障码导出 model
 * @Author: joinly
 * @Date: 2019/2/27
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeRuleExport {

    /** 故障码规则名称 **/
    private String faultName;

    /** 解析方式, 1=字节， 2＝bit **/
    private Integer analyzeType;

    /**故障种类**/
    private String faultTypeName;

    /** 车辆公告型号id, 以, 的形式组成的串 **/
    private String vehModelId;

    /** 车辆公告型号 **/
    private String vehModelName;

    /**故障描述**/
    private String faultDescribe;

    /**解决方案**/
    private String solutionDescribe;

    /**起始位**/
    private String startPoint;

    /** 故障码长度 **/
    private Integer exceptionCodeLength;

    /**正常状态故障码**/
    private String normalCode;

    /**故障码**/
    private String exceptionCode;

    /**报警级别**/
    private Integer alarmLevel;

    /**是否产生报警**/
    private Integer produceAlarm;

    /**平台响应方式**/
    private String responseMode;

    /** 开始时间阈值(秒) **/
    private int beginThreshold;

    /** 结束时间阈值(秒) **/
    private int endThreshold;

    /** 开始故障值连续帧数(帧) **/
    private Integer beginCountThreshold;

    /** 结束正常值连续帧数(帧) **/
    private Integer endCountThreshold;

    /** 所属零部件 **/
    private String subordinatePartsId;

    private Integer enableTimeThreshold;

    private Integer enableCountThreshold;

    private Integer enabledStatus;
}
