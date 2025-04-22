package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.models.ShoppingListItem;
import com.shoply.shoply_backend.repositories.ShoppingListRepository;
import com.shoply.shoply_backend.repositories.ShoppingListItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            ShoppingListItemRepository shoppingListItemRepository
    ) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public List<ShoppingList> getUserShoppingLists(String userId) {
        return shoppingListRepository.findByUserId(userId);
    }

    public ShoppingList createShoppingList(String name, String userId) {
        ShoppingList shoppingList = ShoppingList.ShoppingListFactory.createUserGeneratedList(name, userId, true);
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(String id) {
        shoppingListRepository.deleteById(id);
    }

    public ShoppingListItem addItemToShoppingList(String shoppingListId, String productId, int quantity, String preferredStoreId) {
        return shoppingListItemRepository.findByShoppingListIdAndProductId(shoppingListId, productId).map(existingItem -> {
                    int updatedQuantity = existingItem.getQuantity() + quantity;
                    if (updatedQuantity <= 0) {
                        shoppingListItemRepository.deleteById(existingItem.getShoppingListItemId());
                        return null;
                    }
                    existingItem.setQuantity(updatedQuantity);
                    return shoppingListItemRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    if (quantity <= 0) return null;
                    ShoppingListItem newItem = ShoppingListItem.ShoppingListItemFactory.create(
                            shoppingListId, productId, quantity, preferredStoreId
                    );
                    return shoppingListItemRepository.save(newItem);
                });
    }


}



