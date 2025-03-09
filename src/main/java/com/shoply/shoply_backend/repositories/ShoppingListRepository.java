package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends MongoRepository<ShoppingList, String> {
    List<ShoppingList> findByUserId(String userId);
    List<ShoppingList> findByGeneratedByAI(boolean generatedByAI);
}

