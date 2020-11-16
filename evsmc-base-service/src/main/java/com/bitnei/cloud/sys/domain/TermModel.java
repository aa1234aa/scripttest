package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermModel实体<br>
* 描述： TermModel实体<br>
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
* <td>2018-11-05 10:01:40</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Setter
@Getter
public class TermModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 终端型号 **/
    private String termModelName;
    /** 种类[车机/T-BOX/OBD/智能后视镜等] **/
    private Integer termCategory;
    /** 品牌名称 **/
    private String brandName;
    /** 工作电压 **/
    private String workingVoltage;
    /** 功耗 **/
    private String powerWaste;
    /** 固件版本号 **/
    private String firmwareVersion;
    /** 生产厂商 **/
    private String unitId;
    /** 休眠电流 **/
    private String sleepElectricCurrent;
    /** 内置电池容量 **/
    private String builtInBatteryCapacity;
    /** 终端零件号，用逗号分隔 **/
    private String termPartFirmwareNumbers;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 支持加密芯片 **/
    private Integer supportEncryptionChip;
    /** 车载终端检测编号 **/
    private String termDetectionNo;
    /** 终端检测报告扫描件 **/
    private String detectionReportImgId;



}
