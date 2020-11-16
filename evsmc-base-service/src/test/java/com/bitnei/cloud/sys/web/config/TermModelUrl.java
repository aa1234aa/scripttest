package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 9:24
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class TermModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/termModel";
    String all_url ="/" + Version.VERSION_V1 + "/sys/termModels";
    String update_url ="/" + Version.VERSION_V1 + "/sys/termModels/{id}";
    String detail_url ="/" + Version.VERSION_V1 + "/sys/termModels/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/termModels/{id}";

}
