package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 15:22
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class BatteryDeviceModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/batteryDeviceModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/batteryDeviceModels";
    String update_url ="/" + Version.VERSION_V1 + "/sys/batteryDeviceModels/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/batteryDeviceModels/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/batteryDeviceModels/{id}";

}
