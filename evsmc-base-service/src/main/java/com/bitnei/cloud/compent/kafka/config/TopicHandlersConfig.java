package com.bitnei.cloud.compent.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
@Component
@ConfigurationProperties(prefix = "kafka")
@Data
public class TopicHandlersConfig {

    private Map<String, String> consumers;
}
