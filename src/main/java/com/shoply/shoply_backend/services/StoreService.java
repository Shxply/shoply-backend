package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.repositories.StoreRepository;
import com.shoply.shoply_backend.utilities.GooglePlacesAPI;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreService {

    private StoreRepository storeRepository;

    private GooglePlacesAPI googlePlacesAPI;

    public StoreService() {
    }

    public StoreService(StoreRepository storeRepository, GooglePlacesAPI googlePlacesAPI) {
        this.storeRepository = storeRepository;
        this.googlePlacesAPI = googlePlacesAPI;
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

    public void getStoresNearUser25KM (double latitude, double longitude) {
        StringBuilder sb = new StringBuilder();
        sb.append(latitude).append(", ").append(longitude);
        List<Store> stores = googlePlacesAPI.fetchNearbyStores(sb.toString(), 25000);
        for(Store store : stores) {
            Store existingStore = storeRepository.findByName(store.getName());
            if(existingStore == null) {
                createStore(store);
            }
        }
    }

    public List<Store> getStoresNearUser200M(double latitude, double longitude) {
        double radius = 0.2;
        return storeRepository.findStoresNear(longitude, latitude, radius);
    }
}



