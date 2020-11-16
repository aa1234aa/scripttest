package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehModel;
import com.bitnei.cloud.sys.util.RegexUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 车辆型号model<br>
 * 描述： 车辆型号model<br>
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
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author renshuo
 * @version 1.0
 * @since JDK1.8
 */
@Setter
@Getter
@ApiModel(value = "VehModelModel", description = "车型管理Model")
@Slf4j
public class VehModelModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "车辆型号", example = "HRW0000000000E58", desc = "车辆型号为2-32位中英文数字及特殊字符")
    @NotEmpty(message = "车辆型号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = RegexUtil.C_2_32, message = "车辆型号为2-32位中英文数字及特殊字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @NotEmpty(message = "车辆公告型号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆公告型号")
    private String noticeId;

    @NotBlank(message = "车辆公告型号不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车辆公告型号", example = "CGXHRW0000000000E58", desc = "车辆公告型号名称")
    @LinkName(table = "sys_veh_notice", column = "name", joinField = "noticeId", desc = "车辆公告型号名称")
    @ApiModelProperty(value = "车辆公告型号名称")
    private String noticeName;

    @ApiModelProperty(value = "车辆种类id")
    private String vehTypeId;

    @LinkName(table = "sys_veh_type", column = "name", joinField = "vehTypeId", desc = "车辆种类名称")
    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

    @ColumnHeader(title = "车辆配置名称", example = "荣威2018标准款E58", desc = "车辆配置名称为2-32位中英文及数字")
    @NotEmpty(message = "车辆配置名称不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = RegexUtil.A_H_2_32, message = "车辆配置名称为2-32位中英文及数字-", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "配置名称")
    private String configName;

    @NotEmpty(message = "协议类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "协议类型")
    private String ruleId;

    @NotBlank(message = "协议类型不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "协议类型", example = "GB_T32960", desc = "协议类型名称")
    @ApiModelProperty(value = "协议类型名称")
    @LinkName(table = "dc_rule_type", joinField = "ruleId", desc = "协议类型名称")
    private String ruleIdName;

    @NotEmpty(message = "车辆厂商不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆厂商id")
    private String vehUnitId;

    @NotBlank(message = "车辆厂商不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车辆厂商", example = "海马汽车零配件有限公司", desc = "车辆厂商名称")
    @LinkName(table = "sys_unit", column = "name", joinField = "vehUnitId", desc = "车辆厂商名称")
    @ApiModelProperty(value = "车辆厂商名称")
    private String vehUnitName;

    @ColumnHeader(title = "检测机构名称", notNull = false, example = "北京检测机构", desc = "检测机构名称为2-32位中英文及数字")
    @ApiModelProperty("检测机构名称")
    @Pattern(regexp = RegexUtil.A_M_2_32, message = "检测机构名称为2-32位中英文及数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String detectionMechanismName;

    @ColumnHeader(title = "车辆符合性报告编号", notNull = false, example = "NO00000170", desc = "车型符合性报告编号为2-32位中英文及数字")
    @Pattern(regexp = RegexUtil.A_M_2_32, message = "车型符合性报告编号为2-32位中英文及数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车辆符合性报告编号")
    private String vehReportNumber;

    @Pattern(regexp = RegexUtil.A_M_2_32, message = "底盘型号为2-32位中英文及数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "底盘型号")
    private String chassisModel;

    @ApiModelProperty(value = "底盘照片")
    private String chassisImgId;

    /** 整车控制器型号 **/
    @Pattern(regexp = RegexUtil.C_M_0_100, message = "整车控制器型号最大长度为100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "整车控制器型号")
    private String controllerModel;

    /** 整车控制器厂商id **/
    @ApiModelProperty(value = "整车控制器厂商id")
    private String controllerUnitId;

    @LinkName(table = "sys_unit", column = "name", joinField = "controllerUnitId", desc = "整车控制器厂商名称")
    @ApiModelProperty(value = "整车控制器厂商名称")
    private String controllerUnitName;

    @ApiModelProperty(value = "底盘生产企业")
    private String chassisUnitId;

    /**
     * 关联表名称显示*
     */
    @LinkName(table = "sys_unit", column = "name", joinField = "chassisUnitId", desc = "底盘生产企业名称")
    @ApiModelProperty(value = "底盘生产企业名称")
    private String chassisUnitName;

    @Range(min = 100,max = 999999, message = "总质量(kg)长度为3~6位正整数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "总质量(kg)")
    private Double vehWeight;

    @Range(min = 100,max = 999999, message = "整备质量(kg)长度为3~6位正整数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "整备质量(Kg)")
    private Double curbWeight;

    @Range(min = 0L, max = 99999L, message = "车长(mm)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车长(mm)")
    private Double vehLang;

    @Range(min = 0L, max = 99999L, message = "车宽(mm)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车宽(mm)")
    private Double vehWide;

    @Range(min = 0L, max = 99999L, message = "车高(mm)整数范围0~99999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车高(mm)")
    private Double vehHigh;

    @Range(max = 999, message = "额定载客(人)整数范围0~999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "额定载客(人)")
    private Integer ratedCapacity;

    @ApiModelProperty(value = "节油率水平")
    private Integer saveFuelStandard;

    @ApiModelProperty(value = "电池种类")
    private Integer battType;

    /** 电池种类名称 */
    @DictName(code = "BATTERY_TYPE", joinField = "battType")
    private String battTypeDisplay;

    @Range(max = 999, message = "电池包数量整数范围0~999", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "电池包数量")
    private Integer batteryPackageCount;

    @ApiModelProperty(value = "电池包串并联方式")
    private Integer batteryLinkMode;

    @ApiModelProperty(value = "电池包串并联方式名称")
    @DictName(code = "BATTERY_LINK_MODE", joinField = "batteryLinkMode")
    private String batteryLinkModeName;

    @Range(max = 999, message = "驱动电机数量为1-3位正整数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "驱动电机数量")
    private Integer driveDeviceCount;

    @ApiModelProperty(value = "驱动电机种类")
    private Integer driveType;

    @DictName(code = "DRIVE_DEVICE_TYPE", joinField = "driveType")
    @ApiModelProperty(value = "驱动电机种类描述")
    private String driveTypeDisplay;

//    @NotNull(message = "传动系统结构不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "传动系统结构")
    private Integer driveStructType;

    @DictName(code = "DRIVE_STRUCT_TYPE", joinField = "driveStructType")
    @ApiModelProperty(value = "传动系统结构")
    private String driveStructTypeDisplay;

    @ApiModelProperty(value = "驱动方式")
    private Integer driveMode;

    @DictName(code = "DRIVE_MODE", joinField = "driveMode")
    @ApiModelProperty(value = "驱动方式")
    private String driveModeDisplay;

    @NotNull(message = "动力方式不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "动力方式")
    private Integer powerMode;


    @DictName(code = "POWER_MODE", joinField = "powerMode")
    @ApiModelProperty(value = "动力方式名称")
    private String powerModeDisplay;


//    @NotEmpty(message = "整体性能信息(Json)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "整体性能信息(Json)")
    private String efficiencyJson;

    @Valid
    @ApiModelProperty(value = "整体性能信息")
    private VehModelEfficiencyModel efficiency;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


    @ApiModelProperty(value = "节能-百公里耗油量")
    @Digits(integer = 6, fraction = 2, message = "节能-百公里耗油量(L)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    private Double l100km;

    @Digits(integer = 6, fraction = 2, message = "节能-百公里耗电量(kWh)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "节能-百公里耗电量")
    private Double kwh100h;

    @Valid
    @ApiModelProperty("通用报警阈值信息")
    private VehModelAlarmModel alarmModel;


    @Pattern(regexp = "^.{0,128}|$", message = "发动机型号长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "发动机型号")
    private String engineModel;
    @Pattern(regexp = "^.{0,128}|$", message = "排放标准长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "排放标准")
    private String emissionLevel;
    @Pattern(regexp = "^.{0,128}|$", message = "尿素箱容积(L)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "尿素箱容积(L)")
    private String ureaTankCapacity;
    @Pattern(regexp = "^.{0,128}|$", message = "整车生产企业长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "整车生产企业")
    private String vehicleFirm;
    @Pattern(regexp = "^.{0,128}|$", message = "驱动桥主减速比长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "驱动桥主减速比")
    private String reductionRatio;
    @Pattern(regexp = "^.{0,128}|$", message = "前进档个数长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "前进档个数")
    private String transmissionForwardNum;
    @Pattern(regexp = "^.{0,128}|$", message = "各档速比长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "各档速比")
    private String transmissionGearRatio;
    @Pattern(regexp = "^.{0,128}|$", message = "用途类型长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "用途类型")
    private String useType;
    @Pattern(regexp = "^.{0,128}|$", message = "产地长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "产地")
    private String productArea;
    @Pattern(regexp = "^.{0,128}|$", message = "最大允许装载质量长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "最大允许装载质量")
    private String maxAllowMass;
    @Pattern(regexp = "^.{0,128}|$", message = "半挂牵引车鞍座最大允许承载质量长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "半挂牵引车鞍座最大允许承载质量")
    private String bearingMaxAllowMass;
    @Pattern(regexp = "^.{0,128}|$", message = "最小离地间隙长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "最小离地间隙")
    private String minGroundClearance;
    @Pattern(regexp = "^.{0,128}|$", message = "车辆滑行系数(A、B、C)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆滑行系数(A、B、C)")
    private String vehSlidingCoefficient;
    @Pattern(regexp = "^.{0,128}|$", message = "轴数/列车轴数长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轴数/列车轴数")
    private String axlesNumber;
    @Pattern(regexp = "^.{0,128}|$", message = "轴距(mm)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轴距(mm)")
    private String wheelBase;
    @Pattern(regexp = "^.{0,128}|$", message = "轮距(前/后)(mm)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轮距(前/后)(mm)")
    private String wheelbaseDetail;
    @Pattern(regexp = "^.{0,128}|$", message = "车身（或驾驶室）型式长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车身（或驾驶室）型式")
    private String bodyModel;
    @Pattern(regexp = "^.{0,128}|$", message = "轮胎数长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轮胎数")
    private String tireNumber;
    @Pattern(regexp = "^.{0,128}|$", message = "轮胎生产企业长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轮胎生产企业")
    private String tireManufacturer;
    @Pattern(regexp = "^.{0,128}|$", message = "轮胎规格长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轮胎规格")
    private String tireSpec;
    @Pattern(regexp = "^.{0,128}|$", message = "轮胎气压(kPa)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "轮胎气压(kPa)")
    private String tirePressure;
    @Pattern(regexp = "^.{0,128}|$", message = "接近角/离去角(°)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "接近角/离去角(°)")
    private String approachDepartureAngle;
    @Pattern(regexp = "^.{0,128}|$", message = "变速箱长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "变速箱")
    private String transmissionName;
    @Pattern(regexp = "^.{0,128}|$", message = "变速器型号长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "变速器型号")
    private String transmissionModel;
    @Pattern(regexp = "^.{0,128}|$", message = "变速器生产企业长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "变速器生产企业")
    private String transmissionFirm;
    @Pattern(regexp = "^.{0,128}|$", message = "型式/操纵方式长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "型式/操纵方式")
    private String transmissionType;
    @Pattern(regexp = "^.{0,128}|$", message = "油箱容积长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "油箱容积")
    private String fuelTankCapacity;
    @Pattern(regexp = "^.{0,128}|$", message = "由发动机驱动的附件允许吸收的最大功率(kW)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "由发动机驱动的附件允许吸收的最大功率(kW)")
    private String allowedMaxPower;
    @Pattern(regexp = "^.{0,128}|$", message = "ESC试验ABC转速(r/min)长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "ESC试验ABC转速(r/min)")
    private String abcSpeed;
    @Pattern(regexp = "^.{0,128}|$", message = "设备监控版本号长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "设备监控版本号")
    private String tmsMonitorVersion;
    @Pattern(regexp = "^.{0,128}|$", message = "驱动型式及位置长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "驱动型式及位置")
    private String driveTypeLocation;

    /** 车体结构 **/
    @ApiModelProperty(value = "车体结构")
    private Integer carBodyStructure;
    @ApiModelProperty(value = "车体结构名称")
    @DictName(code = "CAR_BODY_STRUCTURE", joinField = "carBodyStructure")
    private String carBodyStructureName;
    /** 核定载重(t) **/
    @ApiModelProperty(value = "核定载重(t)")
    @Digits(integer = 6, fraction = 2, message = "核定载重(t)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    private Double approvedLoad;
    /** 车辆最大总质量(t) **/
    @ApiModelProperty(value = "车辆最大总质量(t)")
    @Digits(integer = 6, fraction = 2, message = "车辆最大总质量(t)长度1~6位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    private Double maxTotalMass;
    /** 环保局车辆类型 **/
    @Pattern(regexp = "^.{2,32}|$", message = "环保局车辆类型长度为2-32个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "环保局车辆类型")
    private String epaVehType;
    /** 运输局车辆类型 **/
    @Pattern(regexp = "^.{2,32}|$", message = "运输局车辆类型长度为2-32个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运输局车辆类型")
    private String transportBureauVehType;
    /** 车辆技术等级 **/
    @ApiModelProperty(value = "车辆技术等级")
    private Integer vehTechLevel;
    @ApiModelProperty(value = "车辆技术等级名称")
    @DictName(code = "VEH_TECH_LEVEL", joinField = "vehTechLevel")
    private String vehTechLevelName;
    /** 等级评定日期 **/
    @Pattern(regexp = "^$|[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "等级评定日期格式不正确(yyyy-MM-dd)", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "等级评定日期")
    private String ratingDate;

    @Pattern(regexp = "^.{0,128}|$", message = "环保信息公开编号长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "环保信息公开编号")
    private String environmentalInfoNo;
    @Pattern(regexp = "^.{0,128}|$", message = "车型详情长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车型详情")
    private String modelDetails;
    @Pattern(regexp = "^.{0,128}|$", message = "车辆类型长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆类型")
    private String vehicleType;
    @ApiModelProperty(value = "车辆检测报告文件")
    private String vehInspectionReport;
    /** 车辆检测报告文件名称显示**/
    @LinkName(table = "sys_upload_file", column = "name", joinField = "vehInspectionReport",desc = "")
    @ApiModelProperty(value = "车辆检测报告文件名称")
    private String vehInspectionReportName;
    @Pattern(regexp = "^.{0,128}|$", message = "总客数长度为0-128个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "总客数")
    private String totalGuestsNum;
    @ApiModelProperty(value = "备案激活模式:1-无需备案激活,2-需备案激活,3-其他")
    private Integer recordActivationMode;
    @ApiModelProperty(value = "备案激活模式名称")
    @DictName(code = "RECORD_ACTIVATION_MODE", joinField = "recordActivationMode")
    private String recordActivationModeName;

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehModelModel fromEntry(VehModel entry) {
        VehModelModel m = new VehModelModel();
        BeanUtils.copyProperties(entry, m);
        if (StringUtils.isNotBlank(m.getEfficiencyJson())) {
            ObjectMapper om = new ObjectMapper();
            try {
                VehModelEfficiencyModel e = om.readValue(m.getEfficiencyJson(), VehModelEfficiencyModel.class);
                e.setEnvironmentalProtectionTypeDisplay(null);
                e.setRevFuelTypeDisplay(null);
                e.setHevFuelTypeDisplay(null);
                e.setPhevFuelTypeDisplay(null);
                e.setRevFuelFormDisplay(null);
                e.setFcvFuelFormDisplay(null);
                e.setHevFuelFormDisplay(null);
                e.setPhevFuelFormDisplay(null);
                DataLoader.loadNames(e);
                m.setEfficiency(e);
            } catch (Exception e) {
               log.error("error", e);
            }
        }
        return m;
    }

}