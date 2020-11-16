package com.bitnei.cloud.sms.model;

import lombok.Data;

/**
 * @Desc： 控件字段
 * @Author: joinly
 * @Date: 2019/8/19
 */

@Data
public class FieldModel {

    /**字段名**/
    private String displayName;

    /**字段英文名**/
    private String valueName;

    /**字段值**/
    private String value;
}
