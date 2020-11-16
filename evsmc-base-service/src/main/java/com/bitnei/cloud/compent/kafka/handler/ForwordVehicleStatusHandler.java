package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.util.KafkaMessageUtil;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 车辆转发状态 <br>
 * 描述： 消息格式 string notice = "TYPE:9,TIME:" + Utils::GetTime() + ",VID:" + vid + ",FORWARD_ID:" + ite->id;<br>
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
 * <td>2019-04-02</td>
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
public class ForwordVehicleStatusHandler extends AbstractKafkaHandler{

    @Autowired
    private IForwardVehicleService forwardVehicleService;

    @Override
    public boolean handle(String data) {

        Map<String, String> map = KafkaMessageUtil.parserMsg(data);
        if (map == null || map.size() == 0){
            return false;
        }
        String vid = map.get("VID");
        String forwardId = map.get("FORWARD_ID");
        String time = DateUtil.converseStr(map.get("TIME"));
        String type = map.get("TYPE");

        if ("9".equals(type)){
            log.info("车辆转发状态通知：" + data);
            forwardVehicleService.changeForwardStatus(vid, forwardId, time);
        }
        return true;
    }
}
