package com.shoply.shoply_backend.dto;

public class BarcodeScanRequest {
    private String userId;
    private String storeId;
    private String productId;
    private double scannedPrice;

    public String getUserId() {
        return userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getProductId() {
        return productId;
    }

    public double getScannedPrice() {
        return scannedPrice;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setScannedPrice(double scannedPrice) {
        this.scannedPrice = scannedPrice;
    }
}

