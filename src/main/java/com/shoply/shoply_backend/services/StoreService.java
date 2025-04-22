package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.repositories.StoreRepository;
import com.shoply.shoply_backend.utilities.GooglePlacesAPI;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreService {

    private StoreRepository storeRepository;

    private MongoTemplate mongoTemplate;

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
                    existing.getLocation().getX() == store.getLocation().getX() &&
                            existing.getLocation().getY() == store.getLocation().getY()
            );

            if (!alreadyExists) {
                createStore(store);
            }
        }
    }



    public List<Store> getStoresNearUser200M (double latitude, double longitude) {
        double radius = 0.2;
        Point userLocation = new Point(longitude, latitude);
        Criteria geoCriteria = Criteria.where("location").nearSphere(userLocation).maxDistance(radius / 6371.0);
        Query query = new Query(geoCriteria);
        return mongoTemplate.find(query, Store.class);
    }
}



