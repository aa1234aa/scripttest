package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRuleVehicleModel新增模型<br>
* 描述： ForwardRuleVehicleModel新增模型<br>
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
* <td>2019-05-08 18:29:14</td>
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
@ApiModel(value = "ForwardRuleVehicleModel", description = "转发规则车辆Model")
public class ForwardRuleVehicleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "制造工厂id")
    private String manuUnitId;

    @ApiModelProperty(value = "制造工厂")
    @LinkName(table = "sys_unit", joinField = "manuUnitId")
    private String manuUnitName;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;
    /** 运营单位名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId",desc = "")
    @ApiModelProperty(value = "运营单位名称", hidden = true)
    private String operUnitName;


    @ApiModelProperty(value = "内部编号")
    private String interNo;
    @ApiModelProperty(value = "支持的协议")
    private String supportProtocol;
    @ApiModelProperty(value = "支持通讯协议名称")
//    @LinkName(table = "dc_rule", joinField = "supportProtocol", desc = "支持通讯协议名称")
    private String supportProtocolName;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "上牌城市名称")
//    @LinkName(table = "sys_area", joinField = "operLicenseCityId")
    private String operLicenseCityName;

    @ApiModelProperty(value = "车辆种类id")
    private String vehTypeId;

    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardRuleVehicleModel fromEntry(ForwardVehicle entry){
        ForwardRuleVehicleModel m = new ForwardRuleVehicleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
