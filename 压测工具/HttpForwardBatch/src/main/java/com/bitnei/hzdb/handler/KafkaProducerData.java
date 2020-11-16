package com.bitnei.hzdb.handler;

import com.bitnei.hzdb.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
@Order(value = 3)
public class KafkaProducerData implements  CommandLineRunner {


    /**
     * 车辆VIN可配置
     * 发送频率可配置
     * topic可配置
     * 发送数据模板可配置
     */

    // 车辆信息配置目录
    @Value("${spring.vin.path}")
    private String vinPath;

    @Value("${spring.send.frequency}")
    private int sendFrequency;

    private String topic= "us_general_17691";

    @Value("${spring.msg.format}")
    private String message;


    @Autowired
    private KafkaSender kafkaSender;

    final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10, new DefaultManagedAwareThreadFactory());

    final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(sendFrequency);


    private final AtomicLong count = new AtomicLong();


    @PostConstruct
    void init() throws IOException {

        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        scheduledThreadPool.scheduleAtFixedRate(() -> {
                    log.info("10秒发送计数:{}", count.get());
                    count.set(0);
                },
                2, 10000, TimeUnit.MILLISECONDS);

    }


    @Override
    public void run(String... args) throws Exception {

        sendProducerKafka();
    }



    private void sendProducerKafka() throws IOException {

        FileReader fileReader = new FileReader(new File(vinPath));
        final List<String> vins = IOUtils.readLines(fileReader);
        log.info("读取配置VIN数量 [{}]", vins.size());
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for(String vin : vins) {
                    log.info("vin======{}", vin);
                    threadPoolExecutor.submit(new Runnable() {
                        @Override
                        public void run() {
                            String msg1 = "{\"MESSAGETYPE\":\"REALTIME\",\"TYPE\":2,\"VIN\":\""+vin+"\",\"VID\":\"efb0f89f79354082956f76528037c1d0\",\"VTYPE\":\"4510ffdb254446c8a222e238d6ba282d\",\"CTYPE\":\"1_3_1\",\"2000\":\""+ DateUtil.getKafkaDataSyncTime() +"\",\"2201\":\"0.000\",\"2202\":\"0.0\",\"2501\":\"0\",\"2502\":\"113.995628\",\"2503\":\"22.592377\",\"60010\":\"488\",\"60026\":\"0.0\",\"60027\":\"-125\",\"60028\":\"-125\",\"60029\":\"0.000\",\"60030\":\"0.00\",\"60031\":\"\",\"60032\":\"\",\"60033\":\"\",\"60034\":\"0.00\",\"60035\":\"\",\"60036\":\"\",\"60037\":\"0.0\",\"60038\":\"-40\",\"60039\":\"0.0\",\"60043\":\"0\",\"60044\":\"0.0\",\"60045\":\"0.0\",\"60046\":\"-40\",\"60047\":\"0.00\",\"60048\":\"0\",\"60049\":\"-273.00000\",\"60050\":\"0.0\",\"60051\":\"0.00\",\"60052\":\"0.00000000\",\"SUBTYPE\":\"2\",\"TIME\":\""+ DateUtil.getKafkaDataSyncTime()+"\",\"RECVTIME\":\""+DateUtil.getKafkaDataSyncTime()+"\"}";
                            kafkaSender.send(topic, msg1);
                            count.incrementAndGet();
                        }
                    });
                }
            }
        }, 30, sendFrequency, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        for(int i = 0;i < 10000; i++) {
            String val = new Random().nextInt(9999)+"";
            System.out.println(getAllFlowNumber(val));
        }
    }


    private static String getAllFlowNumber(String flowNumber) {
        int len = 5 - flowNumber.length();
        for (int i = 0; i < len; i++) {
            flowNumber = "0" + flowNumber;
        }
        return "BITNEITESTDB" + flowNumber;
    }


}
