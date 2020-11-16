package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.VehModelAlarm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 车辆车型报警模型<br>
* 描述： 车辆车型报警模型<br>
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
* <td>2019-02-20 13:45:57</td>
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
@ApiModel(value = "VehModelAlarmModel", description = "车型报警阈值Model, 属性值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  <br/>无值保留间隔符,有值格式为数值,最长6位,保留2位小数点")
public class VehModelAlarmModel extends BaseModel {

    @ApiModelProperty(value = "主键标识|车型ID")
    private String id;

    @ApiModelProperty(value = "温度差异报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "温度差异报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String temperatureDifferenceAlarm;

    @ApiModelProperty(value = "电池高温报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "电池高温报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String batteryHighTemperaturAlarm;

    @ApiModelProperty(value = "车载储能装置类型过压报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "车载储能装置类型过压报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String energyDeviceOvervoltageAlarm;

    @ApiModelProperty(value = "车载储能装置类型欠压报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "车载储能装置类型欠压报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String energyDeviceUndervoltageAlarm;

    @ApiModelProperty(value = "驱动电机控制器温度报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "驱动电机控制器温度报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String driveMotorControllerTemperaAlarm;

    @ApiModelProperty(value = "驱动电机温度报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "驱动电机温度报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String driveMotorTemperatureAlarm;

    @ApiModelProperty(value = "单体电池过压报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "单体电池过压报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String singleBatteryOvervoltageAlarm;

    @ApiModelProperty(value = "单体电池欠压报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "单体电池欠压报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String singleBatteryUndervoltageAlarm;

    @ApiModelProperty(value = "SOC低报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "SOC低报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String socLowAlarm;

    @ApiModelProperty(value = "SOC跳变报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "SOC跳变报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String socJumpAlarm;

    @ApiModelProperty(value = "SOC过高报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "SOC过高报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String socHighAlarm;

    @ApiModelProperty(value = "电池单体一致性差报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "电池单体一致性差报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String batteryConsistencyAlarm;

    @ApiModelProperty(value = "车载储能装置过充报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "车载储能装置过充报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String vehicularDeviceOverchargeAlarm;

    @ApiModelProperty(value = "绝缘故障报警值")
    @Pattern(regexp = "^(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?;(-?\\d{1,6}(\\.\\d{1,2})?)?,(-?\\d{1,6}(\\.\\d{1,2})?)?$",
            message = "绝缘故障报警值格式:1级下限,1级上限;2级下限,2级上限;3级下限,3级上限  无值保留间隔符,有值格式为数值,最长6位,保留2位小数点", groups = {GroupInsert.class, GroupUpdate.class})
    private String insulationFaultAlarm;

    @ApiModelProperty(value = "是否启用")
    private int enable;

    @ApiModelProperty(value = "平台响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知")
    private String responseMode;

    @Min(value = 0, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "开始时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
//    @NotNull(message = "开始时间(秒)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "开始时间阈值(秒)")
    private Integer beginThreshold;

    @Min(value = 0, message = "结束时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Max(value = 3600, message = "结束时间阈值范围为0-3600秒", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
//    @NotNull(message = "结束时间(秒)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "结束时间阈值(秒)")
    private Integer endThreshold;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehModelAlarmModel fromEntry(VehModelAlarm entry){
        VehModelAlarmModel m = new VehModelAlarmModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
