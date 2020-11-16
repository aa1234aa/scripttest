package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-04-13</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Data
@ApiModel(value = "VehicleForGroupModel", description = "数据权限组用的车辆Model")
public class VehicleForGroupModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆阶段")
    @JsonIgnore
    private String stage;

    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")
    @ApiModelProperty(value = "车辆阶段名称")
    private String stageName;


    @ApiModelProperty(value = "车辆类型ID")
    @JsonIgnore
    private String vehTypeId;

    @ApiModelProperty(value = "车辆类型")
    private String vehTypeName;

    @ApiModelProperty(value = "制造工厂ID")
    @JsonIgnore
    private String factoryId;

    @ApiModelProperty(value = "制造工厂")
    @LinkName(table = "sys_unit", joinField = "factoryId")
    private String factoryName;


    @ApiModelProperty(value = "汽车厂商ID")
    @JsonIgnore
    private String vehManuUnitId;

    @ApiModelProperty(value = "汽车厂商")
    @LinkName(table = "sys_unit", joinField = "vehManuUnitId")
    private String manuUnitName;

    @ApiModelProperty(value = "终端厂商ID")
    @JsonIgnore
    private String termUnitId;

    @ApiModelProperty(value = "终端厂商")
    @LinkName(table = "sys_unit", joinField = "termUnitId")
    private String termUnitName;

    @ApiModelProperty(value = "终端型号")
    private String termModelName;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @LinkName(table = "sys_unit", joinField = "operUnitId")
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitName;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "上牌城市名称")
    @LinkName(table = "sys_area", joinField = "operLicenseCityId")
    private String operLicenseCityName;

    @ApiModelProperty(value = "购车城市id")
    private String sellCityId;

    @LinkName(table = "sys_area", joinField = "sellCityId")
    @ApiModelProperty(value = "购车城市名称")
    private String sellCityName;

    @ApiModelProperty(value = "运营区域")
    private String operAreaId;

    @LinkName(table = "sys_area", joinField = "operAreaId")
    @ApiModelProperty(value = "运营地区名称")
    private String operAreaName;



}
