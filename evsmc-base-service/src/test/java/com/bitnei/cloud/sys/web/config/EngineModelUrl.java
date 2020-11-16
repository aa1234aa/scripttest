package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 13:18
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class EngineModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/engineModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/engineModels";
    String update_url = "/" + Version.VERSION_V1 + "/sys/engineModels/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/engineModels/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/engineModels/{id}";

}
