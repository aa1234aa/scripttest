package com.bitnei.cloud.fault.domain;

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
* 功能： IncidentHandling实体<br>
* 描述： IncidentHandling实体<br>
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
* <td>2019-07-03 16:32:41</td>
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
public class IncidentHandling extends TailBean {

    /** id **/
    private String id;
    /** 车型id **/
    private String vehModelId;
    /** id **/
    private String documentName;
    /** 车辆配置名称 **/
    private String configName;
    /** 文档类型 **/
    private Integer documentType;
    /** 创建时间 **/
    private String createTime;
    /** 上报状态 **/
    private Integer reportState;
    /** 失败原因 **/
    private String reasonsForFailure;
    /** 上报时间 **/
    private String reportTime;
    /** 上报平台 **/
    private Integer platform;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 主要部件信息 **/
    private String componentInformation;
    /** 救援行动 **/
    private String rescue;
    /** 打开车辆 **/
    private String openVehicle;
    /** 禁止行为 **/
    private String prohibitoryActs ;
    /** 附件id **/
    private String fileId;
    /** 文档描述 **/
    private String documentDescribe;
}
