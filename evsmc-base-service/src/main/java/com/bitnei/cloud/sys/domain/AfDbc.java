package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AfDbc实体<br>
 * 描述： AfDbc实体<br>
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
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AfDbc extends TailBean {

    /**
     * 唯一标识
     **/
    private String id;
    /**
     * 名称
     **/
    private String name;
    /**
     * 类型：0DBC，1CAN,2ECU,3CAN报文，4信号
     **/
    private BigDecimal type;
    /**
     * 父子结构
     **/
    private String path;
    /**
     * 报文-1标准帧，2扩展帧，3J1939
     **/
    private BigDecimal canRadio;
    /**
     * 报文-报文ID
     **/
    private String canReportId;
    /**
     * 报文-字节数
     **/
    private BigDecimal canByteNum;
    /**
     * CAN信号-开始位
     **/
    private BigDecimal signalBeginPosition;
    /**
     * CAN信号-位数量
     **/
    private BigDecimal signalPositionNum;
    /**
     * CAN信号-1Intel0Motorola
     **/
    private BigDecimal signalByteOrder;
    /**
     * CAN信号-1Unsigned2bit3byte4word5dword6string
     **/
    private BigDecimal signalDataType;
    /**
     * SIGNAL_GAIN
     **/
    private BigDecimal signalGain;
    /**
     * SIGNAL_BIAS
     **/
    private BigDecimal signalBias;
    /**
     * SIGNAL_MIN_VALUE
     **/
    private BigDecimal signalMinValue;
    /**
     * SIGNAL_MAX_VALUE
     **/
    private BigDecimal signalMaxValue;
    /**
     * CAN信号-默认值
     **/
    private BigDecimal signalDefaultValue;
    /**
     * CAN信号-单位
     **/
    private String signalUnit;
    /**
     * CAN信号-通道属性1触发条件2无线发送通道3两者都有
     **/
    private BigDecimal signalChannelQualit;
    /**
     * CAN信号-名称ID
     **/
    private String signalNameId;
    /**
     * 状态：1有效0无效
     **/
    private BigDecimal state;
    /**
     * 创建时间
     **/
    private String createTime;
    /**
     * 修改时间
     **/
    private String updateTime;
    /**
     * 操作人id
     **/
    private String operatorId;
    /**
     * 备注
     **/
    private String dbcRemark;
    /**
     * 父ID
     **/
    private String parentId;
    /**
     * 数据来源：1页面录入，2dbc导入
     **/
    private BigDecimal dataSource;
    /**
     * 波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用
     **/
    private BigDecimal baudRate1;
    /**
     * 波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用
     **/
    private BigDecimal baudRate2;
    /**
     * 波特率“：1-50k；2-100k；3-125k；4-250k；5-500；6-1000k；0不可用
     **/
    private BigDecimal baudRate3;
    /**
     * 编辑人员
     **/
    private String editName;
    /**
     * 车辆类型
     **/
    private String vehModelId;
    /**
     * 文件MD5值
     **/
    private String md5code;
    /**
     * 序号-数据项对应
     **/
    private Integer serialNum;
    /**
     * 协议数据项id
     **/
    private String dataItemId;

}
