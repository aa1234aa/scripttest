package com.bitnei.hzdb.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Order(value = 2)
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ThreadPoolExecutor kafkaExecutor;

    private Gson gson = new GsonBuilder().create();

    public void send(String topic, Object data) {
        if (data instanceof String) {
            kafkaTemplate.send(topic, data.toString());
        } else if (null != data) {
            kafkaTemplate.send(topic, gson.toJson(data));
        }
    }

    public void send(String topic, String key, String value) {
        ProducerRecord producerRecord = new ProducerRecord<String, String>(topic, key, value);
        kafkaTemplate.send(producerRecord);
    }

    public void sendSync(String topic, String key, String value) {
        ProducerRecord producerRecord = new ProducerRecord<String, String>(topic, key, value);
        kafkaExecutor.execute(() -> {
            kafkaTemplate.send(producerRecord);
        });
    }
}