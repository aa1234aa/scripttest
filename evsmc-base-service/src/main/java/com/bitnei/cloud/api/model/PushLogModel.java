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

import com.bitnei.cloud.api.domain.PushLog;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushLog新增模型<br>
* 描述： PushLog新增模型<br>
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
* <td>2019-01-16 15:24:34</td>
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
@ApiModel(value = "PushLogModel", description = "推送日志Model")
public class PushLogModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "推送明细ID")
    @NotEmpty(message = "推送明细ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "推送明细ID")
    private String pushId;

    @LinkName(table = "api_push_detail", column = "name", joinField = "pushId", desc = "推送名称")
    private String pushName;

    @ColumnHeader(title = "账号id")
    @NotEmpty(message = "账号id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "账号id")
    private String accountId;

    @LinkName(table = "api_account", column = "name", joinField = "accountId", desc = "授权账号")
    private String accountName;

    @LinkName(table = "api_account", column = "token", joinField = "accountId", desc = "令牌")
    private String tokenName;

    @ColumnHeader(title = "推送时间")
    @ApiModelProperty(value = "推送时间")
    private String pushTime;

    @ColumnHeader(title = "是否推送成功")
    @ApiModelProperty(value = "是否推送成功")
    private Integer isSuccess;

    @ColumnHeader(title = "推送成功时间")
    @ApiModelProperty(value = "推送成功时间")
    private String pushSuccessTime;

    @ColumnHeader(title = "错误信息")
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @ColumnHeader(title = "推送内容")
    @ApiModelProperty(value = "推送内容")
    private String pushBodyContent;

    @ColumnHeader(title = "响应体内容")
    @ApiModelProperty(value = "响应体内容")
    private String rspBodyContent;

    @ColumnHeader(title = "是否重新推送")
    @ApiModelProperty(value = "是否重新推送")
    private Integer rePushFlag;

    @ApiModelProperty(value = "加密方案 0:AES 1:RSA")
    private Integer encryptType;

    @ApiModelProperty(value = "AES密钥")
    private String appKey;

    @ApiModelProperty(value = "RSA公钥")
    private String rsaPubKey;

    @ApiModelProperty(value = "RSA私钥")
    private String rsaPriKey;

    @ApiModelProperty(value = "解密的推送内容")
    private String pushBodyDecoded;

    @ApiModelProperty(value = "解密的响应体内容")
    private String rspBodyDecoded;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PushLogModel fromEntry(PushLog entry){
        PushLogModel m = new PushLogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
