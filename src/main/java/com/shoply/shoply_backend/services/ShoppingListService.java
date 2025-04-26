package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.BarcodeScan;
import com.shoply.shoply_backend.models.ShoppingList;
import com.shoply.shoply_backend.models.ShoppingListItem;
import com.shoply.shoply_backend.repositories.ProductRepository;
import com.shoply.shoply_backend.repositories.ShoppingListItemRepository;
import com.shoply.shoply_backend.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final BarcodeScanService barcodeScanService;
    private final ProductRepository productRepository;

    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            ShoppingListItemRepository shoppingListItemRepository,
            BarcodeScanService barcodeScanService,
            ProductRepository productRepository
    ) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.barcodeScanService = barcodeScanService;
        this.productRepository = productRepository;
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

    public List<ShoppingListItem> getItemsForShoppingList(String shoppingListId) {
        return shoppingListItemRepository.findByShoppingListId(shoppingListId);
    }

    public Map<String, List<ShoppingListItem>> getOptimizedShoppingListGroupedByStore(String shoppingListId) {
        List<ShoppingListItem> items = getItemsForShoppingList(shoppingListId);
        Map<String, List<ShoppingListItem>> groupedByStore = new HashMap<>();

        for (ShoppingListItem item : items) {
            List<BarcodeScan> scans = barcodeScanService.getScansByProductId(item.getProductId());
            if (scans.isEmpty()) {
                continue; // Skip if no scan data available
            }

            BarcodeScan lowestScan = scans.stream()
                    .min(Comparator.comparingDouble(BarcodeScan::getScannedPrice))
                    .orElse(null);

            if (lowestScan != null) {
                String storeId = lowestScan.getStoreId();
                item.setPreferredStoreId(storeId);

                productRepository.findById(item.getProductId()).ifPresent(product -> {
                    product.setPrice(lowestScan.getScannedPrice());
                    item.setProduct(product);
                });

                groupedByStore.computeIfAbsent(storeId, k -> new ArrayList<>()).add(item);
            }
        }

        return groupedByStore;
    }


    public List<List<ShoppingListItem>> getOptimizedShoppingListAsListOfLists(String shoppingListId) {
        Map<String, List<ShoppingListItem>> grouped = getOptimizedShoppingListGroupedByStore(shoppingListId);
        return new ArrayList<>(grouped.values());
    }
}



