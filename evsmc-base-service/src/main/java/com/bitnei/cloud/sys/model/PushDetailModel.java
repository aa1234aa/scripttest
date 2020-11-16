package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 车辆推送详情model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class PushDetailModel {

    private String id;
    /** 状态 1:成功 2:错误 */
    private Integer status;
    /** 关联id(车辆id|车型id|运营单位id) */
    private String formId;
    /** 关联名称(车型名称|车辆vin|运营单位名称) */
    private String formName;
    /** 消息 */
    private String message;

}

