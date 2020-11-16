package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.sms.service.ITermSmsTaskService;
import com.bitnei.cloud.sms.service.impl.SmsTaskItemService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Desc： 终端指令批量下发短信信息回调
 * @Author: joinly
 * @Date: 2019/8/29
 */
@Slf4j
@ElasticJobConf(name = "TermSmsCallBackJob", cron = "0 0/1 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "终端指令批量下发短信信息回调", eventTraceRdbDataSource = "druidDataSource")
@Component
public class TermSmsCallBackJob implements SimpleJob {

    @Autowired
    private ITermSmsTaskService termSmsTaskService;

    @Value("${use.aliyun.term.sms:false}")
    private boolean useAliyunTermSms;

    @Override
    public void execute(ShardingContext context) {
        try {
            //判断任务是否启动
            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                log.error("配置文件未设置启动项…");
                return;
            }
            if (useAliyunTermSms) {
                termSmsTaskService.processAddAllMsgCallBack();
            } else {
                termSmsTaskService.processUniComSmsCallBack();
            }
        } catch (Exception e) {
            log.error("TermSmsCallBackJob短信信息回调接口出错：", e);
        }
    }
}
