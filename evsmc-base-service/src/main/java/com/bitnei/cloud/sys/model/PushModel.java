package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆推送model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class PushModel {

    /** 批次号 */
    private String batchNum;
    /** 批次类型 */
    private Integer batchType;

    /** 转发请求内容 **/
    private String reqBody;
    /** 解密请求内容 **/
    private String decryptReqBody;
    /** 响应原始内容 **/
    private String rspBody;
    /** 解密响应内容 **/
    private String decryptRspBody;
    /** 推送时间 **/
    private String reqTime;
    /** 响应时间 **/
    private String rspTime;
    /** 推送状态 **/
    private Integer pushStatus;
    /** 错误原因 **/
    private String errorMessage;
    /** 转发平台id **/
    private String platformId;
    /** 车辆id **/
    private String vehicleId;
    /** 备注 **/
    private String remark;
    /** 确认账号 **/
    private String confirmBy;
    /** 推送厂商社会统一信用码 **/
    private String mfrsCode;



    List<PushDetailModel> list = new ArrayList<>();


}
