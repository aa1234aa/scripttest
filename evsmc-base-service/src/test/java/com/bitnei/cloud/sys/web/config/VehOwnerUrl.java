package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/1 12:45
*/

import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class VehOwnerUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/vehOwner";
    String all_url = "/" + Version.VERSION_V1 + "/sys/vehOwners";
    String update_url = "/" + Version.VERSION_V1 + "/sys/vehOwners/{id}";
    String detail_url =  "/" + Version.VERSION_V1 + "/sys/vehOwners/{id}";
    String delete_url =  "/" + Version.VERSION_V1 + "/sys/vehOwners/{id}";
    
}
