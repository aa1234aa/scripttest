package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.bitnei.cloud.sys.model.VehicleEngeryDeviceLkModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.fault.domain.SafetyAccidents;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SafetyAccidents新增模型<br>
* 描述： SafetyAccidents新增模型<br>
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
* <td>2019-07-02 16:50:26</td>
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
@ApiModel(value = "SafetyAccidentsModel", description = "安全事故管理Model")
public class SafetyAccidentsModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ColumnHeader(title = "VIN码", example = "LA9G3MBD9JSWXB070", desc = "VIN码")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "vid")
    private String vid;

    @ColumnHeader(title = "事故发生时间", example = "2019-07-06 10:08:45", desc = "事故发生时间")
    @NotEmpty(message = "事故发生时间不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "事故发生时间")
    private String time;

    @ColumnHeader(title = "可充电储能装置编码", example = "ERV20171002", desc = "可充电储能装置编码")
    @ApiModelProperty(value = "可充电储能装置编码")
    private String engeryDeviceNames;

    @ColumnHeader(title = "事故发生时车辆总里程", example = "1234", desc = "事故发生时车辆总里程")
    @NotEmpty(message = "事故发生时车辆总里程不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "事故发生时车辆总里程")
    private String vehicleTotalMileage;

    @ApiModelProperty(value = "事故发生时车辆状态")
    private Integer vehicleState;

    @ColumnHeader(title = "事故发生时车辆状态", example = "运行", desc = "事故发生时车辆状态")
    @ApiModelProperty(value = "事故发生时车辆状态名称")
    @DictName(code = "FAULT_VEHICLE_STATUS", joinField = "vehicleState")
    private String vehicleStateName;

    @ColumnHeader(title = "故障情况", example = "故障情况", desc = "故障情况")
    @NotEmpty(message = "故障情况不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "故障情况")
    @Length(max = 200, message = "故障情况最大字符长度为200")
    private String faultSituation;

    @ColumnHeader(title = "事故表征", example = "事故表征", desc = "事故表征")
    @NotEmpty(message = "事故表征不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "事故表征")
    @Length(max = 500, message = "事故表征最大字符长度为500")
    private String representation;

    @ColumnHeader(title = "事故影响", example = "事故影响", desc = "事故影响")
    @NotEmpty(message = "事故影响不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "事故影响")
    @Length(max = 500, message = "事故影响最大字符长度为500")
    private String accident;

    @ColumnHeader(title = "事故原因初判", example = "事故原因初判", desc = "事故原因初判")
    @NotEmpty(message = "事故原因初判不能为空", groups = { GroupInsert.class,GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "事故原因初判")
    @Length(max = 500, message = "事故原因初判最大字符长度为500")
    private String reason;

    @NotNull(message = "是否关联上报车型处置预案不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否关联上报车型处置预案")
    private Integer relation;

    @ApiModelProperty(value = "事故上报状态")
    private Integer reportState;

    @ApiModelProperty(value = "上报失败原因")
    private String reasonsForFailure;

    @ApiModelProperty(value = "上报平台")
    private Integer platform;

    @ApiModelProperty(value = "最后操作人")
    private String finalOperator;

    @ApiModelProperty(value = "操作时间")
    private String operatingTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ApiModelProperty(value = "电池单体型号")
    private String batteryCellModels;

    @ApiModelProperty(value = "单体生产企业")
    private String batteryCellUnit;

    @ApiModelProperty(value = "可充电储能装置型号")
    private String batteryModel;

    @ApiModelProperty(value = "储能装置生产企业")
    private String engeryDeviceUnit;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "运营区域id")
    private String operAreaId;

    @ApiModelProperty(value = "车辆生产日期")
    private String produceDate;

    @ApiModelProperty(value = "车辆销售日期")
    private String sellDate;

    // 原型修改 不多个
    @NotEmpty(message = "可充电储能装置不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "可充电储能装置id列表,多个逗号间隔")
    private String engeryDeviceIds;

    @ApiModelProperty(value = "监管数据文件id")
    private String superviseDataFileId;

    @ApiModelProperty(value = "分析报告文件id")
    private String analysisReportFileId;

    @ApiModelProperty(value = "照片1")
    private String photo1Id;

    @ApiModelProperty(value = "照片2")
    private String photo2Id;

    @ApiModelProperty(value = "照片3")
    private String photo3Id;

    @ApiModelProperty(value = "照片4")
    private String photo4Id;

    @ApiModelProperty(value = "报告号")
    private String reportNumber;

    @ApiModelProperty(value = "监管数据上报状态")
    private Integer dataReportState;

    @ApiModelProperty(value = "分析报告上传状态")
    private Integer reportUploadState;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;
    /** 运营单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;
    /** 运营区域名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operAreaId", desc = "")
    private String operAreaName;
    /** 监管数据文件名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "superviseDataFileId", desc = "")
    @ApiModelProperty(value = "监管数据文件名称")
    private String superviseDataFileName;

    /** 分析报告文件名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "analysisReportFileId", desc = "")
    @ApiModelProperty(value = "分析报告文件名称")
    private String analysisReportFileName;



    @DictName(code = "BOOL_TYPE", joinField = "relation")
    @ApiModelProperty(value = "是否关联上报车型处置预案(是|否)")
    private String relationName;

    @DictName(code = "FAULT_REPORT_STATUS", joinField = "reportState")
    @ApiModelProperty(value = "事故上报状态")
    private String reportStateName;

    @DictName(code = "DATA_REPORT_STATE", joinField = "dataReportState")
    @ApiModelProperty(value = "监管数据上报状态名称")
    private String dataReportStateName;

    @DictName(code = "REPORT_UPLOAD_STATE", joinField = "reportUploadState")
    @ApiModelProperty(value = "分析报告上传状态名称")
    private String reportUploadStateName;

    @DictName(code = "FAULT_REPORT_UNIT", joinField = "platform")
    @ApiModelProperty(value = "事故上报单位名称")
    private String platformName;

    /** 照片1名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "photo1Id", desc = "")
    @ApiModelProperty(value = "照片1名称")
    private String photo1Name;

    /** 照片2名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "photo2Id", desc = "")
    @ApiModelProperty(value = "照片2名称")
    private String photo2Name;

    /** 照片3名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "photo3Id", desc = "")
    @ApiModelProperty(value = "照片3名称")
    private String photo3Name;

    /** 照片4名称**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "photo4Id", desc = "")
    @ApiModelProperty(value = "照片4名称")
    private String photo4Name;

    @ApiModelProperty(value = "用于批量更新")
    private List<String> listId;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SafetyAccidentsModel fromEntry(SafetyAccidents entry){

        SafetyAccidentsModel m = new SafetyAccidentsModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }


    public boolean isReportSuccess(){
        if (null == reportState){
            return false;
        }
        return reportState == 4;
    }
}
