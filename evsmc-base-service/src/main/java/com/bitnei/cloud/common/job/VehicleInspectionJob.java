package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.VehicleOperModel;
import com.bitnei.cloud.sys.service.IVehicleOperService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ElasticJobConf(name = "VehicleInspectionJob", cron = "0 0 1 * * ?", shardingItemParameters = "0=0,1=1",
        description = "车辆运营年检状态任务", eventTraceRdbDataSource = "druidDataSource")
@Component
public class VehicleInspectionJob implements SimpleJob {

    @Autowired
    private IVehicleOperService vehicleOperService;

    @Override
    public void execute(ShardingContext shardingContext) {
        // 车辆运营年检状态任务
        if (!JobCheckUtil.getJobEnableStatus(getClass())) {
            return;
        }

        PagerInfo pagerInfo = new PagerInfo();

        Condition condition = new Condition();
        condition.setName("annualInspectionDateEnd");
        Date oneYearBefore = DateUtil.addYear(new Date(), -1);
        condition.setValue(DateUtil.formatTime(oneYearBefore, DateUtil.DAY_FORMAT));
        pagerInfo.setConditions(Collections.singletonList(condition));

        int i = 0;
        int partSize = 1000;

        List<VehicleOperModel> vehicleOperModels = new ArrayList<>();

        do {
            pagerInfo.setStart(partSize * i++);
            pagerInfo.setLimit(partSize);

            PagerResult result = (PagerResult) vehicleOperService.list(pagerInfo);
            if (CollectionUtils.isNotEmpty(result.getData())) {
                vehicleOperModels = (List<VehicleOperModel>) result.getData().get(0);
            }

            if (CollectionUtils.isNotEmpty(vehicleOperModels)) {
                List<String> vins = vehicleOperModels.stream().map(VehicleOperModel::getVin)
                        .collect(Collectors.toList());
                vehicleOperService.updateInspectStatusOutOfPeriodByVins(vins);
            }
        } while (CollectionUtils.isNotEmpty(vehicleOperModels));
    }
}
