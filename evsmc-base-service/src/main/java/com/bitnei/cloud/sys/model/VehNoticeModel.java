package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehNotice新增模型<br>
 * 描述： VehNotice新增模型<br>
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
 * <td>2018-11-12 14:50:08</td>
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
@ApiModel(value = "VehNoticeModel", description = "车辆公告Model")
public class VehNoticeModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "车辆公告型号", example = "GGXH001", desc = "车辆公告号长度为2-20个字符")
    @Length(min = 2, max = 20, message = "车辆公告号长度为2-20个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotEmpty(message = "车辆公告号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车辆公告号")
    private String name;

    @NotEmpty(message = "车辆种类不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆种类id")
    private String vehTypeId;

    /** 车辆种类名称 */
    @NotEmpty(message = "车辆种类不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车辆种类", example = "有轨电车", desc = "车辆种类")
    @LinkName(table = "sys_veh_type", joinField = "vehTypeId", desc = "车辆种类名称")
    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

    @ColumnHeader(title = "公告目录年份", example = "2019", desc = "公告目录年份长度为4个数字")
    @Range(min = 1000, max = 9999, message = "公告目录年份长度为4个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = "公告目录年份不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "公告目录年份")
    private Integer noticeYear;

    @ColumnHeader(title = "公告目录批次", example = "101", desc = "公告目录批次长度为1-4个数字")
    @Range(min = 1, max = 9999, message = "公告目录批次长度为1-4个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = "公告目录批次不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "公告目录批次")
    private Integer noticeBatch;

    @ColumnHeader(title = "推荐目录年份", example = "2019", desc = "推荐目录年份长度为4个数字")
    @Range(min = 1000, max = 9999, message = "推荐目录年份长度为4个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = "推荐目录年份不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "推荐目录年份")
    private Integer recommendYear;

    @ColumnHeader(title = "推荐目录批次", example = "1", desc = "推荐目录批次长度为1-4个数字")
    @Range(min = 1, max = 9999, message = "推荐目录批次长度为1-4个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @NotNull(message = "推荐目录批次不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "推荐目录批次")
    private Integer recommendBatch;

    @NotEmpty(message = "车辆品牌不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆品牌id")
    private String brandId;

    /**车辆品牌名称 */
    @NotEmpty(message = "品牌不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "品牌", example = "本田", desc = "车牌品牌")
    @LinkName(table = "sys_veh_brand", joinField = "brandId", desc = "车辆品牌名称")
    @ApiModelProperty(value = "车辆品牌名称")
    private String brandName;

    /** 车辆英文品牌名称 */
    @LinkName(table = "sys_veh_brand", column = "englist_name", joinField = "brandId", desc = "车辆英文品牌名称")
    @ApiModelProperty(value = "车辆英文品牌名称")
    private String brandEnglistName;

    @NotEmpty(message = "品牌车型系列不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车型系列id")
    private String seriesId;

    @NotEmpty(message = "车型系列不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车型系列", example = "雅阁", desc = "车牌品牌下的车型系列")
    @LinkName(table = "sys_veh_series", joinField = "seriesId", desc = "车系名称")
    @ApiModelProperty(value = "车型系列名称")
    private String seriesName;

    @ColumnHeader(title = "发布日期", notNull = false, example = "2019-04-12", desc = "发布日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "发布日期格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发布日期格式为yyyy-MM-dd")
    private String publishDate;

    @ApiModelProperty(value = "是否免征")
    private Integer isExempt;

    @ColumnHeader(title = "免征", notNull = false, example = "是", desc = "免征选项:[是|否],默认为是")
    @DictName(code = "BOOL_TYPE", joinField = "isExempt")
    @ApiModelProperty(value = "是否免征文字")
    private String isExemptDisplay;

    @ApiModelProperty(value = "是否燃油")
    private Integer isFuel;

    @ColumnHeader(title = "燃油", notNull = false, example = "是", desc = "燃油选项:[是|否],默认为是")
    @DictName(code = "BOOL_TYPE", joinField = "isFuel")
    @ApiModelProperty(value = "是否燃油")
    private String isFuelDisplay;

    @ApiModelProperty(value = "是否环保")
    private Integer isProtection;

    @ColumnHeader(title = "环保", notNull = false, example = "是", desc = "环保选项:[是|否],默认为是")
    @DictName(code = "BOOL_TYPE", joinField = "isProtection")
    @ApiModelProperty(value = "是否环保")
    private String isProtectionDisplay;

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
     *
     * @param entry VehNotice
     * @return VehNoticeModel
     */
    public static VehNoticeModel fromEntry(VehNotice entry) {
        VehNoticeModel m = new VehNoticeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
