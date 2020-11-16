package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 15:01
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class SuperCapacitorModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/superCapacitorModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/superCapacitorModels";
    String update_url ="/" + Version.VERSION_V1 + "/sys/superCapacitorModels/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/superCapacitorModels/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/superCapacitorModels/{id}";

}
