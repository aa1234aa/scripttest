package com.bitnei.cloud.sys.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Table {

    private String tableName;
    private String tableComment;
    private String columnKey;
    private String columnName;
    private String columnComment;

}
