package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 10:04
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class FuelGeneratorModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/fuelGeneratorModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/fuelGeneratorModels";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/fuelGeneratorModels/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/fuelGeneratorModels/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/fuelGeneratorModels/{id}";

}
