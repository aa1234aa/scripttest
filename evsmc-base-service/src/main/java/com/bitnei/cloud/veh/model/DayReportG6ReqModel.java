package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.client.model.DayReportG6Req;
import lombok.Data;

/**
 * @author Lijiezhou
 */
@Data
public class DayReportG6ReqModel extends DayReportG6Req {

    private Integer start;
    private Integer limit;
}
