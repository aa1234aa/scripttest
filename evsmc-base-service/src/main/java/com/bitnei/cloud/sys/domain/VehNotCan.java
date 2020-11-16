package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Float;
import java.util.Date;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotCan实体<br>
* 描述： VehNotCan实体<br>
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
* <td>2019-03-02 13:08:14</td>
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
public class VehNotCan extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 车辆id **/
    private String vehId;
    /** 车辆标识uuid **/
    private String vehUuid;
    /** can最后上传时间 **/
    private Timestamp lastUploadTime;
    /** can恢复上传时间 **/
    private Timestamp regainUploadTime;
    /** 更新时间 **/
    private Timestamp updateTime;
    /** 创建时间 **/
    private Timestamp createTime;
    /** 是否无can上传：0是1否 **/
    private Integer state;
    /** 异常结束时里程，单位公里 **/
    private Double regainUploadMileage;
    /** 异常开始时里程，单位公里 **/
    private Double lastUploadMileage;
    /** 时间阈值,单位毫秒 **/
    private Float timeThreshold;

    /** 无CAN天数 **/
    private String days;
    /** 无CAN秒数 **/
    private String seconds;
    /** can最后上传时间 **/
    private String showLastUploadTime;
    /** can恢复上传时间 **/
    private String showRegainUploadTime;
}
