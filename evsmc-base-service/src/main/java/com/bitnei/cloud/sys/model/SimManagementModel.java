package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.SimManagement;
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
* 功能： SimManagement新增模型<br>
* 描述： SimManagement新增模型<br>
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
* <td>2018-11-05 10:01:32</td>
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
@Setter
@Getter
@ApiModel(value = "SimManagementModel", description = "SIM卡管理Model")
public class SimManagementModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @NotNull(message = "运营商", groups = { GroupInsert.class,GroupUpdate.class})
    @Digits(integer = 1,fraction = 9999,message = "运营商只能为数字，并且大小为1-9999", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运营商")
    private Integer globalSimType;

    @ColumnHeader(title = "运营商", example = "移动", desc = "运营商选项:移动、联通、电信")
    @NotBlank(message = "运营商不能为空", groups = {GroupExcelImport.class})
    @DictName(code = "CARRIER_TYPE", joinField = "globalSimType")
    @ApiModelProperty(value = "运营商名称")
    private String globalSimTypeDisplay;

    @ColumnHeader(title = "ICCID", example = "ICCIDVQ8S83T87OJ8AAA", desc = "ICCID长度为20个中英文或数字")
    @NotEmpty(message = "ICCID不能为空", groups = { GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[a-zA-Z0-9]{20}$", message = "ICCID长度为20个英文或数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "ICCID")
    private String iccid;

    @ColumnHeader(title = "移动用户号(MSISDN)", notNull = false, example = "15877778888", desc = "移动用户号(MSISDN)长度为9-15个英文或数字")
    @Pattern(regexp = "^[a-zA-Z0-9]{9,15}|$", message = "移动用户号(MSISDN)长度为9-15个英文或数字", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "移动用户号(MSISDN)")
    private String msisd;

    @ApiModelProperty(value = "激活状态")
    private Integer isActive;

    @ApiModelProperty(value = "激活状态名称")
    private String activeDisplay;

    @ApiModelProperty(value = "已用流量")
    private Double mb;

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
     * @param entry SimManagement
     * @return SimManagementModel
     */
    public static SimManagementModel fromEntry(SimManagement entry){
        SimManagementModel m = new SimManagementModel();
        BeanUtils.copyProperties(entry, m);
        if(Constant.TrueAndFalse.TRUE.equals(m.getIsActive())) {
            m.setActiveDisplay("已激活");
        } else {
            m.setActiveDisplay("未激活");
        }
        return m;
    }

}
