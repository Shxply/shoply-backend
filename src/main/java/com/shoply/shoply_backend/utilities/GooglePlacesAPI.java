package com.shoply.shoply_backend.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoply.shoply_backend.models.Store;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

public class GooglePlacesAPI {

    private static final String API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String API_KEY = System.getenv("GOOGLE_PLACES_API_KEY");

    private GooglePlacesAPI() {
        // Prevent instantiation
    }

    public static List<Store> fetchNearbyStores(String location, double radius) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("location", location)
                .queryParam("radius", radius)
                .queryParam("type", "grocery_or_supermarket")
                .queryParam("key", API_KEY)
                .toUriString();

        System.out.println("\uD83C\uDF10 Requesting Google Places API:");
        System.out.println("\uD83D\uDD17 URL: " + url);
        System.out.println("\uD83D\uDD11 API_KEY is " + (API_KEY == null ? "❌ MISSING" : "✅ PRESENT"));

        List<Store> stores = new ArrayList<>();

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("Raw JSON Response: " + jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);
            String status = root.path("status").asText();
            System.out.println("\uD83D\uDCCA API Response Status: " + status);

            if (!status.equals("OK")) {
                System.out.println("⚠️ Warning: Google API returned non-OK status.");
                return stores;
            }

            JsonNode results = root.path("results");

            for (JsonNode place : results) {
                String name = place.path("name").asText();
                String businessStatus = place.path("business_status").asText();

                List<String> types = new ArrayList<>();
                for (JsonNode typeNode : place.path("types")) {
                    types.add(typeNode.asText());
                }

                String vicinity = place.path("vicinity").asText();
                Double rating = place.has("rating") ? place.path("rating").asDouble() : null;
                Integer userRatingsTotal = place.has("user_ratings_total") ? place.path("user_ratings_total").asInt() : null;
                Integer priceLevel = place.has("price_level") ? place.path("price_level").asInt() : null;
                String placeId = place.path("place_id").asText();
                Boolean openNow = place.path("opening_hours").has("open_now") ? place.path("opening_hours").path("open_now").asBoolean() : null;

                double lat = place.path("geometry").path("location").path("lat").asDouble();
                double lng = place.path("geometry").path("location").path("lng").asDouble();

                String photoReference = null;
                List<String> photoAttributions = new ArrayList<>();
                if (place.has("photos") && place.path("photos").isArray() && place.path("photos").size() > 0) {
                    JsonNode firstPhoto = place.path("photos").get(0);
                    photoReference = firstPhoto.path("photo_reference").asText();
                    for (JsonNode attribution : firstPhoto.path("html_attributions")) {
                        photoAttributions.add(attribution.asText());
                    }
                }

                System.out.println("\uD83C\uDFEA Found Store: " + name + " (" + lat + ", " + lng + ")");

                stores.add(Store.StoreFactory.create(
                        name,
                        businessStatus,
                        types,
                        vicinity,
                        rating,
                        userRatingsTotal,
                        priceLevel,
                        placeId,
                        photoReference,
                        photoAttributions,
                        lat,
                        lng,
                        openNow
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Exception while calling Google Places API:");
            e.printStackTrace();
        }

        System.out.println("✅ Total Stores Parsed: " + stores.size());
        return stores;
    }

    public static void main(String[] args) {
        String testLocation = "36.698958395999554,-121.8008024250207";
        double radiusInMeters = 25000;

        List<Store> results = fetchNearbyStores(testLocation, radiusInMeters);

        if (results.isEmpty()) {
            System.out.println("❌ No stores returned from Google Places API.");
        } else {
            System.out.println("✅ Retrieved " + results.size() + " store(s):");
            for (Store store : results) {
                System.out.println("- " + store.getName() + " @ " + store.getLocation());
            }
        }
    }
}
