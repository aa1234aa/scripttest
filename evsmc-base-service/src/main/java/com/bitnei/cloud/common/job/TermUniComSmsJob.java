package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.sms.service.ITermSmsTaskService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desc： 终端指令批量下发短信信息回调
 * @Author: joinly
 * @Date: 2019/8/29
 */
@Slf4j
@ElasticJobConf(name = "TermUniComSmsJob", cron = "0 0/1 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "终端指令批量下发短信信息回调(联通)", eventTraceRdbDataSource = "druidDataSource")
@Component
public class TermUniComSmsJob implements SimpleJob {

    @Autowired
    private ITermSmsTaskService termSmsTaskService;

    @Override
    public void execute(ShardingContext context) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                log.error("配置文件未设置启动项…");
                return;
            }
            termSmsTaskService.sendUniComSms();
        } catch (Exception e) {
            log.error("TermUniComSmsJob发送联通短信到终端接口出错：", e);
        }
    }

}
