package com.shoply.shoply_backend.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesAPI {

    private final String apiKey;
    private final RestTemplate restTemplate;
    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    public GooglePlacesAPI(RestTemplate restTemplate, StoreRepository storeRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.storeRepository = storeRepository;
        this.objectMapper = objectMapper;
        this.apiKey = System.getenv("GOOGLE_PLACES_API_KEY");
    }

    public List<Store> searchAndSavePlaces(String query) {
        String url = UriComponentsBuilder.fromUriString("https://maps.googleapis.com/maps/api/place/textsearch/json").queryParam("query", query).queryParam("key", apiKey).toUriString();

        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<Store> stores = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            for (JsonNode place : results) {
                String name = place.path("name").asText();
                double latitude = place.path("geometry").path("location").path("lat").asDouble();
                double longitude = place.path("geometry").path("location").path("lng").asDouble();

                Store store = Store.StoreFactory.create(name, latitude, longitude);
                stores.add(store);
            }
            return storeRepository.saveAll(stores);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }
}
