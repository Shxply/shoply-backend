package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

    Store findByName(String name);

    Store findByStoreId(String id);

    @Query("{ 'location': { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: ?2 } } }")
    List<Store> findStoresNear(double longitude, double latitude, double maxDistance);
}


