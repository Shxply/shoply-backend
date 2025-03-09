package com.shoply.shoply_backend.utilities;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTAPI {

    private final String apiKey;
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatGPTAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiKey = System.getenv("CHAT_GPT_API_KEY");
    }

    public String getChatGPTResponse(String prompt) {
        return makeChatGPTRequest("SYSTEM_PROMPT_PLACEHOLDER", prompt);
    }

    public String getChatGPTCustomResponse(String customPrompt) {
        return makeChatGPTRequest("CUSTOM_PROMPT_PLACEHOLDER", customPrompt);
    }

    private String makeChatGPTRequest(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, Map.class);

        return extractResponse(response);
    }

    private String extractResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "Error: Unexpected API response.";
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices == null || choices.isEmpty()) {
            return "Error: No response from ChatGPT.";
        }

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        return message != null ? ((String) message.get("content")).trim() : "Error: Empty response.";
    }
}

