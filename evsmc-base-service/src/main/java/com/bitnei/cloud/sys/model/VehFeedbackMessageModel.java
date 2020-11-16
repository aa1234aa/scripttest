package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

/**
* 车辆上报信息反馈message model
*/
@Setter
@Getter
public class VehFeedbackMessageModel {

    /** 各上报接口主键信息 <br>
     车型名称、车辆vin、运营单位名称
     */
    private String id;

    /** 响应码成功，失败 */
    private String code;

    /** 错误描述 */
    private String message;

    /** 平台预留1*/
    private String topId;

    /** 平台预留2*/
    private String ids;

    /** 平台预留3 */
    private String codes;

    /** 平台预留4 */
    private String messages;
}
