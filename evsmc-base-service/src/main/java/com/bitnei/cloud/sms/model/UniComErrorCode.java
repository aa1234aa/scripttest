package com.bitnei.cloud.sms.model;

/**
 * @Desc： 联通错误代码
 * @Author: joinly
 * @Date: 2019/9/11
 */
public enum UniComErrorCode {

    INVALID_ICCID("20000001", "找不到指定ICCID"),
    INVALID_MESSAGE("20000002","找不到指定短消息ID"),
    SERVICE_ERROR("30000001", "未知服务器错误");

    private String code;
    private String desc;

    UniComErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getRuleEnum(String code){
        if (INVALID_ICCID.code.equals(code)){
            return INVALID_ICCID.desc;
        } else if (INVALID_MESSAGE.code.equals(code)){
            return INVALID_MESSAGE.desc;
        } else if (SERVICE_ERROR.code.equals(code)){
            return SERVICE_ERROR.desc;
        }
        return INVALID_ICCID.desc;
    }
}
