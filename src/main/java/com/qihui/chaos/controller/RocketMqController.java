package com.qihui.chaos.controller;

import com.qihui.chaos.rocketmq.RocketProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Elliott Chen on 2023/11/17 15:56
 */

@RestController
public class RocketMqController {
    private final RocketProducer rocketProducer;

    public RocketMqController(RocketProducer rocketProducer) {
        this.rocketProducer = rocketProducer;
    }

    @GetMapping("/rocket/mq/message")
    public void sendMessage(@RequestParam("message") String message) {
        rocketProducer.sendMessage(message);
    }
}
