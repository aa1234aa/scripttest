package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AlarmInfoHistory新增模型<br>
* 描述： AlarmInfoHistory新增模型<br>
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
* <td>2019-03-04 20:02:06</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AlarmInfoHistoryModel", description = "历史故障信息Model")
public class AlarmInfoHistoryModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    private String uuid;

    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ApiModelProperty(value = "故障类型：1 = 平台, 2 = 故障码")
    private Integer faultType;

    @ApiModelProperty(value = "故障类型")
    @DictName(code = "FAULT_TYPE", joinField = "faultType")
    private String faultTypeDisplay;

    @ApiModelProperty(value = "父规则id")
    private String parentRuleId;

    @ApiModelProperty(value = "规则id")
    private String ruleId;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "报警级别 0：无、1：1级、2：2级、3：3级")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警级别")
    @DictName(code = "ALARM_LEVEL", joinField = "alarmLevel")
    private String alarmLevelDisplay;

    @ApiModelProperty(value = "报警开始时间")
    private String faultBeginTime;

    @ApiModelProperty(value = "报警结束时间")
    private String faultEndTime;

    @ApiModelProperty(value = "故障状态  1:进行中, 2已结束")
    private Integer faultStatus;

    @ApiModelProperty(value = "故障状态")
    @DictName(code = "FAULT_STATUS", joinField = "faultStatus")
    private String faultStatusDisplay;

    @ApiModelProperty(value = "故障经度")
    private Double longitude;

    @ApiModelProperty(value = "故障纬度")
    private Double latitude;

    @ApiModelProperty(value = "响应方式：0= 无、1=系统弹窗、2=系统弹窗+声音提醒、3=APP弹窗提醒、4=短信通知")
    private String responseMode;

    @ApiModelProperty(value = "故障触发项")
    private String triggerItem;

    @ApiModelProperty(value = "提醒类型")
    @DictName(code = "RESPONSE_MODE", joinField = "responseMode")
    private String responseModeDisplay;

    @ApiModelProperty(value = "处理状态  1:未处理, 2:处理中, 3:已处理")
    private Integer procesStatus;

    @ApiModelProperty(value = "故障处理状态")
    @DictName(code = "PROCES_STATUS", joinField = "procesStatus")
    private String procesStatusDisplay;

    @ApiModelProperty(value = "处理时间")
    private String procesTime;

    @ApiModelProperty(value = "推送状态 1未推送，2已推送")
    private Integer pushStatus;

    @ApiModelProperty(value = "故障推送状态")
    @DictName(code = "FAULT_PUSH_STATUS", joinField = "pushStatus")
    private String pushStatusDisplay;

    @ApiModelProperty(value = "推送时间")
    private String pushTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /** 车辆vin **/
    @ApiModelProperty(value = "车辆vin")
    private String vin;

    /** 车牌 **/
    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    /** 车辆型号 **/
    private String vehModelName;

    /**购车领域**/
    @ApiModelProperty(value = "购车领域")
    private String sellForField;

    /** 个人名称/购车单位 **/
    @ApiModelProperty(value = "个人名称/购车单位")
    private String sellUnitOrVita;

    /** 联系方式 **/
    @ApiModelProperty(value = "联系方式")
    private String telephone;

    /** 持续时长 **/
    private String duration;

    /**中文地理位置**/
    @ApiModelProperty(value = "中文地理位置")
    private String address;

    /**处理记录**/
    @ApiModelProperty(value = "历史处理记录")
    private String processRecord;

    /**
     * 围栏类型  圆形 多边形
     */
    @ApiModelProperty(value = "围栏类型  圆形 多边形")
    private String chartType;

    /**
     * 形状的经纬度
     */
    @ApiModelProperty(value = "经纬度")
    private String lonlatRange;

    /**
     * 处理记录数组
     */
    private List<AlarmProcessModel> processlList;

    /**
     *  权限字段
     */
    @ApiModelProperty(value = "权限字段")
    private String whoCanSeeMe;

    private String[] whoCanSeeMes;

    public void setWhoCanSeeMe(String whoCanSeeMe) {
        this.whoCanSeeMe = whoCanSeeMe;
        this.whoCanSeeMes = this.whoCanSeeMe.split("\\s+");
    }

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static AlarmInfoHistoryModel fromEntry(AlarmInfoHistory entry){
        AlarmInfoHistoryModel m = new AlarmInfoHistoryModel();
        BeanUtils.copyProperties(entry, m);
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(m, entry.getTails());
        } catch (IllegalAccessException| InvocationTargetException ignore) {
        }

        if (StringUtils.isNotEmpty(entry.getWhoCanSeeMe())) {
            m.setWhoCanSeeMes(entry.getWhoCanSeeMe().split("\\s+"));
        }
        return m;
    }

}
