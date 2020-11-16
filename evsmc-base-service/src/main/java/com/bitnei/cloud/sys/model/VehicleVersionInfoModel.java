package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.VehicleVersionInfo;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleVersionInfo新增模型<br>
* 描述： VehicleVersionInfo新增模型<br>
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
* <td>2019-03-14 15:25:22</td>
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
@ApiModel(value = "VehicleVersionInfoModel", description = "车辆版本信息Model")
public class VehicleVersionInfoModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "序列号")
    @NotEmpty(message = "序列号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "序列号")
    private String serialNumber;

    @ColumnHeader(title = "iccid")
    @NotEmpty(message = "iccid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "iccid")
    private String iccid;

    @ColumnHeader(title = "vin")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "mpu_firmware_version")
    @NotEmpty(message = "mpu_firmware_version不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "mpu_firmware_version")
    private String mpuFirmwareVersion;

    @ColumnHeader(title = "mpu_software_version")
    @NotEmpty(message = "mpu_software_version不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "mpu_software_version")
    private String mpuSoftwareVersion;

    @ColumnHeader(title = "mcu_software_version")
    @NotEmpty(message = "mcu_software_version不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "mcu_software_version")
    private String mcuSoftwareVersion;

    @ColumnHeader(title = "状态")
    @NotNull(message = "状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "创建人id")
    @NotEmpty(message = "创建人id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "创建人id")
    private String createById;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ColumnHeader(title = "数据来源")
    @NotNull(message = "数据来源不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据来源")
    private Integer dataSource;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehicleVersionInfoModel fromEntry(VehicleVersionInfo entry){
        VehicleVersionInfoModel m = new VehicleVersionInfoModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
