package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ApiDetail新增模型<br>
 * 描述： ApiDetail新增模型<br>
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
 * <td>2019-01-15 17:07:10</td>
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
@ApiModel(value = "PushDetailAuthorityModel", description = "推送授权明细Model")
public class PushDetailAuthorityModel {

    @ApiModelProperty(value = "推送授权记录id")
    private String id;

    @ApiModelProperty(value = "账号id")
    private String accountId;

    @ApiModelProperty(value = "推送名称")
    private String pushName;

    @ApiModelProperty(value = "推送id")
    private String pushId;

    @ApiModelProperty(value = "应用服务")
    private String applicationName;

    @ApiModelProperty(value = "推送地址")
    private String pushUrl;

    @ColumnHeader(title = "接口版本号")
    @NotEmpty(message = "接口版本号不能为空", groups = { GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "版本号")
    private String version;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String note;

}
