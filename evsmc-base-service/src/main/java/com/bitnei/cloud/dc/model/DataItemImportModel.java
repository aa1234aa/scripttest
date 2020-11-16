package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DataItemImportModel导入模型<br>
 * 描述： DataItemImportModel导入模型<br>
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
 * <td>2019-03-30 10:28:53</td>
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DataItemImportModel", description = "数据项导入Model")
public class DataItemImportModel extends BaseModel {

    @ColumnHeader(title = "数据项名称", example = "车辆登出流水号", notNull = true, desc = "数据项名称")
    @NotBlank(message = "数据项名称不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,32}|$", message = "数据项名称长度2~32个字符", groups = {GroupExcelImport.class})
    private String name;
    @ColumnHeader(title = "编号", example = "1033", notNull = true, desc = "编号")
    @NotBlank(message = "编号不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[0-9]{1,16}|$", message = "编号长度1~16个数字", groups = {GroupExcelImport.class})
    @DecimalMin(value = "20000", message = "编号必须是一个数字，其值必须大于等于20000", groups = { GroupExcelImport.class})
    @DecimalMax(value = "300000", message = "编号必须是一个数字，其值必须小于等于300000", groups = { GroupExcelImport.class})
    private String seqNo;
    @ColumnHeader(title = "数据项类型名称", example = "车辆登出", notNull = true, desc = "数据项类型名称")
    @NotBlank(message = "数据项类型名称不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,32}|$", message = "数据项类型名称长度为2~32个字符", groups = {GroupExcelImport.class})
    private String itemGroup;
    @ColumnHeader(title = "协议类型名称", example = "GB_T32960", notNull = true, desc = "协议类型名称")
    @NotBlank(message = "协议类型名称不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,64}|$", message = "协议类型名称长度2~64个字符", groups = {GroupExcelImport.class})
    private String ruleType;

    @NotNull(message = "开始BYTE位置不能为空", groups = { GroupExcelImport.class})
    @ColumnHeader(title = "开始BYTE位置", example = "1", notNull = true, desc = "开始BYTE位置")
    @Digits(integer = 5, fraction = 0, message = "开始BYTE位置长度1~5位", groups = {GroupExcelImport.class})
    @DecimalMin(value = "0", message = "开始BYTE位置必须是一个数字，其值必须大于等于0", groups = { GroupExcelImport.class})
    @DecimalMax(value = "65534", message = "开始BYTE位置必须是一个数字，其值必须小于等于65534", groups = { GroupExcelImport.class})
    private Integer byteStartPos;

    @NotNull(message = "开始BIT位置不能为空", groups = { GroupExcelImport.class})
    @ColumnHeader(title = "开始BIT位置", example = "2", notNull = true, desc = "开始BIT位置")
    @Digits(integer = 1, fraction = 0, message = "开始BIT位置长度1位", groups = {GroupExcelImport.class})
    @DecimalMin(value = "0", message = "开始BIT位置必须是一个数字，其值必须大于等于0", groups = { GroupExcelImport.class})
    @DecimalMax(value = "7", message = "开始BIT位置必须是一个数字，其值必须小于等于7", groups = { GroupExcelImport.class})
    private Integer bitStartPos;

    @ColumnHeader(title = "占BIT数长度", example = "1", notNull = true, desc = "占BIT数长度")
    @NotNull(message = "占BIT数长度不能为空", groups = { GroupExcelImport.class})
    @Digits(integer = 6, fraction = 0, message = "占BIT数长度长度1~6位", groups = {GroupExcelImport.class})
    @DecimalMin(value = "0", message = "占BIT数长度必须是一个数字，其值必须大于等于0", groups = { GroupExcelImport.class})
    @DecimalMax(value = "999999", message = "占BIT数长度必须是一个数字，其值必须小于等于999999", groups = { GroupExcelImport.class})
    private Integer length;
    @ColumnHeader(title = "系数", example = "1", notNull = false, desc = "系数")
    @Pattern(regexp = "^$|^\\d{1,7}(\\.\\d{1,10})?$", message = "系数整数长度1~7位,保留10位小数", groups = {GroupExcelImport.class})
    private String factor;
    @ColumnHeader(title = "偏移量", example = "0", notNull = false, desc = "偏移量")
    @Pattern(regexp = "^$|^-?\\d{1,6}$", message = "偏移量长度1~6位数字", groups = {GroupExcelImport.class})
    private String offset;
    @ColumnHeader(title = "数据精确度", example = "0", notNull = false, desc = "数据精确度")
    @Digits(integer = 1, fraction = 0, message = "数据精确度长度1位", groups = {GroupExcelImport.class})
    @DecimalMin(value = "0", message = "数据精确度必须是一个数字，其值必须大于等于0", groups = { GroupExcelImport.class})
    @DecimalMax(value = "9", message = "数据精确度必须是一个数字，其值必须小于等于9", groups = { GroupExcelImport.class})
    private Integer dot;
    @ColumnHeader(title = "单位", example = "个", notNull = false, desc = "单位")
    @Pattern(regexp = "^.{1,10}|$", message = "单位长度1~10个字符", groups = {GroupExcelImport.class})
    private String unit;
    @ColumnHeader(title = "数据类型", example = "WORD类型", notNull = false, desc = "数据类型")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,20}|$", message = "数据类型长度1~20个中英文、数字", groups = {GroupExcelImport.class})
    private String dataType;
    @ColumnHeader(title = "下限数值", example = "1", notNull = false, desc = "下限数值")
    @Pattern(regexp = "^$|^-?\\d{1,9}(\\.\\d{1,10})?$", message = "下限数值长度1~9位数字,保留10位小数", groups = {GroupExcelImport.class})
    private String upperLimit;
    @ColumnHeader(title = "上限数值", example = "2", notNull = false, desc = "上限数值")
    @Pattern(regexp = "^$|^-?\\d{1,9}(\\.\\d{1,10})?$", message = "上限数值长度1~9位数字,保留10位小数", groups = {GroupExcelImport.class})
    private String lowerLimit;
    @ColumnHeader(title = "高低位", example = "低位", notNull = false, desc = "高低位")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,10}|$", message = "高低位长度1~10个中文", groups = {GroupExcelImport.class})
    private String highLowFlg;
    @ColumnHeader(title = "是否启动解析", example = "是", notNull = false, desc = "是否启动解析")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,2}|$", message = "是否启动解析长度1个中文", groups = {GroupExcelImport.class})
    private String enableParseRule;
    @ColumnHeader(title = "是否显示", example = "是", notNull = false, desc = "是否显示")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,2}|$", message = "是否显示长度1个中文", groups = {GroupExcelImport.class})
    private String isShow;
    @ColumnHeader(title = "是否生效", example = "是", notNull = false, desc = "是否生效")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,2}|$", message = "是否生效长度1个中文", groups = {GroupExcelImport.class})
    private String isValid;
    @ColumnHeader(title = "是否数组", example = "否", notNull = false, desc = "是否数组")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,2}|$", message = "是否数组长度1个中文", groups = {GroupExcelImport.class})
    private String isArray;
    @ColumnHeader(title = "描述", example = "描述", notNull = false, desc = "描述")
    @Pattern(regexp = "^[\\s\\S]{0,100}|$", message = "描述长度为0~100个字符", groups = {GroupExcelImport.class})
    private String note;







}
