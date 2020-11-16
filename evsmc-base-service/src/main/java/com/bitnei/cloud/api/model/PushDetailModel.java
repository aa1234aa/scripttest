package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
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

import com.bitnei.cloud.api.domain.PushDetail;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushDetail新增模型<br>
* 描述： PushDetail新增模型<br>
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
* <td>2019-01-15 17:06:43</td>
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
@ApiModel(value = "PushDetailModel", description = "推送明细Model")
public class PushDetailModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "编码")
    @NotEmpty(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String code;

    @ColumnHeader(title = "归属应用编码")
    @NotEmpty(message = "归属应用编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "归属应用编码")
    private String applicationCode;

    @ColumnHeader(title = "归属应用名称")
    @NotEmpty(message = "归属应用名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "归属应用名称")
    private String applicationName;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String note;

    @ColumnHeader(title = "版本号")
    @NotEmpty(message = "版本号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "版本号")
    private String version;

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
     * @param entry
     * @return
     */
    public static PushDetailModel fromEntry(PushDetail entry){
        PushDetailModel m = new PushDetailModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
