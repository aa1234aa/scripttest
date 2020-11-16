package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.sys.model.InstructSendCanModel;
import com.bitnei.cloud.sys.service.IInstructSendCanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCanSendResultHandler extends AbstractKafkaHandler {

    private final IInstructSendCanService iInstructSendCanService;
    private final ControlResponseHandler controlResponseHandler;

    @Override
    public boolean handle(String message) {

        PlatformPojo platformPojo = PlatformPojo.parse(message);

        //SETCAN更新CAN数据的发送结果状态
        if (platformPojo.getCmd().equals("SETCAN")) {
            setCan(platformPojo);
        }

        return true;
    }

    /**
     * 更新CAN数据的发送结果状态
     * REPORT 0 VIN SETCAN {VID:vid,VTYPE:vtype,SESSIONLEN:sessionlen,SESSION:session,RET:ret,TIME:time}
     * (ret取值0:应答成功；1:应答失败,time：应答时间)
     *
     * @param platformPojo
     */
    private void setCan(PlatformPojo platformPojo) {

        //转成国标控制指令的处理
        controlResponseHandler.control(platformPojo);

//        log.info("更新CAN数据的发送结果状态Start");
//        //根据会话id查询指令下发CAN数据
//        String sessionId = platformPojo.getData().get("SESSION");
//        log.info("sessionId值:" + sessionId);
//        List<InstructSendCanModel> instructSendCanList = iInstructSendCanService.findBySqlId(
//                "findBySessionId", sessionId);
//        log.info("instructSendCanList大小值:" + instructSendCanList.size() + ", 对象字符串值:" +
//                JSONObject.fromObject(instructSendCanList.get(0)).toString());
//        if (!instructSendCanList.isEmpty()) {
//            int sendResult = Integer.parseInt(platformPojo.getData().get("RET"));
//            for (InstructSendCanModel instructSendCan : instructSendCanList) {
//                //发送结果0：成功、1：失败
//                instructSendCan.setSendResult(sendResult);
//                iInstructSendCanService.update(instructSendCan);
//            }
//        }
//        log.info("更新CAN数据的发送结果状态End");
    }
}
