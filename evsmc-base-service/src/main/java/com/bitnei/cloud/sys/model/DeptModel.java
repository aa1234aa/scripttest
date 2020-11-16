package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Dept;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Pattern;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： Dept新增模型<br>
 * 描述： Dept新增模型<br>
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
 * <td>2018-11-07 14:11:13</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@ApiModel(value = "DeptModel", description = "组织机构Model")
public class DeptModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "节点名称")
    @NotEmpty(message = "节点名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,30}+$", message = "节点名称为2-30位中英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "节点名称")
    private String name;

    @ColumnHeader(title = "单位id")
    @NotEmpty(message = "单位不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单位id")
    private String unitId;

    @ColumnHeader(title = "上级id")
//    @NotEmpty(message = "上级id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ColumnHeader(title = "路径")
//    @NotEmpty(message = "路径不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_dept", column = "name", joinField = "parentId", desc = "上级名称")
    private String parentName;

    @ColumnHeader(title = "备注")
    @ApiModelProperty(value = "备注")
    @Pattern(regexp = RegexUtil.C_0_100, message = "备注最大长度为100", groups = {GroupInsert.class, GroupUpdate.class})
    private String note;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "单位名称")
    @LinkName(table = "sys_unit", joinField = "unitId",desc = "")
    private String unitName;

    @ApiModelProperty(value = "用户数量")
    private Integer userCount;

    @ApiModelProperty(value = "联系人数量")
    private Integer ownerPeopleCount;

    /** 单位统一社会码 **/
    @ApiModelProperty(value = "单位统一社会码")
    private String organizationCode;
    /** 单位类型 **/
    @ApiModelProperty(value = "单位类型")
    private String unitTypeNames;
    /** 单位联系人姓名 **/
    @ApiModelProperty(value = "单位联系人姓名")
    private String contactorName;
    /** 单位联系人电话 **/
    @ApiModelProperty(value = "单位联系人电话")
    private String contactorPhone;


    /**
     * 将实体转为前台model
     * @param entry Dept
     * @return DeptModel
     */
    public static DeptModel fromEntry(Dept entry) {
        DeptModel m = new DeptModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
