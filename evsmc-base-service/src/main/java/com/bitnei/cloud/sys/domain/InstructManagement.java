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
* 功能： InstructManagement实体<br>
* 描述： InstructManagement实体<br>
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
* <td>2019-03-11 15:53:11</td>
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
public class InstructManagement extends TailBean {

    /** 主键标识 **/
    private String id;
    /** 控制命令名称 **/
    private String name;
    /** 控制命令种类ID **/
    private String instructCategoryId;
    /** 参数数据 **/
    private String paramData;
    /** 操作密码 **/
    private String passwd;
    /** 控制命令状态：0 禁用，1 启用。 **/
    private Integer status;
    /** 控制命令描述 **/
    private String description;
    /** 创建人 **/
    private String createBy;
    /** 创建人id **/
    private String createById;
    /** 创建时间 **/
    private String createTime;
    /** 更新人 **/
    private String updateBy;
    /** 更新时间 **/
    private String updateTime;

    //适用车型id列表字符串，','号分割
    private String vehModelIdStrs;

}
