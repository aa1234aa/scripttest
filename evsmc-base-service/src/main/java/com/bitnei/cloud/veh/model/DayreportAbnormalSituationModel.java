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

import com.bitnei.cloud.veh.domain.DayreportAbnormalSituation;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportAbnormalSituation新增模型<br>
* 描述： DayreportAbnormalSituation新增模型<br>
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
* <td>2019-03-22 11:01:58</td>
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
@ApiModel(value = "DayreportAbnormalSituationModel", description = "异常车辆日统计Model")
public class DayreportAbnormalSituationModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ColumnHeader(title = "uuid")
    @NotEmpty(message = "uuid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "uuid")
    private String vid;

    @ColumnHeader(title = "vin")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "报表日期")
    @NotEmpty(message = "报表日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报表日期")
    private Timestamp reportDate;

    @ColumnHeader(title = "异常类型(1：速度，2：里程，3：经纬度，4：时间，5：电压，6：电流，7：soc)")
    @NotNull(message = "异常类型(1：速度，2：里程，3：经纬度，4：时间，5：电压，6：电流，7：soc)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常类型(1：速度，2：里程，3：经纬度，4：时间，5：电压，6：电流，7：soc)")
    private Integer type;

    @ColumnHeader(title = "异常类别(1：有效性，2：数值)")
    @NotNull(message = "异常类别(1：有效性，2：数值)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常类别(1：有效性，2：数值)")
    private Integer category;

    @ColumnHeader(title = "异常数据条数")
    @NotNull(message = "异常数据条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常数据条数")
    private Integer abnormalNum;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;

    @ApiModelProperty(value = "终端零件号")
    private String termPartFirmwareNumber;

    @ApiModelProperty(value = "终端厂商自定义编号")
    private String serialNumber;

    @ApiModelProperty(value = "车辆厂商")
    private String manuUnitId;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;

    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityId;

    @ApiModelProperty(value = "激活时间")
    private String firstRegTime;

    @ApiModelProperty(value = "销售日期")
    private String sellDate;

    @ApiModelProperty(value = "ICCID")
    private String iccid;

    @ApiModelProperty(value = "最近异常时间")
    private String lastDate;

    @ApiModelProperty(value = "车速异常（条）")
    private String speedNumber;

    @ApiModelProperty(value = "里程异常（条）")
    private String mileageNumber;

    @ApiModelProperty(value = "经纬度异常（条）")
    private String lngLatNumber;

    @ApiModelProperty(value = "时间异常（条）")
    private String timeNumber;

    @ApiModelProperty(value = "总电压异常（条）")
    private String voltageNumber;

    @ApiModelProperty(value = "总电流异常（条）")
    private String electricNumber;

    @ApiModelProperty(value = "SOC异常（条）")
    private String socNumber;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    @ApiModelProperty(value = "车辆公告型号id")
    private String noticeId;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;
    /** 运营单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;
    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;
    /** 车辆厂商名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "manuUnitId", desc = "")
    private String manuUnitName;
    /** 车辆公告型号名称显示**/
    @LinkName(table = "sys_veh_notice", column = "name", joinField = "noticeId",desc = "")
    @ApiModelProperty(value = "车辆公告型号名称")
    private String noticeName;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportAbnormalSituationModel fromEntry(DayreportAbnormalSituation entry){
        DayreportAbnormalSituationModel m = new DayreportAbnormalSituationModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setSellDate(entry.get("sellDate") == null ? null : entry.get("sellDate").toString());
        m.setSerialNumber(entry.get("serialNumber") == null ? null : entry.get("serialNumber").toString());
        m.setIccid(entry.get("iccid") == null ? null : entry.get("iccid").toString());
        m.setManuUnitId(entry.get("manuUnitId") == null ? null : entry.get("manuUnitId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setTermPartFirmwareNumber(entry.get("termPartFirmwareNumber") == null ? null : entry.get("termPartFirmwareNumber").toString());
        m.setFirstRegTime(entry.get("firstRegTime") == null ? null : entry.get("firstRegTime").toString());
        m.setLastCommitTime(entry.get("lastCommitTime") == null ? null : entry.get("lastCommitTime").toString());
        m.setSpeedNumber(entry.get("speedNumber") == null ? null : entry.get("speedNumber").toString());
        m.setMileageNumber(entry.get("mileageNumber") == null ? null : entry.get("mileageNumber").toString());
        m.setLngLatNumber(entry.get("lngLatNumber") == null ? null : entry.get("lngLatNumber").toString());
        m.setVoltageNumber(entry.get("voltageNumber") == null ? null : entry.get("voltageNumber").toString());
        m.setElectricNumber(entry.get("electricNumber") == null ? null : entry.get("electricNumber").toString());
        m.setSocNumber(entry.get("socNumber") == null ? null : entry.get("socNumber").toString());
        m.setNoticeId(entry.get("noticeId") == null ? null : entry.get("noticeId").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin() == null ? "(已删除)" : m.getVin()+"(已删除)" : m.getVin());
        if (m.getLastCommitTime() != null && !"".equals(m.getLastCommitTime())){
            String times[] = m.getLastCommitTime().split("\\.");
            m.setLastCommitTime(times[0]);
        }
        return m;
    }

}
