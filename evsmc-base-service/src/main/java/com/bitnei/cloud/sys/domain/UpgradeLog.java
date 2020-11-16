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
* 功能： UpgradeLog实体<br>
* 描述： UpgradeLog实体<br>
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
* <td>2019-03-09 09:56:09</td>
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
public class UpgradeLog extends TailBean {

    /** 主键标识 **/
    private String id;
    /** 操作类型(10 上传升级包、20 编辑升级包、30 删除升级包、40 创建任务、50 删除任务、60 开始任务、70 开始车辆升级、80 终止车辆升级、90 强制终止车辆升级) **/
    private Integer action;
    /** 任务名称 **/
    private String taskName;
    /** 操作车辆 **/
    private String licensePlate;
    /** 操作描述 **/
    private String description;
    /** 操作人 **/
    private String createById;
    /** 操作姓名 **/
    private String createBy;
    /** 操作时间 **/
    private String createTime;
    /** 备注 **/
    private String remarks;

}
