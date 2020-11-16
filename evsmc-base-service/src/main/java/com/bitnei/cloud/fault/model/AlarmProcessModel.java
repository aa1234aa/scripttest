package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.AlarmProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmProcess新增模型<br>
 * 描述： AlarmProcess新增模型<br>
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
 * <td>2019-03-04 17:13:13</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AlarmProcessModel", description = "故障处理表Model")
public class AlarmProcessModel extends BaseModel {

    private String id;

    @ApiModelProperty(value = "报警信息id")
    private String faultAlarmId;

    @ApiModelProperty(value = "保存故障处理时, 传报警信息id, 多个故障处理里以：id1,id2,id3 传")
    private String faultAlarmIds;

    @ApiModelProperty(value = "处理状态  1:未处理, 2:处理中, 3:已处理")
    private Integer procesStatus;

    @ApiModelProperty(value = "故障处理状态")
    @DictName(code = "PROCES_STATUS", joinField = "procesStatus")
    private String procesStatusDisplay;

    @NotBlank(message = "备注不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否再次提醒: 0=不再提醒, 30=30分钟后, 60=一小时后, 120=两小时后, 360=六小时后")
    private Integer againRemindStatus;

    @ApiModelProperty(value = "是否再次提醒")
    @DictName(code = "AGAIN_REMIND", joinField = "againRemindStatus")
    private String againRemindStatusDisplay;

    @ApiModelProperty(value = "再次提醒时间")
    private String againRemindTime;

    private String createTime;

    private String createBy;

    @NotNull(message = "处理类型(type)不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "用type区分实时与历史; type = 1实时故障处理；type = 2 历史故障处理")
    private Integer type;

    @ApiModelProperty(value = "保存故障处理时, 传报警信息中的故障开始时间, 多个故障处理里以：value1, value2, value3传")
    private String faultBeginTimes;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static AlarmProcessModel fromEntry(AlarmProcess entry) {
        AlarmProcessModel m = new AlarmProcessModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

    public void checkParamLength() {
        if (StringUtils.isBlank(faultAlarmIds)) {
            throw new BusinessException("报警信息id不能为空");
        }
        if (type.intValue() == 1) {
            if (procesStatus == null) {
                throw new BusinessException("处理状态不能为空");
            }
            if (null == againRemindStatus) {
                throw new BusinessException("是否再次提醒不能为空");
            }
        }
        if (StringUtils.isBlank(remark)) {
            throw new BusinessException("备注不能为空");
        } else {
            if (remark.length() > 200) {
                throw new BusinessException("备注长度为:1~200");
            }
        }
    }
}
