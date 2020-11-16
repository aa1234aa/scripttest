package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 16:11
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehModelUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehModel";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/vehModels";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/vehModels/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehModels/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehModels/{id}";

}
