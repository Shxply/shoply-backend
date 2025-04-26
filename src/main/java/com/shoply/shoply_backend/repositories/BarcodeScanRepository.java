package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.BarcodeScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarcodeScanRepository extends MongoRepository<BarcodeScan, String> {
    List<BarcodeScan> findByUserId(String userId);

    Optional<BarcodeScan> findTopByProductIdAndStoreIdOrderByScanTimestampDesc(String productId, String storeId);

    List<BarcodeScan> findByProductId(String productId);
}
