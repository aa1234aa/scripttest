package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PersonalCenterInfoModel {

    @ColumnHeader(title = "用户名")
    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户名不能为空")
    private String ownerName;
    @ColumnHeader(title = "性别")
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ColumnHeader(title = "手机号")
    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,7,8,9][0-9]{9}$",message = "手机号不正确")
    private String telPhone;
    @ColumnHeader(title = "邮箱")
    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$|^$",message = "邮箱格式不正确")
    private String email;
    @ColumnHeader(title = "工号")
    @ApiModelProperty(value = "工号")

    private String jobNumber;
    @ColumnHeader(title = "联系地址")
    @ApiModelProperty(value = "联系地址")

    private String address;
    @ColumnHeader(title = "证件类型")
    @ApiModelProperty(value = "证件类型")
    private Integer cardType;
    @ColumnHeader(title = "证件号")
    @ApiModelProperty(value = "证件号")
    private String cardNo;
    @ColumnHeader(title = "证件地址")
    @ApiModelProperty(value = "证件地址")
    private String cardAddress;
    @ColumnHeader(title = "证件正面照id")
    @ApiModelProperty(value = "证件正面照id")
    private String frontCardImgId;
    @ColumnHeader(title = "证件反面照id")
    @ApiModelProperty(value = "证件反面照id")
    private String backCardImgId;
    @ColumnHeader(title = "手持证件照id")
    @ApiModelProperty(value = "手持证件照id")
    private String faceCardImgId;


}
