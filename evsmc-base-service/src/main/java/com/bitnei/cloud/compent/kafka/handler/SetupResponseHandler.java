package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.compent.kafka.service.KafkaSender;
import com.bitnei.cloud.sys.model.TermParamRecordModel;
import com.bitnei.cloud.sys.service.ITermParamRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SetupResponseHandler extends AbstractKafkaHandler {

    private static final String KAFKA_TOPIC = "ds_ctrlreq";         //全量查询topic
    private static final String KAFKA_GETARG_KEY = "GETARG";       //全量查询key
    private static final String KAFKA_GETARG_MSG = "SUBMIT 1 %s GETARG {VID:%s,VTYPE:%s,5001:%s,5002:%s}";       //全量查询参数模板
    private static final String SETARG_ANSWER_TIME = "5301";        //设置应答消息应答时间

    private final KafkaSender kafkaSender;
    private final ITermParamRecordService iTermParamRecordService;

    @Override
    public boolean handle(String message) {

        PlatformPojo platformPojo = PlatformPojo.parse(message);

        //UPDATESTATUS为升级状态上报信息
        if (platformPojo.getCmd().equals("SETARG")) {
            setArg(platformPojo);
        }

        return false;
    }

    /**
     * 设置应答消息处理
     * REPORT 1 12312312312312312 SETARG {VID:4028cd8160f8fb0e0160f91629d70016,VTYPE:228,RET:0,5301:20180125151256}
     *
     * @param platformPojo
     */
    private void setArg(PlatformPojo platformPojo) {
        log.info("处理设置命令返回Start");
        //返回设置应答成功，发起全量请求
        if (Integer.parseInt(platformPojo.getData().get("RET")) == 0) {
            String codes = iTermParamRecordService.unique("findParameterCodes", null);
            if (StringUtils.isNotBlank(codes)) {
                String msg = String.format(KAFKA_GETARG_MSG, platformPojo.getVin(), platformPojo.getData().get("VID"),
                        platformPojo.getData().get("VTYPE"), platformPojo.getData().get(SETARG_ANSWER_TIME), codes);
                kafkaSender.send(KAFKA_TOPIC, KAFKA_GETARG_KEY, msg);
                log.info("发送全量查询命令成功");
            } else {
                log.info("设置应答失败");
            }
        } else {
            TermParamRecordModel model = iTermParamRecordService.findByVin(platformPojo.getVin());
            //如果设置失败了，提示设置失败结果，不再下发查询
            model.setErrorMessage("设置应答失败");
            iTermParamRecordService.update(model);
        }
        log.info("处理设置命令返回End");
    }
}
