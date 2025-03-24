package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.annotations.ExpectedResult;
import com.shoply.shoply_backend.annotations.IntegrationTest;
import com.shoply.shoply_backend.annotations.MockDependency;
import com.shoply.shoply_backend.annotations.TestableService;
import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@TestableService
@Service
public class ShoppingListService {

    @MockDependency
    private ShoppingListRepository shoppingListRepository;

    public ShoppingListService() {
    }

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"user1\"", expectedJson = "[{\"id\":\"1\",\"userId\":\"user1\",\"items\":[\"Milk\", \"Eggs\"]}]")
    @ExpectedResult(inputJson = "\"user2\"", expectedJson = "[]")
    public List<ShoppingList> getUserShoppingLists(String userId) {
        return shoppingListRepository.findByUserId(userId);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "{\"userId\":\"user1\",\"items\":[\"Milk\", \"Eggs\"]}", expectedJson = "{\"id\":\"1\",\"userId\":\"user1\",\"items\":[\"Milk\", \"Eggs\"]}")
    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "__VOID__")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__VOID__")
    public void deleteShoppingList(String id) {
        shoppingListRepository.deleteById(id);
    }
}


