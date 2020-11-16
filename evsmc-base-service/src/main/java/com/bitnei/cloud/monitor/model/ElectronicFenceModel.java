package com.bitnei.cloud.monitor.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.monitor.domain.ElectronicFence;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.Date;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ElectronicFence新增模型<br>
 * 描述： ElectronicFence新增模型<br>
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
 * <td>2019-05-17 11:04:12</td>
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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ElectronicFenceModel", description = "电子围栏Model")
public class ElectronicFenceModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "组编码，时间戳")
//    @NotEmpty(message = "组编码，时间戳不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "组编码，时间戳")
    private String groupCode;

    @ColumnHeader(title = "围栏名称")
    @NotEmpty(message = "围栏名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Length(min = 1, max = 50, message = "围栏名称长度为1-50个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "围栏名称")
    private String name;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ColumnHeader(title = "规则状态1、启用0、禁用")
    @NotNull(message = "规则状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "规则状态1、启用0、禁用")
    private Integer ruleStatus;

    @ApiModelProperty(value = "规则状态")
    private String ruleStatusName;

    @ColumnHeader(title = "是否有效1、启用0、禁用")
//    @NotBlank(message = "是否有效不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "是否有效1、启用0、禁用")
    private Integer statusFlag;

    @ApiModelProperty(value = "是否有效")
    private String statusFlagName;

    @ColumnHeader(title = "规则类型8、驶离4、驶入")
    @NotNull(message = "规则类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "规则类型8、驶离4、驶入")
    private Integer ruleType;

    @ApiModelProperty(value = "规则类型")
    private String ruleTypeName;

    @ColumnHeader(title = "规则用途1、行驶围栏2、停车围栏")
    @NotNull(message = "规则用途不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "规则用途1、行驶围栏2、停车围栏")
    private Integer ruleUse;

    @ApiModelProperty(value = "规则用途")
    private String ruleUseName;

    @ColumnHeader(title = "周期类型1、单次执行2、每周循环3、每天循环")
    @NotNull(message = "周期类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "周期类型1、单次执行2、每周循环3、每天循环")
    private Integer periodType;

    @ApiModelProperty(value = "周期类型")
    private String periodTypeName;

    @ColumnHeader(title = "平台响应方式（多个用逗号分隔）：0、无1、系统弹窗2、声音提醒3、APP弹窗提醒4、短信通知")
    @NotNull(message = "平台响应方式不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "平台响应方式（多个用逗号分隔）：0、无1、系统弹窗2、声音提醒3、APP弹窗提醒4、短信通知")
    private String responseMode;

    @ApiModelProperty(value = "平台响应方式")
    private String responseModeName;

    @ColumnHeader(title = "开始日期")
//    @NotEmpty(message = "开始日期不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "开始日期String类型")
    private String startDateString;

    @ColumnHeader(title = "结束日期")
//    @NotEmpty(message = "结束日期不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "结束日期String类型")
    private String endDateString;

    @ColumnHeader(title = "星期，多个之间用的逗号分隔，周一为1到周日为7")
//    @NotEmpty(message = "星期，多个之间用的逗号分隔，周一为1到周日为7不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "星期，多个之间用的逗号分隔，周一为1到周日为7")
    private String ruleWeek;

    @ColumnHeader(title = "开始启用时间")
    @NotEmpty(message = "开始启用时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "开始启用时间")
    private String startTime;

    @ColumnHeader(title = "结束启用时间")
    @NotEmpty(message = "结束启用时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "结束启用时间")
    private String endTime;

    @ColumnHeader(title = "chart类型1、圆形2、多边形")
    @NotNull(message = "围栏图形类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "chart类型1、圆形2、多边形")
    private Integer chartType;

    @ApiModelProperty(value = "围栏图形类型")
    private String chartTypeName;

    @ColumnHeader(title = "经纬度范围当地图类型 为1圆形时，两个值为半径;圆点为2多边形时，每一个;的值为经纬度点")
    @NotEmpty(message = "围栏坐标不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "经纬度范围当地图类型为1圆形时，两个值为半径;圆点为2多边形时，每一个;的值为经纬度点")
    private String lonlatRange;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static ElectronicFenceModel fromEntry(ElectronicFence entry) {
        ElectronicFenceModel m = new ElectronicFenceModel();
        BeanUtils.copyProperties(entry, m);
        m.setStartDateString(DateUtil.formatTime(entry.getStartDate(), DateUtil.DAY_FORMAT));
        m.setEndDateString(DateUtil.formatTime(entry.getEndDate(), DateUtil.DAY_FORMAT));
        // 转译规则状态
        switch (m.getRuleStatus()) {
            case 1:
                m.setRuleStatusName("启用");
                break;
            case 0:
                m.setRuleStatusName("禁用");
                break;
        }
        // 转译有效状态
        switch (m.getStatusFlag()) {
            case 1:
                m.setStatusFlagName("有效");
                break;
            case 0:
                m.setStatusFlagName("无效");
        }
        // 转译规则类型
        switch (m.getRuleType()) {
            case 4:
                m.setRuleTypeName("驶入");
                break;
            case 8:
                m.setRuleTypeName("驶离");
                break;
        }
        // 转译规则用途
        switch (m.getRuleUse()) {
            case 1:
                m.setRuleUseName("行驶围栏");
                break;
            case 2:
                m.setRuleUseName("停车围栏");
                break;
        }
        // 转译周期类型
        switch (m.getPeriodType()) {
            case 1:
                m.setPeriodTypeName("单次执行");
                break;
            case 2:
                m.setPeriodTypeName("每周循环");
                break;
            case 3:
                m.setPeriodTypeName("每天循环");
                break;
        }
        // 转译平台响应方式
        if (StringUtils.isNotEmpty(m.getResponseMode())) {
            String[] rm = m.getResponseMode().split(",");
            String responseModeName = "";
            for (String s : rm) {
                switch (s) {
                    case "0":
                        responseModeName += ",无";
                        break;
                    case "1":
                        responseModeName += ",系统弹窗";
                        break;
                    case "2":
                        responseModeName += ",声音提醒";
                        break;
                    case "3":
                        responseModeName += ",APP弹窗提醒";
                        break;
                    case "4":
                        responseModeName += ",短信通知";
                        break;
                }
            }
            if (StringUtils.isNotEmpty(responseModeName.substring(1, responseModeName.length()))) {
                responseModeName = responseModeName.substring(1, responseModeName.length());
            }
            m.setResponseModeName(responseModeName);
        }
        // 转译围栏图形类型
        switch (m.getChartType()) {
            case 1:
                m.setChartTypeName("圆形");
                break;
            case 2:
                m.setChartTypeName("多边形");
                break;
        }
        return m;
    }

}
