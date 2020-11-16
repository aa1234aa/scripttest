package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import com.bitnei.cloud.fault.service.IAlarmInfoMoveService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
 * ----------------------------------------------------------------------------- <br>
 * 修改历史 <br>
 * <table width="432" border="1">
 * <tr>
 * <td>版本</td>
 * <td>时间</td>
 * <td>作者</td>
 * <td>改变</td>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2019-07-11</td>
 * <td>huangweimin</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author huangweimin
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@ElasticJobConf(name = "StopFaultAlarmJob", cron = "0 0/10 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "定时结束报警数据进入历史报警(大数据存储)", eventTraceRdbDataSource = "druidDataSource")
@Component
public class StopFaultAlarmJob implements SimpleJob {

    @Autowired
    private IAlarmInfoMoveService alarmInfoMoveService;


    @Override
    public void execute(ShardingContext shardingContext) {

        //判断任务是否启动
        if (!JobCheckUtil.getJobEnableStatus(getClass())) {
            return;
        }

        try {
            log.info("开始定时任务: 定时结束报警数据进入历史报警(大数据存储)");
            alarmInfoMoveService.move();
        } catch (Exception e) {
            log.error("定时结束报警数据进入历史报警(大数据存储)任务异常!", e);
        }
    }
}
