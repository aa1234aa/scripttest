package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.Integer;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreTag实体<br>
* 描述： CoreTag实体<br>
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
* <td>2019-03-25 15:57:04</td>
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
public class CoreTag extends TailBean {

    /** 主键 **/
    private Integer id;
    /** 表名 **/
    private String tableName;
    /**  id值 **/
    private String idValue;
    /** 我的标签列表 **/
    private String myTags;
    /** 谁能看到我 **/
    private String whoCanSeeMe;
    /** 更新时间 **/
    private String updateTime;
    /** 标签ID **/
    private String tagId;

}
