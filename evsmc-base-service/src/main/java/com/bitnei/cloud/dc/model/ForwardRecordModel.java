package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Lijiezhou on 2019/2/22.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ForwardRecordModel", description = "数据转发日志Model")
public class ForwardRecordModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "转发平台")
    private String forwardId;

    @ApiModelProperty(value = "转发时间")
    private String time;

    @ApiModelProperty(value = "转发类型")
    private String flag;

    @ApiModelProperty(value = "转发方式")
    private String type;

    @ApiModelProperty(value = "转发结果")
    private String result;

    @ApiModelProperty(value = "原始报文")
    private String packet;

    /** 转发平台名称**/
    @LinkName(table = "dc_forward_platform", column = "name", joinField = "forwardId", desc = "")
    private String forwardName;

    /** 转发方式**/
    @DictName(code = "FORWARD_MODE",joinField = "type")
    private String typeName;

    /** 转发类型**/
    @DictName(code = "FORWARD_TYPE",joinField = "flag")
    private String flagName;

    /** 转发结果**/
    @DictName(code = "FORWARD_RESULT",joinField = "result")
    private String resultName;
}
