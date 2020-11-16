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

import com.bitnei.cloud.veh.domain.DayreportSummary;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportSummary新增模型<br>
* 描述： DayreportSummary新增模型<br>
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
* <td>2019-03-11 09:40:45</td>
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
@ApiModel(value = "DayreportSummaryModel", description = "日汇总报表Model")
public class DayreportSummaryModel extends BaseModel {

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

    @ColumnHeader(title = "是否是新增的车辆")
    @NotNull(message = "是否是新增的车辆不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否是新增的车辆")
    private Integer append;

    @ColumnHeader(title = "最后通讯时间")
    @NotEmpty(message = "最后通讯时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ColumnHeader(title = "最后有效里程")
    @NotNull(message = "最后有效里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "最后有效里程")
    private Double lastEndMileage;

    @ColumnHeader(title = "是否可监控")
    @NotNull(message = "是否可监控不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否可监控")
    private Integer monitor;

    @ColumnHeader(title = "总上线里程")
    @NotNull(message = "总上线里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总上线里程")
    private Double totalOnlineMileage;

    @ColumnHeader(title = "	总轨迹里程")
    @NotNull(message = "	总轨迹里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "	总轨迹里程")
    private Double totalGpsMileage;

    @ColumnHeader(title = "总有效里程")
    @NotNull(message = "总有效里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总有效里程")
    private Double totalValidMileage;

    @ColumnHeader(title = "总核算里程")
    @NotNull(message = "总核算里程不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "总核算里程")
    private Double totalCheckMileage;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "用车单位id")
    private String operUnitId;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @ApiModelProperty(value = "首次上线时间")
    private String firstRegTime;

    @ApiModelProperty(value = "车辆录入时间")
    private String vehicleCreateTime;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 车辆阶段**/
    @DictName(code = "VEHICLE_STAGE_TYPE",joinField = "stage")
    private String stageName;

    /** 用于导出 **/
    @ApiModelProperty(value = "总里程(仪表)km")
    private String lastEndMileageExport;
    @ApiModelProperty(value = "总在线里程(km)")
    private String totalOnlineMileageExport;
    @ApiModelProperty(value = "	总轨迹里程(km)")
    private String totalGpsMileageExport;
    @ApiModelProperty(value = "总有效里程(km)")
    private String totalValidMileageExport;
    @ApiModelProperty(value = "最终核算里程(km)")
    private String totalCheckMileageExport;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportSummaryModel fromEntry(DayreportSummary entry){
        DayreportSummaryModel m = new DayreportSummaryModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setStage(entry.get("stage") == null ? null : entry.get("stage").toString());
        m.setFirstRegTime(entry.get("firstRegTime") == null ? null : entry.get("firstRegTime").toString());
        m.setVehicleCreateTime(entry.get("vehicleCreateTime") == null ? null : entry.get("vehicleCreateTime").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin()+"(已删除)" : m.getVin());
        m.setLastCommitTime(m.getLastCommitTime() == null || "".equals(m.getLastCommitTime()) ? null : m.getLastCommitTime().replaceAll("\\.0",""));
        return m;
    }

}
