package com.shoply.shoply_backend.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoply.shoply_backend.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OpenFoodFactsAPI {

    private static final Logger logger = LoggerFactory.getLogger(OpenFoodFactsAPI.class);
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v2/product/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Product getProductByBarcodeMapped(String barcode) {
        try {
            logger.info("Fetching product with barcode: {}", barcode);
            String url = BASE_URL + barcode + ".json";
            logger.debug("Request URL: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("HTTP Status: {}", response.statusCode());

            if (response.statusCode() != 200) {
                logger.warn("Failed to fetch product. HTTP Status: {}", response.statusCode());
                return null;
            }

            logger.info("Product data fetched successfully. Starting JSON parsing...");
            return parseProductFromJson(response.body(), barcode);  // Pass the original barcode
        } catch (Exception e) {
            logger.error("Exception occurred during product fetch:", e);
            return null;
        }
    }

    private static Product parseProductFromJson(String json, String originalBarcode) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode productNode = root.get("product");

            if (productNode == null) {
                logger.warn("No 'product' node found in JSON.");
                return null;
            }

            logger.debug("Extracting product fields from JSON...");

            String name = productNode.path("product_name").asText(null);
            String brand = productNode.path("brands").asText(null);
            String brandOwner = productNode.path("brand_owner").asText(null);
            String category = productNode.path("categories").asText(null);
            String ingredients = productNode.path("ingredients_text_en").asText(null);
            String nutriScore = productNode.path("nutriscore_grade").asText(null);
            Double energyKcal = productNode.path("nutriments").path("energy-kcal_100g").asDouble(0.0);
            Double salt = productNode.path("nutriments").path("salt_100g").asDouble(0.0);
            Double sugar = productNode.path("nutriments").path("sugars_100g").asDouble(0.0);

            String imageUrl = productNode.path("image_url").asText(null);
            String imageFrontUrl = productNode.path("image_front_url").asText(null);
            String imageIngredientsUrl = productNode.path("image_ingredients_url").asText(null);
            String imageNutritionUrl = productNode.path("image_nutrition_url").asText(null);

            List<String> ingredientTags = productNode.has("ingredients_tags")
                    ? StreamSupport.stream(productNode.get("ingredients_tags").spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.toList())
                    : null;

            logger.info("Product parsed successfully: {}", name);

            // Use the original barcode from frontend instead of what's in the JSON
            return Product.ProductFactory.create(
                    originalBarcode, name, brand, brandOwner, category,
                    ingredients, nutriScore, energyKcal, salt, sugar,
                    imageUrl, imageFrontUrl, imageIngredientsUrl, imageNutritionUrl,
                    ingredientTags
            );

        } catch (Exception e) {
            logger.error("Exception occurred while parsing product JSON:", e);
            return null;
        }
    }
}
