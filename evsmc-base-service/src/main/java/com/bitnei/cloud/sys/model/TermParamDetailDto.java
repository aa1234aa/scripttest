package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import lombok.Data;

@Data
public class TermParamDetailDto {

    @ColumnHeader(title = "长度")
    private Integer dataSize;

    @ColumnHeader(title = "数据类型")
    private String dataType;

    @ColumnHeader(title = "参数名称")
    private String name;

    @ColumnHeader(title = "值")
    private String value;

}
