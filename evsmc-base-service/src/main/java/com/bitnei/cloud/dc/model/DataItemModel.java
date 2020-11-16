package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.DataItem;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DataItem新增模型<br>
 * 描述： DataItem新增模型<br>
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
 * <td>2019-01-30 17:28:53</td>
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
@ApiModel(value = "DataItemModel", description = "数据项Model")
public class DataItemModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "名称")
    @NotBlank(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,32}|$", message = "名称长度为2~32个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "编号")
    @NotBlank(message = "编号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[0-9]{1,16}|$", message = "编号长度1~16个数字", groups = {GroupInsert.class,GroupUpdate.class})
    @DecimalMin(value = "20000", message = "编号必须是一个数字，其值必须大于等于20000", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "300000", message = "编号必须是一个数字，其值必须小于等于300000", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "编号")
    private String seqNo;

    @ColumnHeader(title = "数据项组ID")
    @ApiModelProperty(value = "数据项组ID")
    private String itemGroupId;

    @ColumnHeader(title = "数据类别")
    @ApiModelProperty(value = "数据类别")
    private Integer itemType;

    @ColumnHeader(title = "开始Byte位置")
    @ApiModelProperty(value = "开始Byte位置")
    @NotNull(message = "开始Byte位置不能为空", groups = { GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 5, fraction = 0, message = "开始Byte位置长度1~5位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "开始Byte位置必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "65534", message = "开始Byte位置必须是一个数字，其值必须小于等于65534", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer byteStartPos;

    @ColumnHeader(title = "开始Bit位置")
    @ApiModelProperty(value = "开始Bit位置")
    @NotNull(message = "开始Bit位置不能为空", groups = { GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 1, fraction = 0, message = "开始Bit位置长度1位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "开始Bit位置必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "7", message = "开始Bit位置必须是一个数字，其值必须小于等于7", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer bitStartPos;

    @ColumnHeader(title = "占BIT数长度")
    @ApiModelProperty(value = "占BIT数长度")
    @NotNull(message = "占BIT数长度不能为空", groups = { GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 6, fraction = 0, message = "占BIT数长度长度1~6位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "占BIT数长度必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "999999", message = "占BIT数长度必须是一个数字，其值必须小于等于999999", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer length;

    @ColumnHeader(title = "高低位")
    @ApiModelProperty(value = "高低位")
    private Integer highLowFlg;

    @ColumnHeader(title = "系数")
    @Pattern(regexp = "^$|^\\d{1,7}(\\.\\d{1,10})?$", message = "系数整数长度1~7位,保留10位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "系数")
    private String factor;

    @ColumnHeader(title = "偏移量")
    @Pattern(regexp = "^$|^-?\\d{1,6}$", message = "偏移量长度1~6位数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "偏移量")
    private String offset;

    @ColumnHeader(title = "单位")
    @Pattern(regexp = "^.{1,10}|$", message = "单位长度1~10个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单位")
    private String unit;

    @ColumnHeader(title = "精确度")
    @ApiModelProperty(value = "精确度")
    @Digits(integer = 1, fraction = 0, message = "精确度长度1位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "精确度必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "9", message = "精确度必须是一个数字，其值必须小于等于9", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer dot;

    @ColumnHeader(title = "序号")
    @ApiModelProperty(value = "序号")
    @Digits(integer = 8, fraction = 0, message = "序号长度1~8位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "序号必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "99999999", message = "序号必须是一个数字，其值必须小于等于99999999", groups = { GroupInsert.class,GroupUpdate.class})
    private Integer orderNum;

    @ColumnHeader(title = "描述")
    @Pattern(regexp = "^[\\s\\S]{0,100}|$", message = "描述长度为0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String note;

    @ColumnHeader(title = "上限值")
    @Pattern(regexp = "^$|^-?\\d{1,9}(\\.\\d{1,10})?$", message = "上限值长度1~9位数字,保留10位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "上限值")
    private String upperLimit;

    @ColumnHeader(title = "下限值")
    @Pattern(regexp = "^$|^-?\\d{1,9}(\\.\\d{1,10})?$", message = "下限值长度1~9位数字,保留10位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "下限值")
    private String lowerLimit;

    @ColumnHeader(title = "数据类型")
    @ApiModelProperty(value = "数据类型")
    private Integer dataType;

    @ColumnHeader(title = "是否数值")
    @ApiModelProperty(value = "是否数值")
    private Integer isnum;

    @ColumnHeader(title = "是否显示")
    @ApiModelProperty(value = "是否显示")
    private Integer isShow;

    @ColumnHeader(title = "是否监控项")
    @ApiModelProperty(value = "是否监控项")
    private Integer isMonitor;

    @ColumnHeader(title = "是否数组")
    @ApiModelProperty(value = "是否数组")
    private Integer isArray;

    @ColumnHeader(title = "是否启动解析")
    @ApiModelProperty(value = "是否启动解析")
    private Integer enableParseRule;

    @ColumnHeader(title = "是否自定义")
    @ApiModelProperty(value = "是否自定义")
    private Integer isCustom;

    @ColumnHeader(title = "是否生效")
    @ApiModelProperty(value = "是否生效")
    private Integer isValid;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "协议类型ID")
    @ApiModelProperty(value = "协议类型ID")
    private String ruleTypeId;

    /** 协议类型名称显示**/
    @LinkName(table = "dc_rule_type", column = "name", joinField = "ruleTypeId",desc = "")
    @ApiModelProperty(value = "协议类型名称")
    private String ruleTypeDisplay;

    /** 数据项组名称显示**/
    @LinkName(table = "dc_data_item_group", column = "name", joinField = "itemGroupId",desc = "")
    @ApiModelProperty(value = "数据项组名称")
    private String itemGroupDisplay;

    /** 高低位名称显示**/
    @DictName(code = "ENDIAN_TYPE", joinField = "highLowFlg")
    @ApiModelProperty(value = "高低位名称")
    private String highLowFlgDisplay;

    /** 数据类型名称显示**/
    @DictName(code = "DATA_TYPE", joinField = "dataType")
    @ApiModelProperty(value = "数据类型名称")
    private String dataTypeDisplay;

    /** 是否数值名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isnum")
    @ApiModelProperty(value = "是否数值名称")
    private String isnumDisplay;

    /** 是否显示名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isShow")
    @ApiModelProperty(value = "是否显示名称")
    private String isShowDisplay;

    /** 是否监控项名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isMonitor")
    @ApiModelProperty(value = "是否监控项名称")
    private String isMonitorDisplay;

    /** 是否数组名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isArray")
    @ApiModelProperty(value = "是否数组名称")
    private String isArrayDisplay;

    /** 是否启动解析名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "enableParseRule")
    @ApiModelProperty(value = "是否启动解析名称")
    private String enableParseRuleDisplay;

    /** 是否自定义名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isCustom")
    @ApiModelProperty(value = "是否自定义名称")
    private String isCustomDisplay;

    /** 是否生效名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "isValid")
    @ApiModelProperty(value = "是否生效名称")
    private String isValidDisplay;

    @ColumnHeader(title = "上级id")
    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ColumnHeader(title = "路径")
    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "是否数据项")
    private String isItem;

    @ColumnHeader(title = "数据项类型")
    @ApiModelProperty(value = "数据项类型")
    @LinkName(table = "dc_data_item_group", column = "path_name", joinField = "itemGroupId",desc = "")
    private String itemGroupName;

    @ApiModelProperty(value = "disabled")
    private String disabled;



    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataItemModel fromEntry(DataItem entry){
        DataItemModel m = new DataItemModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
