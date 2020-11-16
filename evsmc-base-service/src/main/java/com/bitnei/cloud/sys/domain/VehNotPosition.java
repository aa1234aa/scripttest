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

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotPosition实体<br>
* 描述： VehNotPosition实体<br>
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
* <td>2019-03-05 11:24:01</td>
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
public class VehNotPosition extends TailBean {

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
    /** 是否未定位：1是0否 **/
    private Integer state;
    /** 异常情况说明 **/
    private String remark;

    /** 定位异常天数 **/
    private String days;
    /** 定位异常秒数 **/
    private String seconds;
    /** 定位异常开始时间 **/
    private String showLastUploadTime;
    /** 恢复定位时间 **/
    private String showRegainUploadTime;
}
