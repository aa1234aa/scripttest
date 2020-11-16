package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehBrand;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 车辆品牌model<br>
* 描述： 车辆品牌model<br>
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
* <td>2019-01-22 14:33:02</td>
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
@ApiModel(value = "VehBrandModel", description = "车辆品牌Model")
public class VehBrandModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "id不能为空", groups = {GroupUpdate.class})
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = RegexUtil.A_2_10, message = "名称长度2-10个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "英文品牌名称")
    @Pattern(regexp = RegexUtil.E_M_2_64, message = "英文品牌名称长度2-64个英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "英文品牌名称")
    private String englistName;

    @ColumnHeader(title = "编码")
    @NotEmpty(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = RegexUtil.A_1_10, message = "编码长度1-10个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String code;

    @ColumnHeader(title = "商标图片ID")
    @ApiModelProperty(value = "商标图片ID")
    private String brandImgId;

    @ColumnHeader(title = "备注")
    @ApiModelProperty(value = "备注")
    @Pattern(regexp = RegexUtil.C_0_100, message = "备注最大长度为100", groups = {GroupInsert.class, GroupUpdate.class})
    private String note;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private String createTime;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private String updateTime;

    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;


   /**
     * 将实体转为前台model
     * @param entry VehBrand
     * @return VehBrandModel
     */
    public static VehBrandModel fromEntry(VehBrand entry){
        VehBrandModel m = new VehBrandModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
