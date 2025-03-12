package com.qihui.chaos.controller.ai;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@RestController
public class AiChatController {
    @Value("${spring.ai.dashscope.api-key}")
    private String key;

    private final ChatClient chatClient;

    private final ImageModel imageModel;


    public AiChatController(ChatClient chatClient, ImageModel imageModel) {
        this.chatClient = chatClient;
        this.imageModel = imageModel;
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
                .withModel("wanx2.0-t2i-turbo")
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
}
