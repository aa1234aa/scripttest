package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/11 17:14
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class RoleUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/role";
    String all_url = "/" + Version.VERSION_V1 + "/sys/roles";
    String update_url = "/" + Version.VERSION_V1 + "/sys/roles/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/roles/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/roles/{id}";
}
