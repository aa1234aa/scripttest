package com.bitnei.cloud.common.manager;

import com.bitnei.cloud.diary.Alarm;
import com.bitnei.cloud.diary.Diary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
 * <td>2019-04-23</td>
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
@Component
@Slf4j
public class DailyManager {

    /** 健康状态 Map**/
    private List<Alarm> alarms = new ArrayList<>();


    @Value("${spring.application.name}")
    private String appId;


    @PostConstruct
    public void registerApp(){
        try {
            Diary.app(appId, "基础服务接口", "1.0", "chenpeng", "1");
//            //Cto Redis实时状态
//            alarms.add(new Alarm(CTO, "Redis集群状态", "0", "ok"));
//            //明细数据查询状态
//            alarms.add(new Alarm(HISTORY, "历史明细数据", "0", "ok"));
//            //报文数据
//            alarms.add(new Alarm(PROTOCOL, "历史明细数据", "0", "ok"));

            Diary.health(appId, alarms.toArray(new Alarm[alarms.size()]));
        } catch (IOException e) {
            log.error("error", e);
        }
    }



    /**
     * 写入健康状态
     * @param alarmId   报警ID
     * @param status    状态 0：正常 1：报警
     * @param detail    详细信息
     */
    public synchronized void writeAlarm(final String alarmId,  final String status, final String detail){

        for (Alarm alarm: alarms){
            //设置报警状态
            if (alarm.getId().equals(alarmId)){
                alarm.setStatus(status);
                alarm.setDetail(detail);
            }
        }

        try {
            Diary.health(appId, alarms.toArray(new Alarm[alarms.size()]));
        } catch (IOException e) {
            log.error("error", e);
        }

    }

    /**
     * 心跳包
     */
    public void heartBeat(){
        try {
            Diary.health(appId, alarms.toArray(new Alarm[alarms.size()]));
        } catch (IOException e) {
            log.error("error", e);
        }
    }

}
