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
                "Compare the following two products in detail. Include analysis of nutrition, ingredients, brand, and other product characteristics.\n\n" +

                        "**Product 1**:\n" +
                        "- Name: %s\n" +
                        "- Brand: %s\n" +
                        "- Brand Owner: %s\n" +
                        "- Category: %s\n" +
                        "- Barcode: %s\n" +
                        "- Ingredients: %s\n" +
                        "- NutriScore: %s\n" +
                        "- Energy (kcal): %.2f\n" +
                        "- Salt (g): %.2f\n" +
                        "- Sugar (g): %.2f\n" +
                        "- Ingredient Tags: %s\n\n" +

                        "**Product 2**:\n" +
                        "- Name: %s\n" +
                        "- Brand: %s\n" +
                        "- Brand Owner: %s\n" +
                        "- Category: %s\n" +
                        "- Barcode: %s\n" +
                        "- Ingredients: %s\n" +
                        "- NutriScore: %s\n" +
                        "- Energy (kcal): %.2f\n" +
                        "- Salt (g): %.2f\n" +
                        "- Sugar (g): %.2f\n" +
                        "- Ingredient Tags: %s\n\n" +

                        "Give a clear, structured comparison and state which product may be healthier or better overall.",
                product1.getName(), product1.getBrand(), product1.getBrandOwner(), product1.getCategory(), product1.getBarcode(),
                product1.getIngredients(), product1.getNutriScore(),
                product1.getEnergyKcal() != null ? product1.getEnergyKcal() : 0.0,
                product1.getSalt() != null ? product1.getSalt() : 0.0,
                product1.getSugar() != null ? product1.getSugar() : 0.0,
                product1.getIngredientTags() != null ? String.join(", ", product1.getIngredientTags()) : "N/A",

                product2.getName(), product2.getBrand(), product2.getBrandOwner(), product2.getCategory(), product2.getBarcode(),
                product2.getIngredients(), product2.getNutriScore(),
                product2.getEnergyKcal() != null ? product2.getEnergyKcal() : 0.0,
                product2.getSalt() != null ? product2.getSalt() : 0.0,
                product2.getSugar() != null ? product2.getSugar() : 0.0,
                product2.getIngredientTags() != null ? String.join(", ", product2.getIngredientTags()) : "N/A"
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


