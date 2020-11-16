package com.bitnei.cloud.sys.domain;

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
* 功能： TermParamRecord实体<br>
* 描述： TermParamRecord实体<br>
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
public class TermParamRecord extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 车牌号 **/
    private String licensePlate;
    /** Vin **/
    private String vin;
    /** 发送时间 **/
    private String sendTime;
    /** 接收时间 **/
    private String responseTime;
    /** 终端编码 **/
    private String termCode;
    /** 参数值json **/
    private String paramValues;
    /** 10: 成功  20： 错误： 30 ：超时 **/
    private Integer state;
    /** 描述 **/
    private String describes;
    /** 修改人 **/
    private String updateUser;
    /** 修改时间 **/
    private String updateTime;
    /** 1 在线 2 离线 **/
    private Integer historyOnlineState;
    /** 车辆种类 **/
    private String vehicleTypeValue;
    /** 运营区域 **/
    private String operatingArea;
    /** 运营单位 **/
    private String operatingUnit;
    /** kafka反馈状态 **/
    private Integer receiveState;

    /** 车辆型号id **/
    private String vehModelId;

    /** 车辆型号 **/
    private String vehModelName;

    /** 内部编码 **/
    private String interNo;

    /** 命令名称 **/
    private String operation;

    /** 失败原因 **/
    private String errorMessage;
}
