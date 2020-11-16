package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EncryptionChipModel实体<br>
* 描述： EncryptionChipModel实体<br>
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
public class EncryptionChipModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 芯片型号 **/
    private String name;
    /** 芯片厂商 **/
    private String unitId;
    /** 厂商说明 **/
    private String unitDeclare;
    /** 厂商执照图片 **/
    private String licenceImgId;
    /** 芯片资质图片 **/
    private String qualificationImgId;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 芯片资质图片国密 **/
    private String isoSecretImgId;
    /** 芯片附件 **/
    private String chipAttachmentId;
    /** 芯片说明 **/
    private String chipDeclare;
    /** 芯片资质图片9001 **/
    private String iso9001ImgId;
    /** 国家备案编码 **/
    private String filingCode;
    /** 厂商授权证书 **/
    private String authorizationFileId;
    /** 备案状态(字典项:CHIP_FILING_STATUS) **/
    private Integer filingStatus;

}
