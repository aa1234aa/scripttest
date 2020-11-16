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
* 功能： DataItem实体<br>
* 描述： DataItem实体<br>
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
* <td>2019-01-30 17:28:53</td>
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
public class DataItem extends TailBean {

    /** 主键 **/
    private String id;
    /** 名称 **/
    private String name;
    /** 编号 **/
    private String seqNo;
    /** 数据项组ID **/
    private String itemGroupId;
    /** 数据类别 **/
    private Integer itemType;
    /** 开始Byte位置 **/
    private Integer byteStartPos;
    /** 开始Bit位置 **/
    private Integer bitStartPos;
    /** Bit位数 **/
    private Integer length;
    /** 高低位 **/
    private Integer highLowFlg;
    /** 系数 **/
    private String factor;
    /** 偏移量 **/
    private String offset;
    /** 单位 **/
    private String unit;
    /** 精确度 **/
    private Integer dot;
    /** 序号 **/
    private Integer orderNum;
    /** 备注 **/
    private String note;
    /** 上限值 **/
    private String upperLimit;
    /** 下限值 **/
    private String lowerLimit;
    /** 数据类型 **/
    private Integer dataType;
    /** 是否数值 **/
    private Integer isnum;
    /** 是否显示 **/
    private Integer isShow;
    /** 是否监控项 **/
    private Integer isMonitor;
    /** 是否数组 **/
    private Integer isArray;
    /** 是否启用解析规则 **/
    private Integer enableParseRule;
    /** 是否自定义 **/
    private Integer isCustom;
    /** 是否生效 **/
    private Integer isValid;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 上级id **/
    private String parentId;
    /** 协议类型ID **/
    private String ruleTypeId;
    /** 是否数据项 **/
    private String isItem;

    private int index;

}
