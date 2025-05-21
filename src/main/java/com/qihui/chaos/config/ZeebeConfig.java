package com.qihui.chaos.config;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.Topology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
public class ZeebeConfig {

    @Value("${camunda.bpm.zeebe.broker.gateway-address}")
    private String gatewayAddress;

    @Bean
    public ZeebeClient zeebeClient() {
        log.info("Connecting to Zeebe broker at: {}", gatewayAddress);
        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress(gatewayAddress)
                .usePlaintext()
                .defaultRequestTimeout(Duration.ofSeconds(30))
                .keepAlive(Duration.ofSeconds(30))
                .numJobWorkerExecutionThreads(3)
                .defaultJobWorkerMaxJobsActive(32)
                .defaultJobWorkerName("chaos-worker")
                .defaultJobTimeout(Duration.ofMinutes(5))
                .defaultJobPollInterval(Duration.ofSeconds(1))
                .build();

        // 测试连接
        try {
            log.info("Testing connection to Zeebe broker...");
            Topology topology = client.newTopologyRequest()
                    .send()
                    .join();
            log.info("Successfully connected to Zeebe broker. Cluster size: {}", topology.getClusterSize());
        } catch (Exception e) {
            log.error("Failed to connect to Zeebe broker at {}: {}", gatewayAddress, e.getMessage());
            throw new RuntimeException("Failed to connect to Zeebe broker", e);
        }

        // 部署 BPMN 文件
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:processes/*.bpmn");
            
            for (Resource resource : resources) {
                log.info("Deploying BPMN file: {}", resource.getFilename());
                try {
                    DeploymentEvent deployment = client.newDeployResourceCommand()
                            .addResourceFromClasspath("processes/" + resource.getFilename())
                            .send()
                            .join();
                    log.info("Successfully deployed BPMN file: {}", resource.getFilename());
                } catch (Exception e) {
                    log.error("Failed to deploy BPMN file {}: {}", resource.getFilename(), e.getMessage());
                    throw new RuntimeException("Failed to deploy BPMN file: " + resource.getFilename(), e);
                }
            }
        } catch (IOException e) {
            log.error("Failed to find BPMN files", e);
            throw new RuntimeException("Failed to find BPMN files", e);
        }

        return client;
    }
}