package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sms.domain.SmsTaskItem;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SmsTaskItems新增模型<br>
* 描述： SmsTaskItems新增模型<br>
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
* <td>2019-08-17 15:45:24</td>
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
@ApiModel(value = "SmsTaskItemModel", description = "短信下发任务明细Model")
public class SmsTaskItemModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "任务id")
    @NotEmpty(message = "任务id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ColumnHeader(title = "业务类型: 1、短信下发; 2、终端短信唤醒")
    @NotNull(message = "业务类型: 1、短信下发; 2、终端短信唤醒不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "业务类型: 1、短信下发; 2、终端短信唤醒")
    private Integer serviceType;

    @ColumnHeader(title = "接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人")
    @NotNull(message = "接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人")
    private Integer receiverType;

    @ColumnHeader(title = "接收人id")
    @NotEmpty(message = "接收人id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接收人id")
    private String receiverId;

    @ColumnHeader(title = "接收人")
    @NotEmpty(message = "接收人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接收人")
    private String receiver;

    @ColumnHeader(title = "车辆id")
    @NotEmpty(message = "车辆id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ColumnHeader(title = "发送成功状态 : 1成功, 2失败")
    @NotNull(message = "发送成功状态 : 1成功, 2失败不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "发送成功状态 : 1成功, 2失败")
    private Integer sendStatus;

    @DictName(joinField = "sendStatus", code = "SMS_SEND_STATUS")
    private String sendStatusDisplay;

    @ColumnHeader(title = "失败原因")
    @NotEmpty(message = "失败原因不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "失败原因")
    private String failMsg;

    @ColumnHeader(title = "电话号码")
    @NotEmpty(message = "电话号码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "电话号码")
    private String msisd;

    /** 车架号 **/
    private String vin;

    /** 发送时间 **/
    private String sendTime;

    /**短信模板**/
    private String templateName;

    /**短信内容**/
    private String smsContent;

    /**是否草稿**/
    private String taskStatus;

    /**最后操作时间**/
    private String taskUpdateTime;

    /**最后操作人**/
    private String taskUpdateBy;

    private String faultId;

    /** 备注 **/
    private String remarks;

    /** 收件人**/
    private String consignee;

    /**所属单位**/
    private String unitName;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SmsTaskItemModel fromEntry(SmsTaskItem entry){
        SmsTaskItemModel m = new SmsTaskItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
