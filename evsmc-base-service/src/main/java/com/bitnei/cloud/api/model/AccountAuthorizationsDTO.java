package com.bitnei.cloud.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AccountAuthorizationsDTO {

    @ApiModelProperty(value = "账号id")
    private String accountId;

    @ApiModelProperty(value = "AES密钥")
    private String appKey;

    @ApiModelProperty(value = " 令牌")
    private String token;

    @ApiModelProperty(value = " 权限接口列表")
    private List<ApiDetailModel> authorities;

    @ApiModelProperty(value = " 权限推送列表")
    private List<PushDetailModel> pushes;

    @ApiModelProperty(value = " 有效期开始时间，格式为2017-01-01")
    private String validTimeBg;

    @ApiModelProperty(value = " 有效期结束，格式为2017-01-01")
    private String validTimeEd;

    @ApiModelProperty(value = "有效状态，1：有效 0：无效")
    private Integer isValid;

    @ApiModelProperty(value = "签名密钥")
    private String signSecret;

    @ApiModelProperty(value = "RSA公钥")
    private String rsaPubKey;

    @ApiModelProperty(value = "RSA私钥")
    private String rsaPriKey;

    @ApiModelProperty(value = "加密方案 0:AES 1:RSA")
    private Integer encryptType;

    @ApiModelProperty(value = "关联用户id")
    private String userId;
}
