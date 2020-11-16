package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.cloud.sys.model.VehicleEngeryDeviceLkModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SafetyAccidents实体<br>
* 描述： SafetyAccidents实体<br>
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
public class SafetyAccidents extends TailBean {

    /** id **/
    private String id;
    /** 车辆id **/
    private String vehicleId;
    /** 车辆型号id **/
    private String vehModelId;
    private String interNo;
    private String licensePlate;
    private String operUnitId;
    private String operAreaId;
    private String produceDate;
    private String sellDate;
    /** vin **/
    private String vin;
    /** vid **/
    private String vid;
    /** 事故发生时车辆总里程 **/
    private String vehicleTotalMileage;
    /** 事故发生时车辆状态 **/
    private Integer vehicleState;
    /** 故障情况 **/
    private String faultSituation;
    /** 事故表征 **/
    private String representation;
    /** 事故影响 **/
    private String accident;
    /** 事故原因初判 **/
    private String reason;
    /** 事故发生时间 **/
    private String time;
    /** 是否关联上报车型处置预案 **/
    private Integer relation;
    /** 事故上报状态 **/
    private Integer reportState;
    /** 上报失败原因 **/
    private String reasonsForFailure;
    /** 上报平台 **/
    private Integer platform;
    /** 最后操作人 **/
    private String finalOperator;
    /** 操作时间 **/
    private String operatingTime;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 可充电储能装置 **/
//    private List<VehicleEngeryDeviceLkModel> engeryDevices;
    /** 照片1 **/
    private String photo1Id;
    /** 照片2 **/
    private String photo2Id;
    /** 照片3 **/
    private String photo3Id;
    /** 照片4 **/
    private String photo4Id;
    /** 报告号 **/
    private String reportNumber;
    /** 监管数据文件id **/
    private String superviseDataFileId;
    /** 分析报告文件id **/
    private String analysisReportFileId;
    /** 监管数据上报状态 **/
    private Integer dataReportState;
    /** 分析报告上传状态 **/
    private Integer reportUploadState;
    /** 可充电储能装置id **/
    private String engeryDeviceIds;

    /** 可充电储能装置编码 **/
    private String engeryDeviceNames;
    /** 单体生产企业 **/
    private String batteryCellUnit;
    /** 可充电储能装置型号 **/
    private String batteryModel;
    /** 储能装置生产企业 **/
    private String engeryDeviceUnit;

    private String batteryCellModels;
}
