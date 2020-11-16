package com.bitnei.framework.kafka.sender;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.bitnei.framework.kafka.config.SenderConfig;
import com.bitnei.tools.core.constant.SymbolConstant;
import com.bitnei.tools.core.entity.DateFormatEnum;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Kafka消息发送工具
 *
 * @author zhaogd
 * @date 2020/3/10
 */
@Component
@Slf4j
public class KafkaSend implements CommandLineRunner {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private SenderConfig senderConfig;

    private final AtomicLong count = new AtomicLong();
    private final AtomicLong batch = new AtomicLong();

    @Override
    public void run(String... args) throws Exception {
        send(senderConfig.getVinFilePath(),
                senderConfig.getFrequency(),
                senderConfig.getPacketCount(),
                senderConfig.getTopic(),
                senderConfig.getType());
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void send(String vinFilePath, int frequency, long packetCount, String topic, SenderTypeEnum type) throws IOException {
        final ExecutorService pool = Executors.newFixedThreadPool(20, new DefaultThreadFactory("KafkaPacketSend"));

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("ScheduledSend"));
        final List<String> vins = IOUtils.readLines(new FileReader(vinFilePath));
        log.info("读取配置VIN数量 [{}]", vins.size());
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (String vin : vins) {
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            count.incrementAndGet();
                            String data = "";
                            if (SenderTypeEnum.GEELY.equals(type)) {
                                data = rebuildMessage(vin);
                            } else if (SenderTypeEnum.GB.equals(type)) {
                                data = rebuildGbMessage(vin);
                            }
                            kafkaTemplate.send(topic, vin, data);
                        }
                    });
                }

                final long i = batch.incrementAndGet();
                if (i >= packetCount) {
                    executor.shutdown();
                }
            }
        }, 3, frequency, TimeUnit.SECONDS);


        {
            MetricRegistry metricRegistry = new MetricRegistry();

            metricRegistry.register("总发送批次", new Gauge<Long>() {
                @Override
                public Long getValue() {
                    return batch.longValue();
                }
            });

            metricRegistry.register("总发送数据条数", new Gauge<Long>() {
                @Override
                public Long getValue() {
                    return count.longValue();
                }
            });

            Slf4jReporter consoleReporter = Slf4jReporter.forRegistry(metricRegistry)
                    .outputTo(LoggerFactory.getLogger("com.codahale.metrics"))
                    .build();
            consoleReporter.start(10, TimeUnit.SECONDS);
        }
    }

    private String rebuildGbMessage(String vin) {
        final PacketMessage message = new PacketMessage();
        final String packet = message.getData();

        final byte[] bytes = ByteBufUtil.decodeHexDump(packet);
        final byte[] vinBytes = vin.getBytes(CharsetUtil.UTF_8);
        System.arraycopy(vinBytes, 0, bytes, 4, 17);

        final LocalDateTime time = LocalDateTime.now();
        final byte[] timeBytes = new byte[]{
                (byte) (time.getYear() - 2000),
                (byte) time.getMonthValue(),
                (byte) time.getDayOfMonth(),
                (byte) time.getHour(),
                (byte) time.getMinute(),
                (byte) time.getSecond()
        };
        System.arraycopy(timeBytes, 0, bytes, 24, 6);

        final byte xor = getXor(Arrays.copyOf(bytes, bytes.length - 1));
        bytes[bytes.length - 1] = xor;

        final String format = time.format(DateTimeFormatter.ofPattern(DateFormatEnum.DATE_TIME_NO_SEPARATOR.getFormat()));

        Map<String, Object> map = Maps.newHashMap();
        map.put("VID", "78b19a898a2d49c7a2a0e014bc0a1f82");
        map.put("CTYPE", "2_1_1");
        map.put("1", "2");
        map.put("2", Base64.encode(bytes));
        map.put("3", format);
        map.put("4", "0");
        map.put("5", format);

        StringBuilder builder = new StringBuilder();
        builder.append("SUBMIT")
                .append(SymbolConstant.WHITE_SPACE)
                .append("0")
                .append(SymbolConstant.WHITE_SPACE)
                .append(vin)
                .append(SymbolConstant.WHITE_SPACE)
                .append("PACKET")
                .append(SymbolConstant.WHITE_SPACE)
                .append(JSON.toJSONString(map).replaceAll("\"", ""));
        return builder.toString();
    }


    private String rebuildMessage(String vin) {
        final PacketMessage message = new PacketMessage();
        final String packet = message.getData();

        final byte[] bytes = ByteBufUtil.decodeHexDump(packet);
        final byte[] vinBytes = vin.getBytes(CharsetUtil.UTF_8);
        System.arraycopy(vinBytes, 0, bytes, 4, 17);

        final LocalDateTime time = LocalDateTime.now();
        final byte[] timeBytes = new byte[]{
                (byte) (time.getYear() - 2000),
                (byte) time.getMonthValue(),
                (byte) time.getDayOfMonth(),
                (byte) time.getHour(),
                (byte) time.getMinute(),
                (byte) time.getSecond()
        };
        System.arraycopy(timeBytes, 0, bytes, 24, 6);

        final byte xor = getXor(Arrays.copyOf(bytes, bytes.length - 1));
        bytes[bytes.length - 1] = xor;

        final String format = time.format(DateTimeFormatter.ofPattern(DateFormatEnum.DATE_TIME_NO_SEPARATOR.getFormat()));

        message.setVin(vin);
        message.setData(HexUtil.encodeHexStr(bytes));
        message.setTerminalTime(format);
        message.setPlatformTime(format);
        return JSONUtil.toJsonStr(message);
    }

    public static byte getXor(byte[] bytes) {
        byte temp = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            temp ^= bytes[i];
        }
        return temp;
    }

    @Data
    private static class PacketMessage {
        private String vin = "MMDXX118477110030";
        private int type = 2;
        private String terminalTime = "20200205183155";
        private String platformTime = "20200205183155";
        private String data = "232302fe4d4d4458583131383437373131303033300101aa140205121e270102030103b6000002940b402aee5f010600c70000020101033b4e414fd85f0bb82738030000000000000006646464646464003c01003c01003c0101050006f00572026130bd0601052ee0010c03e801013a010a2e0700000000000000010000000601000000080801010b402aee00780001780cf00cf00cf00cf02ee00cf00cf00cf00cf00cf00cf003e80cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf0090101003c3a3a3a3a3a3a3a3a3a2e3a3a3a3a3a3a3a3a3a3a3a303a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3aa1";
    }
}
