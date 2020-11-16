package com.bitnei.cloud.compent.kafka.service;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.compent.kafka.config.TopicHandlersConfig;
import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xiky
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaHandlerService {

    private final Map<String, AbstractKafkaHandler> handlerMap;
    private final TopicHandlersConfig topicHandlersConfig;
    private Map<String, List<AbstractKafkaHandler>> topicHandlers = new HashMap<>();
    private final ConsumerFactory consumerFactory;
    private Map<String, ThreadPoolExecutor> executorMap = new HashMap<>();
    private final Environment env;

    @PostConstruct
    @SneakyThrows
    public void initKafkaHandler() {
        //初始化topic映射handlers的map
        Map<String, String> consumersConfig = topicHandlersConfig.getConsumers();
        Map<String, AbstractKafkaHandler> handlerWithPageName = new HashMap<>();
        List<String> topics = new ArrayList<>();

        for (Map.Entry handler : handlerMap.entrySet()) {
            handlerWithPageName.put(handler.getValue().getClass().getName(), (AbstractKafkaHandler) handler.getValue());
        }
        for (Map.Entry consumer : consumersConfig.entrySet()) {
            String topic = consumer.getKey().toString();
            topics.add(topic);
            List<String> handlerStrs = Arrays.stream(consumer.getValue().toString().split("-"))
                    .map(String::trim).filter(it -> !StringUtils.isEmpty(it)).collect(Collectors.toList());
            List<AbstractKafkaHandler> handlers = handlerStrs.stream().map(handlerWithPageName::get)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            topicHandlers.put(topic, handlers);

            //优化，创建topic对应的线程池，每个topic独立的线程池处理，避免线程池堵塞导致业务干扰
            executorMap.put(topic, new ThreadPoolExecutor(
                    env.getProperty("kafka.excutor.corePoolSize", Integer.class, 10),
                    env.getProperty("kafka.excutor.maxPoolSize", Integer.class, 20),
                    env.getProperty("kafka.excutor.keepAliveSeconds", Long.class, 30000L),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new LocalThreadFactory.NameThreadFactory("kafka-" + topic)));
        }

        //反射修改topics的值
        Method listen = KafkaHandlerService.class.getDeclaredMethod("listen", ConsumerRecord.class);
        KafkaListener kafkaListener = listen.getAnnotation(KafkaListener.class);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(kafkaListener);
        Field hField = invocationHandler.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map) hField.get(invocationHandler);
        String[] topicArray = topics.toArray(new String[0]);
        memberValues.put("topics", topicArray);
    }

    /**
     * 禁止kafka自启动
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(consumerFactory);
        //禁止自动启动
        container.setAutoStartup(false);
        return container;
    }

    /**
     * 单线程方式
     *
     * @param record
     */
    @KafkaListener(id = "${spring.kafka.consumer.group-id}", containerFactory = "delayContainerFactory")
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {

            String message = kafkaMessage.get().toString();
            List<AbstractKafkaHandler> handlers = topicHandlers.get(record.topic());
            if (CollectionUtils.isEmpty(handlers)) {
                log.warn("no handlers for kafka topic:{}", record.topic());
                return;
            }
            //线程池去处理每个topic的handler;
            //每个topic对应一个线程池和n个handler
            executorMap.get(record.topic()).submit(() -> handlers.forEach(it -> {
                //每个handler做异常处理，防止影响别的handler处理
                try {
                    it.handle(message);
                } catch (BusinessException e) {
                    log.error("topic:{}, handler:{}, business error, code:{}, message:{}", record.topic(), it,
                            e.getCode(), e.getMessage());
                    log.error("error message data: {}", message);
                    log.error("error", e);
                } catch (Exception e) {
                    if (CollectionUtils.isNotEmpty(handlers)) {
                        log.error("topic:{}, handler:{}, consume unknown error", record.topic(), it);
                        log.error("error message data: {}", message);
                    }
                    log.error("error", e);
                }
            }));
        }
    }

}
