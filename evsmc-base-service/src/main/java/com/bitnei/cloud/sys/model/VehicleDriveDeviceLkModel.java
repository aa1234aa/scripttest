package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehicleDriveDeviceLk;
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
* 功能： VehicleDriveDeviceLk新增模型<br>
* 描述： VehicleDriveDeviceLk新增模型<br>
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
* <td>2018-12-13 17:09:47</td>
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
@ApiModel(value = "VehicleDriveDeviceLkModel", description = "车辆-驱动装置中间表Model")
public class VehicleDriveDeviceLkModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "车辆ID")
    @NotEmpty(message = "车辆ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆ID")
    private String vehicleId;

    @ColumnHeader(title = "驱动装置id")
    @NotEmpty(message = "驱动装置不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "驱动装置id")
    private String drvieDeviceId;


    @ColumnHeader(title = "驱动装置编码")
    private String drvieDeviceCode;

    /** 驱动电机型号 **/
    @ApiModelProperty(value = "驱动电机型号")
    private String driveModelId;

    /** 驱动电机型号名称显示**/
    @LinkName(table = "sys_drive_motor_model",  joinField = "driveModelId")
    @ApiModelProperty(value = "驱动电机型号名称")
    private String driveModelDisplay;

    @ApiModelProperty(value = "安装位置(1:前置 2:后置 3:轮边 4:轮毂内)")
    private String installPosition;

    /** 安装位置名称显示**/
    @DictName(code = "INSTALL_POSITION", joinField = "installPosition")
    @ApiModelProperty(value = "安装位置名称")
    private String installPositionDisplay;

    /** 驱动装置序号 **/
    @ApiModelProperty(value = "驱动装置序号")
    private String sequenceNumber;

    /** 驱动电机型号名称 **/
    @ApiModelProperty(value = "驱动电机型号名称")
    private String driveMotorModelName;

    /** 额定功率(KW) **/
    @ApiModelProperty(value = "额定功率(KW)")
    private Double ratedPower;

    /** 驱动电机生产企业 **/
    @ApiModelProperty(value = "驱动电机生产企业")
    private String prodUnitId;

    @ApiModelProperty(value = "驱动电机生产企业")
    @LinkName(table = "sys_unit", joinField = "prodUnitId")
    private String prodUnitName;

    /** 发票号 **/
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    /** 发动机型号ID **/
    @ApiModelProperty(value = "发动机型号ID")
    private String engineModelId;

    /** 发动机型号名称 **/
    @ApiModelProperty(value = "发动机型号名称")
    private String engineModelName;

    /** 发动机燃料形式 **/
    @ApiModelProperty(value = "发动机燃料形式")
    private Integer fuelForm;


    /** 额定功率(KW) **/
    private String ratedPowerStr;


    /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehicleDriveDeviceLkModel fromEntry(VehicleDriveDeviceLk entry){
        VehicleDriveDeviceLkModel m = new VehicleDriveDeviceLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
