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
* 功能： InstructTask实体<br>
* 描述： InstructTask实体<br>
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
* <td>2019-03-07 10:26:27</td>
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
public class InstructTask extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 指令记录id **/
    private String instructId;
    /** 指令类型：1远程控制，2远程升级 **/
    private Integer instructType;
    /** 指令任务有效时间 **/
    private Integer effectiveTime;
    /** 指令任务时间单位：1天数2小时数3分钟 **/
    private Integer effectiveTimeType;
    /** 任务已执行次数 **/
    private Integer executedNum;
    /** 任务最大执行次数 **/
    private Integer executeMaxNum;
    /** 任务执行结果 **/
    private String taskResult;
    /** 有效状态：1有效0无效 **/
    private Integer status;
    /** 创建时间 **/
    private String createTime;
    /** 修改时间 **/
    private String updateTime;
    /** 上次执行时间 **/
    private String executeTime;


    /**
     * 协议类型：1：国标，2：CodeValue，128：自定义，99：自定义指令
     */
    private Integer protocolType;
    /**
     * 升级模式：1：强制升级、2：普通升级
     */
    private Integer schemaType;
    /**
     * 升级包id
     */
    private String uppackageId;
    /**
     * 车辆vin
     */
    private String vin;
    /**
     * 终端控制-控制命令
     */
    private String standardCode;
    /**
     * 终端控制-报警等级
     */
    private Integer alarmLevel;

}
