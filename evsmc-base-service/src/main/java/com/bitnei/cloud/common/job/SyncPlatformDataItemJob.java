package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.dc.service.IPlatformDataLkService;
import com.bitnei.cloud.dc.service.IRuleItemLkService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desc： 同步平台的转发数据项
 * @Author: joinly
 * @Date: 2019/7/15
 */

@Slf4j
@ElasticJobConf(name = "SyncPlatformDataItemJob", cron = "0 0/1 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "同步平台的转发数据项", eventTraceRdbDataSource = "druidDataSource")
@Component
public class SyncPlatformDataItemJob implements SimpleJob {

    @Autowired
    private IPlatformDataLkService platformDataLkService;

    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                return;
            }
            platformDataLkService.syncPlatformDataItem();
        } catch (Exception e) {
            log.error("同步协议类型中的数据属性出错: ", e);
        }
    }
}
