package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructSendRule实体<br>
* 描述： InstructSendRule实体<br>
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
* <td>2019-03-07 10:28:43</td>
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
public class InstructSendRule extends TailBean {

    /** 唯一标识 **/
    private String id;
    /** 车辆vin **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 历史在线状态 **/
    private Integer historyOnlineState;
    /** 添加人 **/
    private String createById;
    /** 添加人姓名 **/
    private String createBy;
    /** 添加时间 **/
    private String createTime;
    /** 更新时间 **/
    private String updateTime;
    /** 发送结果 0：成功  1：失败  2：超时 **/
    private Integer sendResult;
    /** 执行结果 **/
    private Integer executeResult;
    /** 运营区域（存储结果，非id） **/
    private String operatingArea;
    /** 运营单位 **/
    private String operatingUnit;
    /** 运营负责人 **/
    private String operationUserName;
    /** 联系方式 **/
    private String operationPhone;
    /** 运营负责人id **/
    private String operationUserId;
    /** 国标value;控制命令id（当type=2即为动力锁车时） **/
    private String standardValue;
    /** 国标code;控制命令参数0x83（当type=2即为动力锁车时） **/
    private String standardCode;
    /** 响应时间 **/
    private String responseTime;
    /** 备注 **/
    private String remarks;
    /** 0、远程终端控制状态 ，2、动力锁车 **/
    private Integer type;
    /** 命令名称 **/
    private String instructName;
    /** 流水号 **/
    private String sessionId;
    /** 数据来源1页面执行2接口执行 **/
    private Integer dataSource;
    /** 报警等级，type=1远程控制时用 **/
    private Integer alarmLevel;
    /** 指令缓存时间 **/
    private Integer instructCacheTime;
    /** 命令执行结果(失败原因) **/
    private String instructRemark;


    //辅助字段
    /** 内部编码 **/
    private String interNo;
    /** 车辆型号名称 **/
    private String vehModelName;
    /** 指令任务有效时间 **/
    private Integer effectiveTime;
    /** 指令任务时间单位：1天数2小时数3分钟 **/
    private Integer effectiveTimeType;
    /** 指令任务创建时间 **/
    private String taskCreateTime;


}
