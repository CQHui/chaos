package com.qihui.chaos.controller;

import com.qihui.chaos.model.LeaveRequest;
import com.qihui.chaos.service.LeaveRequestService;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping("/request")
    public ResponseEntity<?> submitLeaveRequest(@RequestBody LeaveRequest request) {
        log.info("收到请假申请请求: {}", request);
        try {
            String processInstanceId = leaveRequestService.startLeaveRequest(request);
            log.info("请假申请已提交，流程实例ID: {}", processInstanceId);
            return ResponseEntity.ok(processInstanceId);
        } catch (Exception e) {
            log.error("处理请假申请失败", e);
            return ResponseEntity.internalServerError()
                    .body("处理请假申请失败: " + e.getMessage());
        }
    }

    @PostMapping("/task/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable String taskId, @RequestParam boolean approved) {
        log.info("收到完成任务请求: taskId={}, approved={}", taskId, approved);
        try {
            leaveRequestService.completeTask(taskId, approved);
            log.info("任务已完成");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return ResponseEntity.internalServerError()
                    .body("完成任务失败: " + e.getMessage());
        }
    }
} 