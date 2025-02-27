package com.qihui.chaos.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Elliott Chen on 2023/11/17 17:03
 */
@Configuration
public class RocketConfig {
    @Value("${rocketmq.producer.group}")
    private String group;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setNamesrvAddr(nameServer);
        defaultMQProducer.setProducerGroup(group);
        rocketMQTemplate.setProducer(defaultMQProducer);
        return rocketMQTemplate;
    }
}
