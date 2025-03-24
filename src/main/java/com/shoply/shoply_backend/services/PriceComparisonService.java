package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.PriceComparison;
import com.shoply.shoply_backend.repositories.PriceComparisonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PriceComparisonService {

    private PriceComparisonRepository priceComparisonRepository;

    public PriceComparisonService() {
    }

    public PriceComparisonService(PriceComparisonRepository priceComparisonRepository) {
        this.priceComparisonRepository = priceComparisonRepository;
    }

    public List<PriceComparison> getAllComparisons() {
        return priceComparisonRepository.findAll();
    }

    public Optional<PriceComparison> getComparisonById(String id) {
        return priceComparisonRepository.findById(id);
    }

    public List<PriceComparison> getComparisonsByProductId(String productId) {
        return priceComparisonRepository.findByProductId(productId);
    }

    public List<PriceComparison> getComparisonsByStoreId(String storeId) {
        return priceComparisonRepository.findByStoreId(storeId);
    }

    public PriceComparison createComparison(PriceComparison priceComparison) {
        return priceComparisonRepository.save(priceComparison);
    }

    public void deleteComparison(String id) {
        priceComparisonRepository.deleteById(id);
    }
}


