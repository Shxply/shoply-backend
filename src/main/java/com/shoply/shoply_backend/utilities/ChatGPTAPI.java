package com.shoply.shoply_backend.utilities;

import com.shoply.shoply_backend.models.Product;
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

    public String compareProducts(Product product1, Product product2) {
        String systemPrompt = "You are a helpful assistant that compares products based on their details and provides insights.";
        String userPrompt = generateComparisonPrompt(product1, product2);
        return makeChatGPTRequest(systemPrompt, userPrompt);
    }

    private String generateComparisonPrompt(Product product1, Product product2) {
        return String.format(
                "Compare the following two products based on their brand, category, and other details:\n" +
                        "Product 1: \nName: %s\nBrand: %s\nCategory: %s\nBarcode: %s\n" +
                        "Product 2: \nName: %s\nBrand: %s\nCategory: %s\nBarcode: %s\n" +
                        "Provide a detailed comparison, highlighting similarities and differences.",
                product1.getName(), product1.getBrand(), product1.getCategory(), product1.getBarcode(),
                product2.getName(), product2.getBrand(), product2.getCategory(), product2.getBarcode()
        );
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
