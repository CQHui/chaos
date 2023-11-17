package com.qihui.chaos.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * Created by Elliott Chen on 2023/11/17 15:53
 */


@Service
@RocketMQMessageListener(
        topic = "test-topic",
        consumerGroup = "${rocketmq.producer.group:}"
)
public class RocketConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("receive a message, message = "+ s);
    }
}
