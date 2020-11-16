package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.VehIdleRecord;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehIdleRecord新增模型<br>
* 描述： VehIdleRecord新增模型<br>
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
* <td>2019-03-06 14:44:04</td>
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
@ApiModel(value = "VehIdleRecordModel", description = "长期离线车辆表Model")
public class VehIdleRecordModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "车辆id")
    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ColumnHeader(title = "车辆标识uuid")
    @ApiModelProperty(value = "车辆标识uuid")
    private String vehicleUuid;

    @ColumnHeader(title = "恢复上线时间")
    @ApiModelProperty(value = "恢复上线时间")
    private String regainOnlineTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ColumnHeader(title = "是否结束 0: 已结束 1：未结束(实时)")
    @ApiModelProperty(value = "是否结束 0: 已结束 1：未结束(实时)")
    private Integer state;

    @ColumnHeader(title = "提醒状态（0进行中，1已结束）")
    @NotBlank(message = "请设置提醒状态", groups = {GroupUpdate.class})
    @ApiModelProperty(value = "提醒状态（0进行中，1已结束）")
    private String remindState;

    @ColumnHeader(title = "备注")
    @NotEmpty(message = "备注不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ColumnHeader(title = "当前数据阈值")
    @ApiModelProperty(value = "当前数据阈值")
    private String threshold;

    @ColumnHeader(title = "最后上线时间")
    @ApiModelProperty(value = "最后上线时间")
    private String lastOnlineTime;

    @ColumnHeader(title = "离线时里程，单位公里")
    @ApiModelProperty(value = "离线时里程，单位公里")
    private Double lastOnlineMileage;

    @ColumnHeader(title = "恢复上线时里程,单位km")
    @ApiModelProperty(value = "恢复上线时里程,单位km")
    private Double regainOnlineMileage;


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

    @ApiModelProperty(value = "离线秒数")
    private String seconds;

    @ApiModelProperty(value = "离线天数")
    private String days;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;

    /** 提醒状态名称**/
    @DictName(code = "REMIND_STATE",joinField = "remindState")
    private String remindStateName;

    /** 离线时里程用于导出显示**/
    private String lastOnlineMileageExport;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehIdleRecordModel fromEntry(VehIdleRecord entry){
        VehIdleRecordModel m = new VehIdleRecordModel();
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
