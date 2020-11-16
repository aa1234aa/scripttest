package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/5 16:53
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class UnitUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/unit";
    String all_url = "/" + Version.VERSION_V1 + "/sys/units";
    String update_url = "/" + Version.VERSION_V1 + "/sys/units/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/units/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/units/{id}";

}
