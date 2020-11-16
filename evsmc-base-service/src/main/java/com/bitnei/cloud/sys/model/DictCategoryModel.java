package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.DictCategory;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DictCategory新增模型<br>
* 描述： DictCategory新增模型<br>
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
* <td>2018-12-22 10:25:37</td>
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
@ApiModel(value = "DictCategoryModel", description = "字典类别Model")
public class DictCategoryModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "字典名称")
    @NotBlank(message = "字典名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,32}|$", message = "字典名称长度2-32个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "字典名称")
    private String name;

    @ColumnHeader(title = "编码")
    @NotBlank(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]{1,32}|$", message = "编码长度1-32个英文、数字、_", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String code;

    @ColumnHeader(title = "备注")
    @Pattern(regexp = "^[\\s\\S]{0,100}$", message = "备注长度为0-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

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
    public static DictCategoryModel fromEntry(DictCategory entry){
        DictCategoryModel m = new DictCategoryModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
