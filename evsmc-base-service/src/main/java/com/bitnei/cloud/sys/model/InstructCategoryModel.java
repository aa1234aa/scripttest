package com.bitnei.cloud.sys.model;

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

import com.bitnei.cloud.sys.domain.InstructCategory;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructCategory新增模型<br>
* 描述： InstructCategory新增模型<br>
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
* <td>2019-03-11 14:20:16</td>
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
@ApiModel(value = "InstructCategoryModel", description = "控制命令种类Model")
public class InstructCategoryModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "种类编码")
    @NotEmpty(message = "种类编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "种类编码")
    private String categoryCode;

    @ColumnHeader(title = "种类名称")
    @NotEmpty(message = "种类名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "种类名称")
    private String name;

    @ColumnHeader(title = "描述")
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "创建人id")
    @ApiModelProperty(value = "创建人id")
    private String createById;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static InstructCategoryModel fromEntry(InstructCategory entry){
        InstructCategoryModel m = new InstructCategoryModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
