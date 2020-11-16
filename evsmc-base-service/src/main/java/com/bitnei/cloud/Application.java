package com.bitnei.cloud;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.cxytiandi.elasticjob.annotation.EnableElasticJob;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenp
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.bitnei"})
@EnableFeignClients
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT12H")
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.bitnei")
@EnableElasticJob
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
