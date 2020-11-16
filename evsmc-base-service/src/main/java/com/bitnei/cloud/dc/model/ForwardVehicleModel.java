package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardVehicle新增模型<br>
* 描述： ForwardVehicle新增模型<br>
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
* <td>2019-02-21 14:29:14</td>
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
@ApiModel(value = "ForwardVehicleModel", description = "转发车辆Model")
public class ForwardVehicleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "转发平台")
    @NotEmpty(message = "转发平台不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发平台")
    private String platformId;

    /** 转发平台名称显示**/
    /*@LinkName(table = "dc_forward_platform", column = "name", joinField = "platformId",desc = "")*/
    @ApiModelProperty(value = "转发平台名称")
    private String platformDisplay;

    @ColumnHeader(title = "确认状态")
    @ApiModelProperty(value = "确认状态")
    private Integer confirmStatus;

    /** 确认状态名称显示**/
    @DictName(code = "CONFIRMED_STATUS", joinField = "confirmStatus")
    @ApiModelProperty(value = "确认状态名称")
    private String confirmStatusDisplay;

    @ColumnHeader(title = "确认时间")
    @ApiModelProperty(value = "确认时间")
    private String confirmTime;

    @ApiModelProperty(value = "确认人")
    private String confirmBy;

    @ColumnHeader(title = "推送状态")
    @ApiModelProperty(value = "推送状态")
    private Integer pushStatus;

    /** 推送状态名称显示**/
    @DictName(code = "PUSH_STATUS", joinField = "pushStatus")
    @ApiModelProperty(value = "推送状态名称")
    private String pushStatusDisplay;

    @ColumnHeader(title = "推送时间")
    @ApiModelProperty(value = "推送时间")
    private String pushTime;

    @ColumnHeader(title = "动态数据转发状态")
    @ApiModelProperty(value = "动态数据转发状态")
    private Integer forwardStatus;

    /** 动态数据转发状态名称显示**/
    @DictName(code = "FORWARD_STATUS", joinField = "forwardStatus")
    @ApiModelProperty(value = "动态数据转发状态名称")
    private String forwardStatusDisplay;

    @ColumnHeader(title = "首次转发时间")
    @ApiModelProperty(value = "首次转发时间")
    private String forwardFirstTime;

    @ColumnHeader(title = "推送错误信息")
    @ApiModelProperty(value = "推送错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "车辆ID")
    @NotEmpty(message = "车辆ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆ID")
    private String vehicleId;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    @ApiModelProperty(value = "VIN")
    private String vin;
    @ApiModelProperty(value = "车辆阶段")
    private String stage;
    /** 车辆阶段名称显示**/
    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")
    @ApiModelProperty(value = "车辆阶段名称")
    private String stageDisplay;

    @ApiModelProperty(value = "车辆阶段变更时间")
    private String stageChangeDate;
    @ApiModelProperty(value = "车辆型号")
    private String vehModelId;
    /** 车辆型号名称显示**/
    /*@LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId",desc = "")*/
    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelDisplay;

    @ApiModelProperty(value = "通讯协议")
    private String ruleId;
    /** 通讯协议名称显示**/
    /*@LinkName(table = "dc_rule", column = "name", joinField = "ruleId",desc = "")*/
    @ApiModelProperty(value = "通讯协议名称")
    private String ruleDisplay;

    @ApiModelProperty(value = "协议类型")
    private String ruleTypeId;
    /** 协议类型名称显示**/
    /*@LinkName(table = "dc_rule_type", column = "name", joinField = "ruleTypeId",desc = "")*/
    @ApiModelProperty(value = "协议类型名称")
    private String ruleTypeDisplay;

    @ApiModelProperty(value = "制造工厂")
    private String manuUnitId;

    /** 制造工厂名称**/
    @LinkName(table = "sys_unit", column = "name", joinField = "manuUnitId",desc = "")
    @ApiModelProperty(value = "制造工厂名称")
    private String manuUnitDisplay;

    /** 车辆型号 - 车辆厂商id **/
    @ApiModelProperty(value = "车辆厂商id")
    private String vehUnitId;

    /** 车辆型号 - 车辆厂商名称 **/
    @ApiModelProperty(value = "车辆厂商名称")
    @LinkName(table = "sys_unit", joinField = "vehUnitId")
    private String vehUnitName;

    /** 车辆型号 - 车辆厂商统一社会信用码 **/
    @ApiModelProperty(value = "车辆厂商统一社会信用码")
    @LinkName(table = "sys_unit", column = "organization_code", joinField = "vehUnitId")
    private String vehUnitCode;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;

    /** 运营单位名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId",desc = "")
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitDisplay;

    @ApiModelProperty(value = "在线状态")
    private String onlineStatus;
    /** 在线状态名称显示**/
    @DictName(code = "ONLINE_STATUS", joinField = "onlineStatus")
    @ApiModelProperty(value = "在线状态名称")
    private String onlineStatusDisplay;

    @ApiModelProperty(value = "静态推送平台")
    private String staticForwardPlatform;
    /** 静态推送平台名称显示**/
    @DictName(code = "PUSH_PLATFORM_TYPE", joinField = "staticForwardPlatform")
    @ApiModelProperty(value = "静态推送平台名称")
    private String staticPlatformDisplay;

    @ApiModelProperty(value = "内部编号")
    private String interNo;
    @ApiModelProperty(value = "ICCID")
    private String iccid;


    @ApiModelProperty(value = "转发成功车辆")
    private Integer forwardSucVeh;
    @ApiModelProperty(value = "待确认车辆")
    private Integer unconfirmedVeh;
    @ApiModelProperty(value = "已确认车辆")
    private Integer confirmedVeh;
    @ApiModelProperty(value = "待推送车辆")
    private Integer toPushVeh;
    @ApiModelProperty(value = "推送失败车辆")
    private Integer pushFailVeh;
    @ApiModelProperty(value = "待转发车辆")
    private Integer toForwardVeh;
    @ApiModelProperty(value = "转发失败车辆")
    private Integer forwardFailVeh;

    @ApiModelProperty(value = "结果信息")
    private String resultMsg;

    @ApiModelProperty(value = "转发车辆IDs")
    private String ids;
    @ApiModelProperty(value = "成功车辆IDs")
    private String sucIds;
    @ApiModelProperty(value = "重复确认车辆IDs")
    private String repeatIds;
    @ApiModelProperty(value = "重复确认车辆数")
    private Integer repeatVehNum;

    @ApiModelProperty(value = "添加方式")
    private Integer addMode;

    @NotEmpty(message = "添加方式不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "添加方式")
    @DictName(code = "ADD_MODE", joinField = "addMode")
    private String addModeName;

    @ApiModelProperty(value = "车辆种类")
    private String typeName;

    /**车辆品牌**/
    @ApiModelProperty(value = "车辆品牌")
    private String brandName;

    /**车型系列**/
    @ApiModelProperty(value = "车型系列")
    private String seriesName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardVehicleModel fromEntry(ForwardVehicle entry){
        ForwardVehicleModel m = new ForwardVehicleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
