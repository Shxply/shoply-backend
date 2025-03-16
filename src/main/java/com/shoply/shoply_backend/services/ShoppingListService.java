package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public List<ShoppingList> getUserShoppingLists(String userId) {
        return shoppingListRepository.findByUserId(userId);
    }

    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(String id) {
        shoppingListRepository.deleteById(id);
    }
}

