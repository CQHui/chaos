package com.qihui.chaos.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.RequestResponseAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;

import java.util.Map;


@Slf4j
public class LoggingAdvisor implements RequestResponseAdvisor {

    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        log.info("Request: " + request);
        return request;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
