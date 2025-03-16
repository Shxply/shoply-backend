package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(String id) {
        return storeRepository.findById(id);
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public void deleteStore(String id) {
        storeRepository.deleteById(id);
    }
}

