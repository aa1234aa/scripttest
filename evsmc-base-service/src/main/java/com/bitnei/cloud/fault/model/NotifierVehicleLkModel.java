package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.NotifierVehicleLk;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierVehicleLk新增模型<br>
* 描述： NotifierVehicleLk新增模型<br>
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
* <td>2019-03-06 17:37:36</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "NotifierVehicleLkModel", description = "故障Model")
public class NotifierVehicleLkModel extends BaseModel {

    private String id;

    @NotBlank(message = "车辆负责人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "故障通知人id; 多车分配时用 , 分开; 如：value1,value2")
    private String notifierId;

    @NotBlank(message = "车辆不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆id; 多车分配时用 , 分开; 如：value1,value2")
    private String vehicleId;

    @ApiModelProperty(value = "分配人")
    private String createBy;

    @ApiModelProperty(value = "分配时间")
    private String createTime;

    /** 车辆vin **/
    @ApiModelProperty(value = "车辆vin")
    private String vin;

    /** 车牌 **/
    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    /** 车辆型号 **/
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    /** 上牌城市 **/
    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCity;

    /**分配的人数**/
    @ApiModelProperty(value = "分配人数")
    private int allocateCount;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static NotifierVehicleLkModel fromEntry(NotifierVehicleLk entry){
        NotifierVehicleLkModel m = new NotifierVehicleLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
