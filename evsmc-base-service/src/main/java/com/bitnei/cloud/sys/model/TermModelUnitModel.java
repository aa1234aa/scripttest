package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bitnei.cloud.sys.domain.TermModelUnit;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 终端编号模型<br>
* 描述： 终端编号模型<br>
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
* <td>2018-11-05 10:01:48</td>
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
@ApiModel(value = "TermModelUnitModel", description = "终端管理Model")
public class TermModelUnitModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "终端厂商自定义编号", example = "ZXZTERM5G17", notNull = true, desc = "终端厂商自定义编号")
    @NotBlank(message = "终端厂商自定义编号不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^.{1,50}$", message = "终端厂商自定义编号为1-50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "终端厂商自定义编号")
    private String serialNumber;

    @NotBlank(message = "终端型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "终端型号ID")
    private String sysTermModelId;

    @NotBlank(message = "终端型号不能为空", groups = GroupExcelImport.class)
    @ColumnHeader(title = "终端型号", example = "TD-D-55-TNSHA4", desc = "终端型号")
    @LinkName(table = "sys_term_model", column = "term_model_name", joinField = "sysTermModelId", desc = "终端型号名称")
    @ApiModelProperty(value = "终端型号名称")
    private String termModelName;

    @NotBlank(message = "支持通讯协议不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "支持通讯协议")
    private String supportProtocol;

    @NotBlank(message = "支持协议不能为空", groups = GroupExcelImport.class)
    @ColumnHeader(title = "支持协议", example = "GBT32960标准版", desc = "支持协议")
    @ApiModelProperty(value = "支持通讯协议名称")
    @LinkName(table = "dc_rule", joinField = "supportProtocol", desc = "支持通讯协议名称")
    private String supportProtocolName;

    @ColumnHeader(title = "协议版本号", notNull = false, example = "V3.15", desc = "协议版本号长度为2-20个字符")
    @Pattern(regexp = "^$|^.{0,20}$", message = "协议版本号长度为0-20个字符", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "协议版本号")
    private String protocolVersion;

    @ColumnHeader(title = "ICCID", example = "ICCIDVQ8S83T87OJ86B6", desc = "ICCID为20位中英文及数字")
    @NotBlank(message = "ICCID不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "ICCID")
    private String iccid;

    @ColumnHeader(title = "IMEI", notNull = false, example = "073987805771578", desc = "IMEI长度为15个数字")
    @Pattern(regexp = "^[0-9]{15}|$", message = "IMEI长度为15个数字", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "IMEI")
    private String imei;

    @ApiModelProperty(value = "终端零件号")
    private String termPartFirmwareNumber;
    
    @ColumnHeader(title = "固件版本号", notNull = false, example = "V3.56", desc = "固件版本号长度为2-20个字符")
    @Pattern(regexp = "^$|^.{0,20}$", message = "固件版本号长度为0-20个字符", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "固件版本号")
    private String firewareVersion;

    @ColumnHeader(title = "生产批次", notNull = false, example = "201901", desc = "生产批次为2-50个字符")
    @Pattern(regexp = "^.{0,50}|$", message = "生产批次为0-50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "生产批次")
    private String produceBatch;

    @ColumnHeader(title = "出厂日期", notNull = false, example = "2019-04-13", desc = "出厂日期格式为(yyyy-MM-dd或yyyy/MM/dd)")
    @Pattern(regexp = "^$|^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "出厂日期格式为(yyyy-MM-dd或yyyy/MM/dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "出厂日期")
    private String factoryDate;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    //add by renshuo 查询出的数据输出，保存时不用填写
    @ApiModelProperty(value = "msisd")
    private String msisd;

    //add by renshuo 查询出的数据输出，保存时不用填写
    @ApiModelProperty(value = "vin")
    private String vin;

    /** 终端单位 */
    @ApiModelProperty(value = "终端单位ID")
    private String unitId;

    /** 终端单位名称 */
    @LinkName(table = "sys_unit", column = "name", joinField = "unitId")
    @ApiModelProperty(value = "终端单位名称")
    private String unitName;

    /** 协议类型 */
    @ApiModelProperty(value = "协议类型id")
    private String ruleTypeId;

    @ApiModelProperty(value = "协议类型名称")
    @LinkName(table = "dc_rule_type", column = "name", joinField = "ruleTypeId")
    private String ruleTypeName;

    /** 加密芯片ID */
    @ApiModelProperty(value = "加密芯片ID")
    private String encryptionChipId;

    @ColumnHeader(title = "加密芯片ID", notNull = false, example = "ASDFGH", desc = "加密芯片ID")
    @ApiModelProperty(value = "加密芯片ID名称")
    @LinkName(table = "sys_encryption_chip", column = "identification_id", joinField = "encryptionChipId")
    private String encryptionChipName;

    /** 支持加密芯片**/
    @LinkName(table = "sys_term_model", column = "support_encryption_chip", joinField = "sysTermModelId", desc = "支持加密芯片",level = 1)
    @ApiModelProperty(value = "支持加密芯片")
    private String supportEncryptionChip;

    /** 支持加密芯片名称显示**/
    @DictName(code = "BOOL_TYPE", joinField = "supportEncryptionChip")
    @ApiModelProperty(value = "支持加密芯片名称")
    private String supportEncryptionChipDisplay;

    /** 终端入网证号 */
    @ApiModelProperty(value = "终端入网证号")
    @Pattern(regexp = "^$|^[a-zA-Z0-9]{12}$", message = "终端入网证号为12个英文及数字", groups = {GroupInsert.class, GroupUpdate.class})
    private String networkAccessNumber;
    /** 车载终端说明 */
    @Pattern(regexp = "^.{0,100}|$", message = "车载终端说明长度为0-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车载终端说明")
    private String termDescription;
    /** 车载终端其他文件 图片*/
    @ApiModelProperty(value = "车载终端其他文件")
    private String otherFile;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static TermModelUnitModel fromEntry(TermModelUnit entry){
        TermModelUnitModel m = new TermModelUnitModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
