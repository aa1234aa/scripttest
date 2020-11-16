package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.CoreExtendValue;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreExtendValue新增模型<br>
* 描述： CoreExtendValue新增模型<br>
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
* <td>2019-07-31 15:08:40</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CoreExtendValueModel", description = "属性扩展表Model")
public class CoreExtendValueModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "对应id值")
    @NotEmpty(message = "对应id值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "对应id值")
    private String idVal;

    @ColumnHeader(title = "属性值,Json字符串")
    @NotEmpty(message = "属性值,Json字符串不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "属性值,Json字符串")
    private String jsonValue;

    @ColumnHeader(title = "表名")
    @NotEmpty(message = "表名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "表名")
    private String tableName;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CoreExtendValueModel fromEntry(CoreExtendValue entry){
        CoreExtendValueModel m = new CoreExtendValueModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
