package com.bitnei.cloud.sys.domain;

import lombok.Data;

/**
 * @author chf
 * @Date 2019/4/4
 */
@Data
public class UserPersonalSetting {

   /** id **/
    private String id;
    /** 用户id **/
    private String userId;
    /** 用户登录保持时长 **/
    private String keepOnLineTime;
    /** 登录类别 (0是首次登录 1是90天未改密码 )**/
    private Integer loginType;
    /** 上次登录时间 **/
    private String lastLoginTime;
    /** 上次改密码时间 **/
    private String lastChangePasswordTime;
    /** 是否允许重复登录 (1是允许 0是不予许)**/
    private Integer isRepeatLoginAllow;
    /** 是否启用账号回收 (1是启用 0是不启用)**/
    private Integer isAccountDeprecatedActive;
    /** 强制改密码是否启用 (1是启用 0是不启用)**/
    private Integer isForceChangePasswordActive;

}
