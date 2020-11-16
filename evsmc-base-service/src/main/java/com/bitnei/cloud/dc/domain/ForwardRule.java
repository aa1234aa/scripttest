package com.bitnei.cloud.dc.domain;

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
* 功能： ForwardRule实体<br>
* 描述： ForwardRule实体<br>
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
* <td>2019-02-20 10:32:15</td>
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
public class ForwardRule extends TailBean {

    /** 主键 **/
    private String id;
    /** 名称 **/
    private String name;
    /** 启用状态 **/
    private Integer status;
    /** 备注 **/
    private String note;
    /** 创建时间 **/
    private String createTime;
    /** 更新时间 **/
    private String updateTime;
    /** 创建人 **/
    private String createBy;
    /** 更新人 **/
    private String updateBy;
    /** 规则类型 **/
    private Integer ruleType;

    /** 规则描述 **/
    private String ruleDescription;

    /** 默认规则 **/
    private Integer defaultRule;

}