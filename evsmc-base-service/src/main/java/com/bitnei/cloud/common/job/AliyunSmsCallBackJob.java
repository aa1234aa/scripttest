package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.sms.service.ISmsTaskService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Desc： 阿里云短信回调
 * @Author: joinly
 * @Date: 2019/9/20
 */
@Slf4j
@ElasticJobConf(name = "AliyunSmsCallBackJob", cron = "0 0/2 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "阿里云短信回调", eventTraceRdbDataSource = "druidDataSource")
@Component
public class AliyunSmsCallBackJob implements SimpleJob {

    @Autowired
    private ISmsTaskService smsTaskService;

    @Value("${use.aliyun.sms:false}")
    private boolean useAliyunSms;

    @Override
    public void execute(ShardingContext context) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())) {
                log.error("配置文件未设置启动项…");
                return;
            }
            if (useAliyunSms) {
                smsTaskService.smsCallBackJob();
            }
        } catch (Exception e) {
            log.error("AliyunSmsCallBackJob短信信息回调接口出错：", e);
        }
    }
}
