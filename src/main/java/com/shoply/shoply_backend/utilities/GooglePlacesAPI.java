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
                .queryParam("type", "store")
                .queryParam("key", API_KEY)
                .toUriString();

        System.out.println("🌐 Requesting Google Places API:");
        System.out.println("🔗 URL: " + url);
        System.out.println("🔑 API_KEY is " + (API_KEY == null ? "❌ MISSING" : "✅ PRESENT"));

        List<Store> stores = new ArrayList<>();

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("📥 Raw JSON Response:\n" + jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);
            String status = root.path("status").asText();
            System.out.println("📊 API Response Status: " + status);

            if (!status.equals("OK")) {
                System.out.println("⚠️ Warning: Google API returned non-OK status.");
                return stores;
            }

            JsonNode results = root.path("results");

            for (JsonNode place : results) {
                String name = place.path("name").asText();
                double lat = place.path("geometry").path("location").path("lat").asDouble();
                double lng = place.path("geometry").path("location").path("lng").asDouble();

                System.out.println("🏪 Found Store: " + name + " (" + lat + ", " + lng + ")");
                stores.add(Store.StoreFactory.create(name, lat, lng));
            }

        } catch (Exception e) {
            System.out.println("❌ Exception while calling Google Places API:");
            e.printStackTrace();
        }

        System.out.println("✅ Total Stores Parsed: " + stores.size());
        return stores;
    }

    public static void main(String[] args) {
        // Use the coordinates from earlier
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



