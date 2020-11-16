package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.common.util.ApplicationContextProvider;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.sys.model.RemindInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 鹏 on 2015/10/20.
 */
@Component
public class RedisUtil {

    @Resource(name = "webRedisKit")
    private RedisKit redisKit;

    /**
     * 从redis中获取远程升级包的流水号
     *
     * @return
     */
    public String getFlowNumber() {

        String FlowNumber = redisKit.get(4, "longrange_upgrade_flownumber", "");
        if (StringUtils.isEmpty(FlowNumber)) {
            redisKit.set(4,"longrange_upgrade_flownumber", "1");
            FlowNumber = "1";
        } else {
            if (Integer.parseInt(FlowNumber) < 65535) {
                int flowNubers = Integer.parseInt(FlowNumber) + 1;
                FlowNumber = Integer.toString(flowNubers);
                redisKit.set(4,"longrange_upgrade_flownumber", FlowNumber);
            } else {
                redisKit.set(4,"longrange_upgrade_flownumber", "1");
                FlowNumber = "1";
            }
        }

        return FlowNumber;
    }
    public String getFlowNumberJinLong() {

        String FlowNumber = redisKit.get(4, "longrange_upgrade_flownumber_jinlong", "");
        if (StringUtils.isEmpty(FlowNumber)) {
            redisKit.set(4,"longrange_upgrade_flownumber_jinlong", "1");
            FlowNumber = "1";
        } else {
            if (Integer.parseInt(FlowNumber) < 90) {
                int flowNubers = Integer.parseInt(FlowNumber) + 1;
                FlowNumber = Integer.toString(flowNubers);
                redisKit.set(4,"longrange_upgrade_flownumber_jinlong", FlowNumber);
            } else {
                redisKit.set(4,"longrange_upgrade_flownumber_jinlong", "1");
                FlowNumber = "1";
            }
        }

        return FlowNumber;
    }
    /**
     * 从redis中获取车辆提醒信息
     *
     * @return
     */
    public static RemindInfoModel getReminderTime() {

        RedisKit redisKit = (RedisKit) ApplicationContextProvider.getBean("webRedisKit");
        String mileage = redisKit.get(4, "xny_inidle_mileage_web", "");
        String time = redisKit.get(4, "xny_inidle_time_web", "");
        RemindInfoModel remindInfoEntity = new RemindInfoModel();
        remindInfoEntity.setMileagePoor(mileage);
        remindInfoEntity.setOffLineTime(time);
        return remindInfoEntity;
    }

    /**
     * 把离线车辆提醒放入redis
     *
     * @return
     */
    public static void saveReminderTime(String time, String mileage, String whetherFrame) {

        RedisKit redisKit = (RedisKit) ApplicationContextProvider.getBean("webRedisKit");
        redisKit.set(4, "xny_inidle_whetherFrame_web", whetherFrame);
        redisKit.set(4, "xny_inidle_time_web", time);
    }

    /**
     * 获取当前提醒状态
     *
     * @return
     */
    public static String nowReminderStatus() {

        RedisKit redisKit = (RedisKit) ApplicationContextProvider.getBean("webRedisKit");
        return redisKit.get(4, "xny_inidle_whetherFrame_web", "");
    }

    /**
     * 获取离线阈值
     *
     * @return
     */
    public static String timeOutTime() {

        RedisKit redisKit = (RedisKit) ApplicationContextProvider.getBean("webRedisKit");
        return redisKit.get(4, "gt.inidle.timeOut.time", "");
    }

    /**
     * 设置离线时间阈值
     *
     * @return
     */
    public static void saveTimeOutTime(String time) {

        RedisKit redisKit = (RedisKit) ApplicationContextProvider.getBean("webRedisKit");
        redisKit.set(4, "gt.inidle.timeOut.time", time);
    }

    /**
     * 从redis中获取远程锁车的流水号
     *
     * @return
     */
    public String getFlowingWater() {

        String FlowNumber = "";
        FlowNumber = (String) redisKit.get(4,"lock_car_flow_number", "");
        if (StringUtils.isEmpty(FlowNumber)) {
            redisKit.set(4, "lock_car_flow_number", "1");
            FlowNumber = "1";
        } else {
            if (Integer.parseInt(FlowNumber) < 65535) {
                int flowNubers = Integer.parseInt(FlowNumber) + 1;
                FlowNumber = Integer.toString(flowNubers);
                redisKit.set(4, "lock_car_flow_number", FlowNumber);
            } else {
                redisKit.set(4, "lock_car_flow_number", "1");
                FlowNumber = "1";
            }
        }

        return FlowNumber;
    }
}
