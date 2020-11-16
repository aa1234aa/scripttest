package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.fault.domain.VehRiskNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehRiskNotice新增模型<br>
* 描述： VehRiskNotice新增模型<br>
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
@ApiModel(value = "VehRiskNoticeModel", description = "当前国家平台车辆风险通知Model")
public class VehRiskNoticeModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ApiModelProperty(value = "报警提醒名称")
    private String name;

    @ApiModelProperty(value = "通知种类")
    private Integer noticeType;

    @ApiModelProperty(value = "通知状态")
    private Integer noticeStatus;

    @ApiModelProperty(value = "风险等级")
    private Integer riskLevel;

    @ApiModelProperty(value = "通知原文")
    private String noticeOriginalText;

    @ApiModelProperty(value = "批注")
    private String annotations;

    @ApiModelProperty(value = "报警发生时间|首次通知时间")
    private String firstNoticeTime;

    @ApiModelProperty(value = "状态更新时间")
    private String stateUpdateTime;

    @ApiModelProperty(value = "车辆报警状态")
    private String alarmStatus;

    @ApiModelProperty(value = "已读时间")
    private String readTime;

    @ApiModelProperty(value = "处理时间")
    private String processingTime;

    @ApiModelProperty(value = "最近一次国家平台管理员意见")
    private String opinion;

    @ApiModelProperty(value = "最近一次处理批注内容")
    private String lastAnnotations;

    @ApiModelProperty(value = "最近一次回复意见时间")
    private String lastOpinionTime;

    @ApiModelProperty(value = "最近一次操作人员")
    private String lastOperator;

    @ApiModelProperty(value = "通知推送时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "附件id")
    private String fileId;

    @ApiModelProperty(value = "消息编码")
    private String code;

    @ApiModelProperty(value = "正序|倒序(0:倒序，1：正序)")
    private String sortNum;

    @ApiModelProperty(value = "当前风险通知总条数")
    private Integer allNum;

    @ApiModelProperty(value = "未读")
    private Integer unRead;

    @ApiModelProperty(value = "未回复")
    private Integer unReply;

    @ApiModelProperty(value = "已回复")
    private Integer reply;

    @ApiModelProperty(value = "审核中")
    private Integer examining;

    @ApiModelProperty(value = "1-2级")
    private Integer levelOneAndTwo;

    @ApiModelProperty(value = "3-4级")
    private Integer levelThreeAndFour;

    @ApiModelProperty(value = "5级")
    private Integer levelFive;

    @ApiModelProperty(value = "未读占比")
    private String unReadRatio;

    @ApiModelProperty(value = "未回复占比")
    private String unReplyRatio;

    @ApiModelProperty(value = "已回复占比")
    private String replyRatio;

    @ApiModelProperty(value = "审核中占比")
    private String examiningRatio;

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "国家平台管理员意见")
    private List<FaultDisposalOpinionsModel> opinions;

    @DictName(code = "RISK_LEVEL", joinField = "riskLevel")
    @ApiModelProperty(value = "风险等级")
    private String riskLevelName;

    @DictName(code = "NOTICE_STATE", joinField = "noticeStatus")
    @ApiModelProperty(value = "通知状态")
    private String noticeStatusName;

    @DictName(code = "RISK_MESSAGE_TYPE", joinField = "noticeType")
    @ApiModelProperty(value = "通知种类")
    private String noticeTypeName;

    @ApiModelProperty(value = "codes,用于定时更新状态")
    private List<String> codes;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehRiskNoticeModel fromEntry(VehRiskNotice entry){
        VehRiskNoticeModel m = new VehRiskNoticeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
