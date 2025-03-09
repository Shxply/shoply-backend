package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "price_comparisons")
public class PriceComparison {

    @Id
    private String comparisonId;
    private String productId;
    private String storeId;
    private double price;
    private Instant dateRecorded;

    public PriceComparison() {
    }

    public PriceComparison(String comparisonId, String productId, String storeId, double price, Instant dateRecorded) {
        this.comparisonId = comparisonId;
        this.productId = productId;
        this.storeId = storeId;
        this.price = price;
        this.dateRecorded = (dateRecorded != null) ? dateRecorded : Instant.now();
    }

    // Getters and Setters
    public String getComparisonId() {
        return comparisonId;
    }

    public void setComparisonId(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Instant dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceComparison that = (PriceComparison) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(comparisonId, that.comparisonId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(storeId, that.storeId) &&
                Objects.equals(dateRecorded, that.dateRecorded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparisonId, productId, storeId, price, dateRecorded);
    }

    @Override
    public String toString() {
        return new StringBuilder("PriceComparison{")
                .append("comparisonId='").append(comparisonId).append('\'')
                .append(", productId='").append(productId).append('\'')
                .append(", storeId='").append(storeId).append('\'')
                .append(", price=").append(price)
                .append(", dateRecorded=").append(dateRecorded)
                .append('}').toString();
    }

    // Factory Pattern
    public static class PriceComparisonFactory {
        public static PriceComparison create(String productId, String storeId, double price) {
            return new PriceComparison(null, productId, storeId, price, Instant.now());
        }
    }
}


