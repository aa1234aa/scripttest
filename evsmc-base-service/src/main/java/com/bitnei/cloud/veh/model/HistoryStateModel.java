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

import com.bitnei.cloud.veh.domain.HistoryState;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.lang.Double;
import java.util.Date;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HistoryState新增模型<br>
* 描述： HistoryState新增模型<br>
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
* <td>2019-03-09 11:23:08</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "HistoryStateModel", description = "车辆历史状态报表Model")
public class HistoryStateModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ColumnHeader(title = "车辆表uuid")
    @ApiModelProperty(value = "车辆表uuid")
    private String vid;

    @ColumnHeader(title = "VIN")
    @ApiModelProperty(value = "VIN")
    private String vin;

    @ColumnHeader(title = "报表日期")
    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    @ColumnHeader(title = "仪表里程(km)")
    @ApiModelProperty(value = "仪表里程(km)")
    private Double gaugesMileage;

    @ColumnHeader(title = "车辆状态")
    @ApiModelProperty(value = "车辆状态")
    private Integer state;
    /** 车辆状态名称显示**/
    /*@DictName(code = "VEHICLE_STATUS", joinField = "state")*/
    @ApiModelProperty(value = "车辆状态名称")
    private String stateDisplay;

    @ColumnHeader(title = "总电压")
    @ApiModelProperty(value = "总电压")
    private Double totalVoltage;

    @ColumnHeader(title = "总电流")
    @ApiModelProperty(value = "总电流")
    private Double totalCurrent;

    @ColumnHeader(title = "车速")
    @ApiModelProperty(value = "车速")
    private Double speed;

    @ColumnHeader(title = "SOC")
    @ApiModelProperty(value = "SOC")
    private Integer soc;

    @ColumnHeader(title = "经度")
    @ApiModelProperty(value = "经度")
    private Double positionLon;

    @ColumnHeader(title = "纬度")
    @ApiModelProperty(value = "纬度")
    private Double positionLat;

    @ColumnHeader(title = "最后通讯时间")
    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommunicationTime;

    @ColumnHeader(title = "有效CAN数据最后上传时间")
    @ApiModelProperty(value = "有效CAN数据最后上传时间")
    private String lastCanValidTime;

    @ColumnHeader(title = "最后一次充电时间")
    @ApiModelProperty(value = "最后一次充电时间")
    private String lastChargeTime;

    @ColumnHeader(title = "充放电状态")
    @ApiModelProperty(value = "充放电状态")
    private Integer chargeDischargeState;
    /** 充放电状态名称显示**/
    /*@DictName(code = "CHARGE_DISCHARGE_STATE", joinField = "chargeDischargeState")*/
    @ApiModelProperty(value = "充放电状态名称")
    private String chargeDischargeStateDisplay;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    @ApiModelProperty(value = "车辆阶段")
    private String stage;
    /** 车辆阶段名称显示**/
    /*@DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")*/
    @ApiModelProperty(value = "车辆阶段名称")
    private String stageDisplay;
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆厂商")
    private String manuUnitId;
    /** 车辆厂商名称显示**/
    /*@LinkName(table = "sys_unit", column = "name", joinField = "manuUnitId",desc = "")*/
    @ApiModelProperty(value = "车辆厂商名称")
    private String manuUnitDisplay;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;
    /** 车运营单位名称显示**/
    /*@LinkName(table = "sys_unit", column = "name", joinField = "operUnitId",desc = "")*/
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitDisplay;

    @ApiModelProperty(value = "内部编号")
    private String interNo;
    @ApiModelProperty(value = "ICCID")
    private String iccid;

    @ApiModelProperty(value = "车辆公告型号")
    private String noticeId;
    /** 车辆公告型号名称显示**/
    /*@LinkName(table = "sys_veh_notice", column = "name", joinField = "noticeId",desc = "")*/
    @ApiModelProperty(value = "车辆公告型号名称")
    private String noticeDisplay;

    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityId;
    /** 上牌城市名称显示**/
    /*@LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId",desc = "")*/
    @ApiModelProperty(value = "上牌城市名称")
    private String operLicenseCityDisplay;

    @ApiModelProperty(value = "激活时间")
    private String firstRegTime;
    @ApiModelProperty(value = "销售日期")
    private String sellDate;
    @ApiModelProperty(value = "终端零件号")
    private String termPartFirmwareNumbers;
    @ApiModelProperty(value = "终端厂商自定义编号")
    private String serialNumber;
    @ApiModelProperty(value = "地理位置")
    private String vehPosition;

    @ApiModelProperty(value = "录入时间")
    private String createTime;

    @ApiModelProperty(value = "分表名称")
    private String tableName;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static HistoryStateModel fromEntry(HistoryState entry){
        HistoryStateModel m = new HistoryStateModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
