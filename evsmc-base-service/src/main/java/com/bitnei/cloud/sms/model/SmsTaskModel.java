package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sms.domain.SmsTaskItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sms.domain.SmsTask;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SmsTask新增模型<br>
* 描述： SmsTask新增模型<br>
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
* <td>2019-08-16 09:41:04</td>
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
@ApiModel(value = "SmsTaskModel", description = "短信下发任务Model")
public class SmsTaskModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "模板id")
    @NotEmpty(message = "模板id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "模板id")
    private String templateId;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "对应短信提供商的模板code")
    private String templateCode;

    @ColumnHeader(title = "业务类型: 1、短信下发; 2、终端短信唤醒")
    @NotNull(message = "业务类型: 1、短信下发; 2、终端短信唤醒不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "业务类型: 1、短信下发; 2、终端短信唤醒")
    private Integer serviceType;

    @ApiModelProperty(value = "任务状态: 1发送, 2未发送(草搞)")
    private Integer status;

    @DictName(joinField = "status", code = "BOOL_TYPE")
    private String statusDisplay;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "发送人")
    private String createBy;

    @ApiModelProperty(value = "发送人")
    private String createByName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改id")
    private String updateBy;

    @ApiModelProperty(value = "修改人")
    private String updateByName;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "接收人id,多个用,分开")
    private String receiverIds;

    @ApiModelProperty(value = "变量内容")
    private List<FieldModel> variables = new ArrayList<>();

    @ApiModelProperty(value = "联系人类型,　1、单位联系人, 2、个人车主, 3、车辆负责人")
    private Integer receiverType;

    /** 短信内容 **/
    @ApiModelProperty(value = "草稿-短信内容")
    private String smsContent;

    @ApiModelProperty(value = "草稿-收件人")
    private List<SmsTaskItem> items = new ArrayList<>();

    @ApiModelProperty(value = "联系人名称")
    private String queryName;

    @ApiModelProperty(value = "联系人电话")
    private String queryPhone;

    @ApiModelProperty(value = "统计")
    private String statistic;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SmsTaskModel fromEntry(SmsTask entry){
        SmsTaskModel m = new SmsTaskModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
