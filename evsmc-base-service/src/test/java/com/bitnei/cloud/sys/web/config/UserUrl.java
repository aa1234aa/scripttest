package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 9:20
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class UserUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/user";
    String all_url = "/" + Version.VERSION_V1 + "/sys/users";
    String update_url = "/" + Version.VERSION_V1 + "/sys/users/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/users/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/users/{id}";


}
