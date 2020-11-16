package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.common.SysDefine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import com.bitnei.cloud.sys.domain.UppackageSendDetails;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UppackageSendDetails新增模型<br>
 * 描述： UppackageSendDetails新增模型<br>
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
 * <td>2019-03-05 15:53:14</td>
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
@ApiModel(value = "UppackageSendDetailsModel", description = "远程升级任务详情Model")
public class UppackageSendDetailsModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "vin码")
    @NotEmpty(message = "vin码不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "vin码")
    private String vin;

    @ColumnHeader(title = "车牌号")
    @NotEmpty(message = "车牌号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "车辆类型id")
    @ApiModelProperty(value = "车辆类型id")
    private String vehicleTypeId;

    @ColumnHeader(title = "车辆类型")
    @ApiModelProperty(value = "车辆类型")
    private String vehicleTypeValue;

    @ColumnHeader(title = "车型id")
    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ColumnHeader(title = "车型名称")
    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ColumnHeader(title = "升级包名称")
    @NotEmpty(message = "升级包名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级包名称")
    private String fileName;

    @ColumnHeader(title = "文件下发状态：0：未下发、1：已完成、2：下发失败")
    @NotNull(message = "文件下发状态：0：未下发、1：已完成、2：下发失败不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "文件下发状态：0：未下发、1：已完成、2：下发失败")
    private Integer fileSendStatus;

    @DictName(code = "FILE_SEND_STATUS", joinField = "fileSendStatus")
    private String fileSendStatusName;

    @ColumnHeader(title = "升级状态：0未开始、1升级中、2升级成功、3升级失败")
    @NotNull(message = "升级状态：0未开始、1升级中、2升级成功、3升级失败 不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级状态：0未开始、1升级中、2升级成功、3升级失败；")
    private Integer upgradeStatus;

    @DictName(code = "UPGRADE_STATUS", joinField = "upgradeStatus")
    private String upgradeStatusName;

    @ColumnHeader(title = "任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务")
    @NotNull(message = "任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务",
            groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务")
    private Integer uppackageSendStatus;

    @DictName(code = "UPPACKAGE_SEND_STATUS", joinField = "uppackageSendStatus")
    private String uppackageSendStatusName;

    @ColumnHeader(title = "升级包id")
    @NotEmpty(message = "升级包id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级包id")
    private String uppackageInfoId;

    @ColumnHeader(title = "升级任务id")
    @NotEmpty(message = "升级任务id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级任务id")
    private String uppackageSendId;

    @ColumnHeader(title = "升级指令下发状态 0未开始；1下发成功；2下发失败；9终端离线下发失败")
    @NotNull(message = "升级指令下发状态不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级指令下发状态")
    private Integer upgradeSendState;

    @DictName(code = "UPGRADE_SEND_STATUS", joinField = "upgradeSendState")
    private String upgradeSendStateName;

    @ColumnHeader(title = "升级任务百分比")
    @NotEmpty(message = "升级任务百分比不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "升级任务百分比")
    private String percentage;

    @ColumnHeader(title = "历史在线状态：1：在线 2:离线、空为从未上过线")
    @NotNull(message = "历史在线状态：1：在线 2:离线、空为从未上过线不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "历史在线状态：1：在线 2:离线、空为从未上过线")
    private Integer historyOnlineState;

    @DictName(code = "ONLINE_STATUS", joinField = "historyOnlineState")
    private String historyOnlineStateName;

    @ColumnHeader(title = "当前在线状态  1：在线 2:离线、空为从未上过线")
    @ApiModelProperty(value = "当前在线状态  1：在线 2:离线、空为从未上过线")
    private Integer onlineState;

    @DictName(code = "ONLINE_STATUS", joinField = "onlineState")
    private String onlineStateName;

    @ColumnHeader(title = "运营区域")
    @NotEmpty(message = "运营区域不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营区域")
    private String operatingArea;

    @ColumnHeader(title = "session_id")
    @NotEmpty(message = "session_id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "session_id")
    private String sessionId;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remark;

    @ColumnHeader(title = "创建时间")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ColumnHeader(title = "修改时间")
    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ColumnHeader(title = "内部编号")
    @NotEmpty(message = "内部编号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ColumnHeader(title = "指令缓存时间")
    private Integer instructCacheTime;

    // 辅助字段
    @ColumnHeader(title = "升级指令类型：1国标；128自定义；99自定义指令")
    @ApiModelProperty(value = "升级指令类型：1国标；128自定义；99自定义指令")
    private Integer protocolType;

    @DictName(code = "PROTOCOL_TYPE", joinField = "protocolType")
    private String protocolTypeName;

    @ColumnHeader(title = "任务名称")
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ColumnHeader(title = "升级时间")
    @ApiModelProperty(value = "升级时间")
    private String upgradeTime;

    @ColumnHeader(title = "iccid")
    @ApiModelProperty(value = "iccid")
    private String iccid;

    @ColumnHeader(title = "升级任务创建时间")
    @ApiModelProperty(value = "升级任务创建时间")
    private String taskCreateTime;

    @ColumnHeader(title = "指令缓存剩余时间")
    @ApiModelProperty(value = "指令缓存剩余时间")
    private String taskLeftTime;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    @SneakyThrows
    public static UppackageSendDetailsModel fromEntry(UppackageSendDetails entry) {
        UppackageSendDetailsModel m = new UppackageSendDetailsModel();
        BeanUtils.copyProperties(entry, m);
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != m.getTaskCreateTime()) {
            m.setTaskLeftTime(InstructSendRuleModel.getInstructSurplusTime(sdf.parse(m.getTaskCreateTime()), nowDate,
                    m.getInstructCacheTime(), SysDefine.INSTRUCT_EFFECTIVE_TIME_TYPE));
        } else if (null != entry.getInstructCacheTime()) {
            m.setTaskLeftTime(InstructSendRuleModel.transDatePoor(entry.getInstructCacheTime() * 1000 * 60L));
        }
        return m;
    }

    public void setFileSendStatusName(String fileSendStatusName) {
        this.fileSendStatusName = fileSendStatusName;
        if (null != this.protocolType && (this.protocolType.equals(1) || this.protocolType.equals(2))) {
            this.fileSendStatusName = "--";
        }
    }

    public void setUpgradeStatusName(String upgradeStatusName) {
        this.upgradeStatusName = upgradeStatusName;
        if (null != this.protocolType && (this.protocolType.equals(1) || this.protocolType.equals(2))) {
            this.upgradeStatusName = "--";
        }
    }
}
