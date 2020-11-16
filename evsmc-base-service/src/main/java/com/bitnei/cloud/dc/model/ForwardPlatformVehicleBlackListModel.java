package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.ForwardPlatformVehicleBlackList;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardPlatformVehicleBlackList新增模型<br>
* 描述： ForwardPlatformVehicleBlackList新增模型<br>
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
* <td>2019-07-03 14:48:54</td>
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
@ApiModel(value = "ForwardPlatformVehicleBlackListModel", description = "转发平台车辆黑名单Model")
public class ForwardPlatformVehicleBlackListModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "平台id")
    @NotEmpty(message = "平台id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "平台id")
    private String platformId;

    @ColumnHeader(title = "车辆id")
    @NotEmpty(message = "车辆id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "车辆ids")
    private String vehicleIds;

    @ApiModelProperty(value = "VIN")
    private String vin;
    @ApiModelProperty(value = "终端编号")
    private String serialNumber;
    @ApiModelProperty(value = "ICCID")
    private String iccid;
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;
    @ApiModelProperty(value = "所属转发平台")
    private String platformName;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardPlatformVehicleBlackListModel fromEntry(ForwardPlatformVehicleBlackList entry){
        ForwardPlatformVehicleBlackListModel m = new ForwardPlatformVehicleBlackListModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
