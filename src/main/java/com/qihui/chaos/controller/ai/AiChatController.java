package com.qihui.chaos.controller.ai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AiChatController {
    @Value("${spring.ai.dashscope.api-key}")
    private String key;

    private final ChatClient chatClient;

    public AiChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "你是谁?") String message) {
        return Map.of("completion", chatClient.prompt().user(message).call().content());
    }
}
