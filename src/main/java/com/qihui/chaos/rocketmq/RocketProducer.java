package com.qihui.chaos.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Elliott Chen on 2023/11/17 15:48
 */

@Component
public class RocketProducer {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void sendMessage(String message) {
        rocketMQTemplate.convertAndSend("test-topic", "this is a test message");
    }
}
