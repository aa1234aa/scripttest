package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.EncryptionChip;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EncryptionChip新增模型<br>
* 描述： EncryptionChip新增模型<br>
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
* <td>2019-07-03 20:07:23</td>
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
@ApiModel(value = "EncryptionChipModel", description = "加密芯片Model")
public class EncryptionChipModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "安全芯片标识ID", example = "FGHJK", notNull = true, desc = "加密芯片唯一标识")
    @NotBlank(message = "安全芯片标识ID不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Pattern(regexp = "^.{13}|$", message = "安全芯片标识ID长度为13个字符", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "安全芯片标识ID")
    private String identificationId;

    @NotBlank(message = "芯片型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "芯片型号ID")
    private String chipModelId;

    @ColumnHeader(title = "芯片型号", example = "CVBNM", notNull = true, desc = "该芯片所属型号信息")
    @LinkName(table = "sys_encryption_chip_model", column = "name", joinField = "chipModelId",desc = "")
    @NotBlank(message = "芯片型号不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{0,128}|$", message = "芯片型号长度0-128个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "芯片型号名称")
    private String chipModelDisplay;

    /** 防篡改信息备案状态:0-备案失败，1-备案成功，2-未备案**/
    @ApiModelProperty(value = "防篡改信息备案状态")
    private Integer filingStatus;

    /** 防篡改信息备案状态名称显示**/
    @DictName(code = "FILING_STATUS", joinField = "filingStatus")
    @ApiModelProperty(value = "防篡改信息备案状态名称")
    private String filingStatusDisplay;

    @ApiModelProperty(value = "私钥灌装状态")
    private Integer privateKeyStatus;

    /** 私钥灌装状态名称显示**/
    @ColumnHeader(title = "私钥灌装状态", example = "未灌装", notNull = false, desc = "私钥灌装状态:1-未灌装，2-已灌装")
    @DictName(code = "PRIVATE_KEY_FILING_STATUS", joinField = "privateKeyStatus")
    @Pattern(regexp = "^.{0,10}|$", message = "私钥灌装状态长度0-10个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "私钥灌装状态名称")
    private String privateKeyStatusDisplay;

    @ColumnHeader(title = "终端公钥", example = "WERTYU", notNull = true, desc = "十六进制;终端内已存储的加密芯片生成的该终端的密钥对的公钥信息")
    @NotBlank(message = "终端公钥不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @Pattern(regexp = "^.{1,128}|$", message = "终端公钥长度为1-128个字符", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "终端公钥")
    private String termPublicKey;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 芯片厂商ID**/
    @LinkName(table = "sys_encryption_chip_model", column = "unit_id", joinField = "chipModelId",desc = "",level = 1)
    @ApiModelProperty(value = "芯片厂商ID")
    private String ecmUnitId;
    /** 芯片厂商名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "ecmUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "芯片厂商名称")
    private String ecmUnitDisplay;

    /** 国家备案编码**/
    @LinkName(table = "sys_encryption_chip_model", column = "filing_code", joinField = "chipModelId",desc = "",level = 1)
    @ApiModelProperty(value = "国家备案编码")
    private String filingCode;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static EncryptionChipModel fromEntry(EncryptionChip entry){
        EncryptionChipModel m = new EncryptionChipModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
