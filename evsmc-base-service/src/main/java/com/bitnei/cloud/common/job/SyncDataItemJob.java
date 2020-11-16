package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.dc.service.IRuleItemLkService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desc： 同步协议类型下的属性到协议中
 * @Author: joinly
 * @Date: 2019/7/9
 */

@Slf4j
@ElasticJobConf(name = "SyncDataItemJob", cron = "0 0/2 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "同步协议类型中的数据属性", eventTraceRdbDataSource = "druidDataSource")
@Component
public class SyncDataItemJob implements SimpleJob {

    @Autowired
    private IRuleItemLkService ruleItemLkService;

    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                return;
            }
            ruleItemLkService.syncRuleTypeDataItemToRule();
        } catch (Exception e) {
            log.error("同步协议类型中的数据属性出错: ", e);
        }
    }
}
