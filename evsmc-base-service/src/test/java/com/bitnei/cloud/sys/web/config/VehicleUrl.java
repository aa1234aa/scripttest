package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 17:26
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehicleUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehicle";
    String all_url = "/" + Version.VERSION_V1 + "/sys/vehicles";
    String update_url = "/" + Version.VERSION_V1 + "/sys/vehicles/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/vehicles/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/vehicles/{id}";

}
