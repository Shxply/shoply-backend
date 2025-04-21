package com.shoply.shoply_backend.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoply.shoply_backend.models.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OpenFoodFactsAPI {

    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v2/product/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Product getProductByBarcodeMapped(String barcode) {
        try {
            String url = BASE_URL + barcode + ".json";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("❌ Failed to fetch product. HTTP Status: " + response.statusCode());
                return null;
            }

            return parseProductFromJson(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Product parseProductFromJson(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode productNode = root.get("product");

            if (productNode == null) return null;

            String barcode = root.path("code").asText(null);
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

            return Product.ProductFactory.create(
                    barcode, name, brand, brandOwner, category,
                    ingredients, nutriScore, energyKcal, salt, sugar,
                    imageUrl, imageFrontUrl, imageIngredientsUrl, imageNutritionUrl,
                    ingredientTags
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String testBarcode = "855469006229";

        Product product = getProductByBarcodeMapped(testBarcode);

        if (product != null) {
            System.out.println("✅ Product fetched successfully:");
            System.out.println("Name: " + product.getName());
            System.out.println("Brand: " + product.getBrand());
            System.out.println("Calories: " + product.getEnergyKcal());
            System.out.println("Sugar: " + product.getSugar());
            System.out.println("Salt: " + product.getSalt());
            System.out.println("NutriScore: " + product.getNutriScore());
            System.out.println("Ingredients: " + product.getIngredients());
        } else {
            System.out.println("❌ Product not found or failed to parse.");
        }
    }
}
