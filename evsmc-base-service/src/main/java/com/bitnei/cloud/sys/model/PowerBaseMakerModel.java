package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleRealStatus新增模型<br>
 * 描述： VehicleRealStatus新增模型<br>
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
 * <td>2019-03-16 14:55:45</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PowerOneMakerModel", description = "车气泡信息Model")
public class PowerBaseMakerModel extends BaseModel {

    @ColumnHeader(title = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "车架号")
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ColumnHeader(title = "最后通讯时间")
    @ApiModelProperty(value = "最后通讯时间")
    private String finalConnectTime;

    @ColumnHeader(title = "当前位置")
    @ApiModelProperty(value = "当前位置")
    private String address;

    @ColumnHeader(title = "当前车速")
    @ApiModelProperty(value = "当前车速")
    private String speed;

    @ColumnHeader(title = "动力方式")
    @ApiModelProperty(value = "动力方式")
    private Integer powerMode;

    @DictName(code = "POWER_MODE", joinField = "powerMode")
    @ApiModelProperty(value = "动力方式名称")
    private String powerModeDisplay;

    @ColumnHeader(title = "仪表总里程")
    @ApiModelProperty(value = "仪表总里程")
    private Double mils;

    @ColumnHeader(title = "经度")
    @ApiModelProperty(value = "经度")
    private String lng;

    @ColumnHeader(title = "纬度")
    @ApiModelProperty(value = "纬度")
    private String lat;

    @ColumnHeader(title = "车辆是否行驶")
    @ApiModelProperty(value = "车辆是否行驶")
    private String isRun;

    @ColumnHeader(title = "内部编码")
    @ApiModelProperty(value = "内部编码")
    private String interNo;

    @ColumnHeader(title = "车型名称")
    @ApiModelProperty(value = "车型名称")
    private String vehModelName;


    /**
     * 以下字段只有传统燃油车在用
     */

    @ApiModelProperty(value = "REV燃料类型")
    private Integer revFuelType;

    @DictName(code = "REV_FUEL_TYPE", joinField = "revFuelType")
    @ApiModelProperty(value = "REV燃料类型名称")
    private String revFuelTypeDisplay;

    @ApiModelProperty(value = "传统燃油车燃料类型")
    private String normalFuelType;
}
