package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sms.domain.TermSmsTaskExport;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/9/17
 */

@Data
public class TermSmsTaskExportModel extends BaseModel {

    private String templateName;
    private String vehModelName;
    private String updateBy;
    private String createTime;
    private String licensePlate;
    private String vin;
    private String msisd;
    private Integer sendStatus;

    @DictName(joinField = "sendStatus", code = "SMS_SEND_STATUS")
    private String sendStatusDisplay;

    private String failMsg;

    public static TermSmsTaskExportModel fromEntry(TermSmsTaskExport entry){
        TermSmsTaskExportModel m = new TermSmsTaskExportModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
