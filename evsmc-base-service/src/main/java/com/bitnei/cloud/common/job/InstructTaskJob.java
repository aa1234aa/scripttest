package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.sys.service.impl.InstructTaskService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ElasticJobConf(name = "InstructTaskJobTwo", cron = "0 */1 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "远升远控定时任务", eventTraceRdbDataSource = "druidDataSource")
@Component
public class InstructTaskJob implements SimpleJob {

    @Autowired
    private InstructTaskService instructTaskService;

    @Override
    public void execute(ShardingContext shardingContext) {
        // log.info("=========================远升、远控定时任务启动=========================");
        // 开始远升、远控任务
        if (!JobCheckUtil.getJobEnableStatus(getClass())){
            return;
        }
        instructTaskService.beginInstructTask();
        //  log.info("=========================远升、远控定时任务结束=========================");
    }

}
