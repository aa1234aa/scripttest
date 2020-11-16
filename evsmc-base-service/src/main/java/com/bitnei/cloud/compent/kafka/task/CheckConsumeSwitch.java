package com.bitnei.cloud.compent.kafka.task;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckConsumeSwitch implements CommandLineRunner, Ordered {

    private final Environment env;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    /**
     * 每隔两秒判断kafka消费开关
     */
    @Scheduled(fixedRate = 2000)
    public void checkConsumeSwitch() {

        Boolean kafkaSwitch = env.getProperty("kafka.switch", Boolean.class, true);

        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer(env.getProperty("spring.kafka.consumer.group-id"));

        if (kafkaSwitch) {
            if (!listenerContainer.isRunning()) {
                listenerContainer.start();
            }
        } else {
            if (listenerContainer.isRunning()) {
                listenerContainer.stop();
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        checkConsumeSwitch();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
