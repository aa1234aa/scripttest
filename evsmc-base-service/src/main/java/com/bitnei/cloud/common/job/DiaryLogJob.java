package com.bitnei.cloud.common.job;


import com.bitnei.cloud.common.manager.DailyManager;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： data-access-service <br>
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
 * <td>2019-04-24</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Component
public class DiaryLogJob {

    @Autowired
    private DailyManager dailyManager;


    @Scheduled(fixedRate = 1000 * 30)
    public void heart() {
        dailyManager.heartBeat();
    }
}
