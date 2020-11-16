package com.bitnei.cloud.sys.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VehFeedbackModel {

    /** 批次号 */
    private String bnum;

    /** 信息数据集 */
    private List<VehFeedbackMessageModel> messages = Lists.newArrayList();

}
