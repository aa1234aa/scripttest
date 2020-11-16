package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PushLogDTO {

    @NotEmpty(message = "推送明细ID不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "推送明细ID")
    private String pushId;

    @NotEmpty(message = "账号id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "账号id")
    private String accountId;

    @ApiModelProperty(value = "推送时间")
    private String pushTime;

    @ApiModelProperty(value = "是否推送成功")
    private Integer isSuccess;

    @ApiModelProperty(value = "推送成功时间")
    private String pushSuccessTime;

    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @ApiModelProperty(value = "推送内容")
    private String pushBodyContent;

    @ApiModelProperty(value = "响应体内容")
    private String rspBodyContent;

    @ApiModelProperty(value = "是否重新推送")
    private Integer rePushFlag;

}
