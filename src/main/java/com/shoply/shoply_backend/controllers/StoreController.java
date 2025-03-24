package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.models.Store;
import com.shoply.shoply_backend.services.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<Store> getStoresNearUser200M(@RequestParam double latitude, @RequestParam double longitude) {
        return storeService.getStoresNearUser200M(latitude, longitude);
    }

    @PostMapping
    public void getStoresNearUser25KM(@RequestParam double latitude, @RequestParam double longitude) {
        storeService.getStoresNearUser25KM(latitude, longitude);
    }

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable String id) {
        return storeService.getStoreById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable String id) {
        storeService.deleteStore(id);
    }
}

