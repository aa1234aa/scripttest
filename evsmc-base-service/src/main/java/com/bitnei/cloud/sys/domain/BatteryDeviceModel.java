package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： BatteryDeviceModel实体<br>
* 描述： BatteryDeviceModel实体<br>
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
* <td>2018-11-14 13:43:57</td>
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
@Setter
@Getter
public class BatteryDeviceModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 型号名称 **/
    private String name;
    /** 电池类型(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) **/
    private Integer batteryType;
    /** 总成标称电压(V) **/
    private Double fixedVol;
    /** 总储电量(kW.h) **/
    private Double totalElecCapacity;
    /** 系统能量密度(Wh/kg) **/
    private Double capacityDensity;
    /** 额定容量(Ah) **/
    private Double fixedAh;
    /** 额定总重量(kg) **/
    private Double fixedKg;
    /** 额定输出电流(A) **/
    private Double fixedOutputA;
    /** 最高允许温度 **/
    private Double maxTempC;
    /** 串并联方式 1:串联 2:并联 **/
    private Integer linkMode;
    /** 充电循环次数 **/
    private Integer chgCycleCount;
    /** 充电倍率 **/
    private String chgRate;
    /** 最大允许充电容量(kWh) **/
    private Double chgMaxKwh;
    /** 最高允许充电总电压(V) **/
    private Double chgMaxV;
    /** 最大允许充电电流(A) **/
    private Double chgMaxA;
    /** 探针个数(个) **/
    private Integer tempMonitorCount;
    /** 车载能源管理系统型号 **/
    private String bmsModel;
    /** 单体数量 **/
    private Integer batteryCount;
    /** 单体型号 **/
    private String batteryCellModel;
    /** 封装方式 1:圆柱形电池 2:方形电池 3:软包电池 4:固态电池 **/
    private Integer cellPackageMode;
    /** 生产厂商 **/
    private String unitId;
    /** 单体生产企业 **/
    private String batteryCellUnitId;
    /** 车载能源管理系统生产企业 **/
    private String bmsUnitId;
    /** 尺寸(长/宽/高[mm]) **/
    private String appearanceSize;

    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;



}
