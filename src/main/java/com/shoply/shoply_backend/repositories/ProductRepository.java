package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByBarcode(String barcode);
}

