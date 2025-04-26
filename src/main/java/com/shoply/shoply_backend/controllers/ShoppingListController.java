package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.dto.ShoppingListItemRequest;
import com.shoply.shoply_backend.dto.ShoppingListRequest;
import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.models.ShoppingListItem;
import com.shoply.shoply_backend.services.ShoppingListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ShoppingList createShoppingList(@RequestBody ShoppingListRequest request) {
        return shoppingListService.createShoppingList(request.getName(), request.getUserId());
    }

    @PostMapping("/{shoppingListId}/items")
    public ShoppingListItem addItemToShoppingList(@PathVariable String shoppingListId, @RequestBody ShoppingListItemRequest itemRequest) {
        return shoppingListService.addItemToShoppingList(shoppingListId, itemRequest.getProductId(), itemRequest.getQuantity(), itemRequest.getPreferredStoreId());
    }

    @GetMapping("/{shoppingListId}/items")
    public List<ShoppingListItem> getItemsForShoppingList(@PathVariable String shoppingListId) {
        return shoppingListService.getItemsForShoppingList(shoppingListId);
    }

    @DeleteMapping("/{id}")
    public void deleteShoppingList(@PathVariable String id) {
        shoppingListService.deleteShoppingList(id);
    }

    @GetMapping("/{shoppingListId}/optimized-items")
    public Map<String, List<ShoppingListItem>> getOptimizedShoppingListGroupedByStore(@PathVariable String shoppingListId) {
        return shoppingListService.getOptimizedShoppingListGroupedByStore(shoppingListId);
    }
}


