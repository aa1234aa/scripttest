package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.EncryptionChipModel;
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
* 功能： EncryptionChipModel新增模型<br>
* 描述： EncryptionChipModel新增模型<br>
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
* <td>2019-07-03 20:06:31</td>
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
@ApiModel(value = "EncryptionChipModelModel", description = "加密芯片型号Model")
public class EncryptionChipModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "芯片型号")
    @NotBlank(message = "芯片型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{1,128}|$", message = "芯片型号长度为1-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "芯片型号")
    private String name;

    @ColumnHeader(title = "芯片厂商")
    @NotBlank(message = "芯片厂商不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "芯片厂商")
    private String unitId;

    @ColumnHeader(title = "芯片厂商名称")
    @LinkName(table = "sys_unit", column = "name", joinField = "unitId",desc = "")
    @ApiModelProperty(value = "芯片厂商名称")
    private String unitDisplay;

    /** 芯片生产企业机构代码显示**/
    @LinkName(table = "sys_unit", column = "organization_code", joinField = "unitId",desc = "")
    @ApiModelProperty(value = "芯片生产企业机构代码")
    private String organizationCode;

    /** 联系人姓名显示**/
    @LinkName(table = "sys_unit", column = "contactor_name", joinField = "unitId",desc = "")
    @ApiModelProperty(value = "联系人姓名")
    private String contactorName;

    /** 联系人电话显示**/
    @LinkName(table = "sys_unit", column = "contactor_phone", joinField = "unitId",desc = "")
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    @ColumnHeader(title = "厂商说明")
    @Pattern(regexp = "^.{0,100}|$", message = "厂商说明长度为0-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "厂商说明")
    private String unitDeclare;

    @ColumnHeader(title = "厂商执照图片")
    @ApiModelProperty(value = "厂商执照图片")
    private String licenceImgId;

    @ColumnHeader(title = "芯片资质图片")
    @ApiModelProperty(value = "芯片资质图片")
    private String qualificationImgId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "芯片资质图片国密")
    @ApiModelProperty(value = "芯片资质图片国密")
    private String isoSecretImgId;

    @ColumnHeader(title = "芯片附件")
    @ApiModelProperty(value = "芯片附件")
    private String chipAttachmentId;
    /** 芯片附件名称显示**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "chipAttachmentId",desc = "")
    @ApiModelProperty(value = "芯片附件名称")
    private String chipAttachmentName;

    @ColumnHeader(title = "芯片说明")
    @Pattern(regexp = "^.{0,100}|$", message = "芯片说明长度为0-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "芯片说明")
    private String chipDeclare;

    @ColumnHeader(title = "芯片资质图片9001")
    @ApiModelProperty(value = "芯片资质图片9001")
    private String iso9001ImgId;

    @ApiModelProperty(value = "国家备案编码")
    private String filingCode;

    /** 厂商授权证书 **/
    @ApiModelProperty(value = "厂商授权证书")
    private String authorizationFileId;

    /** 备案状态(字典项:CHIP_FILING_STATUS) **/
    @ApiModelProperty(value = "备案状态")
    private Integer filingStatus;

    /** 备案状态名称显示 **/
    @DictName(code = "CHIP_FILING_STATUS", joinField = "filingStatus")
    @ApiModelProperty(value = "芯片型号备案状态名称")
    private String filingStatusDisplay;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static EncryptionChipModelModel fromEntry(EncryptionChipModel entry){
        EncryptionChipModelModel m = new EncryptionChipModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
