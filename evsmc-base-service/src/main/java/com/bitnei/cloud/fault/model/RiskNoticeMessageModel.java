package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 车辆风险wsModel
 * @author zhouxianzhou
 */
@Setter
@Getter
public class RiskNoticeMessageModel {

    /** 车辆风险id fault_veh_risk_notice表id **/
    private String id;

    /** 弹窗标题 **/
    private String title;

    /** 弹窗内容 **/
    private String content;


    public RiskNoticeMessageModel(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
