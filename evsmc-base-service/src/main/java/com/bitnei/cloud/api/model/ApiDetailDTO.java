package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ApiDetailDTO {

    @NotEmpty(message = "名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @NotEmpty(message = "应用编码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "应用编码")
    private String applicationCode;

    @NotEmpty(message = "路径不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "路径")
    private String url;

    @NotEmpty(message = "描述不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String note;

    @NotEmpty(message = "版本号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "版本号")
    private String version;
}
