package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/13 15:36
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehNoticeUrl {

    String add_url = "/" + Version.VERSION_V1 + "/sys/vehNotice";
    String all_url =  "/" + Version.VERSION_V1 + "/sys/vehNotices";
    String update_url =  "/" + Version.VERSION_V1 + "/sys/vehNotices/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehNotices/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehNotices/{id}";

}
