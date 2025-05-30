package com.qihui.chaos.controller.ai;

import com.qihui.chaos.service.ChatService;
import io.modelcontextprotocol.client.McpAsyncClient;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.*;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@RestController
public class AiChatController {

    private final ChatClient chatClient;

    private final ImageModel imageModel;

    private final ChatService chatService;

    public AiChatController(ChatClient chatClient, ImageModel imageModel, ChatService chatService) {
        this.chatClient = chatClient;
        this.imageModel = imageModel;
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "你是谁?") String message) {
        return Map.of("completion", chatClient.prompt().user(message).call().content());
    }

    /**
     * 生成图片 要收费！！！少调用
     * @param input Prompt
     * @param response HttpServletResponse
     */
    @GetMapping("/image")
    public void image(@RequestParam(value = "message", defaultValue = "黑神话悟空") String input, HttpServletResponse response) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .model("wanx2.0-t2i-turbo")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(input, options);
        ImageResponse imageResponse = imageModel.call(imagePrompt);
        String imageUrl = imageResponse.getResult().getOutput().getUrl();

        try {
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();

            response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/rag")
    public Flux<String> chat(
            @RequestParam String message){
        return chatService.chatByVectorStore(message);
    }
}
