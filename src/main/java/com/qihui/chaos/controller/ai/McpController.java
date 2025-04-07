package com.qihui.chaos.controller.ai;

import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

//@RestController
public class McpController {
    private final ChatClient chatClient;
    private final List<McpAsyncClient> mcpAsyncClients;

    public McpController(ChatClient chatClient, List<McpAsyncClient> mcpAsyncClients) {
        this.chatClient = chatClient;
        this.mcpAsyncClients = mcpAsyncClients;
    }

    @GetMapping("/mcp")
    public Flux<String> mcp(@RequestParam(value = "chatId") String chatId,
                            @RequestParam(value = "message", defaultValue = "今天天气怎么样?") String message) {
        List<ToolCallback> toolCallbackProvider = McpToolUtils.getToolCallbacksFromAsyncClinents(mcpAsyncClients);

        return chatClient.prompt().user(message)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .tools(toolCallbackProvider)
                .stream().content();
    }
}
