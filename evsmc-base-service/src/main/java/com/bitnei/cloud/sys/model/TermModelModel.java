package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.TermModel;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermModel新增模型<br>
 * 描述： TermModel新增模型<br>
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
 * <td>2018-11-05 10:01:40</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author zxz
 * @version 1.0
 * @since JDK1.8
 */
@Setter
@Getter
@ApiModel(value = "TermModelModel", description = "终端型号管理Model")
public class TermModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "终端型号", example = "TDD55TNSHA4", desc = "终端型号最大长度为100个字符")
    @NotEmpty(message = "终端型号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = ".{1,100}|", message = "终端型号长度为1-100个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "终端型号")
    private String termModelName;

    @NotNull(message = "种类不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Digits(integer = 1, fraction = 9999, message = "种类只能为数字，并且大小为1-9999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "种类")
    private Integer termCategory;

    @NotBlank(message = "终端种类不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "终端种类", example = "TBOX", desc = "终端种类选项:车机 TBOX OBD 智能后视镜 其他")
    @DictName(code = "TERM_TYPE", joinField = "termCategory")
    @ApiModelProperty(value = "种类名称")
    private String termCategoryDisplay;

    @ColumnHeader(title = "终端品牌", notNull = false, example = "鑫能", desc = "终端品牌长度为1-45个字符")
    @Pattern(regexp = ".{1,45}|", message = "终端品牌长度为1-45个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "终端品牌")
    private String brandName;

    @ColumnHeader(title = "工作额定电压(V)", notNull = false, example = "999", desc = "工作额定电压(V)长度为2-10个数字")
    @Pattern(regexp = "[0-9]{2,10}|", message = "工作额定电压(V)长度为2-10个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "工作额定电压(V)")
    private String workingVoltage;

    @ColumnHeader(title = "硬件版本号", notNull = false, example = "V3.15", desc = "硬件版本号长度为0-32个字符")
    @Pattern(regexp = ".{0,32}|", message = "硬件版本号长度为0-32个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "硬件版本号")
    private String firmwareVersion;

    @ColumnHeader(title = "功耗(W·h)", notNull = false, example = "358", desc = "功耗(W·h)长度为2-10个数字")
    @Pattern(regexp = "[0-9]{2,10}|", message = "功耗(W·h)长度为2-10个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "功耗(W·h)")
    private String powerWaste;

    @NotBlank(message = "终端生产企业不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "终端生产企业")
    private String unitId;

    @NotBlank(message = "终端生产企业不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "终端生产企业", example = "上海汽车新能源有限公司", desc = "终端生产企业")
    @LinkName(table = "sys_unit", joinField = "unitId", desc = "终端生产企业名称")
    private String unitName;

    @ColumnHeader(title = "休眠电流(mA)", notNull = false, example = "152", desc = "休眠电流(mA)长度为2-10个数字")
    @Pattern(regexp = "[0-9]{2,10}|", message = "休眠电流(mA)长度为2-10个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "休眠电流(mA)")
    private String sleepElectricCurrent;

    @ColumnHeader(title = "内置电池容量(Ah)", notNull = false, example = "100", desc = "内置电池容量(Ah)长度为2-10个数字")
    @Pattern(regexp = "[0-9]{2,10}|", message = "内置电池容量(Ah)长度为2-10个数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "内置电池容量(Ah)")
    private String builtInBatteryCapacity;

    @Pattern(regexp = ".{1,1024}|", message = "终端零件号长度为1-1024个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "终端零件号")
    private String termPartFirmwareNumbers;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @NotNull(message = "支持加密芯片不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "支持加密芯片")
    private Integer supportEncryptionChip;

    /** 支持加密芯片名称显示**/
    @ColumnHeader(title = "支持加密芯片", example = "否", notNull = true, desc = "支持加密芯片:0-否，1-是")
    @NotBlank(message = "支持加密芯片不能为空", groups = {GroupExcelImport.class})
    @DictName(code = "BOOL_TYPE", joinField = "supportEncryptionChip")
    @Pattern(regexp = "^.{1,10}|$", message = "支持加密芯片长度1-10个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "支持加密芯片名称")
    private String supportEncryptionChipDisplay;

    @Pattern(regexp = ".{0,50}|", message = "车载终端检测编号长度为0-50个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车载终端检测编号")
    private String termDetectionNo;

    @ApiModelProperty(value = "终端检测报告扫描件")
    private String detectionReportImgId;


    /**
     * 将实体转为前台model
     *
     * @param entry TermModel
     * @return TermModelModel
     */
    public static TermModelModel fromEntry(TermModel entry) {
        TermModelModel m = new TermModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
