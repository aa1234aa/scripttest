package com.bitnei.cloud.veh.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRecord实体<br>
* 描述： DataCheckRecord实体<br>
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
* <td>2019-09-17 14:11:42</td>
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
public class DataCheckRecord extends TailBean {

    /** 主键 **/
    private String id;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 车架号 **/
    private String vin;
    /** 检测结果 0:未通过,  1:已通过 **/
    private Integer checkResult;
    /** 异常原因 **/
    private String reason;
    /** 检测时间范围起 **/
    private String checkTimeBg;
    /** 检测时间范围止 **/
    private String checkTimeEd;
    /** 车辆登入报文数 **/
    private Integer loginPacketNum;
    /** 车辆登出报文数 **/
    private Integer logoutPacketNum;
    /** 实时上报信息报文数 **/
    private Integer realStatusPacketNum;
    /** 需上传报文条数 **/
    private Integer needUploadNum;
    /** 实际上传报文条数 **/
    private Integer realUploadNum;
    /** 丢包率阈值 **/
    private Double packetLossRate;
    /** 实际丢包率 **/
    private Double packetRealLossRate;
    /** 丢包率检测结果 0:未通过，1:已通过 **/
    private Integer lossCheckResult;
    /** 数据项检测报文数 **/
    private Integer dataPacketNum;
    /** 数据项检测异常报文数 **/
    private Integer dataExceptionNum;
    /** 数据项异常阈值 **/
    private Double dataItemException;
    /** 数据项实际异常比例 **/
    private Double dataRealException;
    /** 数据项异常检测结果 0:未通过，1:已通过 **/
    private Integer dataExceptionResult;
    /** 报文类型检测结果 0:未通过，1:已通过 **/
    private Integer packetTypeCheckResult;

    /** 报文类型检测结果 车型id **/
    private String vehModelId;

    /** 报文类型检测结果 车型名称 **/
    private String vehModelName;

}
