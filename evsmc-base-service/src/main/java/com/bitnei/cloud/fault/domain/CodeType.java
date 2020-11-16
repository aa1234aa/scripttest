package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeType实体<br>
* 描述： CodeType实体<br>
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
* <td>2019-02-25 10:22:15</td>
* <td>hzr</td>
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
public class CodeType extends TailBean {

    /** 故障码类型 **/
    private String id;
    /** 故障种类名称 **/
    private String name;
    /** 协议类型, 引用 dc_rule_type表的id **/
    private String dcRuleTypeId;
    /** 类型编码 **/
    private String typeCode;
    /** 故障码个数 **/
    private Integer faultNumber;
    /** 故障种类唯一标识编码 **/
    private String lenCode;
    /** 备注 **/
    private String remark;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 修改时间 **/
    private String updateTime;
    /** 修改人 **/
    private String updateBy;

}