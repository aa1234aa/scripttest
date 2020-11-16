package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lijiezhou on 2018/12/20. <br>
 * 修改model  zxz 2019-03-27 14:28:50
 * @author zxz
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleSubsidyModel", description = "车辆补贴列表Model")
public class VehicleSubsidyModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "VIN", example = "LA9G3MBD8JSWXB075", desc = "VIN")
    @NotEmpty(message = "车架号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "车辆生产单位id")
    private String manuUnitId;

    @ApiModelProperty(value = "车辆生产单位名称")
    @LinkName(table = "sys_unit", joinField = "manuUnitId")
    private String manuUnitName;

    @ColumnHeader(title = "车牌号", example = "皖G13131", desc = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车辆用途")
    private Integer operUseFor;

    @ApiModelProperty(value = "车辆用途名称")
    @DictName(code = "VEH_USE_FOR",joinField = "operUseFor")
    private String operUseForName;


    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "上牌城市名称")
    @LinkName(table = "sys_area", joinField = "operLicenseCityId")
    private String operLicenseCityName;

    @ApiModelProperty(value = "车辆种类")
    private String vehTypeName;


    @ApiModelProperty(value = "车辆型号id")
    private String vehModelId;


    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;


    @NotNull(message = "申报状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "申报状态")
    private Integer subsidyApplyStatus;

    /** 申报状态**/
    @ColumnHeader(title = "申报状态", example = "未申报", desc = "申报状态选项:未申报 待审核 待拨款 已拨款 待整改")
    @ApiModelProperty(value = "申报状态名称")
    @DictName(code = "SUBSIDY_APPLY_STATUS",joinField = "subsidyApplyStatus")
    @NotEmpty(message = "申报状态不能为空", groups = {GroupExcelImport.class})
    private String subsidyApplyStatusName;

    @ColumnHeader(title = "申报次数", example = "3", desc = "申报次数范围为0-99")
    @DecimalMax(value = "99", message = "申报次数范围为0-99", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "申报次数")
    private Integer susidyApplyCount;


    /** 车辆运行统计信息 */
    @ApiModelProperty(value = "累计行驶里程(km)")
    private String accumulatedMileage;

    @ApiModelProperty(value = "纯电续驶里程(km)")
    private String electricalMileage;

    @ApiModelProperty(value = "充电倍率")
    private String chargingRate;

    @ApiModelProperty(value = "燃料消耗量")
    private String fuelConsumption;

    @ApiModelProperty(value = "车辆发票")
    private String sellInvoiceNo;

    @ApiModelProperty(value = "行驶证注册日期")
    private String sellLicenseRegDate;

    @ApiModelProperty(value = "首次上线时间")
    private String firstTimeOnLine;

    /** 可充电储能装置信息 */
    private List<VehicleEngeryDeviceModel> engerys = Lists.newArrayList();

    /** 驱动电机信息 */
    private List<VehicleDriveMotorModel> motors = Lists.newArrayList();

    /** 燃料电池信息 */
    private List<VehicleFuelModel> fuels = Lists.newArrayList();

    @ApiModelProperty(value = "累计行驶里程(km)")
    private Double lastEndMileage;

    /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehicleSubsidyModel fromEntry(Vehicle entry){
        VehicleSubsidyModel m = new VehicleSubsidyModel();
//        DecimalFormat d = new DecimalFormat("#######.##");
        BeanUtils.copyProperties(entry, m);
//        m.setFirstTimeOnLine(entry.get("firstTimeOnLine") == null ? null : entry.get("firstTimeOnLine").toString());
//        m.setAccumulatedMileage(entry.get("lastEndMileage") == null ? null : d.format(entry.get("lastEndMileage")));
        return m;
    }

    /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehicleSubsidyModel fromEntry(Vehicle entry, List<VehicleEngeryDeviceLkModel> engeryList,
          List<VehicleDriveDeviceLkModel> deviceList, List<VehiclePowerDeviceLkModel> powerList){
        VehicleSubsidyModel m = new VehicleSubsidyModel();
//        DecimalFormat d = new DecimalFormat("#######.##");
        BeanUtils.copyProperties(entry, m);
//        m.setFirstTimeOnLine(entry.get("firstTimeOnLine") == null ? null : entry.get("firstTimeOnLine").toString());
//        m.setAccumulatedMileage(entry.get("lastEndMileage") == null ? null : d.format(entry.get("lastEndMileage")));
        if(engeryList != null && !engeryList.isEmpty()) {
            for (VehicleEngeryDeviceLkModel lk : engeryList) {
                DataLoader.loadNames(lk);
                VehicleEngeryDeviceModel model = new VehicleEngeryDeviceModel();
                model.setBatteryCellModel(lk.getBatteryCellModel());
                model.setModelType(lk.getModelType());
                model.setModelName(lk.getModelName());
                model.setBatteryCellUnitName(lk.getBatteryCellUnitName());
                if(Constant.EnergyDeviceType.BATTERY.equals(lk.getModelType())) {
                    model.setModelName(lk.getBatteryModelName());
                    model.setTotalElecCapacity(lk.getTotalElecCapacity());
                    model.setCapacityDensity(lk.getCapacityDensity());
                    model.setUnitName(lk.getUnitName());
                } else if(Constant.EnergyDeviceType.CAPACITY.equals(lk.getModelType())) {
                    model.setModelName(lk.getCapacityModelName());
                    model.setTotalElecCapacity(lk.getTotalCapacity());
                    model.setCapacityDensity(lk.getScmCapacityDensity());
                    model.setUnitName(lk.getScmUnitName());
                }
                model.setBatteryTypeName(lk.getBatteryTypeName());
                model.setInvoiceNo(lk.getInvoiceNo());
                model.setModelTypeName(lk.getModelTypeDisplay());
                m.getEngerys().add(model);
            }
        } else {
            m.getEngerys().add(new VehicleEngeryDeviceModel());
        }
        if(deviceList != null && !deviceList.isEmpty()) {
            for (VehicleDriveDeviceLkModel lk : deviceList) {
                DataLoader.loadNames(lk);
                VehicleDriveMotorModel model = new VehicleDriveMotorModel();
                model.setDriveMotorModelName(lk.getDriveMotorModelName());
                model.setInvoiceNo(lk.getInvoiceNo());
                model.setProdUnitName(lk.getProdUnitName());
                model.setRatedPower(lk.getRatedPower());
                m.getMotors().add(model);
            }
        } else {
            m.getMotors().add(new VehicleDriveMotorModel());
        }
        if(powerList != null && !powerList.isEmpty()) {
            for (VehiclePowerDeviceLkModel lk : powerList) {
                DataLoader.loadNames(lk);
                VehicleFuelModel model = new VehicleFuelModel();
                model.setModelName(lk.getFuelBatteryModelName());
                model.setInvoiceNo(lk.getInvoiceNo());
                model.setProdUnitName(lk.getProdUnitName());
                model.setRatedPower(lk.getRatedPower());
                m.getFuels().add(model);
            }
        } else {
            m.getFuels().add(new VehicleFuelModel());
        }
        return m;
    }

}

/**
 *  可充电储能装置信息
 */
@Data
class VehicleEngeryDeviceModel {

    @ApiModelProperty(value = "单体型号")
    private String batteryCellModel;
    @ApiModelProperty(value = "单体生产企业名称")
    private String batteryCellUnitName;
    @ApiModelProperty(value = "成箱型号")
    private String modelName;
    @ApiModelProperty(value = "系统生产企业名称")
    private String unitName;
    @ApiModelProperty(value = "可充电储能装置种类")
    private Integer modelType;
    @ApiModelProperty(value = "可充电储能装置种类名称")
    private String modelTypeName;
    /** 电池类型(1 三元材料电池,2 磷酸铁锂电池,3 钴酸锂电池,4 锰酸锂电池, 5 钛酸锂电池,6 其它类型电池) **/
    @ApiModelProperty(value = "电池类型名称")
    private String  batteryTypeName;
    @ApiModelProperty(value = "系统总能量(kWh)")
    private Double totalElecCapacity;
    @ApiModelProperty(value = "系统能量密度（Wh/kg)")
    private Double capacityDensity;
    /** 发票号 **/
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;
}

/**
 * 车辆驱动电机信息
 */
@Data
class VehicleDriveMotorModel {

    @ApiModelProperty(value = "型号名称")
    private String driveMotorModelName;
    @ApiModelProperty(value = "额定功率(KW)")
    private Double ratedPower;
    @ApiModelProperty(value = "系统生产企业名称")
    private String prodUnitName;
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

}

/**
 * 燃料电池信息
 */
@Data
class VehicleFuelModel {

    @ApiModelProperty(value = "型号名称")
    private String modelName;
    @ApiModelProperty(value = "额定功率(KW)")
    private Double ratedPower;
    @ApiModelProperty(value = "燃料电池系统生产企业名称")
    private String prodUnitName;
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

}
