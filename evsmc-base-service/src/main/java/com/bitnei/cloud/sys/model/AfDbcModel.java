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

import com.bitnei.cloud.sys.domain.AfDbc;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.math.BigDecimal;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AfDbc新增模型<br>
* 描述： AfDbc新增模型<br>
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
* <td>2019-03-04 17:10:36</td>
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
@ApiModel(value = "AfDbcModel", description = "DBC文件管理Model")
public class AfDbcModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "类型：0DBC，1CAN,2ECU,3CAN报文，4信号")
    @NotEmpty(message = "类型：0DBC，1CAN,2ECU,3CAN报文，4信号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "类型：0DBC，1CAN,2ECU,3CAN报文，4信号")
    private BigDecimal type;

    @ColumnHeader(title = "父子结构")
    @NotEmpty(message = "父子结构不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "父子结构")
    private String path;

    @ColumnHeader(title = "报文-1标准帧，2扩展帧，3J1939")
    @NotEmpty(message = "报文-1标准帧，2扩展帧，3J1939不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报文-1标准帧，2扩展帧，3J1939")
    private BigDecimal canRadio;

    @ColumnHeader(title = "报文-报文ID")
    @NotEmpty(message = "报文-报文ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报文-报文ID")
    private String canReportId;

    @ColumnHeader(title = "报文-字节数")
    @NotEmpty(message = "报文-字节数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报文-字节数")
    private BigDecimal canByteNum;

    @ColumnHeader(title = "CAN信号-开始位")
    @NotEmpty(message = "CAN信号-开始位不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-开始位")
    private BigDecimal signalBeginPosition;

    @ColumnHeader(title = "CAN信号-位数量")
    @NotEmpty(message = "CAN信号-位数量不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-位数量")
    private BigDecimal signalPositionNum;

    @ColumnHeader(title = "CAN信号-1Intel0Motorola")
    @NotEmpty(message = "CAN信号-1Intel0Motorola不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-1Intel0Motorola")
    private BigDecimal signalByteOrder;

    @ColumnHeader(title = "CAN信号-1Unsigned2bit3byte4word5dword6string")
    @NotEmpty(message = "CAN信号-1Unsigned2bit3byte4word5dword6string不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-1Unsigned2bit3byte4word5dword6string")
    private BigDecimal signalDataType;

    @ColumnHeader(title = "SIGNAL_GAIN")
    @NotEmpty(message = "SIGNAL_GAIN不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "SIGNAL_GAIN")
    private BigDecimal signalGain;

    @ColumnHeader(title = "SIGNAL_BIAS")
    @NotEmpty(message = "SIGNAL_BIAS不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "SIGNAL_BIAS")
    private BigDecimal signalBias;

    @ColumnHeader(title = "SIGNAL_MIN_VALUE")
    @NotEmpty(message = "SIGNAL_MIN_VALUE不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "SIGNAL_MIN_VALUE")
    private BigDecimal signalMinValue;

    @ColumnHeader(title = "SIGNAL_MAX_VALUE")
    @NotEmpty(message = "SIGNAL_MAX_VALUE不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "SIGNAL_MAX_VALUE")
    private BigDecimal signalMaxValue;

    @ColumnHeader(title = "CAN信号-默认值")
    @NotEmpty(message = "CAN信号-默认值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-默认值")
    private BigDecimal signalDefaultValue;

    @ColumnHeader(title = "CAN信号-单位")
    @NotEmpty(message = "CAN信号-单位不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-单位")
    private String signalUnit;

    @ColumnHeader(title = "CAN信号-通道属性1触发条件2无线发送通道3两者都有")
    @NotEmpty(message = "CAN信号-通道属性1触发条件2无线发送通道3两者都有不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-通道属性1触发条件2无线发送通道3两者都有")
    private BigDecimal signalChannelQualit;

    @ColumnHeader(title = "CAN信号-名称ID")
    @NotEmpty(message = "CAN信号-名称ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "CAN信号-名称ID")
    private String signalNameId;

    @ColumnHeader(title = "状态：1有效0无效")
    @NotEmpty(message = "状态：1有效0无效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "状态：1有效0无效")
    private BigDecimal state;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ColumnHeader(title = "操作人id")
    @NotEmpty(message = "操作人id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "操作人id")
    private String operatorId;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String dbcRemark;

    @ColumnHeader(title = "父ID")
    @NotEmpty(message = "父ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ColumnHeader(title = "数据来源：1页面录入，2dbc导入")
    @NotEmpty(message = "数据来源：1页面录入，2dbc导入不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据来源：1页面录入，2dbc导入")
    private BigDecimal dataSource;

    @ColumnHeader(title = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    @NotEmpty(message = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    private BigDecimal baudRate1;

    @ColumnHeader(title = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    @NotEmpty(message = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    private BigDecimal baudRate2;

    @ColumnHeader(title = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    @NotEmpty(message = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用")
    private BigDecimal baudRate3;

    @ColumnHeader(title = "编辑人员")
    @NotEmpty(message = "编辑人员不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "编辑人员")
    private String editName;

    @ColumnHeader(title = "车辆类型")
    @NotEmpty(message = "车辆类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆类型")
    private String vehModelId;

    @ColumnHeader(title = "文件MD5值")
    @NotEmpty(message = "文件MD5值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件MD5值")
    private String md5code;

    @ColumnHeader(title = "序号-数据项对应")
    @NotNull(message = "序号-数据项对应不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "序号-数据项对应")
    private Integer serialNum;

    @ColumnHeader(title = "协议数据项id")
    @NotEmpty(message = "协议数据项id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议数据项id")
    private String dataItemId;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static AfDbcModel fromEntry(AfDbc entry){
        AfDbcModel m = new AfDbcModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
