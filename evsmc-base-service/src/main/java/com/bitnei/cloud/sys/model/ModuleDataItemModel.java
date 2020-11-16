package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.ModuleDataItem;
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
* 功能： ModuleDataItem新增模型<br>
* 描述： ModuleDataItem新增模型<br>
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
* <td>2018-11-22 11:43:02</td>
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
@ApiModel(value = "ModuleDataItemModel", description = "模块数据项列表Model")
public class ModuleDataItemModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @Length(min = 2, max = 30, message = "长度为2-50个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @Length(min = 2, max = 30, message = "长度为2-30个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "属性名")
    private String fieldName;

    @Length(min = 2, max = 30, message = "长度为2-30个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "组名")
    private String dataGroupId;

    @Pattern(regexp = ".{2,100}|", message = "备注长度为2-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @NotNull(message = "是否启用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否启用")
    private Integer isEnable;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "模块id")
    private String moduleId;

    @ApiModelProperty(value = "是否隐藏")
    private Integer isHidden;

    @ApiModelProperty(value = "是否脱敏")
    private Integer isSensitive;


    /**
     * 将实体转为前台model
     * @param entry ModuleDataItem
     * @return ModuleDataItemModel
     */
    public static ModuleDataItemModel fromEntry(ModuleDataItem entry){
        ModuleDataItemModel m = new ModuleDataItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
