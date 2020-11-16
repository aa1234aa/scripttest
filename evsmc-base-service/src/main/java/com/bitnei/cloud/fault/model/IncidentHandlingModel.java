package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.fault.domain.IncidentHandling;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： IncidentHandling新增模型<br>
* 描述： IncidentHandling新增模型<br>
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
* <td>2019-07-03 16:32:41</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "IncidentHandlingModel", description = "车型事故处置预案Model")
public class IncidentHandlingModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "车型id")
    @NotEmpty(message = "车辆型号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private String vehModelId;

    @ApiModelProperty(value = "文档名称")
    @Length(max = 128, message = "文档名称最大字符长度为128")
    private String documentName;

    @ApiModelProperty(value = "车辆配置名称")
    private String configName;

    @ApiModelProperty(value = "文档类型")
    @NotNull(message = "文档类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private Integer documentType;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "上报状态")
    private Integer reportState;

    @ApiModelProperty(value = "失败原因")
    private String reasonsForFailure;

    @ApiModelProperty(value = "上报时间")
    private String reportTime;

    @ColumnHeader(title = "上报平台")
    @ApiModelProperty(value = "上报平台")
    private Integer platform;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "主要部件信息")
    @Length(max = 1000, message = "主要部件信息最大字符长度为1000")
    private String componentInformation;

    @ApiModelProperty(value = "救援行动")
    @Length(max = 1000, message = "救援行动最大字符长度为1000")
    private String rescue;

    @ApiModelProperty(value = "打开车辆")
    @Length(max = 1000, message = "打开车辆最大字符长度为1000")
    private String openVehicle;

    @ApiModelProperty(value = "禁止行为")
    @Length(max = 1000, message = "禁止行为最大字符长度为1000")
    private String prohibitoryActs ;

    @ApiModelProperty(value = "附件id")
    @NotEmpty(message = "附件不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private String fileId;

    @ApiModelProperty(value = "文档描述")
    @NotEmpty(message = "文档描述不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Length(max = 17, message = "文档描述最大字符长度为17")
    private String documentDescribe;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    @DictName(code = "FAULT_REPORT_STATUS", joinField = "reportState")
    @ApiModelProperty(value = "上报状态名称")
    private String reportStateName;

    @DictName(code = "DOCUMENT_TYPE", joinField = "documentType")
    @ApiModelProperty(value = "文档类型名称")
    private String documentTypeName;

    /** 附件名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "fileId", desc = "")
    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @DictName(code = "FAULT_REPORT_UNIT", joinField = "platform")
    @ApiModelProperty(value = "上报平台名称")
    private String platformName;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static IncidentHandlingModel fromEntry(IncidentHandling entry){
        IncidentHandlingModel m = new IncidentHandlingModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

    public boolean isReportSuccess(){
        if (null==reportState){
            return false;
        }

        return reportState==4;
    }
}
