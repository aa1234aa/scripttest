package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.dc.model.PlatformEventModel;
import com.bitnei.cloud.dc.service.IPlatformEventService;
import com.bitnei.cloud.sys.util.DateUtil;
import com.google.gson.Gson;
import jodd.json.JsonParser;
import jodd.util.Base64;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bitnei.cloud.compent.kafka.util.KafkaMessageUtil.parserMsg;

/**
 * Created by Lijiezhou on 2019/3/19.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformEventHandler extends AbstractKafkaHandler {

    @Autowired
    private IPlatformEventService iPlatformEventService;

    @Override
    public boolean handle(String data) {
        try {
            Map<String, String> message = parserMsg(data);
            if (message == null){
                log.error("离线报警消息反序化失败，原始消息体:" + data);
                return false;
            }
            Integer type = Integer.parseInt(message.get("TYPE"));
            if (type == 3 || type == 4 ){
                String time = DateUtil.converseStr(message.get("TIME"));
                String platId = message.get("PLATID");
                String seqId = message.get("SEQID");
                Integer status = Integer.parseInt(message.getOrDefault("STATUS", "0"));
                //平台登入
                if (type == 3) {
                    String username = Base64.decodeToString(message.getOrDefault("USERNAME", ""));
                    String password = Base64.decodeToString(message.getOrDefault("PASSWORD", ""));
                    String description = String.format("%s;%s", username, password);
                    PlatformEventModel platformEventModel = new PlatformEventModel();
                    platformEventModel.setType(0);
                    platformEventModel.setTime(time);
                    platformEventModel.setPid(platId);
                    platformEventModel.setSeqid(seqId);
                    platformEventModel.setStatus(status);
                    platformEventModel.setDescription(description);
                    iPlatformEventService.connect(platformEventModel);
                } else if (type == 4) {
                    //平台登出分两种情况，当流水号为空时，当作超时处理
                    if (StringUtil.isEmpty(seqId)) {
                        PlatformEventModel platformEventModel = new PlatformEventModel();
                        platformEventModel.setPid(platId);
                        platformEventModel.setTime(time);
                        platformEventModel.setType(2);
                        iPlatformEventService.timeout(platformEventModel);
                    } else {
                        PlatformEventModel platformEventModel = new PlatformEventModel();
                        platformEventModel.setPid(platId);
                        platformEventModel.setTime(time);
                        platformEventModel.setSeqid(seqId);
                        platformEventModel.setStatus(status);
                        platformEventModel.setType(1);
                        iPlatformEventService.disConnect(platformEventModel);
                    }
                }
            }
        }catch (Exception e) {
            log.error("error", e);
        }
        return true;
    }

}

