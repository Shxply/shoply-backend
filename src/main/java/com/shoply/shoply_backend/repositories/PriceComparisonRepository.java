package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.PriceComparison;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceComparisonRepository extends MongoRepository<PriceComparison, String> {
    List<PriceComparison> findByProductId(String productId);
    List<PriceComparison> findByStoreId(String storeId);
}

