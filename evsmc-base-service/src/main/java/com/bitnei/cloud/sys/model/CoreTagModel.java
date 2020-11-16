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

import com.bitnei.cloud.sys.domain.CoreTag;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreTag新增模型<br>
* 描述： CoreTag新增模型<br>
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
* <td>2019-03-25 15:57:04</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CoreTagModel", description = "系统核心标签表Model")
public class CoreTagModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ColumnHeader(title = "表名")
    @NotEmpty(message = "表名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "表名")
    private String tableName;

    @ColumnHeader(title = " id值")
    @NotEmpty(message = " id值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = " id值")
    private String idValue;

    @ColumnHeader(title = "我的标签列表")
    @NotEmpty(message = "我的标签列表不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "我的标签列表")
    private String myTags;

    @ColumnHeader(title = "谁能看到我")
    @NotEmpty(message = "谁能看到我不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "谁能看到我")
    private String whoCanSeeMe;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ColumnHeader(title = "标签ID")
    @NotEmpty(message = "标签ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "标签ID")
    private String tagId;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CoreTagModel fromEntry(CoreTag entry){
        CoreTagModel m = new CoreTagModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
