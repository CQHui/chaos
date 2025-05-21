package com.qihui.chaos.config;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.Topology;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class ZeebeHealthIndicator implements HealthIndicator {

    private final ZeebeClient zeebeClient;

    @Override
    public Health health() {
        try {
            Topology topology = zeebeClient.newTopologyRequest()
                    .send()
                    .join();
            return Health.up()
                    .withDetail("clusterSize", topology.getClusterSize())
                    .withDetail("brokers", topology.getBrokers())
                    .build();
        } catch (Exception e) {
            log.error("Zeebe health check failed", e);
            return Health.down()
                    .withException(e)
                    .build();
        }
    }
} 