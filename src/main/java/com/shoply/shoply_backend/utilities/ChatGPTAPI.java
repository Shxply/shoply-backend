package com.shoply.shoply_backend.utilities;

import com.shoply.shoply_backend.models.Product;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatGPTAPI {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private ChatGPTAPI() {
        // Prevent instantiation
    }

    public static String compareProducts(Product product1, Product product2) {
        String apiKey = System.getenv("CHAT_GPT_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "Error: Missing API key.";
        }

        String systemPrompt = "You are a helpful assistant that compares products based on their details and provides insights.";
        String userPrompt = generateComparisonPrompt(product1, product2);

        return makeChatGPTRequest(apiKey, systemPrompt, userPrompt);
    }

    private static String generateComparisonPrompt(Product product1, Product product2) {
        return String.format(
                "Compare the following two products based on their brand, category, and other details:\n" +
                        "Product 1:\nName: %s\nBrand: %s\nCategory: %s\nBarcode: %s\n\n" +
                        "Product 2:\nName: %s\nBrand: %s\nCategory: %s\nBarcode: %s\n\n" +
                        "Provide a detailed comparison, highlighting similarities and differences.",
                product1.getName(), product1.getBrand(), product1.getCategory(), product1.getBarcode(),
                product2.getName(), product2.getBrand(), product2.getCategory(), product2.getBarcode()
        );
    }

    private static String makeChatGPTRequest(String apiKey, String systemPrompt, String userPrompt) {
        RestTemplate restTemplate = new RestTemplate();

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

        try {
            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, Map.class);
            return extractResponse(response);
        } catch (Exception e) {
            return "Error: Failed to contact ChatGPT API - " + e.getMessage();
        }
    }

    private static String extractResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return "Error: Unexpected API response.";
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices == null || choices.isEmpty()) {
            return "Error: No response from ChatGPT.";
        }

        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message != null ? ((String) message.get("content")).trim() : "Error: Empty response.";
    }
}


