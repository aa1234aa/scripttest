package com.bitnei.framework.kafka.config;

import com.bitnei.framework.kafka.sender.SenderTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author zhaogd
 * @date 2020/4/10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sender")
public class SenderConfig {

    /**
     * vin文件的路径,两个vin之间使用换行符分割
     */
    private String vinFilePath;
    /**
     * 发送频率
     */
    private int frequency = 10;
    /**
     * 每辆车发送数据条数
     */
    private int packetCount = Integer.MAX_VALUE;
    private String topic;
    private SenderTypeEnum type;

}
