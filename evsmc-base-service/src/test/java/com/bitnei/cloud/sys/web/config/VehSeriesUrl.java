package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 14:30
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehSeriesUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehSeries";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/vehSeriess";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/vehSeriess/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehSeriess/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehSeriess/{id}";

}
