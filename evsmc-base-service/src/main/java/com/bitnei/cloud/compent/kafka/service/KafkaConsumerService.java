package com.bitnei.cloud.compent.kafka.service;

import com.bitnei.cloud.compent.kafka.domain.KafkaMsgDto;
import com.bitnei.cloud.compent.kafka.domain.KafkaSearchDto;
import com.bitnei.cloud.compent.kafka.handler.AbstractKafkaHandler;
import com.bitnei.cloud.sys.util.DateUtil;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final Environment env;

    /**
     * 线程数
     */
    private static final int THREAD_AMOUNT = 1;

    public void createKafkaConsumers(Map<String, List<AbstractKafkaHandler>> topicHandlers) {

        Properties props = buildProperties();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        for (Map.Entry map : topicHandlers.entrySet()) {
            topicCountMap.put(map.getKey().toString(), THREAD_AMOUNT);
        }

        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, List<KafkaStream<byte[], byte[]>>> msgStreams = consumer.createMessageStreams(topicCountMap);
        for (Map.Entry map : topicHandlers.entrySet()) {
            List<KafkaStream<byte[], byte[]>> msgStreamList = msgStreams.get(map.getKey().toString());

            //使用ExecutorService来调度线程
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_AMOUNT);
            for (int i = 0; i < msgStreamList.size(); i++) {
                KafkaStream<byte[], byte[]> kafkaStream = msgStreamList.get(i);
                executor.submit(new HandldMessageThread(kafkaStream, i, (List<AbstractKafkaHandler>) map.getValue()));
            }
        }
    }

    @SneakyThrows
    public List<KafkaMsgDto> createSearchConsumers(KafkaSearchDto kafkaSearchDto) {

        String topic = kafkaSearchDto.getTopic();
        String beginTime = kafkaSearchDto.getBeginTime();
        String endTime = kafkaSearchDto.getEndTime();

        KafkaConsumer consumer = new KafkaConsumer(buildNormalProperties());
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
        Map<TopicPartition, Long> timestampsToSearchEnd = new HashMap<>();

        Long rangeTimeBg = DateUtil.strToDate(beginTime, DateUtil.FULL_ST_FORMAT).getTime();
        Long rangeTimeEd = DateUtil.strToDate(endTime, DateUtil.FULL_ST_FORMAT).getTime();

        for (PartitionInfo it: partitionInfos) {
            TopicPartition topicPartition = new TopicPartition(topic, it.partition());
            timestampsToSearch.put(topicPartition, rangeTimeBg);
            timestampsToSearchEnd.put(topicPartition, rangeTimeEd);
        }
        Map<TopicPartition, OffsetAndTimestamp> offsetsMap =
                consumer.offsetsForTimes(timestampsToSearch, Duration.ofHours(1L));

        Map<TopicPartition, OffsetAndTimestamp> offsetsMapEnd =
                consumer.offsetsForTimes(timestampsToSearchEnd, Duration.ofHours(1L));
        Map<Integer, Long> partitionAndEndOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry: offsetsMapEnd.entrySet()) {
            if (null != entry.getValue()) {
                partitionAndEndOffset.put(entry.getKey().partition(), entry.getValue().offset());
            }
        }

        List<KafkaMsgDto> searchMsgResults = new ArrayList<>();

        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry: offsetsMap.entrySet()) {

            if (null != entry.getValue()) {
                consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
                            @Override
                            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

                            }

                            @Override
                            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                                consumer.seek(entry.getKey(), entry.getValue().offset());
                            }
                        });

                while (true) {
                    boolean nextPartition = false;
                    ConsumerRecords<String, String> msgs = consumer.poll(100);
                    if (msgs.count() == 0) {
                        break;
                    }
                    for (ConsumerRecord<String, String> msg : msgs) {
//                        System.out.println("Key: " + msg.key() + " Value: " + msg.value() +
//                                " Offset: " + msg.offset() + " Partitions: " + msg.partition());

                        if (partitionAndEndOffset.containsKey(msg.partition())) {
                            if (msg.offset() >= partitionAndEndOffset.get(msg.partition())) {
                                log.info("该partition查询到达时间区间最大值offset，查询结束");
                                nextPartition = true;
                                break;
                            }
                        }

                        //字符串包含或匹配符合正则的消息
                        if (StringUtils.isNotEmpty(kafkaSearchDto.getRegex())) {
                            if (msg.value().contains(kafkaSearchDto.getRegex()) ||
                                    msg.value().matches(kafkaSearchDto.getRegex())) {
                                searchMsgResults.add(new KafkaMsgDto(topic, msg.partition(),
                                        msg.offset(), msg.value(), DateUtil.getDateWithMill(msg.timestamp())));
                            }
                        } else {
                            searchMsgResults.add(new KafkaMsgDto(topic, msg.partition(),
                                    msg.offset(), msg.value(), DateUtil.getDateWithMill(msg.timestamp())));
                        }
                    }
                    if (nextPartition) {
                        break;
                    }
                    consumer.commitSync();
                }
            }
        }
        consumer.close();
        return searchMsgResults;
    }

    private Properties buildNormalProperties() {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", env.getProperty("spring.kafka.bootstrap-servers"));
        prop.put("group.id", "searchMessageGroup");
        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return prop;
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        props.put("zookeeper.connect", env.getProperty("spring.kafka.zk", ""));
        props.put("group.id", env.getProperty("spring.kafka.consumer.group-id", ""));
        props.put("auto.offset.reset", "largest");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", env.getProperty("spring.kafka.consumer.auto-commit-interval", ""));
        return props;
    }

    class HandldMessageThread implements Runnable {

        private KafkaStream<byte[], byte[]> kafkaStream = null;
        private int num = 0;
        private List<AbstractKafkaHandler> handlers;

        public HandldMessageThread(KafkaStream<byte[], byte[]> kafkaStream, int num, List<AbstractKafkaHandler> handlers) {
            super();
            this.kafkaStream = kafkaStream;
            this.num = num;
            this.handlers = handlers;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
            while (iterator.hasNext()) {
                String message = new String(iterator.next().message());
                handlers.forEach(it -> it.handle(message));
            }
        }

    }


}
