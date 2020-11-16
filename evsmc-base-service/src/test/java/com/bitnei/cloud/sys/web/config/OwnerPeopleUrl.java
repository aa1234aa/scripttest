package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/5 19:23
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class OwnerPeopleUrl {

    String add_url ="/"+ Version.VERSION_V1+"/sys/ownerPeople";
    String all_url = "/"+ Version.VERSION_V1+"/sys/ownerPeoples";
    String update_url = "/"+ Version.VERSION_V1+"/sys/ownerPeoples/{id}";
    String detail_url =  "/"+ Version.VERSION_V1+"/sys/ownerPeoples/{id}";
    String delete_url =  "/"+ Version.VERSION_V1+"/sys/ownerPeoples/{id}";

}
