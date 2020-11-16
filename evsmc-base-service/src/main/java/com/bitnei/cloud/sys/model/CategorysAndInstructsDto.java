package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategorysAndInstructsDto {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "种类编码")
    @ApiModelProperty(value = "种类编码")
    private String categoryCode;

    @ColumnHeader(title = "种类名称")
    @ApiModelProperty(value = "种类名称")
    private String name;

    @ColumnHeader(title = "命令列表")
    @ApiModelProperty(value = "命令列表")
    private List<InstructManagementModel> instructs;

}
