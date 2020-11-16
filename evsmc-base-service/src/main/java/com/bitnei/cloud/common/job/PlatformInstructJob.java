package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@ElasticJobConf(name = "PlatformInstructJob", cron = "0 0/5 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "定时更新平台转发车辆", eventTraceRdbDataSource = "druidDataSource")
@Slf4j
@Component
public class PlatformInstructJob implements SimpleJob {

    @Autowired
    private IForwardVehicleService forwardVehicleService;

    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                return;
            }
            log.info("==============定时更新平台转发车辆开始================");
            forwardVehicleService.updateForwardVehicle();
            log.info("==============定时更新平台转发车辆结束================");
        } catch (Exception e){
            log.error("定时更新平台转发车辆出错：", e);
        }
    }
}
