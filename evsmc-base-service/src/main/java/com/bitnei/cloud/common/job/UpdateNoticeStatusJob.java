package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.fault.service.IVehRiskNoticeService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lijiezhou
 */
@Slf4j
@ElasticJobConf(name = "UpdateNoticeStatusJob", cron = "0 0/10 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "定时更新车辆安全风险消息状态", eventTraceRdbDataSource = "druidDataSource")
@Component
public class UpdateNoticeStatusJob  implements SimpleJob {

    @Resource
    private IVehRiskNoticeService vehRiskNoticeService;
    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                return;
            }
            vehRiskNoticeService.updateNoticeStatus();
        } catch (Exception e) {
            log.error("更新未读超时和未回复状态时出错: ", e);
        }
    }
}
