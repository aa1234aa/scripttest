package com.bitnei.cloud.dc.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemGroup实体<br>
* 描述： DataItemGroup实体<br>
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
* <td>2019-01-30 11:25:14</td>
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
public class DataItemGroup extends TailBean {

    /** 主键 **/
    private String id;
    /** 组名 **/
    private String name;
    /** 协议类型ID **/
    private String ruleTypeId;
    /** 组编码 **/
    private String code;
    /** 备注 **/
    private String note;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 上一级id **/
    private String parentId;
    /** 树路径 **/
    private String path;
    /** 路径名称 **/
    private String pathName;
    /** 协议类型名称 **/
    private String ruleTypeDisplay;
    /** 端口号 **/
    private String port;

    /** 是否国标: 0：否 1：是 **/
    private Integer isNationalStandard;

}
