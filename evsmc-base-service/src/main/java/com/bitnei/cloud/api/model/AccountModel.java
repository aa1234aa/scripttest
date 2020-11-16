package com.bitnei.cloud.api.model;

import com.bitnei.cloud.api.domain.Account;
import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： Account新增模型<br>
 * 描述： Account新增模型<br>
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
 * <td>2019-01-15 16:35:26</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AccountModel", description = "授权账号Model")
public class AccountModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    @Length(min = 2, max = 32, message = "名称长度为2-32个字符",
            groups = {GroupInsert.class, GroupUpdate.class})
    private String name;

    @ColumnHeader(title = " 令牌")
    @NotEmpty(message = " 令牌不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = " 令牌")
    @Length(min = 2, max = 1024, message = "令牌长度为2-1024个字符",
            groups = {GroupInsert.class, GroupUpdate.class})
    private String token;

    @ColumnHeader(title = "加密方案 0:AES 1:RSA")
    @NotNull(message = "加密方案 0:AES 1:RSA不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "加密方案 0:AES 1:RSA")
    private Integer encryptType;

    @ApiModelProperty(value = "加密方案描述")
    @DictName(code = "ENCRYPT_TYPE", joinField = "encryptType")
    private String encryptTypeDisplay;

    @ColumnHeader(title = "app_key")
    @ApiModelProperty(value = "AES密钥")
    private String appKey;

    @ColumnHeader(title = "签名密钥")
    @ApiModelProperty(value = "签名密钥")
    @Length(min = 2, max = 128, message = "签名密钥长度为2-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    private String signSecret;

    @ColumnHeader(title = "RSA公钥")
    @ApiModelProperty(value = "RSA公钥")
    private String rsaPubKey;

    @ColumnHeader(title = "RSA私钥")
    @ApiModelProperty(value = "RSA私钥")
    private String rsaPriKey;

    @ColumnHeader(title = "生效时间")
    @ApiModelProperty(value = "生效时间")
    private String validTimeBg;

    @ColumnHeader(title = "失效时间")
    @ApiModelProperty(value = "失效时间")
    private String validTimeEd;

    @ColumnHeader(title = "有效状态，1：有效 0：无效")
    @NotNull(message = "有效状态，1：有效 0：无效不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "有效状态，1：有效 0：无效")
    private Integer isValid;

    @ApiModelProperty(value = "有效状态描述")
    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    private String isValidDisplay;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "关联用户id")
    @NotNull(message = "关联用户id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private String userId;

    @ApiModelProperty(value = "关联用户名称")
    @LinkName(table = "sys_user", column = "username", joinField = "userId", desc = "车辆种类名称")
    private String userName;


    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static AccountModel fromEntry(Account entry) {
        AccountModel m = new AccountModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
