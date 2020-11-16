package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 9:46
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class TermModelUnitUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/termModelUnit";
    String all_url ="/" + Version.VERSION_V1 + "/sys/termModelUnits";
    String update_url ="/" + Version.VERSION_V1 + "/sys/termModelUnits/{id}";
    String detail_url ="/" + Version.VERSION_V1 + "/sys/termModelUnits/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/termModelUnits/{id}";

}
