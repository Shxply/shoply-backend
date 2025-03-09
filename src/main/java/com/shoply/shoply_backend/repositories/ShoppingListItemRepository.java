package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.ShoppingListItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListItemRepository extends MongoRepository<ShoppingListItem, String> {
    List<ShoppingListItem> findByShoppingListId(String shoppingListId);
}

