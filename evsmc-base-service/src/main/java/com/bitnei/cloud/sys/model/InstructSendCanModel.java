package com.bitnei.cloud.sys.model;

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

import com.bitnei.cloud.sys.domain.InstructSendCan;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructSendCan新增模型<br>
* 描述： InstructSendCan新增模型<br>
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
* <td>2019-03-15 17:08:14</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "InstructSendCanModel", description = "CAN自定义远程控制任务Model")
public class InstructSendCanModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "车辆vin")
    @NotEmpty(message = "车辆vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆vin")
    private String vin;

    @ColumnHeader(title = "车牌")
    @NotEmpty(message = "车牌不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    @ColumnHeader(title = "命令名称")
    @NotEmpty(message = "命令名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "命令名称")
    private String instructName;

    @ColumnHeader(title = "指令id")
    @NotEmpty(message = "指令id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令id")
    private String instructId;

    @ColumnHeader(title = "添加人")
    @NotEmpty(message = "添加人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "添加人")
    private String createById;

    @ApiModelProperty(value = "添加人姓名")
    private String createBy;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ColumnHeader(title = "发送结果")
    @NotNull(message = "发送结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "发送结果")
    private Integer sendResult;

    @DictName(code = "SEND_RULE_RESULT", joinField = "sendResult")
    private String sendResultName;

    @ColumnHeader(title = "执行结果")
    @NotNull(message = "执行结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "执行结果")
    private Integer executeResult;

    @ColumnHeader(title = "运营区域")
    @NotEmpty(message = "运营区域不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营区域")
    private String operatingArea;

    @ColumnHeader(title = "运营单位")
    @NotEmpty(message = "运营单位不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营单位")
    private String operatingUnit;

    @ColumnHeader(title = "运营负责人")
    @NotEmpty(message = "运营负责人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营负责人")
    private String operationUserName;

    @ColumnHeader(title = "联系方式")
    @NotEmpty(message = "联系方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "联系方式")
    private String operationPhone;

    @ColumnHeader(title = "运营负责人id")
    @NotEmpty(message = "运营负责人id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营负责人id")
    private String operationUserId;

    @ColumnHeader(title = "会话标识")
    @NotEmpty(message = "会话标识不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "会话标识")
    private String sessionId;

    @ColumnHeader(title = "应答id及数据——储存格式：  id: 数据,id:数据,id:数据,....")
    @NotEmpty(message = "应答id及数据——储存格式：  id: 数据,id:数据,id:数据,....不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "应答id及数据——储存格式：  id: 数据,id:数据,id:数据,....")
    private String responseIdData;

    @ColumnHeader(title = "响应时间")
    @NotEmpty(message = "响应时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应时间")
    private String responseTime;

    @ColumnHeader(title = "历史在线状态")
    @ApiModelProperty(value = "历史在线状态")
    private Integer historyOnlineState;

    @DictName(code = "ONLINE_STATUS", joinField = "historyOnlineState")
    private String historyOnlineStateName;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ColumnHeader(title = "命令执行结果(失败原因)")
    @NotEmpty(message = "命令执行结果(失败原因)不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "命令执行结果(失败原因)")
    private String instructRemark;

    @ColumnHeader(title = "内部编码")
    @ApiModelProperty(value = "内部编码")
    private String interNo;

    @ColumnHeader(title = "车辆型号")
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static InstructSendCanModel fromEntry(InstructSendCan entry){
        InstructSendCanModel m = new InstructSendCanModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
