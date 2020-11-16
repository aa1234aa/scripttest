package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreResourceItem实体<br>
* 描述： CoreResourceItem实体<br>
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
* <td>2018-11-05 09:34:18</td>
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
@Setter
@Getter
public class CoreResourceItem extends TailBean {

    /** 主键 **/
    private String id;
    /** 名称 **/
    private String name;
    /** 资源类型id **/
    private String resouceId;
    /** 前缀编码 **/
    private String preCode;
    /** 路径 **/
    private String path;
    /** 上一级 **/
    private String parentId;
    /**  列名 **/
    private String columnName;
    /**  查询表名 **/
    private String queryTableName;
    /**  查询SQL **/
    private String querySql;
    /** 对象表 **/
    private String objectTableName;
    /** 匹配列名 **/
    private String objectColumnName;
    /** 唯一列键名**/
    private String identifyColumnName;
    /** 唯一列名称**/
    private String identifyColumnLabel;
    /** 关联选择url **/
    private String chooseDialogUrl;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;


}
