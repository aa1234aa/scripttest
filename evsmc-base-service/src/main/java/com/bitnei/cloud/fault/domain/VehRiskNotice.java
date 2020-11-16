package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehRiskNotice实体<br>
* 描述： VehRiskNotice实体<br>
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
* <td>2019-07-08 18:07:56</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehRiskNotice extends TailBean {

    /** 消息状态=未读 **/
    public static final int READ_STATUS_UN = 0;

    /** id **/
    private String id;
    /** 车辆id **/
    private String vehicleId;
    /** 报警提醒名称 **/
    private String name;
    /** 通知种类 **/
    private Integer noticeType;
    /** 通知状态 **/
    private Integer noticeStatus;
    /** 消息状态(0未读1已读) **/
    private Integer readStatus;
    /** 风险等级 **/
    private Integer riskLevel;
    /** 通知原文 **/
    private String noticeOriginalText;
    /** 批注 **/
    private String annotations;
    /** 报警发生时间|首次通知时间 **/
    private String firstNoticeTime;
    /** 状态更新时间 **/
    private String stateUpdateTime;
    /** 车辆报警状态 **/
    private String alarmStatus;
    /** 已读时间 **/
    private String readTime;
    /** 处理时间 **/
    private String processingTime;
    /** 国家平台管理员意见 **/
    private String opinion;
    /** 最近一次操作人员 **/
    private String lastOperator;
    /** 通知推送时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 附件 **/
    private String fileId;
    /** 消息编码 **/
    private String code;
    /** 消息编码集合 **/
    private List<String> codes;

    // 统计字段
    /** 当前风险通知总条数 **/
    private Integer allNum;
    /** 未读 **/
    private Integer unRead;
    /** 未回复 **/
    private Integer unReply;
    /** 已回复 **/
    private Integer reply;
    /** 审核中 **/
    private Integer examining;
    /** 1-2级 **/
    private Integer levelOneAndTwo;
    /** 3-4级 **/
    private Integer levelThreeAndFour;
    /** 5级 **/
    private Integer levelFive;
    /** vin **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 最近一次处理批注内容 **/
    private String lastAnnotations;
    /** 最近一次回复意见时间 **/
    private String lastOpinionTime;
}
