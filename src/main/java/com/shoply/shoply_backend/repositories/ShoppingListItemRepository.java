package com.shoply.shoply_backend.repositories;

import com.shoply.shoply_backend.models.ShoppingListItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends MongoRepository<ShoppingListItem, String> {

    List<ShoppingListItem> findByShoppingListId(String shoppingListId);

    Optional<ShoppingListItem> findByShoppingListIdAndProductId(String shoppingListId, String productId);
}


