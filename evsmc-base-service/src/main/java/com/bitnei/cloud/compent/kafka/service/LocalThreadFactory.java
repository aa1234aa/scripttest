package com.bitnei.cloud.compent.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class LocalThreadFactory {

    private final Environment env;

    public static class NameThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NameThreadFactory(String name) {

            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            if (null == name || name.isEmpty()) {
                name = "pool";
            }

            namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    @Bean("kafkaExecutor")
    public ThreadPoolExecutor kafkaExecutor() {
        return new ThreadPoolExecutor(env.getProperty("kafka.excutor.corePoolSize", Integer.class, 10),
                env.getProperty("kafka.excutor.maxPoolSize", Integer.class, 20),
                env.getProperty("kafka.excutor.keepAliveSeconds", Long.class, 30000L),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NameThreadFactory("kafka"));
    }

    @Bean("shardingDataExecutor")
    public ThreadPoolExecutor shardingDataExecutor() {
        return new ThreadPoolExecutor(env.getProperty("kafka.excutor.corePoolSize", Integer.class, 20),
                env.getProperty("kafka.excutor.maxPoolSize", Integer.class, 20),
                env.getProperty("kafka.excutor.keepAliveSeconds", Long.class, 30000L),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NameThreadFactory("sharding"));
    }

    @Bean("instructTaskExecutor")
    public ThreadPoolExecutor instructTaskExecutor() {
        //不限制queue的大小，任务多有可能爆炸，爆炸就爆炸
        return new ThreadPoolExecutor(10, 10, 30000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NameThreadFactory("instruct"));
    }

    @Bean("normalExecutor")
    public ThreadPoolExecutor normalExecutor() {
        //不限制queue的大小，任务多有可能爆炸，爆炸就爆炸
        return new ThreadPoolExecutor(10, 10, 30000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NameThreadFactory("normal"));
    }

    /**
     * 构建任务线程池，单核心线程（手动创建使用的时候请注意一定要记得释放）
     *
     * @param threadName 业务线程名称
     * @return
     */
    public static ScheduledExecutorService buildSingleScheduled(String threadName) {
        return buildMultipleScheduled(threadName, 1);
    }

    /**
     * 构建任务线程池（手动创建使用的时候请注意一定要记得释放）
     *
     * @param threadName 业务线程名称
     * @return
     */
    public static ScheduledExecutorService buildMultipleScheduled(String threadName, Integer corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new NameThreadFactory(threadName));
    }

    @Bean("upgradeResponseScheduleExecutor")
    public ScheduledExecutorService upgradeResponseScheduleExecutor() {
        return buildMultipleScheduled("upgradeResponse", 10);
    }

    @Bean("sendRuleRespScheduleExecutor")
    public ScheduledExecutorService sendRuleRespScheduleExecutor() {
        return buildMultipleScheduled("sendRuleResp", 5);
    }

    @Bean("upgradeBMSAndSearchScheduleExecutor")
    public ScheduledExecutorService upgradeBMSAndSearchScheduleExecutor() {
        return buildMultipleScheduled("upgradeBMSAndSearch", 2);
    }

    @Bean("sendInstructRespScheduleExecutor")
    public ScheduledExecutorService sendInstructRespScheduleExecutor() {
        return buildMultipleScheduled("sendInstructResp", 5);
    }

    @Bean("upgradeDownFileRespScheduleExecutor")
    public ScheduledExecutorService upgradeDownFileRespScheduleExecutor() {
        return buildMultipleScheduled("upgradeDownFileResp", 5);
    }

    @Bean("paramsSearchScheduleExecutor")
    public ScheduledExecutorService paramsSearchScheduleExecutor() {
        return buildMultipleScheduled("paramsSearch", 5);
    }

    @Bean("realTimeDiagnoseScheduleExecutor")
    public ScheduledExecutorService realTimeDiagnoseScheduleExecutor() {
        return buildSingleScheduled("realTimeDiagnose");
    }

    @Bean("smsSendCallbackScheduleExecutor")
    public ScheduledExecutorService smsSendCallbackScheduleExecutor() {
        return buildMultipleScheduled("SMS_SEND_CALLBACK", 5);
    }

    @Bean("vehCheckExecutor")
    public ThreadPoolExecutor vehCheckExecutor() {
        return new ThreadPoolExecutor(10, 20, 30000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NameThreadFactory("vehCheck"));
    }
}
