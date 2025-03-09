package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "barcode_scans")
public class BarcodeScan {

    @Id
    private String scanId;
    private String userId;
    private String storeId;
    private String productId;
    private double scannedPrice;
    private Instant scanTimestamp;

    public BarcodeScan() {
    }

    public BarcodeScan(String scanId, String userId, String storeId, String productId, double scannedPrice, Instant scanTimestamp) {
        this.scanId = scanId;
        this.userId = userId;
        this.storeId = storeId;
        this.productId = productId;
        this.scannedPrice = scannedPrice;
        this.scanTimestamp = scanTimestamp;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getScannedPrice() {
        return scannedPrice;
    }

    public void setScannedPrice(double scannedPrice) {
        this.scannedPrice = scannedPrice;
    }

    public Instant getScanTimestamp() {
        return scanTimestamp;
    }

    public void setScanTimestamp(Instant scanTimestamp) {
        this.scanTimestamp = scanTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarcodeScan that)) return false;

        return Objects.equals(getScanId(), that.getScanId())
                && Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getStoreId(), that.getStoreId())
                && Objects.equals(getProductId(), that.getProductId())
                && Double.compare(getScannedPrice(), that.getScannedPrice()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScanId(), getUserId(), getStoreId(), getProductId(), getScannedPrice());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BarcodeScan{")
                .append("scanId='").append(scanId).append('\'')
                .append(", userId='").append(userId).append('\'')
                .append(", storeId='").append(storeId).append('\'')
                .append(", productId='").append(productId).append('\'')
                .append(", scannedPrice=").append(scannedPrice)
                .append(", scanTimestamp=").append(scanTimestamp)
                .append('}');
        return sb.toString();
    }

    // Factory Pattern
    public static class BarcodeScanFactory {
        public static BarcodeScan create(String userId, String storeId, String productId, double scannedPrice) {
            return new BarcodeScan(null, userId, storeId, productId, scannedPrice, Instant.now());
        }
    }
}
