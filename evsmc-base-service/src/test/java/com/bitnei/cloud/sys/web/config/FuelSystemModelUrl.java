package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/1 11:39
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class FuelSystemModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/fuelSystemModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/fuelSystemModels";
    String update_url = "/" + Version.VERSION_V1 + "/sys/fuelSystemModels/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/fuelSystemModels/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/fuelSystemModels/{id}";

}
