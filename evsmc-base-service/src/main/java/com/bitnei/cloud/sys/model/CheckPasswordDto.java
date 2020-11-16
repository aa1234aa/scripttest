package com.bitnei.cloud.sys.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPasswordDto {

    @ApiModelProperty("记录id")
    private String id;

    @ApiModelProperty("密码")
    private String password;

}
