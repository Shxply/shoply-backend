package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.models.PriceComparison;
import com.shoply.shoply_backend.services.PriceComparisonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/price-comparisons")
public class PriceComparisonController {
    private final PriceComparisonService priceComparisonService;

    public PriceComparisonController(PriceComparisonService priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }

    @GetMapping
    public List<PriceComparison> getAllComparisons() {
        return priceComparisonService.getAllComparisons();
    }

    @GetMapping("/{id}")
    public Optional<PriceComparison> getComparisonById(@PathVariable String id) {
        return priceComparisonService.getComparisonById(id);
    }

    @GetMapping("/product/{productId}")
    public List<PriceComparison> getComparisonsByProductId(@PathVariable String productId) {
        return priceComparisonService.getComparisonsByProductId(productId);
    }

    @GetMapping("/store/{storeId}")
    public List<PriceComparison> getComparisonsByStoreId(@PathVariable String storeId) {
        return priceComparisonService.getComparisonsByStoreId(storeId);
    }

    @PostMapping
    public PriceComparison createComparison(@RequestBody PriceComparison priceComparison) {
        return priceComparisonService.createComparison(priceComparison);
    }

    @DeleteMapping("/{id}")
    public void deleteComparison(@PathVariable String id) {
        priceComparisonService.deleteComparison(id);
    }
}

