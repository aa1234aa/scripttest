package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 13:14
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehTypeUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehType";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/vehTypes";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/vehTypes/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehTypes/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehTypes/{id}";

}
