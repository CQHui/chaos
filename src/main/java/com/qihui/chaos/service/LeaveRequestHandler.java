package com.qihui.chaos.service;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class LeaveRequestHandler {

    @JobWorker(type = "submit-leave-request")
    public void handleSubmitLeaveRequest(final JobClient client, final ActivatedJob job) {
        log.info("处理提交请假申请任务: {}", job.getKey());
        log.info("任务ID为: {}", job.getKey());
        Map<String, Object> variables = job.getVariablesAsMap();
        log.info("请假申请信息: {}", variables);
        
        // 这里可以添加业务逻辑，比如发送通知等
        
        client.newCompleteCommand(job.getKey())
                .variables(variables)
                .send()
                .join();
    }

    @JobWorker(type = "manager-approval")
    public void handleManagerApproval(final JobClient client, final ActivatedJob job) {
        log.info("处理经理审批任务: {}", job.getKey());
        log.info("经理审批任务ID为: {}", job.getKey());
        Map<String, Object> variables = job.getVariablesAsMap();
        log.info("审批信息: {}", variables);
        
        // 这里可以添加业务逻辑，比如发送通知等
        
        client.newCompleteCommand(job.getKey())
                .variables(variables)
                .send()
                .join();
    }
} 