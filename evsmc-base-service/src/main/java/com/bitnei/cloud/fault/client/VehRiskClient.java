package com.bitnei.cloud.fault.client;

import com.bitnei.cloud.fault.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("evsmc-openservice")
public interface VehRiskClient {

    /**
     * 事故现场处置预案信息接口
     * @param model
     * @return
     */
    @PostMapping({"api/v1/evsmc/truUpAlarm"})
    ResultMsgModel truUpAlarm(@RequestBody IncidentModel model);

    /**
     * 车辆安全事故信息接口
     * @param model
     * @return
     */
    @PostMapping({"api/v1/evsmc/truUpDeal"})
    ResultMsgModel truUpDeal(@RequestBody TruUpDealModel model);

    /**
     * 上报车辆安全事故分析报告/监管数据
     * @param model
     * @return
     */
    @PostMapping({"api/v1/evsmc/truUpAlarmDocs"})
    ResultMsgModel truUpAlarmDocs(@RequestBody TruUpAlarmDocModel model);

    /**
     * 批量阅读
     * @param lists
     * @return
     */
    @PostMapping({"api/v1/evsmc/safeUpSate"})
    ResultMsgModel safeUpSate(@RequestBody List<SafeUpSateModel> lists);

    /**
     * 批量回复
     * @param lists
     * @return
     */
    @PostMapping({"api/v1/evsmc/safeUpSate"})
    ResultMsgModel safeUpReplySate(@RequestBody List<SafeUpReplySateModel> lists);

}
