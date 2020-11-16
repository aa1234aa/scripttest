package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.ExtendTable;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreExtendValue实体<br>
* 描述： CoreExtendValue实体<br>
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
* <td>2019-07-31 15:08:40</td>
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
public class CoreExtendValue extends TailBean {

    /** 主键 **/
    private String id;
    /** 对应id值 **/
    private String idVal;
    /** 属性值,Json字符串 **/
    private String jsonValue;
    /** 表名 **/
    private String tableName;



}
