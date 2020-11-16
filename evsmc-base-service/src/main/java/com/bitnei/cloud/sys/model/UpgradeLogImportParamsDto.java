package com.bitnei.cloud.sys.model;

import lombok.Data;

@Data
public class UpgradeLogImportParamsDto {

    private String vin;

    private String licensePlate;

    private String interNo;

    private String iccid;
}
