package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Area;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Area新增模型<br>
* 描述： Area新增模型<br>
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
* <td>2018-12-27 09:27:18</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AreaModel", description = "行政区域Model")
public class AreaModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 2, max = 20, message = "行政区域名称长度为2-20个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "父节点")
    @ApiModelProperty(value = "父节点")
    private String parentId;

    @ColumnHeader(title = "是否根节点")
    @ApiModelProperty(value = "是否根节点")
    private String isRoot;

    @ColumnHeader(title = "路径")
    @ApiModelProperty(value = "路径")
    private String path;

    @ColumnHeader(title = "层级")
    @ApiModelProperty(value = "层级")
    private Integer levels;

    @ColumnHeader(title = "经度")
    @NotNull(message = "经度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "经度")
    private Double lng;

    @ColumnHeader(title = "纬度")
    @NotNull(message = "纬度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ColumnHeader(title = "区域编码")
    @NotEmpty(message = "区域编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[\\d[-]]{6,7}$" ,message="区域编码不能使用特殊字符和汉字且长度为6-7个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "区域编码")
    private String code;

    @ColumnHeader(title = "车牌前缀")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5][A-Z]$" ,message="车牌前缀由一个汉字加一个大写英文字母组成", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车牌前缀")
    private String platPrefix;

    @ColumnHeader(title = "序号")
    @DecimalMin(value = "0", message = "序号范围0至99999", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "99999", message = "序号范围0至99999", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "序号")
    private Integer orderNum;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "label名称")
    private String label;

    /** 行政区域父节点名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "parentId", desc = "")
    private String parentName;

   /**
     * 将实体转为前台model
     * @param entry Area
     * @return AreaModel
     */
    public static AreaModel fromEntry(Area entry){
        AreaModel m = new AreaModel();
        BeanUtils.copyProperties(entry, m);
        m.setLabel(entry.get("label") == null ? null : entry.get("label").toString());
        return m;
    }

}
