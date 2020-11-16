package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.UpgradeLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UpgradeLog新增模型<br>
 * 描述： UpgradeLog新增模型<br>
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
 * <td>2019-03-09 09:56:09</td>
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
@ApiModel(value = "UpgradeLogModel", description = "远程升级日志Model")
public class UpgradeLogModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "操作类型(10 上传升级包、20 编辑升级包、30 删除升级包、40 创建任务、50 删除任务、60 开始任务、70 开始车辆升级、80 终止车辆升级、90 强制终止车辆升级)")
    @NotNull(message = "操作类型(10 上传升级包、20 编辑升级包、30 删除升级包、40 创建任务、50 删除任务、60 开始任务、70 开始车辆升级、80 终止车辆升级、90 强制终止车辆升级)不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "操作类型(10 上传升级包、20 编辑升级包、30 删除升级包、40 创建任务、50 删除任务、60 开始任务、70 开始车辆升级、80 终止车辆升级、90 强制终止车辆升级)")
    private Integer action;

    @DictName(code = "UPGRADE_LOG_ACTION", joinField = "action")
    private String actionName;

    @ColumnHeader(title = "任务名称")
    @NotEmpty(message = "任务名称不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ColumnHeader(title = "操作车辆")
    @NotEmpty(message = "操作车辆不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "操作车辆")
    private String licensePlate;

    @ColumnHeader(title = "操作描述")
    @NotEmpty(message = "操作描述不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "操作描述")
    private String description;

    @ColumnHeader(title = "操作人")
    @NotEmpty(message = "操作人不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "操作人")
    private String createById;

    @ApiModelProperty(value = "操作姓名")
    private String createBy;

    @ApiModelProperty(value = "操作时间")
    private String createTime;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static UpgradeLogModel fromEntry(UpgradeLog entry) {
        UpgradeLogModel m = new UpgradeLogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
