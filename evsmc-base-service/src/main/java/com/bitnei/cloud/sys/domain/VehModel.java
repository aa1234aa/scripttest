package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 车辆型号实体<br>
* 描述： 车辆型号实体<br>
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
* <td>2018-11-12 14:54:43</td>
* <td>renshuo</td>
* <td>创建</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2019-02-20 13:53:08</td>
* <td>zxz</td>
* <td>添加报警信息</td>
 * </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author renshuo
* @since JDK1.8
*/
@Setter
@Getter
public class VehModel extends TailBean {

    /** 主键 **/
    private String id;
    /** 车辆型号 **/
    private String vehModelName;
    /** 公告号id **/
    private String noticeId;
    /** 配置名称 **/
    private String configName;
    /** 检测机构名称 **/
    private String detectionMechanismName;
    /** 规约id **/
    private String ruleId;
    /** 车辆厂商id **/
    private String vehUnitId;
    /** 车型符合性报告编号 **/
    private String vehReportNumber;
    /** 整车控制器型号 **/
    private String controllerModel;
    /** 整车控制器厂商 **/
    private String controllerUnitId;
    /** 底盘型号 **/
    private String chassisModel;
    /** 底盘照片 **/
    private String chassisImgId;
    /** 底盘生产企业 **/
    private String chassisUnitId;
    /** 车重(kg) **/
    private Double vehWeight;
    /** 整备质量 **/
    private Double curbWeight;
    /** 车长(mm) **/
    private Double vehLang;
    /** 车长(mm) **/
    private Double vehWide;
    /** 车高(mm) **/
    private Double vehHigh;
    /** 额定载客(人) **/
    private Integer ratedCapacity;
    /** 节油率水平 **/
    private Integer saveFuelStandard;
    /** 电池种类 **/
    private Integer battType;
    /** 电池包数量 **/
    private Integer batteryPackageCount;
    /** 电池包串并联方式 **/
    private Integer batteryLinkMode;
    /** 驱动电机数量 **/
    private Integer driveDeviceCount;
    /** 驱动电机种类 **/
    private Integer driveType;
    /** 传动系统结构 **/
    private Integer driveStructType;
    /** 驱动方式 **/
    private Integer driveMode;
    /** 动力方式 **/
    private Integer powerMode;
    /** 整体性能信息(Json) **/
    private String efficiencyJson;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新人 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 百公里耗油 **/
    private Double l100km;
    /** 百公里耗电 **/
    private Double kwh100h;
    /** 车辆种类id **/
    private String vehTypeId;

    /** 发动机型号 **/
    private String engineModel;
    /** 排放标准 **/
    private String emissionLevel;
    /** 尿素箱容积(L) **/
    private String ureaTankCapacity;
    /** 整车生产企业 **/
    private String vehicleFirm;
    /** 驱动桥主减速比 **/
    private String reductionRatio;
    /** 前进档个数 **/
    private String transmissionForwardNum;
    /** 各档速比 **/
    private String transmissionGearRatio;
    /** 用途类型 **/
    private String useType;
    /** 产地 **/
    private String productArea;
    /** 最大允许装载质量 **/
    private String maxAllowMass;
    /** 半挂牵引车鞍座最大允许承载质量 **/
    private String bearingMaxAllowMass;
    /** 最小离地间隙 **/
    private String minGroundClearance;
    /** 车辆滑行系数(A、B、C) **/
    private String vehSlidingCoefficient;
    /** 轴数/列车轴数 **/
    private String axlesNumber;
    /** 轴距(mm) **/
    private String wheelBase;
    /** 轮距(前/后)(mm) **/
    private String wheelbaseDetail;
    /** 车身（或驾驶室）型式 **/
    private String bodyModel;
    /** 轮胎数 **/
    private String tireNumber;
    /** 轮胎生产企业 **/
    private String tireManufacturer;
    /** 轮胎规格 **/
    private String tireSpec;
    /** 轮胎气压(kPa) **/
    private String tirePressure;
    /** 接近角/离去角(°) **/
    private String approachDepartureAngle;
    /** 变速箱 **/
    private String transmissionName;
    /** 变速器型号 **/
    private String transmissionModel;
    /** 变速器生产企业 **/
    private String transmissionFirm;
    /** 型式/操纵方式 **/
    private String transmissionType;
    /** 油箱容积 **/
    private String fuelTankCapacity;
    /** 由发动机驱动的附件允许吸收的最大功率(kW) **/
    private String allowedMaxPower;
    /** ESC试验ABC转速(r/min) **/
    private String abcSpeed;
    /** 设备监控版本号 **/
    private String tmsMonitorVersion;
    /** 驱动型式及位置 **/
    private String driveTypeLocation;

    /** 车体结构 **/
    private Integer carBodyStructure;
    /** 核定载重(t) **/
    private Double approvedLoad;
    /** 车辆最大总质量(t) **/
    private Double maxTotalMass;
    /** 环保局车辆类型 **/
    private String epaVehType;
    /** 运输局车辆类型 **/
    private String transportBureauVehType;
    /** 车辆技术等级 **/
    private Integer vehTechLevel;
    /** 等级评定日期 **/
    private String ratingDate;
    /** 环保信息公开编号 **/
    private String environmentalInfoNo;
    /** 车型详情 **/
    private String modelDetails;
    /** 车辆类型 **/
    private String vehicleType;
    /** 车辆检测报告文件 **/
    private String vehInspectionReport;
    /** 总客数 **/
    private String totalGuestsNum;
    /** 备案激活模式 **/
    private Integer recordActivationMode;


}
