package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 16:57
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class EngeryDeviceModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/engeryDevice";
    String all_url = "/" + Version.VERSION_V1 + "/sys/engeryDevices";
    String update_url ="/" + Version.VERSION_V1 + "/sys/engeryDevices/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/engeryDevices/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/engeryDevices/{id}";

}
