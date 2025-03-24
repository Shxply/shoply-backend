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
                .queryParam("key", API_KEY)
                .toUriString();

        List<Store> stores = new ArrayList<>();

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            for (JsonNode place : results) {
                String name = place.path("name").asText();
                double lat = place.path("geometry").path("location").path("lat").asDouble();
                double lng = place.path("geometry").path("location").path("lng").asDouble();

                stores.add(Store.StoreFactory.create(name, lat, lng));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stores;
    }
}


