package com.qihui.chaos.model;

import lombok.Data;

@Data
public class LeaveRequest {
    private String employee;
    private String manager;
    private String reason;
    private Integer days;
    private String status;
} 