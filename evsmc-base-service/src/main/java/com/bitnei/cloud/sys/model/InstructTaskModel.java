package com.bitnei.cloud.sys.model;

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

import com.bitnei.cloud.sys.domain.InstructTask;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructTask新增模型<br>
* 描述： InstructTask新增模型<br>
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
* <td>2019-03-07 10:26:27</td>
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
@ApiModel(value = "InstructTaskModel", description = "指令任务执行记录Model")
public class InstructTaskModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "指令记录id")
    @NotEmpty(message = "指令记录id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令记录id")
    private String instructId;

    @ColumnHeader(title = "指令类型：1远程控制，2远程升级")
    @NotNull(message = "指令类型：1远程控制，2远程升级不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令类型：1远程控制，2远程升级")
    private Integer instructType;

    @ColumnHeader(title = "指令任务有效时间")
    @NotNull(message = "指令任务有效时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令任务有效时间")
    private Integer effectiveTime;

    @ColumnHeader(title = "指令任务时间单位：1天数2小时数3分钟")
    @NotNull(message = "指令任务时间单位：1天数2小时数3分钟不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令任务时间单位：1天数2小时数3分钟")
    private Integer effectiveTimeType;

    @ColumnHeader(title = "任务已执行次数")
    @NotNull(message = "任务已执行次数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务已执行次数")
    private Integer executedNum;

    @ColumnHeader(title = "任务最大执行次数")
    @NotNull(message = "任务最大执行次数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务最大执行次数")
    private Integer executeMaxNum;

    @ColumnHeader(title = "任务执行结果")
    @NotEmpty(message = "任务执行结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务执行结果")
    private String taskResult;

    @ColumnHeader(title = "有效状态：1有效0无效")
    @NotNull(message = "有效状态：1有效0无效不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "有效状态：1有效0无效")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "上次执行时间")
    private String executeTime;


    /**
     * 协议类型：1：国标，2：CodeValue，128：自定义，99：自定义指令
     */
    private Integer protocolType;
    /**
     * 升级模式：1：强制升级、2：普通升级
     */
    private Integer schemaType;
    /**
     * 升级包id
     */
    private String uppackageId;
    /**
     * 车辆vin
     */
    private String vin;
    /**
     * 终端控制-控制命令
     */
    private String standardCode;
    /**
     * 终端控制-报警等级
     */
    private Integer alarmLevel;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static InstructTaskModel fromEntry(InstructTask entry){
        InstructTaskModel m = new InstructTaskModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
