package com.bitnei.hzdb.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Order(value = 1)
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



    @Bean("instructTaskExecutor")
    public ThreadPoolExecutor instructTaskExecutor() {
        //不限制queue的大小，任务多有可能爆炸，爆炸就爆炸
        return new ThreadPoolExecutor(10, 10, 30000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NameThreadFactory("instruct"));
    }

    /**
     * 构建任务线程池，单核心线程
     * @param threadName 业务线程名称
     * @return
     */
    public static ScheduledExecutorService buildSingleScheduled(String threadName) {
        return new ScheduledThreadPoolExecutor(1,
                new NameThreadFactory(threadName));
    }

}
