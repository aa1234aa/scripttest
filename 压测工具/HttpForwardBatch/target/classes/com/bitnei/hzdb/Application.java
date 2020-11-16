package com.bitnei.hzdb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bitnei"})
@EnableScheduling
@EnableKafka
@Slf4j
public class Application {
    public static void main( String[] args ) {

        SpringApplication.run(Application.class, args);
    }
}
