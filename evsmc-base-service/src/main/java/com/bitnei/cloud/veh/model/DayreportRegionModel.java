package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.veh.domain.DayreportRegion;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportRegion新增模型<br>
* 描述： DayreportRegion新增模型<br>
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
* <td>2019-03-11 15:18:50</td>
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
@ApiModel(value = "DayreportRegionModel", description = "区域里程统计Model")
public class DayreportRegionModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ColumnHeader(title = "vid")
    @NotEmpty(message = "vid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vid")
    private String vid;

    @ColumnHeader(title = "vin")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "报表日期")
    @NotEmpty(message = "报表日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报表日期")
    private Timestamp reportDate;

    @ColumnHeader(title = "省份")
    @NotEmpty(message = "省份不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "省份")
    private String province;

    @ColumnHeader(title = "城市")
    @NotEmpty(message = "城市不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "城市")
    private String city;

    @ColumnHeader(title = "该区域首次上线时间")
    @NotEmpty(message = "该区域首次上线时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域首次上线时间")
    private String firstOnlineTime;

    @ColumnHeader(title = "该区域开始里程")
    @NotNull(message = "该区域开始里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域开始里程")
    private Double firstStartMileage;

    @ColumnHeader(title = "该区域最后通讯时间")
    @NotEmpty(message = "该区域最后通讯时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域最后通讯时间")
    private String lastCommitTime;

    @ColumnHeader(title = "该区域结束里程")
    @NotNull(message = "该区域结束里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域结束里程")
    private Double lastEndMileage;

    @ColumnHeader(title = "该区域核查数据总条数")
    @NotNull(message = "该区域核查数据总条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域核查数据总条数")
    private Integer checkDataTotalNum;

    @ColumnHeader(title = "该区域无效条数")
    @NotNull(message = "该区域无效条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域无效条数")
    private Integer invalidNum;

    @ColumnHeader(title = "该区域异常条数")
    @NotNull(message = "该区域异常条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域异常条数")
    private Integer abnormalNum;

    @ColumnHeader(title = "该区域内的一天的仪表的总里程")
    @NotNull(message = "该区域内的一天的仪表的总里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域内的一天的仪表的总里程")
    private Double meterTotalMileage;

    @ColumnHeader(title = "里程跳变扣除里程")
    @NotNull(message = "里程跳变扣除里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "里程跳变扣除里程")
    private Double deductJumpMileage;

    @ColumnHeader(title = "总连续电流扣除里程")
    @NotNull(message = "总连续电流扣除里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总连续电流扣除里程")
    private Double deductCurrentMileage;

    @ColumnHeader(title = "该区域内的有效里程")
    @NotNull(message = "该区域内的有效里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域内的有效里程")
    private Double validTotalMileage;

    @ColumnHeader(title = "该区域内一天行驶的gps的总里程")
    @NotNull(message = "该区域内一天行驶的gps的总里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域内一天行驶的gps的总里程")
    private Double gpsTotalMileage;

    @ColumnHeader(title = "有效里程和轨迹里程相对误差")
    @NotNull(message = "有效里程和轨迹里程相对误差不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "有效里程和轨迹里程相对误差")
    private Double validGpsDeviation;

    @ColumnHeader(title = "上线里程和有效里程相对误差")
    @NotNull(message = "上线里程和有效里程相对误差不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "上线里程和有效里程相对误差")
    private Double onlineValidDeviation;

    @ColumnHeader(title = "该区域内的核查里程")
    @NotNull(message = "该区域内的核查里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域内的核查里程")
    private Double checkTotalMileage;

    @ColumnHeader(title = "该区域行驶时长（分钟）")
    @NotNull(message = "该区域行驶时长（分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域行驶时长（分钟）")
    private Integer runTimeSum;

    @ColumnHeader(title = "该区域充电时长 （分钟）")
    @NotNull(message = "该区域充电时长 （分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域充电时长 （分钟）")
    private Integer chargeTimeSum;

    @ColumnHeader(title = "区域内日充电状态最小SOC")
    @NotNull(message = "区域内日充电状态最小SOC不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "区域内日充电状态最小SOC")
    private Integer chargeSocMin;

    @ColumnHeader(title = "该区域的充电量")
    @NotNull(message = "该区域的充电量不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "该区域的充电量")
    private Double power;

    @ColumnHeader(title = "区域内日充电状态最大SOC")
    @NotNull(message = "区域内日充电状态最大SOC不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "区域内日充电状态最大SOC")
    private Integer chargeSocMax;

    @ColumnHeader(title = "区域在线总时长（分钟）")
    @NotNull(message = "区域在线总时长（分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "区域在线总时长（分钟）")
    private Integer onlineTimeSum;

    @ColumnHeader(title = "运行状态下最大速度")
    @NotNull(message = "运行状态下最大速度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运行状态下最大速度")
    private Double speedMax;

    @ColumnHeader(title = "运行平均速度")
    @NotNull(message = "运行平均速度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运行平均速度")
    private Double speedAvg;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "用车单位id")
    private String operUnitId;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "行驶区域总里程（km）")
    private Double allAreaMileage;

    @ApiModelProperty(value = "行驶区域GPS总里程（km）")
    private Double allGpsMileage;

    @ApiModelProperty(value = "仪表总里程（km）")
    private Double allMileage;

    @ApiModelProperty(value = "行驶区域里程占比（%）")
    private Double ratio;

    @ApiModelProperty(value = "截止日期")
    private String endTime;

    @ApiModelProperty(value = "城市名")
    @LinkName(table = "view_area_code", column = "name", joinField = "city", desc = "")
    private String cityName;

    @ApiModelProperty(value = "行驶区域总里程（km）")
    private String allAreaMileageExport;

    @ApiModelProperty(value = "行驶区域GPS总里程（km）")
    private String allGpsMileageExport;

    @ApiModelProperty(value = "仪表总里程（km）")
    private String allMileageExport;

    @ApiModelProperty(value = "行驶区域里程占比（%）")
    private String ratioExport;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 车辆阶段**/
    @DictName(code = "VEHICLE_STAGE_TYPE",joinField = "stage")
    private String stageName;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportRegionModel fromEntry(DayreportRegion entry){
        DayreportRegionModel m = new DayreportRegionModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setStage(entry.get("stage") == null ? null : entry.get("stage").toString());
        m.setEndTime(entry.get("endTime") == null ? null : entry.get("endTime").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin()+"(已删除)" : m.getVin());
        return m;
    }

}
