package com.bitnei.cloud.fault.kafka;

import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 故障报警通知分流处理
 *
 * @author xzp
 */
@Slf4j
@Service
public class FaultAlarmVehicleHandler extends AbstractKafkaHandler {

    private final @NotNull FaultParameterAlarmHandler faultParameterAlarmHandler;

    private final @NotNull FaultCodeAlarmHandler faultCodeAlarmHandler;

    private final @NotNull FaultFenceAlarmHandler faultFenceAlarmHandler;

    public FaultAlarmVehicleHandler(
        final @NotNull FaultParameterAlarmHandler faultParameterAlarmHandler,
        final @NotNull FaultCodeAlarmHandler faultCodeAlarmHandler,
        final @NotNull FaultFenceAlarmHandler faultFenceAlarmHandler) {

        this.faultParameterAlarmHandler = faultParameterAlarmHandler;
        this.faultCodeAlarmHandler = faultCodeAlarmHandler;
        this.faultFenceAlarmHandler = faultFenceAlarmHandler;
    }


    @Override
    public boolean handle(final String message) {

        if (StringUtils.isBlank(message)) {
            return true;
        }

        try {
            final Gson gson = new Gson();
            final HashMap<String, Object> map = gson.fromJson(
                message,
                new TypeToken<HashMap<String, Object>>() {

                }.getType()
            );
            final String msgType = map
                .getOrDefault(
                    "msgType",
                    ""
                )
                .toString()
                .trim();
            switch (msgType) {
                case "FAULT_PARAMETER_ALARM":
                    faultParameterAlarmHandler.handle(message);
                    break;
                case "FAULT_CODE_ALARM":
                    faultCodeAlarmHandler.handle(message);
                    break;
                case "FAULT_FENCE_ALARM":
                    faultFenceAlarmHandler.handle(message);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(
                "error",
                e
            );
        }

        return true;
    }
}
