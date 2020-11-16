package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 10:44
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class PowerDeviceUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/powerDevice";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/powerDevices";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/powerDevices/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/powerDevices/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/powerDevices/{id}";
}
