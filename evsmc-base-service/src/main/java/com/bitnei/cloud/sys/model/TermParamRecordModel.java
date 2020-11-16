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

import com.bitnei.cloud.sys.domain.TermParamRecord;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermParamRecord新增模型<br>
* 描述： TermParamRecord新增模型<br>
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
* <td>2019-03-07 15:28:19</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TermParamRecordModel", description = "终端参数记录Model")
public class TermParamRecordModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "车牌号")
    @NotEmpty(message = "车牌号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "Vin")
    @NotEmpty(message = "Vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "Vin")
    private String vin;

    @ColumnHeader(title = "发送时间")
    @NotEmpty(message = "发送时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "发送时间")
    private String sendTime;

    @ColumnHeader(title = "接收时间")
    @NotEmpty(message = "接收时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接收时间")
    private String responseTime;

    @ColumnHeader(title = "终端编码")
    @NotEmpty(message = "终端编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "终端编码")
    private String termCode;

    @ColumnHeader(title = "参数值json")
    @NotEmpty(message = "参数值json不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "参数值json")
    private String paramValues;

    @ColumnHeader(title = "10: 成功  20： 错误： 30 ：超时")
    @NotNull(message = "10: 成功  20： 错误： 30 ：超时不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "10: 成功  20： 错误： 30 ：超时")
    private Integer state;

    @DictName(code = "PARAM_RECORD_STATE", joinField = "state")
    private String stateName;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String describes;

    @ColumnHeader(title = "修改人")
    @NotEmpty(message = "修改人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "修改人")
    private String updateUser;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ColumnHeader(title = "1 在线 2 离线")
    @NotNull(message = "1 在线 2 离线不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "1 在线 2 离线")
    private Integer historyOnlineState;

    @DictName(code = "ONLINE_STATUS", joinField = "historyOnlineState")
    private String historyOnlineStateName;

    @ColumnHeader(title = "车辆种类")
    @NotEmpty(message = "车辆种类不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆种类")
    private String vehicleTypeValue;

    @ColumnHeader(title = "运营区域")
    @NotEmpty(message = "运营区域不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营区域")
    private String operatingArea;

    @ColumnHeader(title = "运营单位")
    @NotEmpty(message = "运营单位不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营单位")
    private String operatingUnit;

    @ColumnHeader(title = "kafka反馈状态")
    @NotNull(message = "kafka反馈状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "kafka反馈状态")
    private Integer receiveState;

    @DictName(code = "PARAM_RECEIVE_STATE", joinField = "receiveState")
    private String receiveStateName;

    @ColumnHeader(title = "车辆型号id")
    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ColumnHeader(title = "车辆型号")
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ColumnHeader(title = "内部编码")
    @ApiModelProperty(value = "内部编码")
    private String interNo;

    @ColumnHeader(title = "命令名称")
    @ApiModelProperty(value = "命令名称")
    private String operation;

    @ColumnHeader(title = "失败原因")
    @ApiModelProperty(value = "失败原因")
    private String errorMessage;

    public interface State{
        /** 成功 */
        int success = 10;
        /** 错误 */
        int error = 20;
        /** 超时 */
        int timeout = 30;
    }

    public interface ReceiveState{
        /** 成功 */
        int success = 1;
        /** 等待 */
        int wait = 0;
    }

    public interface HistoryOnlinState{
        /** 在线 */
        int online = 1;
        /** 离线 */
        int offline = 2;
    }

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static TermParamRecordModel fromEntry(TermParamRecord entry){
        TermParamRecordModel m = new TermParamRecordModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
