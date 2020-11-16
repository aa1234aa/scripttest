package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.UnitType;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UnitType新增模型<br>
* 描述： UnitType新增模型<br>
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
* <td>2018-12-20 11:48:35</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UnitTypeModel", description = "单位类型Model")
public class UnitTypeModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    @Length(min=2,max=16,message="名称长度必须为2-16",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]+$",message="名称不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String name;

    @NotEmpty(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    @Pattern(regexp="^[A-Za-z0-9]{4}$",message="编码为4位英文或数字", groups = { GroupInsert.class,GroupUpdate.class})
    private String code;

    @ApiModelProperty(value = "上级单位ID")
    private String parentId;

    @LinkName(table ="sys_unit_type", column = "name", joinField = "parentId")
    @ApiModelProperty(value = "上级单位类型")
    private String parentDisplay;

    @ApiModelProperty(value = "单位类型路径")
    private String path;

    @ApiModelProperty(value = "备注")
    @Pattern(regexp = RegexUtil.C_0_100, message = "备注最大长度为100", groups = {GroupInsert.class, GroupUpdate.class})
    private String notes;

    @ApiModelProperty(value = "是否根节点")
    private int isRoot;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人id")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


   /**
     * 将实体转为前台model
     * @param entry UnitType
     * @return UnitTypeModel
     */
    public static UnitTypeModel fromEntry(UnitType entry){
        UnitTypeModel m = new UnitTypeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
