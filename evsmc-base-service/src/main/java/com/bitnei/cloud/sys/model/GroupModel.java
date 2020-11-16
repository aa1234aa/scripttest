package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Group新增模型<br>
* 描述： Group新增模型<br>
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
* <td>2018-11-08 10:40:16</td>
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
@ApiModel(value = "GroupModel", description = "数据权限组管理Model")
@Data
public class GroupModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "组名")
    @NotEmpty(message = "组名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "组名")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,20}+$", message = "名称为2-20位中英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    private String name;

    @ColumnHeader(title = "是否有效")
    @ApiModelProperty(value = "是否有效")
    @NotNull(message = "是否有效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Range(min=0,max=1,message="0:无效,1:有效，其他输入非法",groups = { GroupInsert.class,GroupUpdate.class})
    private Integer isValid;

    @ApiModelProperty(value = "是否有效名称")
    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    private String isValidName;

    @ApiModelProperty(value = "规则类型 1：属性公式 2：列表选择")
    @NotNull(message = "是否有效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer ruleType;

    @ApiModelProperty(value = "规则类型描述")
    @DictName(code = "RULE_TYPE", joinField = "ruleType")
    private String  ruleTypeName;

    @ApiModelProperty(value = "资源类型id")
    @NotNull(message = "资源类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    private String resourceTypeId;

    @ApiModelProperty(value = "资源类型名称")
    @LinkName(table = "sys_core_resource", joinField = "resourceTypeId")
    private String resourceTypeName;


    @ColumnHeader(title = "组描述")
    @ApiModelProperty(value = "组描述")
    @Length( max = 100, message = "备注长度不能超过100", groups = {GroupInsert.class, GroupUpdate.class})
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static GroupModel fromEntry(Group entry){
        GroupModel m = new GroupModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
