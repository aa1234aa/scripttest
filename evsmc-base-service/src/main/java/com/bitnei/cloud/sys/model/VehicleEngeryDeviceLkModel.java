package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.VehicleEngeryDeviceLk;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleEngeryDeviceLk新增模型<br>
* 描述： VehicleEngeryDeviceLk新增模型<br>
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleEngeryDeviceLkModel", description = "车辆-可充电存储中间表Model")
public class VehicleEngeryDeviceLkModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "车辆id")
    @NotEmpty(message = "车辆id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ColumnHeader(title = "可充电储能装置id")
    @NotEmpty(message = "可充电储能装置id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "可充电储能装置id")
    private String engeryDeviceId;

    @ColumnHeader(title = "可充电储能装置名称")
    private String engeryDeviceName;

    /** 动力蓄电池型号id */
    @ApiModelProperty(value = "动力蓄电池型号id")
    private String batteryModelId;

    /** 动力蓄电池型号名称 **/
    @LinkName(table = "sys_battery_device_model", joinField = "batteryModelId")
    @ApiModelProperty(value = "动力蓄电池型号名称")
    private String batteryModelDisplay;

    /** 安装位置 **/
    @ApiModelProperty(value = "安装位置")
    private String installPostion;

    @ApiModelProperty(value = "超级电容型号id")
    private String capacityModelId;

    /** 超级电容型号id关联表名称显示**/
    @ApiModelProperty(value = "超级电容型号名称")
    @LinkName(table = "sys_super_capacitor_model", joinField = "capacityModelId")
    private String capacityModelIdDisplay;

    /** 可充电装置类型 1:动力蓄电池 2:超级电容**/
    @ApiModelProperty(value = "可充电装置类型 1:动力蓄电池 2:超级电容")
    private Integer modelType;

    /** 可充电装置类型类字典名称显示**/
    @DictName(code = "ENERGY_DEVICE_TYPE", joinField = "modelType")
    @ApiModelProperty(value = "可充电储能装置类型名称")
    private String modelTypeDisplay;


    /** 型号名称 */
    private String modelName;
    public String getModelName() {
        if(Constant.EnergyDeviceType.BATTERY.equals(modelType)) {
            return batteryModelDisplay;
        } else if(Constant.EnergyDeviceType.CAPACITY.equals(modelType)) {
            return capacityModelIdDisplay;
        }
        return "";
    }

    /** 发票号 **/
    private String invoiceNo;

    /** 蓄电池型号名称 */
    private String batteryModelName;
    /** 蓄电池类型(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) **/
    private Integer batteryType;
    @DictName(code = "BATTERY_TYPE", joinField = "batteryType")
    @ApiModelProperty(value = "电池类型名称")
    private String  batteryTypeName;
    /** 蓄电池总储电量(kW.h) **/
    private Double totalElecCapacity;
    /** 蓄电池系统能量密度(Wh/kg) **/
    private Double capacityDensity;
    /** 蓄电池单体型号 **/
    private String batteryCellModel;
    /** 蓄电池生产厂商 **/
    private String unitId;
    @ApiModelProperty(value = "系统生产企业名称")
    @LinkName(table = "sys_unit", joinField = "unitId")
    private String unitName;

    /** 蓄电池单体生产企业 **/
    private String batteryCellUnitId;

    @ApiModelProperty(value = "单体生产企业名称")
    @LinkName(table = "sys_unit", joinField = "batteryCellUnitId")
    private String batteryCellUnitName;

    /** 超级电容型号名称 */
    private String capacityModelName;
    /** 超级电容生产企业 **/
    private String scmUnitId;
    @ApiModelProperty(value = "系统生产企业名称")
    @LinkName(table = "sys_unit", joinField = "scmUnitId")
    private String scmUnitName;
    /** 超级电容总储电量(kW.h) **/
    private Double totalCapacity;
    /** 超级电容能量密度(Wh/kg) **/
    private Double scmCapacityDensity;

    /** 总储电量 */
    private String totalCapacityStr;
    /** 能量密度 */
    private String capacityDensityStr;

    public String getDeviceModelName(){
        if(Constant.EnergyDeviceType.BATTERY.equals(modelType)) {
            return batteryModelName;
        } else if(Constant.EnergyDeviceType.CAPACITY.equals(modelType)) {
            return capacityModelName;
        }
        return "";
    }



   /**
     * 将实体转为前台model
     * @param entry entry
     * @return model
     */
    public static VehicleEngeryDeviceLkModel fromEntry(VehicleEngeryDeviceLk entry){
        VehicleEngeryDeviceLkModel m = new VehicleEngeryDeviceLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}