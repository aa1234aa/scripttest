package com.bitnei.cloud.common.config;

import com.bitnei.cloud.common.util.RedisKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("moduleConfiguration")
public class ModuleConfiguration {

    /**
     * WEB本地缓存
     *
     * @param host
     * @param port
     * @param password
     * @param index
     * @return
     */
    @Bean(name = "ctoRedisKit")
    public RedisKit redisKit(
            @Value("${app.cto.host}") String host,
            @Value("${app.cto.port:6379}")int port,
            @Value("${app.cto.password:}") String password,
            @Value("${app.cto.db.default:1}") int index){
        return new RedisKit(host, port, password, index);
    }
}
