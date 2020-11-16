package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import com.bitnei.cloud.sys.domain.VehicleRealStatus;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.String;
import java.lang.Integer;
import java.lang.Double;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleRealStatus新增模型<br>
 * 描述： VehicleRealStatus新增模型<br>
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
 * <td>2019-03-16 14:55:45</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleRealStatusModel", description = "车辆动态信息Model")
public class VehicleRealStatusModel extends BaseModel {


    private String id;

    @ApiModelProperty(value = "车辆Id")
    private String vehicleId;

    @ColumnHeader(title = "车辆vin")
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "上牌城市")
    @ApiModelProperty(value = "上牌城市")
    private String operLicenseCityName;

    @ColumnHeader(title = "行驶区域")
    @ApiModelProperty(value = "行驶区域")
    private String gpsCityName;

    @ColumnHeader(title = "是否上过线")
    @NotNull(message = "是否上过线不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否上过线")
    private Integer onlined;

    @DictName(code = "BOOL_TYPE", joinField = "onlined")
    @ApiModelProperty(value = "是否上过线")
    private String onlinedDisplay;

    @ColumnHeader(title = "不在线时长(小时)")
    @ApiModelProperty(value = "不在线时长")
    private String offlineTime;

    @ColumnHeader(title = "车辆是否行驶")
    @ApiModelProperty(value = "车辆是否行驶")
    private String isRun;
    private Integer isRunValue;
    @ColumnHeader(title = "是否有CAN")
    @ApiModelProperty(value = "是否有CAN")
    private String isCan;

    private Integer isCanValue;
    @ColumnHeader(title = "GPS是否定位")
    @ApiModelProperty(value = "GPS是否定位")
    private String isGps = "9999";
    private Integer isGpsValue;

    @ApiModelProperty(value = "GPS是否定位中文说明")
    @DictName(code = "GPS_IS_LOCATE", joinField = "isGps")
    private String isGpsDisplay;

    @ColumnHeader(title = "车辆状态")
    @ApiModelProperty(value = "车辆状态")
    private String vehicleState;

    @ColumnHeader(title = "车辆当前位置")
    @ApiModelProperty(value = "车辆当前位置")
    private String address;

    @ColumnHeader(title = "车辆首次上线时间")
    @NotEmpty(message = "车辆首次上线时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆首次上线时间")
    private String firstRegTime;

    @ColumnHeader(title = "在线状态")
    @NotNull(message = "在线状态  1：在线 2:离线不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "终端在线状态  1：在线 2:离线 0:从未上过线")
    private Integer onlineStatus;

    @DictName(code = "ONLINE_STATUS", joinField = "onlineStatus")
    private String onlineStatusDisplay;

    @ColumnHeader(title = "仪表盘里程")
    @NotNull(message = "仪表盘里程不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "仪表盘里程")
    private Double mils;


    @ColumnHeader(title = "最后通讯时间")
    @ApiModelProperty(value = "最后通讯时间")
    private String updateTime;

    @ColumnHeader(title = "首次注册时间")
    private String createTime;
//    //@ColumnHeader(title = "锁止功能状态")
//    @NotNull(message = "锁止功能状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
//    @ApiModelProperty(value = "锁止功能状态")
//    private Integer lockStatus;
//
//    //@ColumnHeader(title = "动力锁止状态")
//    @NotNull(message = "动力锁止状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
//    @ApiModelProperty(value = "动力锁止状态")
//    private Integer powerLockStatus;
//
//    //@ColumnHeader(title = "防拆状态")
//    @NotNull(message = "防拆状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
//    @ApiModelProperty(value = "防拆状态")
//    private Integer preventDismantleStatus;
//
//    //@ColumnHeader(title = "防拆功能码")
//    @NotNull(message = "防拆功能码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
//    @ApiModelProperty(value = "防拆功能码")
//    private Integer preventDismantleFunc;

    @ApiModelProperty(value = "里程统计")
    private Object dayReport;
    @ApiModelProperty(value = "自定义故障")
    private Object customFaults;

    @ApiModelProperty(value = "车辆的实时数据从此处获取(数据项定义参见内部excel文档)")
    private Map<String, String> dataItems;
    //辅助字段

    @ColumnHeader(title = "是否关注")
    @ApiModelProperty(value = "是否关注")
    private Boolean isAttention = false;

    @ColumnHeader(title = "内部编码")
    @ApiModelProperty(value = "内部编码")
    private String interNo;

    @ColumnHeader(title = "车型名称")
    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ColumnHeader(title = "终端型号")
    @ApiModelProperty(value = "终端型号")
    private String termModelName;

    @ApiModelProperty(value = "终端编号")
    private String termSerialNumber;

    @ColumnHeader(title = "运营单位名称")
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitName;

    @ColumnHeader(title = "协议类型名称")
    @ApiModelProperty(value = "协议类型名称")
    private String ruleTypeName;

    @ColumnHeader(title = "动力方式")
    @NotNull(message = "动力方式不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "动力方式")
    private Integer powerMode;

    @DictName(code = "POWER_MODE", joinField = "powerMode")
    @ApiModelProperty(value = "动力方式名称")
    private String powerModeDisplay;

    @ColumnHeader(title = "车型id")
    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ColumnHeader(title = "iccid")
    @ApiModelProperty(value = "iccid")
    private String iccid;

    @ColumnHeader(title = "终端型号id")
    @ApiModelProperty(value = "终端型号id")
    private String termId;

    @ColumnHeader(title = "运营单位id")
    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

    @ApiModelProperty(value = "销售区域")
    private String sellCityName;

    @ApiModelProperty(value = "终端厂商")
    private String termUnitName;

    @ApiModelProperty(value = "车辆品牌")
    private String vehBrandName;

    @ApiModelProperty(value = "车系")
    private String vehSeriesName;

    @ApiModelProperty(value = "车主")
    private String ownerName;

    @ApiModelProperty(value = "联系人")
    private String operSupportOwnerName;

    private Integer faultStatus;

    private Float soc;
    private String vehUnitName;
    private String operOwnerName;


    /**
     * 辅助字段，用于给前端判断用哪种类型的数据项
     */
    @ApiModelProperty(value = "1、国标， 2、国六 （默认是国标）")
    private Integer dataItemType = 1;

    private String termRuleTypeName;
    private String termRuleTypeCode;
    private String uuid;
    private String identificationId;
    private Integer filingStatus;
    private String termPubKey;
    private String filingTime;
    @ApiModelProperty(value = "整体性能信息(Json)")
    private String efficiencyJson;

    @ApiModelProperty(value = "REV燃料类型")
    private Integer revFuelType;

    @DictName(code = "REV_FUEL_TYPE", joinField = "revFuelType")
    @ApiModelProperty(value = "REV燃料类型名称")
    private String revFuelTypeDisplay;

    @ApiModelProperty(value = "传统燃油车燃料类型")
    private String normalFuelType;

    /** 运营单位名称或运营车主名称 */
    @ApiModelProperty(value = "运营单位名称或运营车主名称")
    private String operUnitOrOwnerName;

    public void setIsGps(String isGps) {

        this.isGps = isGps;

        if (StringUtils.isNotEmpty(isGps)) {
            this.isGpsValue = "1".equals(this.isGps) ? 1 : 0;
        } else {
            this.isGpsValue = 0;
        }
    }

    public void setEfficiencyJson(String efficiencyJson) {
        this.efficiencyJson = efficiencyJson;
        ObjectMapper om = new ObjectMapper();
        try {
            VehModelEfficiencyModel e = om.readValue(efficiencyJson, VehModelEfficiencyModel.class);
            DataLoader.loadNames(e);
            this.revFuelType = e.getRevFuelType();
            this.revFuelTypeDisplay = e.getRevFuelTypeDisplay();
            this.normalFuelType = e.getNormalFuelType();
        } catch (Exception e) {
            // log.error("error", e);
        }
    }

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    @SneakyThrows
    public static VehicleRealStatusModel fromEntry(VehicleRealStatus entry) {
        VehicleRealStatusModel m = new VehicleRealStatusModel();
        BeanUtils.copyProperties(entry, m);

        //不在线时长
        if (StringUtils.isNotEmpty(m.getUpdateTime()) && m.getOnlineStatus() != null && m.getOnlineStatus() == 2) {
            String time = m.getUpdateTime();
            if (!m.getUpdateTime().contains("-")) {
                //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下

            }
            m.setOfflineTime(String.format("%.1f", DateUtil.getNowDataDifference(DateUtil.getNow(), time) / 60f));
        }
        if (m.getMils() != null) {
            m.setMils(noMoreZero(m.getMils()));
        }
        m.setOperUnitOrOwnerName(m.getOperUnitOrOwnerName());
        return m;
    }

    private static double noMoreZero(double val) {

        Formatter rmZero = new Formatter();
        // 进行格式化截断尾部小数并转化成字符串
        String rm = "" + rmZero.format("%g", val);
        // 将字符串解析成double并存入大数类
        BigDecimal todo = BigDecimal.valueOf(Double.parseDouble(rm));
        // 将数去0输出
        return todo.stripTrailingZeros().doubleValue();
    }

    public boolean isG6() {
        if (StringUtils.isBlank(termRuleTypeName)) {
            return false;
        }
        return termRuleTypeName.contains("国六") || termRuleTypeCode.contains("17691");
    }

    public boolean isGb() {
        return !isG6();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VehicleRealStatusModel) {
            return ((VehicleRealStatusModel) obj).getVin().equals(this.getVin());
        }
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, vehicleId, vin, licensePlate, operLicenseCityName, gpsCityName, onlined, onlinedDisplay, offlineTime, isRun, isRunValue, isCan, isCanValue, isGps, isGpsValue, isGpsDisplay, vehicleState, address, firstRegTime, onlineStatus, onlineStatusDisplay, mils, updateTime, createTime, dayReport, customFaults, dataItems, isAttention, interNo, vehModelName, termModelName, termSerialNumber, operUnitName, ruleTypeName, powerMode, powerModeDisplay, vehModelId, iccid, termId, operUnitId, sellCityName, termUnitName, vehBrandName, vehSeriesName, ownerName, operSupportOwnerName, faultStatus, soc, vehUnitName, operOwnerName, dataItemType, termRuleTypeName, termRuleTypeCode, uuid, identificationId, filingStatus, termPubKey, filingTime, efficiencyJson, revFuelType, revFuelTypeDisplay, normalFuelType);
    }

    public String getOperUnitOrOwnerName() {
        String str = "";
        if(StringUtils.isNotBlank(this.operUnitName)){
            str = this.operUnitName;
        }else if(StringUtils.isNotBlank(this.operOwnerName)){
            str = this.operOwnerName;
        }else{
            str = this.operUnitOrOwnerName;
        }
        return str;
    }
}


