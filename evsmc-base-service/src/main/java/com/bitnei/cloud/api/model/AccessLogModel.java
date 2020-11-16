package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.api.domain.AccessLog;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AccessLog新增模型<br>
* 描述： AccessLog新增模型<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2019-01-16 15:24:09</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AccessLogModel", description = "接口访问日志Model")
public class AccessLogModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "账号id")
    @NotEmpty(message = "账号id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "账号id")
    private String accountId;

    @LinkName(table = "api_account", column = "name", joinField = "accountId", desc = "授权账号")
    private String accountName;

    @LinkName(table = "api_account", column = "token", joinField = "accountId", desc = "令牌")
    private String tokenName;

    @ColumnHeader(title = "访问时间")
    @NotEmpty(message = "访问时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "访问时间")
    private String accessTime;

    @ColumnHeader(title = "接口明细id")
    @NotEmpty(message = "接口明细id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接口明细id")
    private String apiDetailId;

    @LinkName(table = "api_api_detail", column = "name", joinField = "apiDetailId", desc = "接口名称")
    private String apiName;

    @ColumnHeader(title = "请求内容")
    @NotEmpty(message = "请求内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "请求内容")
    private String requestMsg;

    @ColumnHeader(title = "耗时")
    @NotNull(message = "耗时不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "耗时")
    private Integer takeTime;

    @ColumnHeader(title = "响应码")
    @NotNull(message = "响应码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应码")
    private Integer respCode;

    @ColumnHeader(title = "响应内容")
    @NotEmpty(message = "响应内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应内容")
    private String respMessage;

    @ColumnHeader(title = "访问ip")
    @NotEmpty(message = "访问ip不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "访问ip")
    private String accessIp;

    @ApiModelProperty(value = "加密方案 0:AES 1:RSA")
    private Integer encryptType;

    @ApiModelProperty(value = "AES密钥")
    private String appKey;

    @ApiModelProperty(value = "RSA公钥")
    private String rsaPubKey;

    @ApiModelProperty(value = "RSA私钥")
    private String rsaPriKey;

    @ColumnHeader(title = "请求内容解密")
    @ApiModelProperty(value = "请求内容解密")
    private String requestMsgDecoded;

    @ColumnHeader(title = "响应内容解密")
    @ApiModelProperty(value = "响应内容解密")
    private String respMessageDecoded;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static AccessLogModel fromEntry(AccessLog entry){
        AccessLogModel m = new AccessLogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
