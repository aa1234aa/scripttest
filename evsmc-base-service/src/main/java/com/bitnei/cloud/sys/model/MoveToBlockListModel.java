package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "MoveToBlockListModel", description = "移除至黑名单")
@Data
public class MoveToBlockListModel {

    /**
     * 用户id
     */
    private String userId;
    /**
     * id列表
     */
    private String ids;
    /**
     * 资源类型id
     */
    private String resourceTypeId;
}
