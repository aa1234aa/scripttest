package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehType;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehType新增模型<br>
 * 描述： VehType新增模型<br>
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
 * <td>2018-11-12 15:00:34</td>
 * <td>renshuo</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author renshuo
 * @version 1.0
 * @since JDK1.8
 */
@Setter
@Getter
@ApiModel(value = "VehTypeModel", description = "车辆种类Model")
public class VehTypeModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "上一级种类")
    private String parentId;

    @ColumnHeader(title = "上一级", notNull = false, example = "全部种类", desc = "上一级车辆种类名称")
    @LinkName(table = "sys_veh_type", joinField = "parentId", desc = "车辆种类名称")
    private String parentName;

    @ColumnHeader(title = "种类名称", example = "新能源", desc = "种类名称长度为2-20个字符")
    @NotEmpty(message = "种类名称不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Length(min = 2, max = 20, message = "种类名称长度为2-20个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "种类名称")
    private String name;

    @NotNull(message = "种类性质不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "种类性质")
    private Integer attrCls;

    @NotBlank(message = "种类性质不能为空", groups = GroupExcelImport.class)
    @ColumnHeader(title = "种类性质", example = "乘用车", desc = "种类性质: 商用车、乘用车")
    @DictName(code = "VEH_TYPE_CLS", joinField = "attrCls")
    @ApiModelProperty(value = "种类性质名称")
    private String attrClsDisplay;

    @ColumnHeader(title = "值(CODE)", notNull = false, example = "XNY", desc = "值(CODE)为长度1-10个大写英文及数字")
    @NotEmpty(message = "值(CODE)不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = RegexUtil.E_M_1_10, message = "值(CODE)为长度1-10个大写英文及数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "值(CODE)")
    private String code;

    @ColumnHeader(title = "排序", notNull = false, example = "1", desc = "排序为0-9999之间")
    @Min(value = 0, message = "排序为范围0-9999", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 9999, message = "排序为范围0-9999", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ColumnHeader(title = "备注", notNull = false, example = "种类描述或补充备注", desc = "备注最大长度为100")
    @Pattern(regexp = RegexUtil.C_0_100, message = "备注最大长度为100", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "树路径")
    private String path;

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
     * @param entry VehType
     * @return VehTypeModel
     */
    public static VehTypeModel fromEntry(VehType entry) {
        VehTypeModel m = new VehTypeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }


}
