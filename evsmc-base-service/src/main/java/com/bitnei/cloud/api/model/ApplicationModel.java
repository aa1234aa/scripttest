package com.bitnei.cloud.api.model;

import com.bitnei.cloud.api.domain.Application;
import com.bitnei.cloud.common.annotation.ColumnHeader;
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

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： Application新增模型<br>
 * 描述： Application新增模型<br>
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
 * <td>2019-01-15 15:46:19</td>
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
@ApiModel(value = "ApplicationModel", description = "接口应用服务Model")
public class ApplicationModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "应用名")
    @NotEmpty(message = "应用名不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "应用名")
    @Length(min = 2, max = 10, message = "应用名长度为2-10个字符",
            groups = {GroupInsert.class, GroupUpdate.class})
    private String name;

    @ColumnHeader(title = "应用唯一编码")
    @NotEmpty(message = "应用唯一编码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "应用唯一编码")
    @Length(min = 1, max = 10, message = "应用唯一编码长度为1-10个字符",
            groups = {GroupInsert.class, GroupUpdate.class})
    private String code;

    @ColumnHeader(title = "版本号")
    @ApiModelProperty(value = "版本号")
    private String version;

    @ColumnHeader(title = "备注")
    @ApiModelProperty(value = "备注")
    private String note;

    @ColumnHeader(title = "心跳时间")
    @ApiModelProperty(value = "心跳时间")
    private String heartTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static ApplicationModel fromEntry(Application entry) {
        ApplicationModel m = new ApplicationModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
