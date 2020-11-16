package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Desc： 平台车辆
 * @Author: joinly
 * @Date: 2019/7/3
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PlatformVehicleModel", description = "平台车辆Model")
public class PlatformVehicleModel {

    @ApiModelProperty(value = "记录ids(多记录)")
    private String ids;

    @NotEmpty(message = "转发车辆ids不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发车辆ids")
    private String vehicleIds;

    @NotEmpty(message = "转发平台不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发平台")
    private String platformId;

    @ApiModelProperty(value = "成功数")
    private Integer successNum;

    @ApiModelProperty(value = "黑名单数")
    private Integer blackNum;

    @ApiModelProperty(value = "重复数")
    private Integer repeatNum;

    @ApiModelProperty(value = "黑名单车辆ids")
    private String blackIds;

    @ApiModelProperty(value = "重复车辆ids")
    private String repeatIds;
}
