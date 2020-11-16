package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultMsgModel {

    /**
     0	    成功
     1	    失败
     1001	token不合法或无权限
     1002	参数缺失
     1003	令牌为空
     */
    private int code = 0;
    /** 消息 **/
    private String msg;
    /** 数据 **/
    private Object data;


}
