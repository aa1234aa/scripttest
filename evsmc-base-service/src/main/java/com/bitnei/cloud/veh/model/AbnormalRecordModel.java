package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Lijiezhou on 2019/3/26.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AbnormalRecordModel", description = "异常记录")
public class AbnormalRecordModel extends BaseModel {

    @ApiModelProperty(value = "异常数据上传时间")
    private String reportDate;

    @ApiModelProperty(value = "异常发生开始位置")
    private String address;

    @ApiModelProperty(value = "时间异常发生开始位置")
    private String lngLat;

}
