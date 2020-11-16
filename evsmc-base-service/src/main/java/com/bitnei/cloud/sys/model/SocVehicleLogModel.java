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

import com.bitnei.cloud.sys.domain.SocVehicleLog;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Float;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SocVehicleLog新增模型<br>
* 描述： SocVehicleLog新增模型<br>
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
* <td>2019-03-06 19:00:14</td>
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
@ApiModel(value = "SocVehicleLogModel", description = "SOC过低车辆Model")
public class SocVehicleLogModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ColumnHeader(title = "vid--uuid")
    @NotEmpty(message = "vid--uuid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vid--uuid")
    private String vid;

    @ColumnHeader(title = "开始soc")
    @NotEmpty(message = "开始soc不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "开始soc")
    private String startSoc;

    @ColumnHeader(title = "提醒开始时间")
    @NotEmpty(message = "提醒开始时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "提醒开始时间")
    private String startTime;

    @ColumnHeader(title = "提醒结束时间")
    @NotEmpty(message = "提醒结束时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "提醒结束时间")
    private String endTime;

    @ColumnHeader(title = "结束soc")
    @NotEmpty(message = "结束soc不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "结束soc")
    private String endSoc;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ColumnHeader(title = "当前位置")
    @NotEmpty(message = "当前位置不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "当前位置")
    private String location;

    @ColumnHeader(title = "经度")
    @NotEmpty(message = "经度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "经度")
    private String lng;

    @ColumnHeader(title = "纬度")
    @NotEmpty(message = "纬度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "纬度")
    private String lat;

    @ColumnHeader(title = "附近补电车(mysql--text,oracle-clob)")
    @NotEmpty(message = "附近补电车(mysql--text,oracle-clob)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "附近补电车(mysql--text,oracle-clob)")
    private String nearBatteryTruck;

    @ColumnHeader(title = "扩展字段1")
    @NotEmpty(message = "扩展字段1不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "扩展字段1")
    private String extra1;

    @ColumnHeader(title = "扩展字段2")
    @NotEmpty(message = "扩展字段2不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "扩展字段2")
    private String extra2;

    @ColumnHeader(title = "扩展字段3")
    @NotEmpty(message = "扩展字段3不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "扩展字段3")
    private String extra3;

    @ColumnHeader(title = "扩展字段4")
    @NotEmpty(message = "扩展字段4不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "扩展字段4")
    private String extra4;

    @ColumnHeader(title = "报警状态： 0报警中，1报警结束")
    @NotNull(message = "报警状态： 0报警中，1报警结束不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报警状态： 0报警中，1报警结束")
    private Integer status;

    @ColumnHeader(title = "SOC过低阈值,单位%")
    @NotNull(message = "SOC过低阈值,单位%不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "SOC过低阈值,单位%")
    private Float socThreshold;

    @ColumnHeader(title = "时间阈值，单位ms")
    @NotEmpty(message = "时间阈值，单位ms不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "时间阈值，单位ms")
    private String timeThreshold;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "vin")
    private String vin;

    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "用车单位id")
    private String operUnitId;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SocVehicleLogModel fromEntry(SocVehicleLog entry){
        SocVehicleLogModel m = new SocVehicleLogModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setVin(entry.get("vin") == null ? null : entry.get("vin").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin() == null ? "(已删除)" : m.getVin()+"(已删除)" : m.getVin());
        return m;
    }

}
