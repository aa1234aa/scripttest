package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehiclePowerDeviceLk;
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
* 功能： VehiclePowerDeviceLk新增模型<br>
* 描述： VehiclePowerDeviceLk新增模型<br>
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
* <td>2018-12-13 17:14:00</td>
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
@ApiModel(value = "VehiclePowerDeviceLkModel", description = "车辆-发电装置中间表Model")
public class VehiclePowerDeviceLkModel extends BaseModel {

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

    @ColumnHeader(title = "发电装置id")
    @NotEmpty(message = "发电装置id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "发电装置id")
    private String powerDeviceId;

    @ColumnHeader(title = "发电装置code")
    private String powerDeviceCode;

    /** 安装位置 1：前置 2：中置 3：后置 **/
    private Integer installPosition;

    /** 燃油发电机型号id **/
    private String fuelGeneratorModelId;

    /** 燃油发电机型号名称显示**/
    @LinkName(table = "sys_fuel_generator_model", joinField = "fuelGeneratorModelId")
    @ApiModelProperty(value = "燃油发电机型号名称")
    private String generatorModelDisplay;

    /** 燃料电池型号名称 */
    @ApiModelProperty(value = "燃料电池型号名称")
    private String fuelBatteryModelName;
    /** 燃料电池系统额定功率(kW) */
    @ApiModelProperty(value = "燃料电池系统额定功率(kW)")
    private Double ratedPower;
    /** 燃料电池系统生产企业 */
    @ApiModelProperty(value = "燃料电池系统生产企业")
    private String prodUnitId;
    @ApiModelProperty(value = "系统生产企业名称")
    @LinkName(table = "sys_unit", joinField = "prodUnitId")
    private String prodUnitName;
    /** 发票号 */
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;


    /** 额定功率(KW) **/
    private String ratedPowerStr;





   /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehiclePowerDeviceLkModel fromEntry(VehiclePowerDeviceLk entry){
        VehiclePowerDeviceLkModel m = new VehiclePowerDeviceLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
