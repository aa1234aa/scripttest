package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 14:12
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehBrandUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehBrand";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/vehBrands";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/vehBrands/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehBrands/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehBrands/{id}";

}
