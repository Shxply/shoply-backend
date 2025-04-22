package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.services.ShoppingListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-lists")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/user/{userId}")
    public List<ShoppingList> getUserShoppingLists(@PathVariable String userId) {
        return shoppingListService.getUserShoppingLists(userId);
    }

    @PostMapping
    public ShoppingList createShoppingList(@RequestBody String name, String userId) {
        return shoppingListService.createShoppingList(name, userId);
    }


    @DeleteMapping("/{id}")
    public void deleteShoppingList(@PathVariable String id) {
        shoppingListService.deleteShoppingList(id);
    }
}

