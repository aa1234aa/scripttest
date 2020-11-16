package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierSetting实体<br>
* 描述： NotifierSetting实体<br>
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
* <td>2019-03-06 11:31:31</td>
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
public class NotifierSetting extends TailBean {

    /** 故障类型:安全风险 **/
    public static final String FAULT_TYPE_RISK = "4";

    /** 主键 **/
    private String id;
    /** 故障类型：1=平台, 2=故障码, 3=电子围栏 4=安全风险 **/
    private String faultType;
    /** 姓名 **/
    private String name;
    /** 电话 **/
    private String mobile;
    /** 区域id 多个以,形式分开 **/
    private String areaIds;
    /** 区域名称 多个以,形式分开 **/
    private String areaNames;
    /** 故障等：1：一级；2：2级； 3：三级 **/
    private String alarmLevel;
    /** 是否关联平台账号 1：是；0：否； 默认：是 **/
    private Integer relationUserStatus;
    /** 平台账号id **/
    private String userId;
    /** 启用状态 1：是；0：否； 默认：是 **/
    private Integer enabledStatus;
    /** 是否同步账号下车辆 1：是；0：否； 默认：是**/
    private Integer syncVehicleStatus;
    /** 备注 **/
    private String remark;
    /** 创建人 **/
    private String createBy;
    /** 创建时间 **/
    private String createTime;
    /** 修改人 **/
    private String updateBy;
    /** 修改时间 **/
    private String updateTime;

}
