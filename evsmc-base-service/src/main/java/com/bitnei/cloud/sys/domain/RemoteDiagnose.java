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
* 功能： RemoteDiagnose实体<br>
* 描述： RemoteDiagnose实体<br>
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
* <td>2019-03-25 17:01:37</td>
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
public class RemoteDiagnose extends TailBean {

    /** 主键标识 **/
    private String id;
    /** vin **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 车辆型号名称 **/
    private String vehModelName;
    /** 诊断方式，0历史诊断，1实时诊断 **/
    private Integer diagnosticMode;
    /** 诊断时间 **/
    private String diagnositicTime;
    /** 诊断结果，0正常，1失败，2异常 **/
    private Integer resultState;
    /** 创建人 **/
    private String createBy;
    /** 创建时间 **/
    private String createTime;
    /** 创建人id **/
    private String createById;

    /** 内部编号 **/
    private String interNo;
    /** 诊断明细id **/
    private String detailId;
}
