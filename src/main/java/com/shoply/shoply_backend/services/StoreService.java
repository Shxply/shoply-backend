package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.repositories.StoreRepository;
import com.shoply.shoply_backend.utilities.GooglePlacesAPI;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private static final double COORDINATE_TOLERANCE = 0.0001;

    private final StoreRepository storeRepository;
    private final MongoTemplate mongoTemplate;

    public StoreService(StoreRepository storeRepository, MongoTemplate mongoTemplate) {
        this.storeRepository = storeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Store getStoreById(String storeId) {
        return storeRepository.findByStoreId(storeId);
    }

    public void createStore(Store store) {
        storeRepository.save(store);
    }

    public void deleteStore(String storeId) {
        storeRepository.deleteById(storeId);
    }

    public void getStoresNearUser25KM(double latitude, double longitude) {
        String coordinateString = latitude + "," + longitude;
        List<Store> nearbyStores = GooglePlacesAPI.fetchNearbyStores(coordinateString, 25000);

        for (Store store : nearbyStores) {
            List<Store> existingStoresWithSameName = storeRepository.findByName(store.getName());

            boolean alreadyExists = existingStoresWithSameName.stream().anyMatch(existing ->
                    Math.abs(existing.getLocation().getX() - store.getLocation().getX()) < COORDINATE_TOLERANCE &&
                            Math.abs(existing.getLocation().getY() - store.getLocation().getY()) < COORDINATE_TOLERANCE
            );

            if (!alreadyExists) {
                System.out.println("➕ Inserting store: " + store.getName() + " @ " + store.getLocation());
                createStore(store);
            } else {
                System.out.println("⚠️ Store already exists (fuzzy match): " + store.getName() + " @ " + store.getLocation());
            }
        }
    }

    public List<Store> getStoresNearUser200M(double latitude, double longitude) {
        double radiusKm = 15.0;
        double radiusInMeters = radiusKm * 1000;

        System.out.println("📍 Searching for stores near:");
        System.out.println("   Latitude: " + latitude);
        System.out.println("   Longitude: " + longitude);
        System.out.println("📏 Radius (meters): " + radiusInMeters);

        GeoJsonPoint userLocation = new GeoJsonPoint(longitude, latitude);
        Criteria geoCriteria = Criteria.where("location")
                .nearSphere(userLocation)
                .maxDistance(radiusInMeters); // 👈 Use meters, not radians

        Query query = new Query(geoCriteria);
        System.out.println("🔎 MongoDB Query: " + query);

        List<Store> nearbyStores = mongoTemplate.find(query, Store.class);

        System.out.println("✅ Stores found: " + nearbyStores.size());
        for (Store store : nearbyStores) {
            System.out.println("🏪 " + store.getName() + " @ " + store.getLocation());
        }

        return nearbyStores;
    }
}
