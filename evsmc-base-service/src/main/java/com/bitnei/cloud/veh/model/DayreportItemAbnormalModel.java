package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.util.StringUtils;
import com.bitnei.cloud.veh.domain.DayreportItemAbnormal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportItemAbnormal新增模型<br>
* 描述： DayreportItemAbnormal新增模型<br>
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
* <td>2019-09-20 09:28:41</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DayreportItemAbnormalModel", description = "车辆异常数据项报表Model")
public class DayreportItemAbnormalModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "车辆uuid")
    private String vid;

    @ApiModelProperty(value = "车辆vin")
    private String vin;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆厂商")
    private String vehUnitId;

    @LinkName(table = "sys_unit", column = "name", joinField = "vehUnitId", desc = "车辆厂商名称")
    @ApiModelProperty(value = "车辆厂商名称")
    private String vehUnitName;

    @ApiModelProperty(value = "车辆运营性质")
    private Integer operUseType;

    @ApiModelProperty(value = "运营个人车主")
    private String operVehOwnerName;

    @ApiModelProperty(value = "运营单位")
    private String operUnitName;

    @ApiModelProperty(value = "报表日期")
    private String reportDate;

    @ApiModelProperty(value = "车辆状态异常条数")
    private Integer vehStatusNum;

    @ApiModelProperty(value = "充电状态异常条数")
    private Integer chargeStatusNum;

    @ApiModelProperty(value = "速度异常条数")
    private Integer speedNum;

    @ApiModelProperty(value = "累计里程异常条数")
    private Integer mileageNum;

    @ApiModelProperty(value = "总电压异常条数")
    private Integer totalVoltageNum;

    @ApiModelProperty(value = "总电流异常条数")
    private Integer totalCurrentNum;

    @ApiModelProperty(value = "soc异常条数")
    private Integer socNum;

    @ApiModelProperty(value = "驱动电机异常条数")
    private Integer driverMotorNum;

    @ApiModelProperty(value = "燃料电池异常条数")
    private Integer fuelCellNum;

    @ApiModelProperty(value = "发动机异常条数")
    private Integer engineNum;

    @ApiModelProperty(value = "车辆位置数据异常条数")
    private Integer locationNum;

    @ApiModelProperty(value = "极值数据异常条数")
    private Integer extremeNum;

    @ApiModelProperty(value = "报警数据异常条数")
    private Integer alarmNum;

    @ApiModelProperty(value = "最后通讯时间")
    private String lastCommitTime;

    /** 运营单位/个人 **/
    public String getOperUnitOrOwner() {
        if(Vehicle.VEH_USE_DICT_ENUM.UNIT_OPER.getValue().equals(operUseType)) {
            return this.operUnitName;
        } else if(Vehicle.VEH_USE_DICT_ENUM.PERSONAGE_OPER.getValue().equals(operUseType)) {
            return this.operVehOwnerName;
        }
        return "";
    }

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportItemAbnormalModel fromEntry(DayreportItemAbnormal entry){
        DayreportItemAbnormalModel m = new DayreportItemAbnormalModel();
        m.setVehModelName(StringUtils.ts(entry.get("vehModelName")));
        m.setVehUnitId(StringUtils.ts(entry.get("vehUnitId")));
        m.setVehModelName(StringUtils.ts(entry.get("vehModelName")));
        String operUseType = StringUtils.ts(entry.get("operUseType"));
        if(StringUtils.isNotEmpty(operUseType)) {
            m.setOperUseType(Integer.parseInt(operUseType));
        }
        m.setOperVehOwnerName(StringUtils.ts(entry.get("operVehOwnerName")));
        m.setOperUnitName(StringUtils.ts(entry.get("operUnitName")));
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
