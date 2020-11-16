package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleItem实体<br>
* 描述： CodeRuleItem实体<br>
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
* <td>2019-02-25 18:10:28</td>
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
public class CodeRuleItem extends TailBean {

    /** 主键 **/
    private String id;
    /** 故障规则主表id **/
    private String faultCodeRuleId;
    /** 故障码 **/
    private String exceptionCode;
    /** 是否产生告警 1=是, 0=否 **/
    private Integer produceAlarm;
    /** 报警级别;  0=无、1=1级、2=2级、3=3级 **/
    private Integer alarmLevel;
    /** 响应方式：0=无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知 **/
    private String responseMode;
    /** 删除状态  1：是；0：否 **/
    private Integer deleteStatus;

    /**
     * 创建时间
     **/
    private String createTime;

    /**
     * 创建人
     **/
    private String createBy;


    //辅助字段
    /** 故障码规则名称 **/
    private String faultName;

    /** 解析方式, 1=字节， 2＝bit **/
    private Integer analyzeType;

    /** 起始位 **/
    private Integer startPoint;

    /** 正常码（无故障状态故障码） **/
    private String normalCode;

    /** 车辆公告型号id, 以, 的形式组成的串 **/
    private String vehModelId;
    //故障码类型
    private String faultCodeType;

    private Integer enabledStatus;
}
