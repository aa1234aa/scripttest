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
* 功能： DataItemCheckResult实体<br>
* 描述： DataItemCheckResult实体<br>
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
* <td>2019-09-17 18:35:18</td>
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
public class DataItemCheckResult extends TailBean {

    /** 主键 **/
    private String id;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 车架号 **/
    private String vin;
    /** 车辆数据检测记录id **/
    private String checkRecordId;
    /** 数据项编码 **/
    private String seqNo;
    /** 数据项名称 **/
    private String dataItemName;
    /** 数据项检测报文数 **/
    private Integer itemPacketNum;
    /** 数据项检测异常报文数 **/
    private Integer itemExceptionNum;
    /** 数据项异常阈值 **/
    private Double dataItemException;
    /** 数据项实际异常比例 **/
    private Double itemRealException;
    /** 数据项异常检测结果 **/
    private Integer itemExceptionResult;

}
