package com.bitnei.cloud.sys.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 国家平台EXCEL储能装置电池包（箱）型号Model
 * @author zxz
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EngeryExcelData {

    /** 储能装置电池包(箱)型号 */
    private String modelName;

    /** 车辆配置名称 Y */
    private String configurationNum;

    /** 储能装置电池包(箱)数量 */
    private Integer batteryPackageCount;

    /** VIN **/
    private String vin;

    /** 储能装置电池包(箱)编码 */
    private String engeryDeviceName;

    /** 安装位置 **/
    private String installPostion;

    public EngeryExcelData(String modelName, String configurationNum, Integer batteryPackageCount) {
        this.modelName = modelName;
        this.configurationNum = configurationNum;
        this.batteryPackageCount = batteryPackageCount;
    }

    public EngeryExcelData(String vin, String modelName, String engeryDeviceName, String installPostion) {
        this.modelName = modelName;
        this.vin = vin;
        this.engeryDeviceName = engeryDeviceName;
        this.installPostion = installPostion;
    }
}
