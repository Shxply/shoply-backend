package com.shoply.shoply_backend.dto;

public class ShoppingListItemRequest {
    private String productId;
    private int quantity;
    private String preferredStoreId;

    // Getters and setters
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
}

