package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;
import java.lang.Double;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Area实体<br>
* 描述： Area实体<br>
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
* <td>2018-12-27 09:27:18</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Area extends TailBean {

    /** 主键 **/
    private String id;
    private List<String> idPath;
    /** 名称 **/
    private String name;
    /** 名称路径 **/
    private List<String> namePath;
    /** 父节点 **/
    private String parentId;
    /** 是否根节点 **/
    private String isRoot;
    /** 路径 **/
    private String path;
    /** 层级 **/
    private Integer levels;
    /** 经度 **/
    private Double lng;
    /** 纬度 **/
    private Double lat;
    /** 区域编码 **/
    private String code;
    /** 区域编码路径 **/
    private List<String> codePath;
    /** 车牌前缀 **/
    private String platPrefix;
    /** 序号 **/
    private Integer orderNum;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

}
