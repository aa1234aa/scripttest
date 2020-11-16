package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermParamDic实体<br>
* 描述： TermParamDic实体<br>
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
* <td>2019-03-07 15:39:13</td>
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
public class TermParamDic extends TailBean {

    /** 主键标识 **/
    private String id;
    /** 名称 **/
    private String name;
    /** 编码 **/
    private String code;
    /** 设置id **/
    private String setupCode;
    /** 接收id **/
    private String receiveCode;
    /** 数据长度 **/
    private Integer dataSize;
    /** 数据类型 **/
    private String dataType;
    /** 0： 禁用：1，启用 **/
    private Integer state;
    /** 描述 **/
    private String describes;
    /** 添加人 **/
    private String createBy;
    /** 添加时间 **/
    private String createTime;
    /** 排序 **/
    private Integer sequence;
    /** 1,可设置 0，警用 **/
    private Integer isSetup;
    /** 1：国标, 2,自定义 **/
    private Integer paramType;
    /** 下限 **/
    private Integer lowerLimit;
    /** 上限 **/
    private Integer upperLimit;
    /** 单位 **/
    private String unit;
}
