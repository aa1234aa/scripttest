package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bitnei.cloud.sys.domain.CoreResourceItem;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreResourceItem新增模型<br>
* 描述： CoreResourceItem新增模型<br>
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
* <td>2018-11-05 09:34:18</td>
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
@ApiModel(value = "CoreResourceItemModel", description = "核心资源属性Model")
@Getter
@Setter
public class CoreResourceItemModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    @Length(min=1,max=32,message="名称长度必须为1-32",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message="名称不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String name;

    @ColumnHeader(title = "资源类型id")
    @NotEmpty(message = "资源类型id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "资源类型id")
    @Length(min=1,max=36,message="资源类型id长度必须为1-36",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message="资源类型id不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String resouceId;

    @LinkName(table ="sys_core_resource", column = "name", joinField = "resouceId")
    @ApiModelProperty(value = "资源类型名称")
    private String resourceName;

    @ColumnHeader(title = "前缀编码")
    @NotEmpty(message = "前缀编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "前缀编码")
    @Length(min=1,max=1,message="前缀编码长度必须为1",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Z]$",message="前缀编码固定格式是一个大写字母", groups = { GroupInsert.class,GroupUpdate.class})
    private String preCode;


    @ColumnHeader(title = "路径")
   // @NotEmpty(message = "路径不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "路径")
    //@Length(min=1,max=256,message="路径长度必须为1-32",groups = { GroupInsert.class,GroupUpdate.class})

    private String path;

    @ColumnHeader(title = "上一级")
   // @NotEmpty(message = "上一级不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "上一级")
    //@Length(min=1,max=36,message="上一级长度必须为1-36",groups = { GroupInsert.class,GroupUpdate.class})
   // @Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message="上一级不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String parentId;

    @LinkName(table ="sys_core_resource_item", column = "name", joinField = "parentId")
    @ApiModelProperty(value = "上级属性名称")
    private String parentName;

    @ColumnHeader(title = "列名")
    @ApiModelProperty(value = "列名")
    @Length(min=0,max=32,message="列名长度必须为0-32",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$|^$",message="列名不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String columnName;


    @ColumnHeader(title = "查询表名")
    @ApiModelProperty(value = "查询表名")
    @Length(min=0,max=32,message="查询表名名长度必须为0-32",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$|^$",message="查询表名不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String queryTableName;

    @ColumnHeader(title = "查询SQL")
    @ApiModelProperty(value = "查询SQL")
    private String querySql;



    @ColumnHeader(title = "对象表")
    //@NotEmpty(message = "关联表不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "对象表")
    //@Length(min=1,max=32,message="关联表长度必须为1-32",groups = { GroupInsert.class,GroupUpdate.class})
    //@Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message="关联表不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String objectTableName;

    @ColumnHeader(title = "匹配列名")
    //@NotEmpty(message = "列名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "匹配列名")
    @Length(min=0,max=32,message="匹配列名长度必须为0-32",groups = { GroupInsert.class,GroupUpdate.class})
    //@Pattern(regexp="^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message="列名不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String objectColumnName;

    /** 唯一列键名**/
    @ColumnHeader(title = "唯一列键名")
    @ApiModelProperty(value = "唯一列键名")
    private String identifyColumnName;
    /** 唯一列名称**/
    @ColumnHeader(title = "唯一列名称")
    @ApiModelProperty(value = "唯一列名称")
    private String identifyColumnLabel;

    @ColumnHeader(title = "关联选择url")
   // @NotEmpty(message = "关联选择url不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "关联选择url")
    //@Length(min=1,max=128,message="关联选择url长度必须为1-128",groups = { GroupInsert.class,GroupUpdate.class})
    private String chooseDialogUrl;

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
    public static CoreResourceItemModel fromEntry(CoreResourceItem entry){
        CoreResourceItemModel m = new CoreResourceItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
