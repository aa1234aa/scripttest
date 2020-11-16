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
* 功能： VehicleEngeryDeviceLk实体<br>
* 描述： VehicleEngeryDeviceLk实体<br>
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
* <td>2018-12-13 17:13:45</td>
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
public class VehicleEngeryDeviceLk extends TailBean {

    /** 主键 **/
    private String id;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 车辆id **/
    private String vehicleId;
    /** 可充电储能装置id **/
    private String engeryDeviceId;
    /** 可充电储能装置名称 **/
    private String engeryDeviceName;
    /** 动力蓄电池型号id */
    private String batteryModelId;
    /** 安装位置 **/
    private String installPostion;
    /** 超级电容型号id **/
    private String capacityModelId;
    /** 可充电装置类型 1:动力蓄电池 2:超级电容**/
    private Integer modelType;
    /** 发票号 **/
    private String invoiceNo;

    /** 蓄电池型号名称 */
    private String batteryModelName;
    /** 蓄电池类型(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) **/
    private Integer batteryType;
    /** 蓄电池总储电量(kW.h) **/
    private Double totalElecCapacity;
    /** 蓄电池系统能量密度(Wh/kg) **/
    private Double capacityDensity;
    /** 蓄电池单体型号 **/
    private String batteryCellModel;
    /** 蓄电池生产厂商 **/
    private String unitId;
    /** 蓄电池单体生产企业 **/
    private String batteryCellUnitId;


    /** 超级电容型号名称 */
    private String capacityModelName;
    /** 超级电容生产企业 **/
    private String scmUnitId;
    /** 超级电容总储电量(kW.h) **/
    private Double totalCapacity;
    /** 超级电容能量密度(Wh/kg) **/
    private Double scmCapacityDensity;

    /** 型号名称 */
    private String modelName;
    /** 总储电量 */
    private String totalCapacityStr;
    /** 能量密度 */
    private String capacityDensityStr;
    /** 可充电储能装置类型名称 */
    private String modelTypeDisplay;
    /** 电池类型名称 */
    private String batteryTypeName;
    /** 单体生产企业名称 **/
    private String batteryCellUnitName;
    /** 系统生产企业名称 **/
    private String unitName;








}
