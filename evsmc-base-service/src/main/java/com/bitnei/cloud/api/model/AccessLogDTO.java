package com.bitnei.cloud.api.model;

import com.bitnei.cloud.api.domain.AccessLog;
import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Data
public class AccessLogDTO {

    @NotEmpty(message = "账号id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "账号id")
    private String accountId;

    @NotEmpty(message = "访问时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "访问时间")
    private String accessTime;

    @NotEmpty(message = "接口明细id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接口明细id")
    private String apiDetailId;

    @NotEmpty(message = "请求内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "请求内容")
    private String requestMsg;

    @NotNull(message = "耗时不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "耗时")
    private Integer takeTime;

    @NotNull(message = "响应码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应码")
    private Integer respCode;

    @NotEmpty(message = "响应内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应内容")
    private String respMessage;

    @NotEmpty(message = "访问ip不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "访问ip")
    private String accessIp;

}
