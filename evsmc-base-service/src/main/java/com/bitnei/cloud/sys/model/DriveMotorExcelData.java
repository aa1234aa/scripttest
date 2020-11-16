package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 国家平台EXCEL驱动电机型号model
 * @author zxz
 */
@Setter
@Getter
@NoArgsConstructor
public class DriveMotorExcelData {

    /** 驱动电机型号 */
    private String name;

    /** 车辆配置名称 Y */
    private String configurationNum;

    /** 安装位置(1:前置 2:后置 3:轮边 4:轮毂内) */
    private String installPosition;

    /** 驱动电机冷却方式 */
    private String driveColdModeDisplay;

    /** 额定电压(V) */
    private Double ratedVoltage;

    /** 峰值功率(KW) */
    private Double peakPower;

    /** 驱动电机最高转速(r/min) */
    private Double maxRotateSpeed;

    /** 驱动电机峰值转矩(N.m) */
    private Double peakTorque;

    /** 驱动电机最大工作电流(A) */
    private Double maxElectricity;

    /** 驱动电机最大输出转矩(N.m) */
    private Double maxOutputTorque;

    /** 驱动电机编号 **/
    private String sequenceNumber;

    /** 驱动电机序号 **/
    private String sequenceSerial;

    /** VIN **/
    private String vin;

    public DriveMotorExcelData(String vin, String name, String sequenceNumber, String installPosition, String sequenceSerial) {
        this.vin = vin;
        this.name = name;
        this.installPosition = installPosition;
        this.sequenceNumber = sequenceNumber;
        this.sequenceSerial = sequenceSerial;
    }

    /**
     * 将实体转为前台model
     * @param d DriveMotorModelModel
     * @param configurationNum 车辆配置名称
     * @return DriveMotorExcelData
     */
    public static DriveMotorExcelData fromEntry(DriveMotorModelModel d, String configurationNum, String installPosition){
        DriveMotorExcelData m = new DriveMotorExcelData();
        BeanUtils.copyProperties(d, m);
        m.setConfigurationNum(configurationNum);
        m.setInstallPosition(installPosition);
        return m;
    }

}
