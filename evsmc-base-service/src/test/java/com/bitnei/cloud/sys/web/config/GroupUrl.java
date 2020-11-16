package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/5 20:17
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class GroupUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/group";
    String all_url = "/" + Version.VERSION_V1 + "/sys/groups";
    String update_url = "/" + Version.VERSION_V1 + "/sys/groups/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/groups/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/groups/{id}";

}
