package com.qihui.chaos.service;

import com.qihui.chaos.model.LeaveRequest;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    
    private final ZeebeClient zeebeClient;

    public String startLeaveRequest(LeaveRequest request) {
        log.info("开始处理请假申请: {}", request);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", request.getEmployee());
        variables.put("manager", request.getManager());
        variables.put("reason", request.getReason());
        variables.put("days", request.getDays());
        
        log.info("准备启动流程实例，变量: {}", variables);
        
        try {
            ProcessInstanceEvent processInstance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("leaveRequest")
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();
            
            log.info("流程实例启动成功，实例ID: {}", processInstance.getProcessInstanceKey());
            return String.valueOf(processInstance.getProcessInstanceKey());
        } catch (Exception e) {
            log.error("启动流程实例失败", e);
            throw e;
        }
    }

    public void completeTask(String taskId, boolean approved) {
        log.info("完成任务: {}, 审批结果: {}", taskId, approved);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        
        try {
            zeebeClient.newCompleteCommand(Long.parseLong(taskId))
                    .variables(variables)
                    .send()
                    .join();
            log.info("任务完成成功");
        } catch (Exception e) {
            log.error("完成任务失败", e);
            throw e;
        }
    }
} 