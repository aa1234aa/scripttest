package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sms.domain.SmsTaskItemDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/26
 */

@Data
public class SmsTaskItemDetailModel extends BaseModel {

    private String id;

    private String taskId;

    private Integer serviceType;

    private Integer receiverType;

    private String receiverId;

    private String receiver;

    private String vehicleId;

    private Integer sendStatus;

    @DictName(code = "SMS_SEND_STATUS", joinField = "sendStatus")
    private String sendStatusDisplay;

    private String failMsg;

    private String msisd;

    /** 发送时间 **/
    private String sendTime;

    /**短信模板**/
    private String templateName;

    /**短信内容**/
    private String smsContent;

    /**===============车辆信息==========================**/
    /** 车架号 **/
    private String vin;


    private String licensePlate;

    private Integer operUseType;

    @DictName(code = "VEH_USE_DICT", joinField = "operUseType")
    private String operUserTypeName;

    private String operUnitId;

    /** 运营单位名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "operUnitId",desc = "")
    private String operUnitDisplay;

    private String vehModelId;

    /** 车辆型号名称显示**/
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId", desc = "")
    private String vehModelDisplay;

    /**
     * 报警开始时间
     **/
    private String faultBeginTime;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static SmsTaskItemDetailModel fromEntry(SmsTaskItemDetail entry){
        SmsTaskItemDetailModel m = new SmsTaskItemDetailModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
