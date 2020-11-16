package com.bitnei.cloud.sms.model;

import com.bitnei.cloud.common.bean.BaseModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/21
 */

@Data
public class SmsTemplateModel extends BaseModel {

    private String id;

    private String templateName;

    private String interCode;

    private String templateId;

    private String content;

    private Integer status;

    private String statusName;

    private String msgParam;

    private List<FieldModel> fieldModels = new ArrayList<>();

    public SmsTemplateModel(String templateName, String templateId, String content, String msgParam) {
        this.templateName = templateName;
        this.templateId = templateId;
        this.content = content;
        this.msgParam = msgParam;
    }
}
