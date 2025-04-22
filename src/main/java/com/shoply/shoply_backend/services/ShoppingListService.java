package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
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
}


