package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IInstructSendRuleService;
import com.bitnei.cloud.sys.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TerminalLockResponseHandler extends AbstractKafkaHandler {

    private final IInstructSendRuleService iInstructSendRuleService;
    private final IVehicleService vehicleService;

    @Override
    public boolean handle(String message) {
        PlatformPojo platformPojo = PlatformPojo.parse(message);
        //TERMLOCK为终端锁车应答
        if (platformPojo.getCmd().equals("TERMLOCK")) {
            termLockAnswer(platformPojo);
        }
        return true;
    }

    /**
     * 终端锁车应答
     *
     * @param platformPojo
     */
    private void termLockAnswer(PlatformPojo platformPojo) {
        log.info("终端锁车应答" + platformPojo.getVin());
        Map<String, String> map = platformPojo.getData();
        log.info("终端锁车参数：" + JSONArray.fromObject(map).toString());
        String vid = map.getOrDefault("VID", "").toString().trim();
        VehicleModel vehicle = vehicleService.getByUuid(vid);
        log.info("终端锁车车牌号：" + vehicle.getLicensePlate());

        String sessionId = map.getOrDefault("4710046", "").trim();
        log.info("终端锁车应答流水号：" + sessionId);
        map.put("sessionId", sessionId);
        map.put("vin", vehicle.getVin());
        log.info("终端锁车操作数据库" + platformPojo.getVin());
        iInstructSendRuleService.termLockAnswer(map);
    }
}
