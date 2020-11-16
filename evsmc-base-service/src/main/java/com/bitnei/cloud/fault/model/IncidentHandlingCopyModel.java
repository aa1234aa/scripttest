package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Lijiezhou
 */
@Getter
@Setter
@ApiModel(value = "IncidentHandlingCopyModel", description = "车型复制事故处置预案Model")
public class IncidentHandlingCopyModel {

    @NotEmpty(message = "源预案id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "源预案id")
    private String id;

    @NotEmpty(message = "目标车型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "目标车型id")
    private String targetVehModelId;

    /** 0为否 1为是 **/
    @ApiModelProperty(value = "是否确认复制")
    private Integer trueOrFalse;
}
