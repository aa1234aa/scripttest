package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 环保国家平台API响应Model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class EcoRespModel {

    private String type;
    private String message;
    private int code;
    private Map<String,String> data;
    /** 请求时间 **/
    private String reqTime;
    /** 响应时间 **/
    private String responseTime;
    /** 请求内容 **/
    private String reqBody;
    /** 响应内容 **/
    private String rspBody;
    /** 转发平台id **/
    private String platformId;
    /** 转发车辆表id **/
    private String forwardVehicleId;
    /** 响应类型, 1:发动机 2:芯片型号 3:车载终端 4:车型信息 5:车辆信息 **/
    private int respType;
    /** 车辆id **/
    private String vehicleId;
    /** 确认人 **/
    private String confirmBy;
    /** 备注 **/
    private String remark;
    /** fromId **/
    private String fromId;




}
