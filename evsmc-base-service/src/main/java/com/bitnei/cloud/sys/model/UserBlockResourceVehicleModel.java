package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.UserBlockResource;
import com.bitnei.cloud.sys.domain.UserBlockResourceVehicle;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserBlockResource实体<br>
* 描述： UserBlockResource实体<br>
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
* <td>2019-07-04 14:22:54</td>
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
@Data
@ApiModel(value = "UserBlockResourceVehicleModel", description = "黑名单车辆Model")
public class UserBlockResourceVehicleModel extends BaseModel {
    /** ID **/
    @ApiModelProperty(value = "主键")
    private String id;
    /** VIN **/
    @ApiModelProperty(value = "车架号")
    private String vin;
    /** 车牌号 **/
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    /** 内部编号 **/
    @ApiModelProperty(value = "内部编号")
    private String interNo;
    /** 制造工厂ID **/
    @ApiModelProperty(value = "制造工厂ID", hidden = true)
    @JsonIgnore
    private String manuUnitId;

    @ApiModelProperty(value = "制造工厂")
    @LinkName(table = "sys_unit", joinField = "manuUnitId")
    private String manuUnitName;
    /** 品牌ID **/
    @ApiModelProperty(value = "品牌ID", hidden = true)
    @JsonIgnore
    private String brandId;

    @ApiModelProperty(value = "品牌名称")
    @LinkName(table = "sys_veh_brand", joinField = "brandId")
    private String brandName;
    /** 系列ID **/
    @ApiModelProperty(value = "系列ID", hidden = true)
    @JsonIgnore
    private String seriesId;

    @ApiModelProperty(value = "系列名称")
    @LinkName(table = "sys_veh_series", joinField = "seriesId")
    private String seriesName;
    /** 车型 **/
    private String vehModelName;
    /** 车辆种类 **/
    private String vehTypeName;
    /** 终端编号 **/
    private String termNo;
    /** ICCID **/
    private String iccid;
    /** 车主姓名 **/
    private String ownerName;
    /** 车主姓名 **/
    private String ownerTelPhone;
    /** 公告型号 **/
    @ApiModelProperty(value = "公告型号")
    private String vehNoticeName;
    /** 车辆阶段 **/
    private String stage;
    /** 车辆阶段名称 **/
    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")
    private String stageName;

    @ApiModelProperty(value = "运营单位ID")
    @JsonIgnore
    private String operUnitId;

    @ApiModelProperty(value = "运营单位")
    @LinkName(table = "sys_unit", joinField = "operUnitId")
    private String operUnitName;
    /** 上牌城市  **/
    @ApiModelProperty(value = "上牌城市")
    private String onPlateCity;



    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static UserBlockResourceVehicleModel fromEntry(UserBlockResourceVehicle entry){
        UserBlockResourceVehicleModel m = new UserBlockResourceVehicleModel();
        BeanUtils.copyProperties(entry, m);
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PUBLIC_SPHERE.getValue().equals(entry.getSellForField())) {
            m.setOwnerName(entry.getSellPubUnitName());
            m.setOwnerTelPhone(entry.getSellPubUnitTelephone());
        }
        return m;
    }

}
