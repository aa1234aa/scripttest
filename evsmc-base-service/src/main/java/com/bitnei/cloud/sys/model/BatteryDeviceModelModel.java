package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.BatteryDeviceModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： BatteryDeviceModel新增模型<br>
* 描述： BatteryDeviceModel新增模型<br>
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
* <td>2018-11-14 13:43:57</td>
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
@Setter
@Getter
@ApiModel(value = "BatteryDeviceModelModel", description = "动力蓄电池型号Model")
public class BatteryDeviceModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "动力蓄电池型号", example = "XY400-1", notNull = true, desc = "动力蓄电池型号")
    @NotBlank(message = "动力蓄电池型号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "动力蓄电池型号长度2-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "动力蓄电池型号")
    private String name;

    @NotNull(message = "电池种类不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "电池种类(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池)")
    private Integer batteryType;

    /** 电池种类字典名称显示**/
    @ColumnHeader(title = "电池种类", example = "三元材料电池", notNull = true, desc = "电池种类(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池)")
    @NotBlank(message = "电池种类不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "电池种类长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "BATTERY_TYPE", joinField = "batteryType")
    @ApiModelProperty(value = "电池种类名称")
    private String  batteryTypeDisplay;


    @ColumnHeader(title = "总成标称电压(V)", example = "380", notNull = false, desc = "总成标称电压(V)")
    @Digits(integer = 10, fraction = 0, message = "总成标称电压(V)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "总成标称电压(V)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "总成标称电压(V)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "总成标称电压(V)")
    private Double fixedVol;

    @ColumnHeader(title = "总储电量(kW.h)", example = "62", notNull = true, desc = "总储电量(kW.h)")
    @NotNull(message = "总储电量(kW.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Digits(integer = 10, fraction = 0, message = "总储电量(kW.h)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "总储电量(kW.h)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "总储电量(kW.h)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "总储电量(kW.h)")
    private Double totalElecCapacity;

    @ColumnHeader(title = "系统能量密度(Wh/kg)", example = "141.3", notNull = true, desc = "系统能量密度(Wh/kg)")
    @NotNull(message = "系统能量密度(Wh/kg)不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Digits(integer = 10, fraction = 0, message = "系统能量密度(Wh/kg)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "系统能量密度(Wh/kg)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "系统能量密度(Wh/kg)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "系统能量密度(Wh/kg)")
    private Double capacityDensity;

    @ColumnHeader(title = "额定容量(Ah)", example = "80", notNull = false, desc = "额定容量(Ah)")
    @Digits(integer = 10, fraction = 0, message = "额定容量(Ah)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定容量(Ah)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "额定容量(Ah)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "额定容量(Ah)")
    private Double fixedAh;

    @ColumnHeader(title = "额定总质量(kg)", example = "450", notNull = false, desc = "额定总重量(kg)")
    @Digits(integer = 10, fraction = 0, message = "额定总质量(kg)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定总质量(kg)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "额定总质量(kg)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "额定总质量(kg)")
    private Double fixedKg;

    @ColumnHeader(title = "额定输出电流(A)", example = "100", notNull = false, desc = "额定输出电流(A)")
    @Digits(integer = 10, fraction = 0, message = "额定输出电流(A)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "额定输出电流(A)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "额定输出电流(A)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "额定输出电流(A)")
    private Double fixedOutputA;

    @ColumnHeader(title = "最高允许温度", example = "290", notNull = false, desc = "最高允许温度")
    @Digits(integer = 10, fraction = 0, message = "最高允许温度整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "最高允许温度必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "最高允许温度必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "最高允许温度")
    private Double maxTempC;

    @ApiModelProperty(value = "串并联方式 1:串联 2:并联")
    private Integer linkMode;

    /** 串并联方式字典名称显示**/
    @ColumnHeader(title = "串并联方式", example = "串联", notNull = false, desc = "串并联方式 1:串联 2:并联")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "串并联方式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "BATTERY_LINK_MODE", joinField = "linkMode")
    @ApiModelProperty(value = "串并联方式名称")
    private String linkModeDisplay;

    @ColumnHeader(title = "充电循环次数", example = "2000", notNull = false, desc = "充电循环次数")
    @Digits(integer = 5, fraction = 0, message = "充电循环次数整数长度1~5位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "充电循环次数必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "99999", message = "充电循环次数必须是一个数字，其值必须小于等于99999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "充电循环次数")
    private Integer chgCycleCount;

    @ColumnHeader(title = "充电倍率", example = "1C", notNull = false, desc = "充电倍率")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "充电倍率长度2-10个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "充电倍率")
    private String chgRate;

    @ColumnHeader(title = "最大允许充电容量", example = "63", notNull = false, desc = "最大允许充电容量")
    @Digits(integer = 10, fraction = 0, message = "最大允许充电容量整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "最大允许充电容量必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "最大允许充电容量必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "最大允许充电容量")
    private Double chgMaxKwh;

    @ColumnHeader(title = "最高允许充电总电压(V)", example = "500", notNull = false, desc = "最高允许充电总电压(V)")
    @Digits(integer = 10, fraction = 0, message = "最高允许充电总电压(V)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "最高允许充电总电压(V)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "最高允许充电总电压(V)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "最高允许充电总电压(V)")
    private Double chgMaxV;

    @ColumnHeader(title = "最大允许充电电流(A)", example = "120", notNull = false, desc = "最大允许充电电流(A)")
    @Digits(integer = 10, fraction = 0, message = "最大允许充电电流(A)整数长度1~10位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "最大允许充电电流(A)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "9999999999", message = "最大允许充电电流(A)必须是一个数字，其值必须小于等于9999999999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "最大允许充电电流(A)")
    private Double chgMaxA;

    @ColumnHeader(title = "探针个数(个)", example = "64", notNull = false, desc = "探针个数(个)")
    @Digits(integer = 5, fraction = 0, message = "探针个数(个)整数长度1~5位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "探针个数(个)必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "99999", message = "探针个数(个)必须是一个数字，其值必须小于等于99999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "探针个数(个)")
    private Integer tempMonitorCount;

    @ColumnHeader(title = "车载能源管理系统型号", example = "BMS-1", notNull = false, desc = "车载能源管理系统型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "车载能源管理系统型号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车载能源管理系统型号")
    private String bmsModel;

    @ColumnHeader(title = "单体数量", example = "2410", notNull = false, desc = "单体数量")
    @Digits(integer = 5, fraction = 0, message = "单体数量整数长度1~5位", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @DecimalMin(value = "0", message = "单体数量必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @DecimalMax(value = "99999", message = "单体数量必须是一个数字，其值必须小于等于99999", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "单体数量")
    private Integer batteryCount;

    @ColumnHeader(title = "单体型号", example = "18650", notNull = false, desc = "单体型号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "单体型号长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "单体型号")
    private String batteryCellModel;

    @ApiModelProperty(value = "封装方式 1:圆柱形电池 2:方形电池 3:软包电池 4:固态电池")
    private Integer cellPackageMode;

    /** 封装方式字典名称显示**/
    @ColumnHeader(title = "封装方式", example = "圆柱形电池", notNull = false, desc = "封装方式 1:圆柱形电池 2:方形电池 3:软包电池 4:固态电池")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "封装方式长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "CELL_PACKGE_MODE", joinField = "cellPackageMode")
    @ApiModelProperty(value = "封装方式名称")
    private String  cellPackageModeDisplay;

    @NotBlank(message = "储能装置生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "储能装置生产企业")
    private String unitId;



    /** 储能装置生产企业关联表名称显示**/
    @ColumnHeader(title = "储能装置生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "储能装置生产企业")
    @ApiModelProperty(value = "储能装置生产企业名称")
    @NotBlank(message = "储能装置生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "储能装置生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "unitId",desc = "")
    private String unitIdDisplay;


    @NotBlank(message = "单体生产企业不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "单体生产企业")
    private String batteryCellUnitId;

    /** 单体生产企业关联表名称显示**/
    @ColumnHeader(title = "单体生产企业", example = "海马汽车零配件有限公司", notNull = true, desc = "单体生产企业")
    @ApiModelProperty(value = "单体生产企业名称")
    @NotBlank(message = "单体生产企业不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "单体生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "batteryCellUnitId",desc = "")
    private String batteryCellUnitIdDisplay;

    @ApiModelProperty(value = "车载能源管理系统生产企业")
    private String bmsUnitId;

    /** 车载能源管理系统生产企业关联表名称显示**/
    @ColumnHeader(title = "车载能源管理系统生产企业", example = "海马汽车零配件有限公司", notNull = false, desc = "车载能源管理系统生产企业")
    @ApiModelProperty(value = "车载能源管理系统生产企业名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{5,30}|$", message = "车载能源管理系统生产企业长度5-30个中英文、数字", groups = {GroupExcelImport.class})
    @LinkName(table = "sys_unit", column = "name", joinField = "bmsUnitId",desc = "")
    private String bmsUnitIdDisplay;

    @ColumnHeader(title = "尺寸", example = "11/12/8", notNull = false, desc = "尺寸(长/宽/高[mm])")
    @Pattern(regexp = "^[/0-9]{1,30}|$", message = "尺寸长度1-30个数字，数字之间/分隔", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "尺寸(长/宽/高[mm])")
    private String appearanceSize;


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
     * @param entry
     * @return
     */
    public static BatteryDeviceModelModel fromEntry(BatteryDeviceModel entry){
        BatteryDeviceModelModel m = new BatteryDeviceModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
