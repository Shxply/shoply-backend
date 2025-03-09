package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "shopping_list_items")
public class ShoppingListItem {

    @Id
    private String shoppingListItemId;
    private String shoppingListId;
    private String productId;
    private int quantity;
    private String preferredStoreId;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String shoppingListItemId, String shoppingListId, String productId, int quantity, String preferredStoreId) {
        this.shoppingListItemId = shoppingListItemId;
        this.shoppingListId = shoppingListId;
        this.productId = productId;
        this.quantity = quantity;
        this.preferredStoreId = preferredStoreId;
    }

    // Getters and setters
    public String getShoppingListItemId() {
        return shoppingListItemId;
    }

    public void setShoppingListItemId(String shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPreferredStoreId() {
        return preferredStoreId;
    }

    public void setPreferredStoreId(String preferredStoreId) {
        this.preferredStoreId = preferredStoreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return quantity == that.quantity &&
                Objects.equals(shoppingListItemId, that.shoppingListItemId) &&
                Objects.equals(shoppingListId, that.shoppingListId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(preferredStoreId, that.preferredStoreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingListItemId, shoppingListId, productId, quantity, preferredStoreId);
    }

    @Override
    public String toString() {
        return new StringBuilder("ShoppingListItem{")
                .append("shoppingListItemId='").append(shoppingListItemId).append('\'')
                .append(", shoppingListId='").append(shoppingListId).append('\'')
                .append(", productId='").append(productId).append('\'')
                .append(", quantity=").append(quantity)
                .append(", preferredStoreId='").append(preferredStoreId).append('\'')
                .append('}')
                .toString();
    }

    // Factory Pattern
    public static class ShoppingListItemFactory {
        public static ShoppingListItem create(String shoppingListId, String productId, int quantity, String preferredStoreId) {
            return new ShoppingListItem(null, shoppingListId, productId, quantity, preferredStoreId);
        }
    }
}


