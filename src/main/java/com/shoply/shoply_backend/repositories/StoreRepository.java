package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

    Store findByStoreId(String id);

    List<Store> findByName(String name);

}


