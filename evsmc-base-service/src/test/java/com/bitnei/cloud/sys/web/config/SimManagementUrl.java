package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 17:44
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class SimManagementUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/simManagement";
    String all_url = "/" + Version.VERSION_V1 + "/sys/simManagements";
    String register_url = "/" + Version.VERSION_V1 + "/sys/simManagements/register/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/simManagements/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/simManagements/{id}";

}
