package com.bitnei.cloud.common.job;

import com.bitnei.cloud.common.util.JobCheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import lombok.extern.slf4j.Slf4j;
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
 * <td>2019-04-10</td>
 * <td>chenpeng</td>
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
@ElasticJobConf(name = "SyncNotifierVehicleJob", cron = "0 0/10 * * * ?", shardingItemParameters = "0=0,1=1",
        description = "同步故障负责人关联账号下的车辆", eventTraceRdbDataSource = "druidDataSource")
@Slf4j
@Component
public class SyncNotifierVehicleJob implements SimpleJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${sync.notifier.vehicle.switch}")
    private boolean syncSwitch;

    @Autowired
    private INotifierSettingService notifierSettingService;

    @Override
    public void execute(ShardingContext context) {
        try {

            if (!JobCheckUtil.getJobEnableStatus(getClass())){
                return;
            }

            logger.info("SyncNotifierVehicleJob开始同步故障负责人关联账号下的车辆, syncSwitch开关:" + syncSwitch);
            if (syncSwitch) {
                notifierSettingService.syncNotifierVehicle();
            }
        } catch (Exception e) {
            logger.error("SyncNotifierVehicleJob同步故障负责人关联账号下的车辆出错：", e);
        }
    }

}
