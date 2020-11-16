package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Module;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Module新增模型<br>
* 描述： Module新增模型<br>
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
* <td>2018-12-10 17:33:28</td>
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
@Setter
@Getter
@ApiModel(value = "ModuleModel", description = "模块管理Model")
public class ModuleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "上一级")
    private String parentId;

    @NotNull(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 2, max = 10, message = "名称的长度应为2-10位", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9@-@]{1,128}|$", message = "权限编码为长度1-128位英文或数字", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "权限编码")
    private String code;

    @ApiModelProperty(value = "是否根节点")
    private Integer isRoot;

    @ApiModelProperty(value = "是否功能 1 功能 0:模块")
    private Integer isFun;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ColumnHeader(title = "动作")
    @ApiModelProperty(value = "动作")
    private String action;

    @ColumnHeader(title = "是否全屏")
    @ApiModelProperty(value = "是否全屏")
    private Integer isFullscreen;

    @ColumnHeader(title = "序号")
    @NotNull(message = "序号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "序号")
    private Integer orderNum;

    @ColumnHeader(title = "是否隐藏")
    @NotNull(message = "是否隐藏不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer isHidden;

    @DictName(joinField = "isHidden", code = "BOOL_TYPE")
    @ApiModelProperty(value = "是否隐藏名称")
    private String isHiddenName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "上级名称")
    private String parentName;

    /**
     * 将实体转为前台model
     * @param entry Module
     * @return ModuleModel
     */
    public static ModuleModel fromEntry(Module entry){
        ModuleModel m = new ModuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
