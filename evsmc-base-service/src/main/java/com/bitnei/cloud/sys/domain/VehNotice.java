package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotice实体<br>
* 描述： VehNotice实体<br>
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
* <td>2018-11-12 14:50:08</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Setter
@Getter
public class VehNotice extends TailBean {

    /** 主键 **/
    private String id;
    /** 车辆公告号 **/
    private String name;
    /** 车辆种类id **/
    private String vehTypeId;
    /** 车辆品牌id **/
    private String brandId;
    /** 车系id **/
    private String seriesId;
    /** 公告目录年份 **/
    private Integer noticeYear;
    /** 公告目录批次 **/
    private Integer noticeBatch;
    /** 推荐目录年份 **/
    private Integer recommendYear;
    /** 推荐目录批次 **/
    private Integer recommendBatch;
    /** 发布日期，格式yyyy-MM-dd **/
    private String publishDate;
    /** 是否免征 **/
    private Integer isExempt;
    /** 是否燃油 **/
    private Integer isFuel;
    /** 是否环保 **/
    private Integer isProtection;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;



}
