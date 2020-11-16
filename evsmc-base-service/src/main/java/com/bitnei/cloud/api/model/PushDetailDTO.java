package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PushDetailDTO {

    @NotEmpty(message = "名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @NotEmpty(message = "编码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String code;

    @NotEmpty(message = "归属应用编码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "归属应用编码")
    private String applicationCode;

    @NotEmpty(message = "描述不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String note;

    @NotEmpty(message = "版本号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "版本号")
    private String version;
}
