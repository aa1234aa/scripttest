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

import com.bitnei.cloud.sys.domain.VehNotCan;
import org.springframework.beans.BeanUtils;
import java.util.Date;

import java.lang.String;
import java.sql.Timestamp;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Float;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotCan新增模型<br>
* 描述： VehNotCan新增模型<br>
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
* <td>2019-03-02 13:08:14</td>
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
@ApiModel(value = "VehNotCanModel", description = "无CAN车辆记录表Model")
public class VehNotCanModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "车辆id")
    @NotEmpty(message = "车辆id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆id")
    private String vehId;

    @ColumnHeader(title = "车辆标识uuid")
    @NotEmpty(message = "车辆标识uuid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆标识uuid")
    private String vehUuid;

    @ColumnHeader(title = "can最后上传时间")
    @NotEmpty(message = "can最后上传时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "can最后上传时间")
    private Timestamp lastUploadTime;

    @ColumnHeader(title = "can恢复上传时间")
    @NotEmpty(message = "can恢复上传时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "can恢复上传时间")
    private Timestamp regainUploadTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ColumnHeader(title = "是否无can上传：0是1否")
    @NotNull(message = "是否无can上传：0是1否不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否无can上传：0是1否")
    private Integer state;

    @ColumnHeader(title = "异常结束时里程，单位公里")
    @NotNull(message = "异常结束时里程，单位公里不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常结束时里程，单位公里")
    private Double regainUploadMileage;

    @ColumnHeader(title = "异常开始时里程，单位公里")
    @NotNull(message = "异常开始时里程，单位公里不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常开始时里程，单位公里")
    private Double lastUploadMileage;

    @ColumnHeader(title = "时间阈值,单位毫秒")
    @NotNull(message = "时间阈值,单位毫秒不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "时间阈值,单位毫秒")
    private Float timeThreshold;

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

    @ApiModelProperty(value = "运营区域")
    private String operAreaId;

    @ApiModelProperty(value = "终端厂商自定义编号")
    private String serialNumber;

    @ApiModelProperty(value = "iccid")
    private String iccid;

    @ApiModelProperty(value = "离线秒数")
    private String seconds;

    @ApiModelProperty(value = "离线天数")
    private String days;

    @ApiModelProperty(value = "can最后上传时间")
    private String showLastUploadTime;

    @ApiModelProperty(value = "can恢复上传时间")
    private String showRegainUploadTime;

    /** 车辆型号名称**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelName;

    /** 上牌城市名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operLicenseCityId", desc = "")
    private String operLicenseCityName;

    /** 运营区域名称**/
    @LinkName(table = "sys_area", column = "name", joinField = "operAreaId", desc = "")
    private String operAreaName;

    /** 用车单位名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId", desc = "")
    private String operUnitName;
    /**
      * 将实体转为前台model
      * @param entry
      * @return
      */
    public static VehNotCanModel fromEntry(VehNotCan entry){
        VehNotCanModel m = new VehNotCanModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setVin(entry.get("vin") == null ? null : entry.get("vin").toString());
        m.setInterNo(entry.get("interNo") == null ? null : entry.get("interNo").toString());
        m.setVehModelId(entry.get("vehModelId") == null ? null : entry.get("vehModelId").toString());
        m.setOperLicenseCityId(entry.get("operLicenseCityId") == null ? null : entry.get("operLicenseCityId").toString());
        m.setOperUnitId(entry.get("operUnitId") == null ? null : entry.get("operUnitId").toString());
        m.setOperAreaId(entry.get("operAreaId") == null ? null : entry.get("operAreaId").toString());
        m.setSerialNumber(entry.get("serialNumber") == null ? null : entry.get("serialNumber").toString());
        m.setIccid(entry.get("iccid") == null ? null : entry.get("iccid").toString());
        m.setVin(entry.get("isDelete") == null || "1".equals(entry.get("isDelete").toString()) ? m.getVin() == null ? "(已删除)" : m.getVin()+"(已删除)" : m.getVin());
        return m;
    }

}
