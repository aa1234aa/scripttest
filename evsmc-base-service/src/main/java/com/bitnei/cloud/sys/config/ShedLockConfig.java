package com.bitnei.cloud.sys.config;

/**
 * @author bwwjscd
 */

import jodd.util.StringUtil;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import net.javacrumbs.shedlock.provider.redis.jedis.JedisLockProvider;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableScheduling

public class ShedLockConfig {

    @Value("${app.redis.host}")
    private String hostName;
    @Value("${app.redis.password}")
    private String password;
    @Value("${app.redis.port}")
    private int port;


    @Bean
    public LockProvider lockProvider() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(500);
        config.setMaxWaitMillis(5000L);
        config.setTestOnBorrow(true);
        JedisPool jedisPool;
        if (StringUtil.isEmpty(password)) {
            jedisPool = new JedisPool(config, hostName, port);
        } else {
            jedisPool = new JedisPool(config, hostName, port, 10000, password);
        }
        return new JedisLockProvider(jedisPool);
    }


}
