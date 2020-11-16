package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.ApplicationContextProvider;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.InstructSendRule;
import com.bitnei.cloud.sys.service.impl.DictService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructSendRule新增模型<br>
 * 描述： InstructSendRule新增模型<br>
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
 * <td>2019-03-07 10:28:43</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "InstructSendRuleModel", description = "国标控制指令Model")
public class InstructSendRuleModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "车辆vin")
    @NotEmpty(message = "车辆vin不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆vin")
    private String vin;

    @ColumnHeader(title = "车牌号")
    @NotEmpty(message = "车牌号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "历史在线状态")
    @NotNull(message = "历史在线状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "历史在线状态")
    private Integer historyOnlineState;

    @DictName(code = "ONLINE_STATUS", joinField = "historyOnlineState")
    private String historyOnlineStateName;

    @ColumnHeader(title = "添加人")
    @NotEmpty(message = "添加人不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "添加人")
    private String createById;

    @ApiModelProperty(value = "添加人姓名")
    private String createBy;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ColumnHeader(title = "发送结果 0：成功  1：失败  2：超时")
    @NotNull(message = "发送结果不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "发送结果")
    private Integer sendResult;

    @DictName(code = "SEND_RULE_RESULT", joinField = "sendResult")
    private String sendResultName;

    @ColumnHeader(title = "执行结果")
    @NotNull(message = "执行结果不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "执行结果")
    private Integer executeResult;

    //    @DictName(code = "EXECUTE_LOCK_RESULT", joinField = "executeResult")
    @ApiModelProperty(value = "锁车专用，执行结果描述")
    private String executeLockResultName;

    @ColumnHeader(title = "运营区域（存储结果，非id）")
    @NotEmpty(message = "运营区域（存储结果，非id）不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营区域（存储结果，非id）")
    private String operatingArea;

    @ColumnHeader(title = "运营单位")
    @NotEmpty(message = "运营单位不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营单位")
    private String operatingUnit;

    @ColumnHeader(title = "运营负责人")
    @NotEmpty(message = "运营负责人不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营负责人")
    private String operationUserName;

    @ColumnHeader(title = "联系方式")
    @NotEmpty(message = "联系方式不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "联系方式")
    private String operationPhone;

    @ColumnHeader(title = "运营负责人id")
    @NotEmpty(message = "运营负责人id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营负责人id")
    private String operationUserId;

    @ColumnHeader(title = "国标value;控制命令id（当type=2即为动力锁车时）")
    @NotEmpty(message = "国标value;控制命令id（当type=2即为动力锁车时）不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "国标value;控制命令id（当type=2即为动力锁车时）")
    private String standardValue;

    @ColumnHeader(title = "国标code;控制命令参数0x83（当type=2即为动力锁车时）")
    @NotEmpty(message = "国标code;控制命令参数0x83（当type=2即为动力锁车时）不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "国标code;控制命令参数0x83（当type=2即为动力锁车时）")
    private String standardCode;

    @ColumnHeader(title = "响应时间")
    @NotEmpty(message = "响应时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "响应时间")
    private String responseTime;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ColumnHeader(title = "0、远程终端控制状态 ，2、动力锁车")
    @NotNull(message = "0、远程终端控制状态 ，2、动力锁车不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "0、远程终端控制状态 ，2、动力锁车")
    private Integer type;

    @ColumnHeader(title = "命令名称")
    @NotEmpty(message = "命令名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "命令名称")
    private String instructName;

    @ColumnHeader(title = "流水号")
    @NotEmpty(message = "流水号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "流水号")
    private String sessionId;

    @ColumnHeader(title = "数据来源1页面执行2接口执行")
    @NotNull(message = "数据来源1页面执行2接口执行不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "数据来源1页面执行2接口执行")
    private Integer dataSource;

    @ColumnHeader(title = "报警等级，type=1远程控制时用")
    @NotNull(message = "报警等级，type=1远程控制时用不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "报警等级，type=1远程控制时用")
    private Integer alarmLevel;

    @ColumnHeader(title = "指令缓存时间")
    @NotNull(message = "指令缓存时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "指令缓存时间")
    private Integer instructCacheTime;

    @ColumnHeader(title = "命令执行结果(失败原因)")
    @NotEmpty(message = "命令执行结果(失败原因)不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "命令执行结果(失败原因)")
    private String instructRemark;

    @ColumnHeader(title = "内部编码")
    @ApiModelProperty(value = "内部编码")
    private String interNo;

    @ColumnHeader(title = "车辆型号名称")
    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    @ColumnHeader(title = "指令任务有效时间")
    @ApiModelProperty(value = "指令任务有效时间")
    private Integer effectiveTime;

    @ColumnHeader(title = "指令任务时间单位：1天数2小时数3分钟")
    @ApiModelProperty(value = "指令任务时间单位：1天数2小时数3分钟")
    private Integer effectiveTimeType;

    @ColumnHeader(title = "指令任务创建时间")
    @ApiModelProperty(value = "指令任务创建时间")
    private String taskCreateTime;

    @ColumnHeader(title = "指令缓存剩余时间")
    @ApiModelProperty(value = "指令缓存剩余时间")
    private String taskLeftTime;

    public void setSendResult(Integer sendResult) {
        //发送结果 0：成功  1：失败  2：超时
        this.sendResult = sendResult;
        if (null != this.sendResult &&
                (null == this.executeResult || !getExecuteResultMap().containsKey(this.executeResult.toString()))) {
            this.executeLockResultName = "无效";
        } else {
            if (null != this.executeResult) {
                this.executeLockResultName = getExecuteResultMap().get(this.executeResult.toString()).getName();
            }
        }
    }

    static class ExecuteResult {

        public static Map<String, DictModel> executeResultMap =
                ApplicationContextProvider.getBean(DictService.class).findCacheByDictType("EXECUTE_LOCK_RESULT");
    }

    public static Map<String, DictModel> getExecuteResultMap() {

        return ExecuteResult.executeResultMap;
    }


    /**
     * 执行结果字典集合，有时间再改成字典表读取，本地缓存一下
     */
    protected static final List<Integer> EXECUTE_RESULT_LIST =
            Arrays.asList(0, 1, 2, 3, 4, 5, 16, 17, 18, 19, 32, 33, 34, 35, 48, 49, 50, 51, 52, 999999);

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    @SneakyThrows
    public static InstructSendRuleModel fromEntry(InstructSendRule entry) {
        InstructSendRuleModel m = new InstructSendRuleModel();
        BeanUtils.copyProperties(entry, m);
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != m.getTaskCreateTime()) {
            m.setTaskLeftTime(getInstructSurplusTime(sdf.parse(m.getTaskCreateTime()), nowDate,
                    m.getEffectiveTime(), m.getEffectiveTimeType()));
        } else if (null != entry.getInstructCacheTime()) {
            m.setTaskLeftTime((transDatePoor(entry.getInstructCacheTime() * 1000 * 60L)));
        }
        return m;
    }

    /**
     * 计算指令缓存剩余时间
     *
     * @param createTime        指令缓存创建时间
     * @param effectiveTime     指令缓存有效时间
     * @param effectiveTimeType 指令缓存有效时间单位：1天数2小时数3分钟
     * @return
     */
    public static String getInstructSurplusTime(Date createTime, Date nowDate, Integer effectiveTime,
                                                Integer effectiveTimeType) {
        if (null == createTime || null == effectiveTime || null == effectiveTimeType) {
            return "";
        }
        long time = 0;
        // 计算到毫秒级
        if (1 == effectiveTimeType) {
            time = (long)effectiveTime * 24 * 60 * 60 * 1000;
        } else if (2 == effectiveTimeType) {
            time = (long)effectiveTime * 60 * 60 * 1000;
        } else if (3 == effectiveTimeType) {
            time = (long)effectiveTime * 60 * 1000;
        }
        // 计算指令缓存是否已经超过最大时间
        if ((nowDate.getTime() - createTime.getTime()) >= time) {
            return "0分钟";
        }
        return getDatePoor(createTime, nowDate, time);
    }

    /**
     * 获取时间差
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param maxTime   时间上限
     * @return
     */
    public static String getDatePoor(Date beginDate, Date endDate, Long maxTime) {
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = 0;
        if (null != maxTime) {
            diff = maxTime - (endDate.getTime() - beginDate.getTime());
        } else {
            diff = endDate.getTime() - beginDate.getTime();
        }
        return transDatePoor(diff);
    }

    public static String transDatePoor(Long diff) {
        long nd = (long) 1000 * 24 * 60 * 60;
        long nh = (long) 1000 * 60 * 60;
        long nm = (long) 1000 * 60;

        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        if (day != 0) {
            return day + "天" + hour + "小时" + min + "分钟";
        } else if (hour != 0) {
            return hour + "小时" + min + "分钟";
        } else {
            return min + "分钟";
        }
    }

}
