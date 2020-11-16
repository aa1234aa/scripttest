package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lijiezhou on 2019/3/8.
 */
@Setter
@Getter
public class RemindInfoModel {

    //离线时长
    private String offLineTime;
    //里程差值
    private String mileagePoor;
    //唯一用户名
    private String userName;
}
