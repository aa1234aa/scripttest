package com.bitnei.cloud.sys.web.config;

/*
@author 黄永雄
@create 2019/12/12 14:10
*/


import com.bitnei.cloud.common.constant.Version;
import lombok.Data;

@Data
public class DriveMotorModelUrl {

    String add_url ="/" + Version.VERSION_V1 + "/sys/driveMotorModel";
    String all_url = "/" + Version.VERSION_V1 + "/sys/driveMotorModels";
    String update_url ="/" + Version.VERSION_V1 + "/sys/driveMotorModels/{id}";
    String detail_url = "/" + Version.VERSION_V1 + "/sys/driveMotorModels/{id}";
    String delete_url = "/" + Version.VERSION_V1 + "/sys/driveMotorModels/{id}";

}
