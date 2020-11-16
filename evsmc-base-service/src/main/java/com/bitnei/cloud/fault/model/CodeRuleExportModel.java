package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.fault.domain.CodeRuleExport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/2/27
 */

@Data
public class CodeRuleExportModel extends BaseModel {

    /** 故障码规则名称 **/
    private String faultName;

    /** 解析方式, 1=字节， 2＝bit **/
    private Integer analyzeType;

    @DictName(code = "ANALYZE_TYPE", joinField = "analyzeType")
    private String analyzeTypeDisplay;

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

    @DictName(code = "ALARM_LEVEL", joinField = "alarmLevel")
    private String alarmLevelDisplay;

    /**是否产生报警**/
    private Integer produceAlarm;

    @DictName(code = "PRODUCE_ALARM", joinField = "produceAlarm")
    private String produceAlarmDisplay;

    /**平台响应方式**/
    private String responseMode;

    private String responseModeDisplay;
    /** 时间开始阈值 单位 秒 **/
    private Integer beginThreshold;
    /** 时间结束阈值 单位 秒 **/
    private Integer endThreshold;

    /** 开始故障值连续帧数(帧) **/
    private Integer beginCountThreshold;

    /** 结束正常值连续帧数(帧) **/
    private Integer endCountThreshold;

    /** 是否启用持续时间 是否有效 1=启用, 0=禁用 */
    private Integer enableTimeThreshold;
    @DictName(code = "ENABLED_STATUS", joinField = "enableTimeThreshold")
    private String enableTimeThresholdDesc;

    /** 是否启用持续帧数 是否有效 1=启用, 0=禁用 */
    private Integer enableCountThreshold;
    @DictName(code = "ENABLED_STATUS", joinField = "enableCountThreshold")
    private String enableCountThresholdDesc;

    /** 所属零部件 **/
    private String subordinatePartsId;
    @DictName(code = "SUBORDINATE_PARTS", joinField = "subordinatePartsId")
    private String subordinateParts;

    private Integer enabledStatus;
    private String enabledStatusDisplay;

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CodeRuleExportModel fromEntry(CodeRuleExport entry){
        CodeRuleExportModel m = new CodeRuleExportModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
