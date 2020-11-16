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

import com.bitnei.cloud.veh.domain.DayreportChargestate;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportChargestate新增模型<br>
* 描述： DayreportChargestate新增模型<br>
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
* <td>2019-03-15 16:33:37</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayreportChargestateModel", description = "充耗电情况统计Model")
public class DayreportChargestateModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ColumnHeader(title = "报表日期 年月日 ")
    @NotEmpty(message = "报表日期 年月日 不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报表日期 年月日 ")
    private String reportTime;

    @ColumnHeader(title = "车辆编码")
    @NotEmpty(message = "车辆编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆编码")
    private String vid;

    @ColumnHeader(title = "日总充电时长h")
    @NotNull(message = "日总充电时长h不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日总充电时长h")
    private Double chargeTimeSum;

    @ColumnHeader(title = "充电次数")
    @NotNull(message = "充电次数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电次数")
    private Double chargeTimes;

    @ColumnHeader(title = "日总耗电量(Kw.h)")
    @NotNull(message = "日总耗电量(Kw.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日总耗电量(Kw.h)")
    private Double chargeConsume;

    @ColumnHeader(title = "百公里耗电量(Kw.h) ")
    @NotNull(message = "百公里耗电量(Kw.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "百公里耗电量(Kw.h) ")
    private Double chargeCon100km;

    @ColumnHeader(title = "日最大充电时长h")
    @NotNull(message = "日最大充电时长h不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日最大充电时长h")
    private Double chargeTimeMax;

    @ColumnHeader(title = "日平均充电时长")
    @NotNull(message = "日平均充电时长不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日平均充电时长")
    private Double chargeTimeAvg;

    @ColumnHeader(title = "充电状态最高总电压(v) ")
    @NotNull(message = "充电状态最高总电压(v)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最高总电压(v)")
    private Double chargeVolMax;

    @ColumnHeader(title = "充电状态最低总电压(v)")
    @NotNull(message = "充电状态最低总电压(v)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最低总电压(v)")
    private Double chargeVolMin;

    @ColumnHeader(title = "充电状态最高总电流(A)")
    @NotNull(message = "充电状态最高总电流(A)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最高总电流(A)")
    private Double chargeCurMax;

    @ColumnHeader(title = "充电状态最低总电流(A)")
    @NotNull(message = "充电状态最低总电流(A)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最低总电流(A)")
    private Double chargeCurMin;

    @ColumnHeader(title = "充电状态最大SOC(%)")
    @NotNull(message = "充电状态最大SOC(%)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最大SOC(%)")
    private Double chargeSocMax;

    @ColumnHeader(title = "充电状态最小SOC(%)")
    @NotNull(message = "充电状态最小SOC(%)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态最小SOC(%)")
    private Double chargeSocMin;

    @ColumnHeader(title = "充电状态单体最高电压(V)")
    @NotNull(message = "充电状态单体最高电压(V)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态单体最高电压(V)")
    private Double chargeSvolMax;

    @ColumnHeader(title = "充电状态单体最低电压(V)")
    @NotNull(message = "充电状态单体最低电压(V)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态单体最低电压(V)")
    private Double chargeSvolMin;

    @ColumnHeader(title = "充电状态采集点最高温度(°C)")
    @NotNull(message = "充电状态采集点最高温度(°C)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态采集点最高温度(°C)")
    private Double chargeCptempMax;

    @ColumnHeader(title = "充电状态采集点最低温度(°C)")
    @NotNull(message = "充电状态采集点最低温度(°C)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态采集点最低温度(°C)")
    private Double chargeCptempMin;

    @ColumnHeader(title = "充电状态电机最高温度(°C)")
    @NotNull(message = "充电状态电机最高温度(°C)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态电机最高温度(°C)")
    private Double chargeEngtempMax;

    @ColumnHeader(title = "充电状态电机最低温度(°C)")
    @NotNull(message = "充电状态电机最低温度(°C)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "充电状态电机最低温度(°C)")
    private Double chargeEngtempMin;

    @ColumnHeader(title = "单次充电最大耗电量(Kw.h)")
    @NotNull(message = "单次充电最大耗电量(Kw.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "单次充电最大耗电量(Kw.h)")
    private Double chargeSconsumeMax;

    @ColumnHeader(title = "日均单次耗电量(Kw.h)")
    @NotNull(message = "日均单次耗电量(Kw.h)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "日均单次耗电量(Kw.h)")
    private Double chargeSconsumeAvg;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportChargestateModel fromEntry(DayreportChargestate entry){
        DayreportChargestateModel m = new DayreportChargestateModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
