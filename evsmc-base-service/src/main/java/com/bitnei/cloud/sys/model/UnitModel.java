package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Unit;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： Unit新增模型<br>
 * 描述： Unit新增模型<br>
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
 * <td>2018-11-05 17:33:20</td>
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
@Setter
@Getter
@ApiModel(value = "UnitModel", description = "单位列表Model")
public class UnitModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "单位名称", example = "北京理工新源信息科技有限公司", desc = "5-30位中英文数字及特殊字符")
    @NotEmpty(message = "单位名称不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = RegexUtil.C_5_30, message = "单位名称长度为5~30位", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "单位名称")
    private String name;

    @ColumnHeader(title = "单位简称", notNull = false, example = "理工新源", desc = "单位简称为2~10位中英文或数字")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "单位简称为2~10位中英文或数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "单位简称")
    private String nickName;

    /** 单位类型id，多个用逗号分隔 */
    @NotNull(message = "单位类型不可为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单位类型，多个用逗号分隔，不可为空")
    private String unitTypeIds;

    /** 单位类型名称，多个用逗号分隔 */
    @ColumnHeader(title = "单位类型", example = "科研单位,运营单位", desc = "单位类型名称,多个用逗号分隔")
    @ApiModelProperty(value = "单位类型名称，多个用逗号分隔，不可为空")
    @NotEmpty(message = "单位类型不能为空", groups = GroupExcelImport.class)
    private String unitTypeNames;

    @ApiModelProperty(value = "是否根节点")
    private Integer isRoot;

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ColumnHeader(title = "上级单位", desc = "上级单位",notNull = false)
    @ApiModelProperty(value = "父节点名称")
    @LinkName(table = "sys_unit", joinField = "parentId")
    private String parentName;

    @ColumnHeader(title = "单位地址", example = "北京理工大学附近", desc = "单位地址长度为4~50个字符")
    @NotEmpty(message = "单位地址不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{4,50}$", message = "单位地址长度为4~50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "单位地址")
    private String address;

    @ColumnHeader(title = "座机号", notNull = false, example = "8780577", desc = "座机号长度为5~11个字符")
    @Pattern(regexp = "^$|^[0-9-]{5,11}$", message = "座机号长度为5~11个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "座机号")
    private String telephone;

    @ColumnHeader(title = "业务种类", notNull = false, example = "新能源电池,充电桩", desc = "业务种类长度为0~120个字符")
    @Length(max = 120, message = "业务种类长度为0-120个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "业务种类")
    private String bussinessLines;

    @ColumnHeader(title = "产品品牌", notNull = false, example = "新能源", desc = "产品品牌长度为0~64个字符")
    @Length(max = 64, message = "产品品牌长度为0~64个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "产品品牌")
    private String brands;

    @ColumnHeader(title = "统一社会信用代码", notNull = false, example = "MMMNNNBBBVVVCCXXZ4", desc = "统一社会信用代码为18位英文或数字")
    @Pattern(regexp = "^[a-zA-Z0-9]{18}|$", message = "统一社会信用代码为18位英文或数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "统一社会信用代码")
    private String organizationCode;

    @ColumnHeader(title = "联系人(法人)姓名", example = "周星星", desc = "联系人名称为2~20位中英文或数字")
    @NotEmpty(message = "联系人姓名不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,20}|$", message = "联系人名称为2~20位中英文或数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "联系人姓名")
    private String contactorName;

    @ColumnHeader(title = "联系人手机号", example = "15875647070", desc = "联系人手机为11位数字")
    @NotEmpty(message = "联系人手机号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[1][3,4,5,7,8,9][0-9]{9}$", message = "联系人手机为11位数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "联系人手机号")
    private String contactorPhone;

    @ColumnHeader(title = "联系人地址", notNull = false, example = "北京理工大学附近", desc = "联系人地址长度为4~50位字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9()（）]{4,50}|$", message = "联系人地址长度为4~50位中英文及数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "联系人地址")
    private String contactorAddress;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "营业执照扫描图片id")
    private String licenceImgId;

    @ApiModelProperty(value = "授权书扫描件ID")
    private String certImgId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "经度",notNull = false, example = "105.980581", desc = "经度为-180到180之间的数值类型")
    @Pattern(regexp = "^$|^-?\\d{1,3}(\\.\\d{1,6})?$", message = "经度长度1~3位数字,保留6位小数", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "经度")
    private String lng;
    @ColumnHeader(title = "纬度",notNull = false, example = "30.777889", desc = "纬度为-90到90之间的数值类型")
    @Pattern(regexp = "^$|^-?\\d{1,2}(\\.\\d{1,6})?$", message = "纬度长度1~2位数字,保留6位小数", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "纬度")
    private String lat;

    /**
     * 将实体转为前台model
     * @param entry entry
     * @return UnitModel
     */
    public static UnitModel fromEntry(Unit entry) {
        UnitModel m = new UnitModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
