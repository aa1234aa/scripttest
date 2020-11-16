package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DateUtil;
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

import com.bitnei.cloud.veh.domain.DayreportMileageCheck;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Double;
import java.text.SimpleDateFormat;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportMileageCheck新增模型<br>
* 描述： DayreportMileageCheck新增模型<br>
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
* <td>2019-03-14 16:46:26</td>
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
@ApiModel(value = "DayreportMileageCheckModel", description = "里程核查日报Model")
public class DayreportMileageCheckModel extends BaseModel {

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

    @ColumnHeader(title = "统计日期")
    @NotEmpty(message = "统计日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "统计日期")
    private Timestamp reportDate;

    @ColumnHeader(title = "当日首次上线时间")
    @NotEmpty(message = "当日首次上线时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日首次上线时间")
    private String firstOnlineTime;

    @ColumnHeader(title = "当日开始里程")
    @NotNull(message = "当日开始里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日开始里程")
    private Double firstStartMileage;

    @ColumnHeader(title = "当日最后通讯时间")
    @NotEmpty(message = "当日最后通讯时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日最后通讯时间")
    private String lastCommitTime;

    @ColumnHeader(title = "当日结束里程")
    @NotNull(message = "当日结束里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日结束里程")
    private Double lastEndMileage;

    @ColumnHeader(title = "核查数据总条数")
    @NotNull(message = "核查数据总条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "核查数据总条数")
    private Integer checkDataTotalNum;

    @ColumnHeader(title = "无效数据条数")
    @NotNull(message = "无效数据条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "无效数据条数")
    private Integer invalidNum;

    @ColumnHeader(title = "异常数据条数")
    @NotNull(message = "异常数据条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常数据条数")
    private Integer abnormalNum;

    @ColumnHeader(title = "当日上线里程")
    @NotNull(message = "当日上线里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日上线里程")
    private Double dayOnlineMileage;

    @ColumnHeader(title = "总跳变扣除里程")
    @NotNull(message = "总跳变扣除里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总跳变扣除里程")
    private Double deductJumpMileage;

    @ColumnHeader(title = "总连续电流扣除里程")
    @NotNull(message = "总连续电流扣除里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总连续电流扣除里程")
    private Double deductCurrentMileage;

    @ColumnHeader(title = "当日有效里程")
    @NotNull(message = "当日有效里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日有效里程")
    private Double dayValidMileage;

    @ColumnHeader(title = "当日轨迹里程")
    @NotNull(message = "当日轨迹里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当日轨迹里程")
    private Double dayGpsMileage;

    @ColumnHeader(title = "有效里程和轨迹里程相对误差")
    @NotNull(message = "有效里程和轨迹里程相对误差不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "有效里程和轨迹里程相对误差")
    private Double validGpsDeviation;

    @ColumnHeader(title = "上线里程和有效里程相对误差")
    @NotNull(message = "上线里程和有效里程相对误差不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "上线里程和有效里程相对误差")
    private Double onlineValidDeviation;

    @ColumnHeader(title = "单日核算里程")
    @NotNull(message = "单日核算里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "单日核算里程")
    private Double dayCheckMileage;

    @ColumnHeader(title = "运行时长（分钟）")
    @NotNull(message = "运行时长（分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "运行时长（分钟）")
    private Integer runTimeSum;

    @ColumnHeader(title = "日充电时长（分钟）")
    @NotNull(message = "日充电时长（分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日充电时长（分钟）")
    private Integer chargeTimeSum;

    @ColumnHeader(title = "日充电状态最小SOC")
    @NotNull(message = "日充电状态最小SOC不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日充电状态最小SOC")
    private Integer chargeSocMin;

    @ColumnHeader(title = "日充电量（kw.h）")
    @NotNull(message = "日充电量（kw.h）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日充电量（kw.h）")
    private Double power;

    @ColumnHeader(title = "日充电状态最大SOC")
    @NotNull(message = "日充电状态最大SOC不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日充电状态最大SOC")
    private Integer chargeSocMax;

    @ColumnHeader(title = "日在线总时长（分钟）")
    @NotNull(message = "日在线总时长（分钟）不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日在线总时长（分钟）")
    private Integer onlineTimeSum;

    @ColumnHeader(title = "日运行最大速度")
    @NotNull(message = "日运行最大速度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日运行最大速度")
    private Double speedMax;

    @ColumnHeader(title = "日运行平均速度")
    @NotNull(message = "日运行平均速度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日运行平均速度")
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

    @ApiModelProperty(value = "无效异常总条数（条）")
    private String invalidAndAbnormal;

    @ApiModelProperty(value = "有效里程和轨迹里程相对误差（百分比）")
    private String showValidGpsDeviation;

    @ApiModelProperty(value = "上线里程和有效里程相对误差（百分比）")
    private String showOnlineValidDeviation;

    @ApiModelProperty(value = "（前端显示）统计日期")
    private String showReportDate;

    @ApiModelProperty(value = "无效数据占比")
    private String showInvalidNum;

    @ApiModelProperty(value = "异常数据占比")
    private String showAbnormalNum;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    @ApiModelProperty(value = "车辆型号名称")
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    //用于导出文件
    @ApiModelProperty(value = "当日轨迹里程")
    private String dayGpsMileageExport;

    @ApiModelProperty(value = "单日核算里程")
    private String dayCheckMileageExport;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportMileageCheckModel fromEntry(DayreportMileageCheck entry){
        DayreportMileageCheckModel m = new DayreportMileageCheckModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setInvalidAndAbnormal(entry.get("invalidAndAbnormal") == null ? null : entry.get("invalidAndAbnormal").toString());
        m.setShowInvalidNum(entry.get("showInvalidNum") == null ? null : entry.get("showInvalidNum").toString());
        m.setShowAbnormalNum(entry.get("showAbnormalNum") == null ? null : entry.get("showAbnormalNum").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin()+"(已删除)" : m.getVin());
        return m;
    }

}
