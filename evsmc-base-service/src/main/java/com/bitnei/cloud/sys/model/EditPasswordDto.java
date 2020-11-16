package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditPasswordDto {

    @ApiModelProperty("记录id")
    private String id;

//    @ApiModelProperty("原密码")
//    private String originPassword;

    @ApiModelProperty("新密码")
    private String newPassword;

}
