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
* 功能： UppackageSend实体<br>
* 描述： UppackageSend实体<br>
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
* <td>2019-03-05 14:50:32</td>
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
public class UppackageSend extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 任务名称 **/
    private String taskName;
    /** 1,强制升级  2、普通升级 **/
    private Integer schemaType;
    /** 任务包id **/
    private String uppackageId;
    /** 文件名称 **/
    private String fileName;
    /** 任务描述 **/
    private String describes;
    /** 0: 未开始、1: 进行中、2: 已结束 **/
    private Integer uppackageSendStatus;
    /** 创建用户id **/
    private String createById;
    /** 创建用户 **/
    private String createBy;
    /** 创建时间 **/
    private String createTime;
    /** 协议类型 **/
    private Integer protocolType;
    /** 指令缓存时间 **/
    private Integer instructCacheTime;
    /** 任务记录状态 0:正常 1:已删除 **/
    private Integer recordStatus;
}
