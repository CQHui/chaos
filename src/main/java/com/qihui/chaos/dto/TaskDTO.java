package com.qihui.chaos.dto;

import lombok.Data;
import java.util.Map;

@Data
public class TaskDTO {
    private Long taskId;
    private Long processInstanceKey;
    private String elementId;
    private Map<String, Object> variables;
} 