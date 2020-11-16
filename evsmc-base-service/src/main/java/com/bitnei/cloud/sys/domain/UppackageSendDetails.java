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
* 功能： UppackageSendDetails实体<br>
* 描述： UppackageSendDetails实体<br>
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
* <td>2019-03-05 15:53:14</td>
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
public class UppackageSendDetails extends TailBean {

    /** 主键标识 **/
    private String id;
    /** vin码 **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 车辆类型id **/
    private String vehicleTypeId;
    /** 车辆类型 **/
    private String vehicleTypeValue;
    /** 车型id **/
    private String vehModelId;
    /** 车型名称 **/
    private String vehModelName;
    /** 升级包名称 **/
    private String fileName;
    /** 文件下发状态：0: 未下发、1 下发中、2 已完成、3 下发失败 **/
    private Integer fileSendStatus;
    /** 升级状态：0: 未开始、1: 升级成功、2: 升级失败； **/
    private Integer upgradeStatus;
    /** 任务状态：0: 未开始、1: 进行中、2: 已结束 **/
    private Integer uppackageSendStatus;
    /** 升级包id **/
    private String uppackageInfoId;
    /** 升级任务id **/
    private String uppackageSendId;
    /** 升级指令下发状态 **/
    private Integer upgradeSendState;
    /** 升级任务百分比 **/
    private String percentage;
    /** 历史在线状态：0：离线、1：在线、空为从未上过线 **/
    private Integer historyOnlineState;
    /** 操作区域 **/
    private String operatingArea;
    /** session_id **/
    private String sessionId;
    /** 备注 **/
    private String remark;
    /** interNo **/
    private String interNo;
    /** 指令缓存时间 **/
    private Integer instructCacheTime;
    /** 当前在线状态 **/
    private Integer onlineState;
    /** 创建时间 **/
    private String createTime;
    /** 修改时间 **/
    private String updateTime;


    // 辅助字段
    /** 升级指令类型：1国标；128自定义；99自定义指令 **/
    private Integer protocolType;
    /** 任务名称 **/
    private String taskName;
    /** 升级时间 **/
    private String upgradeTime;
    /** iccid **/
    private String iccid;
    /** 升级任务创建时间 **/
    private String taskCreateTime;
}
