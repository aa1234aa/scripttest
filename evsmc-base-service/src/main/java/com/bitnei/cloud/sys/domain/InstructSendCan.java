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
* 功能： InstructSendCan实体<br>
* 描述： InstructSendCan实体<br>
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
* <td>2019-03-15 17:08:14</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstructSendCan extends TailBean {

    /** 主键标识 **/
    private String id;
    /** 车辆vin **/
    private String vin;
    /** 车牌 **/
    private String licensePlate;
    /** 命令名称 **/
    private String instructName;
    /** 指令id **/
    private String instructId;
    /** 添加人 **/
    private String createById;
    /** 添加人姓名 **/
    private String createBy;
    /** 添加时间 **/
    private String createTime;
    /** 发送结果 **/
    private Integer sendResult;
    /** 执行结果 **/
    private Integer executeResult;
    /** 运营区域 **/
    private String operatingArea;
    /** 运营单位 **/
    private String operatingUnit;
    /** 运营负责人 **/
    private String operationUserName;
    /** 联系方式 **/
    private String operationPhone;
    /** 运营负责人id **/
    private String operationUserId;
    /** 会话标识 **/
    private String sessionId;
    /** 应答id及数据——储存格式：  id: 数据,id:数据,id:数据,.... **/
    private String responseIdData;
    /** 响应时间 **/
    private String responseTime;
    /** 历史在线状态 **/
    private Integer historyOnlineState;
    /** 备注 **/
    private String remarks;
    /** 命令执行结果(失败原因) **/
    private String instructRemark;

    /** 内部编码 **/
    private String interNo;
    /** 车辆型号 **/
    private String vehModelName;

}
