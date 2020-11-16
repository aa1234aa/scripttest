package com.bitnei.cloud.sms.domain;

import lombok.Data;

/**
 * @Desc： 终端指令短信导出
 * @Author: joinly
 * @Date: 2019/9/17
 */

@Data
public class TermSmsTaskExport {
    private String templateName;
    private String vehModelName;
    private String updateBy;
    private String createTime;
    private String licensePlate;
    private String vin;
    private String msisd;
    private Integer sendStatus;
    private String failMsg;
}
