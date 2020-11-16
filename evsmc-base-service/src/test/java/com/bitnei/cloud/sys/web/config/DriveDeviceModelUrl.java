package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 14:32
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class DriveDeviceModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/driveDevice";
    String all_url = "/" + Version.VERSION_V1 + "/sys/driveDevices";
    String update_url ="/" + Version.VERSION_V1 + "/sys/driveDevices/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/driveDevices/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/driveDevices/{id}";

}
