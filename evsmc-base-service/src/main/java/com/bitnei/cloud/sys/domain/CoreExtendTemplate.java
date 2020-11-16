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
* 功能： CoreExtendTemplate实体<br>
* 描述： CoreExtendTemplate实体<br>
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
* <td>2019-07-31 14:07:09</td>
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
public class CoreExtendTemplate extends TailBean {

    /** 主键 **/
    private String id;
    /** 模块名称 **/
    private String name;
    /** 表名 **/
    private String tableName;
    /** 属性名 **/
    private String fieldName;
    /** 组标识，多个用空格分隔 **/
    private String groupTag;
    /** 1: 文本
    2: 下拉框(单选)
    3: 下拉框(多选)
    4: 日期
    5: 时间
    6: 图片
    7: 文件
     **/
    private Integer dataType;
    /** 校验规则(正则表达式) **/
    private String validateRule;
    /** 校验信息 **/
    private String validateMessage;
    /** 是否可编辑 **/
    private Integer updateAble;
    /** 是否可新增 **/
    private Integer insertAble;
    /** 是否在列表显示 **/
    private Integer listAble;
    /** 其他参数选项 **/
    private String options;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

}
